package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.weeking.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.mikephil.charting.formatter.ValueFormatter;
public class Stadistics extends AppCompatActivity {
    PieChart pieChart;
    PieChart pieChart2;
    PieChart pieChart3;
    LineChart mpLineChart;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView textViewTotalUsers;

    private TextView textViewTotalStudents;
    private TextView textViewTotalGraduates;
    private int totalStudents = 0;
    private int newtotalStudents = 0;
    private int totalGraduates = 0;
    private int totalAdministrators = 0;
    private int totalDelegates = 0;
    private HashMap<String, Integer> eventCountByLocation = new HashMap<>();




    int[] colorClassArray=new int []{Color.BLUE,Color.RED};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistics);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Estadisticas");
        } else {
            Log.e("Estadisticas", "El título del Toolbar no se ha encontrado.");
        }

        textViewTotalUsers = findViewById(R.id.textView57);
        textViewTotalStudents = findViewById(R.id.textView59);
        textViewTotalGraduates = findViewById(R.id.textView63);

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int numberOfUsers = task.getResult().size();
                        textViewTotalUsers.setText("Alumnos en total: " + numberOfUsers);
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });


        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String role = document.getString("estado");
                            if ("Estudiante".equals(role)) {
                                totalStudents++;
                            } else if ("Egresado".equals(role)) {
                                totalGraduates++;
                            }
                        }
                        // Actualizacion TextViews y el PieChart
                        textViewTotalStudents.setText("Estudiantes: " + totalStudents);
                        textViewTotalGraduates.setText("Egresados: " + totalGraduates);
                        updatePieChart();
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String role = document.getString("rol");
                            switch (role) {
                                case "administrador":
                                    totalAdministrators++;
                                    break;
                                case "delegado_de_actividad":
                                    totalDelegates++;
                                    break;
                                case "alumno":
                                    newtotalStudents++;
                                    break;

                            }
                        }

                        updateBarChart();
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });



        db.collection("Eventos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String location = document.getString("ubicacion");
                            if (location != null) {
                                if (!eventCountByLocation.containsKey(location)) {
                                    eventCountByLocation.put(location, 1);
                                } else {
                                    int currentCount = eventCountByLocation.get(location);
                                    eventCountByLocation.put(location, currentCount + 1);
                                }
                            }
                        }
                        updateBarChart2();
                    } else {
                        Log.d("Firestore", "Error getting documents: ", task.getException());
                    }
                });








    }
    /*private ArrayList<PieEntry> dataValue1(){
        ArrayList<PieEntry> dataVals=new ArrayList<>();
        dataVals.add(new PieEntry(113,"Estudiantes"));
        dataVals.add(new PieEntry(37,"Egresados"));
        return dataVals;
    }*/
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
    private class MyValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.format("%.1f%%", value);
        }
    }
    private void updatePieChart() {
        pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> entries = new ArrayList<>();

        int totalUsers = totalStudents + totalGraduates;
        if (totalUsers > 0) {
            float percentageStudents = ((float) totalStudents / totalUsers) * 100;
            float percentageGraduates = ((float) totalGraduates / totalUsers) * 100;

            // Asegúrate de que los porcentajes son los correctos
            entries.add(new PieEntry(percentageStudents, "Estudiantes"));
            entries.add(new PieEntry(percentageGraduates, "Egresados"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(1f); // Espacio entre los segmentos
        dataSet.setSelectionShift(1f); // Tamaño de segmento seleccionado


        int[] colorClassArray = {Color.parseColor("#2D68C4"), Color.parseColor("#1E88E5")};
        dataSet.setColors(colorClassArray);


        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTypeface(Typeface.DEFAULT_BOLD);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.1f%%", value);
            }
        });

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterText("");
        pieChart.setCenterTextSize(10f);

        // Configuración de la leyenda
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pieChart.getLegend().setDrawInside(false);
        pieChart.getLegend().setYEntrySpace(0f);
        pieChart.getLegend().setXEntrySpace(10f);

        // Animación
        pieChart.animateY(1400, Easing.EaseInOutQuad);

        // Refresca el gráfico
        pieChart.invalidate();
    }

    private void updateBarChart() {
        BarChart barChart = findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, totalAdministrators));
        entries.add(new BarEntry(1f, totalDelegates));
        entries.add(new BarEntry(2f, newtotalStudents));

        BarDataSet barDataSet = new BarDataSet(entries, "Roles");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Administrador", "Delegado", "Alumno"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1500);
        barChart.invalidate();
    }



    private void updateBarChart2() {
        BarChart barChart = findViewById(R.id.bar_chart2);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> locations = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        int index = 0;
        for (Map.Entry<String, Integer> entry : eventCountByLocation.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            locations.add(entry.getKey()); // Asumiendo que 'locations' tiene los nombres de las ubicaciones
            index++;
        }

        BarDataSet barDataSet = new BarDataSet(entries, "");
        for (int i = 0; i < entries.size(); i++) {
            // Agrega un color para cada entrada
            colors.add(ColorTemplate.JOYFUL_COLORS[i % ColorTemplate.JOYFUL_COLORS.length]);
        }
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Configuración de la leyenda para expandirse verticalmente
        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(5f);
        legend.setTextSize(12f);
        legend.setYEntrySpace(5f);
        legend.setMaxSizePercent(0.5f);
        barChart.setExtraBottomOffset(10f);

        barChart.setExtraBottomOffset(legend.mNeededHeight + legend.getYOffset());
        // Configura los nombres de las ubicaciones y sus colores en la leyenda
        LegendEntry[] legendEntries = new LegendEntry[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = locations.get(i);
            entry.formColor = colors.get(i);
            legendEntries[i] = entry;
        }
        legend.setCustom(legendEntries);
        barChart.setExtraBottomOffset(30f);

        // Desactiva las etiquetas del eje X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(false);

        barChart.invalidate(); // Refresca el gráfico
    }




}

