package org.jbpm.formModeler.ng.editor.service;

import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.uberfire.backend.vfs.Path;
import org.uberfire.paging.PageRequest;
import org.uberfire.paging.PageResponse;

@Remote
public interface FormEditorService {

    Path createForm(Path packageMainResourcesPath, String formName);

    FormEditorContextTO loadForm(Path path, String localeName);

    void delete(Path path, String comment);

    void removeContext(String ctxUID);

    Path rename(Path path, String newName, String comment);

    void changeContextPath(String ctxUID, Path path);

    PageResponse<DataHolderTO> listFormDataHolders(PageRequest pageRequest, String ctxUID);

    DataHolderTO[] getAvailableDataHolders(String ctxUID);

    DataHolderTO[] addFieldFromHolder(String ctxUID, DataHolderFieldTO fieldTO);

    DataHolderBuilderTO[] getAvailableDataHolderBuilders(String ctxUID);

    String removeFieldFromForm(String ctxUID,  int fieldPosition);
}
