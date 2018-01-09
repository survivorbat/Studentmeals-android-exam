package nl.mheijden.prog3app.model.domain;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MeeEter {
    private int id;
    private Bewoner bewoner;
    private int gasten;

    public int getAantal(){
        return ++gasten;
    }
}
