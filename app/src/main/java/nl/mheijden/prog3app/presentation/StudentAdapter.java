package nl.mheijden.prog3app.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import nl.mheijden.prog3app.model.domain.Student;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class StudentAdapter extends ArrayAdapter<Student> {
    /**
     * ArrayList with all the Students
     */
    private ArrayList<Student> data;
    /**
     * Context of the activity
     */
    private Context context;
    /**
     * Current user of the activity
     */
    private Student user;

    /**
     * @param context of the activity
     * @param resource that the listview uses for a element
     * @param data list of students
     * @param user current user
     */
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
            viewHolder.image = convertView.findViewById(R.id.student_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.number.setText(context.getResources().getString(R.string.app_dashboard_button_students)+" "+student.getstudentNumber()+"");
        if(student.getInsertion()==null || student.getInsertion().equals("null") || student.getInsertion().equals("")){
            viewHolder.name.setText(student.getFirstname() + " " + student.getLastname() + "");
        } else {
            viewHolder.name.setText(student.getFirstname() + " " + student.getInsertion() + " " + student.getLastname() + "");
        }
        if(student.equals(user)){
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.colorGreen));
        } else {
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.colorSemiWhite));
        }
        viewHolder.email.setText(context.getText(R.string.app_meals_mailicon)+" "+student.getEmail()+"");
        if(!student.getPhonenumber().equals("null") && student.getPhonenumber()!=null){
            viewHolder.phonenumber.setText(context.getText(R.string.app_meals_phoneicon)+" "+student.getPhonenumber()+"");
        } else {
            viewHolder.phonenumber.setText(context.getText(R.string.app_meals_phoneicon)+" ");
        }
        File filesDir = context.getFilesDir();
        File f = new File(filesDir, "studentPictures_"+student.getstudentNumber());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            viewHolder.image.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){
            viewHolder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.logo));
        }
        return convertView;
    }

    /**
     * @param position of the element
     * @return The student that is associated with the position
     */
    public Student getItem(int position) {
        return data.get(position);
    }

    /**
     * ViewHolder to save the view elements
     */
    private static class ViewHolder {
        /**
         * Name of the student
         */
        TextView name;
        /**
         * Email of the student
         */
        TextView email;
        /**
         * Phonenumber of the student
         */
        TextView phonenumber;
        /**
         * Studentnumber
         */
        TextView number;
        /**
         * Student image
         */
        ImageView image;
    }
}
