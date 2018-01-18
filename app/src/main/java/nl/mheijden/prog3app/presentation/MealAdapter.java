package nl.mheijden.prog3app.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MealAdapter extends ArrayAdapter<Meal> {
    /**
     * The default image with the logo of the application
     */
    private static Bitmap defaultImage;
    /**
     * All the meals that
     */
    private final ArrayList<Meal> data;
    /**
     * Context from the activity
     */
    private final Context context;
    /**
     * Current user, for comparison reasons
     */
    private final Student user;
    /**
     * directory to get the images from
     */
    private final File filesDir;

    /**
     * @param context of the activity
     * @param data    list of meals
     * @param user    current user
     */
    public MealAdapter(Context context, ArrayList<Meal> data, Student user) {
        super(context, R.layout.listview_meal, data);
        this.data = data;
        this.context = context;
        this.user = user;
        MealAdapter.defaultImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        this.filesDir = context.getFilesDir();
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @SuppressWarnings("NullableProblems") ViewGroup parent) {
        Meal meal = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_meal, parent, false);
            viewHolder.dish = convertView.findViewById(R.id.meal_titel);
            viewHolder.date = convertView.findViewById(R.id.meal_datumtijd);
            viewHolder.chefID = convertView.findViewById(R.id.meal_kok);
            viewHolder.image = convertView.findViewById(R.id.meal_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (meal != null) {
            if (meal.getChef().equals(user)) {
                viewHolder.dish.setText(meal.getDish() + " " + context.getText(R.string.app_meals_cheficon));
                viewHolder.dish.setTextColor(context.getResources().getColor(R.color.colorDarkRed));
            } else if (meal.getStudents().contains(user)) {
                viewHolder.dish.setText(meal.getDish() + " " + context.getText(R.string.app_meals_checkicon));
                viewHolder.dish.setTextColor(context.getResources().getColor(R.color.colorGreen));
            } else {
                viewHolder.dish.setText(meal.getDish() + " ");
                viewHolder.dish.setTextColor(context.getResources().getColor(R.color.colorSemiWhite));
            }
            viewHolder.date.setText(context.getText(R.string.app_meals_timeicon) + " " + meal.getDate() + " ");
            if (!meal.getChef().getInsertion().equals("null")) {
                viewHolder.chefID.setText(context.getText(R.string.app_meals_cheficon) + meal.getChef().getFirstname() + " " + meal.getChef().getInsertion() + " " + meal.getChef().getLastname());
            } else {
                viewHolder.chefID.setText(context.getText(R.string.app_meals_cheficon) + meal.getChef().getFirstname() + " " + meal.getChef().getLastname());
            }

            File f = new File(filesDir, "mealPictures_" + meal.getId());
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                viewHolder.image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                viewHolder.image.setImageBitmap(MealAdapter.defaultImage);
            }
        }
        return convertView;
    }

    /**
     * @param position of the meal
     * @return Meal that is associated with that position
     */
    public Meal getItem(int position) {
        return data.get(position);
    }

    /**
     * ViewHolder to save the elements of the listview
     */
    private static class ViewHolder {
        /**
         * Name of the dish
         */
        TextView dish;
        /**
         * Date that this meal takes place
         */
        TextView date;
        /**
         * Name of the chef
         */
        TextView chefID;
        /**
         * Image of the meal
         */
        ImageView image;
    }
}
