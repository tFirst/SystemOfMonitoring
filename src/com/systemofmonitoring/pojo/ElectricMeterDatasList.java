package com.systemofmonitoring.pojo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Stanislav Trushin on 01.03.17.
 */
public class ElectricMeterDatasList {
    private final StringProperty time;
    private final DoubleProperty valueActive;
    private final DoubleProperty valuePassive;

    public ElectricMeterDatasList(String time, double valueActive, double valuePassive) {
        this.time = new SimpleStringProperty(time);
        this.valueActive = new SimpleDoubleProperty(valueActive);
        this.valuePassive = new SimpleDoubleProperty(valuePassive);
    }

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
