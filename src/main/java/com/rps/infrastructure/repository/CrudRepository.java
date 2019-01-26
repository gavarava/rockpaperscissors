package com.rps.infrastructure.repository;

import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;

import java.io.Serializable;
import java.util.Collection;

public interface CrudRepository<T, ID extends Serializable> {

    void save(T entity) throws AlreadyExistsException;

    T findOne(ID primaryKey);

    Collection<T> findAll();

    int count();

    void delete(T entity);

    void deleteById(ID primaryKey);

    boolean exists(ID primaryKey);
}
