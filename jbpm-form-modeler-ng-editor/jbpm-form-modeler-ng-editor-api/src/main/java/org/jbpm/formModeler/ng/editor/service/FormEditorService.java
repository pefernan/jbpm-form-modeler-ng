package org.jbpm.formModeler.ng.editor.service;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.formModeler.ng.editor.model.DataHolderPageRow;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.uberfire.backend.vfs.Path;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;

@Remote
public interface FormEditorService {

    Path createForm(Path packageMainResourcesPath, String formName);

    FormEditorContextTO loadForm(Path path);

    void delete(Path path, String comment);

    void removeContext(String ctxUID);

    Path rename(Path path, String newName, String comment);

    void changeContextPath(String ctxUID, Path path);

    PageResponse<DataHolderPageRow> listFormDataHolders(PageRequest pageRequest, String ctxUID);
}
