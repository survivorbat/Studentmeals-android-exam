package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.view.MealAdapter;

public class MealsActivity extends AppCompatActivity {
    private MaaltijdenApp app;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);
        app=new MaaltijdenApp(this);
        app.reloadData();

        this.list = findViewById(R.id.mealslist);

        loadList();
    }

    private void loadList(){
        ListAdapter adapter = new MealAdapter(this,R.layout.listview_meal,app.getMeals());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal m = (Meal) adapterView.getItemAtPosition(i);
                handleViewClick(m);
            }
        });
    }
    private void handleViewClick(Meal m){
        Intent intent = new Intent(this, MealsActivity_Detail.class);
        intent.putExtra("Meal", m);
        startActivity(intent);
    }
}
