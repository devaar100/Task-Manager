package chhabra.shriya.mytasks;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.Services.MyService;
import chhabra.shriya.mytasks.db.Tables.TaskTable;
import chhabra.shriya.mytasks.db.TaskDatabaseHelper;

public class AddItemActivity extends AppCompatActivity {

    TextView place_name;
    Switch swtch;
    ImageView imageMap;
    Button addTask;
    EditText range_m;
    TextInputEditText task_name;
    Boolean ischecked;
    String TaskName;
    String placeName, remRange;
    double longitude = 0, latitude = 0;
    android.support.v7.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        task_name = findViewById(R.id.task_name);
        range_m = findViewById(R.id.range_m);
        addTask = findViewById(R.id.addTask);
        place_name = findViewById(R.id.place);
        swtch = findViewById(R.id.swtch);
        imageMap = findViewById(R.id.imageMap);
        TaskDatabaseHelper myDBHelper = new TaskDatabaseHelper(this);
        final SQLiteDatabase writeDb = myDBHelper.getWritableDatabase();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ischecked = swtch.isChecked();
                TaskName = task_name.getText().toString();
                placeName = place_name.getText().toString();
                remRange = range_m.getText().toString();
                int r = Integer.valueOf(remRange);
                if (!TaskName.equals("") && !placeName.equals("") && !remRange.equals("")) {
                    Task t = new Task(TaskName, placeName, r, longitude, latitude, ischecked, false);
                    Log.d("TAG", TaskName + ":" + placeName + ":" + r + ":" + longitude + ":" + latitude + ":" + ischecked);
                    TaskTable.insertTask(t, writeDb);
                    Intent i = new Intent(AddItemActivity.this, MyService.class);
                    i.putExtra("name", TaskName);
                    if (ActivityCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    new GeofencingClient(AddItemActivity.this).addGeofences(new GeofencingRequest.Builder().
                            addGeofence(new Geofence.Builder()
                                    .setRequestId(TaskName)
                                    .setCircularRegion(latitude,longitude,r)
                                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                    .build())
                            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                            .build(), PendingIntent.getService(AddItemActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
                    );
                  finish();
              }
            }
        });
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(AddItemActivity.this);
                    startActivityForResult(intent, 100);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(),AddItemActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(AddItemActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        View loaderView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_date_time,null);
        dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(loaderView)
                .create();

        findViewById(R.id.task_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                CharSequence name = place.getName();
                LatLng ll = place.getLatLng();
                longitude = ll.longitude;
                latitude = ll.latitude;
                place_name.setText(name.toString());
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}