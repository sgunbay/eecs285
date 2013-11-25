package com.eecs285.siegegame;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainGameFrame extends JFrame{

  private static final long serialVersionUID = 1L;
  JPanel mapPanel;
  JPanel auxPanel;
  JScrollPane narrationPanel;
  JTextArea narrationTextArea;
  JPanel HUDPanel;
  JPanel currentPositionPanel;
  JLabel currentPositionLabel;
  JPanel [][] gridSquares;
  Tile[][] mapTiles;
  
  GridSquareMouseListener gridSquareMouseListener;
  Integer numGridSquares;
  final Integer ROWS;
  final Integer COLS;
  final Integer SQUARESIZE;
  final Integer SCREEN_HEIGHT = 690;
  final Integer SCREEN_WIDTH = 1190;
  final Integer AUX_SIZE = 200;
  final Integer NARRATION_FONT_SIZE = 11;
  
  public MainGameFrame(String inTitle, Integer numRows, Integer numCols, Tile[][] tiles){
    super(inTitle);
    ROWS = numRows;
    COLS = numCols;
    SQUARESIZE = Math.min(SCREEN_HEIGHT / ROWS, SCREEN_WIDTH / COLS);
    setLayout(new BorderLayout());
    mapPanel = new JPanel();
    mapPanel.setLayout(new GridLayout(ROWS, COLS));
    numGridSquares = numRows * numCols;
    mapTiles = tiles;
    
    gridSquareMouseListener = new GridSquareMouseListener();
    
    gridSquares = new JPanel[ROWS][COLS];
    
    for (int i = 0; i < ROWS; i++){
      for (int j = 0; j < COLS; j++){
        gridSquares[i][j] = new JPanel();
        gridSquares[i][j].setBackground(mapTiles[i][j].getColor());
        gridSquares[i][j].addMouseListener(gridSquareMouseListener);
        gridSquares[i][j].setPreferredSize(new Dimension(SQUARESIZE, SQUARESIZE));
        mapPanel.add(gridSquares[i][j]);
      }
    }
    mapPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    add(mapPanel, BorderLayout.CENTER);
    
    auxPanel = new JPanel(new BorderLayout());
    auxPanel.setPreferredSize(new Dimension(AUX_SIZE, ROWS * SQUARESIZE));
    auxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
    narrationTextArea = new JTextArea("Welcome to Siege!\n");
    narrationTextArea.setFont(new Font(narrationTextArea.getName(), Font.PLAIN, NARRATION_FONT_SIZE));
    narrationTextArea.setEditable(false);
    narrationTextArea.setWrapStyleWord(true);
    narrationTextArea.setLineWrap(true);
    narrationTextArea.setOpaque(false);
    
    narrationPanel = new JScrollPane();
    narrationPanel.setBorder(BorderFactory.createCompoundBorder
        (BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(0, 3, 0, 0)));
    narrationPanel.add(narrationTextArea);
    narrationPanel.setViewportView(narrationTextArea);
    narrationPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    narrationPanel.getVerticalScrollBar().setPreferredSize(new Dimension(15, 0));
    
    HUDPanel = new JPanel(new BorderLayout());
    HUDPanel.setPreferredSize(new Dimension(AUX_SIZE, AUX_SIZE));
    HUDPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    HUDPanel.add(new JLabel("HUD:", JLabel.CENTER), BorderLayout.NORTH);
    
    currentPositionLabel = new JLabel();
    currentPositionLabel.setFont(new Font(currentPositionLabel.getName(), Font.PLAIN, 9));
    currentPositionLabel.setOpaque(false);
    currentPositionPanel = new JPanel();
    currentPositionPanel.add(currentPositionLabel);
    HUDPanel.add(currentPositionPanel, BorderLayout.SOUTH);
    
    auxPanel.add(narrationPanel, BorderLayout.CENTER);
    auxPanel.add(HUDPanel, BorderLayout.SOUTH);
    
    add(auxPanel, BorderLayout.WEST);
    
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  

  public class GridSquareMouseListener extends MouseAdapter{
  
    public void mouseClicked(MouseEvent e) {
      String rowNumber = null;
      String colNumber = null;
      for (Integer i = 0; i < ROWS; i++)
        for (Integer j = 0; j < COLS; j++)
          if (e.getSource() == gridSquares[i][j]){
            rowNumber = i.toString();
            colNumber = j.toString();
          }
      if(SwingUtilities.isLeftMouseButton(e))
        System.out.println("left clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
      if(SwingUtilities.isRightMouseButton(e))
        System.out.println("right clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
    }
  
    public void mouseEntered(MouseEvent e) {
      for (Integer i = 0; i < ROWS; i++){
        for (Integer j = 0; j < COLS; j++){
          if (e.getSource() == gridSquares[i][j]){
            gridSquares[i][j].setBackground(gridSquares[i][j].getBackground().brighter());
            currentPositionLabel.setText("(Row: " + i + " Col: " + j + ")");
          }
        }
      }
    }
  
    public void mouseExited(MouseEvent e) {
      for (Integer i = 0; i < ROWS; i++){
        for (Integer j = 0; j < COLS; j++){
          if (e.getSource() == gridSquares[i][j])
            gridSquares[i][j].setBackground(mapTiles[i][j].getColor());
        }
      }
    }
    
  }
  
  void printNarration(String add){
    narrationTextArea.setText(narrationTextArea.getText() + add + '\n');
    narrationTextArea.setCaretPosition(narrationTextArea.getDocument().getLength());
  }
}

