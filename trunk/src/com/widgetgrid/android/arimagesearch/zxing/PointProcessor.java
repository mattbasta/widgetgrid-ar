package com.widgetgrid.android.arimagesearch.zxing;

public class PointProcessor {

  private final BitMatrix image;

  public PointProcessor(BitMatrix image) {
    this.image = image;
  }

  protected BitMatrix getImage() {
    return image;
  }
  /**
   * <p>Detects a QR Code in an image, simply.</p>
   *
   * @return {@link DetectorResult} encapsulating results of detecting a QR Code
   * @throws ReaderException if no QR Code can be found
   */
  public PointInfoObject detect() {

    PointPatternFinder finder = new PointPatternFinder(image);
    PointInfoObject info = finder.find();

    return info;
  }

  /**
   * <p>Computes an average estimated module size based on estimated derived from the positions
   * of the three finder patterns.</p>
   */
  protected float calculateModuleSize(PointPattern topLeft,
                                      PointPattern topRight,
                                      PointPattern bottomLeft) {
    // Take the average
    return (calculateModuleSizeOneWay(topLeft, topRight) +
        calculateModuleSizeOneWay(topLeft, bottomLeft)) / 2.0f;
  }

  /**
   * <p>Estimates module size based on two finder patterns -- it uses
   * {@link #sizeOfBlackWhiteBlackRunBothWays(int, int, int, int)} to figure the
   * width of each, measuring along the axis between their centers.</p>
   */
  private float calculateModuleSizeOneWay(PointPattern pattern, PointPattern otherPattern) {
    float moduleSizeEst1 = sizeOfBlackWhiteBlackRunBothWays((int) pattern.getX(),
        (int) pattern.getY(),
        (int) otherPattern.getX(),
        (int) otherPattern.getY());
    float moduleSizeEst2 = sizeOfBlackWhiteBlackRunBothWays((int) otherPattern.getX(),
        (int) otherPattern.getY(),
        (int) pattern.getX(),
        (int) pattern.getY());
    if (Float.isNaN(moduleSizeEst1)) {
      return moduleSizeEst2 / 7.0f;
    }
    if (Float.isNaN(moduleSizeEst2)) {
      return moduleSizeEst1 / 7.0f;
    }
    // Average them, and divide by 7 since we've counted the width of 3 black modules,
    // and 1 white and 1 black module on either side. Ergo, divide sum by 14.
    return (moduleSizeEst1 + moduleSizeEst2) / 14.0f;
  }

  /**
   * See {@link #sizeOfBlackWhiteBlackRun(int, int, int, int)}; computes the total width of
   * a finder pattern by looking for a black-white-black run from the center in the direction
   * of another point (another finder pattern center), and in the opposite direction too.</p>
   */
  private float sizeOfBlackWhiteBlackRunBothWays(int fromX, int fromY, int toX, int toY) {

   float result = sizeOfBlackWhiteBlackRun(fromX, fromY, toX, toY);

   // Now count other way -- don't run off image though of course
   float scale = 1.0f;
   int otherToX = fromX - (toX - fromX);
   if (otherToX < 0) {
     scale = (float) fromX / (float) (fromX - otherToX);
     otherToX = 0;
   } else if (otherToX >= image.getWidth()) {
     scale = (float) (image.getWidth() - 1 - fromX) / (float) (otherToX - fromX);
     otherToX = image.getWidth() - 1;
   }
   int otherToY = (int) (fromY - (toY - fromY) * scale);

   scale = 1.0f;
   if (otherToY < 0) {
     scale = (float) fromY / (float) (fromY - otherToY);
     otherToY = 0;
   } else if (otherToY >= image.getHeight()) {
     scale = (float) (image.getHeight() - 1 - fromY) / (float) (otherToY - fromY);
     otherToY = image.getHeight() - 1;
   }
   otherToX = (int) (fromX + (otherToX - fromX) * scale);

   result += sizeOfBlackWhiteBlackRun(fromX, fromY, otherToX, otherToY);
   return result - 1.0f; // -1 because we counted the middle pixel twice
 }

  /**
   * <p>This method traces a line from a point in the image, in the direction towards another point.
   * It begins in a black region, and keeps going until it finds white, then black, then white again.
   * It reports the distance from the start to this point.</p>
   *
   * <p>This is used when figuring out how wide a finder pattern is, when the finder pattern
   * may be skewed or rotated.</p>
   */
  private float sizeOfBlackWhiteBlackRun(int fromX, int fromY, int toX, int toY) {
    // Mild variant of Bresenham's algorithm;
    // see http://en.wikipedia.org/wiki/Bresenham's_line_algorithm
    boolean steep = Math.abs(toY - fromY) > Math.abs(toX - fromX);
    if (steep) {
      int temp = fromX;
      fromX = fromY;
      fromY = temp;
      temp = toX;
      toX = toY;
      toY = temp;
    }

    int dx = Math.abs(toX - fromX);
    int dy = Math.abs(toY - fromY);
    int error = -dx >> 1;
    int ystep = fromY < toY ? 1 : -1;
    int xstep = fromX < toX ? 1 : -1;
    int state = 0; // In black pixels, looking for white, first or second time
    for (int x = fromX, y = fromY; x != toX; x += xstep) {

      int realX = steep ? y : x;
      int realY = steep ? x : y;
      if (state == 1) { // In white pixels, looking for black
        if (image.get(realX, realY)) {
          state++;
        }
      } else {
        if (!image.get(realX, realY)) {
          state++;
        }
      }

      if (state == 3) { // Found black, white, black, and stumbled back onto white; done
        int diffX = x - fromX;
        int diffY = y - fromY;
        return (float) Math.sqrt((double) (diffX * diffX + diffY * diffY));
      }
      error += dy;
      if (error > 0) {
        if (y == toY) {
          break;
        }
        y += ystep;
        error -= dx;
      }
    }
    int diffX = toX - fromX;
    int diffY = toY - fromY;
    return (float) Math.sqrt((double) (diffX * diffX + diffY * diffY));
  }
	
}
