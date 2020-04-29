package com.example.mystupidapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

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
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends BaseDemoActivity
        implements ClusterManager.OnClusterClickListener<Photo2>,
        ClusterManager.OnClusterItemClickListener<Photo2>, OnMapReadyCallback {
    private ClusterManager<Photo2> mClusterManager;
    private Context context;

    @Override
    protected void startDemo(boolean isRestore) {
        context = getApplicationContext();
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
                            mClusterManager.setRenderer(new PhotoRenderer());
                            getMap().setOnCameraIdleListener(mClusterManager);
                            for (DocumentSnapshot document : task.getResult()) {
                                Photo dbPhoto = document.toObject(Photo.class);
                                byte[] byteArray = dbPhoto.getPhoto().toBytes();
                                Bitmap bm = getBitmap(byteArray);
                                Photo2 photo2 = new Photo2(new LatLng(dbPhoto.getPosition().latitude,
                                        dbPhoto.getPosition().longitude), dbPhoto.getDescription(), bm);
                                mClusterManager.addItem(photo2);
                            }
                            listeners();
                            mClusterManager.cluster();
                        }
                    }
                });

    }

    public void listeners() {
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
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
        Intent intent = new Intent(this, hello.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        item.getPhoto().compress(Bitmap.CompressFormat.JPEG, 100, bs);
        intent.putExtra("byteArray", bs.toByteArray());
        String desc = item.getDescription();
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
        //private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        //private final ImageView mClusterImageView;
        private final int mDimension;

        public PhotoRenderer() throws InflateException {
            super(getApplicationContext(), getMap(), mClusterManager);
            try {
                View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
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
            // Draw a single Photo.
            // Set the info window to show their name.
            mImageView.setImageBitmap(Photo.getPhoto());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }

}