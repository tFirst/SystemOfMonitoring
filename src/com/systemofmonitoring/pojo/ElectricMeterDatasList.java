package com.systemofmonitoring.pojo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ElectricMeterDatasList {
    private final StringProperty date, time;
    private final DoubleProperty valueActive, valuePassive;

    public ElectricMeterDatasList(String date, String time, double valueActive, double valuePassive) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.valueActive = new SimpleDoubleProperty(valueActive);
        this.valuePassive = new SimpleDoubleProperty(valuePassive);
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
        return valueActive.get();
    }

    public DoubleProperty activeProp() {
        return valueActive;
    }

    public double getValuePassive() {
        return valuePassive.get();
    }

    public DoubleProperty passiveProp() {
        return valuePassive;
    }
}
