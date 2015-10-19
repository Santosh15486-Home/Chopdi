package com.ssl.san.chopdi.Utils;

/**
 * Created by Santosh on 22-Jul-15.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.ssl.san.chopdi.Beans.BillBean;
import com.ssl.san.chopdi.Beans.CustomerBean;
import com.ssl.san.chopdi.Beans.ExpenseBean;
import com.ssl.san.chopdi.Beans.TotalsBean;
import com.ssl.san.chopdi.LoginActivity;
import com.ssl.san.chopdi.ReportsActivity;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key AUTOINCREMENT, name text,mobile text,address text)"
        );
        db.execSQL(
                "create table expense " +
                        "(id integer primary key AUTOINCREMENT, expense text,category text,desc text,amount double,day int, month int,year int)"
        );
        db.execSQL(
                "create table bills " +
                        "(id integer primary key AUTOINCREMENT, customer integer,p_height double,p_width double,all_height text,all_width text," +
                        "p_size double,p_rate double,total double,paid double,discount double, remain double, paid_by_cheque boolean," +
                        "cheque_amt double, cheque_no text, cheque_date text, cheque_bank text,day int, month int,year int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertExpense(ExpenseBean expense) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("expense", expense.getExpense());
        contentValues.put("category", expense.getCategory());
        contentValues.put("desc", expense.getDescription());
        contentValues.put("amount", expense.getAmount());

        contentValues.put("day", date);
        contentValues.put("month", month);
        contentValues.put("year", year);

        db.insert("expense", null, contentValues);
        //Log.e("Inserted", "#############");
        return true;
    }

    public boolean insertCustomer(CustomerBean customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", customer.getName());
        contentValues.put("mobile", customer.getMobile());
        contentValues.put("address", customer.getAddress());
        db.insert("contacts", null, contentValues);
        //Log.e("Inserted", "#############");
        return true;
    }

    public boolean insertBills(BillBean bill) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("customer", bill.getCustomer());

        contentValues.put("p_height", bill.getP_height());
        contentValues.put("p_width", bill.getP_width());

        contentValues.put("all_height", bill.getsHeight());
        contentValues.put("all_width", bill.getsWidth());

        contentValues.put("p_size", bill.getP_size());
        contentValues.put("p_rate", bill.getP_rate());
        contentValues.put("total", bill.getTotal());
        contentValues.put("paid", bill.getPaid());
        contentValues.put("discount", bill.getDiscount());
        contentValues.put("remain", bill.getRemain());

        contentValues.put("paid_by_cheque", bill.isPaidByCheque());
        contentValues.put("cheque_amt", bill.getChequeAmt());
        contentValues.put("cheque_no", bill.getChequeNo());
        contentValues.put("cheque_date", bill.getChequeDate());
        contentValues.put("cheque_bank", bill.getChequeBank());

        contentValues.put("day", date);
        contentValues.put("month", month);
        contentValues.put("year", year);

        db.insert("bills", null, contentValues);
        //Log.e("Inserted", "#############");
        return true;
    }

    public ArrayList<CustomerBean> getAllCustomers() {
        ArrayList<CustomerBean> customers = new ArrayList<CustomerBean>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            CustomerBean customer = new CustomerBean();
            customer.setId(res.getInt(res.getColumnIndex("id")));
            customer.setName(res.getString(res.getColumnIndex("name")));
            customer.setMobile(res.getString(res.getColumnIndex("mobile")));
            customer.setAddress(res.getString(res.getColumnIndex("address")));
            customers.add(customer);
            res.moveToNext();
        }
        return customers;
    }

    public TotalsBean getTotals(String year,String month,String day){
        TotalsBean totals = new TotalsBean();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(total),sum(paid),sum(discount),sum(remain),sum(cheque_amt) from bills where 1=1";
        if(!year.contains("All")){
            query = query + " and year = "+year;
            if(!month.contains("All")){
                query = query + " and month = "+LoginActivity.monthsList.indexOf(month);
            }
            if(!day.contains("All")){
                query = query + " and day = "+day;
            }
        }
        //Log.e("Totals Query",query);
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        //total paid discount remain cheque_amt
        while (res.isAfterLast() == false) {
            totals.setTotal(res.getDouble(0));
            totals.setPaid(res.getDouble(1));
            totals.setDiscount(res.getDouble(2));
            totals.setRemain(res.getDouble(3));
            totals.setChequeAmt(res.getDouble(4));
            res.moveToNext();
        }

        query = "select sum(amount) from expense where expense !='"+Finals.COLLECTION+"' ";
        if(!year.contains("All")){
            query = query + " and year = "+year;
            if(!month.contains("All")){
                query = query + " and month = "+LoginActivity.monthsList.indexOf(month);
            }
            if(!day.contains("All")){
                query = query + " and day = "+day;
            }
        }
        //Log.e("Totals Query",query);
        Cursor res1 = db.rawQuery(query, null);
        res1.moveToFirst();
        //total paid discount remain cheque_amt
        while (res1.isAfterLast() == false) {
            totals.setExpense(res1.getDouble(0));
            //Log.e("Totals Query",res1.getDouble(0)+"");
            res1.moveToNext();
        }


        query = "select sum(amount) from expense where expense ='"+Finals.COLLECTION+"' ";
        if(!year.contains("All")){
            query = query + " and year = "+year;
            if(!month.contains("All")){
                query = query + " and month = "+LoginActivity.monthsList.indexOf(month);
            }
            if(!day.contains("All")){
                query = query + " and day = "+day;
            }
        }
        //Log.e("Totals Query",query);
        Cursor res2 = db.rawQuery(query, null);
        res2.moveToFirst();
        //total paid discount remain cheque_amt
        while (res2.isAfterLast() == false) {
            totals.setCollection(res2.getDouble(0));
            //Log.e("Totals Query",res1.getDouble(0)+"");
            res2.moveToNext();
        }

        return totals;
    }

    public ArrayList<ExpenseBean> getExpenses(String year,String month,String day, int page) {
        ArrayList<ExpenseBean> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from expense where 1=1 ";
        if(!year.contains("All")){
            query = query + " and year = "+year;
            if(!month.contains("All")){
                query = query + " and month = "+LoginActivity.monthsList.indexOf(month);
            }
            if(!day.contains("All")){
                query = query + " and day = "+day;
            }
        }
        query = query +" order by 1 desc limit "+(page*Finals.RECORD_LIMIT)+","+Finals.RECORD_LIMIT;
        //Log.e("Record query",query);
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        //id  expense  category  desc  amount day   month year
        while (res.isAfterLast() == false) {
            ExpenseBean expense = new ExpenseBean();
            expense.setId(res.getInt(0));
            expense.setExpense(res.getString(1));
            expense.setCategory(res.getString(2));
            expense.setDescription(res.getString(3));
            expense.setAmount(res.getDouble(4));
            expense.setDate(res.getInt(5)+" "+LoginActivity.monthsList.get(res.getInt(6))+" "+res.getInt(7));
            expenses.add(expense);
            res.moveToNext();
        }
        return expenses;
    }


    public ArrayList<BillBean> getBills(String year,String month,String day, int page) {
        ArrayList<BillBean> bills = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select b.*,c.* from bills b, contacts c where b.customer = c.id ";
        if(!year.contains("All")){
            query = query + " and year = "+year;
            if(!month.contains("All")){
                query = query + " and month = "+LoginActivity.monthsList.indexOf(month);
            }
            if(!day.contains("All")){
                query = query + " and day = "+day;
            }
        }
        query = query +" order by 1 desc limit "+(page*Finals.RECORD_LIMIT)+","+Finals.RECORD_LIMIT;
       // Log.e("Record query",query);
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        //id , customer ,p_height ,p_width ,all_height ,
        // all_width ,p_size ,p_rate ,total ,
        // paid ,discount , remain ,
        // paid_by_cheque , cheque_amt , cheque_no , cheque_date , cheque_bank ,
        // day , month ,year
        while (res.isAfterLast() == false) {
            BillBean bill = new BillBean();
            bill.setId(res.getInt(0));
            bill.setCustomer(res.getInt(1));
            bill.setP_height(res.getDouble(2));
            bill.setP_width(res.getDouble(3));
            bill.setsHeight(res.getString(4));

            bill.setsWidth(res.getString(5));
            bill.setP_size(res.getDouble(6));
            bill.setP_rate(res.getDouble(7));
            bill.setTotal(res.getDouble(8));

            bill.setPaid(res.getDouble(9));
            bill.setDiscount(res.getDouble(10));
            bill.setRemain(res.getDouble(11));

            //bill.setPaidByCheque(res.getShort(12));
            bill.setChequeAmt(res.getDouble(13));
            bill.setChequeNo(res.getString(14));
            bill.setChequeDate(res.getString(15));
            bill.setChequeBank(res.getString(16));

            bill.setDate(res.getInt(17)+" "+LoginActivity.monthsList.get(res.getInt(18))+" "+res.getInt(19));

            bill.getCustomerBean().setName(res.getString(21));
            bill.getCustomerBean().setMobile(res.getString(22));
            bill.getCustomerBean().setAddress(res.getString(23));

            bills.add(bill);
            res.moveToNext();
        }
        return bills;
    }

    public BillBean getSingleBill(String index) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select b.*,c.* from bills b, contacts c where b.customer = c.id ";
        if(!index.equals("last")){
            query = query + " and b.id = "+index;
        }
        query = query +" order by 1 desc limit 0,1 ";
        Log.e(" Single Record query", query);
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        BillBean bill = null;
        while (res.isAfterLast() == false) {
            bill = new BillBean();
            bill.setId(res.getInt(0));
            bill.setCustomer(res.getInt(1));
            bill.setP_height(res.getDouble(2));
            bill.setP_width(res.getDouble(3));
            bill.setsHeight(res.getString(4));

            bill.setsWidth(res.getString(5));
            bill.setP_size(res.getDouble(6));
            bill.setP_rate(res.getDouble(7));
            bill.setTotal(res.getDouble(8));

            bill.setPaid(res.getDouble(9));
            bill.setDiscount(res.getDouble(10));
            bill.setRemain(res.getDouble(11));

            //bill.setPaidByCheque(res.getShort(12));
            bill.setChequeAmt(res.getDouble(13));
            bill.setChequeNo(res.getString(14));
            bill.setChequeDate(res.getString(15));
            bill.setChequeBank(res.getString(16));

            bill.setDate(res.getInt(17)+" "+LoginActivity.monthsList.get(res.getInt(18))+" "+res.getInt(19));

            bill.getCustomerBean().setName(res.getString(21));
            bill.getCustomerBean().setMobile(res.getString(22));
            bill.getCustomerBean().setAddress(res.getString(23));

            res.moveToNext();
        }
        return bill;
    }

    public ArrayList<String> getYears(String reportType) {
        ArrayList<String> years = new ArrayList<>();
        years.add("All Years");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct(year) from bills";
        if(!reportType.equals("BILL")){
            query = "select distinct(year) from expense";
        }
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            years.add(res.getInt(0) + "");
            res.moveToNext();
        }
        return years;
    }

    public ArrayList<String> getMonths(String year,String reportType) {
        ArrayList<String> months = new ArrayList<>();
        months.add("All Months");
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct(month) from bills where year = "+year;
        if(!reportType.equals("BILL")){
            query = "select distinct(month) from expense where year = "+year;
        }
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            months.add(LoginActivity.monthsList.get(res.getInt(0)));
            res.moveToNext();
        }
        return months;
    }

    public void deleteRecord(String id, int table){
        SQLiteDatabase db = this.getReadableDatabase();
        if(table==0) {
            String query = "delete from bills where id = " + id;
            db.execSQL(query);
        } else {
            String query = "delete from expense where id = " + id;
            db.execSQL(query);
        }
    }

    public ArrayList<String> getDays(String year, String month,String reportType) {
        ArrayList<String> days = new ArrayList<>();
        days.add("All Days");
        String query = "select distinct(day) from bills where year = "+year+" and month = "+LoginActivity.monthsList.indexOf(month);
        if(!reportType.equals("BILL")){
            query = "select distinct(day) from expense where year = "+year+" and month = "+LoginActivity.monthsList.indexOf(month);
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            days.add(res.getInt(0)+"");
            res.moveToNext();
        }
        return days;
    }

    public boolean updateCustomer(CustomerBean customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", customer.getName());
        contentValues.put("mobile", customer.getMobile());
        contentValues.put("address", customer.getAddress());
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(customer.getId())});
        return true;
    }

    public boolean setRecovery(Double amount,Double remain,Double paid, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("paid", paid+amount);
        contentValues.put("remain", remain-amount);
        db.update("bills", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean setOther(String other, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cheque_bank", other);
        db.update("bills", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }
}