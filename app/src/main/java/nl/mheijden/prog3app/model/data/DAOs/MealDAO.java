package nl.mheijden.prog3app.model.data.DAOs;

import android.content.ContentValues;
import android.database.Cursor;

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
    private SQLiteLocalDatabase db;

    /**
     * @param db object to send queries to
     */
    public MealDAO(SQLiteLocalDatabase db) {
        this.db=db;
    }

    /**
     * @return a list of Meals
     */
    public ArrayList<Meal> getAll(){
        ArrayList<Meal> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT ID, Dish, DateTime, Info, ChefID, FirstName, Insertion, LastName, Email,PhoneNumber, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals LEFT OUTER JOIN Students ON Meals.ChefID = Students.StudentNumber WHERE DateTime >= Date('now') ORDER BY DateTime", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                try {
                    String[] date = i.getString(2).split("T");
                    s.setDate(date[0] + " " + date[1].substring(0, 8));
                }
                catch (ArrayIndexOutOfBoundsException e){
                    s.setDate(i.getString(2));
                }
                s.setInfo(i.getString(3));
                s.setChef(new Student(i.getString(4),i.getString(5),i.getString(6),i.getString(7),i.getString(8),i.getString(9)));
                s.setPrice(i.getDouble(11));
                s.setMaxFellowEaters(i.getInt(12));
                s.setDoesCookEat(Boolean.parseBoolean(i.getInt(13) + ""));
                Cursor e = db.rawQuery("SELECT FellowEaters.ID, AmountOfGuests, Students.StudentNumber,FirstName, LastName, Insertion, Email, PhoneNumber FROM FellowEaters INNER JOIN Students ON Students.StudentNumber = FellowEaters.StudentNumber WHERE FellowEaters.MealID ="+s.getId(), null);
                if(e.moveToFirst()){
                    while(!e.isAfterLast()){
                        FellowEater f = new FellowEater();
                        f.setMeal(s);
                        f.setId(e.getInt(0));
                        f.setGuests(e.getInt(1));
                        f.setStudent(new Student(e.getString(2),e.getString(3),e.getString(4),e.getString(5),e.getString(6),e.getString(7)));
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
        return rs;
    }

    /**
     * @param id of the object that has to be returned
     * @return one meal
     */
    public Meal getOne(int id){
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT ID, Dish, Date, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat FROM Meals WHERE ID ="+id, null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                String[] date = i.getString(2).split("T");
                s.setDate(date[0]+" "+date[1].substring(0,8));
                s.setInfo(i.getString(3));
                s.setChef(new Student(i.getString(4)));
                s.setPrice(i.getDouble(6));
                s.setMaxFellowEaters(i.getInt(7));
                if(i.getInt(8)==1){
                    s.setDoesCookEat(true);
                } else {
                    s.setDoesCookEat(false);
                }
                i.close();
                db.close();
                return s;
            }
        }
        return null;
    }

    /**
     * @param data is a list of objects to call insertOne for
     */
    public void insertData(ArrayList<Meal> data){
        for(Meal meal : data){
            insertOne(meal);
        }
    }

    /**
     * @param object you want to insert into the database
     */
    public void insertOne(Meal object){
        android.database.sqlite.SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", object.getId());
        i.put("Dish", object.getDish());
        i.put("DateTime", object.getDate());
        i.put("Info", object.getInfo());
        i.put("ChefID", object.getChef().getstudentNumber());
        i.put("Picture", object.getInfo());
        i.put("Price", object.getPrice());
        i.put("MaxFellowEaters", object.getMaxFellowEaters());
        i.put("DoesCookEat", object.isDoesCookEat());
        if(t.insert("Meals", "ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat", i)==0){
            System.out.println("Something went wrong importing "+object.getDish());
        };
        t.close();
        db.close();
    }

    /**
     * Delete all the rows
     */
    public void clear(){
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Meals");
        db.close();
    }
}
