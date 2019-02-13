package com.example.broadcastalarmmanagerintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TemporizadorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Temporizador", "Todo correcto.");
        Intent activity = new Intent(context, MainActivity.class);
        context.startActivity(activity);
    }
}
