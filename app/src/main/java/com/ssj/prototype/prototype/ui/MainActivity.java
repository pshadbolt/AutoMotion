package com.ssj.prototype.prototype.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ssj.prototype.prototype.R;
import com.ssj.prototype.prototype.developer.DebugActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchGarage(View view) {
        startActivity(new Intent(this, GarageActivity.class));
    }

    public void launchDebug(View view) {
        startActivity(new Intent(this, DebugActivity.class));
    }

}
