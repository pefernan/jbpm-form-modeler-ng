package org.jbpm.formModeler.ng.editor.backend;

import org.apache.commons.lang.StringUtils;
import org.guvnor.common.services.backend.exceptions.ExceptionUtilities;
import org.guvnor.common.services.shared.file.DeleteService;
import org.guvnor.common.services.shared.file.RenameService;
import org.jboss.errai.bus.server.annotations.Service;
import org.jbpm.formModeler.ng.editor.model.DataHolderPageRow;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.service.FormEditorService;
import org.jbpm.formModeler.ng.model.DataHolder;
import org.jbpm.formModeler.ng.model.Form;
import org.jbpm.formModeler.ng.services.context.ContextConfiguration;
import org.jbpm.formModeler.ng.services.context.FormRenderContext;
import org.jbpm.formModeler.ng.services.context.FormRenderContextManager;
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
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
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
    private FormManager formManager;

    @Inject
    private FormSerializationManager formSerializationManager;

    @Inject
    private FormRenderContextManager contextManager;

    @Override
    public PageResponse<DataHolderPageRow> listFormDataHolders(PageRequest pageRequest, String ctxUID) {
        FormRenderContext context = contextManager.getFormRenderContext(ctxUID);

        if (context == null) return null;

        final PageResponse<DataHolderPageRow> response = new PageResponse<DataHolderPageRow>();
        final List<DataHolderPageRow> tradeRatePageRowList = new ArrayList<DataHolderPageRow>();

        int i = 0;
        for (DataHolder holder : context.getForm().getHolders()) {
            if ( i >= pageRequest.getStartRowIndex() + pageRequest.getPageSize() ) {
                break;
            }
            if ( i >= pageRequest.getStartRowIndex() ) {
                DataHolderPageRow to = new DataHolderPageRow();
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

            ContextConfiguration config = new ContextConfiguration(form, new HashMap<String, Object>(), new HashMap<String, Object>(), new Locale(localeName));
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
}
