package br.com.brasil.spa.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Ivan on 21/11/2016.
 */

public class DateDialog extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {


    public DateDialog(){

    }


    public Dialog onCreateDialog(Bundle savedInstaceState){

        final Calendar calendar = Calendar.getInstance();
        int anoR = calendar.get(Calendar.YEAR);
        int mesR = calendar.get(Calendar.MONTH);
        int diaR = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dp = new DatePickerDialog(getContext(), this, anoR, mesR, diaR);

        return dp;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {

        String data = dia + "/" + (mes + 1) + "/" + ano;
        EventBus.getDefault().post(new Eventos.RecebeData(data));

    }
}
