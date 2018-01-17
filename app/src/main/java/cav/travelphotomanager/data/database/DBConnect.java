package cav.travelphotomanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cav.travelphotomanager.data.models.MainPhotoModels;

public class DBConnect {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;

    public DBConnect(Context context){
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }

    public void close(){
        if (database!=null) {
            database.close();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public boolean isOpen(){
        return database.isOpen();
    }

    //-------------------- Запросы к базе ------------------------------

    public void addNewRecord(){
        open();
        ContentValues values = new ContentValues();
        values.put("file_img1","");
        database.insert(DBHelper.PHOTOINFO,null,values);
        close();
    }

    public ArrayList<MainPhotoModels> getAllRecord(){
        ArrayList<MainPhotoModels> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.PHOTOINFO,
                new String[]{"id","create_date","create_time",
                        "file_img1","file_img2","file_img3"},
                null,null,null,null,"id");
        while (cursor.moveToNext()){
            rec.add(new MainPhotoModels(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("file_img1")),
                    cursor.getString(cursor.getColumnIndex("file_img2")),
                    cursor.getString(cursor.getColumnIndex("file_img3"))
            ));
        }
        close();
        return rec;
    }

    public void updateRecord(int id,int img_id,String value){
        open();
        ContentValues values = new ContentValues();
        //values.put("id",id);
        values.put("file_img"+img_id,value);
        database.update(DBHelper.PHOTOINFO,values,"id="+id,null);
        close();
    }

    public void deleteRecord(int id){
        open();
        database.delete(DBHelper.PHOTOINFO,"id="+id,null);
        close();
    }

}