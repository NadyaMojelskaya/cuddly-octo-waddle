package com.example.mystupidapplication;

import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.clustering.ClusterItem;
import com.google.firebase.firestore.Blob;

import java.io.Serializable;

public class Photo implements ClusterItem{
    private GeoPoint position;
    public String description;
    public Blob photo;
    //public BitmapDrawable bmdr;

    private Photo(){};

    public Photo(GeoPoint position, String description, Blob photo) {
        this.description = description;
        this.photo = photo;
        this.position = position;
    }
//    public photo(LatLng position, String description, BitmapDrawable bmdr2) {
//        this.description = description;
//        this.bmdr = bmdr2;
//        this.Position = position;
//    }
//    public photo(LatLng position, String description, Bitmap pictureResource) {
//        this.description = description;
//        bm = pictureResource;
//        this.position = position;
//    }

    @Override
    public LatLng getPosition() {
        return new LatLng(position.getLatitude(), position.getLongitude());
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
    public Blob getPhoto() { return photo; }
    //public BitmapDrawable getBmdr() { return bmdr; }
}
