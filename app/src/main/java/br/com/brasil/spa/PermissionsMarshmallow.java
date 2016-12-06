package br.com.brasil.spa;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Ivan on 18/11/2016.
 */

public class PermissionsMarshmallow {

    public static boolean temPermissaoInternet(Activity activity){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checaPermissaoInternet(activity) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private static int checaPermissaoInternet(Activity activity){
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);
    }

    public static void solicitaPermissaoInternet(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.INTERNET},
                1); // 1 = resultado
    }


    public static boolean temPermissaoNetworkState(Activity activity){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checaPermissaoNetWorkState(activity) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private static int checaPermissaoNetWorkState(Activity activity){
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_NETWORK_STATE);
    }

    public static void solicitaPermissaoNetworkState(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                1); // 1 = resultado
    }

}
