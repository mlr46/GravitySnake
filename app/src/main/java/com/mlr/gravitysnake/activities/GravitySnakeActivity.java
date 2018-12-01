package com.mlr.gravitysnake.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.mlr.gravitysnake.R;
import com.mlr.gravitysnake.models.Cell;
import com.mlr.gravitysnake.models.Point;
import com.mlr.gravitysnake.views.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * At all time, we must know where the snake is, and the snake should not have any white dots in
 * the middle of its body.
 *
 * There should be an apple randomly on the board where the snake is not.
 *
 * To do:
 * 1. learn how to do the body of the snake and how to make it move
 * The body of the snake is subsequent squares.
 */
public class GravitySnakeActivity extends AppCompatActivity {

  public static final String EXTRA_APPLES_SIZE = "apples-size";
  private static final int INITIAL_LENGTH_OF_SNAKE = 10;
  private static final int SCREEN_X_RESOLUTION = 100;
  private static final int SCREEN_Y_RESOLUTION = 200;

  private int gridHeight;
  private int gridWidth;
  private Random randomIntGenerator;
  private int apples;
  private int applesLeft;
  private TextView apples_tv;
  private Cell[][] screen; // TODO:: need to decide on the size of a square, and for the squares to
  // be superimposed
  private List<Point> snake; // TODO: we should use a list because it keeps the order and we just want to
  // know where is the head and where is the tail of a snake

  // TODO:: add an on-touch listener to make the snake move for testing.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gravity_snake);
    init();
  }

  private void init() {
    randomIntGenerator = new Random();

    apples_tv = findViewById(R.id.apples_size);

    Intent intent = getIntent();
    apples = intent.getIntExtra(EXTRA_APPLES_SIZE, 1);
    initializeScreenSize();
    initializeScreen();
    initializeSnake();
    placeApple();
  }

  private void updateDisplay() {
    apples_tv.setText(String.valueOf(apples));
  }

  private void initializeScreenSize() {
    Grid grid = findViewById(R.id.grid);
    gridHeight = grid.getMeasuredHeight();
    gridHeight = grid.getMeasuredWidth();
  }

  /**
   * This function will randomly place the snake in one direction and will also randomly place the
   * the first apple.
   */
  private void initializeScreen() {
    screen = new Cell[gridWidth][gridHeight];
    for (int i = 0; i < gridWidth; i++) {
      for (int j = 0; j < gridHeight; j++) {
        screen[i][j] = Cell.EMPTY;
      }
    }
  }

  /**
   * Initially places the snake: we will place the snake at the middle of the screen each time.
   */
  private void initializeSnake() {
    snake = new ArrayList<>();
    int xInitial = gridWidth / 2;
    int yPosition = gridHeight / 2;

    for (int i = 0; i < INITIAL_LENGTH_OF_SNAKE; i++) {
      int xPosition = xInitial + i;
      Point snakePoint = new Point(xPosition, yPosition);
      snake.add(snakePoint);
      screen[xPosition][yPosition] = Cell.SNAKE;
    }
  }

  private void placeApple() {
    if (apples <= 0) {
      showEndOfGame();
      return;
    }

    Point apple = findEmptySpotOnScreen();
    screen[apple.getX()][apple.getY()] = Cell.APPLE;
    apples--;
    updateDisplay();
  }

  private Point findEmptySpotOnScreen() {
    int positionX;
    int positionY;
    do {
      positionX = randomIntGenerator.nextInt(gridHeight);
      positionY = randomIntGenerator.nextInt(gridWidth);
    } while (screen[positionX][positionY] != Cell.EMPTY);

    return new Point(positionX, positionY);
  }

  private void showEndOfGame() {}
}
