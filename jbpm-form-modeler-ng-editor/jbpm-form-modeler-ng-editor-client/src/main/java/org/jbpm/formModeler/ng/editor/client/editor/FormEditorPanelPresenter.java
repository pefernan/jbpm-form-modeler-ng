/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.ng.editor.client.editor;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.editor.client.editor.dataHolders.DataHoldersView;
import org.jbpm.formModeler.ng.editor.client.resources.i18n.Constants;
import org.jbpm.formModeler.ng.editor.client.type.FormDefinitionResourceType;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderInfo;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.jbpm.formModeler.ng.editor.type.FormResourceTypeDefinition;
import org.kie.uberfire.client.callbacks.HasBusyIndicatorDefaultErrorCallback;
import org.kie.uberfire.client.common.BusyIndicatorView;
import org.kie.uberfire.client.common.MultiPageEditor;
import org.kie.uberfire.client.common.Page;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilder;
import org.kie.workbench.common.widgets.client.popups.file.*;
import org.kie.workbench.common.widgets.client.popups.validation.DefaultFileNameValidator;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.annotations.WorkbenchEditor;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.lifecycle.OnClose;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnSave;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.menu.Menus;
import org.uberfire.workbench.type.FileNameUtil;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.New;
import javax.inject.Inject;

@Dependent
@WorkbenchEditor(identifier = "FormModelerEditor", supportedTypes = { FormDefinitionResourceType.class })
public class FormEditorPanelPresenter {

    @Inject
    private PlaceManager placeManager;

    private MultiPageEditor editorTabs;

    @Inject
    private BusyIndicatorView busyIndicatorView;

    @Inject
    private Event<NotificationEvent> notification;

    @Inject
    @New
    private FileMenuBuilder menuBuilder;

    @Inject
    private FormResourceTypeDefinition resourceType;

    @Inject
    private DefaultFileNameValidator fileNameValidator;

    @Inject
    Caller<FormEditorService> editorService;

    private ObservablePath path;

    private ObservablePath.OnConcurrentUpdateEvent concurrentUpdateSessionInfo = null;

    private Menus menus;

    protected boolean isReadOnly;

    private String version;

    private PlaceRequest place;

    private FormEditorContextTO ctx;

    private AsyncDataProvider<DataHolderInfo> holdersProvider;

    @Inject
    private DataHoldersView holdersView;

    @OnStartup
    public void onStartup( final ObservablePath path,
                           PlaceRequest placeRequest ) {

        this.place = placeRequest;
        this.path = path;

        this.isReadOnly = place.getParameter( "readOnly", null ) != null;
        this.version = place.getParameter( "version", null );

        editorTabs = new MultiPageEditor(MultiPageEditor.TabPosition.ABOVE);

        holdersView.init(this);

        editorService.call(new RemoteCallback<FormEditorContextTO>() {
            @Override
            public void callback(FormEditorContextTO context) {
                ctx = context;
                editorTabs.addPage(new Page(holdersView, "Data_Holders") {
                    @Override
                    public void onFocus() {
                        initDataHoldersForm();
                        refreshDataHoldersTable();
                    }

                    @Override
                    public void onLostFocus() {
                    }
                });
            }
        }).loadForm(path, LocaleInfo.getCurrentLocale().getLocaleName());
    }

    protected void initDataHoldersForm() {
        editorService.call(new RemoteCallback<DataHolderBuilderTO[]>() {
            @Override
            public void callback(DataHolderBuilderTO[] builders) {
                holdersView.initDataHolderBuilders(builders);
            }
        }).getAvailableDataHolderBuilders(ctx.getCtxUID());
    }

    protected void refreshDataHoldersTable() {
        holdersProvider = new AsyncDataProvider<DataHolderInfo>() {
            protected void onRangeChanged( HasData<DataHolderInfo> display ) {
                final Range range = display.getVisibleRange();
                PageRequest request = new PageRequest( range.getStart(),
                        range.getLength() );

                editorService.call( new RemoteCallback<PageResponse<DataHolderInfo>>() {
                    @Override
                    public void callback( final PageResponse<DataHolderInfo> response ) {
                        updateRowCount(response.getTotalRowSize(), response.isTotalRowSizeExact());
                        updateRowData(response.getStartRowIndex(), response.getPageRowList());
                    }
                } ).listFormDataHolders(request, ctx.getCtxUID());
            }
        };
        holdersProvider.addDataDisplay(holdersView.getDataHoldersGrid());
    }

