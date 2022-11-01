package ro.pub.cs.systems.eim.practicaltest02.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.general.Utilities;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView informationTextView;

    public ClientAsyncTask(TextView informationTextView) {
        this.informationTextView = informationTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);
            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter writer = Utilities.getWriter(socket);
            String city = params[2];
            String requiredInformation = params[3];
            writer.println(city);
            writer.println(requiredInformation);
            BufferedReader reader = Utilities.getReader(socket);
            String line = reader.readLine();
            while (line != null) {
                publishProgress(line);
                line = reader.readLine();
            }
            socket.close();
        } catch (Exception exception) {
            Log.e(Constants.TAG, "CLIENT - exception: " + exception.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        informationTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        informationTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}

}
