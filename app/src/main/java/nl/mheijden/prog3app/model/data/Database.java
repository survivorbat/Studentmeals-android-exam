package nl.mheijden.prog3app.model.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Gemaakt door Maarten van der Heijden on 1-1-2018.
 */

public class Database extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "StudentMaaltijden";
    private static int DATABASE_VERSION = 2;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS`FellowEaters` (\n" +
                "  `ID` int(11) NOT NULL,\n" +
                "  `AmountOfGuests` int(11) NOT NULL DEFAULT '0',\n" +
                "  `StudentNumber` int(11) NOT NULL,\n" +
                "  `MealID` int(11) NOT NULL\n" +
                ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Meals` (\n" +
                "  `ID` int(11) NOT NULL,\n" +
                "  `Dish` varchar(100) NOT NULL,\n" +
                "  `DateTime` datetime NOT NULL,\n" +
                "  `Info` text NOT NULL,\n" +
                "  `ChefID` int(11) NOT NULL,\n" +
                "  `Picture` longblob NOT NULL,\n" +
                "  `Price` decimal(10,0) NOT NULL,\n" +
                "  `MaxFellowEaters` int(11) NOT NULL,\n" +
                "  `DoesCookEat` tinyint(1) NOT NULL DEFAULT '1'\n" +
                ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `Students` (\n" +
                "  `StudentNumber` int(11) NOT NULL,\n" +
                "  `FirstName` varchar(50) NOT NULL,\n" +
                "  `Insertion` varchar(50) DEFAULT NULL,\n" +
                "  `LastName` varchar(50) NOT NULL,\n" +
                "  `Email` varchar(50) NOT NULL,\n" +
                "  `PhoneNumber` varchar(30) DEFAULT NULL\n" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("INFO", "Local database updated");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Students; DROP TABLE IF EXISTS Meals; DROP TABLE IF EXISTS FellowEaters");
        this.onCreate(sqLiteDatabase);
    }

    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Students",null,null);
        db.delete("Meals",null,null);
        db.delete("FellowEaters",null,null);
    }
}
