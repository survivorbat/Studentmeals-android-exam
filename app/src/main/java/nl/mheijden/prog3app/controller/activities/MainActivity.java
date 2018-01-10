package nl.mheijden.prog3app.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.view.MealAdapter;

public class MainActivity extends AppCompatActivity {
    private MaaltijdenApp app;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.app = new MaaltijdenApp(this);
        this.list = findViewById(R.id.lijsttest);
        app.addDummy();
        ListAdapter adapter = new MealAdapter(this, R.layout.listview_meal, app.getMeals());
        this.list.setAdapter(adapter);
    }
}
