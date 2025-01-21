package com.example.sensorsimulation.entity;

public class SensorData {

    private long timestamp;
    private Double measurement_value;
    private String deviceId;

    public SensorData() {

    }

    public SensorData(long timestamp, String device_id, Double measurement_value) {
        this.timestamp = timestamp;
        this.deviceId = device_id;
        this.measurement_value = measurement_value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getMeasurement_value() {
        return measurement_value;
    }

    public void setMeasurement_value(Double measurement_value) {
        this.measurement_value = measurement_value;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", measurementValue=" + measurement_value +
                '}';
    }
}
