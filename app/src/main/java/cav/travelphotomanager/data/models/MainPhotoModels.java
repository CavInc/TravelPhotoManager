package cav.travelphotomanager.data.models;

import java.util.Date;

public class MainPhotoModels {
    private int mId;
    private String mImg1;
    private String mImg2;
    private String mImg3;
    private double lon = 0.0;
    private double lat = 0.0;
    private String ulr;
    private Date createDate;

    public MainPhotoModels(String img1, String img2, String img3) {
        mImg1 = img1;
        mImg2 = img2;
        mImg3 = img3;
    }

    public MainPhotoModels(int id, String img1, String img2, String img3) {
        mId = id;
        mImg1 = img1;
        mImg2 = img2;
        mImg3 = img3;
    }

    public MainPhotoModels(int id, String img1, String img2, String img3, double lon, double lat) {
        mId = id;
        mImg1 = img1;
        mImg2 = img2;
        mImg3 = img3;
        this.lon = lon;
        this.lat = lat;
    }

    public MainPhotoModels(int id, String img1, String img2, String img3, double lon, double lat, String ulr) {
        mId = id;
        mImg1 = img1;
        mImg2 = img2;
        mImg3 = img3;
        this.lon = lon;
        this.lat = lat;
        this.ulr = ulr;
    }

    public int getId() {
        return mId;
    }

    public String getImg1() {
        return mImg1;
    }

    public String getImg2() {
        return mImg2;
    }

    public String getImg3() {
        return mImg3;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getUlr() {
        return ulr;
    }
}