package com.greenriver.commons.web.services;

import com.greenriver.commons.data.dao.queryArguments.QueryArgs;
import java.util.List;

/**
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
