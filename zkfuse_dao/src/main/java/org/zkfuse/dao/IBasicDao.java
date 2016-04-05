package org.zkfuse.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;

/**
 * Generic DAO (Data Access Object) with common methods to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) DAO's for your
 * domain objects.
 *
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public interface IBasicDao<T, PK extends Serializable> {
    
    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T get(PK id);

    /**
     * Retrieves a non-paged query. Use with care, this method could potentially instantiate large amounts of data.
     */
    List<T> find(String queryString, Object[] params);

    /**
     * Retrieves a paged query.
     */
    List<T> find(String queryString, int firstResult, int maxResults, Object[] params);

    /**
     * @return a single object that satisfies the query.
     * @throws NoResultException
     *             if there is no result
     * @throws NonUniqueResultException
     *             if more than one result
     * @throws IllegalStateException
     *             if called for a Java Persistence query language UPDATE or DELETE statement
     */
    Object findSingle(String queryString, Object[] params);

    T merge(T entity);

    void persist(Object entity);

    /**
     * Removes a persistent instance.
     * 
     * @param <T>
     *            The persistent class
     * @param clazz
     *            The persistent class
     * @param id
     *            the primary key to remove
     * @return the removed instance
     */
    T remove(Class<T> clazz, Serializable id) throws EntityNotFoundException;

    List<T> findAll(Class<T> clazz, int firstResult, int maxResults);

    List<T> findAll(Class<T> clazz);

    Long count(Class<T> clazz);

    Long count();

    T find(Class<T> clazz, Serializable id) throws EntityNotFoundException;

    void flush();

    void clear();

    EntityManager getEntityManager();

    void refresh(Object entity);

    /**
     * Retrieves a non-paged query.
     */
    List<T> findNamedQuery(final String namedQuery, int firstResult, int maxResult, Object[] params);

    /**
     * @return a single object that satisfies the named query.
     * @throws NoResultException
     *             if there is no result
     * @throws NonUniqueResultException
     *             if more than one result
     * @throws IllegalStateException
     *             if called for a Java Persistence query language UPDATE or DELETE statement
     */
    Object findNamedQuerySingle(String namedQuery, Object[] params);

    int bulkUpdate(String queryString, Object[] params);

}