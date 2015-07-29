package com.ssl.san.chopdi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.CustomerBean;
import com.ssl.san.chopdi.Utils.DBHelper;
import com.ssl.san.chopdi.Utils.Finals;

public class CustomerActivity extends AppCompatActivity {
    EditText name,mobile,address;
    boolean singleFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        name = (EditText)findViewById(R.id.name);
        mobile = (EditText)findViewById(R.id.mobile);
        address = (EditText)findViewById(R.id.address);
        if(getIntent().getStringExtra(Finals.CALL).equals(Finals.SINGLE)){
            singleFlag = true;
        }
    }

    public void saveCustomer(View v){
        if(name.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Customer Name",Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if(mobile.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Mobile Number",Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
            return;
        }
        CustomerBean customer = new CustomerBean();
        customer.setName(name.getText().toString());
        customer.setMobile(mobile.getText().toString());
        customer.setAddress(address.getText().toString());
        DBHelper db = new DBHelper(getApplicationContext());
        boolean flag = db.insertCustomer(customer);
        if(flag){
            Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
            clearForm();
            if(singleFlag){
                onBackPressed();
            }
        } else {
            Toast.makeText(getApplicationContext(),"Error In Saving",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void clearForm(){
        name.setText("");
        mobile.setText("");
        address.setText("");
    }
}
