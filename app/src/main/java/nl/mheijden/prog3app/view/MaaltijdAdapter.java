package nl.mheijden.prog3app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mheijden.prog3app.model.domain.Maaltijd;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class MaaltijdAdapter extends ArrayAdapter<Maaltijd> {
    private ArrayList<Maaltijd> data;

    public MaaltijdAdapter(Context context, int resource, ArrayList<Maaltijd> data) {
        super(context, resource, data);
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Maaltijd maaltijd = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.personlist_view_row, parent, false);
//            viewHolder.id = convertView.findViewById(R.id.tv_id);
//            viewHolder.naam = convertView.findViewById(R.id.tv_naam);
//            viewHolder.aantal = convertView.findViewById(R.id.tv_aantalspottings);
//            viewHolder.locaties = convertView.findViewById(R.id.tv_spottings);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.id.setText(person.getId() + "");
//        viewHolder.naam.setText(person.getNaam() + "");
//        viewHolder.aantal.setText(person.getAantalSpottings() + "");
        if (/*person.getSpottings().length() < 120*/true) {
//            viewHolder.locaties.setText(person.getSpottings() + "");
        } else {
//            viewHolder.locaties.setText(person.getSpottings().substring(0, 120) + "...");
        }
        return convertView;
    }

    public Maaltijd getItem(int position) {
        return data.get(position);
    }

    private static class ViewHolder {
        TextView id;
        TextView naam;
        TextView locaties;
        TextView aantal;
    }
}
