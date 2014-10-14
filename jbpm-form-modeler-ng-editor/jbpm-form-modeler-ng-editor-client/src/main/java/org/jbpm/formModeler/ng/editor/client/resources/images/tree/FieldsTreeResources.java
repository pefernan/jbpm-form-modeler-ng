package org.jbpm.formModeler.ng.editor.client.resources.images.tree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

public interface FieldsTreeResources extends Tree.Resources {

    @Source("FolderClosed.png")
    ImageResource treeClosed();

    @Source("trans.png")
    ImageResource treeLeaf();

    @Source("FolderOpen.png")
    ImageResource treeOpen();

    @Source("AddField.png")
    ImageResource addField();

    @Source("EditField.png")
    ImageResource editField();
}
