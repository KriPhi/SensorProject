package com.vu.lorasensorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

public class userSettings extends AppCompatActivity {

    private CheckBox checkBox1, checkBox2, checkBox3;
    private Button buttonBack;
    private boolean isButton2Enabled = true, isButton3Enabled = true, isButton4Enabled = true;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        buttonBack = findViewById(R.id.bckBtnUS);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        isButton2Enabled = sharedPreferences.getBoolean("isButton2Enabled", false);
        isButton3Enabled = sharedPreferences.getBoolean("isButton3Enabled", false);
        isButton4Enabled = sharedPreferences.getBoolean("isButton4Enabled", false);
        checkBox1.setChecked(isButton2Enabled);
        checkBox2.setChecked(isButton3Enabled);
        checkBox3.setChecked(isButton4Enabled);


        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.getId() == R.id.checkBox) {
                    isButton2Enabled = isChecked;
                } else if (buttonView.getId() == R.id.checkBox2) {
                    isButton3Enabled = isChecked;
                } else if (buttonView.getId() == R.id.checkBox3) {
                    isButton4Enabled = isChecked;
                }

                Intent intent = new Intent();
                intent.putExtra("isButton2Enabled", isButton2Enabled);
                intent.putExtra("isButton3Enabled", isButton3Enabled);
                intent.putExtra("isButton4Enabled", isButton4Enabled);
                setResult(RESULT_OK, intent);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isButton2Enabled", isButton2Enabled);
                editor.putBoolean("isButton3Enabled", isButton3Enabled);
                editor.putBoolean("isButton4Enabled", isButton4Enabled);
                editor.apply();
            }
        };

        checkBox1.setOnCheckedChangeListener(checkedChangeListener);
        checkBox2.setOnCheckedChangeListener(checkedChangeListener);
        checkBox3.setOnCheckedChangeListener(checkedChangeListener);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
