package com.ssl.san.chopdi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.ExpenseBean;
import com.ssl.san.chopdi.Utils.DBHelper;

import java.util.ArrayList;


public class ExpensesActivity extends AppCompatActivity {
    ArrayList<String> transport,bill,saving,other;
    Spinner expense,subExpense;
    EditText desc,amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        expense = (Spinner)findViewById(R.id.expens_type);
        subExpense = (Spinner)findViewById(R.id.expens_sub_type);
        desc = (EditText)findViewById(R.id.desc);
        amount = (EditText)findViewById(R.id.amount);

        transport = new ArrayList<>();
        bill = new ArrayList<>();
        saving = new ArrayList<>();
        other = new ArrayList<>();
        addSubExpense();
        expense.setOnItemSelectedListener(selected);
    }

    public void saveExpense(View v){
        if(amount.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Amount",Toast.LENGTH_SHORT).show();
            amount.requestFocus();
            return;
        }

        ExpenseBean expenseBean = new ExpenseBean();
        expenseBean.setExpense(expense.getSelectedItem().toString());
        expenseBean.setCategory(subExpense.getSelectedItem().toString());
        expenseBean.setDescription(desc.getText().toString());
        expenseBean.setAmount(Double.parseDouble(amount.getText().toString()));
        DBHelper db = new DBHelper(getApplicationContext());
        boolean flag = db.insertExpense(expenseBean);
        db.close();
        if(flag){
            Toast.makeText(getApplicationContext(),"Expense Saved",Toast.LENGTH_SHORT).show();
            amount.setText("");
            desc.setText("");
            expense.setSelection(0);
        } else {
            Toast.makeText(getApplicationContext(),"Error in saving Expense",Toast.LENGTH_SHORT).show();
        }
    }

    public AdapterView.OnItemSelectedListener selected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_row,transport);
                subExpense.setAdapter(adapter);
            } else  if(position==1){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_row,bill);
                subExpense.setAdapter(adapter);
            } else if(position==2){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_row,saving);
                subExpense.setAdapter(adapter);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_row,other);
                subExpense.setAdapter(adapter);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void addSubExpense(){
        transport.add("Fare");
        transport.add("Petrol");
        transport.add("Diesel");
        transport.add("Mechanic");
        transport.add("Other");

        bill.add("Phone Bill");
        bill.add("Light Bill");
        bill.add("Recharge");
        bill.add("Other");

        saving.add("Bhisi");
        saving.add("Bachat Gat");
        saving.add("Other");

        other.add("Labour Charge");
        other.add("Other");
    }
}
