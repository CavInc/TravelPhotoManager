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
import cav.travelphotomanager.utils.Func;

public class MainPhotoAdapter extends RecyclerView.Adapter<MainPhotoAdapter.ViewHolder>{

    private List<MainPhotoModels> mData;
    private ViewHolder.CallbackClickListener mCallbackClickListener;

    public MainPhotoAdapter (List<MainPhotoModels> data, ViewHolder.CallbackClickListener listener) {
        mData = data;
        mCallbackClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new ViewHolder(contentView,mCallbackClickListener);
    }

    private int mPosition;

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mPosition = position;
        MainPhotoModels models = mData.get(position);
        if (models.getImg1() != null  && models.getImg1().length()!=0) {
            holder.mImg1.setImageBitmap(Func.getPicSize(models.getImg1(),holder.mImg1));
        } else {
            holder.mImg1.setImageResource(R.drawable.nofoto);
        }
        if (models.getImg2() != null  && models.getImg2().length()!=0) {
            holder.mImg2.setImageBitmap(Func.getPicSize(models.getImg2(),holder.mImg2));
        } else {
            holder.mImg2.setImageResource(R.drawable.nofoto);
        }
        if (models.getImg3() != null  && models.getImg3().length()!=0) {
            holder.mImg3.setImageBitmap(Func.getPicSize(models.getImg3(),holder.mImg2));
        } else {
            holder.mImg3.setImageResource(R.drawable.nofoto);
        }
        if (models.getLat() == 0.0 && models.getLon() == 0.0) {
            holder.mCoordinate.setText("Координаты не определены");
        } else {
            // Координаты : lat = %1$.4f, lon = %2$.4f
            holder.mCoordinate.setText(String.format("Координаты : lat = %1$.6f, lon = %2$.6f",
                    models.getLat(),models.getLon())
                    +"\n\n"+models.getUlr());
        }

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

    public MainPhotoModels getPosition(int position){
        return mData.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private ImageView mImg1;
        private ImageView mImg2;
        private ImageView mImg3;
        private TextView mCoordinate;

        public View v;

        private CallbackClickListener mCallbackClickListener;


        public ViewHolder(View itemView,CallbackClickListener listener) {
            super(itemView);
            mCallbackClickListener = listener;
            mImg1 = (ImageView) itemView.findViewById(R.id.item_im1);
            mImg2 = (ImageView) itemView.findViewById(R.id.item_im2);
            mImg3 = (ImageView) itemView.findViewById(R.id.item_im3);
            mCoordinate = (TextView) itemView.findViewById(R.id.itemc_coordinate);

            mImg1.setOnClickListener(this);
            mImg2.setOnClickListener(this);
            mImg3.setOnClickListener(this);

            v = itemView;

            itemView.setOnLongClickListener(this);

            mImg1.setOnLongClickListener(this);
            mImg2.setOnLongClickListener(this);
            mImg3.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mCallbackClickListener == null) return;

            switch (view.getId()){
                case R.id.item_im1:
                    mCallbackClickListener.OnClick(getAdapterPosition(),1);
                    break;
                case R.id.item_im2:
                    mCallbackClickListener.OnClick(getAdapterPosition(),2);
                    break;
                case R.id.item_im3:
                    mCallbackClickListener.OnClick(getAdapterPosition(),3);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mCallbackClickListener!= null) {
                mCallbackClickListener.OnLongItemClick(getAdapterPosition());
            }
            return true;
        }

        public interface CallbackClickListener {
            public void OnClick(int position,int img);
            public void OnLongItemClick(int position);
        }
    }


}