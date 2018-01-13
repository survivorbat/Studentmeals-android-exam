package nl.mheijden.prog3app.controller.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.JoinControllerCallback;
import nl.mheijden.prog3app.model.domain.FellowEater;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;

public class MealsActivity_Join extends AppCompatActivity implements JoinControllerCallback {
    private Meal meal;
    private EditText amountOfPeople;
    private MaaltijdenApp app;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals__join);
        Intent i = getIntent();
        meal = (Meal) i.getSerializableExtra("Meal");
        TextView title = findViewById(R.id.joinscreen_title);
        title.setText(meal.getDish()+"");

        amountOfPeople = findViewById(R.id.amountOfPeople);

        TextView info = findViewById(R.id.joinscreen_mealinfo);
        info.setText(meal.getDate()+"\n"+meal.getAmountOfEaters()+"/"+meal.getMax()+" "+getText(R.string.app_dashboard_button_students)+"\n\n"+meal.getInfo()+"");
        if(meal.getMax()-meal.getAmountOfEaters()-1<=0){
            amountOfPeople.setEnabled(false);
            amountOfPeople.setText(0);
        } else {
            amountOfPeople.setHint(getText(R.string.app_joinscreen_chooseamount)+" (max. "+(meal.getMax()-meal.getAmountOfEaters()-1)+")");
        }
        final Button confirmButton = findViewById(R.id.joinscreen_confirmbutton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmJoin();
            }
        });
    }

    private void confirmJoin(){
        if(Integer.parseInt(amountOfPeople.getText().toString())>(meal.getMax()-meal.getAmountOfEaters()-1)){
            amountOfPeople.setError(getText(R.string.app_input_error_tooManyPeople)+"(max."+(meal.getMax()-meal.getAmountOfEaters()-1)+")");
        } if(Integer.parseInt(amountOfPeople.getText().toString())<0){
            amountOfPeople.setError(getText(R.string.app_input_error_notnegative));
        } else {
            app = new MaaltijdenApp(this);
            FellowEater fellowEater = new FellowEater();
            fellowEater.setGuests(Integer.parseInt(amountOfPeople.getText().toString()));
            fellowEater.setMeal(meal);
            fellowEater.setStudent(app.getUser());
            app.addFellowEater(fellowEater,this);
        }
    }

    @Override
    public void onJoinComplete(boolean result) {

    }
}
