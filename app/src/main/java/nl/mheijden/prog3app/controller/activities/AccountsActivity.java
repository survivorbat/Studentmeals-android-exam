package nl.mheijden.prog3app.controller.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Student;

public class AccountsActivity extends AppCompatActivity {
    Student student = new MaaltijdenApp(this).getUser();
    EditText firstname, lastname, insertion, phonenumber, email, password, passwordconfirm;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        TextView studentnummer = findViewById(R.id.account_number);
        firstname = findViewById(R.id.accounts_voornaam);
        insertion = findViewById(R.id.accounts_insertion);
        lastname = findViewById(R.id.accounts_lastname);
        phonenumber = findViewById(R.id.accounts_telephone);
        email = findViewById(R.id.accounts_email);
        password = findViewById(R.id.accounts_password);
        passwordconfirm = findViewById(R.id.accounts_passwordconfirm);

        studentnummer.setText(student.getstudentNumber()+"");
        firstname.setText(student.getFirstname()+"");
        if(student.getInsertion()!=null && !student.getInsertion().trim().equals("")){
            insertion.setText(student.getInsertion()+"");
        }
        lastname.setText(student.getLastname()+"");
        phonenumber.setText(student.getPhonenumber()+"");
        email.setText(student.getEmail()+"");
    }
}
