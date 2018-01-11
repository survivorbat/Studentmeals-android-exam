package nl.mheijden.prog3app.model.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public class StudentDAO {
    private Database db;
    public StudentDAO(Database db){
        this.db=db;
    }

    public ArrayList<Student> getAll(){
        Log.i("Database","Retrieving all students");
        ArrayList<Student> rs = new ArrayList<>();
        SQLiteDatabase db = this.db.getReadableDatabase();
        Cursor i = db.rawQuery("SELECT * FROM Students ORDER BY LastName", null);
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
        Log.i("Database","Found "+rs.size()+" students");
        i.close();
        return rs;
    }
    public Student getStudent(String id){
        Log.i("Database","Retrieving student with ID "+id);
        SQLiteDatabase db = this.db.getReadableDatabase();
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
                return s;
            }
        }
        return null;
    }
    public boolean insertData(ArrayList<Student> data){
        Log.i("Database","Adding "+data.size()+" students");
        SQLiteDatabase db = this.db.getReadableDatabase();
        for(Student student : data){
            if(!addStudent(student)){
                return false;
            };
        }
        return true;
    }
    private boolean addStudent(Student student){
        SQLiteDatabase t = db.getWritableDatabase();
        ContentValues i = new ContentValues();
        i.put("StudentNumber", student.getstudentNumber());
        i.put("FirstName", student.getFirstname());
        i.put("Insertion", student.getInsertion());
        i.put("LastName", student.getLastname());
        i.put("Email", student.getEmail());
        i.put("PhoneNumber", student.getPhonenumber());
        if (t.insert("Students", "StudentNumber, Firstname, Insertion, Lastname, Email, PhoneNumber", i) != -1) {
            t.close();
            return true;
        }
        t.close();
        return false;
    }
    public void clear(){
        SQLiteDatabase db = this.db.getWritableDatabase();
        db.execSQL("DELETE FROM Students");
    }
}
