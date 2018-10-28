package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Main5Activity extends AppCompatActivity {
EditText editTextSubject,editTextMessage, timepicker,datepick;
LinearLayout book;
    private int mYear, mMonth, mDay;
String pick,drop,ctype,cost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        book=(LinearLayout)findViewById(R.id.callnow);
        editTextSubject=(EditText)findViewById(R.id.name);
        editTextMessage=(EditText)findViewById(R.id.mno);
        editTextMessage.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextMessage.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        timepicker=(EditText)findViewById(R.id.time);
        Intent inte =getIntent();
        Bundle extras = inte.getExtras();
        if (extras != null)
        {
          pick = extras.getString("plocation");
          drop=extras.getString("dlocation");
          ctype=extras.getString("ctype");
          cost=extras.getString("cost");
        }
  timepicker.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          Calendar mcurrentTime = Calendar.getInstance();
          int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
          int minute = mcurrentTime.get(Calendar.MINUTE);

          TimePickerDialog mTimePicker;
          mTimePicker = new TimePickerDialog(Main5Activity.this, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                 timepicker.setText( selectedHour + ":" + selectedMinute);
              }
          }, hour, minute, true);
          mTimePicker.setTitle("Select Pickup Time");
          mTimePicker.show();
      }
  });
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Main5Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datepick.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
book.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String sub=editTextMessage.getText().toString().trim();
        String mess =editTextSubject.getText().toString().trim();
        String timing =timepicker.getText().toString().trim();
        if(!sub.isEmpty()&&!mess.isEmpty()&&!timing.isEmpty())
        sendEmail();
        else
            Toast.makeText(getApplicationContext(),"Please fill the required field",Toast.LENGTH_SHORT).show();
        }
});
    }

    private void sendEmail() {
        //Getting content for email
        String email = "duraivel1198@gmail.com";
        String subject = editTextSubject.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
        String time =timepicker.getText().toString().trim();
String info="NEW BOOKING"+"  "+subject;
String mess="<table border='1' style='border-collapse:collapse;'><tr><td style='background-color:crimson;color:white;align:center; 'colspan='2' ><center><b>BOOKING INFORMATION</b><center></td></tr><tr><td><b>NAME</b></td><td>"+subject+"</td></tr><tr><td><b>CONTACT</b></td><td>"+message+"</td></tr><tr><td><b>TIME</b></td><td>"+time+"</td></tr><td><b>PICKUP LOCATION</b></td><td>"+pick+"</td><tr><td><b>DROP LOCATION</b></td><td>"+drop+"</td></tr><tr><td>CAR_TYPE</td><td>"+ctype+"</td></tr><tr><td>COST</td><td>"+cost+"</td></tr></table>";
        //Creating SendMail object
        SendMail sm = new SendMail(this,email, info, mess);
        //Executing sendmail to send email
        sm.execute();
    }

}
