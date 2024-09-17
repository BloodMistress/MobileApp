package com.example.kyrsach;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;


public class Calendar extends AppCompatActivity {

private CalendarView calendarView;
private EditText caloriesInput;
private Spinner workoutTypeSpinner;
private EditText workoutTimeInput;
private Button saveButton;
private DatabaseReference databaseReference;
private String selectedDate;
private FirebaseAuth auth;
private TextView caloriesTextView, workoutTypeTextView, workoutTimeTextView;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale("ru");
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);
        caloriesInput = findViewById(R.id.caloriesInput);
        workoutTypeSpinner = findViewById(R.id.workoutTypeSpinner);
        workoutTimeInput = findViewById(R.id.workoutTimeInput);
        saveButton = findViewById(R.id.saveButton);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        workoutTypeTextView = findViewById(R.id.workoutTypeTextView);
        workoutTimeTextView = findViewById(R.id.workoutTimeTextView);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("CalendarNotes");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.workout_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(adapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
@Override
public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        loadNote();
        }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        saveNote();
        hideKeyboard();
        }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
                case R.id.navigation_calendar:
                        return true;
                case R.id.navigation_timer:
                        startActivity(new Intent(Calendar.this, Timer.class));
                        return true;
                case R.id.navigation_videos:
                        startActivity(new Intent(Calendar.this, Videos.class));
                        return true;
                case R.id.navigation_account:
                        startActivity(new Intent(Calendar.this, AccountActivity.class));
                        return true;
                }
                return false;
        }

        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_calendar);
}
private void saveNote() {
        String calories = caloriesInput.getText().toString();
        String workoutType = workoutTypeSpinner.getSelectedItem().toString();
        String workoutTime = workoutTimeInput.getText().toString();

        if (calories.isEmpty() || workoutTime.isEmpty() || selectedDate == null) {
        Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
        String userId = user.getUid();
        Note note = new Note(selectedDate, calories, workoutType, workoutTime);
        databaseReference.child(userId).child(selectedDate).setValue(note);
        Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
        }
        }
private void loadNote() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
        String userId = user.getUid();
        databaseReference.child(userId).child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
        Note note = snapshot.getValue(Note.class);
        if (note != null) {
        caloriesTextView.setText("Калории: " + note.getCalories());
        workoutTypeTextView.setText("Тип тренировки: " + note.getWorkoutType());
        workoutTimeTextView.setText("Время тренировки: " + note.getWorkoutTime() + " мин");
        }
        } else {
        clearTextViews();
        }
        }

@Override
public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(Calendar.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
        }
        });
        }
        }
private void clearTextViews() {
        caloriesTextView.setText("Калории: ");
        workoutTypeTextView.setText("Тип тренировки: ");
        workoutTimeTextView.setText("Время тренировки: ");
        }
private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        }
private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        }










