package nl.mheijden.prog3app.model.Callbacks;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public interface APICallbacks {

    /**
     * @param meals to be inserted into the application
     */
    void loadMeals(ArrayList<Meal> meals);

    /**
     * @param result dictates whether the meal was added succesfully or not
     */
    void addedMeal(boolean result);

    /**
     * @param result dictates whether meal was removed sucesfully or not
     */
    void removedMeal(boolean result);

    /**
     * @param response dictates what the response of the server was concerning the login attempt
     */
    void loginCallback(String response);

    /**
     * If given token was invaalid this will be called
     */
    @SuppressWarnings({"EmptyMethod", "unused"})
    void invalidToken();

    /**
     * @param students that need to be added to the application
     */
    void loadStudents(ArrayList<Student> students);

    /**
     * @param result dictates whether the student was added or not
     */
    void addedStudent(boolean result);

    /**
     * @param result dictates wether the student was changed or not
     */
    void changedStudent(boolean result);

    /**
     * @param fellowEaters that need  to be added to the application
     */
    void loadFellowEaters(ArrayList<FellowEater> fellowEaters);

    /**
     * @param result dictates wether the felloweater was removed succesfully or not
     */
    void removedFellowEater(boolean result);

    /**
     * @param result dictates wether the felloweater was added succesfully or not
     */
    void addedFellowEater(boolean result);
}
