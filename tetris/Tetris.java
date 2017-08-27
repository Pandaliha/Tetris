package tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;


@SuppressWarnings("serial")
public class Tetris extends JFrame {

    JLabel statusbar;
    JLabel howto;


    public Tetris() {

        statusbar = new JLabel("Points: 0");
        howto = new JLabel("Pause : p - Move Left: ← - Move Right: → - Turn Left: ↑ - Turn Right: ↓ - Drop Faster: d - To Bottom: Space - Reset: r");
  

        
        add(statusbar, BorderLayout.SOUTH);
        add(howto, BorderLayout.NORTH);
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(600, 700);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getStatusBar() {
       return statusbar;
   }

    public static void main(String[] args) {

        Tetris game = new Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    } 
}