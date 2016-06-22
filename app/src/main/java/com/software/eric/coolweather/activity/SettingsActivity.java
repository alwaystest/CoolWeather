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
import com.software.eric.coolweather.constants.ExtraConstant;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsActivity extends Activity {

    @Bind(R.id.numberPicker)
    NumberPicker np;

    @Bind(R.id.configure)
    Button configure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        np.setMinValue(1);
        np.setMaxValue(24);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int autoUpdateTime = prefs.getInt(ExtraConstant.AUTO_UPDATE_TIME, 24);
        np.setValue(autoUpdateTime);

        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit();
                editor.putInt(ExtraConstant.AUTO_UPDATE_TIME, np.getValue());
                editor.apply();
                finish();
            }
        });
    }
}
