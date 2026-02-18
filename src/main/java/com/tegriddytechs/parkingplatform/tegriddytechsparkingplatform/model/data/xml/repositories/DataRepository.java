package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DataRepository<T> {
    List<T> findAll();
    Optional<T> findById(int id);
    void upsert(T t) throws IOException;
    boolean deleteById(int id) throws IOException;
    boolean delete(T t) throws IOException;
}
