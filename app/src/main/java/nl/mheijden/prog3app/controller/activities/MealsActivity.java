package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.view.MealAdapter;

public class MealsActivity extends AppCompatActivity implements ReloadCallback {
    private MaaltijdenApp app;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        this.list = findViewById(R.id.mealslist);
        app=new MaaltijdenApp(this);
        app.reloadData(this);
    }

    private void handleViewClick(Meal m){
        Intent intent = new Intent(this, MealsActivity_Detail.class);
        intent.putExtra("Meal", m);
        startActivity(intent);
    }

    @Override
    public void reloaded(boolean result) {
        ListAdapter adapter = new MealAdapter(this, R.layout.listview_meal, app.getMeals(), app.getUser());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal p = (Meal) adapterView.getItemAtPosition(i);
                handleViewClick(p);
            }
        });
        list.setAdapter(adapter);
    }
}
