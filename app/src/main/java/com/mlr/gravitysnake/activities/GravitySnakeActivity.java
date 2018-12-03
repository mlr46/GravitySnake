package com.mlr.gravitysnake.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mlr.gravitysnake.R;
import com.mlr.gravitysnake.models.Cell;
import com.mlr.gravitysnake.models.Direction;
import com.mlr.gravitysnake.models.Point;
import com.mlr.gravitysnake.views.Grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * At all time, we must know where the snake is, and the snake should not have any white dots in
 * the middle of its body.
 *
 * There should be an apple randomly on the board where the snake is not.
 *
 * We can decide on a cell size from here - then we create a grid essentially
 * Then we draw a square that is twice the size of the cell size.
 *
 * To do:
 * 1. learn how to do the body of the snake and how to make it move
 * The body of the snake is subsequent squares.
 */
public class GravitySnakeActivity extends AppCompatActivity implements SensorEventListener {

  public static final String EXTRA_APPLES_SIZE = "apples-size";
  private static final int INITIAL_LENGTH_OF_SNAKE = 10;
  private static final int SCREEN_X_RESOLUTION = 100;
  private static final int SCREEN_Y_RESOLUTION = 200;

  private static Map<Direction, Set<Direction>> WHERE_NEXT =
    ImmutableMap.<Direction, Set<Direction>>of(
      Direction.LEFT, ImmutableSet.of(Direction.LEFT, Direction.DOWN, Direction.UP),
      Direction.RIGHT, ImmutableSet.of(Direction.RIGHT, Direction.DOWN, Direction.UP),
      Direction.UP, ImmutableSet.of(Direction.UP, Direction.LEFT, Direction.RIGHT),
      Direction.DOWN, ImmutableSet.of(Direction.DOWN, Direction.LEFT, Direction.RIGHT));

  private int gridHeight;
  private int gridWidth;
  private Random randomIntGenerator;
  private int apples;
  private TextView apples_tv;
  private Cell[][] screen;
  private Grid grid;
  private List<Point> snake;
  private Direction direction;
  private SensorManager sensorManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gravity_snake);
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    init();
  }

  @Override
  protected void onResume() {
    super.onResume();
    sensorManager.registerListener(
      this,
      sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
      SensorManager.SENSOR_DELAY_UI);
  }

  @Override
  protected void onPause() {
    super.onPause();
    sensorManager.unregisterListener(this);
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
      float xAxis = event.values[0] * 100 / SensorManager.GRAVITY_EARTH;
      float yAxis = event.values[1] * 100 / SensorManager.GRAVITY_EARTH;
      Direction nextDirection = getNextDirection(xAxis, yAxis);

      if (WHERE_NEXT.get(direction).contains(nextDirection)) {
        direction = nextDirection;
      }
      moveSnake();
    }
  }

  private Direction getNextDirection(float xAxis, float yAxis) {
    if (Math.abs(xAxis) > Math.abs(yAxis)) {
      return xAxis < 0 ? Direction.RIGHT : Direction.LEFT;
    } else {
      return yAxis < 0 ? Direction.UP : Direction.DOWN;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) { }

  /**
   * Initializes the game.
   */
  private void init() {
    randomIntGenerator = new Random();

    apples_tv = findViewById(R.id.apples_size);

    Intent intent = getIntent();
    apples = intent.getIntExtra(EXTRA_APPLES_SIZE, 1);
    grid = findViewById(R.id.grid);

    initializeScreen();
    initializeSnake();
    placeApple();
    grid.setCanDraw();
  }

  private void updateDisplay() {
    apples_tv.setText(String.valueOf(apples));
  }

  /**
   * This function will randomly place the snake in one direction and will also randomly place the
   * the first apple.
   */
  private void initializeScreen() {
    gridWidth = SCREEN_X_RESOLUTION;
    gridHeight = SCREEN_Y_RESOLUTION;

    screen = new Cell[gridWidth][gridHeight];
    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        screen[i][j] = Cell.EMPTY;
      }
    }

    grid.setResolutionX(gridWidth);
    grid.setResolutionY(gridHeight);
  }

  /**
   * Initially places the snake: we will place the snake at the middle of the screen each time.
   */
  private void initializeSnake() {
    direction = Direction.LEFT;
    snake = new ArrayList<>();
    int xInitial = SCREEN_X_RESOLUTION / 2;
    int yPosition = SCREEN_Y_RESOLUTION / 2;

    for (int i = 0; i < INITIAL_LENGTH_OF_SNAKE; i++) {
      int xPosition = xInitial + i;
      Point snakePoint = new Point(xPosition, yPosition);
      snake.add(snakePoint);
      screen[xPosition][yPosition] = Cell.SNAKE;
    }

    grid.setSnake(snake);
  }

  private void placeApple() {
    if (apples <= 0) {
      showEndOfGame();
      return;
    }

    Point apple = findEmptySpotOnScreen();
    grid.setApple(apple);
    apples--;
    updateDisplay();
  }

  private Point findEmptySpotOnScreen() {
    int positionX;
    int positionY;
    do {
      positionX = randomIntGenerator.nextInt(gridWidth);
      positionY = randomIntGenerator.nextInt(gridHeight);
    } while (screen[positionX][positionY] != Cell.EMPTY);

    return new Point(positionX, positionY);
  }

  private void moveSnake() {
    int sizeOfSnake = snake.size();
    Point nextPoint = getNextPoint(snake, direction);

    if (isGameOver(nextPoint)) {
      snake.remove(sizeOfSnake - 1);
      snake.add(0, nextPoint);

      if (isApple(nextPoint)) {

        // TODO:: do something here
        // Eat the apple - i.e. grow bigger from the bum
        // Show a new apple
      }
    } else {
      showEndOfGame();
    }

    grid.postInvalidate();
  }

  private boolean isApple(Point nextPoint) {
    return screen[nextPoint.getX()][nextPoint.getY()] == Cell.APPLE;
  }

  private Point getNextPoint(List<Point> snake, Direction direction) {
    return new Point(
      snake.get(0).getX() + direction.getDeltaX(),
      snake.get(0).getY() + direction.getDeltaY());
  }

  /**
   * At the moment, we pick a random direction for the snake to go to.
   * @return
   */
  private Direction getNextDirection() {

    Set<Direction> possibleDirections = WHERE_NEXT.get(direction);
    int index = randomIntGenerator.nextInt(possibleDirections.size());
    Iterator<Direction> iter = possibleDirections.iterator();
    for (int i = 0; i < index; i++) {
      iter.next();
    }
    return iter.next();
  }

  /**
   * The game is over if the snake goes outside of the screen or eats itself.
   * @param point
   * @return
   */
  private boolean isGameOver(Point point) {
    return point.getX() >= 0 && point.getX() < screen.length
      && point.getY() >= 0 && point.getY() < screen[0].length;
  }

  /**
   * Announces the end of the game and returns to the main activity.
   */
  private void showEndOfGame() {
    sensorManager.unregisterListener(this);
    grid.setOnTouchListener(null);

    AlertDialog alertDialog = new AlertDialog.Builder(this)
      .setMessage("Game over")
      .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      })
      .create();
    alertDialog.show();
  }
}
