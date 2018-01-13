package nl.mheijden.prog3app.view;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
    private Context context;
    private Student user;

    public StudentAdapter(Context context, int resource, ArrayList<Student> data, Student user) {
        super(context, resource, data);
        this.data = data;
        this.context = context;
        this.user = user;
    }

    @SuppressLint("SetTextI18n")
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
            viewHolder.number = convertView.findViewById(R.id.student_nummer);
            viewHolder.phonenumber = convertView.findViewById(R.id.student_phonenumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText(context.getResources().getString(R.string.app_dashboard_button_students)+" "+student.getstudentNumber()+"");
        if(student.getInsertion()==null || student.getInsertion().equals("null")){
            viewHolder.name.setText(student.getFirstname() + " " + student.getLastname() + "");
        } else {
            viewHolder.name.setText(student.getFirstname() + " " + student.getInsertion() + " " + student.getLastname() + "");
        }
        if(student.equals(user)){
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }
        viewHolder.email.setText(context.getText(R.string.app_meals_mailicon)+" "+student.getEmail()+"");
        viewHolder.phonenumber.setText(context.getText(R.string.app_meals_phoneicon)+" "+student.getPhonenumber()+"");
        return convertView;
    }

    public Student getItem(int position) {
        return data.get(position);
    }

    private static class ViewHolder {
        TextView name;
        TextView email;
        TextView phonenumber;
        TextView number;
    }
}
