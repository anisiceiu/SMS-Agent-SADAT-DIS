package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
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

    private void createSMS(){


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

            Toast.makeText(this, "দীর্ঘ SMS পাঠানো হয়েছে!", Toast.LENGTH_SHORT).show();

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