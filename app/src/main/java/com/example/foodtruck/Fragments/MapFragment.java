package com.example.foodtruck.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.Customer.MenuCustomerViewFragment;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import static androidx.core.content.ContextCompat.getSystemService;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;
    TextView radiusTxt, radiusDisplay;
    CircleOptions circleOptions;
    Circle mapCircle;
    FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        radiusTxt = v.findViewById(R.id.txtMapRadius);
        radiusDisplay = v.findViewById(R.id.txtClickableRadius);
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
                FoodTrucksContract fc = new FoodTrucksContract(getActivity());
                VendorsContract vc = new VendorsContract(getActivity());
                Bitmap bitmap = onCreateBitmap();

                // Check if device has location services permitted
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Unable to get current location. Be sure location permissions are enabled", Toast.LENGTH_SHORT).show();
                    // Propose to enable said location permissions
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                } else {
                    // Establish a criteria that will gather the food truck location and current device location at the best accuracy as the device can
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
                    criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
                    criteria.setPowerRequirement(Criteria.POWER_HIGH);
                    criteria.setAltitudeRequired(false);
                    criteria.setSpeedRequired(false);
                    criteria.setBearingRequired(false);
                    criteria.setCostAllowed(true);

                    // Location Manager handles the current location based on the initialized criteria
                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    Location deviceLocation = new Location(locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true)));
                    mMap.setMyLocationEnabled(true);
                    Toast.makeText(getContext(), "Location success", Toast.LENGTH_SHORT).show();
                    radiusTxt.setVisibility(View.VISIBLE);
                    radiusDisplay.setVisibility(View.VISIBLE);

                    // Instantiates a new CircleOptions object and defines the center and radius, required to create a circle visible on the map
                    circleOptions = new CircleOptions()
                            .center(new LatLng(deviceLocation.getLatitude(), deviceLocation.getLongitude())) // TOOD: have circle center on device's location (getMyMapLocation methods deprecated & doesn't actually work) GEOFENCE??
                            .radius(Integer.parseInt(radiusDisplay.getText().toString()) * 1609.34)
                            .strokeWidth(2)
                            .strokeColor(Color.argb(155, 52, 189, 235))
                            .fillColor(Color.argb(44, 52, 189, 235)); // In meters
                    // Draw circle on map with the specified options
                    mapCircle = mMap.addCircle(circleOptions);

                    radiusDisplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OpenCircleRadiusDialog(radiusDisplay.getText().toString());
                        }
                    });

                }

                // Loop through each vendor
                for(int c = 1; c <= vc.CountContracts(); c++) {
                    if(fc.FoodTruckList(c) != null) {
                        fc.FoodTruckList(c).forEach((n) -> mMap.addMarker(new MarkerOptions().
                                position(new LatLng(n.getM_Latitude(), n.getM_Longitude())).
                                title(n.getM_Name()).
                                snippet("Tap to view menu").
                                icon(BitmapDescriptorFactory.fromBitmap(bitmap)))); // Create bitmap definition with Map's BitmapDescriptorFactory class
                    }
                }

                // Goes to menu when an information window from a clicked marker is tapped
                // It will ALWAYS reference the title of the marker itself, since the title is created as the name of the truck
                // This might need to be changed as trucks with the same name can cause potential issues
                googleMap.setOnInfoWindowClickListener(marker -> {
                    FoodTruck truckAtPos = fc.findFoodTruckByName(marker.getTitle());
                    GotoMenu(truckAtPos);
                });

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(40.79, -73.29)).zoom(9).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

    private int getMarkerIDPos(String mID) {
        int myIndex = -1;
        try{
            myIndex = Integer.parseInt(mID.replace("m", ""));
        }catch(NumberFormatException nfe){
            Log.e("MARKER ID: ", nfe.getMessage());
        }
        return myIndex;
    }

    // Open menu when marker clicked
    private void GotoMenu(FoodTruck ft) {
        MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("mKey", ft.getM_ID());
        menuFrag.setArguments(bundle);
        transaction.replace(R.id.mainFragment_container, menuFrag).commit();
    }

    // Dialog to adjust circle's radius located on current location
    private void OpenCircleRadiusDialog(String val) {
        LayoutInflater dialogInflater = getLayoutInflater();
        View dv = dialogInflater.inflate(R.layout.dialog_setmapradius, null);
        NumberPicker radiusPicker = dv.findViewById(R.id.numRadius);
        radiusPicker.setMinValue(1);
        radiusPicker.setMaxValue(100);
        radiusPicker.setWrapSelectorWheel(false);
        radiusPicker.setValue(Integer.parseInt(val));
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv)
                .setNeutralButton("Confirm", null)
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.quantum_deeppurple, null));
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radiusDisplay.setText(String.valueOf(radiusPicker.getValue()));
                mapCircle.setRadius(radiusPicker.getValue() * 1609.34); // Convert meters to miles
                alertDialog.dismiss();
            }

        });
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
