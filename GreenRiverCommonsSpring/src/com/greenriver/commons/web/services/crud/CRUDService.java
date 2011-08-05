package com.greenriver.commons.web.services.crud;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.web.services.Dto;
import com.greenriver.commons.web.services.FormDto;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.Result;

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
    
    //Result<D> save(F item);
    Result<D> remove(Long id);
    
    PagedResult<D> query(QueryArgs args);
}
