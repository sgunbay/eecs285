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

public class MainGameFrame extends JFrame {

  private static final long serialVersionUID = 1L;
  JPanel mapPanel, auxPanel;
  JScrollPane narrationPanel;
  JTextArea narrationTextArea;
  JPanel HUDPanel, HUDTopPanel;
  JPanel HUDLockedPanel, HUDArmySelectPanel, HUDCitySelectPanel,
      HUDResSelectPanel, HUDNoneSelectPanel;
  CardLayout HUDLayout;
  String currentHUDCard;
  JPanel HUDNoneSelectMiddlePanel, HUDLockedMainPanel;
  JLabel currentGold, currentIncome;

  JPanel currentPositionPanel;
  JLabel currentPositionLabel;
  JPanel[][] gridSquares;
  Tile currentSelected;
  Tile prevSelected;

  GridSquareMouseListener gridSquareMouseListener;
  Integer numGridSquares;
  final Integer ROWS, COLS, SQUARESIZE;
  final Integer SCREEN_HEIGHT, SCREEN_WIDTH, TASKBAR_HEIGHT;
  final Integer AUX_SIZE = 267;
  final Font NARRATION_FONT = new Font(Font.SERIF, Font.PLAIN, 14);
  final Font POSITION_FONT = new Font(Font.SERIF, Font.PLAIN, 12);
  final Font HUD_HEADER_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 18);

  final int BASIC_COST;
  final int EXPLORER_COST;
  final int ATTACKER_COST;
  final int RUSHER_COST;
  final Integer MAX_ARMY_SIZE = 100;

  GetNameDialog getNameDialog;

  String name;
  static Integer playerIndex = 0;
  static ArrayList<String> currentNames = new ArrayList<String>();

  JButton readyButton;
  JLabel readyLabel;
  JButton endTurnButton;

  JLabel armyOwner_ARMY;
  ImageIcon basicIcon_ARMY, attackerIcon_ARMY, explorerIcon_ARMY,
      rusherIcon_ARMY;
  JLabel numBasic_ARMY, numAttacker_ARMY, numExplorer_ARMY, numRusher_ARMY;
  JLabel strength_ARMY;
  JLabel numberOfUnits_ARMY;
  JPanel HUDArmySelectTopPanel;
  JPanel HUDArmySelectBottomPanel;

  JLabel cityStatus_CITY;
  ButtonGroup trainingButtons_CITY;
  JRadioButton basicRadioBut_CITY, attackerRadioBut_CITY,
      explorerRadioBut_CITY, rusherRadioBut_CITY;
  JPanel HUDCitySelectMainPanel;
  CardLayout cityMainLayout;
  JPanel unoccupiedCityPanel;
  JPanel myCityPanel;
  JPanel enemyCityPanel, enemyCityTopPanel, enemyCityBottomPanel;

  ImageIcon basicIconMyCity, attackerIconMyCity, explorerIconMyCity,
      rusherIconMyCity;
  JLabel numBasicMyCity, numAttackerMyCity, numExplorerMyCity, numRusherMyCity;
  JLabel strengthLabelMyCity;
  JLabel numberOfUnitsLabelMyCity;
  JTextField trainNumberOfUnits;
  JButton trainingButton;

  ImageIcon pressBasicIcon, pressAttackerIcon, pressExplorerIcon,
      pressRusherIcon;

  ImageIcon basicIconEnemyCity, attackerIconEnemyCity, explorerIconEnemyCity,
      rusherIconEnemyCity;
  JLabel numBasicEnemyCity, numAttackerEnemyCity, numExplorerEnemyCity,
      numRusherEnemyCity;
  JLabel strengthLabelEnemyCity;
  JLabel numberOfUnitsLabelEnemyCity;

  JLabel resourceState;
  JLabel resourceBonus;

  public MainGameFrame(Grid grid) throws Exception{
    super("Siege");
    ImageIcon icon = new ImageIcon("src/Resources/castleIcon3.png");
    setIconImage(icon.getImage());
    ROWS = grid.rows;
    COLS = grid.cols;

    BASIC_COST = new UnitBasic().cost;
    EXPLORER_COST = new UnitExplorer().cost;
    ATTACKER_COST = new UnitAttacker().cost;
    RUSHER_COST = new UnitRusher().cost;

    TASKBAR_HEIGHT = Toolkit.getDefaultToolkit().getScreenInsets(
        getGraphicsConfiguration()).bottom;
    SCREEN_HEIGHT = (int) GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getMaximumWindowBounds().getHeight()
        - TASKBAR_HEIGHT;
    SCREEN_WIDTH = (int) GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getMaximumWindowBounds().getWidth();

    SQUARESIZE = Math.min(SCREEN_HEIGHT / ROWS, SCREEN_WIDTH / COLS);
    setLayout(new BorderLayout());
    mapPanel = new JPanel();
    mapPanel.setLayout(new GridLayout(ROWS, COLS));
    numGridSquares = ROWS * COLS;

    gridSquareMouseListener = new GridSquareMouseListener();

    name = "Player " + playerIndex.toString();
    playerIndex++;
    getNameDialog = new GetNameDialog(this);
    if (currentNames.indexOf(getNameDialog.getInput()) == -1) {
      name = getNameDialog.getInput();
      currentNames.add(name);
    }

    gridSquares = new JPanel[ROWS][COLS];

    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        JPanel adding = new JPanel();
        LayoutManager overlay = new OverlayLayout(adding);
        gridSquares[i][j] = adding;
        gridSquares[i][j].setLayout(overlay);
        if (Siege.grid.getTile(new Coord(i, j)) != null)
          gridSquares[i][j].setBackground(stringToColor(Siege.grid.getTile(
              new Coord(i, j)).getColor()));
        else {
          // mapTiles[i][j] is a city and I need to pick the image of the right
          // color
        }
        gridSquares[i][j].addMouseListener(gridSquareMouseListener);
        gridSquares[i][j]
            .setPreferredSize(new Dimension(SQUARESIZE, SQUARESIZE));
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
    narrationPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK),
        BorderFactory.createEmptyBorder(0, 3, 0, 0)));
    narrationPanel.add(narrationTextArea);
    narrationPanel.setViewportView(narrationTextArea);
    narrationPanel
        .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    narrationPanel.getVerticalScrollBar()
        .setPreferredSize(new Dimension(15, 0));

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

    // ************************LOCKED SELECT HUD***************************
    HUDLockedPanel = new JPanel(new BorderLayout());
    readyLabel = new JLabel("Press when ready", JLabel.CENTER);
    ReadyButtonListener readyButtonListener = new ReadyButtonListener();
    EndTurnButtonListener endTurnButtonListener = new EndTurnButtonListener();

    readyButton = new JButton("Ready");
    endTurnButton = new JButton("End Turn");
    endTurnButton.addActionListener(endTurnButtonListener);

    readyButton.addActionListener(readyButtonListener);
    HUDLockedMainPanel = new JPanel(new FlowLayout());
    HUDLockedMainPanel.add(readyButton);

    HUDLockedPanel.add(readyLabel, BorderLayout.NORTH);
    HUDLockedPanel.add(HUDLockedMainPanel, BorderLayout.CENTER);

    // ************************NONE SELECT HUD***************************
    HUDNoneSelectPanel = new JPanel(new BorderLayout());

    currentGold = new JLabel("Current Gold: 0", JLabel.CENTER);
    currentGold.setFont(HUD_HEADER_FONT);
    currentIncome = new JLabel("Current Income: 0", JLabel.CENTER);
    currentIncome.setFont(HUD_HEADER_FONT);

    HUDNoneSelectMiddlePanel = new JPanel(new GridLayout(4, 1));
    HUDNoneSelectMiddlePanel.add(new JLabel());
    HUDNoneSelectMiddlePanel.add(currentGold);
    HUDNoneSelectMiddlePanel.add(currentIncome);
    HUDNoneSelectMiddlePanel.add(new JLabel());
    HUDNoneSelectPanel.add(HUDNoneSelectMiddlePanel, BorderLayout.CENTER);
    JPanel temporaryPanel = new JPanel(new FlowLayout());
    temporaryPanel.add(endTurnButton);
    HUDNoneSelectPanel.add(temporaryPanel, BorderLayout.SOUTH);

    // ************************ARMY SELECT HUD***************************
    basicIcon_ARMY = new ImageIcon("src/Resources/basicIcon.png");
    attackerIcon_ARMY = new ImageIcon("src/Resources/attackerIcon.png");
    explorerIcon_ARMY = new ImageIcon("src/Resources/explorerIcon.png");
    rusherIcon_ARMY = new ImageIcon("src/Resources/rusherIcon.png");
    armyOwner_ARMY = new JLabel("Jordan's army", JLabel.CENTER);

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
    HUDArmySelectBottomPanel = new JPanel(new GridLayout(2, 2));

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

    // ************************CITY SELECT HUD***************************
    cityStatus_CITY = new JLabel("City is occupied by Jordan", JLabel.CENTER);

    cityStatus_CITY.setFont(HUD_HEADER_FONT);

    cityMainLayout = new CardLayout();
    // ******unoccupied card*******
    unoccupiedCityPanel = new JPanel(new BorderLayout());

    // ******my city card*******
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
    strengthLabelMyCity = new JLabel("Occupying army strength: ???",
        JLabel.CENTER);
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

    TrainingTextMouseListener trainingTextMouseListener = new TrainingTextMouseListener();

    trainNumberOfUnits = new JTextField();
    trainNumberOfUnits.setPreferredSize(new Dimension(30, 20));
    trainNumberOfUnits.setText("0");
    trainNumberOfUnits.addMouseListener(trainingTextMouseListener);
    trainingButton = new JButton();
    trainingButton.setText("$0");

    myCityTrainingTopPanel.add(new JLabel("Train "));
    myCityTrainingTopPanel.add(trainNumberOfUnits);
    myCityTrainingTopPanel.add(new JLabel("(up to 100) units"));
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
    trainingButtons_CITY.add(explorerRadioBut_CITY);
    trainingButtons_CITY.add(attackerRadioBut_CITY);
    trainingButtons_CITY.add(rusherRadioBut_CITY);
    trainingButton.addActionListener(unitButtonListener);

    myCityTrainingBottomPanel.add(basicRadioBut_CITY);
    myCityTrainingBottomPanel.add(explorerRadioBut_CITY);
    myCityTrainingBottomPanel.add(attackerRadioBut_CITY);
    myCityTrainingBottomPanel.add(rusherRadioBut_CITY);

    myCityPanel.add(myCityTopPanel, BorderLayout.NORTH);
    myCityPanel.add(myCityArmyPanel, BorderLayout.CENTER);
    myCityPanel.add(myCityTrainingPanel, BorderLayout.SOUTH);

    // ******enemy city card*******
    enemyCityPanel = new JPanel(new BorderLayout());

    basicIconEnemyCity = new ImageIcon("src/Resources/basicIcon.png");
    attackerIconEnemyCity = new ImageIcon("src/Resources/attackerIcon.png");
    explorerIconEnemyCity = new ImageIcon("src/Resources/explorerIcon.png");
    rusherIconEnemyCity = new ImageIcon("src/Resources/rusherIcon.png");
    strengthLabelEnemyCity = new JLabel("Occupying army strength: ???",
        JLabel.CENTER);
    numberOfUnitsLabelEnemyCity = new JLabel("Number of units: ???",
        JLabel.CENTER);

    numBasicEnemyCity = new JLabel(": ???");
    numAttackerEnemyCity = new JLabel(": ???");
    numExplorerEnemyCity = new JLabel(": ???");
    numRusherEnemyCity = new JLabel(": ???");

    enemyCityTopPanel = new JPanel(new GridLayout(2, 1));
    enemyCityTopPanel.add(strengthLabelEnemyCity);
    enemyCityTopPanel.add(numberOfUnitsLabelEnemyCity);
    enemyCityBottomPanel = new JPanel(new GridLayout(4, 2));

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

    // add all cards
    HUDCitySelectMainPanel = new JPanel(cityMainLayout);
    HUDCitySelectMainPanel.add(unoccupiedCityPanel, "unoccupied");
    HUDCitySelectMainPanel.add(myCityPanel, "my");
    HUDCitySelectMainPanel.add(enemyCityPanel, "enemy");
    cityMainLayout.show(HUDCitySelectMainPanel, "enemy");

    HUDCitySelectPanel = new JPanel(new BorderLayout());
    HUDCitySelectPanel.add(cityStatus_CITY, BorderLayout.NORTH);
    HUDCitySelectPanel.add(HUDCitySelectMainPanel, BorderLayout.CENTER);

    // **********************************RESOURCE SELECT
    // HUD************************************
    HUDResSelectPanel = new JPanel(new GridLayout(4, 1));

    resourceState = new JLabel("Resource is owned by Jordan", JLabel.CENTER);
    resourceState.setFont(HUD_HEADER_FONT);
    resourceBonus = new JLabel("Resource adds 10 income", JLabel.CENTER);

    resourceBonus.setFont(HUD_HEADER_FONT);

    HUDResSelectPanel.add(new JLabel());
    HUDResSelectPanel.add(resourceState);
    HUDResSelectPanel.add(resourceBonus);
    HUDResSelectPanel.add(new JLabel());

    // add all cards
    HUDTopPanel.add(HUDLockedPanel, "Lock");
    HUDTopPanel.add(HUDNoneSelectPanel, "None");
    HUDTopPanel.add(HUDArmySelectPanel, "Army");
    HUDTopPanel.add(HUDCitySelectPanel, "City");
    HUDTopPanel.add(HUDResSelectPanel, "Resource");
    HUDLayout.show(HUDTopPanel, "Lock");
    currentHUDCard = "Lock";

    HUDPanel.add(HUDTopPanel, BorderLayout.CENTER);
    HUDPanel.add(currentPositionPanel, BorderLayout.SOUTH);

    auxPanel.add(narrationPanel, BorderLayout.CENTER);
    auxPanel.add(HUDPanel, BorderLayout.SOUTH);

    add(auxPanel, BorderLayout.WEST);

    updateAllGridSquares();
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public class EndTurnButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == endTurnButton) {
        System.out.println(name + " ends turn");
        try {
          Siege.sendToServer(name + " ends turn");
        } catch (Exception ex) {
          System.exit(-1);
        }
      }
    }
  }

  public class ReadyButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == readyButton) {
        readyLabel.setText("Waiting...");
        System.out.println(name + " is ready");
        try {
          Siege.sendToServer(name + " is ready");
        } catch (Exception ex) {
          System.exit(-1);
        }
        readyButton.setEnabled(false);
      }
    }
  }

  public class UnitButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == basicRadioBut_CITY) {
        System.out.println("Clicked on basic unit button");
        trainingButton.setText(getPriceOf(BASIC_COST));// price of basic * num
      }
      if (e.getSource() == attackerRadioBut_CITY) {
        System.out.println("Clicked on attacker unit button");
        trainingButton.setText(getPriceOf(ATTACKER_COST));// price of attacker *
                                                          // num
      }
      if (e.getSource() == explorerRadioBut_CITY) {
        System.out.println("Clicked on explorer unit button");
        trainingButton.setText(getPriceOf(EXPLORER_COST));// price of explorer *
                                                          // num
      }
      if (e.getSource() == rusherRadioBut_CITY) {
        System.out.println("Clicked on rusher unit button");
        trainingButton.setText(getPriceOf(RUSHER_COST));// price of rusher * num
      }
      if (e.getSource() == trainingButton) {
        if (basicRadioBut_CITY.isSelected()) {
          try {
            Siege.sendToServer(name + " trains " + trainNumberOfUnits.getText()
                + " Basic units at city " + currentSelected.coord);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        } else if (attackerRadioBut_CITY.isSelected()) {
          try {
            Siege.sendToServer(name + " trains " + trainNumberOfUnits.getText()
                + " Attacker units at city " + currentSelected.coord);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        } else if (explorerRadioBut_CITY.isSelected()) {
          try {
            Siege.sendToServer(name + " trains " + trainNumberOfUnits.getText()
                + " Explorer units at city " + currentSelected.coord);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        } else if (rusherRadioBut_CITY.isSelected()) {
          try {
            Siege.sendToServer(name + " trains " + trainNumberOfUnits.getText()
                + " Rusher units at city " + currentSelected.coord);
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        } else
          System.out.println("Nothing selected");
        trainingButtons_CITY.clearSelection();
        // Player trains 50 Basic unit(s) at city (r, c)
      }
    }
  }

  public class TrainingTextMouseListener extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      trainingButtons_CITY.clearSelection();
    }
  }

  public class GridSquareMouseListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (currentHUDCard != "Lock") {
        Integer rowNumber = null;
        Integer colNumber = null;
        prevSelected = currentSelected;
        for (Integer i = 0; i < ROWS; i++)
          for (Integer j = 0; j < COLS; j++)
            if (e.getSource() == gridSquares[i][j]) {
              currentSelected = Siege.grid.getTile(new Coord(i, j));
              if (prevSelected != null && (currentSelected != prevSelected)
                  && prevSelected.getOccupant() != null) {
                // if last click is outlined
                for (Coord x : prevSelected.getOccupant().possibleMoves)
                  undoOutlineSquare(x);
              }
              rowNumber = i;
              colNumber = j;
              if (prevSelected == null || prevSelected.getOccupant() == null) {
                // last click not an army
                if (currentSelected.getOccupant() != null
                    && !currentSelected.isCity()) {// occupied by army
                  System.out.println(currentSelected);
                  printArmyPanel(currentSelected);
                  HUDLayout.show(HUDTopPanel, "Army");
                  currentHUDCard = "Army";
                  for (Coord x : currentSelected.getOccupant().possibleMoves) {
                    outlineSquare(x);
                  }
                } else if (currentSelected.isCity()) {// is a city
                  HUDLayout.show(HUDTopPanel, "City");
                  if (currentSelected.owner == -1) {
                    printUnoccupiedCityPanel(currentSelected);
                    cityMainLayout.show(HUDCitySelectMainPanel, "unoccupied");
                    currentHUDCard = "City";
                  } else if (currentSelected.owner == Siege.currentPlayer) {
                    printMyCityPanel(currentSelected);
                    cityMainLayout.show(HUDCitySelectMainPanel, "my");
                    currentHUDCard = "City";
                    for (Coord x : currentSelected.getOccupant().possibleMoves) {
                      outlineSquare(x);
                    }
                  } else {
                    printEnemyCityPanel(currentSelected);
                    cityMainLayout.show(HUDCitySelectMainPanel, "enemy");
                    currentHUDCard = "City";
                  }
                } else if (currentSelected.isResource()) {// is a resource
                  printResourcePanel(currentSelected);
                  HUDLayout.show(HUDTopPanel, "Resource");
                  currentHUDCard = "Resource";
                } else {
                  printNonePanel();
                  HUDLayout.show(HUDTopPanel, "None");
                  currentHUDCard = "None";
                }
              } else {
                // last map click was an army/my city
                // perform attack or move
                // currentSelected = mapTiles.getTile(new Coord(i, j));
                if (prevSelected == currentSelected) {// clicked again
                  currentSelected = null;
                } else if (currentSelected.getOccupant() != null) {// occupied
                  // by army
                  if (currentSelected.getOccupant().getColor() == prevSelected
                      .getOccupant().getColor()) {
                    System.out
                        .println(name + " merged army at " + prevSelected.coord
                            + " with " + currentSelected.coord);
                    try {
                      Siege.sendToServer(name + " merged army at "
                          + prevSelected.coord + " with "
                          + currentSelected.coord);
                    } catch (Exception ex) {
                      System.exit(-1);
                    }
                  } else {
                    System.out
                        .println(name
                            + "'s army at "
                            + prevSelected.coord
                            + "attacks "
                            + Siege.players[currentSelected.getOccupant().owner].name
                            + "army at " + currentSelected.coord);
                    try {
                      Siege
                          .sendToServer(name
                              + "'s army at "
                              + prevSelected.coord
                              + " attacks "
                              + Siege.players[currentSelected.getOccupant().owner].name
                              + "'s army at " + currentSelected.coord);
                    } catch (Exception ex) {
                      System.exit(-1);
                    }
                  }
                } else if (currentSelected.isCity()) {// is a city
                  System.out.println(name + "'s army at " + prevSelected.coord
                      + "attacks "
                      + Siege.players[currentSelected.getOccupant().owner].name
                      + "city at " + currentSelected.coord);
                  try {
                    Siege
                        .sendToServer(name
                            + "'s army at "
                            + prevSelected.coord
                            + " attacks "
                            + Siege.players[currentSelected.getOccupant().owner].name
                            + "'s city at " + currentSelected.coord);
                  } catch (Exception ex) {
                    System.exit(-1);
                  }
                } else {
                  System.out.println(name + " moved army from "
                      + prevSelected.coord + " to " + currentSelected.coord);
                  try {
                    Siege.sendToServer(name + " moved army from "
                        + prevSelected.coord + " to " + currentSelected.coord);
                  } catch (Exception ex) {
                    System.exit(-1);
                  }
                }
              }
            }
        if (SwingUtilities.isLeftMouseButton(e)) {
          if (currentSelected != null)
            outlineSquare(currentSelected.coord);
          if (prevSelected != null)
            undoOutlineSquare(prevSelected.coord);
          if (currentSelected == prevSelected && !currentSelected.isCity()) {
            System.out.println("Clicked on same space");
            currentSelected = null;
          }
          System.out.println("left clicked on square at: (Row: " + rowNumber
              + " Col: " + colNumber + ")");
          if (currentSelected != null)
            System.out.println("currentSelected: " + currentSelected.coord);
          else
            System.out.println("currentSelected: null");
          if (prevSelected != null)
            System.out.println("prevSelected: " + prevSelected.coord);
          else
            System.out.println("prevSelected: null");
          // outlineSquare(rowNumber, colNumber);
        }
        if (SwingUtilities.isRightMouseButton(e)) {
          System.out.println("right clicked on square at: (Row: " + rowNumber
              + " Col: " + colNumber + ")");
          // undoOutlineSquare(rowNumber, colNumber);
        }
      }
    }
 // simulate mouse entering board to update all tiles
    public void simulateMouseEntered() {
        for (Integer i = 0; i < ROWS; i++) {
            for (Integer j = 0; j < COLS; j++) {
                Tile currentTile = Siege.grid.getTile(new Coord(i, j));

                gridSquares[i][j].setBackground(gridSquares[i][j]
                        .getBackground());
                currentPositionLabel.setText("(Row: " + i + " Col: "
                        + j + ")");
                
                if (currentTile.owner != -1) {
                    for (Coord x : currentTile.getOccupant().possibleInfluences) {
                        gridSquares[x.row][x.col]
                                .setBackground(gridSquares[x.row][x.col]
                                        .getBackground());
                    }
                }
            }
        }
    }
    public void mouseEntered(MouseEvent e) {
      for (Integer i = 0; i < ROWS; i++) {
        for (Integer j = 0; j < COLS; j++) {
          if (e.getSource() == gridSquares[i][j]) {
            Tile currentTile = Siege.grid.getTile(new Coord(i, j));

            gridSquares[i][j].setBackground(gridSquares[i][j].getBackground()
                .brighter());
            currentPositionLabel.setText("(Row: " + i + " Col: " + j + ")");

            if (currentTile.getOccupant() != null && Siege.players[currentTile.getOccupant().owner].name.equals(name)) {
              for (Coord x : currentTile.getOccupant().possibleInfluences) {
                gridSquares[x.row][x.col]
                    .setBackground(gridSquares[x.row][x.col].getBackground()
                        .brighter());
                System.out.println(x + " is influenced");
              }
            }
            /*
            if (mapTiles.getTile(new Coord(i, j)).getOccupant() != null
                && mapTiles.getTile(new Coord(i, j)).getOccupant().owner != -1) {
              for (Coord x : currentTile.getOccupant().possibleInfluences) {
                gridSquares[x.row][x.col]
                    .setBackground(gridSquares[x.row][x.col].getBackground()
                        .brighter());
              }
            }
            */
          }
        }
      }
    }

    public void mouseExited(MouseEvent e) {
      for (Integer i = 0; i < ROWS; i++) {
        for (Integer j = 0; j < COLS; j++) {
          if (e.getSource() == gridSquares[i][j]) {
            Tile currentTile = Siege.grid.getTile(new Coord(i, j));
            if (currentTile.owner == -1)
              gridSquares[i][j].setBackground(stringToColor(Siege.grid.getTile(
                  new Coord(i, j)).getColor()));
            else {
              for (Coord x : currentTile.getOccupant().possibleInfluences) {
                gridSquares[x.row][x.col].setBackground(stringToColor(Siege.grid
                    .getTile(x).getColor()));
                System.out.println(Siege.grid.getTile(x).getColor());
              }
            }
          }
        }
      }
    }
  }

  void printNarration(String add) {
    narrationTextArea.setText(narrationTextArea.getText() + "\u2023 " + add
        + '\n');
    narrationTextArea.setCaretPosition(narrationTextArea.getDocument()
        .getLength());
  }

  void updateGridSquare(Coord pos) throws Exception{
    gridSquares[pos.row][pos.col].removeAll();
    Tile upTile = Siege.grid.getTile(pos);
    setBackgroundOfGridSquare(upTile);
    JLabel imageLabel = null, textLabel = new JLabel("");
    textLabel.setForeground(Color.BLACK);
    if (upTile.isCity()){
      BufferedImage cityImage = ImageIO.read(new File(getPathTo(upTile.getColor(), "City")));
      cityImage = resizeImage(SQUARESIZE, cityImage);
      String cityStrength = ""; 
      if (upTile.getOccupant() != null)
        cityStrength = String.valueOf(upTile.getOccupant().getStrength());
      imageLabel = new JLabel(new ImageIcon(cityImage));
      textLabel.setText(cityStrength);
    }
    else if (upTile.isResource()){
      BufferedImage resImage = ImageIO.read(new File(getPathTo(upTile.getColor(), "Resource")));
      resImage = resizeImage(SQUARESIZE, resImage);
      String resourceBonus = String.valueOf(upTile.income);
      imageLabel = new JLabel(new ImageIcon(resImage));
      textLabel.setText(resourceBonus);
    }
    else if (upTile.getOccupant() != null){
      String armyColor = playerToColor(upTile.getOccupant().getColor());
      BufferedImage armyImage = ImageIO.read(new File("src/Resources/" + armyColor + "Army.png"));
      armyImage = resizeImage(SQUARESIZE, armyImage);
      String armyStrength = String.valueOf(upTile.getOccupant().getStrength());
      imageLabel = new JLabel(new ImageIcon(armyImage));
      textLabel.setText(armyStrength);
    }

    if (textLabel != null){
      textLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, SQUARESIZE / 2));
      if (textLabel.getText().length() == 1)
        imageLabel.setAlignmentX(0.36f);
      if (textLabel.getText().length() == 2)
        imageLabel.setAlignmentX(0.21f);
      if (textLabel.getText().length() == 3) {
        textLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, SQUARESIZE / 2
            - SQUARESIZE / 9));
        imageLabel.setAlignmentX(0.157f);
      }
    }
    
    System.out.println(textLabel.getText());
    gridSquares[pos.row][pos.col].add(textLabel);
    if (imageLabel != null)
      gridSquares[pos.row][pos.col].add(imageLabel);


    
    return;
  }

  void setBackgroundOfGridSquare(Tile in){
    String color = in.getColor();
    if (in.isCity() || in.isResource())
      color = guessResourceBackground(in); 
    else if (color.equals("plains"))
      gridSquares[in.coord.row][in.coord.col].setBackground(Color.GREEN.darker());
    else if (color.equals("forest"))
      gridSquares[in.coord.row][in.coord.col].setBackground(Color.green.darker().darker());
    else if (color.equals("muddy"))
      gridSquares[in.coord.row][in.coord.col].setBackground(new Color(90, 90, 0));
    else if (color.equals("mountain"))
      gridSquares[in.coord.row][in.coord.col].setBackground(new Color(90, 50, 0));
    else if (color.equals("water"))
      gridSquares[in.coord.row][in.coord.col].setBackground(Color.BLUE);
    else{
      System.out.println("Unable to set background of grid square");
    }
  }
  
  String getPathTo(String playerColor, String tileType){
    String path = playerToColor(playerColor);
    return "src/resources/" + path + tileType + ".png";
  }
  
  void updateAllGridSquares() throws Exception {
    for (int i = 0; i < ROWS; i++)
      for (int j = 0; j < COLS; j++)
        updateGridSquare(new Coord(i, j));
  }

  BufferedImage resizeImage(int size, BufferedImage img) {
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

  Color stringToColor(String in) {
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

  String playerToColor(String in) {
    if (in == null)
      return "white";
    else if (in.equalsIgnoreCase("player0color"))
      return "red";
    else if (in.equalsIgnoreCase("player1color"))
      return "blue";
    else if (in.equalsIgnoreCase("player2color"))
      return "green";
    else if (in.equalsIgnoreCase("player3color"))
      return "yellow";
    else
      return "white";
  }

  String getPlayerName() {
    return name;
  }

  void outlineAroundSquare(Integer row, Integer col, Integer radius) {
    gridSquares[row][col]
        .setBorder(BorderFactory.createLineBorder(Color.BLACK));
    for (int j = 0; j <= radius; j++) {
      for (int i = 0; i <= radius - j; i++) {
        if (row + j < ROWS && col + i < COLS)
          gridSquares[row + j][col + i].setBorder(BorderFactory
              .createLineBorder(Color.BLACK));
        if (row + j < ROWS && col - i >= 0)
          gridSquares[row + j][col - i].setBorder(BorderFactory
              .createLineBorder(Color.BLACK));
        if (row - j >= 0 && col + i < COLS)
          gridSquares[row - j][col + i].setBorder(BorderFactory
              .createLineBorder(Color.BLACK));
        if (row - j >= 0 && col - i >= 0)
          gridSquares[row - j][col - i].setBorder(BorderFactory
              .createLineBorder(Color.BLACK));
      }
    }
  }

  void undoOutlineAroundSquare(Integer row, Integer col, Integer radius) {
    gridSquares[row][col].setBorder(null);
    for (int j = 0; j <= radius; j++) {
      for (int i = 0; i <= radius - j; i++) {
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

  void outlineSquare(Coord x) {
    outlineSquare(x.row, x.col);
  }

  void outlineSquare(Integer row, Integer col) {
    gridSquares[row][col]
        .setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }

  void undoOutlineSquare(Coord x) {
    undoOutlineSquare(x.row, x.col);
  }

  void undoOutlineSquare(Integer row, Integer col) {
    gridSquares[row][col].setBorder(null);
  }

  String getPriceOf(int cost) {
    String amt = trainNumberOfUnits.getText();
    int num = 0;
    Integer toReturn;
    try {
      num = Integer.parseInt(amt);
      toReturn = cost * num;
    } catch (NumberFormatException e) {
      num = 0;
      toReturn = 0;
      trainNumberOfUnits.setText("0");
    }
    if (num > MAX_ARMY_SIZE) {
      num = MAX_ARMY_SIZE;
      toReturn = cost * num;
      trainNumberOfUnits.setText(MAX_ARMY_SIZE.toString());
    }
    if (num < 0) {
      num = 0;
      toReturn = 0;
      trainNumberOfUnits.setText("0");
    }
    return "$" + toReturn.toString();
  }

  void printNonePanel() {
    currentGold.setText("Current Gold: $"
        + Siege.players[Siege.currentPlayer].getGold());
    currentIncome.setText("Current Income: $"
        + Siege.players[Siege.currentPlayer].getIncome());
  }

  void printUnoccupiedCityPanel(Tile noneTile) {
    cityStatus_CITY.setText("City is unoccupied");
  }

  void printMyCityPanel(Tile cityTile) {
    String owner = Siege.players[cityTile.owner].name;
    if (cityTile.infers != 0)
      cityStatus_CITY.setText(owner + "'s city is under siege");
    else
      cityStatus_CITY.setText("City is occupied by " + owner);
    strengthLabelMyCity.setText("Occuyping army strength: "
        + cityTile.getOccupant().getStrength());
    numberOfUnitsLabelMyCity.setText("Number of units: "
        + cityTile.getOccupant().units.size());
    Integer b = 0, e = 0, a = 0, r = 0;
    for (Unit x : cityTile.getOccupant().units) {
      if (x.name.equalsIgnoreCase("Basic"))
        b++;
      if (x.name.equalsIgnoreCase("Explorer"))
        e++;
      if (x.name.equalsIgnoreCase("Attacker"))
        a++;
      if (x.name.equalsIgnoreCase("Rusher"))
        r++;
    }
    numBasicMyCity.setText(b.toString());
    numExplorerMyCity.setText(e.toString());
    numAttackerMyCity.setText(a.toString());
    numRusherMyCity.setText(r.toString());
  }

  void printEnemyCityPanel(Tile cityTile) {
    String owner = Siege.players[cityTile.owner].name;
    if (cityTile.infers != 0)
      cityStatus_CITY.setText(owner + "'s city is under siege");
    cityStatus_CITY.setText("City is occupied by " + owner);
    strengthLabelMyCity.setText("Occuyping army strength: "
        + cityTile.getOccupant().getStrength());
    numberOfUnitsLabelMyCity.setText("Number of units: "
        + cityTile.getOccupant().units.size());
    Integer b = 0, e = 0, a = 0, r = 0;
    for (Unit x : cityTile.getOccupant().units) {
      if (x.name == "Basic")
        b++;
      if (x.name == "Explorer")
        e++;
      if (x.name == "Attacker")
        a++;
      if (x.name == "Rusher")
        r++;
    }
    numBasicEnemyCity.setText(b.toString());
    numExplorerEnemyCity.setText(e.toString());
    numAttackerEnemyCity.setText(a.toString());
    numRusherEnemyCity.setText(r.toString());
  }

  void printArmyPanel(Tile armyTile) {
    System.out.println(armyTile.owner);
    armyOwner_ARMY.setText(Siege.players[armyTile.getOccupant().owner].name
        + "'s Army");
    strength_ARMY.setText("Army strength: "
        + armyTile.getOccupant().getStrength());
    numberOfUnits_ARMY.setText("Number of units: "
        + armyTile.getOccupant().units.size());
    Integer b = 0, e = 0, a = 0, r = 0;
    for (Unit x : armyTile.getOccupant().units) {
      if (x.name == "Basic")
        b++;
      if (x.name == "Explorer")
        e++;
      if (x.name == "Attacker")
        a++;
      if (x.name == "Rusher")
        r++;
    }
    numBasic_ARMY.setText(b.toString());
    numAttacker_ARMY.setText(e.toString());
    numExplorer_ARMY.setText(a.toString());
    numRusher_ARMY.setText(r.toString());
  }

  void printResourcePanel(Tile resourceTile) {
    if (resourceTile.owner == -1)
      resourceState.setText("Resource is under conflict");
    else
      resourceState.setText("Resource is owned by "
          + Siege.players[resourceTile.owner].name);
    resourceBonus.setText("Resource adds " + resourceTile.income + "income");
  }

  void printLockedPanel() {
    HUDLockedPanel.add(new JLabel("Currently "
        + Siege.players[Siege.currentPlayer].name + "'s turn", JLabel.CENTER),
        BorderLayout.CENTER);
  }

  void removeReady() {
    HUDLockedPanel.remove(HUDLockedMainPanel);
    HUDLockedPanel.remove(readyLabel);
    printLockedPanel();
  }

  void updatePlayer() {
    if (!Siege.players[Siege.currentPlayer].name.equalsIgnoreCase(name)) {
      HUDLayout.show(HUDTopPanel, "Lock");
      currentHUDCard = "Lock";
    } else {
      HUDLayout.show(HUDTopPanel, "None");
      currentHUDCard = "None";
    }
  }

  String guessResourceBackground(Tile res) {
    int plains = 0, forest = 0, mountain = 0, muddy = 0;
    if (res.coord.row > 0) {// not top
      if (Siege.grid.getTile(new Coord(res.coord.row - 1, res.coord.col))
          .getColor() == "forest")
        forest++;
      else if (Siege.grid.getTile(new Coord(res.coord.row - 1, res.coord.col))
          .getColor() == "mountain")
        mountain++;
      else if (Siege.grid.getTile(new Coord(res.coord.row - 1, res.coord.col))
          .getColor() == "muddy")
        muddy++;
      else
        plains++;
    }
    if (res.coord.row < ROWS - 1) {// not bottom
      if (Siege.grid.getTile(new Coord(res.coord.row + 1, res.coord.col))
          .getColor() == "forest")
        forest++;
      else if (Siege.grid.getTile(new Coord(res.coord.row + 1, res.coord.col))
          .getColor() == "mountain")
        mountain++;
      else if (Siege.grid.getTile(new Coord(res.coord.row + 1, res.coord.col))
          .getColor() == "muddy")
        muddy++;
      else
        plains++;
    }
    if (res.coord.col > 0) {// not left
      if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col - 1))
          .getColor() == "forest")
        forest++;
      else if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col - 1))
          .getColor() == "mountain")
        mountain++;
      else if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col - 1))
          .getColor() == "muddy")
        muddy++;
      else
        plains++;
    }
    if (res.coord.col < COLS - 1) {// not right
      if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col + 1))
          .getColor() == "forest")
        forest++;
      else if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col + 1))
          .getColor() == "mountain")
        mountain++;
      else if (Siege.grid.getTile(new Coord(res.coord.row, res.coord.col + 1))
          .getColor() == "muddy")
        muddy++;
      else
        plains++;
    }
    if (muddy >= 2)
      return "muddy";
    else if (forest >= 2)
      return "forest";
    else if (mountain >= 2)
      return "mountain";
    else if (plains > 0)
      return "plains";
    else if (forest > 0)
      return "forest";
    else
      return "mountain";
  }

}
