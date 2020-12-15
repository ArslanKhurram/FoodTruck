package com.example.foodtruck.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtruck.Adapter.MySearchAdapter;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.MenusContract;
import com.example.foodtruck.DataBase.RatingsContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.Customer.MenuCustomerViewFragment;
import com.example.foodtruck.Models.FoodTruck;
import com.example.foodtruck.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class SearchFragment extends Fragment implements MySearchAdapter.onCardClickListener, View.OnClickListener {

    MySearchAdapter searchAdapter = new MySearchAdapter(getContext(), this, this);
    ArrayList<FoodTruck> searchList = new ArrayList<>();
    ArrayList<FoodTruck> resultsList = new ArrayList<>();
    AppCompatRadioButton radioName, radioCategory;
    Button btnSort;
    ListView listView;
    String searchType = "Name";
    String searchSort = "Alphabetically";
    SearchView searchView;
    RecyclerView recyclerView;
    FusedLocationProviderClient locClient;
    Location devLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        locClient = LocationServices.getFusedLocationProviderClient(getContext());
        devLocation = new Location(LocationManager.GPS_PROVIDER);

        recyclerView = v.findViewById(R.id.search_recycler);
        searchView = v.findViewById(R.id.search_view);
        radioName = v.findViewById(R.id.rbName);
        radioCategory = v.findViewById(R.id.rbCategory);
        btnSort = v.findViewById(R.id.btnSortSearch);
        radioName.setOnClickListener(this);
        radioCategory.setOnClickListener(this);
        v.findViewById(R.id.btnSortSearch).setOnClickListener(this);

        fillSearchList(searchList);
        resetList(resultsList);
        searchAdapter.submitList(resultsList);

        // Search view's X button set by Android Studio
        ImageView btnClear = (ImageView) searchView.findViewById(R.id.search_close_btn);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(searchAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                OpenSearchSort(v);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Conduct a search through entire list when search button is pressed (Case-sensitive for now)
            @Override
            public boolean onQueryTextSubmit(String query) {
                QuerySearch(resultsList, query, searchSort);
                searchAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update search query automatically when at least 3 letters have been entered (BAD PERFORMANCE)
                QuerySearch(resultsList, newText, searchSort);
                searchAdapter.submitList(resultsList);
                return false;
            }
        });


        return v;
    }

    @Override
    public void onClick(View v) {
        boolean isSelected = ((AppCompatRadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.rbName:
                if (isSelected) {
                    radioName.setTextColor(Color.WHITE);
                    radioCategory.setTextColor(Color.BLACK);
                    searchType = "Name";
                    searchView.setQueryHint("Search Food Truck");
                    searchView.setQuery("", false);
                }
                if (!searchView.isEnabled())
                    searchView.setEnabled(true);
                break;
            case R.id.rbCategory:
                if (isSelected) {
                    radioCategory.setTextColor(Color.WHITE);
                    radioName.setTextColor(Color.BLACK);
                    searchType = "Category";
                    searchView.setQueryHint("Search Category");
                    searchView.setQuery("", false);
                }
                if (!searchView.isEnabled())
                    searchView.setEnabled(true);
                break;
        }
    }

    @Override
    public void onCardClick(int pos) {
        GotoMenu(resultsList.get(pos));
    }

    private void fillSearchList(ArrayList<FoodTruck> ftList) {
        FoodTrucksContract fc = new FoodTrucksContract(getActivity());
        VendorsContract vc = new VendorsContract(getActivity());
        ftList.clear();
        // Loop through each vendor for every food truck in database
        for (int c = 1; c <= vc.CountContracts(); c++) {
            if (fc.FoodTruckList(c) != null)
                ftList.addAll(fc.FoodTruckList(c));
        }
    }

    private void resetList(ArrayList<FoodTruck> rList) {
        rList.clear();
        rList.addAll(searchList);
        searchView.setQuery("", true);
        searchAdapter.notifyDataSetChanged();
    }

    // Reduce to list of food trucks according to the name/category search input by user
    // Triggers whenever the search view is changed or user presses search button
    private void QuerySearch(ArrayList<FoodTruck> ftList, String query, String sortType) {
        ftList.clear();
        switch (searchType) { // Duplicate code, should refactor soon
            case "Name":
                for (int i = 0; i < searchList.size(); i++) {
                    if (searchList.get(i).getM_Name().toLowerCase().contains(query.toLowerCase())) {
                        ftList.add(searchList.get(i));
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                break;
            case "Category":
                for (int i = 0; i < searchList.size(); i++) {
                    if (searchList.get(i).getM_Category().toLowerCase().contains(query.toLowerCase())) {
                        ftList.add(searchList.get(i));
                    }
                    searchAdapter.notifyDataSetChanged();
                }
                break;
        }

        if (searchSort != null) {
            ArrayList tempList = new ArrayList();
            tempList.addAll(resultsList);
            resultsList.clear();
            switch (sortType) {
                case "Alphabetically":
                    Collections.sort(tempList, new Comparator<FoodTruck>() {
                        @Override
                        public int compare(FoodTruck ft1, FoodTruck ft2) {
                            return ft1.getM_Name().compareTo(ft2.getM_Name());
                        }
                    });
                    resultsList.addAll(tempList);
                    searchAdapter.notifyDataSetChanged();
                    break;
                case "Distance":
                    Collections.sort(tempList, new Comparator<FoodTruck>() {
                        @Override
                        public int compare(FoodTruck ft1, FoodTruck ft2) {
                            return distanceToFoodTruck(ft1) < distanceToFoodTruck(ft2) ? -1 : 1;
                        }
                    });
                    resultsList.addAll(tempList);
                    searchAdapter.notifyDataSetChanged();
                    break;
                case "Rating":
                    Collections.sort(tempList, new Comparator<FoodTruck>() {
                        @Override
                        public int compare(FoodTruck ft1, FoodTruck ft2) {
                            RatingsContract rc = new RatingsContract(getContext());
                            return rc.averageRatingsForID(ft1.getM_ID()) > rc.averageRatingsForID(ft2.getM_ID()) ? -1 : 1;
                        }
                    });
                    resultsList.addAll(tempList);
                    searchAdapter.notifyDataSetChanged();
                    break;
                default:
                    resultsList.addAll(tempList);
                    break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void OpenSearchSort(View view) {
        View v = getLayoutInflater().inflate(R.layout.search_sort_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(v);
        dialog.show();
        Button btnSortAlpha = v.findViewById(R.id.btnSortAlphabetically);
        Button btnSortDist = v.findViewById(R.id.btnSortDistance);
        Button btnSortRating = v.findViewById(R.id.btnSortRating);

        btnSortAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSort = (String) btnSortAlpha.getText();
                QuerySearch(resultsList, searchView.getQuery().toString(), searchSort);
                searchAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // Check if device's location permissions are enabled in order to sort by distance
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            btnSortDist.setEnabled(false);
        } else {
            btnSortDist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchSort = (String) btnSortDist.getText();
                    QuerySearch(resultsList, searchView.getQuery().toString(), searchSort);
                    dialog.dismiss();
                }
            });
        }

        btnSortRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSort = (String) btnSortRating.getText();
                QuerySearch(resultsList, searchView.getQuery().toString(), searchSort);
                dialog.dismiss();
            }
        });
    }

    public double distanceToFoodTruck(FoodTruck ft) {
        double distance = -1;
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
        Location ftLocation = new Location(locationManager.getBestProvider(criteria,true));
        ftLocation.setLatitude(ft.getM_Latitude());
        ftLocation.setLongitude(ft.getM_Longitude());

        // Check if location permissions are enabled
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return distance;
        else {
            if (locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true)) != null) { // distanceTo method in Location, result is in meters, then converted into miles
                distance = (ftLocation.distanceTo(locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria,true)))) * 0.00062137119;
            }

            return distance;
        }
    }


    // Go to specified menu based on position clicked
    private void GotoMenu(FoodTruck ft) {
        MenuCustomerViewFragment menuFrag = new MenuCustomerViewFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong("mKey", ft.getM_ID());
        MenusContract mc = new MenusContract(getContext());
        menuFrag.setArguments(bundle);
        transaction.replace(R.id.mainFragment_container, menuFrag).commit();
    }

}
