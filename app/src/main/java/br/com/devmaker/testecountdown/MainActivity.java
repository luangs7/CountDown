package br.com.devmaker.testecountdown;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private CountDown countDown;
    private NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    private NotificationManager mNotifyMgr;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

//    Atributos do tempo
    public static long TIME = 1*60;
    public static long ALARM = 10;
    public static Date dateclose;
    public static long MILLIS;
    public static long secondsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

        createNotification();

        //Cria o contador com 1 minuto
        countDown = new CountDown(TIME);

        //Cria e atribui um CountDownBehavior ao contador
        countDown.setCountDownListener(new CountDownBehavior(ALARM, "mm:ss") {
            @Override
            public void onEnd() {
                mBuilder.mActions.clear();
                mBuilder.setOngoing(false);
                mBuilder.setContentText("Seu tempo acabou.");
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                //mNotifyMgr.cancel(mNotificationId);
            }

            @Override
            protected void onAlarm() {
                alarmMethodwithCount();
            }

            @Override
            protected void displayTimeLeft(String timeLeft) {
                mBuilder.setContentText(timeLeft + " restante");
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                textView.setText(timeLeft);
            }
        });

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        countDown.start();
        alarmMethod();
    }

    protected void startClick(View v){
        countDown.start();
    }

    protected void addClick(View v){
        secondsLeft = countDown.increaseBy(60);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        TIME = TIME + (1*60);
        alarmMethod();

    }

    protected void stopClick(View v){

    }

    protected void resumeClick(View v){
        countDown.resume();
    }

     int exit = 0;

    @Override
    protected void onDestroy() {

     mNotifyMgr.cancel(mNotificationId);
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("Se você fechar o app, algumas funções podem não funcionar corretamente.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       dialog.dismiss();
                    }});


        super.onStop();
    }

    public void createNotification(){

        Intent resultIntent = new Intent(this, TempoActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_lock_power_off)
                        .setContentTitle("EstaR")
//                        .setContentText("1 minuto restante")
//                        .addAction(android.R.drawable.btn_plus,"Renovar",pendingIntentYes)
//                        .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Cancelar",null)
                        .setOngoing(true);




        // Gets an instance of the NotificationManager service
        mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.

        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void alarmMethod(){

        Intent myIntent = new Intent(this , NotifyService.class);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Date data = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(data);

        Integer seconds = (int) (long) TIME;
        seconds = (seconds / 60);

        calendar.add(Calendar.MINUTE,seconds);

        MILLIS  = calendar.getTimeInMillis() - (ALARM * 1000);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, MILLIS, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, MILLIS, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, MILLIS, pendingIntent);

       Toast.makeText(MainActivity.this, "Alarme programado", Toast.LENGTH_LONG).show();
    }

    private void alarmMethodwithCount(){
        Intent myIntent = new Intent(this , NotifyService.class);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Date data = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(data);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
           alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Toast.makeText(MainActivity.this, "Start Alarm2", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
