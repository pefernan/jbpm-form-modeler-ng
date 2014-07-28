package org.jbpm.formModeler.ng.renderer.client.panel;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jbpm.formModeler.ng.renderer.client.rendering.FormDescription;
import org.jbpm.formModeler.ng.renderer.service.FormRendererService;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberView;
import org.uberfire.workbench.events.NotificationEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@Dependent
@WorkbenchScreen(identifier = "FDPresenter")
public class FormDisplayerPresenter {

    public interface FormDisplayerView
            extends
            UberView<FormDisplayerPresenter> {

        void load(FormDescription description);
    }

    private FormDescription description;

    @Inject
    private FormDisplayerView view;

    @Inject
    private Event<NotificationEvent> notification;

    @Inject
    Caller<FormRendererService> includerService;

    private String context;

    @PostConstruct
    public void init() {


    }

    public void submitForm() {
        includerService.call().unMarshallContext(context, new JSONObject(description).toString());
    }

    public void startTest() {
        includerService.call(new RemoteCallback<String>() {
            @Override
            public void callback(String ctx) {
                if (ctx.equals("error")) Window.alert("error!");
                context = ctx;
                includerService.call(new RemoteCallback <String>() {

                    @Override
                    public void callback(String formJSON) {
                        description = JsonUtils.safeEval(formJSON);
                        view.load(description);
                    }
                }).getUnmarshalledContext(context);
            }
        }).initTest(LocaleInfo.getCurrentLocale().getLocaleName());
    }

    public void startEmptyTest() {
        includerService.call(new RemoteCallback<String>() {
            @Override
            public void callback(String ctx) {
                if (ctx.equals("error")) Window.alert("error!");
                context = ctx;
                includerService.call(new RemoteCallback <String>() {

                    @Override
                    public void callback(String formJSON) {
                        description = JsonUtils.safeEval(formJSON);
                        view.load(description);
                    }
                }).getUnmarshalledContext(context);
            }
        }).initEmptyTest(LocaleInfo.getCurrentLocale().getLocaleName());
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Form Displayer";
    }

    @WorkbenchPartView
    public UberView<FormDisplayerPresenter> getView() {
        return view;
    }
}
