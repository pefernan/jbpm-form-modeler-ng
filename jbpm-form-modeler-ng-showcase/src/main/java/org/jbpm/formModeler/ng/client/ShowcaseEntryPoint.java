/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jbpm.formModeler.ng.client;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.jboss.errai.bus.client.api.BusErrorCallback;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.jboss.errai.security.shared.service.AuthenticationService;
import org.jbpm.formModeler.ng.client.resources.StandaloneResources;
import org.kie.workbench.common.screens.projecteditor.client.menu.ProjectMenu;
import org.guvnor.common.services.shared.security.KieWorkbenchACL;
import org.guvnor.common.services.shared.security.KieWorkbenchPolicy;
import org.kie.workbench.common.services.shared.security.KieWorkbenchSecurityService;
import org.kie.workbench.common.widgets.client.handlers.NewResourcesMenu;
import org.uberfire.client.mvp.ActivityManager;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.widgets.menu.WorkbenchMenuBarPresenter;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@EntryPoint
public class ShowcaseEntryPoint {

    @Inject
    private SyncBeanManager manager;

    @Inject
    private WorkbenchMenuBarPresenter menubar;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private ActivityManager activityManager;

    @Inject
    private KieWorkbenchACL kieACL;

    @Inject
    private Caller<KieWorkbenchSecurityService> kieSecurityService;

    @Inject
    private Caller<AuthenticationService> authService;

    @Inject
    private NewResourcesMenu newResourcesMenu;

    @Inject
    private ProjectMenu projectMenu;

    @AfterInitialization
    public void startApp() {
        kieSecurityService.call(new RemoteCallback<String>() {
            public void callback(final String str) {
                KieWorkbenchPolicy policy = new KieWorkbenchPolicy(str);
                kieACL.activatePolicy(policy);
                loadStyles();
                setupMenu();
                hideLoadingPopup();
            }
        }).loadPolicy();
    }

    private void logout() {
        authService.call(new RemoteCallback<Void>() {
                             @Override
                             public void callback(Void response) {
                                 redirect(GWT.getHostPageBaseURL() + "login.jsp");
                             }
                         }, new BusErrorCallback() {
                             @Override
                             public boolean error(Message message,
                                                  Throwable throwable) {
                                 Window.alert("Logout failed: " + throwable);
                                 return true;
                             }
                         }
        ).logout();
    }

    private void loadStyles() {
        StandaloneResources.INSTANCE.CSS().ensureInjected();
    }

    private void setupMenu() {

        menubar.addMenus(
                MenuFactory
                        .newTopLevelMenu("Projects")
                        .respondsWith(new Command() {
                            @Override
                            public void execute() {
                                placeManager.goTo("org.kie.guvnor.explorer");
                            }
                        })
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

                        .newTopLevelMenu("Logout").respondsWith(new Command() {
                    @Override
                    public void execute() {
                        logout();
                    }
                }).endMenu().build());
    }


    private List<? extends MenuItem> getFormDisplay() {
        final List<MenuItem> result = new ArrayList<MenuItem>(1);

        result.add(MenuFactory.newSimpleItem("Form Display").respondsWith(new Command() {
            @Override
            public void execute() {
                placeManager.goTo(new DefaultPlaceRequest("FMDisplayPerspective"));
            }
        }).endMenu().build().getItems().get(0));

        result.add(MenuFactory.newSimpleItem("Errai Binding Display").respondsWith(new Command() {
            @Override
            public void execute() {
                placeManager.goTo(new DefaultPlaceRequest("EBPerspective"));
            }
        }).endMenu().build().getItems().get(0));


        return result;
    }

    //Fade out the "Loading application" pop-up
    private void hideLoadingPopup() {
        final Element e = RootPanel.get("loading").getElement();

        new Animation() {

            @Override
            protected void onUpdate(double progress) {
                e.getStyle().setOpacity(1.0 - progress);
            }

            @Override
            protected void onComplete() {
                e.getStyle().setVisibility(Style.Visibility.HIDDEN);
            }
        }.run(500);
    }

    public static native void redirect(String url)/*-{
        $wnd.location = url;
    }-*/;

}
