package org.jbpm.formModeler.ng.editor.backend;

import org.apache.commons.lang.StringUtils;
import org.guvnor.common.services.backend.exceptions.ExceptionUtilities;
import org.guvnor.common.services.shared.file.DeleteService;
import org.guvnor.common.services.shared.file.RenameService;
import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.formModeler.ng.editor.events.canvas.EndFieldEditionEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.LoadFieldEditionContextEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.RefreshCanvasEvent;
import org.jbpm.formModeler.ng.editor.events.canvas.StartEditFieldPropertyEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.DeleteDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.NewDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.RangedDataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.jbpm.formModeler.ng.model.DataFieldHolder;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Field;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.context.FormRenderContextMarshaller;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.RangedDataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
import org.jbpm.formModeler.ng.services.management.forms.utils.BindingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.backend.server.util.Paths;
import org.uberfire.backend.vfs.Path;
import org.uberfire.io.IOService;
import org.uberfire.java.nio.base.options.CommentedOption;
import org.uberfire.java.nio.file.FileAlreadyExistsException;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.Identity;
import org.uberfire.workbench.events.ResourceOpenedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Service
@ApplicationScoped
public class FormEditorServiceImpl implements FormEditorService {

    private Logger log = LoggerFactory.getLogger(FormEditorServiceImpl.class);

    @Inject
    @Named("ioStrategy")
    private IOService ioService;

    @Inject
    private Identity identity;

    @Inject
    private SessionInfo sessionInfo;

    @Inject
    private RenameService renameService;

    @Inject
    private DeleteService deleteService;

    @Inject
    private Event<ResourceOpenedEvent> resourceOpenedEvent;

    @Inject
    private Event<RefreshHoldersListEvent> refreshHoldersListEvent;

    @Inject
    private Event<RefreshCanvasEvent> refreshCanvasEvent;

    @Inject
    private Event<LoadFieldEditionContextEvent> loadEditionEvent;

    @Inject
    private LocaleManager localeManager;

    @Inject
    private FormManager formManager;

    @Inject
    private DataHolderManager dataHolderManager;

    @Inject
    private FormSerializationManager formSerializationManager;

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
                tradeRatePageRowList.add(buildDataHolderTo(holder, false, context.getForm(), false));
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
    public DataHolderTO[] getAvailableDataHolders(String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return new DataHolderTO[0];

        return getAvailabeHoldersList(context.getForm());
    }


    @Override
    public DataHolderTO[] addFieldFromHolder(String ctxUID, DataHolderFieldTO fieldTO) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return new DataHolderTO[0];

        DataHolder holder = context.getForm().getDataHolderById(fieldTO.getHolderId());

        if (holder != null) {
            DataFieldHolder field = holder.getDataFieldHolderById(fieldTO.getId());
            if (formManager.addDataHolderField(context.getForm(), holder, field)) {
                refreshCanvasEvent.fire(new RefreshCanvasEvent(ctxUID, contextMarshaller.marshallContext(context)));
            }
        }

