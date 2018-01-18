package nl.mheijden.prog3app.controllers.activities;

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
import nl.mheijden.prog3app.controllers.callbacks.LoginControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;

public class MainActivity extends AppCompatActivity implements LoginControllerCallback {
    /**
     * Application domain class
     */
    private MaaltijdenApp app;
    private TextView errorfield;
    private EditText studentNumber;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.app = new MaaltijdenApp(this);
        this.studentNumber = findViewById(R.id.login_number);
        this.password = findViewById(R.id.login_pass);
        this.errorfield = findViewById(R.id.login_errorfield);
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

    /**
     * Called when login button is pressed
     */
    private void submitLogin() {
        errorfield.setText("");
        String user = studentNumber.getText().toString();
        String pass = password.getText().toString();

        boolean errorFree = true;
        if (user.equals("") || user.length() == 0) {
            studentNumber.setError(getText(R.string.app_input_error_nousername));
            errorFree = false;
        }
        if (pass.equals("") || password.length() == 0) {
            password.setError(getText(R.string.app_input_error_nopass));
            errorFree = false;
        }

        if (errorFree) {
            errorfield.setText(R.string.app_input_error_loading);
            app.login(user, pass, this);
        }
    }

    /**
     * Is called when the register button is pressed
     */
    private void registerHandler() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    /**
     * @param response from the application
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void login(String response) {
        switch (response) {
            case "errorwrong":
                errorfield.setText(R.string.app_input_error_wrong);
                break;
            case "errorobj":
                errorfield.setText(R.string.app_error_obj);
                break;
            case "errorconn":
                errorfield.setText(R.string.app_error_conn);
                break;
            case "success":
                errorfield.setText("");
                Intent i = new Intent(this, DashboardActivity.class);
                startActivity(i);
                break;
        }
        errorfield.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }
}
