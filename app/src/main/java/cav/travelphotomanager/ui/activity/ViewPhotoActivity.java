package cav.travelphotomanager.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import cav.travelphotomanager.R;
import cav.travelphotomanager.utils.ConstantManager;
import cav.travelphotomanager.utils.Func;

public class ViewPhotoActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        String file = getIntent().getStringExtra(ConstantManager.REQUEST_FILENAME);

        mImageView = (ImageView) findViewById(R.id.vp_img);

        mImageView.setImageBitmap(Func.getPicSize(file,mImageView));

    }
}
