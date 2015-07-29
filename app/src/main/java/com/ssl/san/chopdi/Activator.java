package com.ssl.san.chopdi;

/**
 * Created by Santosh on 21-Jul-15.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.ssl.san.chopdi.Utils.AccountDetails;
import com.ssl.san.chopdi.Utils.Finals;


public class Activator extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            String smsBody = "";
            String address="";
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                smsBody = smsMessage.getMessageBody().toString();
                address = smsMessage.getOriginatingAddress();
            }
            if (address.contains(Finals.SMS_SENDER)) {
                if(smsBody.equals("YES")){
                    AccountDetails.setActivated(context,"yes");
                }
            }
        }
    }
}
