package com.example.kyrsach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class Timer extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnSetTimer;
    private Button btnStartTimer;
    private ProgressBar progressBar;

    private CountDownTimer countDownTimer;
    private long timeInMillis = 0; // Время таймера в миллисекундах
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Связываем элементы интерфейса
        tvTimer = findViewById(R.id.tv_timer);
        btnSetTimer = findViewById(R.id.btn_set_timer);
        btnStartTimer = findViewById(R.id.btn_start_timer);
        progressBar = findViewById(R.id.progressBar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        // Установка времени таймера
        btnSetTimer.setOnClickListener(v -> showTimePicker());

        // Запуск или остановка таймера
        btnStartTimer.setOnClickListener(v -> {
            if (isTimerRunning) {
                stopTimer();
            } else {
                startTimer();
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_calendar:
                        startActivity(new Intent(Timer.this, Calendar.class));
                        return true;
                    case R.id.navigation_timer:
                        return true;
                    case R.id.navigation_videos:
                        startActivity(new Intent(Timer.this, Videos.class));
                        return true;
                    case R.id.navigation_account:
                        startActivity(new Intent(Timer.this, AccountActivity.class));
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_timer);
    }


    // Показываем диалог для выбора времени
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // Преобразуем часы и минуты в миллисекунды
            timeInMillis = (hourOfDay * 3600 + minute * 60) * 1000;
            updateTimerText();
            progressBar.setMax((int) (timeInMillis / 1000)); // Устанавливаем максимальное значение индикатора прогресса
            progressBar.setProgress(progressBar.getMax());   // Сбрасываем прогресс на максимум
        }, 0, 0, true); // Начальные значения 0 часов и 0 минут
        timePickerDialog.show();
    }

    // Обновляем текст таймера
    private void updateTimerText() {
        int hours = (int) (timeInMillis / 1000) / 3600;
        int minutes = (int) ((timeInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        tvTimer.setText(timeFormatted);
    }

    // Запускаем таймер
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeInMillis = millisUntilFinished;
                updateTimerText();
                progressBar.setProgress((int) (millisUntilFinished / 1000)); // Обновляем индикатор прогресса
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                btnStartTimer.setText("Запустить таймер");
            }
        }.start();

        isTimerRunning = true;
        btnStartTimer.setText("Остановить таймер");
    }

    // Останавливаем таймер
    private void stopTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        btnStartTimer.setText("Запустить таймер");
    }
}
