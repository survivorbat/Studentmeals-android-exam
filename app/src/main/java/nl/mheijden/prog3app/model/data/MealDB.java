package nl.mheijden.prog3app.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Gemaakt door Maarten van der Heijden on 1-1-2018.
 */

public class MealDB extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "Prog3LocalDB";
    private static int DATABASE_VERSION = 1;

    public MealDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("CREATE TABLE ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
