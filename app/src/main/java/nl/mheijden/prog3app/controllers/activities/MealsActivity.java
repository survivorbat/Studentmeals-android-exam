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
import android.widget.ListView;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.presentation.MealAdapter;

public class MealsActivity extends AppCompatActivity implements ReloadCallback {
    private MaaltijdenApp app;
    private ArrayAdapter adapter;
    private ListView list;
    private SwipeRefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        this.list = findViewById(R.id.mealslist);

        app=new MaaltijdenApp(this);

        adapter = new MealAdapter(this, R.layout.listview_meal, app.getMeals(), app.getUser());
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

    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("SHOULDRELOAD",false)){
            onReloadInitiated();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("SHOULDRELOAD",false);
            editor.apply();
        }
    }

    private void handleViewClick(Meal m){
        Intent intent = new Intent(this, MealsActivity_Detail.class);
        intent.putExtra("Meal", m);
        startActivity(intent);
    }
    private void onReloadInitiated(){
        app.reloadMeals(this);
    }

    @Override
    public void reloaded(boolean result) {
        if(result){
            adapter.clear();
            adapter.addAll(app.getMeals());
            adapter.notifyDataSetChanged();
            if(layout.isRefreshing()){
                layout.setRefreshing(false);
                Toast.makeText(this,R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
