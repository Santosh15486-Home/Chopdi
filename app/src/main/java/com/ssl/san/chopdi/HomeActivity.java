package com.ssl.san.chopdi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowId;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.BillBean;
import com.ssl.san.chopdi.Beans.CustomerBean;
import com.ssl.san.chopdi.Utils.DBHelper;
import com.ssl.san.chopdi.Utils.Finals;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    ArrayList<String> paper_size = new ArrayList<>();
    ArrayList<CustomerBean> customers;

    Spinner paper;
    AutoCompleteTextView cust_auto;
    EditText rate, paid, discount;
    EditText height1, height2, height3, width1, width2, width3;
    TextView remain, total, paperArea, height, width;

    CheckBox cb_cheque;
    EditText chequeAmt, chequeNo, chequeDate, chequeBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        paper = (Spinner) findViewById(R.id.paper_type);
        cust_auto = (AutoCompleteTextView) findViewById(R.id.cust_auto);

        height = (TextView) findViewById(R.id.height);
        width = (TextView) findViewById(R.id.width);
        rate = (EditText) findViewById(R.id.rate);
        paid = (EditText) findViewById(R.id.paid);
        discount = (EditText) findViewById(R.id.disc);


        height1 = (EditText) findViewById(R.id.height1);
        width1 = (EditText) findViewById(R.id.width1);
        height2 = (EditText) findViewById(R.id.height2);
        width2 = (EditText) findViewById(R.id.width2);
        height3 = (EditText) findViewById(R.id.height3);
        width3 = (EditText) findViewById(R.id.width3);

        chequeAmt = (EditText) findViewById(R.id.cheque_amt);
        chequeNo = (EditText) findViewById(R.id.cheque_no);
        chequeDate = (EditText) findViewById(R.id.cheque_date);
        chequeBank = (EditText) findViewById(R.id.cheque_bank);
        cb_cheque = (CheckBox) findViewById(R.id.cb_cheque);

        height1.addTextChangedListener(watcher);
        width1.addTextChangedListener(watcher);
        height2.addTextChangedListener(watcher);
        width2.addTextChangedListener(watcher);
        height3.addTextChangedListener(watcher);
        width3.addTextChangedListener(watcher);
        chequeAmt.addTextChangedListener(watcher);
        height.addTextChangedListener(watcher);
        width.addTextChangedListener(watcher);

        rate.addTextChangedListener(watcher);
        paid.addTextChangedListener(watcher);
        discount.addTextChangedListener(watcher);

        remain = (TextView) findViewById(R.id.remain);
        total = (TextView) findViewById(R.id.total);
        paperArea = (TextView) findViewById(R.id.paper_area);

        paper_size.add("350");
        paper_size.add("500");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, paper_size);
        paper.setAdapter(adapter1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBHelper db = new DBHelper(getApplicationContext());
        customers = db.getAllCustomers();
        db.close();
        ArrayAdapter<CustomerBean> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, customers);
        cust_auto.setAdapter(adapter);
    }

    public void newCustomer(View v) {
        Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
        intent.putExtra(Finals.CALL, Finals.SINGLE);
        startActivity(intent);
    }

    public void calculateAll() {
//        BigDecimal bHeight1 = new BigDecimal(height1.getText().toString().equals("") ? "0" : height1.getText().toString());
//        BigDecimal bWidth1 = new BigDecimal(width1.getText().toString().equals("") ? "0" : width1.getText().toString());
//        BigDecimal bHeight2 = new BigDecimal(height2.getText().toString().equals("") ? "0" : height2.getText().toString());
//        BigDecimal bWidth2 = new BigDecimal(width2.getText().toString().equals("") ? "0" : width2.getText().toString());
//        BigDecimal bHeight3 = new BigDecimal(height3.getText().toString().equals("") ? "0" : height3.getText().toString());
//        BigDecimal bWidth3 = new BigDecimal(width3.getText().toString().equals("") ? "0" : width3.getText().toString());
//
//        BigDecimal bH = new BigDecimal("0");
//        if (bHeight1.compareTo(new BigDecimal("0")) != 0) {
//            bH = bH.add(new BigDecimal("1"));
//        }
//        if (bHeight2.compareTo(new BigDecimal("0")) != 0) {
//            bH = bH.add(new BigDecimal("1"));
//        }
//        if (bHeight3.compareTo(new BigDecimal("0")) != 0) {
//            bH = bH.add(new BigDecimal("1"));
//        }
//
//        BigDecimal bW = new BigDecimal("0");
//        if (bWidth1.compareTo(new BigDecimal("0")) != 0) {
//            bW = bW.add(new BigDecimal("1"));
//        }
//        if (bWidth2.compareTo(new BigDecimal("0")) != 0) {
//            bW = bW.add(new BigDecimal("1"));
//        }
//        if (bWidth3.compareTo(new BigDecimal("0")) != 0) {
//            bW = bW.add(new BigDecimal("1"));
//        }
//
//        if (bH.compareTo(new BigDecimal("0")) == 0) {
//            bH = new BigDecimal("3");
//        }
//
//        if (bW.compareTo(new BigDecimal("0")) == 0) {
//            bW = new BigDecimal("3");
//        }
//
//        BigDecimal meanHeight = bHeight1.add(bHeight2.add(bHeight3));
//        BigDecimal meanWidth = bWidth1.add(bWidth2.add(bWidth3));

//        height.setText(meanHeight.divide(bH, 2, RoundingMode.HALF_UP) + "");
//        width.setText(meanWidth.divide(bW, 2, RoundingMode.HALF_UP) + "");

        BigDecimal bHeight = new BigDecimal(height.getText().toString().equals("") ? "0" : height.getText().toString());
        BigDecimal bWidth = new BigDecimal(width.getText().toString().equals("") ? "0" : width.getText().toString());
        BigDecimal bRate = new BigDecimal(rate.getText().toString().equals("") ? "0" : rate.getText().toString());
        BigDecimal bPaid = new BigDecimal(paid.getText().toString().equals("") ? "0" : paid.getText().toString());
        BigDecimal bDisc = new BigDecimal(discount.getText().toString().equals("") ? "0" : discount.getText().toString());
        BigDecimal bCheque = new BigDecimal(chequeAmt.getText().toString().equals("") ? "0" : chequeAmt.getText().toString());

        paperArea.setText(round(bHeight.multiply(bWidth)).toString() + " Sq.M");
        BigDecimal bTotal = (round(bHeight.multiply(bWidth.multiply(bRate))));
        total.setText(bTotal.toString());
        BigDecimal bRemain = bTotal.subtract(bPaid);
        remain.setText(round((bRemain.subtract(bDisc)).subtract(bCheque)) + "");
    }

    public BigDecimal round(BigDecimal bigDecimal) {
        return bigDecimal.divide(new BigDecimal("1"), 2, RoundingMode.HALF_UP);
    }

    public void saveData(View v) {
        BillBean bill = new BillBean();
        String cust = cust_auto.getText().toString();
        if (cust.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Customer Name", Toast.LENGTH_SHORT).show();
            cust_auto.setText("");
            cust_auto.requestFocus();
            return;
        } else {
            try {
                String idStr = cust.split(":")[0];
                bill.setCustomer(Integer.parseInt(idStr));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Choose Customer From List", Toast.LENGTH_SHORT).show();
                cust_auto.setText("");
                cust_auto.requestFocus();
                return;
            }
        }
        if (height.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Paper Height", Toast.LENGTH_SHORT).show();
            height.requestFocus();
            return;
        }
        if (width.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Paper Width", Toast.LENGTH_SHORT).show();
            width.requestFocus();
            return;
        }
        if (rate.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Paper rate", Toast.LENGTH_SHORT).show();
            rate.requestFocus();
            return;
        }
        if (paid.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Paid Amount", Toast.LENGTH_SHORT).show();
            paid.requestFocus();
            return;
        }
        if (discount.getText().toString().equals("")) {
            discount.setText("0");
        }

        if (cb_cheque.isChecked()) {
            if (chequeAmt.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Cheque Amount", Toast.LENGTH_SHORT).show();
                chequeAmt.requestFocus();
                return;
            }
        }

        bill.setP_height(Double.parseDouble(height.getText().toString()));
        bill.setP_width(Double.parseDouble(width.getText().toString()));
        bill.setsHeight(height1.getText().toString() + " X " + height2.getText().toString() + " X " + height3.getText().toString());
        bill.setsWidth(width1.getText().toString() + " X " + width2.getText().toString() + " X " + width3.getText().toString());
        bill.setP_rate(Double.parseDouble(rate.getText().toString()));
        bill.setPaid(Double.parseDouble(paid.getText().toString()));
        bill.setDiscount(Double.parseDouble(discount.getText().toString()));
        bill.setP_size(Double.parseDouble(paper.getSelectedItem().toString()));
        bill.setPaidByCheque(cb_cheque.isChecked());
        if(cb_cheque.isChecked()){
            bill.setChequeAmt(Double.parseDouble(chequeAmt.getText().toString()));
            bill.setChequeNo(chequeNo.getText().toString());
            bill.setChequeDate(chequeDate.getText().toString());
            bill.setChequeBank(chequeBank.getText().toString());
        }
        bill.setTotal(Double.parseDouble(total.getText().toString()));
        bill.setRemain(Double.parseDouble(remain.getText().toString()));
        DBHelper db = new DBHelper(getApplicationContext());
        boolean flag = db.insertBills(bill);
        db.close();
        if (flag) {
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            clearForm();
            Intent intent = new Intent(getApplicationContext(),SingleBillActivity.class);
            intent.putExtra("bill_no","last");
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Erroe in Saving", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_new_cust) {
            Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
            intent.putExtra(Finals.CALL, Finals.MULTIPLE);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            super.onBackPressed();
            return true;
        }
        if (id == R.id.action_reports) {
            Intent intent = new Intent(getApplicationContext(), ReportsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_expenses) {
            Intent intent = new Intent(getApplicationContext(), ExpensesActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_collection) {
            Intent intent = new Intent(getApplicationContext(), CollectionActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            calculateAll();
        }
    };

    public void clearForm() {
        cust_auto.setText("");
        width.setText("");
        height.setText("");
        width1.setText("");
        height1.setText("");
        width2.setText("");
        height2.setText("");
        width3.setText("");
        height3.setText("");
        rate.setText("");
        discount.setText("");
        paid.setText("");
        total.setText("");
        remain.setText("");
        paperArea.setText("0 Sq.Ft.");
        chequeAmt.setText("");
        chequeNo.setText("");
        chequeDate.setText("");
        chequeBank.setText("");
        cb_cheque.setChecked(false);
        hideChequeDetails();
    }

    public void showChequeDetails() {
        chequeAmt.setVisibility(View.VISIBLE);
        chequeNo.setVisibility(View.VISIBLE);
        chequeDate.setVisibility(View.VISIBLE);
        chequeBank.setVisibility(View.VISIBLE);
    }

    public void hideChequeDetails() {
        chequeAmt.setVisibility(View.INVISIBLE);
        chequeNo.setVisibility(View.INVISIBLE);
        chequeDate.setVisibility(View.GONE);
        chequeBank.setVisibility(View.GONE);
    }

    public void payByCheque(View v) {
        CheckBox cb = (CheckBox) v;
        if (cb.isChecked()) {
            showChequeDetails();
        } else {
            hideChequeDetails();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
