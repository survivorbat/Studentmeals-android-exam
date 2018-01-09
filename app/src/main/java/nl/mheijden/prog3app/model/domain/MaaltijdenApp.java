package nl.mheijden.prog3app.model.domain;

import android.content.Context;

import nl.mheijden.prog3app.model.data.MaaltijdDB;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdenApp {
    private MaaltijdDB db;
    private Context context;

    public MaaltijdenApp(Context context) {
        this.context = context;
        this.db=new MaaltijdDB(context);
    }
}
