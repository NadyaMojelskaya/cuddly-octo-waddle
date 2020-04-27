package com.example.mystupidapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Blob;


public class Photo{
    private GeoPoint position;
    public String description;
    public Blob photo;

    private Photo(){};

    public Photo(GeoPoint position, String description, Blob photo) {
        this.description = description;
        this.photo = photo;
        this.position = position;
    }

    public LatLng getPosition() {
        return new LatLng(position.getLatitude(), position.getLongitude());
    }
    public String getDescription(){ return description; }
    public Blob getPhoto() { return photo; }
}
