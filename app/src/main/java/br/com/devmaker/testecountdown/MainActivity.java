package br.com.devmaker.testecountdown;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
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

import br.com.devmaker.testecountdown.local.LocalDbImplement;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private CountDown countDown;
    private NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    private NotificationManager mNotifyMgr;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

//    Atributos do tempo
    public static long TIME = 0;
    public static long ALARM = 0;
    public static Date dateclose;
    public static long MILLIS;
    public static long secondsLeft;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

       timer = new LocalDbImplement<Timer>(MainActivity.this).getDefault(Timer.class);

        TIME = getDateDifference(timer.getDateFinish());

        // wrap your stuff in a componentName
        ComponentName mServiceComponent = new ComponentName(this, TimerJobSchedulerService.class);
        // set up conditions for the job
        android.app.job.JobInfo.Builder builder = new android.app.job.JobInfo.Builder(1, mServiceComponent);

        android.app.job.JobInfo task = builder.setRequiredNetworkType(android.app.job.JobInfo.NETWORK_TYPE_UNMETERED)
                .setOverrideDeadline(100)
                .setPersisted(true)
                .build();
        // inform the system of the job
        android.app.job.JobScheduler jobScheduler = (android.app.job.JobScheduler ) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(task);


        textView.setText("00:00");



//        createNotification();
//
        //Cria o contador com 1 minuto
        countDown = new CountDown(TIME);

        //Cria e atribui um CountDownBehavior ao contador
        countDown.setCountDownListener(new CountDownBehavior(0, "mm:ss") {
            @Override
            public void onEnd() {
//                mBuilder.mActions.clear();
//                mBuilder.setOngoing(false);
//                mBuilder.setContentText("Seu tempo acabou.");
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                //mNotifyMgr.cancel(mNotificationId);
            }

            @Override
            protected void onAlarm() {
                //alarmMethodwithCount();
            }

            @Override
            protected void displayTimeLeft(String timeLeft) {
//                mBuilder.setContentText(timeLeft + " restante");
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                textView.setText(timeLeft);
            }
        });

//        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            countDown.start();
//        alarmMethod();
    }

    protected void startClick(View v){
        countDown.start();
    }

    protected void addClick(View v){
        TimerJobSchedulerService.addIncrease();

    }

    protected void stopClick(View v){

    }

    protected void resumeClick(View v){
        countDown.resume();
    }

     int exit = 0;

    @Override
    protected void onDestroy() {

     //mNotifyMgr.cancel(mNotificationId);
        super.onDestroy();
    }


    @Override
    protected void onStop() {
//        new AlertDialog.Builder(this)
//                .setTitle("Confirmar")
//                .setMessage("Se você fechar o app, algumas funções podem não funcionar corretamente.")
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                       dialog.dismiss();
//                    }});
//

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


//    comparar o tempo que foi aberto a activity, com o tempo para finalizar o periodo
    public long getDateDifference(String finishDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String actualDate = dateFormat.format(Calendar.getInstance().getTime());

        try {
            Date dateFinish = dateFormat.parse(finishDate);

            Date currentDate = dateFormat.parse(actualDate);

            long diff = dateFinish.getTime() - currentDate.getTime();

            long days = diff / (24 * 60 * 60 * 1000);
            diff -= days * (24 * 60 * 60 * 1000);

            long hours = diff / (60 * 60 * 1000);
            diff -= hours * (60 * 60 * 1000);

            long minutes = diff / (60 * 1000);
            diff -= minutes * (60 * 1000);

            long seconds = diff / 1000;

            seconds = seconds + 60*minutes;


            return  seconds;

//            counterDaysTV.setText(days + "");
//            counterMinsTV.setText(minutes + "");
//            counterHoursTV.setText(hours + "");
//            counterSecTV.setText(seconds + "");

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    
//
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }
}
