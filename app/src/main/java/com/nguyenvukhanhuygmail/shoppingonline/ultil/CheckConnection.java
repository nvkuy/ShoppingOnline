package com.nguyenvukhanhuygmail.shoppingonline.ultil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by toannq on 11/5/2017.
 */

public class CheckConnection {

    //kiểm tra kết nối
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        //lấy tất cả các kết nối của thiết bị và lưu vào mảng
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        //kiểm tra có kết nối wifi hoặc mạng di động trong mảng kết nối hay ko
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    //thông báo khi ko có kết nối wifi và mạng di động
    public static void notification(Context context, String mes) {
        Toast.makeText(context, mes, Toast.LENGTH_LONG);
    }

}
