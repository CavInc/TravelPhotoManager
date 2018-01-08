package cav.travelphotomanager.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cav.travelphotomanager.R;
import cav.travelphotomanager.data.managers.DataManager;
import cav.travelphotomanager.data.models.MainPhotoModels;
import cav.travelphotomanager.ui.adapters.MainPhotoAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();

        mFab = (FloatingActionButton) findViewById(R.id.main_fab);
        mFab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_viewv);

        List<MainPhotoModels> models = mDataManager.getDB().getAllRecord();
        MainPhotoAdapter adapter = new MainPhotoAdapter(models);

        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        mDataManager.getDB().addNewRecord();
    }
}
