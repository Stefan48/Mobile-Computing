package ro.pub.cs.systems.eim.practicaltest02.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    public final String TAG = "[PracticalTest]";

    public final String WEB_SERVICE_INTERNET_ADDRESS = "https://api.openweathermap.org/data/2.5/weather";
    public final String APP_ID = "e03c3b32cfb5a6f7069f2ef29237d87e";

    public final String TEMPERATURE = "temperature";
    public final String WIND_SPEED = "wind_speed";
    public final String WEATHER = "weather";
    public final String PRESSURE = "pressure";
    public final String HUMIDITY = "humidity";
    public final String ALL = "all";

    public final List<String> INFORMATION_OPTIONS = Arrays.asList(TEMPERATURE, WIND_SPEED, WEATHER, PRESSURE, HUMIDITY, ALL);

    public final long MAX_VALID_CACHE_TIME = 60000;

    // 192.168.100.114, 192.168.100.115
}
