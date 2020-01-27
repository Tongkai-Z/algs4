import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
  private Picture pic;
  private double[][] energyM;
  private boolean isTransposed;
  private boolean isHorizontalCall;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    this.pic = picture;
    int r = pic.height();
    int c = pic.width();
    energyM = new double[r][c];
    // get the energy for each pixel
    for (int i = 0;i < r;i++) {
      for (int j = 0;j < c;j++) {
        energyM[i][j] = calEnergy(j, i);
      }
    }
    isTransposed = false;
    isHorizontalCall = false;
  }

  // current picture
  public Picture picture() {
    if (isTransposed) {
      transpose();
      isTransposed = false;
    }
    return pic;
  }

  // width of current picture
  public int width() {
    return isTransposed? pic.height() : pic.width();
  }

  // height of current picture
  public int height() {
    return isTransposed? pic.width() : pic.height();
  }

  // energy of pixel at column x and row y
  private double calEnergy(int x, int y) {
    int c = energyM[0].length;
    int r = energyM.length;
    // check the border
    if (x == 0 || x == c - 1 || y == 0 || y == r - 1) {
      return 1000;
    } else {
      return getEnergy(x, y);
    }
  }

  public double energy(int x, int y) {
    check(x, y);
    return isTransposed? energyM[x][y] : energyM[y][x];
  }

  // sequence of indices for horizontal seam
  // transpose the energy matrix and call findVerticalSeam
  public int[] findHorizontalSeam() {
    if (!isTransposed) {
      transpose();
      isTransposed = true;
    }
    isHorizontalCall = true;
    int[] res = findVerticalSeam();
    isHorizontalCall = false;
    return res;
  }

  // sequence of indices for vertical seam
  // build an energy matrix and a dis matrix
  // find the spt by traverse the matrix in topological order
  public int[] findVerticalSeam() {
    if (isTransposed && !isHorizontalCall) {
      transpose();
      isTransposed = false;
    }
    int r = energyM.length;
    int c = energyM[0].length;
    double[][] dis = new double[r][c];
    // initialize dis
    for (int i = 0;i < r;i++) {
      for (int j = 0;j < c;j++) {
        dis[i][j] = i == 0? 1000 : Double.POSITIVE_INFINITY;
      }
    }
    int[][] edgeTo = new int[r][c];// track the sp, energyM[i - 1][edgeTo[i][j]] is its previous node
    // Due to the property of the DAG structure in pic
    // the topological order is just the linear traversing order
    int[][] dirs = {{1, -1}, {1, 0}, {1, 1}};
    for (int i = 0;i < r - 1;i++) {// terminate at the last row
      for (int j = 0;j < c;j++) {
        // relax the edges from current node
        for (int[] dir : dirs) {
          int neiR = i + dir[0];
          int neiC = j + dir[1];
          if (neiC >= 0 && neiC < c) {
            if (dis[neiR][neiC] > dis[i][j] + energyM[neiR][neiC]) {
              edgeTo[neiR][neiC] = j;
              dis[neiR][neiC] = dis[i][j] + energyM[neiR][neiC];
            }
          }
        }
      }
    }
    // find the min path and extract it from the edgeTo matrix
    int minC = 0;
    for (int i = 1;i < c;i++) {
      if (dis[r - 1][i] < dis[r - 1][minC]) {
        minC = i;
      }
    }
    int[] res = new int[r];
    // extract the r pixels on the sp
    int currR = r - 1;
    int currC = minC;
    while (currR >= 0) {
      res[currR] = currC;
      currC = edgeTo[currR][currC];
      currR--;
    }
    return res;
  }

  // remove horizontal seam from current picture
  // transpose the image, call removeVerticalSeam and transpose it back
  public void removeHorizontalSeam(int[] seam) {
    if (!isTransposed) {
      transpose();
      isTransposed = true;
    }
    // now the energyM  & pic is transposed
    isHorizontalCall = true;
    removeVerticalSeam(seam);
    isHorizontalCall = false;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (isTransposed && !isHorizontalCall) {
      transpose();
      isTransposed = false;
    }
    if (seam == null || seam.length != energyM.length)
    // seam[i] = j means pixel(j, i) need to be removed
    int r = energyM.length;
    int c = energyM[0].length;
    // remove the pixel and recalculate only the energy that changes
    // right shift all the pixels in the energyM and in the pic
    double[][] newEnergy = new double[r][c - 1];
    Picture newPic = new Picture(c - 1, r);
    // copy the pixels and skip the removed ones
    for (int i = 0;i < r;i++) {
      for (int j = 0;j < c - 1;j++) {
        if (j < seam[i]) {
          newEnergy[i][j] = energyM[i][j];
          newPic.set(j, i, pic.get(j, i));
        } else {
          newEnergy[i][j] = energyM[i][j + 1];
          newPic.set(j, i, pic.get(j + 1, i));
        }
      }
    }
    pic = newPic;
    energyM = newEnergy;
    // Recalculate the energy
    // calculate the pixels on the path and at its left
    for (int i = 0;i < r;i++) {
      for (int j = 0;j < c - 1;j++) {
        if (seam[i] == j) {
          // cal self
          energyM[i][j] = calEnergy(j, i);
          // cal left
          if (j > 0) {
            energyM[i][j - 1] = calEnergy(j - 1, i);
          }
        }
      }
    }
  }

  private void check(int x, int y) {
    if (x < 0 || x >= width() || y < 0 || y >= height()) {
      throw new IllegalArgumentException("index is out of bound");
    }
  }

  private double getEnergy(int x, int y) {
    int left = pic.getRGB(x - 1, y);
    int right = pic.getRGB(x + 1, y);
    int top = pic.getRGB(x, y - 1);
    int bottom = pic.getRGB(x, y + 1);
    return Math.sqrt(diff(left, right) + diff(top, bottom));
  }

  private double diff(int either, int other) {
    int rd = ((either >> 16) & 0xFF) - ((other >> 16) & 0xFF);
    int gd = ((either >> 8) & 0xFF) - ((other >> 8) & 0xFF);
    int bd = ((either >> 0) & 0xFF) - ((other >> 0) & 0xFF);
    return rd * rd + gd * gd + bd * bd;
  }

  // This method transpose the energy matrix as well as the picture
  private void transpose() {
    int r = pic.height();
    int c = pic.width();
    double[][] res = new double[c][r];
    Picture newPic = new Picture(r, c);
    for (int i = 0;i < c;i++) {
      for (int j = 0;j < r;j++) {
        res[i][j] = energyM[j][i];
        newPic.set(j, i, pic.get(i, j));
      }
    }
    energyM = res;
    pic = newPic;
  }

  //  unit testing (optional)
  public static void main(String[] args) {

  }

}
