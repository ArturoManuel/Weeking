package com.example.weeking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Stadistics extends AppCompatActivity {
    PieChart pieChart;
    PieChart pieChart2;
    PieChart pieChart3;
    LineChart mpLineChart;
    int[] colorClassArray=new int []{Color.BLUE,Color.RED};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistics);



        pieChart2 = findViewById(R.id.pieChart2);
        PieDataSet pieDataSet2 = new PieDataSet(dataValue2(), "");
        pieDataSet2.setColors(colorClassArray);
        PieData pieData2 = new PieData(pieDataSet2);
        pieChart2.setDrawEntryLabels(true);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.setUsePercentValues(true);
        pieChart2.setDrawHoleEnabled(false);
        pieChart2.getLegend().setEnabled(false);
        pieDataSet2.setDrawValues(false);
        pieChart2.setData(pieData2);
        pieChart2.invalidate();


        pieChart=findViewById(R.id.pieChart);
        PieDataSet pieDataSet=new PieDataSet(dataValue1(),"");
        pieDataSet.setColors(colorClassArray);
        PieData pieData =new PieData(pieDataSet);
        pieChart.setDrawEntryLabels(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Alumnos en total");
        pieChart.setCenterTextSize(7);
        pieChart.setCenterTextRadiusPercent(50);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart3 = findViewById(R.id.pieChart3);
        PieDataSet pieDataSet3 = new PieDataSet(dataValue3(), "");
        pieDataSet3.setColors(colorClassArray);
        PieData pieData3 = new PieData(pieDataSet3);
        pieChart3.setDrawEntryLabels(true);
        pieChart3.getDescription().setEnabled(false);
        pieChart3.setUsePercentValues(true);
        pieChart3.setDrawHoleEnabled(false);
        pieChart3.getLegend().setEnabled(false);
        pieDataSet3.setDrawValues(false);
        pieChart3.setData(pieData3);
        pieChart3.invalidate();

        mpLineChart=(LineChart) findViewById(R.id.line_chart);
        XAxis xAxis = mpLineChart.getXAxis();
        final String[] xLabels = new String[]{"15 oct","", "17 oct","", "19 oct", "Hoy"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        mpLineChart.getDescription().setEnabled(false);
        LineDataSet lineDataSet1=new LineDataSet(dataValue4(),"Donaciones en soles");
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);
        LineData data =new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();


    }
    private ArrayList<PieEntry> dataValue1(){
        ArrayList<PieEntry> dataVals=new ArrayList<>();
        dataVals.add(new PieEntry(113,"Estudiantes"));
        dataVals.add(new PieEntry(37,"Egresados"));
        return dataVals;
    }
    private ArrayList<PieEntry> dataValue2(){
        ArrayList<PieEntry> dataVals2=new ArrayList<>();
        dataVals2.add(new PieEntry(80,"80%"));
        dataVals2.add(new PieEntry(20,"20%"));
        return dataVals2;
    }
    private ArrayList<PieEntry> dataValue3(){
        ArrayList<PieEntry> dataVals3=new ArrayList<>();
        dataVals3.add(new PieEntry(64,"63.83%"));
        dataVals3.add(new PieEntry(36,"36.17%"));
        return dataVals3;
    }
    private ArrayList<Entry> dataValue4(){
        ArrayList<Entry> dataVals4=new ArrayList<Entry>();
        dataVals4.add(new Entry(0,20));
        dataVals4.add(new Entry(1,100));
        dataVals4.add(new Entry(2,200));
        dataVals4.add(new Entry(3,300));
        dataVals4.add(new Entry(4,100));
        dataVals4.add(new Entry(5,200));
        return dataVals4;
    }

}