package com.ssl.san.chopdi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ssl.san.chopdi.Beans.BillBean;
import com.ssl.san.chopdi.Beans.ExpenseBean;
import com.ssl.san.chopdi.Beans.TotalsBean;
import com.ssl.san.chopdi.Utils.DBHelper;
import com.ssl.san.chopdi.Utils.Finals;

import java.util.ArrayList;


public class ReportsActivity extends AppCompatActivity {
    Spinner yearsSpinner, monthsSpinner, daySpinner;
    ArrayList<String> years, months, days;
    String year = "All", month = "All", day = "All";
    int page = 0;
    Button next, prev;
    ArrayList<BillBean> bills = null;
    ArrayList<ExpenseBean> expenses = null;
    LinearLayout records;

    Button expenseBtn, billBtn;

    String reportType = "BILL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        yearsSpinner = (Spinner) findViewById(R.id.all_years);
        monthsSpinner = (Spinner) findViewById(R.id.all_months);
        daySpinner = (Spinner) findViewById(R.id.all_days);

        expenseBtn = (Button) findViewById(R.id.report_expense);
        billBtn = (Button) findViewById(R.id.report_bill);

        next = (Button) findViewById(R.id.btn_next);
        prev = (Button) findViewById(R.id.btn_prev);
        records = (LinearLayout) findViewById(R.id.records);

        yearsSpinner.setOnItemSelectedListener(itemClicked1);
        monthsSpinner.setOnItemSelectedListener(itemClicked2);
        daySpinner.setOnItemSelectedListener(itemClicked3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setYearsAdapter();
    }

