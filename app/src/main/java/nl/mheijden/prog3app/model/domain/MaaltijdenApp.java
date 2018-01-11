package nl.mheijden.prog3app.model.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


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

    private StudentDAO studentDAO;
    private MealDAO mealDAO;
    private FellowEaterDAO fellowEaterDAO;

    private LoginControllerCallback callback;

    public ArrayList<FellowEater> getFellowEaters() {
        return fellowEaterDAO.getAll();
    }

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.db=new Database(context);

        this.api = new APIServices(context, this);
        this.studentDAO = new StudentDAO(db);
        this.mealDAO = new MealDAO(db,studentDAO);
        this.fellowEaterDAO = new FellowEaterDAO(db,studentDAO,mealDAO);
    }

    public void refreshData(){
        api.getStudents();
        api.getFellowEaters();
        api.getMeals();
    }

    public ArrayList<Meal> getMeals() {
        return mealDAO.getAll();
    }

    public ArrayList<Student> getStudents() {
        return studentDAO.getAll();
    }

    public void login(Context context, String studentNumber, String password, LoginControllerCallback callback){
        this.callback = callback;
        api.login(context, studentNumber, password);
    }

    public void loginCallback(String response){
        if(response.equals("error")){
            callback.login("error");
        } else {
            Log.i("API",response);
            SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("APITOKEN",response);
            callback.login("success");
            editor.apply();
        }
    }

    @Override
    public void invalidToken() {

    }

    @Override
    public void loadStudents(ArrayList<Student> students) {
        studentDAO.clear();
        studentDAO.insertData(students);
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
