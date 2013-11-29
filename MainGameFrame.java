package com.eecs285.siegegame;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  final Integer AUX_SIZE = 267;
  final Font NARRATION_FONT = new Font(Font.SERIF, Font.PLAIN, 14);
  final Font POSITION_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
  final Font HUD_HEADER_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
  
  GetNameDialog getNameDialog;
  String name;
  static Integer playerIndex = 0;
  static ArrayList<String> currentNames = new ArrayList<String>();
  
  JTextArea armyOwner_ARMY;
  ImageIcon basicIcon_ARMY, attackerIcon_ARMY, explorerIcon_ARMY, rusherIcon_ARMY;
  JLabel numBasic_ARMY, numAttacker_ARMY, numExplorer_ARMY, numRusher_ARMY;
  JLabel strength_ARMY;
  JLabel numberOfUnits_ARMY;
  JPanel HUDArmySelectTopPanel;
  JPanel HUDArmySelectBottomPanel;
  
  JLabel cityStatus_CITY;
  ButtonGroup trainingButtons_CITY;
  JRadioButton basicRadioBut_CITY, attackerRadioBut_CITY, explorerRadioBut_CITY, rusherRadioBut_CITY;
  JPanel HUDCitySelectMainPanel;
  CardLayout cityMainLayout;
  JPanel unoccupiedCityPanel;
  JPanel myCityPanel;
  JPanel enemyCityPanel, enemyCityTopPanel, enemyCityBottomPanel;

  ImageIcon basicIconMyCity, attackerIconMyCity, explorerIconMyCity, rusherIconMyCity;
  JLabel numBasicMyCity, numAttackerMyCity, numExplorerMyCity, numRusherMyCity;
  JLabel strengthLabelMyCity;
  JLabel numberOfUnitsLabelMyCity;
  JTextField trainNumberOfUnits;
  JButton trainingButton;
  
  ImageIcon pressBasicIcon, pressAttackerIcon, pressExplorerIcon, pressRusherIcon;
  
  ImageIcon basicIconEnemyCity, attackerIconEnemyCity, explorerIconEnemyCity, rusherIconEnemyCity;
  JLabel numBasicEnemyCity, numAttackerEnemyCity, numExplorerEnemyCity, numRusherEnemyCity;
  JLabel strengthLabelEnemyCity;
  JLabel numberOfUnitsLabelEnemyCity;
  
  JLabel resourceState;
  JLabel resourceBonus;
  
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
    
    narrationTextArea = new JTextArea("Welcome to Siege, " + name + "!\n");
    narrationTextArea.setFont(NARRATION_FONT);
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
    
    currentPositionLabel = new JLabel("Row: Col:");
    currentPositionLabel.setFont(POSITION_FONT);
    currentPositionLabel.setOpaque(false);
    currentPositionPanel = new JPanel();
    currentPositionPanel.add(currentPositionLabel);

    //************************LOCKED SELECT HUD***************************
    HUDLockedPanel = new JPanel(new BorderLayout());
    HUDLockedPanel.add(new JLabel("Waiting for other players", JLabel.CENTER), BorderLayout.CENTER);

    //************************NONE SELECT HUD***************************
    HUDNoneSelectPanel = new JPanel(new BorderLayout());
    
    currentGold = new JLabel("Current Gold: 0", JLabel.CENTER);
    currentGold.setFont(HUD_HEADER_FONT);
    currentIncome = new JLabel("Current Income: 0", JLabel.CENTER);
    currentIncome.setFont(HUD_HEADER_FONT);

    HUDNoneSelectMiddlePanel = new JPanel(new GridLayout(4,1));
    HUDNoneSelectMiddlePanel.add(new JLabel());
    HUDNoneSelectMiddlePanel.add(currentGold);
    HUDNoneSelectMiddlePanel.add(currentIncome);
    HUDNoneSelectMiddlePanel.add(new JLabel());
    HUDNoneSelectPanel.add(HUDNoneSelectMiddlePanel, BorderLayout.CENTER);
    

    //************************ARMY SELECT HUD***************************
    basicIcon_ARMY = new ImageIcon("src/Resources/basicIcon.png");
    attackerIcon_ARMY = new ImageIcon("src/Resources/attackerIcon.png");
    explorerIcon_ARMY = new ImageIcon("src/Resources/explorerIcon.png");
    rusherIcon_ARMY = new ImageIcon("src/Resources/rusherIcon.png");
    armyOwner_ARMY = new JTextArea("Jordan's army");
    armyOwner_ARMY.setEditable(false);
    armyOwner_ARMY.setOpaque(false);
    
    armyOwner_ARMY.setFont(HUD_HEADER_FONT);
    strength_ARMY = new JLabel("Army strength: ???", JLabel.CENTER);
    numberOfUnits_ARMY = new JLabel("Number of units: ???", JLabel.CENTER);
    
    numBasic_ARMY = new JLabel(": ???");
    numAttacker_ARMY = new JLabel(": ???");
    numExplorer_ARMY = new JLabel(": ???");
    numRusher_ARMY = new JLabel(": ???");
    
    HUDArmySelectTopPanel = new JPanel(new GridLayout(3, 1));
    HUDArmySelectTopPanel.add(armyOwner_ARMY);
    HUDArmySelectTopPanel.add(strength_ARMY);
    HUDArmySelectTopPanel.add(numberOfUnits_ARMY);
    HUDArmySelectBottomPanel = new JPanel(new GridLayout(2,2));
    
    JPanel basicPan = new JPanel(new FlowLayout());
    JPanel attackerPan = new JPanel(new FlowLayout());
    JPanel explorerPan = new JPanel(new FlowLayout());
    JPanel rusherPan = new JPanel(new FlowLayout());
    
    basicPan.add(new JLabel(basicIcon_ARMY));
    basicPan.add(numBasic_ARMY);
    attackerPan.add(new JLabel(attackerIcon_ARMY));
    attackerPan.add(numAttacker_ARMY);
    explorerPan.add(new JLabel(explorerIcon_ARMY));
    explorerPan.add(numExplorer_ARMY);
    rusherPan.add(new JLabel(rusherIcon_ARMY));
    rusherPan.add(numRusher_ARMY);
    
    HUDArmySelectBottomPanel.add(basicPan);
    HUDArmySelectBottomPanel.add(attackerPan);
    HUDArmySelectBottomPanel.add(explorerPan);
    HUDArmySelectBottomPanel.add(rusherPan);


    
    HUDArmySelectPanel = new JPanel(new BorderLayout());
    HUDArmySelectPanel.add(HUDArmySelectTopPanel, BorderLayout.NORTH);
    HUDArmySelectPanel.add(HUDArmySelectBottomPanel, BorderLayout.CENTER);
    

    //************************CITY SELECT HUD***************************
    cityStatus_CITY = new JLabel("City is occupied by Jordan", JLabel.CENTER);

    cityStatus_CITY.setFont(HUD_HEADER_FONT);
    
    cityMainLayout = new CardLayout();
    //******unoccupied card*******
    unoccupiedCityPanel = new JPanel(new BorderLayout());

    //******my city card*******
    myCityPanel = new JPanel(new BorderLayout());
    
    JPanel myCityTopPanel = new JPanel(new GridLayout(2, 1));
    JPanel myCityArmyPanel = new JPanel(new GridLayout(2, 4));
    JPanel myCityTrainingPanel = new JPanel(new BorderLayout());
    JPanel myCityTrainingTopPanel = new JPanel(new FlowLayout());
    JPanel myCityTrainingBottomPanel = new JPanel(new FlowLayout());
    
    basicIconMyCity = new ImageIcon("src/Resources/basicIcon.png");
    attackerIconMyCity = new ImageIcon("src/Resources/attackerIcon.png");
    explorerIconMyCity = new ImageIcon("src/Resources/explorerIcon.png");
    rusherIconMyCity = new ImageIcon("src/Resources/rusherIcon.png");
    pressBasicIcon = new ImageIcon("src/Resources/pressBasicIcon.png");
    pressAttackerIcon = new ImageIcon("src/Resources/pressAttackerIcon.png");
    pressExplorerIcon = new ImageIcon("src/Resources/pressExplorerIcon.png");
    pressRusherIcon = new ImageIcon("src/Resources/pressRusherIcon.png");
    strengthLabelMyCity = new JLabel("Occupying army strength: ???", JLabel.CENTER);
    numberOfUnitsLabelMyCity = new JLabel("Number of units: ???", JLabel.CENTER);
    
    numBasicMyCity = new JLabel(": ???");
    numAttackerMyCity = new JLabel(": ???");
    numExplorerMyCity = new JLabel(": ???");
    numRusherMyCity = new JLabel(": ???");
    
    myCityTopPanel.add(strengthLabelMyCity);
    myCityTopPanel.add(numberOfUnitsLabelMyCity);
    
    myCityArmyPanel.add(new JLabel(basicIconMyCity));
    myCityArmyPanel.add(numBasicMyCity);
    myCityArmyPanel.add(new JLabel(attackerIconMyCity));
    myCityArmyPanel.add(numAttackerMyCity);
    myCityArmyPanel.add(new JLabel(explorerIconMyCity));
    myCityArmyPanel.add(numExplorerMyCity);
    myCityArmyPanel.add(new JLabel(rusherIconMyCity));
    myCityArmyPanel.add(numRusherMyCity);
    
    trainNumberOfUnits = new JTextField();
    trainNumberOfUnits.setPreferredSize(new Dimension(30, 20));
    trainNumberOfUnits.setText("0");
    trainingButton = new JButton();
    trainingButton.setText("$0");
    
    myCityTrainingTopPanel.add(new JLabel("Train "));
    myCityTrainingTopPanel.add(trainNumberOfUnits);
    myCityTrainingTopPanel.add(new JLabel("(up to 20) units"));
    myCityTrainingTopPanel.add(trainingButton);
    
    myCityTrainingPanel.add(myCityTrainingTopPanel, BorderLayout.NORTH);
    myCityTrainingPanel.add(myCityTrainingBottomPanel, BorderLayout.SOUTH);
    myCityTrainingPanel.setBorder(BorderFactory.createEtchedBorder());
    UnitButtonListener unitButtonListener = new UnitButtonListener();
    basicRadioBut_CITY = new JRadioButton(basicIconMyCity);
    basicRadioBut_CITY.setSelectedIcon(pressBasicIcon);
    basicRadioBut_CITY.setBorder(null);
    basicRadioBut_CITY.setToolTipText("Basic Unit");
    basicRadioBut_CITY.addActionListener(unitButtonListener);
    attackerRadioBut_CITY = new JRadioButton(attackerIconMyCity);
    attackerRadioBut_CITY.setSelectedIcon(pressAttackerIcon);
    attackerRadioBut_CITY.setBorder(null);
    attackerRadioBut_CITY.setToolTipText("Attacker");
    attackerRadioBut_CITY.addActionListener(unitButtonListener);
    explorerRadioBut_CITY = new JRadioButton(explorerIconMyCity);
    explorerRadioBut_CITY.setSelectedIcon(pressExplorerIcon);
    explorerRadioBut_CITY.setBorder(null);
    explorerRadioBut_CITY.setToolTipText("Explorer");
    explorerRadioBut_CITY.addActionListener(unitButtonListener);
    rusherRadioBut_CITY = new JRadioButton(rusherIconMyCity);
    rusherRadioBut_CITY.setSelectedIcon(pressRusherIcon);
    rusherRadioBut_CITY.setBorder(null);
    rusherRadioBut_CITY.setToolTipText("Rusher");
    rusherRadioBut_CITY.addActionListener(unitButtonListener);
    trainingButtons_CITY = new ButtonGroup();
    trainingButtons_CITY.add(basicRadioBut_CITY);
    trainingButtons_CITY.add(attackerRadioBut_CITY);
    trainingButtons_CITY.add(explorerRadioBut_CITY);
    trainingButtons_CITY.add(rusherRadioBut_CITY);
    
    myCityTrainingBottomPanel.add(basicRadioBut_CITY);
    myCityTrainingBottomPanel.add(attackerRadioBut_CITY);
    myCityTrainingBottomPanel.add(explorerRadioBut_CITY);
    myCityTrainingBottomPanel.add(rusherRadioBut_CITY);
    
    
    myCityPanel.add(myCityTopPanel, BorderLayout.NORTH);
    myCityPanel.add(myCityArmyPanel, BorderLayout.CENTER);
    myCityPanel.add(myCityTrainingPanel, BorderLayout.SOUTH);

    //******enemy city card*******
    enemyCityPanel = new JPanel(new BorderLayout());
    
    basicIconEnemyCity = new ImageIcon("src/Resources/basicIcon.png");
    attackerIconEnemyCity = new ImageIcon("src/Resources/attackerIcon.png");
    explorerIconEnemyCity = new ImageIcon("src/Resources/explorerIcon.png");
    rusherIconEnemyCity = new ImageIcon("src/Resources/rusherIcon.png");
    strengthLabelEnemyCity = new JLabel("Occupying army strength: ???", JLabel.CENTER);
    numberOfUnitsLabelEnemyCity = new JLabel("Number of units: ???", JLabel.CENTER);
    
    numBasicEnemyCity = new JLabel(": ???");
    numAttackerEnemyCity = new JLabel(": ???");
    numExplorerEnemyCity = new JLabel(": ???");
    numRusherEnemyCity = new JLabel(": ???");
    
    enemyCityTopPanel = new JPanel(new GridLayout(2, 1));
    enemyCityTopPanel.add(strengthLabelEnemyCity);
    enemyCityTopPanel.add(numberOfUnitsLabelEnemyCity);
    enemyCityBottomPanel = new JPanel(new GridLayout(4,2));
    
    JPanel basicPanEnemyCity = new JPanel(new FlowLayout());
    JPanel attackerPanEnemyCity = new JPanel(new FlowLayout());
    JPanel explorerPanEnemyCity = new JPanel(new FlowLayout());
    JPanel rusherPanEnemyCity = new JPanel(new FlowLayout());
    
    basicPanEnemyCity.add(new JLabel(basicIconEnemyCity));
    basicPanEnemyCity.add(numBasicEnemyCity);
    attackerPanEnemyCity.add(new JLabel(attackerIconEnemyCity));
    attackerPanEnemyCity.add(numAttackerEnemyCity);
    explorerPanEnemyCity.add(new JLabel(explorerIconEnemyCity));
    explorerPanEnemyCity.add(numExplorerEnemyCity);
    rusherPanEnemyCity.add(new JLabel(rusherIconEnemyCity));
    rusherPanEnemyCity.add(numRusherEnemyCity);
    
    enemyCityBottomPanel.add(basicPanEnemyCity);
    enemyCityBottomPanel.add(attackerPanEnemyCity);
    enemyCityBottomPanel.add(explorerPanEnemyCity);
    enemyCityBottomPanel.add(rusherPanEnemyCity);
    enemyCityBottomPanel.add(new JLabel(""));
    enemyCityBottomPanel.add(new JLabel(""));
    enemyCityBottomPanel.add(new JLabel(""));
    enemyCityBottomPanel.add(new JLabel(""));

    enemyCityPanel.add(enemyCityTopPanel, BorderLayout.NORTH);
    enemyCityPanel.add(enemyCityBottomPanel, BorderLayout.CENTER);
    
    //add all cards
    HUDCitySelectMainPanel = new JPanel(cityMainLayout);
    HUDCitySelectMainPanel.add(unoccupiedCityPanel, "unoccupied");
    HUDCitySelectMainPanel.add(myCityPanel, "my");
    HUDCitySelectMainPanel.add(enemyCityPanel, "enemy");
    cityMainLayout.show(HUDCitySelectMainPanel, "my");
    
    HUDCitySelectPanel = new JPanel(new BorderLayout());
    HUDCitySelectPanel.add(cityStatus_CITY, BorderLayout.NORTH);
    HUDCitySelectPanel.add(HUDCitySelectMainPanel, BorderLayout.CENTER);
   
    
    //**********************************RESOURCE SELECT HUD************************************
    HUDResSelectPanel = new JPanel(new GridLayout(4, 1));
    
    resourceState = new JLabel("Resource is owned by Jordan", JLabel.CENTER);
    resourceState.setFont(HUD_HEADER_FONT);
    resourceBonus = new JLabel("Resource adds 10 income", JLabel.CENTER);

    resourceBonus.setFont(HUD_HEADER_FONT);
    
    HUDResSelectPanel.add(new JLabel());
    HUDResSelectPanel.add(resourceState);
    HUDResSelectPanel.add(resourceBonus);
    HUDResSelectPanel.add(new JLabel());
   
    //add all cards
    HUDTopPanel.add(HUDLockedPanel, "Lock");
    HUDTopPanel.add(HUDNoneSelectPanel, "None");
    HUDTopPanel.add(HUDArmySelectPanel, "Army");
    HUDTopPanel.add(HUDCitySelectPanel, "City");
    HUDTopPanel.add(HUDResSelectPanel, "Resource");
    HUDLayout.show(HUDTopPanel, "City");
    
    HUDPanel.add(HUDTopPanel, BorderLayout.CENTER);
    HUDPanel.add(currentPositionPanel, BorderLayout.SOUTH);
    
    auxPanel.add(narrationPanel, BorderLayout.CENTER);
    auxPanel.add(HUDPanel, BorderLayout.SOUTH);
    
    add(auxPanel, BorderLayout.WEST);
    
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  public class UnitButtonListener implements ActionListener{

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == basicRadioBut_CITY)
        System.out.println("Clicked on basic unit button");
      if (e.getSource() == attackerRadioBut_CITY)
        System.out.println("Clicked on attacker unit button");
      if (e.getSource() == explorerRadioBut_CITY)
        System.out.println("Clicked on explorer unit button");
      if (e.getSource() == rusherRadioBut_CITY)
        System.out.println("Clicked on rusher unit button");
    }
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
              else if (currentSelected.isCity()){//is a city
                HUDLayout.show(HUDTopPanel, "City");
                //if (currentSelected.getOccupant() == null)
                //  cityMainLayout.show(cityMainPanel, "unoccupied");
                //else if (currentSelected.getOccupant() == currentPlayer)
                //  cityMainLayout.show(cityMainPanel, "my");
                //else
                //  cityMainLayout.show(cityMainPanel, "enemy");
              }
              else if (currentSelected.isResource())//is a resource
                HUDLayout.show(HUDTopPanel, "Resource");
              else
                HUDLayout.show(HUDTopPanel, "None");
            }
            else{
              //last map click was an army
              Tile prevSelected = currentSelected;
              currentSelected = mapTiles[i][j];
              if (prevSelected.coord.row == currentSelected.coord.row
                  &&prevSelected.coord.col == currentSelected.coord.col){//clicked again
                System.out.println("Tile deselected");
                currentSelected = null;
              }
              else if (currentSelected.getOccupant() != null)//occupied by army
                System.out.println("Army attempting attack");
              else if (currentSelected.isCity())//is a city
                System.out.println("Army attempting attack on city");
              else
                System.out.println("Attempting move");
            }
          }
      if(SwingUtilities.isLeftMouseButton(e)){
        System.out.println("left clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
        outlineSquare(rowNumber, colNumber);
      }
      if(SwingUtilities.isRightMouseButton(e)){
        System.out.println("right clicked on square at: (Row: " + rowNumber + " Col: " + colNumber + ")");
        undoOutlineSquare(rowNumber, colNumber);
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
    narrationTextArea.setText(narrationTextArea.getText() + "\u2023 " + add + '\n');
    narrationTextArea.setCaretPosition(narrationTextArea.getDocument().getLength());
  }
  
  void updateGridSquare(Coord pos, Tile update){
    mapTiles[pos.row][pos.col] = update;
    gridSquares[pos.row][pos.col].setBackground(update.getColor());
    
    if (update.getOccupant() != null || true){
      String occupantColor = "white";//update.getOccpant().getColor();
      Integer strength = 70;//update.getOccupant().getStrength();
      
      BufferedImage armyImage = null;
      try {
        armyImage = ImageIO.read(new File("src/resources/" + occupantColor + "Resource.png"));
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
  
  void outlineSquare(Coord x){
    outlineSquare(x.row, x.col);
  }
  
  void outlineSquare(Integer row, Integer col){
    gridSquares[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }
  
  void undoOutlineSquare(Coord x){
    undoOutlineSquare(x.row, x.col);
  }
  
  void undoOutlineSquare(Integer row, Integer col){
    gridSquares[row][col].setBorder(null);
  }
}

