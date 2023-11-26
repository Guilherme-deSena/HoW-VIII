package com.example.teste;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.teste.ui.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.teste.databinding.ActivityAccessBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class AccessActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ContactDAOSQLite dao = new ContactDAOSQLite(this, "howdb", null, 1);


        Button buttonSubmitAccess = (Button)findViewById(R.id.buttonSubmitAccess);
        CalendarView calendar = (CalendarView)findViewById(R.id.calendarViewAccess);
        Activity thisAccess = this;

        buttonSubmitAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO: impedir acesso duplo no mesmo dia pela mesma pessoa
                TextView warningMessage = (TextView)findViewById(R.id.textViewWarning2);
                ImageView warningImage = (ImageView)findViewById(R.id.imageWarning2);
                if (calendar.getDate() < System.currentTimeMillis()) {
                    warningMessage.setText("Por favor, selecione um dia a partir de amanhã.");
                    warningMessage.setVisibility(View.VISIBLE);
                    warningImage.setVisibility(View.VISIBLE);
                } else {
                    dao.insertScheduling(thisAccess, null, ( calendar.getDate() / 1000 ), 1);
                    warningMessage.setVisibility(View.INVISIBLE);
                    warningImage.setVisibility(View.INVISIBLE);

                    Date selectedDate = new Date(calendar.getDate());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Snackbar.make(v, "Acesso agendado para "+dateFormat.format(selectedDate), Snackbar.LENGTH_LONG)
                            .setAnchorView(R.id.textViewWarning2)
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