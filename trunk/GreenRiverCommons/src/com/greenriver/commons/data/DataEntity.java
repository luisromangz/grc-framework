package com.greenriver.commons.data;

import java.io.Serializable;

/**
 *
 * @author luisro
 */
public interface DataEntity extends Serializable {

    public Long getId();
    public String getLabel();

}
