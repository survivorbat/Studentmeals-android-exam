package nl.mheijden.prog3app.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nl.mheijden.prog3app.R;
import nl.mheijden.prog3app.controllers.callbacks.InvalidTokenCallback;
import nl.mheijden.prog3app.controllers.callbacks.ReloadCallback;
import nl.mheijden.prog3app.model.domain.MaaltijdenApp;
import nl.mheijden.prog3app.presentation.StudentAdapter;

public class StudentsActivity extends AppCompatActivity implements ReloadCallback, InvalidTokenCallback {
    private MaaltijdenApp app;
    private SwipeRefreshLayout layout;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        list = findViewById(R.id.studentslist);
        app = new MaaltijdenApp(this,this);

        layout = findViewById(R.id.students_refreshlayout);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInitiated();
            }
        });
        ArrayAdapter adapter = new StudentAdapter(this, app.getStudents(), app.getUser());
        list.setAdapter(adapter);
    }

    private void refreshInitiated() {
        app.reloadStudents(this);
        layout.setRefreshing(true);
    }

    public void reloaded(boolean result) {
        if (result) {
            if (layout.isRefreshing()) {
                layout.setRefreshing(false);
                Toast.makeText(this, R.string.app_reload_success, Toast.LENGTH_SHORT).show();
            }
            ListAdapter adapter = new StudentAdapter(this, app.getStudents(), app.getUser());
            list.setAdapter(adapter);

        } else {
            Toast.makeText(this, R.string.app_reload_failure, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void invalidToken() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
