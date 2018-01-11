package nl.mheijden.prog3app.model.Callbacks;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 10-1-2018.
 */

public interface APICallbacks {
    void loadStudents(ArrayList<Student> students);
    void loadMeals(ArrayList<Meal> meals);
    void loadFellowEaters(ArrayList<FellowEater> fellowEaters);
    void loginCallback(String response);
}
