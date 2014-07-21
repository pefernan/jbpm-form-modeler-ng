package org.jbpm.formModeler.ng.renderer.backend;

import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.renderer.backend.test.User;
import org.jbpm.formModeler.ng.renderer.service.FormRendererService;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@ApplicationScoped
public class FormRendererServiceImpl implements FormRendererService, Serializable {
    private Logger log = LoggerFactory.getLogger(FormRendererServiceImpl.class);


    @Inject
    private FormRenderContextManager contextManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FormRenderContextMarshaller formRenderContextMarshaller;

    private String json = "{"
            + "     \"id\": \"12345\","
            + "     \"name\": \"test\","
            + "     \"displayMode\": \"default\","
            + "     \"labelMode\": \"before\","
            + "     \"fields\": ["
            + "         {"
            + "             \"id\": \"name\","
            + "             \"label\": \"Enter name:\","
            + "             \"type\": \"InputText\","
            + "             \"position\": 0,"
            + "             \"grouped\": false,"
            + "             \"value\": \"Pere\""
            + "         },"
            + "         {"
            + "             \"id\": \"surname\","
            + "             \"label\": \"Enter surname:\","
            + "             \"type\": \"InputText\","
            + "             \"position\": 1,"
            + "             \"grouped\": true,"
            + "             \"value\": \"Fernandez\""
            + "         },"
            + "         {"
            + "             \"id\": \"birthday\","
            + "             \"label\": \"Enter birthday:\","
            + "             \"type\": \"InputShortDate\","
            + "             \"position\": 2,"
            + "             \"grouped\": false,"
            + "             \"value\": \"" + new Date().getTime() + "\""
            + "         },"
            + "         {"
            + "             \"id\": \"startDate\","
            + "             \"label\": \"Enter start date:\","
            + "             \"type\": \"InputDate\","
            + "             \"position\": 3,"
            + "             \"grouped\": false,"
            + "             \"value\": \"" + new Date().getTime() + "\""
            + "         },"
            + "         {"
            + "             \"id\": \"dbid\","
            + "             \"label\": \"Enter dbid:\","
            + "             \"type\": \"InputTextLong\","
            + "             \"position\": 4,"
            + "             \"grouped\": true,"
            + "             \"value\": \"123456789\""
            + "         },"
            + "         {"
            + "             \"id\": \"married\","
            + "             \"label\": \"Is married\","
            + "             \"type\": \"CheckBox\","
            + "             \"position\": 5,"
            + "             \"grouped\": true,"
            + "             \"value\": \"true\""
            + "         },"
            + "         {"
            + "             \"id\": \"address\","
            + "             \"label\": \"Enter address:\","
            + "             \"type\": \"InputTextArea\","
            + "             \"position\": 6,"
            + "             \"grouped\": false,"
            + "             \"value\": \"Carrer lluna 10 baixos, 08870 Sitges\""
            + "         },"
            + "         {"
            + "             \"id\": \"phone\","
            + "             \"label\": \"Enter phone number:\","
            + "             \"type\": \"InputText\","
            + "             \"position\": 7,"
            + "             \"grouped\": false,"
            + "             \"value\": \"657 075 541\""
            + "         }"
            + "     ]"
            + "}";


    String testJSON;

    @Override
    public String initTest() {
        try {
            User user = new User();
            user.setDbid(System.currentTimeMillis());
            user.setName("Ned");
            user.setSurname("Stark");
            user.setBirthday(new Date(Long.decode("351838800000")));
            user.setStartDate(new Date());
            user.setMarried(Boolean.TRUE);
            user.setAddress("Winterfell");
            user.setPhone("666 66 66 66");
            Form form = formSerializationManager.loadFormFromXML(this.getClass().getResourceAsStream("test/user.form"));
            Map<String, Object> loadData = new HashMap<String, Object>();
            loadData.put("user", user);
            ContextConfiguration configuration = new ContextConfiguration(form, loadData);

            FormRenderContext context = contextManager.newContext(configuration);
            return context.getUID();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return "error";
    }

    @Override
    public String getUnmarshalledContext(String ctxUID) {
        testJSON = formRenderContextMarshaller.marshallContext(contextManager.getFormRenderContext(ctxUID));
        return testJSON;
    }

    @Override
    public void marshallContext(String ctxUID, String ctxJson) {
        log.warn("Original JSON: {}", testJSON);
        log.warn("New JSON: {}", ctxJson);
    }
}
