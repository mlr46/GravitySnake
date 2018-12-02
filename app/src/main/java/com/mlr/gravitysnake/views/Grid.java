package com.mlr.gravitysnake.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.mlr.gravitysnake.models.Point;

import java.util.List;

public class Grid extends View {

  private static final int SQUARE_SIZE = 20;
  private List<Point> snake;
  private Point apple;
  private Paint snakeHeadPaint;
  private Paint snakeBodyPaint;
  private Paint applePaint;
  private Paint gridPaint;
  private boolean canDraw;
  private int xCellSize;
  private int yCellSize;
  private int pointCellSize;
  private int xRes;
  private int yRes;

  public Grid(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  /**
   * Creates the brushes to draw the snake and the apple
   */
  private void init() {

    snakeHeadPaint = new Paint();
    snakeHeadPaint.setColor(Color.BLUE);

    snakeBodyPaint = new Paint();
    snakeBodyPaint.setColor(Color.GREEN);

    applePaint = new Paint();
    applePaint.setColor(Color.RED);

    gridPaint = new Paint();
    gridPaint.setColor(Color.BLACK);
  }

  public void setSnake(List<Point> snake) {
    this.snake = snake;
  }

  public void setApple(Point apple) {
    this.apple = apple;
  }

  public void setResolutionX(int xRes) {
    this.xRes = xRes;
  }

  public void setResolutionY(int yRes) {
    this.yRes = yRes;
  }

  public void setCanDraw() {
    this.canDraw = true;
  }

  /**
   * Drawing the grid each time it is updated.
   *
   * @param canvas the canvas on which the background will be drawn
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (canDraw) {
      this.xCellSize = getWidth() / xRes;
      this.yCellSize = getHeight() / yRes;
      this.pointCellSize = Math.max(xCellSize, yCellSize) * 2;
      drawSnake(canvas);
      drawApple(canvas);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  public boolean performClick() {
    return super.performClick();
  }

  private void drawSnake(Canvas canvas) {
    for (Point point : snake.subList(1, snake.size())) {
      drawSnakeBodySquare(point, canvas);
    }
    drawSnakeHeadSquare(snake.get(0), canvas);
  }

  private void drawApple(Canvas canvas) {
    drawAppleSquare(apple, canvas);
  }

  private void drawSnakeHeadSquare(Point point, Canvas canvas) {
    canvas.drawRect(getRectangleToBeDrawn(point), snakeHeadPaint);
  }

  private void drawSnakeBodySquare(Point point, Canvas canvas) {
    canvas.drawRect(getRectangleToBeDrawn(point), snakeBodyPaint);
  }


  private void drawAppleSquare(Point point, Canvas canvas) {
    canvas.drawRect(getRectangleToBeDrawn(point), applePaint);
  }

  /**
   * TODO:: figure out how to have a proper square here by doing the math and get the biggest of both
   * the xCell and the yCell to get a square.
   * @param point
   * @return
   */
  private Rect getRectangleToBeDrawn(Point point) {

    return new Rect(
      point.getX() * xCellSize,
      point.getY() * yCellSize,
      point.getX() * xCellSize + pointCellSize,
      point.getY() * yCellSize + pointCellSize);
  }
}
