package nl.mheijden.prog3app.model.domain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.activities.MealsActivity;
import nl.mheijden.prog3app.controller.callbacks.JoinControllerCallback;
import nl.mheijden.prog3app.controller.callbacks.LeaveControllerCallback;
import nl.mheijden.prog3app.controller.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.controller.callbacks.NewMealControllerCallback;
import nl.mheijden.prog3app.controller.callbacks.RegisterControllerCallback;
import nl.mheijden.prog3app.controller.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.data.DAOFactory;
import nl.mheijden.prog3app.model.data.DAOs.DAO;
import nl.mheijden.prog3app.model.data.SQLiteLocalDatabase;
import nl.mheijden.prog3app.model.services.APIServices;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp implements APICallbacks {
    private Context context;
    private APIServices api;
    private DAOFactory daoFactory;
    private LoginControllerCallback loginCallback;
    private ReloadCallback reloadCallback;
    private RegisterControllerCallback registerCallback;
    private JoinControllerCallback joinControllerCallback;
    private LeaveControllerCallback leaveControllerCallback;
    private NewMealControllerCallback newMealControllerCallback;
    private String userID;

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.daoFactory = new DAOFactory(new SQLiteLocalDatabase(context));
        this.api = new APIServices(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("USERID", "");
    }

    public Student getUser() {
        return daoFactory.getStudentDAO().getOne(Integer.parseInt(userID));
    }

    public ArrayList<Student> getStudents() {
        return daoFactory.getStudentDAO().getAll();
    }

    public ArrayList<Meal> getMeals() {
        return daoFactory.getMealDAO().getAll();
    }

    public void login(String studentNumber, String password, LoginControllerCallback callback){
        this.loginCallback = callback;
        api.login(context, studentNumber, password);
        userID = studentNumber;
    }

    public void loginCallback(String response){
        Log.i("API",response);
        if(response.equals("errorconn") || response.equals("errorwrong") || response.equals("errorobj")){
            loginCallback.login(response);
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userdata",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("APITOKEN",response);
            editor.putString("USERID", userID);
            loginCallback.login("success");
            editor.apply();
        }
    }

    public void addFellowEater(FellowEater fellowEater, JoinControllerCallback joinControllerCallback){
        this.joinControllerCallback=joinControllerCallback;
        api.addFellowEater(fellowEater);
    }

    @Override
    public void invalidToken() {

    }

    @Override
    public void addedStudent(boolean result) {
        registerCallback.newStudentAdded(result);
    }

    @Override
    public void removedFellowEater(boolean result) {
        leaveControllerCallback.onLeaveComplete(result);
    }

    public void addMeal(Meal meal, NewMealControllerCallback newMealControllerCallback){
        this.newMealControllerCallback=newMealControllerCallback;
        api.addMaaltijd(meal);
    }

    @Override
    public void addedFellowEater(boolean result) {
        joinControllerCallback.onJoinComplete(result);
    }

    public void deleteMeal(Meal meal){
        api.deleteMeal(meal);
    }

    public void deleteFellowEater(FellowEater fellowEater, LeaveControllerCallback leaveControllerCallback){
        this.leaveControllerCallback = leaveControllerCallback;
        api.deleteFellowEater(fellowEater);
    }

    public void register(Student newStudent, RegisterControllerCallback callback){
        this.registerCallback = callback;
        api.addStudent(newStudent);
    }

    public void reloadStudents(ReloadCallback callback){
        this.reloadCallback=callback;
        api.getStudents();
    }

    public void reloadMeals(ReloadCallback callback){
        this.reloadCallback=callback;
        api.getMeals();
        api.getFellowEaters();
    }

    @Override
    public void loadStudents(ArrayList<Student> students) {
        DAO<Student> studentDAO = daoFactory.getStudentDAO();
        studentDAO.clear();
        studentDAO.insertData(students);
        Log.i("API>SQLITE","Importing "+students.size()+" students");
        reloadCallback.reloaded(true);
    }

    @Override
    public void loadMeals(ArrayList<Meal> meals) {
        DAO<Meal> mealDAO = daoFactory.getMealDAO();
        mealDAO.clear();
        mealDAO.insertData(meals);
        Log.i("API>SQLITE","Importing "+meals.size()+" meals");
        reloadCallback.reloaded(true);
    }

    @Override
    public void addedMeal(boolean result) {
        newMealControllerCallback.addedMeal(result);
    }

    @Override
    public void removedMeal(boolean result) {

    }

    @Override
    public void loadFellowEaters(ArrayList<FellowEater> fellowEaters) {
        DAO<FellowEater> fellowEaterDAO = daoFactory.getFellowEaterDAO();
        fellowEaterDAO.clear();
        fellowEaterDAO.insertData(fellowEaters);
        Log.i("API>SQLITE","Importing "+fellowEaters.size()+" felloweaters");
        reloadCallback.reloaded(true);
    }
}
