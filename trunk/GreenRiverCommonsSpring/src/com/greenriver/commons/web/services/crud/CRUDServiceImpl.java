package com.greenriver.commons.web.services.crud;

import com.greenriver.commons.collections.Applicable;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.CRUDDao;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.data.validation.ValidationResult;
import com.greenriver.commons.web.services.Dto;
import com.greenriver.commons.web.services.FormDto;
import com.greenriver.commons.web.services.PagedResult;
import com.greenriver.commons.web.services.Result;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Base class for CRUD-handling services.
 * @author luisro
 */
public abstract class CRUDServiceImpl<E extends DataEntity, D extends Dto, F extends FormDto, Q extends QueryArgs>
        implements CRUDService<D, F, Q> {
    protected static final String DB_ERROR_MSG=
            "Ocurrió un error de base de datos.";
    
    // Will be set as needed.
    private Class<E> entityClass = null;
    private Class<D> dtoClass = null;
    private Class<F> formDtoClass = null;
    private CRUDDao<E, Q> dao;
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
        
        if(id==null) {
            getLogger().error("Null id provided!");
            r.addErrorMessage("El identificador es nulo.");
            return r;
        }

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
        
        if(id==null) {
            getLogger().error("Null id provided!");
            r.addErrorMessage("El identificador es nulo.");
            return r;
        }
        
        if(id==null || id==0) {
            throw new IllegalArgumentException("Not received a valid id");
        }

        E entity = getById(id, r);
        if (!r.isSuccess()) {
            return r;
        }
        
        if(!validateEdition(entity, r)) {
            return r;
        }

        r.setResult(getFormDto(entity));
        return r;
    }
    
    
    protected boolean validateEdition(E entity, Result<F> r) {
       return true;
    }

    protected Result<D> saveInternal(F formDto) {
        Result<D> result = new Result<D>();

        // We validate the received data.
        ValidationResult validation = fieldsValidator.validate(formDto);
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

        if (!validateSaving(formDto, entity, result)) {
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
        dto.setNewEntity(formDto.getId() == null);

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

    protected PagedResult<D> queryInternal(Q args) {
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
            getLogger().error("Error getting new entity", e);
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
            this.formDtoClass = getArgumentClass(FormDto.class);
        }
        return this.formDtoClass;

    }

    private Class<E> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = getArgumentClass(DataEntity.class);
        }

        return this.entityClass;
    }

    private Class<D> getDtoClass() {
        if (this.dtoClass == null) {
            this.dtoClass = getArgumentClass(Dto.class);
        }

        return this.dtoClass;
    }

    protected Class getArgumentClass(Class baseClass) {

        Class type = this.getClass();

        List<Class> argumentClasses = new ArrayList<Class>();

        while (type.getGenericSuperclass() != Object.class) {
            Type superclass = type.getGenericSuperclass();

            if (!(superclass instanceof ParameterizedType)) {
                throw new IllegalStateException(
                        "Entity template class needs to be specified when extending HibernateDaoBase.");
            }

            ParameterizedType parameterizedType = (ParameterizedType) superclass;

            List<Class> localArguments = new ArrayList<Class>();
            for (Type t : parameterizedType.getActualTypeArguments()) {
                if (t instanceof Class) {
                    Class c = (Class) t;
                    if (baseClass.isAssignableFrom(c)) {
                        localArguments.add(c);
                    }
                }

            }

            argumentClasses.addAll(0, localArguments);

            type = type.getSuperclass();
        }

        if(argumentClasses.size()>1) {
            throw new IllegalStateException(String.format("Class '%s' has more than two template parameters of type '%s'.",
                    this.getClass().getName(),
                    baseClass.getName()));
        }

        return argumentClasses.get(0);
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

    protected Logger getLogger() {
        return Logger.getLogger(this.getClass().getName());
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the dao
     */
    public CRUDDao<E, Q> getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(CRUDDao<E, Q> dao) {
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
