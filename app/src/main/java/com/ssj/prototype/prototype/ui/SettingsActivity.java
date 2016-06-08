package com.ssj.prototype.prototype.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ssj.prototype.prototype.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        final TextView textView2 = (TextView) findViewById(R.id.textView2);

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        int threshold = settings.getInt("threshold",10000);

        assert seekbar != null;
        assert textView2 != null;

        seekbar.setProgress(threshold);
        textView2.setText(Integer.toString(threshold));

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView2.setText(Integer.toString(progress));
                settings.edit().putInt("threshold",progress).apply();
            }
        });
    }
}
