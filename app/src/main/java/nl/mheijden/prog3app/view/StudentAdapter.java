package nl.mheijden.prog3app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.Meal;
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class StudentAdapter extends ArrayAdapter<Student> {
    private ArrayList<Student> data;

    public StudentAdapter(Context context, int resource, ArrayList<Student> data) {
        super(context, resource, data);
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_student, parent, false);
            viewHolder.name = convertView.findViewById(R.id.student_name);
            viewHolder.email = convertView.findViewById(R.id.student_email);
            viewHolder.phonenumber = convertView.findViewById(R.id.student_phonenumber);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(student.getFirstname()+" "+student.getInsertion()+" "+student.getLastname()+"");
        viewHolder.email.setText(student.getEmail()+"");
        viewHolder.phonenumber.setText(student.getPhonenumber()+"");
        return convertView;
    }

    public Student getItem(int position) {
        return data.get(position);
    }

    private static class ViewHolder {
        TextView name;
        TextView email;
        TextView phonenumber;
    }
}
