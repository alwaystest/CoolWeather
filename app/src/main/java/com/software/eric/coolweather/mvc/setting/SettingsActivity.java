package com.software.eric.coolweather.mvc.setting;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.constants.ExtraConstant;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends Activity {

    @BindView(R.id.numberPicker)
    NumberPicker np;

    @BindView(R.id.configure)
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
        int autoUpdateTime = prefs.getInt(ExtraConstant.AUTO_UPDATE_TIME, ExtraConstant.DEFAULT_AUTO_UPDATE_TIME);
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

    public static void actionStart(Context context) {
        Intent i = new Intent(context, SettingsActivity.class);
        context.startActivity(i);
    }
}
