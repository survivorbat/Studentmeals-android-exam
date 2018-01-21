package nl.mheijden.prog3app.model.data.DAOs;

import java.util.ArrayList;

/**
 * Gemaakt door Maarten van der Heijden on 11-1-2018.
 */

interface DAO<T> {
    /**
     * @return ArrayList of items
     */
    @SuppressWarnings("unused")
    ArrayList<T> getAll();

    /**
     * @param id of the object that has to be returned
     * @return the object with the ID or null if that object doesnt exist
     */
    @SuppressWarnings("unused")
    T getOne(int id);

    /**
     * @param data is a list of objects to call insertOne for
     */
    @SuppressWarnings("unused")
    void insertData(ArrayList<T> data);
}
