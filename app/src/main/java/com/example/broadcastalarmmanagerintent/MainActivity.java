package com.example.broadcastalarmmanagerintent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edTime;
    private String time;
    private Button btConfirm;
    private int REQUEST_CODE = 100;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edTime = findViewById(R.id.edTime);
        edTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePickerDialog();
            }
        });
        btConfirm = findViewById(R.id.btConfirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (time != null) {
                        setAlarmManager();
                        Toast.makeText(MainActivity.this, "Alarma establecida a las " + time, Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(MainActivity.this, "Error: Establezca una alarma", Toast.LENGTH_SHORT).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // Se crea el BroadcastReceiver y se registra,
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent activity = new Intent(context, MainActivity.class);
                context.startActivity(activity);
            }
        };

        // Creamos el intent filter que corresponde al BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcastalarmmanagerintent");

        // Finalmente se registra.
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * Método que visualiza un cuadro de diálogo que contiene un TimePicker para establecer la alarma.
     */
    private void createTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                time = String.format("%02d:%02d", hourOfDay, minute, Locale.FRANCE);
                setTimeAlarm(time);
            }
        },Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void setTimeAlarm(String time) {
        edTime.setText(time);
    }

    /**
     * Método que establece una alarma en el sistema.
     */
    private void setAlarmManager() throws ParseException {
        Date date = null;
        Intent intent = new Intent(this, TemporizadorReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT); // Si se le da varias veces a establecer la larma, usa la última.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.FRANCE);
        date = simpleDateFormat.parse(time);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }
}
