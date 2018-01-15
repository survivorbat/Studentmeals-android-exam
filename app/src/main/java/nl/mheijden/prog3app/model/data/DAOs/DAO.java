package nl.mheijden.prog3app.model.data.DAOs;

import java.util.ArrayList;

/**
 * Gemaakt door Maarten van der Heijden on 11-1-2018.
 */

public interface DAO<T> {
    /**
     * @return ArrayList of items
     */
    ArrayList<T> getAll();

    /**
     * @param id of the object that has to be returned
     * @return
     */
    T getOne(int id);

    /**
     * Delete all records from the database
     */
    void clear();

    /**
     * @param data is a list of objects to call insertOne for
     */
    void insertData(ArrayList<T> data);

    /**
     * @param object you want to insert into the database
     */
    void insertOne(T object);
}
