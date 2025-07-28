package indie.services;

import java.util.List;

public interface BaseService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    T save(T entity);
    T update(ID id, T entity);
    void deleteById(ID id);
}

