package nl.mheijden.prog3app.model.data.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.data.SQLiteDatabase;
import nl.mheijden.prog3app.model.domain.FellowEater;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class FellowEaterDAO implements DAO<FellowEater> {
    private SQLiteDatabase db;
    private MealDAO mealDAO;
    private StudentDAO studentDAO;

    public FellowEaterDAO(SQLiteDatabase db, StudentDAO studentDAO, MealDAO mealDAO){
        this.db=db;
        this.studentDAO=studentDAO;
        this.mealDAO=mealDAO;
    }
    public ArrayList<FellowEater> getAll(){
        ArrayList<FellowEater> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM FellowEaters ORDER BY ID", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                FellowEater s = new FellowEater();
                s.setId(i.getInt(0));
                s.setGuests(i.getInt(1));
                s.setStudent(studentDAO.getOne(Integer.parseInt(i.getString(2))));
                s.setMeal(mealDAO.getOne(i.getInt(3)));
                rs.add(s);
                i.moveToNext();
            }
        }
        Log.i("SQLiteDatabase","Found "+rs.size()+" students");
        i.close();
        return rs;
    }
    public FellowEater getOne(int id){
        return new FellowEater();
    }
    public void insertData(ArrayList<FellowEater> data){
        Log.i("SQLiteDatabase","Adding "+data.size()+" felloweaters");
        for(FellowEater fellowEater : data){
            insertOne(fellowEater);
        }
    }
    public void insertOne(FellowEater object){
        Log.i("SQLiteDatabase","Inserting fellowEater with id = "+object.getId());
        android.database.sqlite.SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", object.getId());
        i.put("AmountOfGuests", object.getAmount());
        i.put("StudentNumber",object.getStudent().getstudentNumber());
        i.put("MealID",object.getMeal().getId());
        if (t.insert("FellowEaters", "ID, AmountOfGuests, StudentNumber, MealID", i) != -1) {
            t.close();
        }
        t.close();
    }
    public void clear(){
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM FellowEaters");
    }
}
