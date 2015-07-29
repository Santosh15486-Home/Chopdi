package com.ssl.san.chopdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.san.chopdi.Utils.AccountDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    public long freeTime =60*1000*10*24*60;
    EditText pin;
    Button loginButton;
    TextView message, pinLabel;
    public static ArrayList<String> monthsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        monthsList = new ArrayList<>();

        monthsList.add("Jan"); monthsList.add("Feb"); monthsList.add("Mar");
        monthsList.add("Apr");monthsList.add("May"); monthsList.add("Jun");
        monthsList.add("Jul");monthsList.add("Aug"); monthsList.add("Sep");
        monthsList.add("Oct");monthsList.add("Nov"); monthsList.add("Dec");

        pin = (EditText)findViewById(R.id.acc_pin);
        loginButton = (Button)findViewById(R.id.login_button);
        message = (TextView)findViewById(R.id.acc_msg);
        pinLabel = (TextView)findViewById(R.id.pin_label);
        if(!checkAccount()){
            pin.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            pinLabel.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
    }


    public void userLogin(View v){
        String userPin = pin.getText().toString();
        if(userPin.equals(AccountDetails.getPin(getApplicationContext()))){
            pin.setText("");
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"Invalid Pin",Toast.LENGTH_LONG).show();
            pin.setText("");
            pin.requestFocus();
        }
    }

    public boolean checkAccount(){
        boolean access = true;
        if(AccountDetails.getDate(getApplicationContext()).equals("")){
            AccountDetails.setDate(getApplicationContext(), Calendar.getInstance().getTimeInMillis()+"");
        } else{
            long oldTime = Long.parseLong(AccountDetails.getDate(getApplicationContext()));
            long newTime =  Calendar.getInstance().getTimeInMillis();
            if((newTime - oldTime)>freeTime){
                access = false;
            }
        }
        if(!access) {
            access = AccountDetails.isActivated(getApplicationContext());
        }
        return access;
    }
}
