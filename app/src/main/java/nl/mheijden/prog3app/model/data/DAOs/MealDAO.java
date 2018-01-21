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

public class MealDAO implements DAO<Meal> {
    /**
     * Database object
     */
    private final SQLiteLocalDatabase db;

    /**
     * @param db object to send queries to
     */
    public MealDAO(SQLiteLocalDatabase db) {
        this.db = db;
    }

    /**
     * @return a list of Meals
     */
    public ArrayList<Meal> getAll() {
        ArrayList<Meal> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT ID, Dish, DateTime, Info, ChefID, FirstName, Insertion, LastName, Email,PhoneNumber, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals INNER JOIN Students ON Meals.ChefID = Students.StudentNumber WHERE DateTime >= Date('now') ORDER BY DateTime", null);
        if (i.moveToFirst()) {
            while (!i.isAfterLast()) {
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                try {
                    String[] date = i.getString(2).split("T");
                    s.setDate(date[0] + " " + date[1].substring(0, 8));
                } catch (ArrayIndexOutOfBoundsException e) {
                    s.setDate(i.getString(2));
                }
                s.setInfo(i.getString(3));
                s.setChef(new Student(i.getString(4), i.getString(5), i.getString(6), i.getString(7), i.getString(8), i.getString(9)));
                s.setPrice(i.getDouble(11));
                s.setMaxFellowEaters(i.getInt(12));
                s.setDoesCookEat(Boolean.parseBoolean(i.getInt(13) + ""));
                Cursor e = db.rawQuery("SELECT FellowEaters.ID, AmountOfGuests, Students.StudentNumber,FirstName, LastName, Insertion, Email, PhoneNumber FROM FellowEaters INNER JOIN Students ON Students.StudentNumber = FellowEaters.StudentNumber WHERE FellowEaters.MealID =" + s.getId(), null);
                if (e.moveToFirst()) {
                    while (!e.isAfterLast()) {
                        FellowEater f = new FellowEater(e.getInt(0), new Student(e.getString(2), e.getString(3), e.getString(4), e.getString(5), e.getString(6), e.getString(7)), e.getInt(1), s);
                        s.addFellowEater(f);
                        e.moveToNext();
                    }
                }
                e.close();
                rs.add(s);
                i.moveToNext();
            }
        }
        i.close();
        db.close();
        return rs;
    }

    /**
     * @param id of the object that has to be returned
     * @return one meal
     */
    public Meal getOne(int id) {
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT ID, Dish, Date, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals WHERE ID =" + id, null);
        if (i.moveToFirst()) {
            Meal s = new Meal();
            s.setId(i.getInt(0));
            s.setDish(i.getString(1));
            String[] date = i.getString(2).split("T");
            s.setDate(date[0] + " " + date[1].substring(0, 8));
            s.setInfo(i.getString(3));
            s.setChef(new Student(i.getString(4)));
            s.setPrice(i.getDouble(6));
            s.setMaxFellowEaters(i.getInt(7));
            if (i.getInt(8) == 1) {
                s.setDoesCookEat(true);
            } else {
                s.setDoesCookEat(false);
            }
            i.close();
            db.close();
            return s;
        }
        return null;
    }

    /**
     * @param data is a list of objects to call insertOne for
     */
    public void insertData(ArrayList<Meal> data) {
        SQLiteDatabase wd = db.getWritableDatabase();
        wd.execSQL("DELETE FROM Meals");
        ContentValues i = new ContentValues();
        wd.beginTransaction();
        try {
            for (Meal meal : data) {
                i.put("ID", meal.getId());
                i.put("Dish", meal.getDish());
                i.put("DateTime", meal.getDate());
                i.put("Info", meal.getInfo());
                i.put("ChefID", meal.getChef().getstudentNumber());
                i.put("Picture", meal.getInfo());
                i.put("Price", meal.getPrice());
                i.put("MaxFellowEaters", meal.getMaxFellowEaters());
                i.put("DoesCookEat", meal.isDoesCookEat());
                wd.insertOrThrow("Meals", "ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat", i);
            }
            wd.setTransactionSuccessful();
        } finally {
            wd.endTransaction();
            wd.close();
        }
    }
}