    @WorkbenchPartTitle
    public String getTitle() {
        String fileName = FileNameUtil.removeExtension(path, resourceType);
        if ( version != null ) {
            fileName = fileName + " v" + version;
        }
        return Constants.INSTANCE.form_modeler_title( fileName );
    }

    @WorkbenchMenu
    public Menus getMenus() {
        if ( menus == null ) {
            makeMenuBar();
        }
        return menus;
    }
    private void makeMenuBar() {

        if ( isReadOnly ) {
            menus = menuBuilder.addRestoreVersion( path ).build();
        } else {
            menus = menuBuilder
                    .addSave( new Command() {
                        @Override
                        public void execute() {
                            onSave();
                        }
                    } )
                    .addRename( new Command() {
                        @Override
                        public void execute() {
                            onRename();
                        }
                    } )
                    .addDelete( new Command() {
                        @Override
                        public void execute() {
                            onDelete();
                        }
                    } )
                    .build();
        }
    }

    private void onRename() {
        final RemoteCallback<Path> renameCallback = new RemoteCallback<Path>() {
            @Override
            public void callback( final Path path ) {
                busyIndicatorView.hideBusyIndicator();
                notification.fire( new NotificationEvent( CommonConstants.INSTANCE.ItemRenamedSuccessfully() ) );
                editorService.call().changeContextPath( ctx.getCtxUID(), path );
            }
        };
        RenamePopup popup = new RenamePopup( path,
                fileNameValidator,
                new CommandWithFileNameAndCommitMessage() {
                    @Override
                    public void execute( final FileNameAndCommitMessage details ) {
                        busyIndicatorView.showBusyIndicator( CommonConstants.INSTANCE.Renaming() );
                        editorService.call( renameCallback,
                                new HasBusyIndicatorDefaultErrorCallback( busyIndicatorView ) ).rename( path,
                                details.getNewFileName(),
                                details.getCommitMessage() );
                    }
                } );

        popup.show();
    }

    @OnSave
    private void onSave() {

    }

    public void save() {
        new SaveOperationService().save( path,
                new CommandWithCommitMessage() {
                    @Override
                    public void execute( final String commitMessage ) {
                       //TODO implement this
                    }
                }
        );
        concurrentUpdateSessionInfo = null;
    }

    protected void onDelete() {
        final DeletePopup popup = new DeletePopup( new CommandWithCommitMessage() {
            @Override
            public void execute( final String comment ) {
                busyIndicatorView.showBusyIndicator( CommonConstants.INSTANCE.Deleting() );
                editorService.call( new RemoteCallback<Void>() {

                    @Override
                    public void callback( final Void response ) {
                        notification.fire( new NotificationEvent( CommonConstants.INSTANCE.ItemDeletedSuccessfully(), NotificationEvent.NotificationType.SUCCESS ) );
                        placeManager.closePlace( place );
                        onClose();
                        busyIndicatorView.hideBusyIndicator();
                    }
                }, new HasBusyIndicatorDefaultErrorCallback( busyIndicatorView ) ).delete( path, comment );
            }
        } );

        popup.show();
    }

    @OnOpen
    public void onOpen() {
        makeMenuBar();

        if ( ctx == null ) {
            return;
        }
    }

    @OnClose
    public void onClose() {
        if ( ctx != null ) {
            editorService.call().removeContext(ctx.getCtxUID());
        }
    }

    @WorkbenchPartView
    public IsWidget getWidget() {
        return editorTabs;
    }

    public FormEditorContextTO getContext() {
        return ctx;
    }

    public void refreshGrid(@Observes RefreshHoldersListEvent refreshHoldersListEvent) {
        if (ctx != null && ctx.getCtxUID().equals(refreshHoldersListEvent.getContext().getCtxUID())) refreshDataHoldersTable();
    }
}
