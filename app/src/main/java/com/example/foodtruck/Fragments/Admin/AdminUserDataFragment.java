package com.example.foodtruck.Fragments.Admin;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.Models.Admin;
import com.example.foodtruck.R;
import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AdminUserDataFragment extends Fragment  {
    AdminContract ac;
    CustomersContract cc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_userdata, container, false);

        cc = new CustomersContract(getContext());
        ac = new AdminContract(getContext());


//test code

//  Bar graph Template
        BarChart barChart = v.findViewById(R.id.barOne);

        ArrayList<BarEntry> barUsers = new ArrayList<>();

        barUsers.add(new BarEntry(01,420));
        barUsers.add(new BarEntry(02,320));
        barUsers.add(new BarEntry(03,220));
        barUsers.add(new BarEntry(04,120));
        barUsers.add(new BarEntry(5,320));

        BarDataSet barDataSet = new BarDataSet(barUsers,"Month");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(14f);


        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Amount of Users");
        barChart.getDescription().setYOffset(-12);
        barChart.getDescription().setTextSize(12f);
        barChart.animateY(2000);
//Pie Chart Template
        PieChart pieChart = v.findViewById(R.id.pieOne);

        ArrayList<PieEntry> pieUsers = new ArrayList<>();
        pieUsers.add(new PieEntry(508,"Jan"));
        pieUsers.add(new PieEntry(308,"Feb"));
        pieUsers.add(new PieEntry(208,"Mar"));
        pieUsers.add(new PieEntry(608,"Apr"));
        pieUsers.add(new PieEntry(cc.getColumnCount(),"May"));

        PieDataSet pieDataSet = new PieDataSet(pieUsers,"Amount of Users");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(18f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getDescription().setTextSize(14f);
        pieChart.setCenterText("Amount of Users");


//end of test code
        return v; }

}//close AdminMainFragment
