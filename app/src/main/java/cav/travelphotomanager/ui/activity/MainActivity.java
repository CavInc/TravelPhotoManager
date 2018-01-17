package cav.travelphotomanager.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
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
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;

    private MainPhotoAdapter adapter;

    private LocationManager locationManager;

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

    @SuppressWarnings("MissingPermission")
    private void setStartLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
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
        mDataManager.getDB().addNewRecord();
        updateUI();
    }

    private File mPhotoFile = null;
    private int mPosition;
    private int mImg;

    MainPhotoAdapter.ViewHolder.CallbackClickListener mImgListener = new MainPhotoAdapter.ViewHolder.CallbackClickListener() {
        @Override
        public void OnClick(int position, int img) {
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
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(captureImage, ConstantManager.REQUEST_CAMERA_PICTURE);
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
                            mDataManager.getDB().deleteRecord(position);
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
                }
                break;
        }
    }


    private LocationListener locationListener = new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

}
