package nl.mheijden.prog3app.controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;
import nl.mheijden.prog3app.controllers.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.presentation.MealAdapter;

public class MealsActivity extends AppCompatActivity implements ReloadCallback, InvalidTokenCallback {
    private MaaltijdenApp app;
    private ListView list;
    private SwipeRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        this.list = findViewById(R.id.mealslist);

        app = new MaaltijdenApp(this,this);

        ArrayAdapter adapter = new MealAdapter(this, app.getMeals(), app.getUser());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal p = (Meal) adapterView.getItemAtPosition(i);
                handleViewClick(p);
            }
        });
        list.setAdapter(adapter);

        layout = findViewById(R.id.meals_swiperefresh);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onReloadInitiated();
            }
        });
    }
    private void handleViewClick(Meal m) {
        Intent intent = new Intent(this, MealsActivity_Detail.class);
        intent.putExtra("Meal", m);
        startActivity(intent);
    }

    protected void onResume(){
        super.onResume();
        onReloadInitiated();
    }

    private void onReloadInitiated() {
        layout.setRefreshing(true);
        app.reloadMeals(this);
    }

    public void reloaded(boolean result) {
        if (result) {
            ListAdapter adapter = new MealAdapter(this, app.getMeals(), app.getUser());
            list.setAdapter(adapter);
            if (layout.isRefreshing()) {
                layout.setRefreshing(false);
                Toast.makeText(this, R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void invalidToken() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
