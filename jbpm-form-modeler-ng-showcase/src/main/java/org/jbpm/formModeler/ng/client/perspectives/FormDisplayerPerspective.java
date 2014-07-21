package org.jbpm.formModeler.ng.client.perspectives;

import org.kie.workbench.common.screens.projecteditor.client.menu.ProjectMenu;
import org.kie.workbench.common.widgets.client.handlers.NewResourcePresenter;
import org.kie.workbench.common.widgets.client.handlers.NewResourcesMenu;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchMenu;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.annotations.WorkbenchToolBar;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PanelType;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;
import org.uberfire.workbench.model.menu.Menus;
import org.uberfire.workbench.model.toolbar.IconType;
import org.uberfire.workbench.model.toolbar.ToolBar;
import org.uberfire.workbench.model.toolbar.impl.DefaultToolBar;
import org.uberfire.workbench.model.toolbar.impl.DefaultToolBarItem;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@WorkbenchPerspective(identifier = "FMDisplayPerspective")
public class FormDisplayerPerspective {

    @Inject
    private NewResourcePresenter newResourcePresenter;

    @Inject
    private NewResourcesMenu newResourcesMenu;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private ProjectMenu projectMenu;

    private PerspectiveDefinition perspective;
    private Menus menus;
    private ToolBar toolBar;

    @PostConstruct
    public void init() {
        buildPerspective();
        buildMenuBar();
        buildToolBar();
    }

    @Perspective
    public PerspectiveDefinition getPerspective() {
        return perspective;
    }

    private void buildPerspective() {
        perspective = new PerspectiveDefinitionImpl(PanelType.ROOT_STATIC);
        perspective.setName("FMDisplayPerspective");
        perspective.setTransient(true);

        DefaultPlaceRequest request = new DefaultPlaceRequest("FDPresenter");

        this.perspective.getRoot().addPart( new PartDefinitionImpl( request ) );
    }

    @WorkbenchMenu
    public Menus getMenus() {
        return this.menus;
    }

    @WorkbenchToolBar
    public ToolBar getToolBar() {
        return this.toolBar;
    }

    private void buildMenuBar() {
        this.menus = MenuFactory
                .newTopLevelMenu("Projects")
                .respondsWith( new Command() {
                    @Override
                    public void execute() {
                        placeManager.goTo( "org.kie.guvnor.explorer" );
                    }
                } )
                .endMenu()

                .newTopLevelMenu("New")
                .withItems(newResourcesMenu.getMenuItems())
                .endMenu()

                .newTopLevelMenu("Tools")
                .withItems(projectMenu.getMenuItems())
                .endMenu()

                .newTopLevelMenu("Form Display")
                .withItems(getFormDisplay())
                .endMenu()

                .build();
    }

    private List<? extends MenuItem> getFormDisplay() {
        final List<MenuItem> result = new ArrayList<MenuItem>( 1 );

        result.add( MenuFactory.newSimpleItem("Form Display").respondsWith( new Command() {
            @Override
            public void execute() {
                placeManager.goTo( new DefaultPlaceRequest( "FMDisplayPerspective" ) );
            }
        } ).endMenu().build().getItems().get( 0 ) );

        return result;
    }

    private void buildToolBar() {
        this.toolBar = new DefaultToolBar( "guvnor.new.item" );
        final String tooltip = "New";
        final Command command = new Command() {
            @Override
            public void execute() {
                newResourcePresenter.show();
            }
        };
        toolBar.addItem( new DefaultToolBarItem( IconType.FILE, tooltip, command ) );
    }
}
