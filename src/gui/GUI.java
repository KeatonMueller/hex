package src.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import src.agents.Agent;
import src.Board;
import src.game.GameCheck;

class Canvas extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int PADDING = 64;

    private Board board;
    private Agent opponent;
    private int dim, frameWidth, frameHeight;
    private int winner = 0;
    private int cellSize; // width of each hexagon
    private int sideLength; // side length of each hexagon
    private int innerHeight; // height of upper triangle in the hexagon

    public Canvas(int fw, int fh, Board b, int d, Agent o) {
        super();
        frameWidth = fw;
        frameHeight = fh;
        board = b;
        dim = d;
        opponent = o;

        calculateLengths();
        // listen for clicks now that board is loaded
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (winner != 0)
                    return;

                int x = e.getX();
                int y = e.getY();

                int i = (y - PADDING) / (innerHeight + sideLength);
                int j = ((x - PADDING) - (i * cellSize / 2)) / cellSize;

                if (i < 0 || i >= dim || j < 0 || j >= dim)
                    return;

                int idx = i * dim + j;
                if (!board.isValid(idx))
                    return;

                board.move(idx);
                repaint();

                winner = GameCheck.checkWin(board, dim);
                if (winner != 0) {
                    JOptionPane.showMessageDialog(null, "Player " + winner + " won!", null,
                            JOptionPane.PLAIN_MESSAGE);
                    return;
                }

                nextMove();
            }
        });
    }

    /**
     * Get next move from agent, or do nothing if human to move
     */
    private void nextMove() {
        // if human opponent or player 1's turn, next move is a click
        if (opponent == null || board.getTurn() == 0)
            return;
        board.move(opponent.getMove(board, dim));
    }

    /**
     * Handle window resize
     * 
     * @param width  New window width
     * @param height New window height
     */
    public void newSize(int width, int height) {
        frameWidth = width;
        frameHeight = height;
        calculateLengths();
    }

    /**
     * Calculate pixel lengths based on board and window size
     */
    private void calculateLengths() {
        int numWide = (int) Math.ceil(dim + ((double) (dim - 1) / 2));
        int numHigh = (int) Math.ceil(Math.sqrt(3) / 2 * dim);

        cellSize = (int) (Math.min((frameWidth - (2 * PADDING)) / numWide,
                (frameHeight - (2 * PADDING)) / numHigh));

        sideLength = (int) (cellSize / Math.sqrt(3));
        innerHeight = (int) (cellSize * Math.sqrt(3) / 6);
        repaint();
    }

    /**
     * Draw the board
     * 
     * @param g Graphics object
     */
    private void draw(Graphics g) {
        if (board == null)
            return;

        Graphics2D g2d = (Graphics2D) g;

        // draw hexagon tiles
        int idx, i, j, x, y;
        Hexagon h;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        for (i = 0; i < dim; i++) {
            for (j = 0; j < dim; j++) {
                x = PADDING + (j * cellSize) + (i * cellSize / 2);
                y = PADDING + (i * (innerHeight + sideLength));
                h = new Hexagon(x, y, cellSize, sideLength, innerHeight);
                idx = i * dim + j;
                if (board.get(idx) != 0) {
                    g2d.setColor(board.get(idx) == 1 ? Color.RED : Color.BLUE);
                    g2d.fillPolygon(h);
                    g2d.setColor(Color.BLACK);
                }
                g2d.drawPolygon(h);
            }
        }

        int numPoints = 2 * dim;
        int[] xCoords = new int[numPoints + 1];
        int[] yCoords = new int[numPoints + 1];
        g2d.setStroke(new BasicStroke(5));

        // draw left blue line
        for (i = 0; i < numPoints; i++) {
            xCoords[i] = PADDING + ((i / 2) * cellSize / 2);
            yCoords[i] = PADDING + innerHeight + ((i / 2) * (innerHeight + sideLength));
            if (i % 2 == 1) {
                yCoords[i] += sideLength;
            }
        }
        // make minor adjustments so ends are hidden
        yCoords[0] += 2;
        yCoords[numPoints - 1] -= 2;
        g2d.setColor(Color.BLUE);
        g2d.drawPolyline(xCoords, yCoords, numPoints);

        // draw right blue line
        for (i = 0; i < numPoints; i++) {
            xCoords[i] = PADDING + (dim * cellSize) + ((i / 2) * cellSize / 2);
        }
        g2d.drawPolyline(xCoords, yCoords, numPoints);

        // draw top red line
        numPoints++;
        for (i = 0; i < numPoints; i++) {
            xCoords[i] = (int) (PADDING + (i * cellSize / 2));
            yCoords[i] = PADDING;
            if (i % 2 == 0) {
                yCoords[i] += innerHeight;
            }
        }
        g2d.setColor(Color.RED);
        g2d.drawPolyline(xCoords, yCoords, numPoints);

        // draw bottom red line
        for (i = 0; i < numPoints; i++) {
            xCoords[i] = (int) (PADDING + ((dim + i - 1) * cellSize / 2));
            yCoords[i] = (int) (PADDING + (dim * (innerHeight + sideLength)));
            if (i % 2 == 1) {
                yCoords[i] += innerHeight;
            }
        }
        g2d.drawPolyline(xCoords, yCoords, numPoints);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}


public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int FRAME_X = 400, FRAME_Y = 100;
    private static final int FRAME_WIDTH = 1000, FRAME_HEIGHT = 700;
    private static final String FRAME_TITLE = "Hex";

    private Canvas canvas;
    private Board board;
    private Agent opponent;
    private int dim;

    public GUI() {
        super();
        getDim();
        getOpponent();
        board = new Board(dim);
        canvas = new Canvas(FRAME_WIDTH, FRAME_HEIGHT, board, dim, opponent);
        add(canvas);
        setTitle(FRAME_TITLE);
        setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // listen for frame resizes
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                canvas.newSize(getWidth(), getHeight());
            }
        });
    }

    /**
     * Display dialog for user to select an opponent
     */
    private void getOpponent() {
        OpponentSelect selector = new OpponentSelect();
        JOptionPane.showMessageDialog(null, selector, "Choose your opponent",
                JOptionPane.PLAIN_MESSAGE);
        opponent = selector.getOpponent();
    }

    /**
     * Display input dialog for user to enter dimension
     */
    private void getDim() {
        String dimString = JOptionPane.showInputDialog(null, "Enter board dimension (default=11):",
                "Setup", JOptionPane.PLAIN_MESSAGE);

        if (dimString.matches("\\d+")) {
            dim = Math.max(1, Integer.parseInt(dimString));
        } else {
            dim = 11;
        }
    }
}
