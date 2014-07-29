package org.jbpm.formModeler.ng.editor.backend;

import org.apache.commons.lang.StringUtils;
import org.guvnor.common.services.backend.exceptions.ExceptionUtilities;
import org.guvnor.common.services.shared.file.DeleteService;
import org.guvnor.common.services.shared.file.RenameService;
import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.formModeler.ng.editor.events.dataHolders.NewDataHolderEvent;
import org.jbpm.formModeler.ng.editor.events.dataHolders.RefreshHoldersListEvent;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderInfo;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.RangedDataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.LocaleManager;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuildConfig;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.dataHolders.DataHolderManager;
import org.jbpm.formModeler.ng.services.management.dataHolders.RangedDataHolderBuilder;
import org.jbpm.formModeler.ng.services.management.forms.FormManager;
import org.jbpm.formModeler.ng.services.management.forms.FormSerializationManager;
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
    private LocaleManager localeManager;

    @Inject
    private FormManager formManager;

    @Inject
    private DataHolderManager dataHolderManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FormRenderContextManager contextManager;

    @Override
    public PageResponse<DataHolderInfo> listFormDataHolders(PageRequest pageRequest, String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return null;

        final PageResponse<DataHolderInfo> response = new PageResponse<DataHolderInfo>();
        final List<DataHolderInfo> tradeRatePageRowList = new ArrayList<DataHolderInfo>();

        int i = 0;
        for (DataHolder holder : context.getForm().getHolders()) {
            if ( i >= pageRequest.getStartRowIndex() + pageRequest.getPageSize() ) {
                break;
            }
            if ( i >= pageRequest.getStartRowIndex() ) {
                DataHolderInfo to = new DataHolderInfo();
                to.setType(holder.getTypeCode());
                to.setUniqueId(holder.getUniqueId());
                to.setInputId(holder.getInputId());
                to.setOutputId(holder.getOutputId());
                to.setClassName(holder.getClassName());
                to.setRenderColor(holder.getRenderColor());
                tradeRatePageRowList.add(to);
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

    public void createDataHolder(@Observes NewDataHolderEvent newDataHolderEvent) {
        FormRenderContext context = contextManager.getFormRenderContext(newDataHolderEvent.getContext().getCtxUID());
        if (context != null) {
            DataHolderInfo info = newDataHolderEvent.getDataHolder();
            DataHolderBuildConfig config = new DataHolderBuildConfig(info.getUniqueId(), info.getInputId(), info.getOutputId(), info.getRenderColor(), info.getClassName());
            config.addAttribute("path", context.getAttributes().get("path"));
            DataHolder holder = dataHolderManager.createDataHolderByType(info.getType(), config);

            if (holder != null) {
                formManager.addDataHolderToForm(context.getForm(), holder);
                refreshHoldersListEvent.fire(new RefreshHoldersListEvent(newDataHolderEvent.getContext()));
            }
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
