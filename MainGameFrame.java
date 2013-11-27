package com.eecs285.siegegame;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainGameFrame extends JFrame{

  private static final long serialVersionUID = 1L;
  JPanel mapPanel, auxPanel;
  JScrollPane narrationPanel;
  JTextArea narrationTextArea;
  JPanel HUDPanel;
  JPanel HUDTopPanel;
  JPanel HUDArmySelectPanel, HUDCitySelectPanel, HUDResSelectPanel, HUDNoneSelectPanel;
  CardLayout HUDLayout;
  
  JPanel currentPositionPanel;
  JLabel currentPositionLabel;
  JPanel [][] gridSquares;
  Tile[][] mapTiles;
  
  GridSquareMouseListener gridSquareMouseListener;
  Integer numGridSquares;
  final Integer ROWS, COLS, SQUARESIZE;
  final Integer SCREEN_HEIGHT, SCREEN_WIDTH, TASKBAR_HEIGHT;
  final Integer AUX_SIZE = 190;
  final Integer NARRATION_FONT_SIZE = 11;
  final Integer POSITION_FONT_SIZE = 9;
  
  public MainGameFrame(String inTitle, Integer numRows, Integer numCols, Tile[][] tiles){
    super(inTitle);
    ImageIcon icon = new ImageIcon("src/Resources/castleIcon3.png");
    setIconImage(icon.getImage());
    ROWS = numRows;
    COLS = numCols;
    
    TASKBAR_HEIGHT = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom;
    SCREEN_HEIGHT = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight() - TASKBAR_HEIGHT;
    SCREEN_WIDTH = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getWidth();
    
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
        JPanel adding = new JPanel();
        LayoutManager overlay = new OverlayLayout(adding);
        gridSquares[i][j] = adding;
        gridSquares[i][j].setLayout(overlay);
        if (mapTiles[i][j] != null)
          gridSquares[i][j].setBackground(mapTiles[i][j].getColor());
        else{
          //mapTiles[i][j] is a city and I need to pick the image of the right color
        }
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
    
    HUDLayout = new CardLayout();
    
    HUDTopPanel = new JPanel(HUDLayout);
    HUDPanel = new JPanel(new BorderLayout());
    HUDPanel.setPreferredSize(new Dimension(AUX_SIZE, AUX_SIZE));
    HUDPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    
    currentPositionLabel = new JLabel();
    currentPositionLabel.setFont(new Font(currentPositionLabel.getName(), Font.PLAIN, POSITION_FONT_SIZE));
    currentPositionLabel.setOpaque(false);
    currentPositionPanel = new JPanel();
    currentPositionPanel.add(currentPositionLabel);
    
    HUDNoneSelectPanel = new JPanel(new BorderLayout());
    HUDNoneSelectPanel.add(new JLabel("Default Information:", JLabel.CENTER), BorderLayout.NORTH);
    
    HUDArmySelectPanel = new JPanel(new BorderLayout());
    HUDArmySelectPanel.add(new JLabel("Army Information:", JLabel.CENTER), BorderLayout.NORTH);
    
    HUDCitySelectPanel = new JPanel(new BorderLayout());
    HUDCitySelectPanel.add(new JLabel("City Information:", JLabel.CENTER), BorderLayout.NORTH);
   
    HUDResSelectPanel = new JPanel(new BorderLayout());
    HUDResSelectPanel.add(new JLabel("Resource Information:", JLabel.CENTER), BorderLayout.NORTH);
    
    HUDTopPanel.add(HUDNoneSelectPanel, "None");
    HUDTopPanel.add(HUDArmySelectPanel, "Army");
    HUDTopPanel.add(HUDCitySelectPanel, "City");
    HUDTopPanel.add(HUDResSelectPanel, "Resource");
    HUDLayout.show(HUDTopPanel, "None");
    
    HUDPanel.add(HUDTopPanel, BorderLayout.CENTER);
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
            if (mapTiles[i][j].getOccupant() != null)//occupied by army
              HUDLayout.show(HUDTopPanel, "army");
            if (mapTiles[i][j].isCity())//is a city
              HUDLayout.show(HUDTopPanel, "city");
            if (mapTiles[i][j].isResource())//is a resource
              HUDLayout.show(HUDTopPanel, "resource");
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
  
  void updateGridSquare(Coord pos, Tile update){
    mapTiles[pos.row][pos.col] = update;
    gridSquares[pos.row][pos.col].setBackground(update.getColor());
    
    if (update.getOccupant() != null || true){
      String occupantColor = "green";//update.getOccpant().getColor().toString();
      Integer strength = 70;//update.getOccupant().getStrength();
      
      BufferedImage armyImage = null;
      try {
        armyImage = ImageIO.read(new File("src/resources/" + occupantColor + "Army.png"));
        armyImage = resizeImage(SQUARESIZE, armyImage);
      } catch (IOException e) {
        e.printStackTrace();
      }
      JLabel armyLabel = new JLabel(new ImageIcon(armyImage));
  
      JLabel strLabel = new JLabel(strength.toString(), JLabel.CENTER);
      strLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, SQUARESIZE/2));
      strLabel.setForeground(Color.BLACK);
      
      if (strength.toString().length() == 1)
        armyLabel.setAlignmentX(0.36f);
      if (strength.toString().length() == 2)
        armyLabel.setAlignmentX(0.21f);
      if (strength.toString().length() == 3){
        strLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, SQUARESIZE/2 - SQUARESIZE/9));
        armyLabel.setAlignmentX(0.157f);
      }
  
      gridSquares[pos.row][pos.col].add(strLabel);
      gridSquares[pos.row][pos.col].add(armyLabel);
    }
  }
  
  BufferedImage resizeImage(int size, BufferedImage img){
    int w = img.getWidth();
    int h = img.getHeight();
    BufferedImage dimg = new BufferedImage(size, size, img.getType());  
    Graphics2D g = dimg.createGraphics();  
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
    g.drawImage(img, 0, 0, size, size, 0, 0, w, h, null);
    g.dispose();  
    return dimg;
  }
}

