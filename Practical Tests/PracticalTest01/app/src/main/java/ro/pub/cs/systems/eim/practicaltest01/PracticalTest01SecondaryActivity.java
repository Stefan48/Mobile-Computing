package ro.pub.cs.systems.eim.practicaltest01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {

    TextView totalClicksText;
    Button okButton;
    Button cancelButton;

    private class OkButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        }
    }

    private class CancelButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        totalClicksText = findViewById(R.id.total_clicks_text);

        Intent intent = getIntent();
        if (intent != null) {
            int totalClicks = intent.getIntExtra(Constants.TOTAL_CLICKS_KEY, -1);
            if (totalClicks != -1) {
                totalClicksText.setText(String.valueOf(totalClicks));
            } else {
                Toast.makeText(this, "Error receiving the total number of clicks", Toast.LENGTH_LONG).show();
            }
        }

        okButton = findViewById(R.id.button_ok);
        cancelButton = findViewById(R.id.button_cancel);

        okButton.setOnClickListener(new OkButtonOnClickListener());
        cancelButton.setOnClickListener(new CancelButtonOnClickListener());
    }
}