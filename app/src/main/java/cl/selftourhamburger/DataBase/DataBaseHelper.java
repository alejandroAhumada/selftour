package cl.selftourhamburger.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alejandro on 24-05-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "selfTour_db.db";
    private static final int DATABASE_VERSION = 1;

    private static final String LOGIN_CREATE_TABLE =
            "CREATE TABLE login (\n" +
                    "\t\n" +
                    "\t\"USER\" VARCHAR(255),\n" +
                    "\t\"PASSWORD\" VARCHAR(255),\n"+
                    "\t\"NOMBRE\" VARCHAR(255),\n"+
                    "\t\"MAIL\" VARCHAR(255)\n"+
                    ");\n";

    public DataBaseHelper( Context context )
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGIN_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
