
package com.mycompany.tetrismaven;

import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tetris.Shape.Tetraminoe;

import java.util.TimerTask;

public class Board extends JPanel {
    
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int INITIAL_DELAY = 100;
    private final int PERIOD_INTERVAL = 300;
    
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;
    private Tetraminoe[] board;
    
    public Board (Tetris parent) {
        initBoard(parent);
    }
    private void initBoard(Tetris parent) {
        setFocusable(true);
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), INITIAL_DELAY, PERIOD_INTERVAL);
        
        curPiece = new Shape();
        
        statusbar = parent.getStatusBar();
        board = new Tetraminoe[BOARD_WIDTH * BOARD_HEIGHT];
        addKeyListener(new TAdapter());
        clearBoard();
    }
    private int squareWidth() {
        return (int) getSize().getWidth()/BOARD_WIDTH;
    }
    private int squareHeight() {
        return (int) getSize().getHeight()/BOARD_HEIGHT;
    }
    private Tetraminoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }
    public void start() {
        isStarted = true;
        clearBoard();
        newPiece();
    }
    private void pause() {
        
        if(! isStarted) {
            return;
        }
        isPaused = !isPaused;
        
        if(isPaused) {
            statusbar.setText("paused");
        }else {
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
    }
    
}
