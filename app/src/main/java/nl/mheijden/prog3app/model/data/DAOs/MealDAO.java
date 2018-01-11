package nl.mheijden.prog3app.model.data.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.data.SQLiteLocalDatabase;
import nl.mheijden.prog3app.model.domain.Meal;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class MealDAO implements DAO<Meal> {
    private SQLiteLocalDatabase db;
    private StudentDAO studentDAO;
    public MealDAO(SQLiteLocalDatabase db, StudentDAO studentDAO){
        this.db=db;
        this.studentDAO = studentDAO;
    }

    public ArrayList<Meal> getAll(){
        Log.i("SQLiteLocalDatabase","Retrieving all students");
        ArrayList<Meal> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Meals ORDER BY DateTime", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                s.setDate(i.getString(2));
                s.setInfo(i.getString(3));
                s.setChefID(studentDAO.getOne(Integer.parseInt(i.getString(4))));
                s.setImageUrl(i.getString(5));
                s.setPrice(i.getDouble(6));
                s.setMax(i.getInt(7));
                if(i.getInt(8)==1){
                    s.setDoesCookEat(true);
                } else {
                    s.setDoesCookEat(false);
                }
                rs.add(s);
                i.moveToNext();
            }
        }
        Log.i("SQLiteLocalDatabase","Found "+rs.size()+" meals");
        i.close();
        return rs;
    }
    public Meal getOne(int id){
        Log.i("SQLiteLocalDatabase","Retrieving Meal with ID "+id);
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Meals WHERE ID ="+id, null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                s.setDate(i.getString(2));
                s.setInfo(i.getString(3));
                s.setChefID(studentDAO.getOne(Integer.parseInt(i.getString(4))));
                s.setImageUrl(i.getString(5));
                s.setPrice(i.getDouble(6));
                s.setMax(i.getInt(7));
                if(i.getInt(8)==1){
                    s.setDoesCookEat(true);
                } else {
                    s.setDoesCookEat(false);
                }
                return s;
            }
        }
        return null;
    }
    public void insertData(ArrayList<Meal> data){
        Log.i("SQLiteLocalDatabase","Adding "+data.size()+" students");
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        for(Meal meal : data){
            insertOne(meal);
        }
    }
    public void insertOne(Meal object){
        Log.i("SQLiteLocalDatabase","Inserting meals "+object.getDish());
        android.database.sqlite.SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", object.getId());
        i.put("Dish", object.getDish());
        i.put("DateTime", object.getDate());
        i.put("Info", object.getInfo());
        i.put("ChefID", object.getChefID().getstudentNumber());
        i.put("Picture", object.getInfo());
        i.put("Price", object.getPrice());
        i.put("MaxFellowEaters", object.getMax());
        i.put("DoesCookEat", object.isDoesCookEat());
        if (t.insert("Meals", "ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat", i) != -1) {
            t.close();
        }
        t.close();
    }
    public void clear(){
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Meals");
    }
}
