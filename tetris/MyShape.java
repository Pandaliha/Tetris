package tetris;

import java.util.Random;
import java.lang.Math;


public class MyShape {

    enum TetrisShapes { NoShape, ZShape, SShape, LineShape, 
               TShape, SquareShape, LShape, MirroredLShape };

    private TetrisShapes pieceShape;
    private int coords[][];
    private int[][][] coordsTable;


    public MyShape() {

        coords = new int[4][2];
        setShape(TetrisShapes.NoShape);

    }

    public void setShape(TetrisShapes shape) {
    	
    	/**
    	 * holds the coordinates of a shapes
    	 */
         coordsTable = new int[][][] {
            { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },	// no shape
            { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },	// S shape
            { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },	// Z shape
            { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },	// Line shape
            { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },	// T shape
            { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },	// Square shape
            { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },	// Mirrored L shape
            { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }	// L shape
        };

        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j]; //ordinal returns current position from the TetrisShapes enum
            }
        }
        pieceShape = shape;

    }

    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public TetrisShapes getShape()  { return pieceShape; }

    public void setRandomShape()
    {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        TetrisShapes[] values = TetrisShapes.values(); 
        setShape(values[x]);
    }

    public int minX()
    {
      int m = coords[0][0];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coords[i][0]);
      }
      return m;
    }


    public int minY() 
    {
      int m = coords[0][1];
      for (int i=0; i < 4; i++) {
          m = Math.min(m, coords[i][1]);
      }
      return m;
    }

    public MyShape rotateLeft() 
    {
        if (pieceShape == TetrisShapes.SquareShape)
            return this;

        MyShape result = new MyShape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public MyShape rotateRight()
    {
        if (pieceShape == TetrisShapes.SquareShape)
            return this;

        MyShape result = new MyShape();
        result.pieceShape = pieceShape;

        for (int i = 3; i > -1; --i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}