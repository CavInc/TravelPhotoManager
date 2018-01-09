package cav.travelphotomanager.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cav.travelphotomanager.R;
import cav.travelphotomanager.data.models.MainPhotoModels;

public class MainPhotoAdapter extends RecyclerView.Adapter<MainPhotoAdapter.ViewHolder>{

    private List<MainPhotoModels> mData;

    public MainPhotoAdapter (List<MainPhotoModels> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MainPhotoModels models = mData.get(position);
        holder.mCoordinate.setText("А тут координаты");

    }

    @Override
    public int getItemCount() {
        if (mData!= null) {
            return mData.size();
        }
        return 0;
    }

    public void setData(List<MainPhotoModels> data) {
        mData.clear();
        mData.addAll(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImg1;
        private ImageView mImg2;
        private ImageView mImg3;
        private TextView mCoordinate;


        public ViewHolder(View itemView) {
            super(itemView);
            mImg1 = (ImageView) itemView.findViewById(R.id.item_im1);
            mImg2 = (ImageView) itemView.findViewById(R.id.item_im2);
            mImg3 = (ImageView) itemView.findViewById(R.id.item_im3);
            mCoordinate = (TextView) itemView.findViewById(R.id.itemc_coordinate);
        }
    }

}