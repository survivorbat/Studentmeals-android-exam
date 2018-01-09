package nl.mheijden.prog3app.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;

public class MainActivity extends AppCompatActivity {
    private MaaltijdenApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.app = new MaaltijdenApp(this);
    }
}
