package nl.mheijden.prog3app.model.domain;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.ArrayList;

import nl.mheijden.prog3app.controller.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.data.Database;
import nl.mheijden.prog3app.model.data.FellowEaterDAO;
import nl.mheijden.prog3app.model.data.MealDAO;
import nl.mheijden.prog3app.model.data.StudentDAO;
import nl.mheijden.prog3app.model.services.APIServices;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp implements APICallbacks {
    private Database db;
    private Context context;
    private APIServices api;
    private ArrayList<Meal> meals;
    private ArrayList<Student> students;

    private StudentDAO studentDAO;
    private MealDAO mealDAO;
    private FellowEaterDAO fellowEaterDAO;

    private LoginControllerCallback callback;

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.db=new Database(context);
        this.meals = new ArrayList<>();
        this.students = new ArrayList<>();

        this.api = new APIServices(context, this);
        this.studentDAO = new StudentDAO(db);
        this.mealDAO = new MealDAO(db,studentDAO);
        this.fellowEaterDAO = new FellowEaterDAO(db,studentDAO,mealDAO);
    }

    public void refreshData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        api.getStudents(sharedPreferences.getString("APITOKEN","0"));
        api.getMeals(sharedPreferences.getString("APITOKEN","0"));
        api.getFellowEaters(sharedPreferences.getString("APITOKEN","0"));
        System.out.println(sharedPreferences.getString("APITOKEN","0"));
    }

    public boolean loadData(){

        return false;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public boolean login(Context context, String studentNumber, String password, LoginControllerCallback callback){
        this.callback = callback;
        api.login(context, studentNumber, password);
        return false;
    }

    public void loginCallback(String response){
        if(response.equals("error")){
            System.out.println(response);
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("APITOKEN",response);
            editor.commit();
        }
    }

    @Override
    public void loadStudents(ArrayList<Student> students) {
        studentDAO.clear();
        studentDAO.insertData(students);
        for(Student i : students){
            System.out.println(i);
        }
    }

    @Override
    public void loadMeals(ArrayList<Meal> meals) {
        mealDAO.clear();
        mealDAO.insertData(meals);
    }

    @Override
    public void loadFellowEaters(ArrayList<FellowEater> fellowEaters) {
        fellowEaterDAO.clear();
        fellowEaterDAO.insertData(fellowEaters);
    }
}
