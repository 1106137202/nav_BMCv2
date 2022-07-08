package com.example.nav_bmcv2.ui.rate;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nav_bmcv2.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class RateFragment extends Fragment {

    private RateViewModel mViewModel;
    private BarChart barChart;

    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;

    public static RateFragment newInstance() {
        return new RateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rate_fragment, container, false);
        barChart = view.findViewById(R.id.chart);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // gap of 2f
        entries.add(new BarEntry(4f, 70f));
        entries.add(new BarEntry(5f, 60f));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");

        BarData data = new BarData(set);
        data.setBarWidth(0.5f); //設定自定義條形寬度
        barChart.setData(data);
        barChart.setFitBars(true); //使x軸完全適合所有條形
        barChart.invalidate(); // refresh

//        barChart.setData(barData);
//
//        // adding color to our bar data set.
////        barDataSet.setColors(R.color.blue_1);
//
//        // setting text color.
//        barDataSet.setValueTextColor(Color.BLACK);
//
//        // setting text size
//        barDataSet.setValueTextSize(16f);
//        barChart.getDescription().setEnabled(false);

        return view;
    }

//    private void getBarEntries() {
//        // creating a new array list
//        barEntriesArrayList = new ArrayList<>();
//
//        // adding new entry to our array list with bar
//        // entry and passing x and y axis value to it.
//        barEntriesArrayList.add(new BarEntry(1f, 4));
//        barEntriesArrayList.add(new BarEntry(2f, 6));
//        barEntriesArrayList.add(new BarEntry(3f, 8));
//        barEntriesArrayList.add(new BarEntry(4f, 2));
//        barEntriesArrayList.add(new BarEntry(5f, 4));
//        barEntriesArrayList.add(new BarEntry(6f, 1));
//    }



}