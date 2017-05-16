package com.systemofmonitoring.pojo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class GasMeterDatasList {
    private final StringProperty date, time;
    private final DoubleProperty value;

    public GasMeterDatasList(String date, String time, double value) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.value = new SimpleDoubleProperty(value);
    }

    public StringProperty dateProp() { return date; }

    public String getDate() { return date.get(); }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProp() {
        return time;
    }

    public double getValueActive() {
        return value.get();
    }

    public DoubleProperty activeProp() {
        return value;
    }
}
