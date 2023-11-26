package com.example.teste;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.teste.databinding.ActivityScheduleBinding;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityScheduleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);



        ContactDAOSQLite dao = new ContactDAOSQLite(this, "howdb", null, 1);

        Button buttonGoToAudience = (Button)findViewById(R.id.buttonGoToAudience);
        Button buttonGoToAccess = (Button)findViewById(R.id.buttonGoToAccess);
        CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);

        buttonGoToAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToAudience = new Intent(getBaseContext(), AudienceActivity.class);
                startActivity(intentGoToAudience);
            }
        });

        buttonGoToAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoToAccess = new Intent(getBaseContext(), AccessActivity.class);
                startActivity(intentGoToAccess);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Date selectedDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                Log.i("dateunixmili", Long.toString(selectedDate.getTime()));
                calendar.setDate(selectedDate.getTime());

                String audienceDescription = dao.getAudienceDescription(calendar.getDate() / 1000);
                TextView descriptionAudience = (TextView) findViewById(R.id.textViewAudienceThisDay);

                if (!TextUtils.isEmpty(audienceDescription)) {
                    descriptionAudience.setText("Audiência agendada:\n"+audienceDescription);
                    descriptionAudience.setVisibility(1);
                } else {
                    descriptionAudience.setText("Nenhuma audiência agendada.");
                }

                int accessCount = dao.getDayAccesses(calendar.getDate() / 1000);
                TextView descriptionAccess = (TextView) findViewById(R.id.textViewAccessCitizens);
                descriptionAccess.setText("Cidadãos na câmara neste dia: " + Integer.toString(accessCount) + ".");

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