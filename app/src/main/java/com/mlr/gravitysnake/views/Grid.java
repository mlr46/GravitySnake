package com.mlr.gravitysnake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.mlr.gravitysnake.models.Cell;
import com.mlr.gravitysnake.models.Point;

public class Grid extends View {

  private static final int SQUARE_SIZE = 5;
  private Cell[][] screen;
  private Paint snakePaint;
  private Paint applePaint;

  public Grid(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * Creates the brushes to draw the snake and the apple
   */
  private void init() {

    snakePaint = new Paint();
    snakePaint.setColor(Color.GREEN);

    applePaint = new Paint();
    applePaint.setColor(Color.RED);
  }

  public void setScreen(Cell[][] screen) {
    this.screen = screen;
  }

  /**
   * Drawing the grid each time it is updated.
   *
   * @param canvas the canvas on which the background will be drawn
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    drawScreen(canvas);
  }

  private void drawScreen(Canvas canvas) {
    for (int i = 0; i < screen.length; i++) {
      for (int j = 0; j < screen[0].length; j++) {
        switch (screen[i][j]) {
          case APPLE:
            drawApple(new Point(i, j), canvas);
            break;
          case SNAKE:
            drawSnake(new Point(i, j), canvas);
            break;
          case EMPTY: default:
            break;
        }
      }
    }
  }

  private void drawApple(Point point, Canvas canvas) {
    canvas.drawRect(getRectangleToBeDrawn(point), applePaint);
  }

  private void drawSnake(Point point, Canvas canvas) {
    canvas.drawRect(getRectangleToBeDrawn(point), snakePaint);
  }

  private Rect getRectangleToBeDrawn(Point point) {
    return new Rect(
      point.getX(),
      point.getY(),
      point.getX() + SQUARE_SIZE,
      point.getY() + SQUARE_SIZE);
  }
}
