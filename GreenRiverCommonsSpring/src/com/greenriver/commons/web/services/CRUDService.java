package com.greenriver.commons.web.services;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;

/**
 * This interface defines the methods a service ofering CRUD operations for 
 * an entity must implement.
 * 
 * @author luisro
 */
public interface CRUDService <D extends Dto,F extends FormDto>{
    Result<F> getNew();
    Result<D> getForView(Long id);
    Result<F> getForEdit(Long id);
    
    Result<D> save(F item);
    Result<D> remove(Long id);
    
    PagedResult<D> query(QueryArgs args);
}
