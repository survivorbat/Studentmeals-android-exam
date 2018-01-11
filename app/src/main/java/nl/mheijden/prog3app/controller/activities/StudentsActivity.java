package nl.mheijden.prog3app.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.model.domain.Student;
import nl.mheijden.prog3app.view.StudentAdapter;

public class StudentsActivity extends AppCompatActivity {
    private ListView list;
    private MaaltijdenApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        app=new MaaltijdenApp(this);
        app.reloadData();

        this.list= findViewById(R.id.studentslist);
        ListAdapter adapter = new StudentAdapter(this,R.layout.listview_student,app.getStudents());
        list.setAdapter(adapter);
    }
}
