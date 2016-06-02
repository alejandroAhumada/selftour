package cl.selftourhamburger.Util;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Alejandro on 01-06-2016.
 */
public class AlertUtils {

    public static void showErrorAlert(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton("Cerrar", null);
        builder.create().show();
    }

}
