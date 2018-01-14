package nl.mheijden.prog3app.controller.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;

public class NewMealActivity extends AppCompatActivity {
    private MaaltijdenApp app;
    private EditText dish, info, price, date, time, max;
    private Switch doesCookEat;
    private TextView filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        Button imageAdd = findViewById(R.id.newmeal_selectpic);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery(1);
            }
        });
        dish = findViewById(R.id.newmeal_dish);
        info = findViewById(R.id.newmeal_info);
        price = findViewById(R.id.newmeal_price);
        date = findViewById(R.id.newmeal_date);
        time = findViewById(R.id.newmeal_time);
        max = findViewById(R.id.newmeal_max);
        doesCookEat = findViewById(R.id.newmeal_doescookeat);
        filename = findViewById(R.id.newmeal_filename);

    }
    public void openGallery(int req_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getText(R.string.app_newmeal_selectpicture)), req_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            filename.setText(selectedImageUri+"");
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    private void confirmMeal(){
        ArrayList<String> rs= new ArrayList<>();
        for(Meal m : app.getMeals()){
            rs.add(m.getDate());
        }
    }
}
