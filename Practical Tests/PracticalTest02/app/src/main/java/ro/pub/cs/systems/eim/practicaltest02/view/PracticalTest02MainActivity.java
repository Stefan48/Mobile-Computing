package ro.pub.cs.systems.eim.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientAsyncTask;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {
    private EditText serverPortEditText;
    private Button startServerButton;
    private Button stopServerButton;

    private EditText clientAddressEditText;
    private EditText clientPortEditText;
    private EditText cityEditText;
    private EditText informationEditText;
    private Button makeRequestButton;
    private TextView informationTextView;

    private ServerThread serverThread;

    private class StartServerButtonOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            serverThread = new ServerThread(Integer.parseInt(serverPortEditText.getText().toString()));
            serverThread.startServer();
            Log.d(Constants.TAG, "Starting server...");
        }
    }

    private class StopServerButtonOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            serverThread.stopServer();
            Log.d(Constants.TAG, "Stopping server...");
        }
    }

    private class MakeRequestButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ClientAsyncTask clientAsyncTask = new ClientAsyncTask(informationTextView);
            clientAsyncTask.execute(
                    clientAddressEditText.getText().toString(),
                    clientPortEditText.getText().toString(),
                    cityEditText.getText().toString(),
                    informationEditText.getText().toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        startServerButton = (Button)findViewById(R.id.start_server_button);
        startServerButton.setOnClickListener(new StartServerButtonOnClickListener());
        stopServerButton = (Button)findViewById(R.id.stop_server_button);
        stopServerButton.setOnClickListener(new StopServerButtonOnClickListener());

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        cityEditText = (EditText)findViewById(R.id.city_edit_text);
        informationEditText = (EditText)findViewById(R.id.information_edit_text);
        makeRequestButton = (Button)findViewById(R.id.make_request_button);
        makeRequestButton.setOnClickListener(new MakeRequestButtonOnClickListener());
        informationTextView = (TextView)findViewById(R.id.information_text_view);
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}