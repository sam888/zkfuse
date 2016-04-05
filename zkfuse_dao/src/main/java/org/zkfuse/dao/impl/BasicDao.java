package org.zkfuse.dao.impl;

import org.springframework.stereotype.Repository;
import org.zkfuse.dao.IBasicDao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * 
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
@Repository
public class BasicDao<T, PK extends Serializable> implements IBasicDao<T, PK> {

    protected EntityManager entityManager;
    
    private Class<T> persistentClass;
    
    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public BasicDao(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }
    
    public BasicDao() {}

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String queryString, Object... params) {
        // queryString += " order by firstName desc";
        Query query = entityManager.createQuery(queryString);
        query.setFlushMode(FlushModeType.COMMIT);   // Avoid recursive call and StackOverflowError
        setParameters(query, params);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String queryString, int firstResult, int maxResults, Object[] params) {
        Query query = entityManager.createQuery(queryString);
        query.setFlushMode(FlushModeType.COMMIT);   // Avoid recursive call and StackOverflowError
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        setParameters(query, params);
        return query.getResultList();
    }

    public Object findSingle(String queryString, Object[] params) {
        Query query = entityManager.createQuery(queryString);
        setParameters(query, params);
        return query.getSingleResult();
    }

    /**
     * Sets all the parameters of a query
     */
    private void setParameters(Query query, Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
    }

    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public T remove(Class<T> clazz, Serializable id) throws EntityNotFoundException {
        T instance = find(clazz, id);
        entityManager.remove(instance);
        return instance;
    }

    public List<T> findAll(Class<T> clazz, int firstResult, int maxResults) {
        return (List<T>) find("from " + clazz.getName(), firstResult, maxResults);
    }

    public List<T> findAll(Class<T> clazz) {
        return (List<T>) find("from " + clazz.getName());
    }

    public Long count(Class<T> clazz) {
        Query query = entityManager.createQuery("SELECT COUNT(o) FROM " + clazz.getName() + " o");
        return (Long) query.getSingleResult();
    }
    
    public Long count() {
        return count( persistentClass );
    }

    public T find(Class<T> clazz, Serializable id) throws EntityNotFoundException {
        T result = entityManager.find(clazz, id);
        if (result == null) { // Arguable logic here! OK to return null
        	String errorMsg = "Can't find entity " + clazz.getCanonicalName() + " with id " + id;
            throw new EntityNotFoundException( errorMsg );
        }
        return result;
    }
    
    // added
    public T get(PK id) {
        return find(this.persistentClass, id);
    }
    

    public void flush() {
        entityManager.flush();
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }
    
    @SuppressWarnings("unchecked")
    public List<T> findNamedQuery(final String namedQuery, int firstResult, int maxResult, Object[] params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        // query.setFirstResult(firstResult);
        // query.setMaxResults(maxResult);
        setParameters(query, params);
        return query.getResultList();
    }

    public Object findNamedQuerySingle(String namedQuery, Object[] params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        setParameters(query, params);
        return query.getSingleResult();
    }

    public int bulkUpdate(String queryString, Object[] params) {
        Query query = entityManager.createQuery(queryString);
        setParameters(query, params);
        return query.executeUpdate();
    }
    
    public int executeSingleNamedQuery(String namedQuery, Object[] params) {
        Query query = entityManager.createNamedQuery(namedQuery);
        setParameters(query, params);
        return query.executeUpdate();
    }
    
}
