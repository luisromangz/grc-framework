package com.greenriver.commons.web.services;

import com.greenriver.commons.data.DataEntity;

/**
 *
 * @author luisro
 */
public abstract class Dto<T extends DataEntity> implements DataEntity {

    private Long id;
    private boolean newEntity = false;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNewEntity() {
        return newEntity;
    }

    public void setNewEntity(boolean newEntity) {
        this.newEntity = newEntity;
    }

    public abstract void copyFrom(T entity, boolean simplified);
}
