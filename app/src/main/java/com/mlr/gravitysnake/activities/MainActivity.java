package com.mlr.gravitysnake.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mlr.gravitysnake.R;

import static com.mlr.gravitysnake.activities.GravitySnakeActivity.EXTRA_APPLES_SIZE;

public class MainActivity extends AppCompatActivity {

  private static final int MIN_NUMBER_OF_APPLES = 1;
  private static final int MAX_NUMBER_OF_APPLES = 20;
  private int apples;
  private SeekBar applesChoice;
  private TextView applesTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    init();
  }

  private void init() {
    applesChoice = findViewById(R.id.apples_size);
    applesTextView = findViewById(R.id.apples_tv);

    initializeSeekBar();

    // TODO:: no seekbar!
    // It can just be one activity - maybe just put some instructions here and the go-button.
    addSeekBarListener();
  }

  private void initializeSeekBar() {
    apples = MIN_NUMBER_OF_APPLES;
    applesChoice.setMax(MAX_NUMBER_OF_APPLES);
    updateDisplay();
  }

  private void addSeekBarListener() {
    applesChoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        apples = progress + MIN_NUMBER_OF_APPLES;
        updateDisplay();
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  private void updateDisplay() {
    applesTextView.setText(String.valueOf(apples));
  }

  public void startGame(View view) {
    Intent intent = new Intent(this, GravitySnakeActivity.class);
    intent.putExtra(EXTRA_APPLES_SIZE, apples);
    startActivity(intent);
  }

}
