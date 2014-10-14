package org.jbpm.formModeler.ng.common.client.rendering.resources.images;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface FieldTypeImages extends
        ClientBundle {

    public static final FieldTypeImages INSTANCE = GWT.create(FieldTypeImages.class);

    @Source("ico_default.png")
    public ImageResource defaultImage();

    @Source("ico_calendar.png")
    public ImageResource date();

    @Source("ico_checkbox.png")
    public ImageResource checkbox();

    @Source("ico_radiobutton.png")
    public ImageResource radiobutton();

    @Source("ico_number.png")
    public ImageResource number();

    @Source("ico_decim.png")
    public ImageResource decimal();

    @Source("ico_select.png")
    public ImageResource dropdown();

    @Source("ico_text.png")
    public ImageResource textbox();
}
