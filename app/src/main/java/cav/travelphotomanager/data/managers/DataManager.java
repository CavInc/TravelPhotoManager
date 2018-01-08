package cav.travelphotomanager.data.managers;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import cav.travelphotomanager.data.database.DBConnect;
import cav.travelphotomanager.utils.ConstantManager;
import cav.travelphotomanager.utils.TravelPhotoManagerApp;

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PreferensManager mPreferensManager;
    private DBConnect mDB;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager(){
        this.mPreferensManager = new PreferensManager();
        this.mContext = TravelPhotoManagerApp.getContext();
        mDB = new DBConnect(mContext);
    }

    public Context getContext() {
        return mContext;
    }

    public PreferensManager getPreferensManager() {
        return mPreferensManager;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    // возвращает путь к локальной папки приложения
    public String getStorageAppPath(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        File path = new File (Environment.getExternalStorageDirectory(), ConstantManager.APP_WORK_PATH_NAME);
        if (! path.exists()) {
            if (!path.mkdirs()){
                return null;
            }
        }
        return path.getPath();
    }

    public DBConnect getDB() {
        return mDB;
    }
}