package org.zkfuse.web.zul;

import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.WrongValueException;

/**
 * A convenient sub class of Textbox to trim all lead or trailing white spaces.
 *
 *  @author Samuel Huang
 */
public class Textbox extends org.zkoss.zul.Textbox {
    
    private static final long serialVersionUID = 8301845892416904492L;

    @Override
    public String getValue() {
        return StringUtils.trimToEmpty( super.getValue() );
    }
    
    @Override
    public void setValue(String value) throws WrongValueException {
       setText( StringUtils.trimToEmpty( value ) );
    }

}
