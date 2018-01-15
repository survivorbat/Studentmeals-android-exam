package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
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
import nl.mheijden.prog3app.controller.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.view.MealAdapter;

public class MealsActivity extends AppCompatActivity implements ReloadCallback {
    private MaaltijdenApp app;
    private ListView list;
    private SwipeRefreshLayout layout;
    private ArrayAdapter adapter;

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

    protected void onResume() {
        super.onResume();
        Toast.makeText(this, getText(R.string.app_reloading),Toast.LENGTH_SHORT).show();
        onReloadInitiated();
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
            if(layout.isRefreshing()){
                layout.setRefreshing(false);
                Toast.makeText(this,R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            }
            adapter.clear();
            adapter.addAll(app.getMeals());
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this,R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
