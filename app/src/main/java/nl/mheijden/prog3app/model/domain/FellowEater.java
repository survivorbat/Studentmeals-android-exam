package nl.mheijden.prog3app.model.domain;

import java.io.Serializable;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class FellowEater implements Serializable {
    private int id;
    private int student;
    private int guests;
    private int meal;

    public FellowEater(int id, int student, int guests, int meal) {
        this.id = id;
        this.student = student;
        this.guests = guests;
        this.meal = meal;
    }

    public FellowEater(){

    }

    public int getAmount() {
        return ++guests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
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
