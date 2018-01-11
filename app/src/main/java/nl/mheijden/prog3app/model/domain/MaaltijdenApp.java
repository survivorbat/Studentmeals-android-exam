package nl.mheijden.prog3app.model.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.ArrayList;

import nl.mheijden.prog3app.controller.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.data.DAOFactory;
import nl.mheijden.prog3app.model.data.DAOs.DAO;
import nl.mheijden.prog3app.model.data.SQLiteDatabase;
import nl.mheijden.prog3app.model.services.APIServices;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp implements APICallbacks {
    private Context context;
    private APIServices api;
    private DAOFactory daoFactory;

    private LoginControllerCallback callback;

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.api = new APIServices(context, this);
        this.daoFactory = new DAOFactory(new SQLiteDatabase(context));
    }

    public void refreshData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
        api.getStudents(sharedPreferences.getString("APITOKEN","0"));
        api.getMeals(sharedPreferences.getString("APITOKEN","0"));
        api.getFellowEaters(sharedPreferences.getString("APITOKEN","0"));
    }

    public ArrayList<Meal> getMeals() {
        return daoFactory.getMealDAO().getAll();
    }

    public ArrayList<Student> getStudents() {
        return daoFactory.getStudentDAO().getAll();
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
    public void loadStudents(ArrayList<Student> students) {
        DAO<Student> studentDAO = daoFactory.getStudentDAO();
        studentDAO.clear();
        studentDAO.insertData(students);
    }

    @Override
    public void loadMeals(ArrayList<Meal> meals) {
        DAO<Meal> mealDAO = daoFactory.getMealDAO();
        mealDAO.clear();
        mealDAO.insertData(meals);
    }

    @Override
    public void loadFellowEaters(ArrayList<FellowEater> fellowEaters) {
        DAO<FellowEater> fellowEaterDAO = daoFactory.getFellowEaterDAO();
        fellowEaterDAO.clear();
        fellowEaterDAO.insertData(fellowEaters);
    }
}
