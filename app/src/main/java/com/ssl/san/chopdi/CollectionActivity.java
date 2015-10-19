package com.ssl.san.chopdi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.ExpenseBean;
import com.ssl.san.chopdi.Utils.DBHelper;
import com.ssl.san.chopdi.Utils.Finals;

public class CollectionActivity extends AppCompatActivity {
    EditText name, desc, amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        name = (EditText)findViewById(R.id.c_name);
        desc = (EditText)findViewById(R.id.c_desc);
        amount = (EditText)findViewById(R.id.c_amount);
    }

    public void saveCollection(View v){
        if(name.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }
        if(amount.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Enter Amount",Toast.LENGTH_SHORT).show();
            amount.requestFocus();
            return;
        }
        Double amt = null;
        try{
            amt = Double.parseDouble(amount.getText().toString());
        } catch (Exception e){}
        if(amt==null){
            amount.setText("");
            Toast.makeText(getApplicationContext(),"Enter Amount",Toast.LENGTH_SHORT).show();
            amount.requestFocus();
            return;
        } else {
            ExpenseBean expenseBean = new ExpenseBean();
            expenseBean.setExpense(Finals.COLLECTION);
            expenseBean.setCategory(name.getText().toString());
            expenseBean.setDescription(desc.getText().toString());
            expenseBean.setAmount(amt);
            DBHelper db = new DBHelper(getApplicationContext());
            boolean flag = db.insertExpense(expenseBean);
            db.close();
            if(flag){
                Toast.makeText(getApplicationContext(),"Collection Saved",Toast.LENGTH_SHORT).show();
                name.setText("");
                desc.setText("");
                amount.setText("");

            } else {
                Toast.makeText(getApplicationContext(),"Error in saving Collection",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
