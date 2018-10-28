package com.megalatravels.duraivel.cabbooking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplaintActivity extends AppCompatActivity {
Button b;
EditText Name, Mobile, Complaint;
String com,mobi,name;
Context c= this;
AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complaint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Name=(EditText)findViewById(R.id.compname);
        Mobile=(EditText)findViewById(R.id.mobi);
        Complaint=(EditText)findViewById(R.id.complaint);
b=(Button)findViewById(R.id.report);

b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        com=Complaint.getText().toString().trim();
        if(isValidMessage(com))
        {
            Complaint.setError("Complaint Cannot be Empty!");
        }
        else
        {
            sendEmail(Complaint.getText().toString(),Mobile.getText().toString(),Name.getText().toString());
        }
    }
});

    }
    public boolean isValidMessage(String mes) {
        Pattern p = Pattern.compile("\\s*");//. represents single character
        Matcher m = p.matcher(mes);
        boolean b = m.matches();
        return b;
    }

    public void sendEmail(String complaint,String mobile,String Name)
    {
        String mob="";
        //Getting content for email
        String email = "megalatravels@yahoo.com";
        //  String time = editTextSubject.getText().toString().trim();
        String message = complaint;
        //Creating SendMail object

        SendMail sm = new SendMail(this,email, "New complaint from "+Name,message);
        //Executing sendmail to send email
        sm.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.make_journey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if (id == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_rigt);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}