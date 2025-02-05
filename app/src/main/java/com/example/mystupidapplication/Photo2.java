package com.example.mystupidapplication;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Photo2 implements ClusterItem {
    private LatLng position;
    public String description;
    public Bitmap photo;
    public String eng;
    public int period;

    private Photo2(){};

    public Photo2(LatLng position, String description, Bitmap pictureResource, String eng, int period) {
        this.description = description;
        photo = pictureResource;
        this.position = position;
        this.eng=eng;
        this.period = period;
    }

    @Override
    public LatLng getPosition() {
        return this.position;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getDescription(){ return description; }
    public Bitmap getPhoto() { return photo; }
    public String getEng(){ return eng; }
    public int getPeriod(){return period; }
}