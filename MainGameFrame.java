package eecs285.proj4.jminjie;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainGameFrame extends JFrame{

  private static final long serialVersionUID = 1L;
  JPanel mapPanel;
  JPanel auxPanel;
  JPanel narrationPanel;
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
    auxPanel.setPreferredSize(new Dimension(200, ROWS * SQUARESIZE));
    auxPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    
    narrationPanel = new JPanel();
    narrationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    narrationPanel.add(new JLabel("Narration:"));
    
    HUDPanel = new JPanel(new BorderLayout());
    HUDPanel.setPreferredSize(new Dimension(200, 200));
    HUDPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    HUDPanel.add(new JLabel("HUD:", JLabel.CENTER), BorderLayout.NORTH);
    
    currentPositionLabel = new JLabel();
    currentPositionLabel.setFont(new Font(currentPositionLabel.getName(), Font.PLAIN, 9));
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
      String spaceNumber = null;
      String rowNumber = null;
      String colNumber = null;
      for (Integer i = 0; i < numGridSquares; i++)
        if (e.getSource() == gridSquares[i]){
          spaceNumber = i.toString();
          rowNumber = getRow(i).toString();
          colNumber = getCol(i).toString();
        }
      if(SwingUtilities.isLeftMouseButton(e))
        System.out.println("left clicked on square " + spaceNumber + " (Row: " + rowNumber + " Col: " + colNumber + ")");
      if(SwingUtilities.isRightMouseButton(e))
        System.out.println("right clicked on square " + spaceNumber + " (Row: " + rowNumber + " Col: " + colNumber + ")");
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
  
  Integer getRow(Integer spaceNumber){
    return spaceNumber / COLS;
  }
  Integer getCol(Integer spaceNumber){
    return spaceNumber % COLS;
  }
}

