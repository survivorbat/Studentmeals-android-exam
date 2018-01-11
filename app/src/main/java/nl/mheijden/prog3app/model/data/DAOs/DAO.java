package nl.mheijden.prog3app.model.data.DAOs;

import java.util.ArrayList;

/**
 * Gemaakt door Maarten van der Heijden on 11-1-2018.
 */

public interface DAO<T> {
    ArrayList<T> getAll();
    T getOne(int id);
    void clear();
    void insertData(ArrayList<T> data);
    void insertOne(T object);
}
