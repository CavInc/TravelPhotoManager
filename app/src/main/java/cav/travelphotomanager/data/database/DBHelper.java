package cav.travelphotomanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1 ;
    public static final String DATABASE_NAME = "tpm.db3";

    public static final String PHOTOINFO = "photoinfo";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updatedDB(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updatedDB(db,oldVersion,newVersion);
    }

    private void updatedDB(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion < 1 ){
            db.execSQL("create table "+PHOTOINFO+" (" +
                    "id integer not null primary key AUTOINCREMENT,"+
                    "create_date text,"+
                    "create_time text,"+
                    "file_img1 text,"+
                    "file_img2 text,"+
                    "file_img3 text," +
                    "lon real,"+
                    "lat real,"+
                    "url_gm text)"); // ссылка для гугломапс
        }
    }
}