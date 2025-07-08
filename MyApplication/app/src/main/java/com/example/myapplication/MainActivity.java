package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.Manifest;
import android.widget.Toast;
import android.telephony.SmsManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_CONTACT = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSION_REQUEST_SEND_SMS = 123;
    private TextView selectedContact;
    private TextView selectedPhoneNumber;
    EditText multilineEditText;
    Button btnSmsSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        multilineEditText = findViewById(R.id.multilineEditText);
        btnSmsSend = findViewById(R.id.btnSmsSend);
        Button btnSmsPreview = findViewById(R.id.btnSmsPreview);

        btnSmsPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 👉 Put your SMS preview logic here!
                // Example: Show a Toast for now
                //Toast.makeText(MainActivity.this, "SMS Preview Clicked!", Toast.LENGTH_SHORT).show();

                // Example: Maybe call your createSMS() method here
                createSMS();
            }
        });


        btnSmsSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check permission first
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            PERMISSION_REQUEST_SEND_SMS);
                } else {
                    sendSmsMessage();
                }
            }
        });
        // Link to your contact TextView
        selectedContact = findViewById(R.id.selectedContact);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Open contacts picker when FAB clicked

        fab.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestContactsPermission();
            } else {
                openContactsPicker();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSmsMessage();
            } else {
                Toast.makeText(this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestContactsPermission() {
        requestPermissions(
                new String[]{Manifest.permission.READ_CONTACTS},
                PERMISSIONS_REQUEST_READ_CONTACTS
        );
    }

    private void openContactsPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private void createSMS() {
        float purberBaki = 0;
        float ajkerBill = 0;
        float ajkerBaki = 0;
        float sokalerJoma = 0;
        float bikroyJoma=0;
        float total=0;
        float bortomanMotBaki=0;

        EditText amountPurberBaki = findViewById(R.id.amountPurberBaki); // or R.id.amountPurberBaki if you renamed it
        String inputText1 = amountPurberBaki.getText().toString().trim();
        if (!inputText1.isEmpty()) {
            try {
                purberBaki = Float.parseFloat(inputText1);
            } catch (NumberFormatException e) {
                purberBaki = 0; // fallback if not a valid float
            }
        }

        EditText amountAjkerBill = findViewById(R.id.amountAjkerBill);
        String inputText2 = amountAjkerBill.getText().toString().trim();
        if (!inputText2.isEmpty()) {
            try {
                ajkerBill = Float.parseFloat(inputText2);
            } catch (NumberFormatException e) {
                ajkerBill = 0;
            }
        }

        EditText amountAjkerBaki = findViewById(R.id.amountAjkerBaki); // or R.id.amountAjkerBaki if you renamed it
        String inputText3 = amountAjkerBaki.getText().toString().trim();
        if (!inputText3.isEmpty()) {
            try {
                ajkerBaki = Float.parseFloat(inputText3);
            } catch (NumberFormatException e) {
                ajkerBaki = 0;
            }
        }

        //

        EditText amountSokalerJoma = findViewById(R.id.amountSokalerJoma); // or R.id.amountAjkerBaki if you renamed it
        String inputText4 = amountSokalerJoma.getText().toString().trim();
        if (!inputText4.isEmpty()) {
            try {
                sokalerJoma = Float.parseFloat(inputText4);
            } catch (NumberFormatException e) {
                sokalerJoma = 0;
            }
        }

        EditText amountBikroyJoma = findViewById(R.id.amountBikroyJoma); // or R.id.amountAjkerBaki if you renamed it
        String inputText5 = amountBikroyJoma.getText().toString().trim();
        if (!inputText5.isEmpty()) {
            try {
                bikroyJoma = Float.parseFloat(inputText5);
            } catch (NumberFormatException e) {
                bikroyJoma = 0;
            }
        }

        //
        EditText amountBortomanMotBaki = findViewById(R.id.amountBortomanMotBaki); // or R.id.amountAjkerBaki if you renamed it
        String inputText6 = amountBortomanMotBaki.getText().toString().trim();
        if (!inputText6.isEmpty()) {
            try {
                bortomanMotBaki = Float.parseFloat(inputText6);
            } catch (NumberFormatException e) {
                bortomanMotBaki = 0;
            }
        }

        ajkerBaki = ajkerBill - bikroyJoma;
        total = purberBaki + ajkerBaki;
        bortomanMotBaki = total - sokalerJoma;

        amountAjkerBaki.setText(String.valueOf(ajkerBaki));
        amountBortomanMotBaki.setText(String.valueOf(bortomanMotBaki));

        DatePicker datePicker = findViewById(R.id.datePicker);
        String tarikh = "";
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();  // Note: Month is 0-based (January = 0)
        int year = datePicker.getYear();
        tarikh =day + "/" + (month + 1) + "/" + year;

        // Now you can use purberBaki, ajkerBill, ajkerBaki to build your SMS string
        String smsText = "সাদাত ডিস্ট্রিবিউশন\nতারিখঃ"+tarikh
                +"\nপূর্বের বাঁকি: " + purberBaki +
                "\nআজকের বিল: " + ajkerBill +
                "\nআজকের বাঁকি: " + ajkerBaki+
                "\nসকালের জমা: " + sokalerJoma+
                "\nবিক্রয় জমা: " + bikroyJoma+
                "\nমোট: " + total+
                "\nবর্তমান মোট বাঁকি: " + bortomanMotBaki;

        // Example: Show in log or TextView
        multilineEditText = findViewById(R.id.multilineEditText);
        multilineEditText.setText(smsText);
    }

    private void sendSmsMessage() {
        String message = multilineEditText.getText().toString().trim();
        String phoneNumber = selectedPhoneNumber.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "SMS বার্তা ফাঁকা!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.isEmpty() || phoneNumber.contains("কোনো কন্টাক্ট")) {
            Toast.makeText(this, "ফোন নম্বর নির্বাচন করুন!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();

            if (message.length() > 160) {
                // Break the message into parts
                ArrayList<String> parts = smsManager.divideMessage(message);
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
            } else {
                // Send normally
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }

            Toast.makeText(this, "SMS পাঠানো হয়েছে!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "SMS পাঠাতে সমস্যা: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           selectedPhoneNumber = findViewById(R.id.selectedPhoneNumber);
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK && data != null) {
            Uri contactUri = data.getData();
            if (contactUri != null) {
                Cursor cursor = getContentResolver().query(
                        contactUri,
                        new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                        },
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") String contactId = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range") int hasPhoneNumber = cursor.getInt(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    StringBuilder contactInfo = new StringBuilder();
                    contactInfo.append("নাম: ").append(name).append("\n");

                    if (hasPhoneNumber > 0) {
                        Cursor phonesCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contactId},
                                null
                        );

                        if (phonesCursor != null) {
                            contactInfo.append("মোবাইল: ");
                            if (phonesCursor.moveToFirst()) {
                                @SuppressLint("Range") String phoneNumber = phonesCursor.getString(
                                        phonesCursor.getColumnIndex(
                                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                                contactInfo.append(phoneNumber);

                                selectedPhoneNumber.setText(phoneNumber);
                            }
                            phonesCursor.close();
                        }

                    } else {
                        contactInfo.append("মোবাইল নম্বর নেই");
                    }

                    selectedContact.setText(contactInfo.toString());
                    cursor.close();
                }
            }
        }
    }
}