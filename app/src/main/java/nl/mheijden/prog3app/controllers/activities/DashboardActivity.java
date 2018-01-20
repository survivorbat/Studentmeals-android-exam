package nl.mheijden.prog3app.controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;

public class DashboardActivity extends AppCompatActivity implements InvalidTokenCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button button1 = findViewById(R.id.mealsButton);
        Button button2 = findViewById(R.id.studentsButton);
        Button button3 = findViewById(R.id.newMealButton);
        Button button4 = findViewById(R.id.accountButton);
        Button button5 = findViewById(R.id.dashboard_uitloggen);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMeals();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudents();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewMeal();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccount();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    private void openMeals() {
        Intent i = new Intent(this, MealsActivity.class);
        startActivity(i);
    }

    private void openNewMeal() {
        Intent i = new Intent(this, NewMealActivity.class);
        startActivity(i);
    }

    private void openStudents() {
        Intent i = new Intent(this, StudentsActivity.class);
        startActivity(i);
    }

    private void openAccount() {
        Intent i = new Intent(this, AccountsActivity.class);
        startActivity(i);
    }
    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LOGGEDIN", false);
        editor.apply();
        invalidToken();
    }
    @Override
    public void invalidToken() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
