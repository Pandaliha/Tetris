package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.MyShape.TetrisShapes;


@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {


    final int BoardWidth = 18;
    final int BoardHeight = 22;

    Timer timer;
    boolean isFallFinished = false;
    boolean isStarted = false;
    boolean isPaused = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    JLabel statusbar;
    MyShape curPiece;
    TetrisShapes[] board;



    public Board(Tetris parent) {

       setFocusable(true); //keyboard input
       curPiece = new MyShape();
       timer = new Timer(350, this);
       timer.start(); 

       statusbar =  parent.getStatusBar();
       board = new TetrisShapes[BoardWidth * BoardHeight];
       addKeyListener(new TetrisAdapter());
       clearBoard();  
    }

    public void actionPerformed(ActionEvent e) {
        if (isFallFinished) {
            isFallFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }


    int squareWidth() { 
    	return (int) getSize().getWidth() / BoardWidth; 
    	}
    int squareHeight() { 
    	return (int) getSize().getHeight() / BoardHeight; 
    	}
    TetrisShapes shapeAt(int x, int y) { 
    	return board[(y * BoardWidth) + x]; 
    	}


    public void start()
    {
        if (isPaused)
            return;

        isStarted = true;
        isFallFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }

    private void pause()
    {
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusbar.setText("Paused...");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }

    public void paint(Graphics gra)
    { 
        super.paint(gra);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

     // already fallen shapes piling on bottom of the board
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                TetrisShapes shape = shapeAt(j, BoardHeight - i - 1); 
                if (shape != TetrisShapes.NoShape)
                    drawSquare(gra, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            }
        }

        if (curPiece.getShape() != TetrisShapes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(gra, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           curPiece.getShape());
            }
        }
    }

    private void dropDown()
    {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }

    private void oneLineDown()
    {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }


    private void clearBoard()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = TetrisShapes.NoShape;
    }

    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BoardWidth) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallFinished)
            newPiece();
    }

    private void newPiece()
    {
        curPiece.setRandomShape();
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(TetrisShapes.NoShape);
            timer.stop();
            isStarted = false;
            statusbar.setText("GAMEOVER! Points: " + numLinesRemoved);
        }
    }

    private boolean tryMove(MyShape newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != TetrisShapes.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == TetrisShapes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText("Points: " + String.valueOf(numLinesRemoved));
            isFallFinished = true;
            curPiece.setShape(TetrisShapes.NoShape);
            repaint();
        }
     }

    private void drawSquare(Graphics gra, int x, int y, TetrisShapes shape)
    {
        Color colors[] = { new Color(255, 0, 0), new Color(255, 38, 0), 
            new Color(255, 76, 0), new Color(255, 115, 0), 
            new Color(255, 140, 0), new Color(255, 178, 0), 
            new Color(255, 217, 0), new Color(255, 255, 0)
        };


        Color color = colors[shape.ordinal()];

        gra.setColor(color);
        gra.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        gra.setColor(color.brighter());
        gra.drawLine(x, y + squareHeight() - 1, x, y);
        gra.drawLine(x, y, x + squareWidth() - 1, y);

        gra.setColor(color.darker());
        gra.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        gra.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);
    }

    class TetrisAdapter extends KeyAdapter {
         public void keyPressed(KeyEvent e) {

             if (!isStarted || curPiece.getShape() == TetrisShapes.NoShape) {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }

             if (isPaused)
                 return;
             if (keycode == 'r' || keycode == 'R') {
            	 numLinesRemoved = 0;
            	 statusbar.setText("Points: " + String.valueOf(numLinesRemoved));
                 start();
             }
             
             switch (keycode) {
             case KeyEvent.VK_LEFT:
                 tryMove(curPiece, curX - 1, curY);
                 break;
             case KeyEvent.VK_RIGHT:
                 tryMove(curPiece, curX + 1, curY);
                 break;
             case KeyEvent.VK_DOWN:
                 tryMove(curPiece.rotateRight(), curX, curY);
                 break;
             case KeyEvent.VK_UP:
                 tryMove(curPiece.rotateLeft(), curX, curY);
                 break;
             case KeyEvent.VK_SPACE:
                 dropDown();
                 break;
             case 'd':
                 oneLineDown();
                 break;
             case 'D':
                 oneLineDown();
                 break;
             }

         }
     }
}