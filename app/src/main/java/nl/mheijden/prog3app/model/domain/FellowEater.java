package nl.mheijden.prog3app.model.domain;

import java.io.Serializable;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 * FellowEater domain class
 */

public class FellowEater implements Serializable {
    /**
     * Identification number
     */
    private int id;
    /**
     * Student that this FellowEater belongs to
     */
    private Student student;
    /**
     * Amount of guests student takes with
     */
    private int guests;
    /**
     * The meal that the felloweater attentds
     */
    private Meal meal;

    /**
     * Constructor for quickly creating an object
     *
     * @param id      of the felloweater, imported from the mysql database
     * @param student that this felloweater belongs to
     * @param guests  that come with the student
     * @param meal    that this felloweater is about
     */
    public FellowEater(int id, Student student, int guests, Meal meal) {
        this.id = id;
        this.student = student;
        this.guests = guests;
        this.meal = meal;
    }

    /**
     * Empty constructor since the DAO's use setters
     */
    public FellowEater() {

    }

    /**
     * @return number of guest + the student
     */
    int getAmount() {
        return guests + 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

}
