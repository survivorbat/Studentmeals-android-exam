package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;

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
        Button registerbutton = findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerHandler();
            }
        });
    }

    private void submitLogin(){
        errorfield.setText("");
        String user = studentNumber.getText().toString();
        String pass = password.getText().toString();

        boolean errorFree=true;
        if(user.equals("") || user.length()==0){
            studentNumber.setError(getText(R.string.app_input_error_nousername));
            errorFree=false;
        }
        if(pass.equals("") || password.length()==0){
            password.setError(getText(R.string.app_input_error_nopass));
            errorFree=false;
        }

        if(errorFree){
            errorfield.setText(R.string.app_input_error_loading);
            app.login(user, pass, this);
        }
    }
    private void registerHandler(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void login(String response) {
        if(response.equals("error")){
            errorfield.setText(R.string.app_input_error_wrong);
            errorfield.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else if(response.equals("success")) {
            errorfield.setText("");
            Intent i = new Intent(this,DashboardActivity.class);
            startActivity(i);
        }
    }
}
