package com.ssl.san.chopdi.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Santosh on 19-Jul-15.
 */
public class AccountDetails {
    public static boolean isActivated(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        String str = sp.getString(Finals.ACTIVE,"no");
        if(str.equals("yes")){
            return true;
        } else {
            return false;
        }
    }

    public static void setActivated(Context context, String active){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.ACTIVE,active);
        editor.commit();
    }


    public static String getPin(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        return sp.getString(Finals.PIN,"123456");
    }

    public static void setPin(Context context, String pin){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PIN,pin);
        editor.commit();
    }
    public static String getDate(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        return sp.getString(Finals.DATE,"");
    }

    public static void setDate(Context context, String date){
        SharedPreferences sp = context.getSharedPreferences(Finals.SP_NAME,0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE,date);
        editor.commit();
    }
}
