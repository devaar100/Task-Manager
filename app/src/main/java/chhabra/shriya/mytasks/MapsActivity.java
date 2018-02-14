package chhabra.shriya.mytasks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.db.Tables.TaskTable;
import chhabra.shriya.mytasks.db.TaskDatabaseHelper;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Task task;
    private TextView place_name, dist;
    private Switch notify;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String name = getIntent().getStringExtra("name");
        final TaskDatabaseHelper helper = new TaskDatabaseHelper(this);

        task = TaskTable.getTask(helper.getReadableDatabase(), name);
        getSupportActionBar().setTitle(task.getTaskName());
        place_name = findViewById(R.id.place_name);
        dist = findViewById(R.id.dist);
        notify = findViewById(R.id.notify);
        btn = findViewById(R.id.btn);

        place_name.setText(task.getPlaceAddress());
        notify.setChecked(task.getNotification());
        if(task.isDone())
            btn.setText("Mark as undone");
        else
            btn.setText("Mark as done");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",task.getTaskName()+" "+task.isDone());
                TaskTable.flipDone(helper.getWritableDatabase(),task.getTaskName(),!task.isDone());
                task.setDone(!task.isDone());
                if(task.isDone())
                    btn.setText("Mark as undone");
                else
                    btn.setText("Mark as done");
            }
        });
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Location l = new Location("");
                l.setLatitude(task.getLatitude());
                l.setLongitude(task.getLongitude());
                float d = location.distanceTo(new Location(l));
                dist.setText(d+"m away");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                10,
                locLis
        );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(task.getLatitude(),task.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc).title(task.getPlaceAddress()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
}
