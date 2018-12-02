package com.mlr.gravitysnake.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
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
 * We can decide on a cell size from here - then we create a grid essentially
 * Then we draw a square that is twice the size of the cell size.
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

  // Should probably only send the snake and the apple to the view.

  private static final int CELL_SIZE = 10; // half the size of the grid square size.
  private int gridHeight;
  private int gridWidth;
  private Random randomIntGenerator;
  private int apples;
  private int applesLeft;
  private TextView apples_tv;
  private Cell[][] screen; // TODO:: need to decide on the size of a square, and for the squares to
  // be superimposed
  private Grid grid;
  private List<Point> snake; // TODO: we should use a list because it keeps the order and we just want to
  // know where is the head and where is the tail of a snake

  // TODO:: add an on-touch listener to make the snake move for testing.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gravity_snake);
    init();

    grid.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        view.performClick();
        return moveSnake(view, event);
      }
    });
  }

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

  public boolean moveSnake(View view, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      int sizeOfSnake = snake.size();
      Point headOfSnake = snake.get(0);
      Point nextPoint = new Point(headOfSnake.getX() + 1, headOfSnake.getY());

      if (isOnGrid(nextPoint)) {
        snake.remove(sizeOfSnake - 1);
        snake.add(0, nextPoint);
      } else {
        showEndOfGame();
      }
    }

    grid.postInvalidate();
    return true;
  }

  private boolean isOnGrid(Point point) {
    return point.getX() >= 0 && point.getX() < screen.length
      && point.getY() >= 0 && point.getY() < screen[0].length;
  }

  /**
   * Announces the end of the game and returns to the main activity.
   */
  private void showEndOfGame() {
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
