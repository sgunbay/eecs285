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
import java.awt.FlowLayout;
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
import java.util.ArrayList;

public class MainGameFrame extends JFrame{

  private static final long serialVersionUID = 1L;
  JPanel mapPanel, auxPanel;
  JScrollPane narrationPanel;
  JTextArea narrationTextArea;
  JPanel HUDPanel, HUDTopPanel;
  JPanel HUDLockedPanel, HUDArmySelectPanel, HUDCitySelectPanel, HUDResSelectPanel, HUDNoneSelectPanel;
  CardLayout HUDLayout;
  JPanel HUDNoneSelectMiddlePanel;
  JLabel currentGold, currentIncome;
  
  JPanel currentPositionPanel;
  JLabel currentPositionLabel;
  JPanel [][] gridSquares;
  Tile[][] mapTiles;
  Tile currentSelected;
  
  GridSquareMouseListener gridSquareMouseListener;
  Integer numGridSquares;
  final Integer ROWS, COLS, SQUARESIZE;
  final Integer SCREEN_HEIGHT, SCREEN_WIDTH, TASKBAR_HEIGHT;
  final Integer AUX_SIZE = 190;
  final Integer NARRATION_FONT_SIZE = 11;
  final Integer POSITION_FONT_SIZE = 9;
  
  GetNameDialog getNameDialog;
  String name;
  static Integer playerIndex = 0;
  static ArrayList<String> currentNames = new ArrayList<String>();
  
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
    
    name = "Player " + playerIndex.toString();
    playerIndex++;
    getNameDialog = new GetNameDialog(this);
    if (currentNames.indexOf(getNameDialog.getInput()) == -1){
      name = getNameDialog.getInput();
      currentNames.add(name);
    }
    
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
    
    HUDLockedPanel = new JPanel(new BorderLayout());
    HUDLockedPanel.add(new JLabel("Waiting for other players", JLabel.CENTER), BorderLayout.CENTER);
    
    HUDNoneSelectPanel = new JPanel(new BorderLayout());
    HUDNoneSelectPanel.add(new JLabel(name + "'s turn", JLabel.CENTER), BorderLayout.NORTH);
    currentGold = new JLabel("Current Gold: 0", JLabel.CENTER);
    currentGold.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    currentIncome = new JLabel("Current Income: 0", JLabel.CENTER);
    currentIncome.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

    HUDNoneSelectMiddlePanel = new JPanel(new FlowLayout());
    HUDNoneSelectMiddlePanel.add(currentGold);
    HUDNoneSelectMiddlePanel.add(currentIncome);
    HUDNoneSelectPanel.add(HUDNoneSelectMiddlePanel, BorderLayout.CENTER);
    
    HUDArmySelectPanel = new JPanel(new BorderLayout());
    HUDArmySelectPanel.add(new JLabel("Army Information:", JLabel.CENTER), BorderLayout.NORTH);
    
    HUDCitySelectPanel = new JPanel(new BorderLayout());
    HUDCitySelectPanel.add(new JLabel("City Information:", JLabel.CENTER), BorderLayout.NORTH);
   
    HUDResSelectPanel = new JPanel(new BorderLayout());
    HUDResSelectPanel.add(new JLabel("Resource Information:", JLabel.CENTER), BorderLayout.NORTH);
    
    HUDTopPanel.add(HUDLockedPanel, "Lock");
    HUDTopPanel.add(HUDNoneSelectPanel, "None");
    HUDTopPanel.add(HUDArmySelectPanel, "Army");
    HUDTopPanel.add(HUDCitySelectPanel, "City");
    HUDTopPanel.add(HUDResSelectPanel, "Resource");
    HUDLayout.show(HUDTopPanel, "Lock");
    
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
      Integer rowNumber = null;
      Integer colNumber = null;
      for (Integer i = 0; i < ROWS; i++)
        for (Integer j = 0; j < COLS; j++)
          if (e.getSource() == gridSquares[i][j]){
            rowNumber = i;
            colNumber = j;
            if (currentSelected == null || currentSelected.getOccupant() == null){
              //last map click wasn't an army
              currentSelected = mapTiles[i][j];
              if (currentSelected.getOccupant() != null){//occupied by army
                HUDLayout.show(HUDTopPanel, "Army");
                //box outline around
              }
              else if (currentSelected.isCity())//is a city
                HUDLayout.show(HUDTopPanel, "City");
              else if (currentSelected.isResource())//is a resource
                HUDLayout.show(HUDTopPanel, "Resource");
              else
                HUDLayout.show(HUDTopPanel, "None");
            }
            else{
              //last map click was an army
              Army prevSelectedArmy = currentSelected.getOccupant();
              currentSelected = mapTiles[i][j];
              if (currentSelected.getOccupant() != null)//occupied by army
                System.out.println("Army attacking army");
              else if (currentSelected.isCity())//is a city
                System.out.println("Army claims city");
              else if (currentSelected.isResource())//is a resource
                System.out.println("Army claims resource");
              else
                System.out.println("Army moving");
            }
          }
      if(SwingUtilities.isLeftMouseButton(e)){
        System.out.println("left clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
        outlineAroundSquare(rowNumber, colNumber, 7);
      }
      if(SwingUtilities.isRightMouseButton(e)){
        System.out.println("right clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
        undoOutlineAroundSquare(rowNumber, colNumber, 7);
      }
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
      String occupantColor = "green";//update.getOccpant().getColor();
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
  
  Color stringToColor(String in){
    if (in.equals("plains"))
      return Color.GREEN.darker();
    else if (in.equals("forest"))
      return Color.green.darker().darker();
    else if (in.equals("muddy"))
      return new Color(90, 90, 0);
    else if (in.equals("mountain"))
      return new Color(90, 50, 0);
    else if (in.equals("water"))
      return Color.BLUE;
    else
      return null;
  }
  
  String getPlayerName(){
    return name;
  }
  
  void printGold(Integer gold){
    currentGold.setText("Current Gold: " + gold);
  }
  void printIncome(Integer income){
    currentIncome.setText("Current Income: " + income);
  }
  
  void outlineAroundSquare(Integer row, Integer col, Integer radius){
    gridSquares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
    for (int j = 0; j <= radius; j++){
      for (int i = 0; i <= radius - j; i++){
        if (row + j < ROWS && col + i < COLS)
          gridSquares[row + j][col + i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (row + j < ROWS && col - i >= 0)
          gridSquares[row + j][col - i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (row - j >= 0 && col + i < COLS)
          gridSquares[row - j][col + i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        if (row - j >= 0 && col - i >= 0)
          gridSquares[row - j][col - i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
    }
  }
  
  void undoOutlineAroundSquare(Integer row, Integer col, Integer radius){
    gridSquares[row][col].setBorder(null);
    for (int j = 0; j <= radius; j++){
      for (int i = 0; i <= radius - j; i++){
        if (row + j < ROWS && col + i < COLS)
          gridSquares[row + j][col + i].setBorder(null);
        if (row + j < ROWS && col - i >= 0)
          gridSquares[row + j][col - i].setBorder(null);
        if (row - j >= 0 && col + i < COLS)
          gridSquares[row - j][col + i].setBorder(null);
        if (row - j >= 0 && col - i >= 0)
          gridSquares[row - j][col - i].setBorder(null);
      }
    }
  }
}

