package nl.mheijden.prog3app.model.data;

import nl.mheijden.prog3app.model.data.DAOs.FellowEaterDAO;
import nl.mheijden.prog3app.model.data.DAOs.MealDAO;
import nl.mheijden.prog3app.model.data.DAOs.StudentDAO;

/**
 * Gemaakt door Maarten van der Heijden on 11-1-2018.
 */

public class DAOFactory {
    private SQLiteDatabase SQLiteDatabase;
    public DAOFactory(SQLiteDatabase SQLiteDatabase){
        this.SQLiteDatabase = SQLiteDatabase;
    }
    public StudentDAO getStudentDAO(){
        return new StudentDAO(SQLiteDatabase);
    }
    public MealDAO getMealDAO(){
        return new MealDAO(SQLiteDatabase,getStudentDAO());
    }
    public FellowEaterDAO getFellowEaterDAO(){
        return new FellowEaterDAO(SQLiteDatabase,getStudentDAO(),getMealDAO());
    }
}
