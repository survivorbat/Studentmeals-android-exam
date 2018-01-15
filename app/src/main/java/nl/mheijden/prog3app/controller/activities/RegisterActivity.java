package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.RegisterControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Student;

public class RegisterActivity extends AppCompatActivity implements RegisterControllerCallback {
    private EditText id,firstname,lastname,insertion,telephonenumber,email,password, passwordconfirm;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.id = findViewById(R.id.register_number);
        this.firstname = findViewById(R.id.register_voornaam);
        this.insertion = findViewById(R.id.register_tusesnvoegsel);
        this.lastname = findViewById(R.id.register_achternaam);
        this.telephonenumber = findViewById(R.id.register_telephone);
        this.email = findViewById(R.id.register_email);
        this.password = findViewById(R.id.register_pass);
        this.passwordconfirm = findViewById(R.id.register_passconfirm);
        confirmButton = findViewById(R.id.register_confirmbutton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButton();
            }
        });
    }

    private void confirmButton(){
        boolean errorFree=true;
        if(id.getText().toString().equals("")){
            id.setError(getText(R.string.app_input_error_nousername));
            errorFree=false;
        }
        if(firstname.getText().toString().equals("")){
            firstname.setError(getText(R.string.app_input_error_nofirstname));
            errorFree=false;
        }
        if(lastname.getText().toString().equals("")){
            lastname.setError(getText(R.string.app_input_error_nolastname));
            errorFree=false;
        }
        if(email.getText().toString().equals("")){
            email.setError(getText(R.string.app_input_error_noemail));
            errorFree=false;
        }
        if(telephonenumber.getText().toString().equals("")){
            telephonenumber.setError(getText(R.string.app_input_error_notelephonenumber));
            errorFree=false;
        }
        if(password.getText().toString().equals("")){
            password.setError(getText(R.string.app_input_error_nopass));
            errorFree=false;
        }
        if(!passwordconfirm.getText().toString().equals(password.getText().toString())){
            passwordconfirm.setError(getText(R.string.app_input_error_notsamepassword));
            errorFree=false;
        }
        if(errorFree){
            Student newStudent = new Student();
            newStudent.setFirstname(firstname.getText().toString());
            newStudent.setInsertion(insertion.getText().toString());
            newStudent.setLastname(lastname.getText().toString());
            newStudent.setStudentNumber(id.getText().toString());
            newStudent.setEmail(email.getText().toString());
            newStudent.setPhonenumber(telephonenumber.getText().toString());
            newStudent.setPassword(password.getText().toString());

            MaaltijdenApp app = new MaaltijdenApp(this);
            app.register(newStudent, this);
        }
    }

    @Override
    public void newStudentAdded(boolean result) {
        confirmButton.setClickable(true);
        if (result) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this,"Er ging iets mis bij het toevoegen, probeer het nog een keer", Toast.LENGTH_LONG).show();
        }
    }
}
