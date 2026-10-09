package tn.esprit.autoloc.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.service.ICrudService;

import java.util.List;

public abstract class AbstractCrudService<T, I> implements ICrudService<T, I> {

    private final JpaRepository<T, I> repository;

    protected AbstractCrudService(JpaRepository<T, I> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(I id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entité introuvable : id = " + id));
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(I id) {
        repository.deleteById(id);
    }
}
