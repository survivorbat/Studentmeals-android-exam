package nl.mheijden.prog3app.model.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Meal implements Serializable {
    private int id;
    private String dish;
    private String info;
    private String date;
    private Student chefID;
    private double price;
    private int max;
    private byte[] imageUrl;
    private boolean doesCookEat;
    private ArrayList<FellowEater> felloweaters;

    public Meal(int id, String dish, String info, String date, Student chefID, double price, int max,  boolean doesCookEat) {
        this.id = id;
        this.dish = dish;
        this.info = info;
        this.date = date;
        this.chefID = chefID;
        this.price = price;
        this.max = max;
        this.doesCookEat = doesCookEat;
        this.felloweaters = new ArrayList<>();
    }

    public Meal(int id){this.id=id;}

    public Meal(){
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

    public boolean isDoesCookEat() {
        return doesCookEat;
    }

    public void setDoesCookEat(boolean doesCookEat) {
        this.doesCookEat = doesCookEat;
    }

    public ArrayList<FellowEater> getFelloweaters() {
        return felloweaters;
    }

    public void addFellowEater(FellowEater fellowEater){
        this.felloweaters.add(fellowEater);
    }

    public int getAmountOfEaters(){
        int rs=0;
        if(doesCookEat) rs++;
        for(FellowEater e : felloweaters){
            rs+=e.getAmount();
        }
        return rs;
    }

    public ArrayList<Student> getStudents(){
        ArrayList<Student> rs = new ArrayList<>();
        for(FellowEater e : felloweaters){
            rs.add(e.getStudent());
        }
        return rs;
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
                ", doesCookEat=" + doesCookEat +
                ", felloweaters=" + felloweaters +
                '}';
    }
}
