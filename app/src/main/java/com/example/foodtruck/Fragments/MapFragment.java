package com.example.foodtruck.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;
    Spinner radiusSpinner;
    TextView radiusTxt;
    CircleOptions circleOptions;
    Circle mapCircle;
    FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        radiusSpinner = v.findViewById(R.id.spnMapSpinner);
        radiusTxt = v.findViewById(R.id.txtMapRadius);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // need to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {

                mMap = googleMap;

                // Check if device has location services permitted
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Unable to get current location. Be sure location permissions are enabled", Toast.LENGTH_SHORT).show();
                    // Propose to enable said location permissions
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                    radiusSpinner.setVisibility(View.INVISIBLE);
                    radiusTxt.setVisibility(View.INVISIBLE);

                } else {
                    mMap.setMyLocationEnabled(true);
                }
                FoodTrucksContract fc = new FoodTrucksContract(getActivity());
                VendorsContract vc = new VendorsContract(getActivity());
                Bitmap bitmap = onCreateBitmap();
                    Toast.makeText(getContext(), "Location succeess", Toast.LENGTH_SHORT).show();

                    // Instantiates a new CircleOptions object and defines the center and radius, required to create a circle visible on the map
//                     circleOptions = new CircleOptions()
//                             .center(new LatLng(40.806404, -73.25934)) // TOOD: have circle center on device's location (getMyMapLocation methods deprecated & doesn't actually work) GEOFENCE??
//                             .radius(10000)
//                             .strokeWidth(2)
//                             .strokeColor(Color.argb(155, 52, 189, 235))
//                             .fillColor(Color.argb(44, 52, 189, 235)); // In meters

                    // Draw circle on map
                    //mapCircle = mMap.addCircle(circleOptions);

               // LatLng latLng = new LatLng(40.806404, -73.25934);

                // Loop through each vendor
                for(int c = 1; c <= vc.CountContracts(); c++) {
                    if(fc.FoodTruckList(c) != null) {
                        fc.FoodTruckList(c).forEach((n) -> mMap.addMarker(new MarkerOptions().
                                position(new LatLng(n.getM_Latitude(), n.getM_Longitude())).
                                title(n.getM_Name()).
                                icon(BitmapDescriptorFactory.fromBitmap(bitmap)))); // Create bitmap definition with Map's BitmapDescriptorFactory class
                    }
                }

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(40.79,-73.29)).zoom(9).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        radiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // mapCircle.setRadius((int) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // default radius
            }
        });

        return v;
    }

    // Initialize a new bitmap with a canvas, which Google map markers require in order to customize their appearance (only needs to be called once when the fragment loads!)
    private Bitmap onCreateBitmap() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_food_truck_icon_21, null);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }



    //methods to help map fragment run
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
