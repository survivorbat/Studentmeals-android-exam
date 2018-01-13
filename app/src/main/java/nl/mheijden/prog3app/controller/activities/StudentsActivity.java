package nl.mheijden.prog3app.controller.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controller.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.view.StudentAdapter;

public class StudentsActivity extends AppCompatActivity implements ReloadCallback {
    private ListView list;
    private MaaltijdenApp app;
    private SwipeRefreshLayout layout;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        this.list = findViewById(R.id.studentslist);
        app=new MaaltijdenApp(this);

        layout = findViewById(R.id.students_refreshlayout);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInitiated();
            }
        });
        adapter = new StudentAdapter(this, R.layout.listview_student, app.getStudents(),app.getUser());
        list.setAdapter(adapter);
    }

    private void refreshInitiated(){
        app.reloadStudents(this);
        layout.setRefreshing(true);
    }

    @Override
    public void reloaded(boolean result) {
        layout.setRefreshing(false);
        if(result){
            Toast.makeText(this,R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            adapter = new StudentAdapter(this, R.layout.listview_student, app.getStudents(),app.getUser());
            list.setAdapter(adapter);
        } else {
            Toast.makeText(this,R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
