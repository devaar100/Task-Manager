package chhabra.shriya.mytasks.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import chhabra.shriya.mytasks.Models.Task;
import chhabra.shriya.mytasks.R;
import chhabra.shriya.mytasks.db.Tables.TaskTable;
import chhabra.shriya.mytasks.db.TaskDatabaseHelper;

public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TaskDatabaseHelper helper=new TaskDatabaseHelper(this);
        Task evnt= TaskTable.getTask(helper.getReadableDatabase(),intent.getStringExtra("name"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle("Task Notifier : Task proximity reminder")
                .setContentText(evnt.getTaskName()+'\n'+evnt.getPlaceAddress());
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }
}
