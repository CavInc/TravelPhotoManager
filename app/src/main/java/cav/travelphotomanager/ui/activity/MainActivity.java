package cav.travelphotomanager.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cav.travelphotomanager.R;
import cav.travelphotomanager.data.managers.DataManager;
import cav.travelphotomanager.data.models.MainPhotoModels;
import cav.travelphotomanager.ui.adapters.MainPhotoAdapter;
import cav.travelphotomanager.utils.ConstantManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MA";
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int PERMISSION_REQUEST_WSD = 1001;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;

    private MainPhotoAdapter adapter;

    private LocationManager locationManager;
    private boolean isGpsPresent = false;
    private boolean isNetWork = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();

        mFab = (FloatingActionButton) findViewById(R.id.main_fab);
        mFab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_viewv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

        }
        */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST_CODE);
            return; // подумать о перезапуске навигации после разрешения
        }
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_WSD);
        }
        setStartLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_gps:
                startActivity(new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length!=0){

                }
                break;
            case PERMISSION_REQUEST_WSD:
                if (grantResults.length!=0){
                    if (grantResults[0]==PackageManager.PERMISSION_DENIED) {
                        // тут говорим что хуй а не работа без прав на SD
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    private void setStartLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /*
        List<String> matchingProviders = locationManager.getAllProviders();
        for (String provider:matchingProviders){
            Log.i(TAG," Provider: "+matchingProviders);
        }
        */

        isGpsPresent = locationManager.getProvider(LocationManager.GPS_PROVIDER) != null;
        isNetWork = locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null;

        Log.i(TAG,"GPS :"+isGpsPresent+" NET: "+isNetWork);

        if (isGpsPresent) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, locationListener);
        }

        if (isNetWork) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                    locationListener);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void updateUI(){
        List<MainPhotoModels> models = mDataManager.getDB().getAllRecord();
        if (adapter == null){
            adapter = new MainPhotoAdapter(models,mImgListener);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapter.setData(models);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        double lon = 0.0;
        double lat = 0.0;
        if (lonGPS != 0.0) {
            lon = lonGPS;
        } else {
            lon = lonWIFI;
        }
        if (latGPS != 0.0) {
            lat = latGPS;
        } else {
            lat = latWiFI;
        }
        mDataManager.getDB().addNewRecord(lon,lat);
        updateUI();
    }

    private File mPhotoFile = null;
    private int mPosition;
    private int mImg;

    MainPhotoAdapter.ViewHolder.CallbackClickListener mImgListener = new MainPhotoAdapter.ViewHolder.CallbackClickListener() {
        @Override
        public void OnClick(int position, int img) {

            String fileImg = adapter.getPosition(position).getImg(img);
            //Log.d(TAG,"LEN "+fileImg.length());
            if (fileImg != null && fileImg.length() != 0) {
                Log.d(TAG,"USE PHOTO");
                Intent viewIntent = new Intent(MainActivity.this,ViewPhotoActivity.class);
                viewIntent.putExtra(ConstantManager.REQUEST_FILENAME,fileImg);
                viewIntent.putExtra(ConstantManager.REQUEST_ID,adapter.getPosition(position).getId());
                startActivity(viewIntent);
                return;
            }

            mPosition = position;
            mImg = img;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFile = "JPEG_"+timeStamp+"_";
            String path = mDataManager.getStorageAppPath();
            if (path == null) return;
            File image = null;
            try {
                image = File.createTempFile(imageFile,".jpg",new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPhotoFile = image;
            Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // для А7
            Log.d(TAG,"SETTING A7");
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
           // fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            captureImage.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //

            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            try {
                startActivityForResult(captureImage, ConstantManager.REQUEST_CAMERA_PICTURE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void OnLongItemClick(final int position) {
            Log.d(TAG,"LONG CLICK");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            AlertDialog dialog = builder.setTitle("Удаление").setMessage("Удаляем ? Вы уверенны ?")
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.i(TAG,"POS ID :"+adapter.getPosition(position).getId());
                            mDataManager.getDB().deleteRecord(adapter.getPosition(position).getId());
                            updateUI();
                        }
                    }).create();
            dialog.show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile !=null) {
                    System.out.println(mPhotoFile);
                    MainPhotoModels model = adapter.getPosition(mPosition);
                    mDataManager.getDB().updateRecord(model.getId(),mImg,mPhotoFile.toString());
                    updateUI();
                    mPhotoFile = null;
                }
                break;
        }
    }


    private LocationListener locationListener = new LocationListener(){
        public static final String TAG = "LOCATION LISTENER";

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG,"LOCATION CHANGED");
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {
            Log.d(TAG,"STATUS CHANGED");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG,"PROVIDER ENABLE");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG,"PROVIDER DISABLE");
            latGPS = 0.0f;
            lonGPS = 0.0f;
            latWiFI = 0.0f;
            lonWIFI = 0.0f;

        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            //tvLocationGPS.setText(formatLocation(location));
            Log.d(TAG +"GPS",formatLocation(location));
            latGPS = location.getLatitude();
            lonGPS = location.getLongitude();
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            //tvLocationNet.setText(formatLocation(location));
            Log.d(TAG + " WIFI ",formatLocation(location));
            latWiFI = location.getLatitude();
            lonWIFI = location.getLongitude();
        }
    }

    private double latGPS = 0.0f;
    private double lonGPS = 0.0f;
    private double latWiFI = 0.0f;
    private double lonWIFI = 0.0f;


    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

}
