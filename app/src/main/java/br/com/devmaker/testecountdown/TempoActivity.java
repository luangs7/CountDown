package br.com.devmaker.testecountdown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.com.devmaker.testecountdown.local.LocalDbImplement;

public class TempoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempo);

        Button btnActive = (Button) findViewById(R.id.buttonActive);

        btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = "";
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    date = df.format(Calendar.getInstance().getTime());
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                new LocalDbImplement<Timer>(TempoActivity.this).save(new Timer(date));
                startActivity(new Intent(getBaseContext(),MainActivity.class));
            }
        });
    }
}