        return getAvailabeHoldersList(context.getForm());

    }

    protected DataHolderTO[] getAvailabeHoldersList(Form form) {
        List<DataHolderTO> result = new ArrayList<DataHolderTO>();

        for (DataHolder holder : form.getHolders()) {
            DataHolderTO holderTO = buildDataHolderTo(holder, true, form, true);
            if (holderTO.getFields().length > 0) result.add(holderTO);
        }

        return result.toArray(new DataHolderTO[result.size()]);
    }

    protected DataHolderTO buildDataHolderTo(DataHolder holder, boolean addChild, Form form, boolean onlyNonBinded) {
        DataHolderTO to = new DataHolderTO();
        to.setType(holder.getTypeCode());
        to.setUniqueId(holder.getUniqueId());
        to.setInputId(holder.getInputId());
        to.setOutputId(holder.getOutputId());
        to.setClassName(holder.getClassName());
        to.setRenderColor(holder.getRenderColor());

        to.setCanHaveChild(holder.canHaveChildren());

        List<DataHolderFieldTO> fields = new ArrayList<DataHolderFieldTO>();
        if (addChild) {
            for (DataFieldHolder field : holder.getFieldHolders()) {
                if (onlyNonBinded && BindingUtils.isFieldBinded(form, field)) continue;
                DataHolderFieldTO fieldTO = new DataHolderFieldTO();
                fieldTO.setHolderId(to.getUniqueId());
                fieldTO.setId(field.getId());
                fieldTO.setClassName(field.getClassName());
                fieldTO.setIcon(field.getIcon());
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

            String xml = ioService.readAllString(kiePath).trim();

            Map<String, Object> serializationcontext = new HashMap<String, Object>();
            serializationcontext.put("path", path);

            Form form = formSerializationManager.loadFormFromXML(xml, serializationcontext);

            FormEditorContextTO result = new FormEditorContextTO();

            if (form == null) {
                result.setLoadError(true);
                form = formManager.createForm(path.getFileName());
            }

            ContextConfiguration config = new ContextConfiguration(form, new HashMap<String, Object>(), new HashMap<String, Object>(), localeManager.getLocaleById(localeName));
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

            ioService.write(kiePath, formSerializationManager.generateFormXML(form), makeCommentedOption(""));

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
        final String name = identity.getName();
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
            DataHolderBuildConfig config = new DataHolderBuildConfig(info.getUniqueId(), info.getInputId(), info.getOutputId(), info.getRenderColor(), info.getClassName());
            config.addAttribute("path", context.getAttributes().get("path"));
            DataHolder holder = dataHolderManager.createDataHolderByType(info.getType(), config);

            if (holder != null) {
                formManager.addDataHolderToForm(context.getForm(), holder);
                refreshHoldersListEvent.fire(new RefreshHoldersListEvent(event.getContext()));
            }
        }
    }

    @Override
    public String removeFieldFromForm(String ctxUID, int fieldPosition) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);
        String ctxJson = null;
        if (context != null) {
            formManager.deleteField(context.getForm(), fieldPosition);
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

    public void editFieldProperties(@Observes StartEditFieldPropertyEvent event) {
        FormRenderContext context = contextManager.getFormRenderContext(event.getContext());
        if (context != null) {
            Field field = context.getForm().getFieldById(Long.decode(event.getFieldUid()));
            Form editionForm = formManager.getFormForFieldEdition(field.getCode());
            if (editionForm != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("field", field);
                ContextConfiguration config = new ContextConfiguration(editionForm, data, new HashMap<String, Object>(), context.getCurrentLocale());
                FormRenderContext editionContext = contextManager.newContext(config);
                loadEditionEvent.fire(new LoadFieldEditionContextEvent(event.getContext(), editionContext.getUID(), editionContext.getMarshalledCopy()));
            }
        }
    }

    public void endFieldEdition(@Observes EndFieldEditionEvent event) {
        FormRenderContext context = contextManager.getFormRenderContext(event.getContext());
        FormRenderContext editionContext = contextManager.getFormRenderContext(event.getEditionContext());
        if (context != null && editionContext != null) {

            try {
                if (event.isPersist()) {
                    contextManager.persistContext(editionContext, event.getMarshalledContext());
                    String ctxJson = contextManager.marshallContext(context);
                    refreshCanvasEvent.fire(new RefreshCanvasEvent(event.getContext(), ctxJson));
                }
            } catch (Exception ex) {
                log.warn("Unable to persist context {}", editionContext.getUID(), ex);
            }

            contextManager.removeContext(editionContext);
        }
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
                Set<String> sources = rangedBuilder.getHolderSources(ctxUID).keySet();
                String[] values = sources.toArray(new String[sources.size()]);
                response[i] = new RangedDataHolderBuilderTO(builder.getId(), builder.getDataHolderName(context.getCurrentLocale()), values);
            } else {
                response[i] = new DataHolderBuilderTO(builder.getId(), builder.getDataHolderName(context.getCurrentLocale()));
            }
            i++;
        }
        return response;
    }
}
