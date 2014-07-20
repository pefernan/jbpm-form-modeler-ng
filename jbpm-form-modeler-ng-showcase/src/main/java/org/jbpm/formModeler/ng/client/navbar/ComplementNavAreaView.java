package org.jbpm.formModeler.ng.client.navbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import org.jbpm.formModeler.ng.client.resources.StandaloneResources;
import org.uberfire.client.workbench.widgets.menu.PespectiveContextMenusPresenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * A stand-alone (i.e. devoid of Workbench dependencies) View
 */
public class ComplementNavAreaView
        extends Composite
        implements RequiresResize,
        ComplementNavAreaPresenter.View {

    interface ViewBinder
            extends
            UiBinder<Panel, ComplementNavAreaView> {

    }

    private static ViewBinder uiBinder = GWT.create( ViewBinder.class );

    @UiField(provided = true)
    public Image logo;

    @UiField
    public FlowPanel contextMenuArea;

    @Inject
    private PespectiveContextMenusPresenter contextMenu;

    @PostConstruct
    public void init() {
        logo = new Image( StandaloneResources.INSTANCE.images().logo() );
        initWidget( uiBinder.createAndBindUi( this ) );
        contextMenuArea.add( contextMenu.getView() );
    }

    @Override
    public void onResize() {
        int height = getParent().getOffsetHeight();
        int width = getParent().getOffsetWidth();
//        panel.setPixelSize( width, height );
    }

}
