package chhabra.shriya.mytasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import chhabra.shriya.mytasks.Adapters.EmptyRecyclerView;
import chhabra.shriya.mytasks.Adapters.TaskRecyclerAdapter;
import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.db.Tables.TaskTable;
import chhabra.shriya.mytasks.db.TaskDatabaseHelper;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Task> tasks=new ArrayList<>();
    SQLiteDatabase readDb;
    TaskRecyclerAdapter tra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this,AddItemActivity.class);
                startActivity(i);
            }
        });

        TaskDatabaseHelper myDbHelper= new TaskDatabaseHelper(this);
        readDb = myDbHelper.getReadableDatabase();

        EmptyRecyclerView rv=findViewById(R.id.rv);
        tra= new TaskRecyclerAdapter(this,tasks,rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(tra);
        rv.setEmptyView(findViewById(R.id.empty));
    }

    @Override
    protected void onStart() {
        super.onStart();
        tasks = TaskTable.getAllTasks(readDb);
        tra.setArray(tasks);
    }
}
