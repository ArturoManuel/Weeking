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

// Consulta a Firestore para contar estudiantes y egresados
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
        BarChart barChart = findViewById(R.id.bar_chart); // Asegúrate de tener un BarChart con este ID en tu XML
        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, totalAdministrators));
        entries.add(new BarEntry(1f, totalDelegates));
        entries.add(new BarEntry(2f, newtotalStudents));

        BarDataSet barDataSet = new BarDataSet(entries, "Roles");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.setFitBars(true);

        // Configura el eje X para mostrar las etiquetas de los roles
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Administrador", "Delegado", "Alumno"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1500);
        barChart.invalidate(); // Refresca el gráfico
    }


}

