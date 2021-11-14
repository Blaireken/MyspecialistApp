package com.myspecialist.UserInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myspecialist.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorDetail extends AppCompatActivity {

    TextView doctorName, doctorSpecialization, doctorRank, doctorWorkExperience, doctorResearch, doctorLocation;
    ImageView doctor_image;
    MaterialToolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    DatabaseReference databaseReference;
    String DoctorDetail ="";
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton call, video, whatsapp, appointment, sms;
    FirebaseAuth mAuth;
    private TimePickerDialog timePicker;
    private DatePickerDialog datePickerDialog;
    private int year, month, dayOfMonth, hour, minute;
    private Calendar calendar, currentTime;
    public String Name, Message;
    public Dialog dialog;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        doctor_image = findViewById(R.id.doctor_image);
        doctorName = findViewById(R.id.doctor_name);
        doctorSpecialization = findViewById(R.id.doctor_department);
        doctorRank = findViewById(R.id.doctor_duty);
        doctorLocation = findViewById(R.id.doctor_location);
        doctorWorkExperience = findViewById(R.id.doctor_work_experience);
        doctorResearch = findViewById(R.id.doctor_research);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        coordinatorLayout = findViewById(R.id.doctor_detail_layout);
        call = findViewById(R.id.call);
        video = findViewById(R.id.video);
        whatsapp = findViewById(R.id.whatsapp);
        appointment = findViewById(R.id.book_appointment);
        sms = findViewById(R.id.sms);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "1234567890"));
                startActivity(intent);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getApplicationContext().getPackageManager();
                Intent appStartIntent = pm.getLaunchIntentForPackage("zoom.com.cn");
                if (null != appStartIntent)
                {
                    getApplicationContext().startActivity(appStartIntent);
                }
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);
            }
        });
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppointment();
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSMSDialog();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors");

        if (getIntent() != null){
            DoctorDetail = getIntent().getStringExtra("DoctorDetail");
        }
        if (!DoctorDetail.isEmpty() && DoctorDetail != null){
            getDoctorDetail(DoctorDetail);
        }
    }

    private void getDoctorDetail(String doctorDetail) {
        databaseReference.child(DoctorDetail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name = dataSnapshot.child("Name").getValue().toString();
                String Department = dataSnapshot.child("Department").getValue().toString();
                String Duty = dataSnapshot.child("Duty").getValue().toString();
                String Experience = dataSnapshot.child("Experience").getValue().toString();
                String Research = dataSnapshot.child("Research").getValue().toString();
                String Image = dataSnapshot.child("Image").getValue().toString();
                String Location = dataSnapshot.child("Location").getValue().toString();

               // String Name = mAuth.getCurrentUser().getDisplayName();

                doctorName.setText(Name);
                doctorSpecialization.setText(Department);
                doctorRank.setText(Duty);
                doctorWorkExperience.setText(Experience);
                doctorResearch.setText(Research);
                doctorLocation.setText(Location);
                Picasso.get().load(Image).into(doctor_image);
                doctor_image.setContentDescription(Name);
                collapsingToolbarLayout.setTitle(Name);
                collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                toolbar.setTitle(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(coordinatorLayout, ""+error, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark))
                        .setAction("RETRY", null)
                        .show();
            }
        });

    }

    private void showSMSDialog() {
        dialog = new Dialog(DoctorDetail.this);
        dialog.setContentView(R.layout.sms_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView doctorName = dialog.findViewById(R.id.doctor_name);
        doctorName.setText(Name);
        ImageView closeDialog = dialog.findViewById(R.id.dismiss_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        FloatingActionButton fab = dialog.findViewById(R.id.send_sms_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText message = dialog.findViewById(R.id.sms_text);
                TextInputLayout layout = dialog.findViewById(R.id.textInputLayout);
                Message = message.getText().toString().trim();
                if (TextUtils.isEmpty(Message)){
                    layout.setError("Body cannot be empty");
                }
                else {
                    sendSMSMessage();

               //     SmsManager smsManager = SmsManager.getDefault();
               //     smsManager.sendTextMessage("+2547xxxxxxxx", null, Message, null, null);
               //     Snackbar.make(coordinatorLayout, "Send Successfully", Snackbar.LENGTH_LONG)
               //             .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark))
               //             .setAction("Okay", null)
               //             .show();
               //     dialog.dismiss();

                }
            }
        });

        dialog.show();


    }

    private void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+2547xxxxxxxx", null, Message, null, null);
                    Snackbar.make(coordinatorLayout, "Send Successfully", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark))
                            .setAction("Okay", null)
                            .show();
                    dialog.dismiss();


                } else {

                    Snackbar.make(coordinatorLayout, "SMS failed, please try again", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(android.R.color.holo_red_dark))
                            .setAction("Retry", null)
                            .show();
                    dialog.dismiss();

                }
            }
        }

    }


    private void showAppointment() {

        final Dialog dialog = new Dialog(DoctorDetail.this);
        dialog.setContentView(R.layout.appointment_dialog);
        final TextView name = dialog.findViewById(R.id.name);
        final TextView email = dialog.findViewById(R.id.email);
        final TextView appointment_date = dialog.findViewById(R.id.date);
        final TextView appointment_time = findViewById(R.id.time);
        TextInputEditText phone = dialog.findViewById(R.id.phone_number);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String userID = mAuth.getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Name = snapshot.child("Name").getValue().toString();
                String Email = snapshot.child("Email").getValue().toString();

                name.setText(Name);
                email.setText(Email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Date Picker
        TextView date = dialog.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.MONTH);
                datePickerDialog = new DatePickerDialog(DoctorDetail.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        appointment_date.setText(dayOfMonth + "/"+(month + 1) + "/" + year);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });

        final TextView time = dialog.findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentTime = Calendar.getInstance();
                hour = currentTime.get(Calendar.HOUR_OF_DAY);
                minute = currentTime.get(Calendar.MINUTE);
                timePicker = new TimePickerDialog(DoctorDetail.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time

                timePicker.show();
            }
        });

        ImageView dismiss_dialog = dialog.findViewById(R.id.dismiss_dialog);
        dismiss_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button appointment = dialog.findViewById(R.id.book_appointment);
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText phone = dialog.findViewById(R.id.phone_number);
                TextInputLayout layout = dialog.findViewById(R.id.textInputLayout);
                TextView date = dialog.findViewById(R.id.date);
                TextView time = dialog.findViewById(R.id.time);
                String Date = date.getText().toString().trim();
                String Time = time.getText().toString().trim();
                String Phone = phone.getText().toString().trim();

                if (TextUtils.isEmpty(Phone)){
                    layout.setError("Phone is required");
                }
                else if (Phone.length()<10){
                    layout.setError("Phone number is not valid");
                }
                else if (TextUtils.isEmpty(Date)){
                    date.setError("Field is required");
                }else if (TextUtils.isEmpty(Time)){
                    time.setError("Field is required");
                }else {
                    String userID = mAuth.getCurrentUser().getUid();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Appointments").child(userID).push();
                    Map newAppointment = new HashMap();
                    newAppointment.put("Name", mAuth.getCurrentUser().getDisplayName());
                    newAppointment.put("Phone", Phone);
                    newAppointment.put("Date", Date);
                    newAppointment.put("Time", Time);
                    //newAppointment.put("Email", Ema); Email
                    databaseReference1.setValue(newAppointment);

                    Snackbar.make(coordinatorLayout, "Submitted Successfully", Snackbar.LENGTH_LONG)
                            .setBackgroundTint(getResources().getColor(android.R.color.holo_green_dark))
                            .setAction("Okay", null)
                            .show();
                    dialog.dismiss();

                }
            }
        });

        dialog.show();

    }
}