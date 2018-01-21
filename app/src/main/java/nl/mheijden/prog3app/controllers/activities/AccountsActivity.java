package nl.mheijden.prog3app.controllers.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.ChangeStudentCallback;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;
import nl.mheijden.prog3app.controllers.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Student;

public class AccountsActivity extends AppCompatActivity implements ChangeStudentCallback, InvalidTokenCallback, ReloadCallback {
    private Student student;
    private EditText firstname, lastname, insertion, phonenumber, email, password, passwordconfirm;
    private Bitmap newImage;
    private Button imageAdd;
    private ImageView previewImage;
    private MaaltijdenApp app;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        app = new MaaltijdenApp(this);
        student = app.getUser();

        imageAdd = findViewById(R.id.accounts_selectpic);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        TextView studentnummer = findViewById(R.id.account_number);
        firstname = findViewById(R.id.accounts_voornaam);
        insertion = findViewById(R.id.accounts_insertion);
        lastname = findViewById(R.id.accounts_lastname);
        phonenumber = findViewById(R.id.accounts_telephone);
        email = findViewById(R.id.accounts_email);
        password = findViewById(R.id.accounts_password);
        passwordconfirm = findViewById(R.id.accounts_passwordconfirm);
        previewImage = findViewById(R.id.accounts_previewimage);

        studentnummer.setText(student.getstudentNumber() + "");
        firstname.setText(student.getFirstname() + "");
        if (student.getInsertion() != null && !student.getInsertion().trim().equals("") && !student.getInsertion().equals("null")) {
            insertion.setText(student.getInsertion() + "");
        }
        lastname.setText(student.getLastname() + "");
        phonenumber.setText(student.getPhonenumber() + "");
        email.setText(student.getEmail() + "");
        phonenumber.setText(student.getPhonenumber() + "");

        Button confirmButton = findViewById(R.id.accounts_confirmbutton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });

        File filesDir = getFilesDir();
        File f = new File(filesDir, "studentPictures_" + student.getstudentNumber());
        FileInputStream fis;
        try {
            fis = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            previewImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            previewImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        }
    }

    private void applyChanges() {
        boolean errorFree = true;
        if (firstname.getText().toString().equals("")) {
            firstname.setError(getText(R.string.app_input_error_nofirstname));
            errorFree = false;
        }
        if (lastname.getText().toString().equals("")) {
            lastname.setError(getText(R.string.app_input_error_nolastname));
            errorFree = false;
        }
        if (email.getText().toString().equals("")) {
            email.setError(getText(R.string.app_input_error_noemail));
            errorFree = false;
        }
        if (phonenumber.getText().toString().equals("")) {
            phonenumber.setError(getText(R.string.app_input_error_notelephonenumber));
            errorFree = false;
        }
        if (!password.getText().toString().equals("") && !passwordconfirm.getText().toString().equals(password.getText().toString())) {
            passwordconfirm.setError(getText(R.string.app_input_error_notsamepassword));
            errorFree = false;
        }
        if (errorFree) {
            student.setFirstname(firstname.getText().toString().trim());
            student.setInsertion(insertion.getText().toString().trim().toLowerCase());
            student.setLastname(lastname.getText().toString().trim());
            student.setEmail(email.getText().toString());
            student.setPhonenumber(phonenumber.getText().toString().trim());
            if (!password.getText().toString().equals("")) {
                student.setPassword(password.getText().toString());
            }
            student.setPassword(password.getText().toString());
            student.setImage(newImage);
            MaaltijdenApp app = new MaaltijdenApp(this);
            app.changeStudent(student, this);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.app_newmeal_selectpicture)), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1 && null != data) {
            decodeUri(data.getData());
            imageAdd.setText(getText(R.string.app_input_imagechosen));
            imageAdd.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    @Override
    public void onUserChanged(boolean result) {
        if (result) {
            Toast.makeText(this, R.string.app_changedstudent_success, Toast.LENGTH_SHORT).show();
            app.reloadStudents(this);
        } else {
            Toast.makeText(this, getText(R.string.app_error_conn), Toast.LENGTH_SHORT).show();
        }
    }

    private void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            assert parcelFD != null;
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            newImage = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            previewImage.setImageBitmap(newImage);

        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }

    @Override
    public void invalidToken() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    public void reloaded(boolean result) {
        this.finish();
    }
}
