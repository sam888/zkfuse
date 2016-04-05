package org.zkfuse.web.util.i18n;

import java.util.Comparator;

import org.zkfuse.model.i18n.KeyValue;

/**
 * @author Samuel Huang
 */
public class KeyValueComparator implements Comparator<KeyValue>{

    @Override
    public int compare(KeyValue o1, KeyValue o2) {
        return o1.getProperty().compareTo( o2.getProperty() );
    }

}
