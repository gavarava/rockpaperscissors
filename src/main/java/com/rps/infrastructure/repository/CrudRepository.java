package com.rps.infrastructure.repository;

import com.rps.infrastructure.repository.exceptions.AlreadyExistsException;
import com.rps.infrastructure.repository.exceptions.NotFoundException;
import java.io.Serializable;
import java.util.Collection;

public interface CrudRepository<T, ID extends Serializable> {

    void save(T entity) throws AlreadyExistsException;

    T findOne(ID primaryKey);

    Collection<T> findAll();

    int count();

    void delete(T entity) throws NotFoundException;

    void deleteById(ID primaryKey) throws NotFoundException;

    boolean exists(ID primaryKey);
}
