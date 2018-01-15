package nl.mheijden.prog3app.model.Callbacks;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public interface APICallbacks {

    void loadMeals(ArrayList<Meal> meals);
    void addedMeal(boolean result);
    void removedMeal(boolean result);

    void loginCallback(String response);
    void invalidToken();

    void loadStudents(ArrayList<Student> students);
    void addedStudent(boolean result);

    void loadFellowEaters(ArrayList<FellowEater> fellowEaters);
    void removedFellowEater(boolean result);
    void addedFellowEater(boolean result);
}
