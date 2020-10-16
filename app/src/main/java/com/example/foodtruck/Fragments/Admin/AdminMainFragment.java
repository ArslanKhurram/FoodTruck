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

public class AdminMainFragment extends Fragment  {
    AdminContract ac;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_main, container, false);


        ac = new AdminContract(getContext());
        Admin admin = ac.addAdmin("abc@aol.com", "123");
//test code

//  Bar graph Template
       BarChart barChart = v.findViewById(R.id.barOne);

        ArrayList<BarEntry> barSales = new ArrayList<>();

        barSales.add(new BarEntry(01,420));
        barSales.add(new BarEntry(02,320));
        barSales.add(new BarEntry(03,220));
        barSales.add(new BarEntry(04,120));
        barSales.add(new BarEntry(5,320));

        BarDataSet barDataSet = new BarDataSet(barSales,"Month");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Food Truck Sales By Month");
        barChart.animateY(2000);
//Pie Chart Template
        PieChart pieChart = v.findViewById(R.id.pieOne);

        ArrayList<PieEntry> pieSales = new ArrayList<>();
        pieSales.add(new PieEntry(508,"Jan"));
        pieSales.add(new PieEntry(308,"Feb"));
        pieSales.add(new PieEntry(208,"Mar"));
        pieSales.add(new PieEntry(608,"Apr"));
        pieSales.add(new PieEntry(208,"May"));

        PieDataSet pieDataSet = new PieDataSet(pieSales,"Sales");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(10f);

        PieData pieData = new PieData(pieDataSet);

    pieChart.setData(pieData);
    pieChart.getDescription().setEnabled(false);
    pieChart.setCenterText("Sales");
//end of test code
        return v; }

}//close AdminMainFragment
