package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Student;
import nl.mheijden.prog3app.view.MealAdapter;

public class MainActivity extends AppCompatActivity implements LoginControllerCallback {
    private MaaltijdenApp app;
    private TextView errorfield;
    private EditText studentNumber;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.app = new MaaltijdenApp(this);
        this.studentNumber=findViewById(R.id.login_number);
        this.password=findViewById(R.id.login_pass);
        this.errorfield=findViewById(R.id.login_errorfield);
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLogin();
            }
        });
    }

    public void submitLogin(){
        errorfield.setText("");
        String user = studentNumber.getText().toString();
        String pass = password.getText().toString();
        if(user.equals("") || user.length()==0){
            errorfield.setText(R.string.app_input_error_nousername);
        } else if(pass.equals("") || password.length()==0){
            errorfield.setText(R.string.app_input_error_nopass);
        } else {
            errorfield.setText(R.string.app_input_error_loading);
            app.login(this, user, pass, this);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void login(String response) {
        if(response.equals("error")){
            errorfield.setText(R.string.app_input_error_wrong);
            errorfield.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else if(response.equals("success")) {
            Intent i = new Intent(this,DashboardActivity.class);
            i.putExtra("students",app.getStudents());
            i.putExtra("meals",app.getMeals());
            i.putExtra("fellowEaters",app.getFellowEaters());
            startActivity(i);
        }
    }
}
