package nl.mheijden.prog3app.model.domain;

import java.io.Serializable;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class FellowEater implements Serializable {
    private int id;
    private Student student;
    private int guests;
    private Meal meal;

    public FellowEater(int id, Student student, int guests, Meal meal) {
        this.id = id;
        this.student = student;
        this.guests = guests;
        this.meal = meal;
    }

    public FellowEater(){

    }

    public int getAmount() {
        return guests+1;
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

    @Override
    public String toString() {
        return "FellowEater{" +
                "id=" + id +
                ", student=" + student +
                ", guests=" + guests +
                ", meal=" + meal +
                '}';
    }
}
