package ro.pub.cs.systems.eim.practicaltest01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    TextView firstButtonClicksText;
    TextView secondButtonClicksText;
    Button firstButton;
    Button secondButton;
    Button navigateButton;

    private class ButtonOnClickListener implements View.OnClickListener {
        private final TextView clicksText;

        ButtonOnClickListener(TextView clicksText) {
            this.clicksText = clicksText;
        }

        @Override
        public void onClick(View view) {
            clicksText.setText(String.valueOf(Integer.parseInt(clicksText.getText().toString()) + 1));

            int firstButtonClicks = Integer.parseInt(firstButtonClicksText.getText().toString());
            int secondButtonClicks = Integer.parseInt(secondButtonClicksText.getText().toString());
            if (firstButtonClicks + secondButtonClicks >= Constants.CLICKS_THRESHOLD) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.FIRST_BUTTON_CLICKS, firstButtonClicks);
                intent.putExtra(Constants.SECOND_BUTTON_CLICKS, secondButtonClicks);
                getApplicationContext().startService(intent);
            }
        }
    }

    private class NavigateButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent("ro.pub.cs.systems.eim.practicaltest01.intent.action.PracticalTest01SecondaryActivity");
            int totalClicks = Integer.parseInt(firstButtonClicksText.getText().toString()) + Integer.parseInt(secondButtonClicksText.getText().toString());
            intent.putExtra(Constants.TOTAL_CLICKS_KEY, totalClicks);
            startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
        }
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.MAIN_ACTIVITY_TAG, intent.getStringExtra(Constants.DATA));
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        firstButtonClicksText = findViewById(R.id.first_button_clicks_text);
        secondButtonClicksText = findViewById(R.id.second_button_clicks_text);
        firstButton = findViewById(R.id.first_button);
        secondButton = findViewById(R.id.second_button);
        navigateButton = findViewById(R.id.navigate_to_secondary_activity_button);

        firstButton.setOnClickListener(new ButtonOnClickListener(firstButtonClicksText));
        secondButton.setOnClickListener(new ButtonOnClickListener(secondButtonClicksText));
        navigateButton.setOnClickListener(new NavigateButtonOnClickListener());

        intentFilter.addAction(Constants.ACTION_1);
        intentFilter.addAction(Constants.ACTION_2);
        intentFilter.addAction(Constants.ACTION_3);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.FIRST_BUTTON_CLICKS_COUNT, firstButtonClicksText.getText().toString());
        savedInstanceState.putString(Constants.SECOND_BUTTON_CLICKS_COUNT, secondButtonClicksText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(Constants.FIRST_BUTTON_CLICKS_COUNT)) {
            firstButtonClicksText.setText(savedInstanceState.getString(Constants.FIRST_BUTTON_CLICKS_COUNT));
        }
        if (savedInstanceState.containsKey(Constants.SECOND_BUTTON_CLICKS_COUNT)) {
            secondButtonClicksText.setText(savedInstanceState.getString(Constants.SECOND_BUTTON_CLICKS_COUNT));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.SECONDARY_ACTIVITY_REQUEST_CODE:
                Toast.makeText(this, "Secondary activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

}