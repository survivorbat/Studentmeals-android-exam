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
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;
import nl.mheijden.prog3app.controllers.callbacks.NewMealControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;

public class NewMealActivity extends AppCompatActivity implements NewMealControllerCallback, InvalidTokenCallback {
    private final ArrayList<String> rs = new ArrayList<>();
    private MaaltijdenApp app;
    private EditText dish, info, price, date, time, max;
    private Switch doesCookEat;
    private Button imageAdd;
    private Bitmap newImage;
    private ImageView previewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        app = new MaaltijdenApp(this,this);
        for (Meal m : app.getMeals()) {
            rs.add(m.getDate());
        }
        imageAdd = findViewById(R.id.newmeal_selectpic);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        dish = findViewById(R.id.newmeal_dish);
        info = findViewById(R.id.newmeal_info);
        price = findViewById(R.id.newmeal_price);
        date = findViewById(R.id.newmeal_date);
        time = findViewById(R.id.newmeal_time);
        max = findViewById(R.id.newmeal_max);
        doesCookEat = findViewById(R.id.newmeal_doescookeat);
        previewImage = findViewById(R.id.newmeal_imagepreview);
        Button addButton = findViewById(R.id.addmeal_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMeal();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getText(R.string.app_newmeal_selectpicture)), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1 && null != data) {
            decodeUri(data.getData());
            imageAdd.setText(getText(R.string.app_input_imagechosen));
            imageAdd.setTextColor(getResources().getColor(R.color.colorGreen));
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

    private void confirmMeal() {
        boolean errorFree = true;
        if (date.getText().toString().trim().isEmpty() || !isValidDate(date.getText().toString())) {
            errorFree = false;
            date.setError(getText(R.string.newmeal_error_dateformat));
            if (rs.contains(date.getText().toString() + " " + time.getText().toString())) {
                date.setError(getText(R.string.newmeal_takendate));
            }
        }
        if(!time.getText().toString().matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$") || time.toString().trim().isEmpty()){
            errorFree=false;
            time.setError(getText(R.string.app_input_error_time));
        }
        if (dish.getText().toString().trim().isEmpty()) {
            dish.setError(getText(R.string.app_input_error_dish));
            errorFree = false;
        }
        if (info.getText().toString().trim().isEmpty()) {
            info.setError(getText(R.string.app_error_noinfo));
            errorFree = false;
        }
        if (price.toString().trim().isEmpty()) {
            info.setError(getText(R.string.app_error_noprice));
            errorFree = false;
        }
        if (max.getText().toString().equals("")) {
            max.setError(getText(R.string.app_input_error_maxpeople));
            errorFree = false;
        }
        if (errorFree) {
            Meal meal = new Meal();
            meal.setMaxFellowEaters(Integer.parseInt(max.getText().toString()));
            meal.setPrice(Double.parseDouble(price.getText().toString()));
            meal.setDish(dish.getText().toString());
            meal.setInfo(info.getText().toString());
            meal.setDoesCookEat(doesCookEat.isChecked());
            String[] dateForm = date.getText().toString().split("-");
            meal.setDate(dateForm[2]+"-"+dateForm[1]+"-"+dateForm[0] + " " + time.getText().toString());
            meal.setChef(app.getUser());
            meal.setPicture(newImage);
            app.addMeal(meal, this);
        }
    }

    @Override
    public void addedMeal(boolean result) {
        if (result) {
            this.finish();
        } else {
            Toast.makeText(this, getText(R.string.app_error_conn), Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isValidDate(String text) {
        if (text == null || !text.matches("/^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$")) return false;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    @Override
    public void invalidToken() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
