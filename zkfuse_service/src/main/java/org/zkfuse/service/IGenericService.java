package org.zkfuse.service;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityNotFoundException;

public interface IGenericService<T, PK extends Serializable> {

    Long count();
    
    T get(PK id);

    T findByID(Serializable id) throws EntityNotFoundException;

    List<T> findAll();

    List<T> findAll(int firstResult, int maxResults);

    void persist(T object);

    void delete(Serializable id) throws EntityNotFoundException;

    void merge(T object) throws EntityNotFoundException;
    
    void flush();
    void clear();
    void refresh(T object);

}