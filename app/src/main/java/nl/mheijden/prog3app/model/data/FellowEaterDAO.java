package nl.mheijden.prog3app.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class FellowEaterDAO {
    private Database db;
    private MealDAO mealDAO;
    private StudentDAO studentDAO;

    public FellowEaterDAO(Database db, StudentDAO studentDAO, MealDAO mealDAO){
        this.db=db;
        this.studentDAO=studentDAO;
        this.mealDAO=mealDAO;
    }
    public ArrayList<FellowEater> getAll(){
        ArrayList<FellowEater> rs = new ArrayList<>();
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM FellowEaters ORDER BY ID", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                FellowEater s = new FellowEater();
                s.setId(i.getInt(0));
                s.setGuests(i.getInt(1));
                s.setStudent(studentDAO.getStudent(i.getString(2)));
                s.setMeal(mealDAO.getMeal(i.getInt(3)));
                rs.add(s);
                i.moveToNext();
            }
        }
        Log.i("Database","Found "+rs.size()+" students");
        i.close();
        return rs;
    }
    public boolean insertData(ArrayList<FellowEater> data){
        Log.i("Database","Adding "+data.size()+" felloweaters");
        for(FellowEater fellowEater : data){
            if(!(addFellowEater(fellowEater))){
                return false;
            };
        }
        return true;
    }
    private boolean addFellowEater(FellowEater fellowEater){
        Log.i("Database","Inserting fellowEater with id = "+fellowEater.getId());
        SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("ID", fellowEater.getId());
        i.put("AmountOfGuests", fellowEater.getAmount());
        i.put("StudentNumber",fellowEater.getStudent().getstudentNumber());
        i.put("MealID",fellowEater.getMeal().getId());
        if (t.insert("FellowEaters", "ID, AmountOfGuests, StudentNumber, MealID", i) != -1) {
            t.close();
            return true;
        }
        t.close();
        return false;
    }
    public void clear(){
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM FellowEaters");
    }
}
