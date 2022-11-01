package ro.pub.cs.systems.eim.lab04.contactsmanager.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import ro.pub.cs.systems.eim.lab04.contactsmanager.R;
import ro.pub.cs.systems.eim.lab04.contactsmanager.general.Constants;

public class ContactsManagerActivity extends AppCompatActivity {

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save_button:

                    break;
                case R.id.cancel_button:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
            }
        }

    }

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    private Button showHideAdditionalFieldsButton;
    private Button saveButton;
    private Button cancelButton;

    private LinearLayout additionalFieldsContainer;

    private class ShowHideAdditionalFieldsButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (additionalFieldsContainer.getVisibility()) {
                case View.VISIBLE:
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.show_additional_fields));
                    additionalFieldsContainer.setVisibility(View.INVISIBLE);
                    break;
                case View.INVISIBLE:
                    showHideAdditionalFieldsButton.setText(getResources().getString(R.string.hide_additional_fields));
                    additionalFieldsContainer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private class SaveButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String jobTitle = jobTitleEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String website = websiteEditText.getText().toString();
            String im = imEditText.getText().toString();

            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            if (!name.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }
            if (!phone.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            }
            if (!email.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            }
            if (!address.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            }
            if (!jobTitle.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
            }
            if (!company.isEmpty()) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            }
            ArrayList<ContentValues> contactData = new ArrayList<>();
            if (!website.isEmpty()) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
            }
            if (!im.isEmpty()) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
            }
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
        }
    }

    private class CancelButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }

    private ShowHideAdditionalFieldsButtonOnClickListener showHideAdditionalFieldsButtonOnClickListener = new ShowHideAdditionalFieldsButtonOnClickListener();
    private SaveButtonOnClickListener saveButtonOnClickListener = new SaveButtonOnClickListener();
    private CancelButtonOnClickListener cancelButtonOnClickListener = new CancelButtonOnClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        nameEditText = (EditText)findViewById(R.id.name_edit_text);
        phoneEditText = (EditText)findViewById(R.id.phone_number_edit_text);
        emailEditText = (EditText)findViewById(R.id.email_edit_text);
        addressEditText = (EditText)findViewById(R.id.address_edit_text);
        jobTitleEditText = (EditText)findViewById(R.id.job_title_edit_text);
        companyEditText = (EditText)findViewById(R.id.company_edit_text);
        websiteEditText = (EditText)findViewById(R.id.website_edit_text);
        imEditText = (EditText)findViewById(R.id.im_edit_text);

        showHideAdditionalFieldsButton = (Button)findViewById(R.id.show_hide_additional_fields);
        showHideAdditionalFieldsButton.setOnClickListener(showHideAdditionalFieldsButtonOnClickListener);
        saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(saveButtonOnClickListener);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(cancelButtonOnClickListener);

        additionalFieldsContainer = (LinearLayout)findViewById(R.id.additional_fields_container);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}