package nl.mheijden.prog3app.model.data.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.data.SQLiteLocalDatabase;
import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class FellowEaterDAO implements DAO<FellowEater> {
    /**
     * Database object to send queries to
     */
    private final SQLiteLocalDatabase db;

    /**
     * @param db object
     */
    public FellowEaterDAO(SQLiteLocalDatabase db) {
        this.db = db;
    }

    /**
     * @return list of felloweaters
     */
    public ArrayList<FellowEater> getAll() {
        ArrayList<FellowEater> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT FellowEaters.ID, AmountOfGuests, FellowEaters.StudentNumber, FirstName, Insertion, LastName, Email, PhoneNumber, MealID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat FROM FellowEaters INNER JOIN Students ON FellowEaters.StudentNumber = Students.StudentNumber INNER JOIN Meals ON Meals.ID = FellowEaters.MealID ORDER BY FellowEaters.ID", null);
        if (i.moveToFirst()) {
            while (!i.isAfterLast()) {
                FellowEater s = new FellowEater();
                s.setId(i.getInt(0));
                s.setGuests(i.getInt(1));
                s.setStudent(new Student(i.getString(2), i.getString(3), i.getString(4), i.getString(5), i.getString(6), i.getString(7)));
                s.setMeal(new Meal(i.getInt(8), i.getString(9), i.getString(10), i.getString(11), new Student(), i.getDouble(14), i.getInt(15), Boolean.parseBoolean(i.getString(16))));
                rs.add(s);
                i.moveToNext();
            }
        }
        i.close();
        db.close();
        return rs;
    }

    /**
     * Empty method that is never used for this DAO in this application
     */
    public FellowEater getOne(int id) {
        return new FellowEater();
    }


    /**
     * @param data is a list of objects to call insertOne for
     */
    public void insertData(ArrayList<FellowEater> data) {
        clear();
        for (FellowEater fellowEater : data) {
            insertOne(fellowEater);
        }
    }

    /**
     * @param object you want to insert into the database
     */
    public void insertOne(FellowEater object) {
        android.database.sqlite.SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", object.getId());
        i.put("AmountOfGuests", object.getGuests());
        i.put("StudentNumber", object.getStudent().getstudentNumber());
        i.put("MealID", object.getMeal().getId());
        t.replace("FellowEaters", "ID, AmountOfGuests, StudentNumber, MealID", i);
        t.close();
        db.close();
    }

    /**
     * Clear table
     */
    @Override
    public void clear() {
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM FellowEaters;");
        db.close();
    }
}
