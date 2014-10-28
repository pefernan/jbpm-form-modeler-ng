package org.jbpm.formModeler.ng.editor.backend;

import org.apache.commons.lang.StringUtils;
import org.guvnor.common.services.backend.exceptions.ExceptionUtilities;
import org.guvnor.common.services.shared.file.DeleteService;
import org.guvnor.common.services.shared.file.RenameService;
import org.guvnor.common.services.shared.metadata.MetadataService;
import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.security.shared.api.identity.User;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.DeleteDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.NewDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.EditionContextTO;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.*;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.jbpm.formModeler.ng.model.*;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.RangedDataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.forms.FieldManager;
import org.jbpm.formModeler.ng.services.management.forms.FormDefinitionMarshaller;
import org.jbpm.formModeler.ng.services.management.forms.FormLayoutManager;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.base.options.CommentedOption;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.workbench.events.ResourceOpenedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.List;

@Service
@ApplicationScoped
public class FormEditorServiceImpl implements FormEditorService {

    private Logger log = LoggerFactory.getLogger(FormEditorServiceImpl.class);

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

    @Inject
    private User identity;

    @Inject
    private SessionInfo sessionInfo;

    @Inject
    private RenameService renameService;

    @Inject
    private DeleteService deleteService;

    @Inject
    private MetadataService metadataService;

    @Inject
    private Event<ResourceOpenedEvent> resourceOpenedEvent;

    @Inject
    private Event<RefreshHoldersListEvent> refreshHoldersListEvent;

    @Inject
    private Event<RefreshCanvasEvent> refreshCanvasEvent;

    @Inject
    private Event<EditionContextTO> loadEditionEvent;

    @Inject
    private LocaleManager localeManager;

    @Inject
    private FormManager formManager;

    @Inject
    private FieldManager fieldManager;

    @Inject
    private FormLayoutManager layoutManager;

    @Inject
    private DataHolderManager dataHolderManager;

    @Inject
    private FormDefinitionMarshaller formDefinitionMarshaller;

    @Inject
    private FormRenderContextManager contextManager;

    @Inject
    private FormRenderContextMarshaller contextMarshaller;

    @Override
    public PageResponse<DataHolderTO> listFormDataHolders(PageRequest pageRequest, String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return null;

        final PageResponse<DataHolderTO> response = new PageResponse<DataHolderTO>();
        final List<DataHolderTO> tradeRatePageRowList = new ArrayList<DataHolderTO>();

        int i = 0;
        for (DataHolder holder : context.getForm().getHolders()) {
            if ( i >= pageRequest.getStartRowIndex() + pageRequest.getPageSize() ) {
                break;
            }
            if ( i >= pageRequest.getStartRowIndex() ) {
                tradeRatePageRowList.add(buildDataHolderTo(holder, false, context.getForm()));
            }
            i++;
        }

        response.setPageRowList( tradeRatePageRowList );
        response.setStartRowIndex( pageRequest.getStartRowIndex() );
        response.setTotalRowSize( context.getForm().getHolders().size() );
        response.setTotalRowSizeExact( true );

        return response;
    }

    @Override
    public FormDataHoldersTO getAvailableDataHolders(String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return new FormDataHoldersTO();

        return getAvailabeHoldersList(context.getForm());
    }


