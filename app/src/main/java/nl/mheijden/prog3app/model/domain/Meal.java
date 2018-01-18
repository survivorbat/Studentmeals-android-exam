package nl.mheijden.prog3app.model.domain;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Meal implements Serializable {
    /**
     * List of felloweaters that join this meal
     */
    private final ArrayList<FellowEater> felloweaters;
    /**
     * Identification number of the meal, imported from the database
     */
    private int id;
    /**
     * Name of the dish
     */
    private String dish;
    /**
     * Description
     */
    private String info;
    /**
     * Date, it's just a string since we only need to display it somewhere
     */
    private String date;
    /**
     * The person that cooks the meal
     */
    private Student chef;
    /**
     * The sacrifice that needs to be made in order to get food
     */
    private double price;
    /**
     * Maximum amount of people
     */
    private int maxFellowEaters;
    /**
     * Picture of the meal that is only used for uploading
     */
    private Bitmap picture;
    /**
     * If the cook eats
     */
    private boolean doesCookEat;

    public Meal(int id, String dish, String info, String date, Student chef, double price, int maxFellowEaters, boolean doesCookEat) {
        this.id = id;
        this.dish = dish;
        this.info = info;
        this.date = date;
        this.chef = chef;
        this.price = price;
        this.maxFellowEaters = maxFellowEaters;
        this.doesCookEat = doesCookEat;
        this.felloweaters = new ArrayList<>();
    }

    /**
     * Small constructor when used to only transfer the ID to the database
     *
     * @param id of the meal
     */
    public Meal(int id) {
        this.id = id;
        this.felloweaters = new ArrayList<>();
    }

    /**
     * Empty constructor for when setters are used
     */
    public Meal() {
        this.felloweaters = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Student getChef() {
        return chef;
    }

    public void setChef(Student chef) {
        this.chef = chef;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxFellowEaters() {
        return maxFellowEaters;
    }

    public void setMaxFellowEaters(int maxFellowEaters) {
        this.maxFellowEaters = maxFellowEaters;
    }

    public boolean isDoesCookEat() {
        return doesCookEat;
    }

    public void setDoesCookEat(boolean doesCookEat) {
        this.doesCookEat = doesCookEat;
    }

    public ArrayList<FellowEater> getFelloweaters() {
        return felloweaters;
    }

    public void addFellowEater(FellowEater fellowEater) {
        this.felloweaters.add(fellowEater);
    }

    /**
     * @return amount of people that eat, also check if the chef eats as well
     */
    public int getAmountOfEaters() {
        int rs = 0;
        if (doesCookEat) rs++;
        for (FellowEater e : felloweaters) {
            rs += e.getAmount();
        }
        return rs;
    }

    /**
     * The students that belong to the felloweaters
     *
     * @return an arraylist of students that join the meal
     */
    public ArrayList<Student> getStudents() {
        ArrayList<Student> rs = new ArrayList<>();
        for (FellowEater e : felloweaters) {
            rs.add(e.getStudent());
        }
        return rs;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
