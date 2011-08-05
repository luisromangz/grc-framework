package com.greenriver.commons.web.services;

import com.greenriver.commons.data.DataEntity;

/**
 *
 * @author luisro
 */
public abstract class FormDto<T extends DataEntity> implements DataEntity {

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public abstract void copyFrom(T entity);
    
    public abstract void copyTo(T entity);
}
