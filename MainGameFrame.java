package eecs285.proj4.jminjie;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
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
  JPanel [] gridSquares;
  
  GridSquareMouseListener gridSquareMouseListener;
  Integer numGridSquares;
  final Integer ROWS;
  final Integer COLS;
  final Integer SQUARESIZE = 22;
  
  public MainGameFrame(String inTitle, Integer numRows, Integer numCols){
    super(inTitle);
    setLayout(new BorderLayout());
    mapPanel = new JPanel();
    mapPanel.setLayout(new GridLayout(numRows, numCols));
    numGridSquares = numRows * numCols;
    ROWS = numRows;
    COLS = numCols;
    
    gridSquareMouseListener = new GridSquareMouseListener();
    
    gridSquares = new JPanel[numGridSquares];
    
    for (int i = 0; i < numGridSquares; i++){
      gridSquares[i] = new JPanel();
      gridSquares[i].setBackground(Color.GREEN.darker());
      gridSquares[i].addMouseListener(gridSquareMouseListener);
      gridSquares[i].setSize(30, 30);
      gridSquares[i].setPreferredSize(new Dimension(SQUARESIZE, SQUARESIZE));
      mapPanel.add(gridSquares[i]);
    }
    mapPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    add(mapPanel, BorderLayout.CENTER);
    
    auxPanel = new JPanel(new BorderLayout());
    auxPanel.setPreferredSize(new Dimension(200, 600));
    auxPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    
    narrationPanel = new JPanel();
    narrationPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    narrationPanel.add(new JLabel("Narration:"));
    
    HUDPanel = new JPanel(new BorderLayout());
    HUDPanel.setPreferredSize(new Dimension(200, 200));
    HUDPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    HUDPanel.add(new JLabel("HUD:", JLabel.CENTER), BorderLayout.NORTH);
    
    currentPositionLabel = new JLabel();
    currentPositionLabel.setFont(new Font(currentPositionLabel.getName(), Font.PLAIN, 10));
    currentPositionPanel = new JPanel();
    currentPositionPanel.add(currentPositionLabel);
    HUDPanel.add(currentPositionPanel, BorderLayout.SOUTH);
    
    auxPanel.add(narrationPanel, BorderLayout.CENTER);
    auxPanel.add(HUDPanel, BorderLayout.SOUTH);
    
    add(auxPanel, BorderLayout.WEST);
    
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocation(150, 10);
    setVisible(true);
  }
  

  public class GridSquareMouseListener extends MouseAdapter{
  
    public void mouseClicked(MouseEvent e) {
      String spaceNumber = "";
      String rowNumber = "";
      String colNumber = "";
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
      for (int i = 0; i < numGridSquares; i++){
        if (e.getSource() == gridSquares[i]){
          gridSquares[i].setBackground(Color.GREEN.brighter());
          currentPositionLabel.setText("(Row: " + getRow(i).toString() + " Col: " + getCol(i).toString() + ")");
        }
      }
    }
  
    public void mouseExited(MouseEvent e) {
      for (int i = 0; i < numGridSquares; i++){
        if (e.getSource() == gridSquares[i])
          gridSquares[i].setBackground(Color.GREEN.darker());
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

