package nl.mheijden.prog3app.model.data.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.data.SQLiteLocalDatabase;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class StudentDAO implements DAO<Student> {
    private SQLiteLocalDatabase db;
    public StudentDAO(SQLiteLocalDatabase db){
        this.db=db;
    }

    public ArrayList<Student> getAll(){
        ArrayList<Student> rs = new ArrayList<>();
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT StudentNumber, FirstName, Insertion, LastName, Email, PhoneNumber FROM Students ORDER BY StudentNumber", null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Student s = new Student();
                s.setStudentNumber(i.getString(0));
                s.setFirstname(i.getString(1));
                s.setInsertion(i.getString(2));
                s.setLastname(i.getString(3));
                s.setEmail(i.getString(4));
                s.setPhonenumber(i.getString(5));
                rs.add(s);
                i.moveToNext();
            }
        }
        Log.i("SQLiteLocalDatabase","Found "+rs.size()+" students");
        i.close();
        db.close();
        return rs;
    }
    public Student getOne(int id){
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Students WHERE StudentNumber ="+id, null);
        if(i.moveToFirst()){
            while(!i.isAfterLast()){
                Student s = new Student();
                s.setStudentNumber(i.getString(0));
                s.setFirstname(i.getString(1));
                s.setInsertion(i.getString(2));
                s.setLastname(i.getString(3));
                s.setEmail(i.getString(4));
                s.setPhonenumber(i.getString(5));
                i.close();
                db.close();
                return s;
            }
        }
        return null;
    }
    public void insertData(ArrayList<Student> data){
        Log.i("SQLiteLocalDatabase","Adding "+data.size()+" students");
        android.database.sqlite.SQLiteDatabase db = this.db.getReadableDatabase();
        for(Student student : data){
            insertOne(student);
        }
    }
    public void insertOne(Student object){
        android.database.sqlite.SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("StudentNumber", object.getstudentNumber());
        i.put("FirstName", object.getFirstname());
        i.put("Insertion", object.getInsertion());
        i.put("LastName", object.getLastname());
        i.put("Email", object.getEmail());
        i.put("PhoneNumber", object.getPhonenumber());
        if (t.insert("Students", "StudentNumber, Firstname, Insertion, Lastname, Email, PhoneNumber", i) != -1) {
            t.close();
        }
        t.close();
        db.close();
    }
    public void clear(){
        android.database.sqlite.SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Students");
        db.close();
    }
}
