
package com.mycompany.tetrismaven;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.mycompany.tetrismaven.Shape.Tetraminoe;

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
    private void doDrawing(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetraminoe shape = shapeAt(j, BOARD_HEIGHT - i -1);
                if(shape != Tetraminoe.NoShape) {
                    drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
                }
            }
            
        }
        if(curPiece.getShape()!= Tetraminoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),boardTop + (BOARD_HEIGHT - y - 1)*
                        squareHeight(),curPiece.getShape());
            }
        }
    }
    @Override
    public void paintComponent(Graphics g ) {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void dropDown() {
        int newY = curY;
        while(newY > 0) {
            if(! tryMove(curPiece, curX,newY-1)) {
                break;
            }
            --newY;
        }
        pieceDropped();
    }
    private void oneLineDown() {
        if(! tryMove(curPiece, curX, curY-1)) {
            pieceDropped();
        }
    }
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetraminoe.NoShape;
        }
    }
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }
    }
}
