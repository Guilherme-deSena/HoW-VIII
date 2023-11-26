package com.example.teste;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.example.teste.databinding.ActivityAudienceBinding;
import com.example.teste.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class AudienceActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAudienceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAudienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);



        ContactDAOSQLite dao = new ContactDAOSQLite(this, "howdb", null, 1);


        Button buttonSubmitAudience = (Button)findViewById(R.id.buttonSubmitAudience);
        CalendarView calendar = (CalendarView)findViewById(R.id.calendarViewAudience);
        Activity thisAudience = this;

        buttonSubmitAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView warningMessage = (TextView)findViewById(R.id.textViewWarning);
                ImageView warningImage = (ImageView)findViewById(R.id.imageWarning);
                EditText descriptionView = (EditText)findViewById(R.id.editTextDescription);
                String description = descriptionView.getText().toString();

                if (calendar.getDate() < System.currentTimeMillis()) {
                    warningMessage.setText("Por favor, selecione um dia a partir de amanhã.");
                    warningMessage.setVisibility(View.VISIBLE);
                    warningImage.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(description)) {
                    warningMessage.setText("Por favor, escreva uma descrição da audiência.");
                    warningMessage.setVisibility(View.VISIBLE);
                    warningImage.setVisibility(View.VISIBLE);
                } else if (!(TextUtils.isEmpty(dao.getAudienceDescription(calendar.getDate() / 1000)))) { //TODO: impedir agendamento duplo no mesmo dia
                    warningMessage.setText("Já há uma audiência para esta data.\nEscolha outro dia.");
                    warningMessage.setVisibility(View.VISIBLE);
                    warningImage.setVisibility(View.VISIBLE);
                } else {
                    warningMessage.setVisibility(View.INVISIBLE);
                    warningImage.setVisibility(View.INVISIBLE);
                    dao.insertScheduling(thisAudience, description, ( calendar.getDate() / 1000 ), 0);

                    Date selectedDate = new Date(calendar.getDate());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Snackbar.make(v, "Audiência agendada para "+dateFormat.format(selectedDate), Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.textViewWarning)
                            .setAction("Action", null).show();
                }
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Date selectedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                Log.i("dateunixmili", Long.toString(selectedDate.getTime()));
                calendar.setDate(selectedDate.getTime());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        if (item.getItemId() == R.id.menu_item_1) {
            myIntent = new Intent(this, ScheduleActivity.class);
            startActivity(myIntent);
            return true;
        } else if (item.getItemId() == R.id.menu_item_2) {
            myIntent = new Intent(this, AudienceActivity.class);
            startActivity(myIntent);
            return true;
        } else if (item.getItemId() == R.id.menu_item_3) {
            myIntent = new Intent(this, AccessActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);


    }

}