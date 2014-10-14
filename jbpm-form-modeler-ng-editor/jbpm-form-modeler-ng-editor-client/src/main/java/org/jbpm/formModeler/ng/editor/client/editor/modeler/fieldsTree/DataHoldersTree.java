package org.jbpm.formModeler.ng.editor.client.editor.modeler.fieldsTree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;

public class DataHoldersTree extends Tree {
    public DataHoldersTree() {
    }

    public DataHoldersTree(Resources resources) {
        super(resources);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK)
            return;
        if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
            int scrollLeftInt = Window.getScrollLeft();
            int scrollTopInt = Window.getScrollTop();
            DOM.setStyleAttribute(this.getElement(), "position",
                    "fixed");
            super.onBrowserEvent(event);
            DOM.setStyleAttribute(this.getElement(), "position",
                    "static");

            Window.scrollTo(scrollLeftInt, scrollTopInt);
            return;
        }
        super.onBrowserEvent(event);
    }
}
