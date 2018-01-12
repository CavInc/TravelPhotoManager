package cav.travelphotomanager.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;

    private MainPhotoAdapter adapter;

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
}
