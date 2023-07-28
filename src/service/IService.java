package service;

import java.util.List;

public interface IService<E, K> {

    String insert(E entity);

    void update(E entity, K id);

    void delete(K id);

    List<E> getAll();
}
