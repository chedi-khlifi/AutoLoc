package tn.esprit.autoloc.service;

import java.util.List;

public interface ICrudService<T, I> {

    List<T> findAll();

    T findById(I id);

    T save(T entity);

    void deleteById(I id);
}
