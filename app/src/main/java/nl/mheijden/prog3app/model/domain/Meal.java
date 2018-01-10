package nl.mheijden.prog3app.model.domain;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Meal {
    private int id;
    private String dish;
    private String info;
    private String date;
    private Student chefID;
    private double price;
    private int max;
    private String time;
    private String imageUrl;
    private boolean doesCookEat;
    private ArrayList<FellowEater> felloweaters;

    public Meal(int id, String dish, String info, String date, Student chefID, double price, int max, String time, String imageUrl, boolean doesCookEat) {
        this.id = id;
        this.dish = dish;
        this.info = info;
        this.date = date;
        this.chefID = chefID;
        this.price = price;
        this.max = max;
        this.imageUrl = imageUrl;
        this.doesCookEat = doesCookEat;
        this.time = time;
    }

    public Meal(){

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

    public Student getChefID() {
        return chefID;
    }

    public void setChefID(Student chefID) {
        this.chefID = chefID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public void setFelloweaters(ArrayList<FellowEater> felloweaters) {
        this.felloweaters = felloweaters;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dish='" + dish + '\'' +
                ", info='" + info + '\'' +
                ", date=" + date +
                ", chefID=" + chefID +
                ", price=" + price +
                ", max=" + max +
                ", time=" + time +
                ", imageUrl='" + imageUrl + '\'' +
                ", doesCookEat=" + doesCookEat +
                ", felloweaters=" + felloweaters +
                '}';
    }
}
