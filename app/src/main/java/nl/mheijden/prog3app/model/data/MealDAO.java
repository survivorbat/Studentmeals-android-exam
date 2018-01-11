package nl.mheijden.prog3app.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class MealDAO {
    private Database db;
    private StudentDAO studentDAO;
    public MealDAO(Database db, StudentDAO studentDAO){
        this.db=db;
        this.studentDAO = studentDAO;
    }

    public ArrayList<Meal> getAll(){
        Log.i("Database","Retrieving all students");
        ArrayList<Meal> rs = new ArrayList<>();
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Meals ORDER BY DateTime", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                s.setDate(i.getString(2));
                s.setInfo(i.getString(3));
                s.setChefID(studentDAO.getStudent(i.getString(4)));
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
        Log.i("Database","Found "+rs.size()+" meals");
        i.close();
        return rs;
    }
    public Meal getMeal(int id){
        Log.i("Database","Retrieving Meal with ID "+id);
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Meals WHERE ID ="+id, null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Meal s = new Meal();
                s.setId(i.getInt(0));
                s.setDish(i.getString(1));
                s.setDate(i.getString(2));
                s.setInfo(i.getString(3));
                s.setChefID(studentDAO.getStudent(i.getString(4)));
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
    public boolean insertData(ArrayList<Meal> data){
        Log.i("Database","Adding "+data.size()+" students");
        SQLiteDatabase db = this.db.getReadableDatabase();
        for(Meal meal : data){
            if(!addStudent(meal)){
                return false;
            };
        }
        return true;
    }
    private boolean addStudent(Meal meal){
        Log.i("Database","Inserting meals "+meal.getDish());
        SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", meal.getId());
        i.put("Dish", meal.getDish());
        i.put("DateTime", meal.getDate());
        i.put("Info", meal.getInfo());
        i.put("ChefID", meal.getChefID().getstudentNumber());
        i.put("Picture", meal.getInfo());
        i.put("Price", meal.getPrice());
        i.put("MaxFellowEaters", meal.getMax());
        i.put("DoesCookEat", meal.isDoesCookEat());
        if (t.insert("Meals", "ID, Dish, DateTime, Info, ChefID, Picture, Price, MaxFellowEaters, DoesCookEat", i) != -1) {
            t.close();
            return true;
        }
        t.close();
        return false;
    }
    public void clear(){
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Meals");
    }
}
