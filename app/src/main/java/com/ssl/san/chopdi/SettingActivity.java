package com.ssl.san.chopdi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ssl.san.chopdi.Utils.AccountDetails;

public class SettingActivity extends AppCompatActivity {
    EditText old, new1, new2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        old = (EditText)findViewById(R.id.old_pin);
        new1 = (EditText)findViewById(R.id.new_pin1);
        new2 = (EditText)findViewById(R.id.new_pin2);
    }

    public void saveSettings(View v){
        if(old.getText().toString().length()!=6){
            Toast.makeText(getApplicationContext(),"Please Enter 6 Digits In Old",Toast.LENGTH_SHORT).show();
            old.requestFocus();
            return;
        }
        if(new1.getText().toString().length()!=6){
            Toast.makeText(getApplicationContext(),"Please Enter 6 Digits In New",Toast.LENGTH_SHORT).show();
            new1.requestFocus();
            return;
        }
        if(new2.getText().toString().length()!=6){
            Toast.makeText(getApplicationContext(),"Please Enter 6 Digits In Confirm",Toast.LENGTH_SHORT).show();
            new2.requestFocus();
            return;
        }
        if(new1.getText().toString().equals(new2.getText().toString())){
            if(old.getText().toString().equals(AccountDetails.getPin(getApplicationContext()))){
                AccountDetails.setPin(getApplicationContext(),new1.getText().toString());
                Toast.makeText(getApplicationContext(),"Pin Changed Successfully",Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(),"Enter Correct Old Pin",Toast.LENGTH_SHORT).show();
                old.setText("");
                old.requestFocus();
            }
        } else {
            Toast.makeText(getApplicationContext(),"New Pin Not Matching",Toast.LENGTH_SHORT).show();
            new1.setText("");
            new2.setText("");
            new1.requestFocus();
        }
    }
}
