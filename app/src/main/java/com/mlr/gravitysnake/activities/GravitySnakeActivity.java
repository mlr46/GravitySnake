package com.mlr.gravitysnake.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mlr.gravitysnake.R;

public class GravitySnakeActivity extends AppCompatActivity {

  public static final String EXTRA_APPLES_SIZE = "apples-size";

  private int apples;
  private TextView apples_tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gravity_snake);
    init();
  }

  private void init() {
    apples_tv = findViewById(R.id.apples_size);

    Intent intent = getIntent();
    apples = intent.getIntExtra(EXTRA_APPLES_SIZE, 1);
    updateDisplay();
  }

  private void updateDisplay() {
    apples_tv.setText(String.valueOf(apples));
  }
}
