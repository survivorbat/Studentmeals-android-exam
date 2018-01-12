package nl.mheijden.prog3app.model.data;

import nl.mheijden.prog3app.model.data.DAOs.FellowEaterDAO;
import nl.mheijden.prog3app.model.data.DAOs.MealDAO;
import nl.mheijden.prog3app.model.data.DAOs.StudentDAO;

/**
 * Gemaakt door Maarten van der Heijden on 11-1-2018.
 */

public class DAOFactory {
    private SQLiteLocalDatabase SQLiteLocalDatabase;
    public DAOFactory(SQLiteLocalDatabase SQLiteLocalDatabase){
        this.SQLiteLocalDatabase = SQLiteLocalDatabase;
    }
    public StudentDAO getStudentDAO(){
        return new StudentDAO(SQLiteLocalDatabase);
    }
    public MealDAO getMealDAO(){
        return new MealDAO(SQLiteLocalDatabase, getStudentDAO(), getFellowEaterDAO());
    }
    public FellowEaterDAO getFellowEaterDAO(){
        return new FellowEaterDAO(SQLiteLocalDatabase);
    }
}
