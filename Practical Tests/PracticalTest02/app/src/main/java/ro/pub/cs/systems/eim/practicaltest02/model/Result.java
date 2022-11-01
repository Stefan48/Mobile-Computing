package ro.pub.cs.systems.eim.practicaltest02.model;

import java.util.Calendar;
import java.util.Date;

public class Result {
    private Date lastModified;
    private double temperature;
    private double windSpeed;
    private String weather;
    private double pressure;
    private double humidity;

    public Result(double temperature, double windSpeed, String weather, double pressure, double humidity) {
        this.lastModified = Calendar.getInstance().getTime();
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.weather = weather;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getWeather() {
        return weather;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }
}