    public void setYearsAdapter() {
        DBHelper db = new DBHelper(getApplicationContext());
        years = db.getYears(reportType);
        db.close();
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, years);
        yearsSpinner.setAdapter(yearAdapter);
    }

    public void setMonthsAdapter() {
        DBHelper db = new DBHelper(getApplicationContext());
        months = db.getMonths(year, reportType);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, months);
        monthsSpinner.setAdapter(monthAdapter);
        db.close();
    }

    public void setDaysAdapter() {
        DBHelper db = new DBHelper(getApplicationContext());
        days = db.getDays(year, month, reportType);
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, days);
        daySpinner.setAdapter(dayAdapter);
        db.close();
    }

    public AdapterView.OnItemSelectedListener itemClicked1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                year = yearsSpinner.getSelectedItem().toString();
                if (!year.contains("All")) {
                    setMonthsAdapter();
                    ((LinearLayout) monthsSpinner.getParent()).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) monthsSpinner.getParent()).setVisibility(View.INVISIBLE);
                    ((LinearLayout) daySpinner.getParent()).setVisibility(View.INVISIBLE);
                    month = "All";
                    day = "All";
                    page = 0;
                    new GetData().execute();
                }
            } catch (Exception e) {
                year = "All";
                month = "All";
                day = "All";
                ((LinearLayout) monthsSpinner.getParent()).setVisibility(View.INVISIBLE);
                ((LinearLayout) daySpinner.getParent()).setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public AdapterView.OnItemSelectedListener itemClicked2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                month = monthsSpinner.getSelectedItem().toString();

                if (!month.contains("All")) {
                    setDaysAdapter();
                    ((LinearLayout) daySpinner.getParent()).setVisibility(View.VISIBLE);
                } else {
                    ((LinearLayout) daySpinner.getParent()).setVisibility(View.INVISIBLE);
                    day = "All";
                    page = 0;
                    new GetData().execute();
                }
            } catch (Exception e) {
                month = "All";
                day = "All";
                ((LinearLayout) daySpinner.getParent()).setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public AdapterView.OnItemSelectedListener itemClicked3 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                day = daySpinner.getSelectedItem().toString();
            } catch (Exception e) {
                Log.d("day", e + "");
                day = "All";
            } finally {
                page = 0;
                new GetData().execute();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void showPrev(View v) {
        if (page > 0) {
            page--;
        }
        new GetData().execute();
    }

    public void showNext(View v) {
        page++;
        new GetData().execute();
    }

    public class GetData extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (page == 0) {
                prev.setVisibility(View.INVISIBLE);
            } else {
                prev.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            DBHelper db = new DBHelper(getApplicationContext());
            if (reportType.equals("BILL")) {
                bills = db.getBills(year, month, day, page);
            } else {
                expenses = db.getExpenses(year, month, day, page);
            }
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (reportType.equals("BILL")) {
                if (bills != null && bills.size() < Finals.RECORD_LIMIT) {
                    next.setVisibility(View.INVISIBLE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
            } else {
                if (expenses != null && expenses.size() < Finals.RECORD_LIMIT) {
                    next.setVisibility(View.INVISIBLE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
            }
            setData();
        }
    }

    public void setData() {
        records.removeAllViews();
        if (reportType.equals("BILL")) {
            for (int i = 0; i < bills.size(); i++) {
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout record = (LinearLayout) inflater.inflate(R.layout.record, null);

                TextView name = (TextView) record.findViewById(R.id.r_name);
                LinearLayout ll_1 = (LinearLayout) record.findViewById(R.id.ll_1);
                LinearLayout ll_2 = (LinearLayout) record.findViewById(R.id.ll_2);
                final Button delete = (Button) record.findViewById(R.id.del_rec);

                name.setTag(bills.get(i).getId());
                ll_1.setTag(bills.get(i).getId());
                ll_2.setTag(bills.get(i).getId());
                delete.setTag(bills.get(i).getId());

                name.setText(bills.get(i).getId() + ": " + bills.get(i).getCustomerBean().getName());
                ((TextView) record.findViewById(R.id.r_date)).setText(bills.get(i).getDate());
                ((TextView) record.findViewById(R.id.r_dimen)).setText(bills.get(i).getP_height() + " X " + bills.get(i).getP_width());
                ((TextView) record.findViewById(R.id.r_size)).setText("OF " + bills.get(i).getP_size() + "m");
                ((TextView) record.findViewById(R.id.r_rate)).setText(bills.get(i).getP_rate() + "/Sq.M");
                ((TextView) record.findViewById(R.id.r_total)).setText("Rs. " + bills.get(i).getTotal());
                ((TextView) record.findViewById(R.id.r_paid)).setText((bills.get(i).getPaid() + bills.get(i).getChequeAmt()) + " Rs.");
                ((TextView) record.findViewById(R.id.r_remain)).setText(bills.get(i).getRemain() + " Rs.");
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SingleBillActivity.class);
                        intent.putExtra("bill_no", v.getTag() + "");
                        startActivity(intent);
                    }
                });
                ll_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SingleBillActivity.class);
                        intent.putExtra("bill_no", v.getTag() + "");
                        startActivity(intent);
                    }
                });
                ll_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SingleBillActivity.class);
                        intent.putExtra("bill_no", v.getTag() + "");
                        startActivity(intent);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askDelete(v.getTag() + "",0);
                    }
                });
                records.addView(record);
            }
        } else {
            for (int i = 0; i < expenses.size(); i++) {
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout record = (LinearLayout) inflater.inflate(R.layout.expense, null);
                final Button delete = (Button) record.findViewById(R.id.del_rec);
                if(expenses.get(i).getExpense().equals(Finals.COLLECTION)) {
                    ((TextView) record.findViewById(R.id.e_expance)).setText(expenses.get(i).getId() + ": " + expenses.get(i).getExpense());
                } else {
                    ((TextView) record.findViewById(R.id.e_expance))
                            .setText(Html.fromHtml("<font color=red>" + expenses.get(i).getId() + ": " + expenses.get(i).getExpense() + "</font>"));
                }

                delete.setTag(expenses.get(i).getId());
                ((TextView) record.findViewById(R.id.e_date)).setText(expenses.get(i).getDate());
                ((TextView) record.findViewById(R.id.e_category)).setText(expenses.get(i).getCategory());
                ((TextView) record.findViewById(R.id.e_amount)).setText(expenses.get(i).getAmount() + " Rs.");
                ((TextView) record.findViewById(R.id.e_desc)).setText(expenses.get(i).getDescription());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askDelete(v.getTag() + "",1);
                    }
                });
                records.addView(record);
            }
        }

        DBHelper db = new DBHelper(getApplicationContext());
        try {
            TotalsBean totals = db.getTotals(year, month, day);
            ((TextView) findViewById(R.id.all_total)).setText("Total : " + totals.getTotal());
            ((TextView) findViewById(R.id.all_paid)).setText((totals.getPaid() + totals.getChequeAmt()) + " : Paid");
            ((TextView) findViewById(R.id.all_discount)).setText("Disc : " + totals.getDiscount());
            ((TextView) findViewById(R.id.all_remain)).setText(totals.getRemain() + " : Remain");
            ((TextView) findViewById(R.id.all_expense)).setText("Expense : " + totals.getExpense());
            ((TextView) findViewById(R.id.all_collection)).setText(totals.getCollection()+" : Collection");
        } catch (Exception e) {
            //Log.e("Totals Error",e+"");
        }
        db.close();
    }

    public void showBills(View v) {
        v.setVisibility(View.GONE);
        expenseBtn.setVisibility(View.VISIBLE);
        reportType = "EXPENSE";
        page = 0;
        setYearsAdapter();
        new GetData().execute();
    }

    public void showExpenses(View v) {
        v.setVisibility(View.GONE);
        billBtn.setVisibility(View.VISIBLE);
        reportType = "BILL";
        page = 0;
        setYearsAdapter();
        new GetData().execute();
    }

    public void askDelete(final String id,final int table){
        AlertDialog.Builder alert = new AlertDialog.Builder(ReportsActivity.this).setTitle("Sure to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord(id,table);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    public void deleteRecord(String id, int table){
        Log.e("Deleting",id);
        DBHelper db = new DBHelper(getApplicationContext());
        db.deleteRecord(id,table);
        db.close();
        new GetData().execute();
    }
}
