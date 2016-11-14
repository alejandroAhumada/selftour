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
                    "\t\"NOMBRE\" VARCHAR(255),\n" +
                    "\t\"APELLIDO\" VARCHAR(255),\n" +
                    "\t\"MAIL\" VARCHAR(255)\n" +
                    ");\n";

    private static final String RECORRIDO_CREATE_TABLE =
            "CREATE TABLE puntos_de_recorridos (\n" +
                    "\t\n" +
                    "\t\"ID_RECORRIDO\" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"ID_DESTINO\" INTEGER,\n" +
                    "\t\"NOMBRE_DESTINO\" VARCHAR(255),\n" +
                    "\t\"NOMBRE_RECORRIDO\" VARCHAR(255),\n" +
                    "\t\"DESCRIPCION_RECORRIDO\" VARCHAR(255),\n" +
                    "\t\"DURACION\" VARCHAR(255),\n" +
                    "\t\"POSICION\" INTEGER,\n" +
                    "\t\"NOMBRE_LUGAR\" VARCHAR(255),\n" +
                    "\t\"DESCRIPCION_LUGAR\" VARCHAR(255),\n" +
                    "\t\"LATITUD\" VARCHAR(255),\n" +
                    "\t\"LONGITUD\" VARCHAR(255),\n" +
                    "\t\"ID_MARCA\" INTEGER,\n" +
                    "\t\"NOMBRE_TIPO_MARCA\" VARCHAR(255)\n" +
                    ");\n";

    private static final String NACIONALIDADES_CREATE_TABLE =
            "CREATE TABLE nacionalidad (\n" +
                    "\t\n" +
                    "\t\"ID_NACIONALIDAD\" INTEGER,\n" +
                    "\t\"NACIONALIDAD\" VARCHAR(255)\n" +
                    ");\n";

    private static final String DESTINO_CREATE_TABLE =
            "CREATE TABLE destino_punto_interes (\n" +
                    "\t\n" +
                    "\t\"ID_DESTINO\" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "\t\"NOMBRE_DESTINO\" VARCHAR(255),\n" +
                    "\t\"DESCRIPCION_DESTINO\" VARCHAR(255),\n" +
                    "\t\"NOMBRE_LUGAR\" VARCHAR(255),\n" +
                    "\t\"DESCRIPCION_LUGAR\" VARCHAR(255),\n" +
                    "\t\"LATITUD\" VARCHAR(255),\n" +
                    "\t\"LONGITUD\" VARCHAR(255),\n" +
                    "\t\"ID_MARCA\" INTEGER,\n" +
                    "\t\"NOMBRE_TIPO_MARCA\" VARCHAR(255)\n" +
                    ");\n";

    private static final String USUARIO_DESTINO =
            "CREATE TABLE usuario_destino (\n" +
                    "\t\n" +
                    "\t\"NOMBRE_USUARIO\" VARCHAR(255),\n" +
                    "\t\"ID_DESTINO\" INTEGER,\n" +
                    "\t\"NOMBRE_DESTINO\" VARCHAR(255)\n" +
                    ");\n";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGIN_CREATE_TABLE);
        db.execSQL(RECORRIDO_CREATE_TABLE);
        db.execSQL(NACIONALIDADES_CREATE_TABLE);
        db.execSQL(DESTINO_CREATE_TABLE);
        db.execSQL(USUARIO_DESTINO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "puntos_de_recorridos");
        db.execSQL("DROP TABLE IF EXISTS " + "destino_punto_interes");
    }
}
