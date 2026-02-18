package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml;

import java.io.IOException;
import java.util.List;

public interface PersistenceOperations<T> {
    public void save() throws IOException;
    public List<T> load();
}
