package nl.mheijden.prog3app.model.domain;

import android.content.Context;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import nl.mheijden.prog3app.model.data.MealDB;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp {
    private MealDB db;
    private Context context;
    private ArrayList<Meal> meals;
    private ArrayList<Student> students;

    public void addDummy(){
        Student gerben = new Student("Gerben","","Droogers","g@droog.com","0293712947");
        students.add(gerben);
        Meal maaltijd = new Meal(0,"Spaghetti","Voedsame maaltijd gemaakt met allergieën","20-12-2017",gerben,2.30,10,"20:23","link",false);
        meals.add(maaltijd);
        maaltijd = new Meal(1,"Pizza","Voedsame maaltijd gemaakt met allergieën","20-12-2017",gerben,2.30,10,"20:23","link",false);
        meals.add(maaltijd);
        maaltijd = new Meal(2,"Pizza","Voedsame maaltijd gemaakt met allergieën","20-12-2017",gerben,2.30,10,"20:23","link",false);
        meals.add(maaltijd);
        maaltijd = new Meal(3,"Pizza","Voedsame maaltijd gemaakt met allergieën","20-12-2017",gerben,2.30,10,"20:23","link",false);
        meals.add(maaltijd);
    }

    public boolean refreshData(){
        return false;
    }

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.db=new MealDB(context);
        this.meals = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
