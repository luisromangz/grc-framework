package com.greenriver.commons.web.services;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import java.util.List;

/**
 * This interface defines the methods a service ofering CRUD operations for 
 * an entity must implement.
 * 
 * @author luisro
 */
public interface CRUDService <D,F>{
    Result<F> getNew();
    Result<D> getForView(Long id);
    Result<F> getForEdit(Long id);
    
    Result<D> save(F item);
    Result<D> remove(Long id);
    
    Result<List<D>> query(QueryArgs args);
}
