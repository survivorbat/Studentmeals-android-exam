package nl.mheijden.prog3app.model.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import nl.mheijden.prog3app.controllers.callbacks.ChangeStudentCallback;
import nl.mheijden.prog3app.controllers.callbacks.DeleteMealControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;
import nl.mheijden.prog3app.controllers.callbacks.JoinControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.LeaveControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.NewMealControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.RegisterControllerCallback;
import nl.mheijden.prog3app.controllers.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.Callbacks.APICallbacks;
import nl.mheijden.prog3app.model.data.DAOFactory;
import nl.mheijden.prog3app.model.data.SQLiteLocalDatabase;
import nl.mheijden.prog3app.model.services.APIServices;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp implements APICallbacks {

    /**
     * Context of the activity
     */
    private final Context context;
    /**
     * APIServices object in order to retrieve data
     */
    private final APIServices api;
    /**
     * DAOfactory where the application will get its database access from
     */
    private final DAOFactory daoFactory;
    /**
     * Logincallback that sends ot the activity wether the login was successfully or not
     */
    private LoginControllerCallback loginCallback;
    /**
     * Reloadcallback that is called when a reload is complete
     */
    private ReloadCallback reloadCallback;
    /**
     * Notify the register activity
     */
    private RegisterControllerCallback registerCallback;
    /**
     * Callback to tell the activity user joined
     */
    private JoinControllerCallback joinControllerCallback;
    /**
     * Callback to tell the activity user left the meal
     */
    private LeaveControllerCallback leaveControllerCallback;
    /**
     * Callback to tell the activity a new meal is created
     */
    private NewMealControllerCallback newMealControllerCallback;
    /**
     * Callback to tell the activity a meal has been deleted
     */
    private DeleteMealControllerCallback deleteMealControllerCallback;
    /**
     * Callback to tell the activity a student has been changed
     */
    private ChangeStudentCallback changeStudentCallback;
    /**
     * Callback for a 401 response
     */
    private InvalidTokenCallback invalidTokenCallback;
    /**
     * The studentnumber of the user that is currently in the activity
     */
    private String userID;




    /* Constructor *?

    /**
     * @param context of the current activity
     * @param invalidTokenCallback that is used incase a 401 error comes up
     */
    public MaaltijdenApp(Context context, InvalidTokenCallback invalidTokenCallback) {
        this.context = context;
        this.daoFactory = new DAOFactory(new SQLiteLocalDatabase(context));
        this.api = new APIServices(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("USERID", "");
        this.invalidTokenCallback = invalidTokenCallback;
    }

    /**
     * @param context of the current activity
     */
    public MaaltijdenApp(Context context) {
        this.context = context;
        this.daoFactory = new DAOFactory(new SQLiteLocalDatabase(context));
        this.api = new APIServices(context, this);
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("USERID", "");
    }


    /* Get current user */

    /**
     * @return the user that is currently using the application
     */
    public Student getUser() {
        return daoFactory.getStudentDAO().getOne(Integer.parseInt(userID));
    }




    /* Database methods */

    /**
     * @return all the students from the database
     */
    public ArrayList<Student> getStudents() {
        return daoFactory.getStudentDAO().getAll();
    }

    /**
     * @return all the meals from the database
     */
    public ArrayList<Meal> getMeals() {
        return daoFactory.getMealDAO().getAll();
    }




    /* Login method */

    /**
     * @param studentNumber that identifies the student
     * @param password      that verifies the student
     * @param callback      that is used in order to let the activity know what's up
     */
    public void login(String studentNumber, String password, LoginControllerCallback callback) {
        this.loginCallback = callback;
        api.login(studentNumber, password);
        userID = studentNumber;
    }




    /* Login callback */

    /**
     * @param response dictates what the response of the server was concerning the login attempt
     */
    public void loginCallback(String response) {
        Log.i("API", response);
        if (response.equals("errorconn") || response.equals("errorwrong") || response.equals("errorobj")) {
            loginCallback.login(response);
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("APITOKEN", response);
            editor.putString("USERID", userID);
            loginCallback.login("success");
            editor.apply();
        }
    }




    /* Add methods */

    /**
     * @param fellowEater            that needs to be added
     * @param joinControllerCallback that let's the controller know what's up
     */
    public void addFellowEater(FellowEater fellowEater, JoinControllerCallback joinControllerCallback) {
        this.joinControllerCallback = joinControllerCallback;
        api.addFellowEater(fellowEater);
    }

    /**
     * @param meal                      that needs to be added
     * @param newMealControllerCallback is stored in order to let the activity know what's up
     */
    public void addMeal(Meal meal, NewMealControllerCallback newMealControllerCallback) {
        this.newMealControllerCallback = newMealControllerCallback;
        api.addMaaltijd(meal);
    }

    /**
     * @param newStudent that needs to be added
     * @param callback   that is stored to let the activity know what's up
     */
    public void register(Student newStudent, RegisterControllerCallback callback) {
        this.registerCallback = callback;
        api.addStudent(newStudent);
    }




    /* Add method callbacks */

    /**
     * @param result dictates wether the felloweater was added succesfully or not
     */
    @Override
    public void addedFellowEater(boolean result) {
        joinControllerCallback.onJoinComplete(result);
    }

    /**
     * @param result dictates whether the meal was added succesfully or not
     */
    @Override
    public void addedMeal(boolean result) {
        newMealControllerCallback.addedMeal(result);
    }

    /**
     * @param result dictates whether the student was added or not
     */
    @Override
    public void addedStudent(boolean result) {
        registerCallback.newStudentAdded(result);
    }




    /* Delete methods */

    /**
     * @param meal                         that needs to be deleted
     * @param deleteMealControllerCallback that is stored in order to let the activity know what's up
     */
    public void deleteMeal(Meal meal, DeleteMealControllerCallback deleteMealControllerCallback) {
        this.deleteMealControllerCallback = deleteMealControllerCallback;
        api.deleteMeal(meal);
    }

    /**
     * @param fellowEater             that needs to be deleted
     * @param leaveControllerCallback that is stored to let the activity know what's up
     */
    public void deleteFellowEater(FellowEater fellowEater, LeaveControllerCallback leaveControllerCallback) {
        this.leaveControllerCallback = leaveControllerCallback;
        api.deleteFellowEater(fellowEater);
    }




    /* Delete method callbacks */

    /**
     * @param result dictates whether meal was removed sucesfully or not
     */
    @Override
    public void removedMeal(boolean result) {
        deleteMealControllerCallback.onDeleteMealComplete(result);
    }

    /**
     * @param result dictates wether the felloweater was removed succesfully or not
     */
    @Override
    public void removedFellowEater(boolean result) {
        leaveControllerCallback.onLeaveComplete(result);
    }




    /* Put methods */

    /**
     * @param student that needs to be changed
     * @param changeStudentcallback to be called when student is changed or not
     */
    public void changeStudent(Student student, ChangeStudentCallback changeStudentcallback) {
        this.changeStudentCallback = changeStudentcallback;
        api.changeStudent(student);
        if (student.getImage() != null) {
            api.changeStudentImage(student);
        }
    }




    /* Put method callbacks*/

    /**
     * @param result dictates wether the student was changed or not
     */
    @Override
    public void changedStudent(boolean result) {
        changeStudentCallback.onUserChanged(result);
    }




    /* Reload Database from api */

    /**
     * @param callback for the activity
     */
    public void reloadStudents(ReloadCallback callback) {
        this.reloadCallback = callback;
        api.getStudents();
    }

    /**
     * @param callback for the activity
     */
    public void reloadMeals(ReloadCallback callback) {
        this.reloadCallback = callback;
        api.getMeals();
        api.getFellowEaters();
    }




    /* API callbacks */

    /**
     * @param students that need to be added to the application
     */
    @Override
    public void loadStudents(ArrayList<Student> students) {
        if (students != null && students.size() > 0) {
            daoFactory.getStudentDAO().insertData(students);
            reloadCallback.reloaded(true);
        } else {
            reloadCallback.reloaded(false);
        }
    }

    /**
     * @param meals to be inserted into the application
     */
    @Override
    public void loadMeals(ArrayList<Meal> meals) {
        if (meals != null && meals.size() > 0) {
            daoFactory.getMealDAO().insertData(meals);
            reloadCallback.reloaded(true);
        } else {
            reloadCallback.reloaded(false);
        }
    }

    /**
     * @param fellowEaters that need  to be added to the application
     */
    @Override
    public void loadFellowEaters(ArrayList<FellowEater> fellowEaters) {
        if (fellowEaters != null && fellowEaters.size() > 0) {
            daoFactory.getFellowEaterDAO().insertData(fellowEaters);
            reloadCallback.reloaded(true);
        } else {
            reloadCallback.reloaded(false);
        }
    }




    /* Invalid token callback */

    /**
     * Called in case the token has expired
     */
    @Override
    public void invalidToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGGEDIN", false);
        editor.apply();
        invalidTokenCallback.invalidToken();
    }
}
