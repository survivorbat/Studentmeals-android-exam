package nl.mheijden.prog3app.model.domain;

import android.graphics.Bitmap;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Maaltijd {
    private int id;
    private String gerecht;
    private Date datum;
    private Bewoner kok;
    private double prijs;
    private int max_meeeters;
    private Time tijd;
    private String afbeelding;

    private ArrayList<MeeEter> meeeters;
}
