package com.pao.proiect.licitatii.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID>{

    void create(T obiect);
    List<T> readAll();
    void update(T obiect);
    void delete(ID id);
    Optional<T> findById(ID id);

    void initCounter();
}
