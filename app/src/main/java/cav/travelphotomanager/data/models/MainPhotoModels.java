package cav.travelphotomanager.data.models;

public class MainPhotoModels {
    private int mId;
    private String mImg1;
    private String mImg2;
    private String mImg3;

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
}