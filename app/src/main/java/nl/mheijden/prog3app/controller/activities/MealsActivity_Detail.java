package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.Meal;

public class MealsActivity_Detail extends AppCompatActivity {
    private Meal meal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals__detail);
        Intent i = getIntent();
        this.meal = (Meal) i.getSerializableExtra("Meal");
        if (this.meal != null) {
            TextView meal_title = findViewById(R.id.meal_title);
            TextView meal_desc = findViewById(R.id.meal_desc);
            TextView meal_chef = findViewById(R.id.meal_chef);
            TextView meal_price = findViewById(R.id.meal_price);
            TextView meal_date = findViewById(R.id.meal_date);
            TextView meal_amount = findViewById(R.id.meal_eaters);
            TextView meal_eaters = findViewById(R.id.meal_felloweaterlist);
            Button meal_addbutton = findViewById(R.id.meal_eatwithbutton);

            meal_title.setText(meal.getDish() + "");
            meal_desc.setText(meal.getInfo() + "");
            if (meal.getChefID().getInsertion() != "null") {
                meal_chef.setText(getText(R.string.app_meals_cheficon)+" "+meal.getChefID().getFirstname() + " " + meal.getChefID().getInsertion() + "" + meal.getChefID().getLastname());
            } else {
                meal_chef.setText(getText(R.string.app_meals_cheficon)+" "+meal.getChefID().getFirstname() + " " + meal.getChefID().getLastname());
            }
            meal_price.setText(getText(R.string.app_meals_moneyicon)+" €"+meal.getPrice());
            meal_date.setText(getText(R.string.app_meals_timeicon)+" "+meal.getDate() + "");
            meal_amount.setText(getText(R.string.app_dashboard_button_students)+" "+meal.getAmountOfEaters()+"/"+meal.getMax());
            meal_addbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eatWith();
                }
            });

            StringBuilder eaters= new StringBuilder(getText(R.string.app_meals_eaters).toString());
            boolean first=true;
            for(FellowEater e : meal.getFelloweaters()){
                if(!first){
                    eaters.append(", ");
                }
                eaters.append(e.getStudent()).append(" (").append(e.getAmount()).append(")");
                first=false;
            }
            meal_eaters.setText(eaters+"");
        }
    }

    private void eatWith(){
        Intent i = new Intent(this, MealsActivity_Join.class);
        i.putExtra("Meal",meal);
        startActivity(i);
    }
}
