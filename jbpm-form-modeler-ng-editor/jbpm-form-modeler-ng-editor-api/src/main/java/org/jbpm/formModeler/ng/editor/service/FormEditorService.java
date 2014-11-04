package org.jbpm.formModeler.ng.editor.service;

import org.guvnor.common.services.shared.metadata.model.Metadata;
import org.jboss.errai.bus.server.annotations.Remote;
import org.jbpm.formModeler.ng.editor.model.EditionContextTO;
import org.jbpm.formModeler.ng.editor.model.FormEditorContextTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderBuilderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderFieldTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.DataHolderTO;
import org.jbpm.formModeler.ng.editor.model.dataHolders.FormDataHoldersTO;
import org.uberfire.backend.vfs.ObservablePath;
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

    FormDataHoldersTO getAvailableDataHolders(String ctxUID);

    FormDataHoldersTO addFieldFromHolder(String ctxUID, DataHolderFieldTO fieldTO);

    String addFieldFromTypeCode(String ctxUID, String code);

    String removeFieldFromForm(String ctxUID,  Long fieldId);

    String moveSelectedFieldToFieldPosition(String ctxUID, Long fieldId, int row, int column);

    Path save(ObservablePath path, String ctxUID, Metadata content, String commitMessage);

    EditionContextTO startFieldEdition(String ctxUID, Long fieldId, String fieldJson);

    void editFieldValue(String ctxUID, String editionCtxUID, String editionMarshalledCtx, boolean persist);

    EditionContextTO changeFieldType(String ctxUID, String fieldName, String code, String editionCtxUID);

    Long getFieldIdFromExpression(String ctxUID, String bindingExpression);

    String changeFormLayout(String ctxUID, String id);

    String changeFormLabelPosition(String ctxUID, String labelPosition);
}
