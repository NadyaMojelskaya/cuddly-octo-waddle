package com.example.mystupidapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Blob;


public class Photo{
    private GeoPoint position;
    public String description;
    public Blob photo;
    public String eng;
    public int  period;

    private Photo(){};

    public Photo(GeoPoint position, String description, Blob photo, String eng,int  period) {
        this.description = description;
        this.photo = photo;
        this.position = position;
        this.eng = eng;
        this.period = period;
    }

    public LatLng getPosition() { return new LatLng(position.getLatitude(), position.getLongitude()); }
    public String getDescription(){ return description; }
    public Blob getPhoto() { return photo; }
    public String getEng(){ return eng; }
    public int getPeriod(){return period; }
}