    @Override
    public Long getFieldIdFromExpression(String ctxUID, String bindingExpression) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context != null) {
            Field field = BindingUtils.getFielForBindingExpression(bindingExpression, context.getForm());
            if (field != null) return field.getId();
        }

        return null;
    }

    @Override
    public FormDataHoldersTO addFieldFromHolder(String ctxUID, DataHolderFieldTO fieldTO) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return new FormDataHoldersTO();

        DataHolder holder = context.getForm().getDataHolderById(fieldTO.getHolderId());

        if (holder != null) {
            DataFieldHolder field = holder.getDataFieldHolderById(fieldTO.getId());
            if (formManager.addDataHolderField(context.getForm(), holder, field)) {
                context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
                refreshCanvasEvent.fire(new RefreshCanvasEvent(ctxUID, contextMarshaller.marshallContext(context)));
            }
        }

        return getAvailabeHoldersList(context.getForm());
    }

    @Override
    public String addFieldFromTypeCode(String ctxUID, String code) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return null;

        if (formManager.addFieldByType(context.getForm(), code)) {
            context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
            return contextMarshaller.marshallContext(context);
        }

        return null;
    }

    protected FormDataHoldersTO getAvailabeHoldersList(Form form) {

        FormDataHoldersTO formDataHoldersTO = new FormDataHoldersTO();

        Comparator<DataHolderTO> holderComparator = new Comparator<DataHolderTO>() {
            @Override
            public int compare(DataHolderTO o1, DataHolderTO o2) {
                return o1.getUniqueId().compareTo(o2.getUniqueId());
            }
        };

        Set<DataHolderTO> complex = new TreeSet<DataHolderTO>(holderComparator);
        Set<DataHolderTO> simple = new TreeSet<DataHolderTO>(holderComparator);

        for (DataHolder holder : form.getHolders()) {
            DataHolderTO holderTO = buildDataHolderTo(holder, true, form);
            if (holderTO.getFields().length > 0) {
                if (holder.canHaveChildren()) complex.add(holderTO);
                else simple.add(holderTO);
            }
        }

        formDataHoldersTO.setComplexHolders(complex.toArray(new DataHolderTO[complex.size()]));
        formDataHoldersTO.setSimpleHolders(simple.toArray(new DataHolderTO[simple.size()]));
        return formDataHoldersTO;
    }

    protected DataHolderTO buildDataHolderTo(DataHolder holder, boolean addChild, Form form) {
        DataHolderTO to = new DataHolderTO();
        to.setType(holder.getTypeCode());
        to.setUniqueId(holder.getUniqueId());
        to.setClassName(holder.getClassName());

        to.setCanHaveChild(holder.canHaveChildren());

        List<DataHolderFieldTO> fields = new ArrayList<DataHolderFieldTO>();
        if (addChild) {
            for (DataFieldHolder field : holder.getFieldHolders()) {
                DataHolderFieldTO fieldTO = new DataHolderFieldTO();
                fieldTO.setHolderId(to.getUniqueId());
                fieldTO.setId(field.getId());
                Field type = fieldManager.getFieldByClass(field.getClassName());
                fieldTO.setTypeCode(type.getCode());

                String bindingExpression = "";
                if (holder.canHaveChildren()) bindingExpression = holder.getUniqueId() + "/";
                bindingExpression += field.getId();
                Field formField = BindingUtils.getFielForBindingExpression(bindingExpression, form);
                if (formField != null) {
                    fieldTO.setBinded(true);
                    fieldTO.setBindedFieldId(formField.getId().toString());
                }
                fields.add(fieldTO);
            }
        }
        to.setFields(fields.toArray(new DataHolderFieldTO[fields.size()]));
        return to;
    }

    @Override
    public FormEditorContextTO loadForm(Path path, String localeName) {
        try {
            org.uberfire.java.nio.file.Path kiePath = Paths.convert(path);

            String serializedForm = ioService.readAllString(kiePath).trim();

            Map<String, Object> serializationcontext = new HashMap<String, Object>();
            serializationcontext.put("path", path);

            FormEditorContextTO result = new FormEditorContextTO();

            ContextConfiguration config = new ContextConfiguration(serializedForm, localeManager.getLocaleById(localeName));
            config.addAttribute("path", path);
            FormRenderContext context = contextManager.newContext(config);

            result.setCtxUID(context.getUID());
            result.setMarshalledContext(context.getMarshalledCopy());

            resourceOpenedEvent.fire(new ResourceOpenedEvent(path, sessionInfo));

            return result;
        } catch (Exception e) {
            log.warn("Error loading form " + path.toURI(), e);
            return null;
        }
    }

    @Override
    public void removeContext(String ctxUID) {
        contextManager.removeContext(ctxUID);
    }

    @Override
    public void changeContextPath(String ctxUID, Path path) {
        if (StringUtils.isEmpty(ctxUID)) return;

        contextManager.getFormRenderContext(ctxUID).getAttributes().put("path", path);
    }

    @Override
    public Path createForm(Path path, String formName) {
        org.uberfire.java.nio.file.Path kiePath = Paths.convert(path).resolve(formName);
        try {
            if (ioService.exists(kiePath)) {
                throw new FileAlreadyExistsException(kiePath.toString());
            }
            Form form = formManager.createForm(formName);

            ioService.write(kiePath, formDefinitionMarshaller.marshall(form), makeCommentedOption(""));

            return Paths.convert(kiePath);
        } catch ( Exception e ) {
            throw ExceptionUtilities.handleException(e);
        }
    }

    @Override
    public void delete(Path path, String comment) {
        deleteService.delete(path, comment);
    }

    @Override
    public Path rename(Path path, String newName, String comment) {
        return renameService.rename(path, newName, comment);
    }

    private CommentedOption makeCommentedOption( final String commitMessage ) {
        final String name = identity.getIdentifier();
        final Date when = new Date();
        final CommentedOption co = new CommentedOption(sessionInfo.getId(),
                name,
                null,
                commitMessage,
                when);
        return co;
    }

    public void createDataHolder(@Observes NewDataHolderEvent event) {
        FormRenderContext context = contextManager.getFormRenderContext(event.getContext());
        if (context != null) {
            DataHolderTO info = event.getDataHolder();
            DataHolderBuildConfig config = new DataHolderBuildConfig(info.getUniqueId(), info.getClassName());
            config.addAttribute("path", context.getAttributes().get("path"));
            config.addAttribute("context", context);
            DataHolder holder = dataHolderManager.createDataHolderByType(info.getType(), config);

            if (holder != null) {
                formManager.addDataHolderToForm(context.getForm(), holder);
            }
            refreshHoldersListEvent.fire(new RefreshHoldersListEvent(event.getContext()));
        }
    }

    @Override
    public String changeFormLayout(String ctxUID, String layoutId) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        String ctxJson = null;
        if (context != null) {
            Layout layout = layoutManager.getLayout(layoutId);

            Form form = context.getForm();

            if (layout.getCode().equals(form.getLayout().getCode())) ctxJson = context.getMarshalledCopy();
            else {
                Layout oldLayout = form.getLayout();

                for (LayoutArea area : oldLayout.getAreas()) {
                    for (Long elementId : area.getElementIds()) {
                        layout.addElement(elementId);
                    }
                }
                form.setLayout(layout);
                context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
                ctxJson = contextManager.marshallContext(context);
            }
        }
        return ctxJson;
    }

    @Override
    public String changeFormLabelPosition(String ctxUID, String labelPosition) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        String ctxJson = null;
        if (context != null) {
            Form form = context.getForm();
            form.setLabelMode(labelPosition);
            context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
            ctxJson = contextManager.marshallContext(context);
        }
        return ctxJson;
    }

    @Override
    public String removeFieldFromForm(String ctxUID, Long fieldId) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        String ctxJson = null;
        if (context != null) {
            context.getForm().deleteField(fieldId);
            context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
            ctxJson = contextManager.marshallContext(context);
        }
        return ctxJson;
    }

    @Override
    public String moveSelectedFieldToFieldPosition(String ctxUID, Long fieldId, int row, int column) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        String ctxJson = null;
        if (context != null) {
            Form form = context.getForm();
            int areas = form.getLayout().getAreas().size();
            form.getLayout().removeElement(fieldId);
            if (areas > form.getLayout().getAreas().size() && row >= form.getLayout().getAreas().size()) row --;
            form.getLayout().addElement(row, column, fieldId);
            context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
            ctxJson = contextManager.marshallContext(context);
        }
        return ctxJson;
    }

    public void deleteDataHolder(@Observes DeleteDataHolderEvent event) {
        FormRenderContext context = contextManager.getFormRenderContext(event.getContext());
        if (context != null) {
            formManager.removeDataHolderFromForm(context.getForm(), event.getDataHolder().getUniqueId());
            refreshHoldersListEvent.fire(new RefreshHoldersListEvent(event.getContext()));
        }
    }

    @Override
    public EditionContextTO startFieldEdition(String ctxUID, Long fieldId, String fieldJson) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        if (context != null) {
            Field field = context.getForm().getFieldById(fieldId);
            if (field != null) {
                Form editionForm = formManager.getFormForFieldEdition(field.getCode());
                if (editionForm != null) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("field", field);
                    data.put("code", field.getCode());
                    ContextConfiguration config = new ContextConfiguration(editionForm, data, context.getCurrentLocale(), fieldJson);
                    FormRenderContext editionContext = contextManager.newContext(config);
                    return new EditionContextTO(editionContext.getUID(), editionContext.getMarshalledCopy());
                }
            }
        }
        return null;
    }

    @Override
    public void editFieldValue(String ctxUID, String editionCtxUID, String editionMarshalledCtx, boolean persist) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        FormRenderContext editionContext = contextManager.getFormRenderContext(editionCtxUID);
        if (context != null && editionContext != null) {
            try {
                if (persist) {
                    contextManager.persistContext(editionContext, editionMarshalledCtx);
                } else {
                    contextManager.removeContext(editionContext);
                }
                context.setFormTemplate(formDefinitionMarshaller.marshall(context.getForm()));
            } catch (Exception ex) {
                log.warn("Unable to persist context {}", editionContext.getUID(), ex);
            }
        }
    }

    @Override
    public EditionContextTO changeFieldType(String editionCtxUID, String fieldName, String code, String rootCtxUID) {
        FormRenderContext editionContext = contextManager.getFormRenderContext(editionCtxUID);
        FormRenderContext rootContext = contextManager.getFormRenderContext(rootCtxUID);

        if (editionContext == null || rootContext == null) return null;

        Field editedField = (Field) editionContext.getInputData().get("field");

        Form rootForm = rootContext.getForm();

        if (editedField == null || rootForm == null) return null;

        if (formManager.changeFieldType(rootForm, editedField, code)) {
            Field newField = rootForm.getFieldById(editedField.getId());
            Form editionForm = formManager.getFormForFieldEdition(newField.getCode());
            if (editionForm != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("field", newField);
                data.put("code", newField.getCode());
                ContextConfiguration config = new ContextConfiguration(editionForm, data, editionContext.getCurrentLocale());
                editionContext = contextManager.newContext(config);
                return new EditionContextTO(editionContext.getUID(), editionContext.getMarshalledCopy());
            }
        }
        return null;
    }

    @Override
    public DataHolderBuilderTO[] getAvailableDataHolderBuilders(String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return new DataHolderBuilderTO[0];

        DataHolderBuilderTO[] response = new DataHolderBuilderTO[dataHolderManager.getHolderBuilders().size()];

        int i = 0;
        for (DataHolderBuilder builder : dataHolderManager.getHolderBuilders()) {
            if (builder instanceof RangedDataHolderBuilder) {
                RangedDataHolderBuilder rangedBuilder = (RangedDataHolderBuilder) builder;
                response[i] = new RangedDataHolderBuilderTO(builder.getId(), builder.getDataHolderName(context.getCurrentLocale()), builder.needsConfig(), rangedBuilder.getHolderSources(ctxUID));
            } else {
                response[i] = new DataHolderBuilderTO(builder.getId(), builder.getDataHolderName(context.getCurrentLocale()), builder.needsConfig());
            }
            i++;
        }
        return response;
    }

    @Override
    public Path save(ObservablePath path, String ctxUID, Metadata metadata, String comment) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        if (context != null) {
            ioService.write(Paths.convert(path), formDefinitionMarshaller.marshall(context.getForm()), metadataService.setUpAttributes(path, metadata), makeCommentedOption(comment));
        }
        return path;
    }
}
