package nl.mheijden.prog3app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.Meal;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MealAdapter extends ArrayAdapter<Meal> {
    private ArrayList<Meal> data;
    private Context context;

    public MealAdapter(Context context, int resource, ArrayList<Meal> data) {
        super(context, resource, data);
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Meal meal = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_meal, parent, false);
            viewHolder.dish = convertView.findViewById(R.id.meal_titel);
            viewHolder.date = convertView.findViewById(R.id.meal_datumtijd);
            viewHolder.chefID = convertView.findViewById(R.id.meal_kok);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dish.setText(meal.getDish() + " ");
        viewHolder.date.setText(context.getText(R.string.app_meals_timeicon)+meal.getDate() + " ");
        if(meal.getChefID().getInsertion()!="null"){
            viewHolder.chefID.setText(context.getText(R.string.app_meals_cheficon)+meal.getChefID().getFirstname() + " "+meal.getChefID().getInsertion()+ "" + meal.getChefID().getLastname());
        } else {
            viewHolder.chefID.setText(context.getText(R.string.app_meals_cheficon)+meal.getChefID().getFirstname() + " " + meal.getChefID().getLastname());
        }

        return convertView;
    }

    public Meal getItem(int position) {
        return data.get(position);
    }

    private static class ViewHolder {
        TextView dish;
        TextView date;
        TextView chefID;
        ImageView imageUrl;
    }
}
