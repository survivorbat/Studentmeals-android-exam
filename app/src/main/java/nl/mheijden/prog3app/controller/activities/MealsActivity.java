package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.view.MealAdapter;

public class MealsActivity extends AppCompatActivity implements ReloadCallback {
    private MaaltijdenApp app;
    private ListView list;
    private SwipeRefreshLayout layout;
    private ListAdapter adapter;

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
    public void onResume() {
        super.onResume();
        onReloadInitiated();
    }

    private void handleViewClick(Meal m){
        Intent intent = new Intent(this, MealsActivity_Detail.class);
        intent.putExtra("Meal", m);
        startActivity(intent);
    }
    private void onReloadInitiated(){
        Toast.makeText(this, getText(R.string.app_loading), Toast.LENGTH_SHORT).show();
        layout.setRefreshing(true);
        app.reloadMeals(this);
    }
    @Override
    public void reloaded(boolean result) {
        if(result){
            if(layout.isRefreshing()){
                layout.setRefreshing(false);
                Toast.makeText(this,R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            }
            adapter = new MealAdapter(this, R.layout.listview_meal, app.getMeals(), app.getUser());
            list.setAdapter(adapter);
        } else {
            Toast.makeText(this,R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
