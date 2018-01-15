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
import android.widget.Toast;

import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.NewMealControllerCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

public class NewMealActivity extends AppCompatActivity implements NewMealControllerCallback {
    private MaaltijdenApp app;
    private EditText dish, info, price, date, time, max;
    private Switch doesCookEat;
    private TextView filename;
    private Button addButton;
    private ArrayList<String> rs= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        app = new MaaltijdenApp(this);
        for(Meal m : app.getMeals()){
            rs.add(m.getDate());
        }
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
        addButton = findViewById(R.id.addmeal_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmMeal();
            }
        });
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
        boolean errorFree=true;
        if(date.getText().toString().trim().isEmpty() || time.toString().trim().isEmpty() || !date.getText().toString().matches("^([0-9]{4}[-/]?((0[13-9]|1[012])[-/]?(0[1-9]|[12][0-9]|30)|(0[13578]|1[02])[-/]?31|02[-/]?(0[1-9]|1[0-9]|2[0-8]))|([0-9]{2}(([2468][048]|[02468][48])|[13579][26])|([13579][26]|[02468][048]|0[0-9]|1[0-6])00)[-/]?02[-/]?29)$")){
            errorFree=false;
            date.setError(getText(R.string.newmeal_error_dateformat));
            if(rs.contains(date.getText().toString()+" "+time.getText().toString())){
                date.setError(getText(R.string.newmeal_takendate));
            }
        }
        if(dish.getText().toString().trim().isEmpty()){
            dish.setError(getText(R.string.app_input_error_dish));
            errorFree=false;
        }
        if(info.getText().toString().trim().isEmpty()){
            info.setError(getText(R.string.app_error_noinfo));
            errorFree=false;
        }
        if(price.toString().trim().isEmpty()){
            info.setError(getText(R.string.app_error_noprice));
            errorFree=false;
        }
        if(max.getText().toString().equals("")){
            max.setError(getText(R.string.app_input_error_maxpeople));
            errorFree=false;
        }
        if(errorFree){
            Meal meal = new Meal();
            meal.setMax(Integer.parseInt(max.getText().toString()));
            meal.setPrice(Double.parseDouble(price.getText().toString()));
            meal.setDish(dish.getText().toString());
            meal.setInfo(info.getText().toString());
            meal.setDoesCookEat(doesCookEat.isChecked());
            meal.setDate(date.getText().toString()+" "+time.getText().toString());
            meal.setChefID(app.getUser());
            app.addMeal(meal, this);
            System.out.println("sending meal");
        }
    }

    @Override
    public void addedMeal(boolean result) {
        if(result){
            Intent i = new Intent(this, MealsActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this,getText(R.string.app_error_conn), Toast.LENGTH_SHORT).show();
        }
    }
}
