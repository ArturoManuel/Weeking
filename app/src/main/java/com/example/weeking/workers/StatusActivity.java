package com.example.weeking.workers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weeking.R;

public class StatusActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        radioGroup=findViewById(R.id.radioGroup);
        int savedSelection = getSavedSelection();

        if (savedSelection != -1) {
            radioGroup.check(savedSelection);
        }
        findViewById(R.id.btnSaveStatus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if(selectedRadioButtonId != -1){
                    savedSelection(selectedRadioButtonId);
                    Toast.makeText(StatusActivity.this, "Estado guardado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int getSavedSelection() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("SelectedRadioButtonId", -1);
    }
    private void savedSelection(int selectedRadioButtonId){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SelectedRadioButtonId", selectedRadioButtonId);
        editor.apply();
    }
}