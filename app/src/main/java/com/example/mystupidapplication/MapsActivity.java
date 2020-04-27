package com.example.mystupidapplication;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.ByteArrayOutputStream;

/**
 * Demonstrates heavy customisation of the look of rendered clusters.
 */
public class MapsActivity extends BaseDemoActivity
        implements ClusterManager.OnClusterClickListener<Photo>,
        ClusterManager.OnClusterItemClickListener<Photo>, OnMapReadyCallback {
    private ClusterManager<Photo> mClusterManager;
    private Context context;

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    @Override
    protected void startDemo(boolean isRestore) {
        context=getApplicationContext();
        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.220222, 39.707417), 13));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photos2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mClusterManager = new ClusterManager<>(context, getMap());
                            getMap().setOnCameraIdleListener(mClusterManager);
                            for (DocumentSnapshot document : task.getResult()) {
                                Photo dbPhoto = document.toObject(Photo.class);
                                getMap().addMarker(new MarkerOptions()
                                        .position(new LatLng(dbPhoto.getPosition().latitude, dbPhoto.getPosition().longitude)));
                                mClusterManager.addItem(dbPhoto);
                            }
                            listeners();
                            mClusterManager.cluster();
                        }
                    }
                });

    }
    public void listeners(){
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
    }
    @Override
    public boolean onClusterClick(Cluster<Photo> cluster) {
        // Show a toast with some info when the cluster is clicked.
//        String firstName = cluster.getItems().iterator().next().name;
//        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onClusterItemClick(Photo item) {
        Intent intent = new Intent(this, hello.class);
        byte[] byteArray = item.getPhoto().toBytes();
        Bitmap bm = getBitmap(byteArray);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("byteArray", bs.toByteArray());
        String desc = item.getDescription();
        intent.putExtra("textString", desc);
        startActivity(intent);
        return false;
    }
    public Bitmap getBitmap(byte[] bytes){
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        //BitmapDrawable bmdr = new BitmapDrawable(bm);
        return bm;
    }
}
