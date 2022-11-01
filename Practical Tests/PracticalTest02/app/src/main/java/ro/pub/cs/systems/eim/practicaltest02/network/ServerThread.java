package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;
import ro.pub.cs.systems.eim.practicaltest02.model.Result;

public class ServerThread extends Thread {
    private int port;
    private boolean isRunning;
    private ServerSocket serverSocket;
    private HashMap<String, Result> serverCache;

    public ServerThread(int port) {
        this.port = port;
        this.isRunning = false;
        this.serverCache = new HashMap<>();
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.d(Constants.TAG, "SERVER - startServer() was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "SERVER - exception: " + ioException.getMessage());
        }
        Log.d(Constants.TAG, "stopServer() was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Log.d(Constants.TAG, "SERVER - Running...");
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    Log.d(Constants.TAG, "SERVER - new client connection");
                    communicateWithClient(socket);
                    socket.close();
                    Log.d(Constants.TAG, "SERVER - Connection with client closed");
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "SERVER - IOException: " + ioException.getMessage());
        }
    }

    private void communicateWithClient(Socket socket) throws IOException {

        BufferedReader reader = Utilities.getReader(socket);
        PrintWriter writer = Utilities.getWriter(socket);
        String city = reader.readLine();
        if (city == null) {
            Log.d(Constants.TAG, "SERVER - City is null");
            return;
        }
        String requiredInformation = reader.readLine();
        if (requiredInformation == null) {
            Log.d(Constants.TAG, "SERVER - Required information is null");
            return;
        }
        if (!Constants.INFORMATION_OPTIONS.contains(requiredInformation)) {
            Log.d(Constants.TAG, "SERVER - Requested information is invalid");
            writer.println("Invalid request");
        }
        if (serverCache.containsKey(city)) {
            Log.d(Constants.TAG, "SERVER - Key " + city + " found in cache");
            Result result = serverCache.get(city);
            long differenceInMilliseconds = Calendar.getInstance().getTime().getTime() - result.getLastModified().getTime();
            if (differenceInMilliseconds > Constants.MAX_VALID_CACHE_TIME) {
                Log.d(Constants.TAG, "SERVER - Cache entry is too old");
                result = makeApiRequest(city);
                sendResponseToClient(writer, requiredInformation, result);
            }
            sendResponseToClient(writer, requiredInformation, result);
        }
        else {
            Log.d(Constants.TAG, "SERVER - Key " + city + " not found in cache");
            Result result = makeApiRequest(city);
            sendResponseToClient(writer, requiredInformation, result);
        }
    }

    private Result makeApiRequest(String city) {
        HttpClient httpClient = new DefaultHttpClient();
        String url = Constants.WEB_SERVICE_INTERNET_ADDRESS + "?q=" + city + "&appid=" + Constants.APP_ID;
        Log.d(Constants.TAG, "SERVER - URL: " + url);
        HttpGet httpGet = new HttpGet(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            String content = httpClient.execute(httpGet, responseHandler);
            JSONObject jsonObject = new JSONObject(content);
            Result result = new Result(
                    jsonObject.getJSONObject("main").getDouble("temp"),
                    jsonObject.getJSONObject("wind").getDouble("speed"),
                    jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"),
                    jsonObject.getJSONObject("main").getDouble("pressure"),
                    jsonObject.getJSONObject("main").getDouble("humidity")
            );
            serverCache.put(city, result);
            return result;
        } catch (Exception exception) {
            Log.e(Constants.TAG, "SERVER - exception:" + exception.getMessage());
        }
        return null;
    }

    private void sendResponseToClient(PrintWriter writer, String requiredInformation, Result result) {
        Log.d(Constants.TAG, "SERVER - Sending response to client");
        if (requiredInformation.equals(Constants.TEMPERATURE) || requiredInformation.equals(Constants.ALL)) {
            writer.println("temperature = " + result.getTemperature());
        }
        if (requiredInformation.equals(Constants.WIND_SPEED) || requiredInformation.equals(Constants.ALL)) {
            writer.println("wind speed = " + result.getWindSpeed());
        }
        if (requiredInformation.equals(Constants.WEATHER) || requiredInformation.equals(Constants.ALL)) {
            writer.println("weather = " + result.getWeather());
        }
        if (requiredInformation.equals(Constants.PRESSURE) || requiredInformation.equals(Constants.ALL)) {
            writer.println("pressure = " + result.getPressure());
        }
        if (requiredInformation.equals(Constants.HUMIDITY) || requiredInformation.equals(Constants.ALL)) {
            writer.println("humidity = " + result.getHumidity());
        }
    }
}
