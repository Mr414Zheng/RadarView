package com.demo.radarview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.demo.radarview.widget.LegendProperty;
import com.demo.radarview.widget.RadarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadarView radarView = findViewById(R.id.radarView);
        LegendProperty legendProperty = new LegendProperty();
        legendProperty.setAD(670);
        legendProperty.setAP(120);
        legendProperty.setARM(560);
        legendProperty.setCritical(800);
        legendProperty.setMR(350);
        legendProperty.setSpeed(436);
        radarView.setData(legendProperty);
    }
}
