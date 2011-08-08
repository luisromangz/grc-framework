package com.greenriver.commons.data.dao;

import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import java.util.List;

/**
 *
 * @author luisro
 */
public interface CRUDDao<E extends DataEntity, Q extends QueryArgs> {
    public E get(E entity);
    public E getById(Long id);
    public void save(E entity);
    public void remove(E entity);
    public List<E> getAll(String orderField);
    public int query(Q qArgs, List<E> entities);
}
