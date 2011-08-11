package com.greenriver.commons.web.services.crud;

import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.CRUDDao;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.web.services.Dto;
import com.greenriver.commons.web.services.FormDto;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.Result;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for CRUD-handling services.
 * @author luisro
 */
public abstract class CRUDServiceImpl<E extends DataEntity, D extends Dto, F extends FormDto>
        implements CRUDService<D, F> {

    // Will be set as needed.
    private Class<E> entityClass = null;
    private Class<D> dtoClass = null;
    private Class<F> formDtoClass = null;
    private CRUDDao<E, QueryArgs> dao;
    private FieldsValidator fieldsValidator;
    private boolean maleElement = true;
    private String element = "elemento";

    //<editor-fold defaultstate="collapsed" desc="Service methods">
    @Override
    public Result<F> getNew() {
        Result<F> result = new Result<F>();

        E newEntity = getNewEntity(result);
        if (!result.isSuccess()) {
            return result;
        }

        result.setResult(getFormDto(newEntity));

        return result;
    }

    @Override
    public Result<D> getForView(Long id) {
        Result<D> r = new Result<D>();

        E entity = getById(id, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(getDto(entity, false));
        return r;
    }

    @Override
    public Result<F> getForEdit(Long id) {
        Result<F> r = new Result<F>();

        E entity = getById(id, r);
        if (!r.isSuccess()) {
            return r;
        }

        r.setResult(getFormDto(entity));
        return r;
    }

    
    protected Result<D> saveInternal(F formDto) {
        Result<D> result = new Result<D>();

        // We validate the received data.
        FieldsValidationResult validation = fieldsValidator.validate(formDto);
        if (!validation.isValid()) {
            result.addErrorMessages(validation.getErrorMessages());
            return result;
        }

        // We load the entity from the database, or create  a new instance
        // if the element is being added, nor updated.
        E entity = null;
        if (formDto.getId() == null) {
            entity = getNewEntity(result);
        } else {
            entity = getById(formDto.getId(), result);
        }

        if (!result.isSuccess()) {
            return result;
        }
        
        if(!validateSaving(formDto, entity,result)){
            return result;
        }

        // We copy the received values to the entity.
        formDto.copyTo(entity);

        // And save it.
        try {
            dao.save(entity);
        } catch (RuntimeException e) {
            result.addErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        D dto = getDto(entity, true);
        dto.setNewEntity(formDto.getId()==null);
        
        result.setResult(dto);

        return result;
    }

    @Override
    public Result<D> remove(Long id) {
        Result<D> res = new Result<D>();

        //Don't let a user to be removed if it is the current user


        E existingEntity = getById(id, res);
        if (!res.isSuccess()) {
            return res;
        }

        if (!validateRemoval(existingEntity, res)) {
            return res;
        }

        try {
            dao.remove(existingEntity);
        } catch (RuntimeException e) {
            res.addErrorMessage("Ocurrió un error de base de datos.");
        }

        res.setResult(getDto(existingEntity, true));


        return res;
    }

    protected boolean validateRemoval(E entity, Result res) {
        return true;
    }
    
    protected boolean validateSaving(F item, E entity, Result result) {
        return true;
    }


    @Override
    public PagedResult<D> query(QueryArgs args) {
        PagedResult<D> result = new PagedResult<D>();

        List<E> entitities = new ArrayList<E>();

        try {
            result.setTotal(dao.query(args, entitities));
        } catch (RuntimeException ex) {
            result.formatErrorMessage("Ocurrió un error de base de datos.");
            return result;
        }

        result.setResult(Lists.apply(entitities, new Applicable<E, D>() {

            @Override
            public D apply(E element) {
                return getDto(element, true);
            }
        }));

        return result;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Auxiliary methods">

    protected E getNewEntity(Result result) {
        E newEntity = null;
        try {
            newEntity = getEntityClass().newInstance();            
        } catch (Throwable e) {
            result.formatErrorMessage(
                    "Ocurrió un error al obtener %s %s.",
                    maleElement ? "un nuevo" : "una nueva",
                    element);
        }

        return newEntity;
    }

    protected F getFormDto(E entity) {
        F formDto = null;
        try {
            formDto = getFormDtoClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        formDto.copyFrom(entity);
        return formDto;
    }

    protected D getDto(E entity, boolean simplify) {
        D dto = null;
        try {
            dto = getDtoClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dto.copyFrom(entity, simplify);
        return dto;
    }

    private Class<F> getFormDtoClass() {
        if (this.formDtoClass == null) {
            this.formDtoClass = getArgumentClass(2);
        }
        return this.formDtoClass;

    }

    private Class<E> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = getArgumentClass(0);
        }

        return this.entityClass;
    }

    private Class<D> getDtoClass() {
        if (this.dtoClass == null) {
            this.dtoClass = getArgumentClass(1);
        }

        return this.dtoClass;
    }

    protected Class getArgumentClass(int argumentIndex) {

        Class type = this.getClass();

        List<Class> argumentClasses = new ArrayList<Class>();

        while (type.getGenericSuperclass()!= Object.class) {
            Type superclass = type.getGenericSuperclass();

            if (!(superclass instanceof ParameterizedType)) {
                throw new IllegalStateException(
                        "Entity template class needs to be specified when extending HibernateDaoBase.");
            }

            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            
            List<Class> localArguments = new ArrayList<Class>();
            for(Type t : parameterizedType.getActualTypeArguments()) {
                if(t instanceof Class) {
                    Class c = (Class) t;        
                    localArguments.add(c);
                }
                
            }
            
            argumentClasses.addAll(0, localArguments);
            
            type = type.getSuperclass();
        }



       return argumentClasses.get(argumentIndex);
    }

    protected E getById(Long id, Result r) {
        E entity = null;
        try {
            entity = dao.getById(id);
        } catch (RuntimeException e) {
            r.addErrorMessage("Ocurrió un error en la base de datos.");
            return null;
        }

        if (entity == null) {
            r.formatErrorMessage("%s %s indicado no existe.",
                    maleElement ? "El" : "La",
                    element);
            return null;
        }

        return entity;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the dao
     */
    public CRUDDao<E, QueryArgs> getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CRUDDao<E, QueryArgs> dao) {
        this.dao = dao;
    }

    /**
     * @return the maleElement
     */
    public boolean isMaleElement() {
        return maleElement;
    }

    /**
     * @param maleElement the maleElement to set
     */
    public void setMaleElement(boolean maleElement) {
        this.maleElement = maleElement;
    }

    /**
     * @return the element
     */
    public String getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(String element) {
        this.element = element;
    }
    
    
    /**
     * @return the fieldsValidator
     */
    public FieldsValidator getFieldsValidator() {
        return fieldsValidator;
    }

    /**
     * @param fieldsValidator the fieldsValidator to set
     */
    public void setFieldsValidator(FieldsValidator fieldsValidator) {
        this.fieldsValidator = fieldsValidator;
    }
    //</editor-fold>

  
}
