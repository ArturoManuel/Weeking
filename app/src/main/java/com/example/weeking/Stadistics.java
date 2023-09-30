package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class Stadistics extends AppCompatActivity {
    PieChart pieChart;
    int[] colorClassArray=new int []{Color.BLUE,Color.RED};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistics);

        pieChart=findViewById(R.id.pieChart);
        PieDataSet pieDataSet=new PieDataSet(dataValue1(),"");
        pieDataSet.setColors(colorClassArray);
        PieData pieData =new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Alumnos en total");
        pieChart.setCenterTextSize(20);
        pieChart.setCenterTextRadiusPercent(50);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
    private ArrayList<PieEntry> dataValue1(){
        ArrayList<PieEntry> dataVals=new ArrayList<>();
        dataVals.add(new PieEntry(113,"Estudiantes"));
        dataVals.add(new PieEntry(37,"Egresados"));
        return dataVals;
    }
}