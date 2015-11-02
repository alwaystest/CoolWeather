package com.software.eric.coolweather.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.software.eric.coolweather.R;


public class SettingsActivity extends Activity{

    NumberPicker np;
    Button configure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        configure = (Button) findViewById(R.id.configure);

        np.setMinValue(1);
        np.setMaxValue(24);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int autoUpdateTime = prefs.getInt("auto_update_time",24);
        np.setValue(autoUpdateTime);

        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit();
                editor.putInt("auto_update_time",np.getValue());
                editor.commit();
            }
        });
    }
}
