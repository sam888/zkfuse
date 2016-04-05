
package org.zkfuse.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkfuse.dao.impl.BasicDao;
import org.zkfuse.service.IGenericService;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class GenericService<T, PK extends Serializable> implements IGenericService<T, PK> {

    private Class<T> objectClass;

    @Autowired
    private BasicDao<T, PK> basicDao;

    public GenericService() {}
    
    public GenericService(final Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    public Long count() {
        return this.basicDao.count(this.objectClass);
    }
    
    public T get(PK id) {
        return this.basicDao.get(id);
    }

    public T findByID(Serializable id) throws EntityNotFoundException {
        return (T)basicDao.find(objectClass, id);
    }

    public List<T> findAll() {
        List<T> objects = this.basicDao.findAll(objectClass);
        return objects;
    }

    public List<T> findAll(int firstResult, int maxResults) {
        List<T> objects = this.basicDao.findAll(objectClass, firstResult, maxResults);
        return objects;
    }

    protected List<T> findByNamedQuery(String namedQuery, int firstResult, int maxResults, String... params) {
        return this.basicDao.findNamedQuery(namedQuery, firstResult, maxResults, params);
    }

    protected Object findByNamedQuerySingle(String namedQuery, String... params) {
        return this.basicDao.findNamedQuerySingle(namedQuery, params);
    }

    public Object findSingle(String queryString, String... params) {
        return this.basicDao.findSingle(queryString, params);
    }
    
    public void flush() {
        this.basicDao.flush();
    }

    public void clear() {
        this.basicDao.clear();
    }

    @Override
    public void refresh(T object) {
        this.basicDao.refresh( object );
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void persist(T object) {
        this.basicDao.persist(object);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(Serializable id) throws EntityNotFoundException {
        this.basicDao.remove(objectClass, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void merge(T object) throws EntityNotFoundException {
        this.basicDao.merge(object);
    }

}
