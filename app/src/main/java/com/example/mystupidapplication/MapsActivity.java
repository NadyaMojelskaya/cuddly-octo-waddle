package com.example.mystupidapplication;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.InflateException;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import androidx.annotation.NonNull;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.ByteArrayOutputStream;

public class MapsActivity extends BaseDemoActivity
        implements ClusterManager.OnClusterClickListener<Photo2>,
        ClusterManager.OnClusterItemClickListener<Photo2>, OnMapReadyCallback {
    private ClusterManager<Photo2> mClusterManager;
    private Context context;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private float GEOFENCE_RADIUS = 20;
    @Override
    protected void startDemo(boolean isRestore) {
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        context = getApplicationContext();
        if (!isRestore) {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.220222, 39.707417), 13));
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        if(getIntent().getBooleanArrayExtra("Array")!=null){Log.d("getIntent()",   "!=null");
            query();}
        else {
            Log.d("getIntent()",   "is null");
            db.collection("photos2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                mClusterManager = new ClusterManager<>(context, getMap());
                                mClusterManager.setRenderer(new PhotoRenderer());
                                getMap().setOnCameraIdleListener(mClusterManager);
                                for (DocumentSnapshot document : task.getResult()) {
                                    Photo dbPhoto = document.toObject(Photo.class);
                                    byte[] byteArray = dbPhoto.getPhoto().toBytes();
                                    Bitmap bm = getBitmap(byteArray);
                                    Photo2 photo2 = new Photo2(new LatLng(dbPhoto.getPosition().latitude,
                                            dbPhoto.getPosition().longitude), dbPhoto.getDescription(), bm, dbPhoto.getEng(), dbPhoto.getPeriod());
                                    mClusterManager.addItem(photo2);
                                    addGeofence(photo2.getPosition(), GEOFENCE_RADIUS);
                                }
                                listeners();
                                mClusterManager.cluster();
                            }
                        }
                    });
        }
    }
public void query(){
    Intent intent = getIntent();
    boolean[] array = intent.getBooleanArrayExtra("Array");
    mClusterManager = new ClusterManager<>(context, getMap());
    mClusterManager.setRenderer(new PhotoRenderer());
    getMap().setOnCameraIdleListener(mClusterManager);
    //geofenceHelper.remove(geofencingClient);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference citiesRef = db.collection("photos2");
    // Create a query against the collection.
    for (int i=0; i<10; i++){
        if (array[i]==true){
            Query query1 = citiesRef.whereEqualTo("period", i);
            query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            Photo dbPhoto = documentSnapshot.toObject(Photo.class);
                            byte[] byteArray = dbPhoto.getPhoto().toBytes();
                            Bitmap bm = getBitmap(byteArray);
                            Photo2 photo2 = new Photo2(new LatLng(dbPhoto.getPosition().latitude,
                                    dbPhoto.getPosition().longitude), dbPhoto.getDescription(),
                                    bm, dbPhoto.getEng(), dbPhoto.getPeriod());
                            mClusterManager.addItem(photo2);
                        }
                        listeners();
                        mClusterManager.cluster();
                    }
                    else{
                        Log.d("QUERY1", "everything's bad");
                    }
                }
            });
        }
    }

}
    public void listeners() {
        getMap().setOnCameraIdleListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
    }

    @Override
    public boolean onClusterClick(Cluster<Photo2> cluster) {
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
    public boolean onClusterItemClick(Photo2 item) {
        //передача данных в активность hello
        Intent intent = new Intent(this, hello.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        item.getPhoto().compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("byteArray", bs.toByteArray());
        String desc;
        if(super.lang=="rus")
            desc = item.getDescription();
        else
            desc = item.getEng();
        intent.putExtra("textString", desc);
        startActivity(intent);
        return false;
    }

    public Bitmap getBitmap(byte[] bytes) {
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bm;
    }

    private class PhotoRenderer extends DefaultClusterRenderer<Photo2> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final int mDimension;

        public PhotoRenderer() throws InflateException {
            super(getApplicationContext(), getMap(), mClusterManager);
            try {
                mImageView = new ImageView(getApplicationContext());
                mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
                mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
                int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
                mImageView.setPadding(padding, padding, padding, padding);
                mIconGenerator.setContentView(mImageView);
            } catch (Exception e) {
                Log.e("INFLATER", "onCreateView", e);
                throw e;
            }
        }

        @Override
        protected void onBeforeClusterItemRendered(Photo2 Photo, MarkerOptions markerOptions) {
            mImageView.setImageBitmap(Photo.getPhoto());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

    private void addGeofence(LatLng latLng, float radius) {
        //у каждого Geofence должен быть уникальный id
        Random r = new Random();
        char c = (char)(r.nextInt(26) + 'a');
        String GEOFENCE_ID = "id_"+c;
        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addGeofence", "onSuccess: Geofence Added...");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d("addGeofence", "onFailure: " + errorMessage);
                    }
                });
    }

}