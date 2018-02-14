package chhabra.shriya.mytasks.Models;

/**
 * Created by LENOVO on 1/10/2018.
 */

public class Task {
    private String taskName;
    private String placeAddress;
    private int radius;
    private double latitude;
    private double longitude;
    private boolean notification;
    private boolean done;

    public Task(){}
    public Task(String taskName, String placeAddress, int radius,  double latitude,double longitude, boolean notify,boolean done) {
        this.taskName=taskName;
        this.placeAddress=placeAddress;
        this.radius=radius;
        this.latitude=latitude;
        this.longitude=longitude;
        this.notification=notify;
        this.done = done;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public int getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean getNotification() {
        return notification;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
