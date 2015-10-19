package com.ssl.san.chopdi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.BillBean;
import com.ssl.san.chopdi.Utils.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Random;

public class SingleBillActivity extends AppCompatActivity {
    TextView bill_no;
    BillBean bill;
    String index;
    EditText recoverAmt,other;
    View recovery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_bill);
        bill_no = (TextView) findViewById(R.id.bill_no);
        index = getIntent().getStringExtra("bill_no");
        recoverAmt = (EditText)findViewById(R.id.recover_amt);
        other = (EditText)findViewById(R.id.other);
        recovery = findViewById(R.id.recovery);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBill();
    }

    public void setBill() {
        super.onResume();
        DBHelper db = new DBHelper(getApplicationContext());
        bill = db.getSingleBill(index);
        db.close();
        if (bill == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        ((TextView) findViewById(R.id.bill_no)).setText("Bill No : " + bill.getId());
        ((TextView) findViewById(R.id.cust_name)).setText(bill.getCustomerBean().getName());
        ((TextView) findViewById(R.id.cust_addr)).setText(bill.getCustomerBean().getAddress());
        ((TextView) findViewById(R.id.cust_mob)).setText(bill.getCustomerBean().getMobile());

        ((TextView) findViewById(R.id.date)).setText(bill.getDate());

        ((TextView) findViewById(R.id.all_height)).setText(bill.getsHeight().replace("X", "-"));
        ((TextView) findViewById(R.id.all_width)).setText(bill.getsWidth().replace("X", "-"));
        ((TextView) findViewById(R.id.paper_size)).setText(bill.getP_size() + "");

        ((TextView) findViewById(R.id.bill_height)).setText("" + bill.getP_height());
        ((TextView) findViewById(R.id.bill_width)).setText("" + bill.getP_width());
        ((TextView) findViewById(R.id.bill_area)).setText(round(new BigDecimal(bill.getP_height() + "").multiply(new BigDecimal(bill.getP_width() + ""))).toString());
        ((TextView) findViewById(R.id.bill_rate)).setText("x " + bill.getP_rate());
        ((TextView) findViewById(R.id.bill_total1)).setText("" + bill.getTotal());
        ((TextView) findViewById(R.id.bill_total2)).setText("" + bill.getTotal());
        ((TextView) findViewById(R.id.bill_paid)).setText("- " + bill.getPaid());
        ((TextView) findViewById(R.id.bill_cheque)).setText("- " + bill.getChequeAmt());
        ((TextView) findViewById(R.id.bill_disc)).setText("- " + bill.getDiscount());
        ((TextView) findViewById(R.id.bill_remain)).setText("" + bill.getRemain());
        ((TextView) findViewById(R.id.bill_date)).setText(date + " " + LoginActivity.monthsList.get(month) + " " + year);
        try{
            other.setText(bill.getChequeBank().split("#")[1]);
        }catch (Exception e){
            other.setText("0.0");
        }
        if(bill.getRemain()<=0){
            recovery.setVisibility(View.INVISIBLE);
        }
    }

    public void callCustomer(View v){
        TextView mobile  = (TextView)v;
        if(mobile.getText().toString().length()!=10){
            Toast.makeText(getApplicationContext(),"Incorrect Mobile Number",Toast.LENGTH_LONG).show();
            return;
        }
        final String mobileNo = mobile.getText().toString();
        final AlertDialog.Builder alert = new AlertDialog.Builder(SingleBillActivity.this)
                .setTitle("Alert !!!")
                .setMessage("Make call to "+mobile.getText().toString()+" ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+mobileNo));
                        startActivity(callIntent);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });
        alert.show();

    }

    public BigDecimal round(BigDecimal bigDecimal) {
        return bigDecimal.divide(new BigDecimal("1"), 2, RoundingMode.HALF_UP);
    }

    public void goBack(View v) {
        onBackPressed();
    }

    public void saveBill(View v) {
        String otherData = "";
        String chequeBank="";
        try{
            chequeBank = bill.getChequeBank().split("#")[0];
            otherData = bill.getChequeBank().split("#")[1];
        }catch (Exception e){
            chequeBank = bill.getChequeBank();
            otherData = other.getText().toString();
        }
        otherData = other.getText().toString();
        DBHelper db = new DBHelper(getApplicationContext());
        db.setOther(chequeBank+"#"+otherData, bill.getId());
        Toast.makeText(getApplicationContext(),"Recovery Saved",Toast.LENGTH_LONG).show();
        db.close();


        View view = findViewById(R.id.bill);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap  bm = view.getDrawingCache();

        String root = Environment.getExternalStorageDirectory()
                .toString();
        File myDir = new File(root + "/bills");
        myDir.mkdirs();
        String fname = bill.getId()+"-"+bill.getCustomerBean().getName()+".jpg";
        File file = new File(myDir, fname);

        Log.e("Saving", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(),"Bill Saved In Bill Folder",Toast.LENGTH_LONG).show();
            //v.setEnabled(false);
            //v.setAlpha(0.5f);
            Uri uri = Uri.fromFile(file);
            Intent scanFileIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            sendBroadcast(scanFileIntent);
        } catch (Exception e) {
           Log.e("Saveing error",e+"");
        }
    }

    public void recoverAmount(View v){
        String recovery = recoverAmt.getText().toString();
        if(recovery.equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Recovery Amount",Toast.LENGTH_LONG).show();
            return;
        }

        if(Double.parseDouble(recovery)<=0){
            Toast.makeText(getApplicationContext(),"Enter Recovery Amount More Than Zero",Toast.LENGTH_LONG).show();
            return;
        }

        if(Double.parseDouble(recovery)>bill.getRemain()){
            Toast.makeText(getApplicationContext(),"Enter Recovery Amount Less Than Or Equal To Remaining Amount",Toast.LENGTH_LONG).show();
            recoverAmt.setText(bill.getRemain()+"");
            return;
        }

        DBHelper db = new DBHelper(getApplicationContext());
        db.setRecovery(Double.parseDouble(recovery),bill.getRemain(),bill.getPaid(), bill.getId());
        Toast.makeText(getApplicationContext(),"Recovery Saved",Toast.LENGTH_LONG).show();
        db.close();
        setBill();
        recoverAmt.setText("");
    }
}
