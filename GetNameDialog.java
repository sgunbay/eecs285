package com.eecs285.siegegame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//gets the name from the user and displays custom title card
public class GetNameDialog extends JDialog {

  JTextField inputField;
  JButton okButton;
  private static final long serialVersionUID = 1L;

  public GetNameDialog(JFrame mainFrame) {
    super(mainFrame, "", true);

    ImageIcon title = new ImageIcon("src/Resources/titlecard.png");

    JPanel topUpperPan = new JPanel(new FlowLayout());
    JPanel topLowerPan = new JPanel(new FlowLayout());
    JPanel topPan = new JPanel(new BorderLayout());
    JPanel bottomPan = new JPanel(new FlowLayout());
    setLayout(new BorderLayout());
    okButton = new JButton("OK");
    bottomPan.add(okButton);
    topUpperPan.add(new JLabel("Player name:", SwingConstants.LEFT));
    inputField = new JTextField(40);
    topLowerPan.add(inputField);
    //fill topPan
    topPan.add(new JLabel(title), BorderLayout.NORTH);
    topPan.add(topUpperPan, BorderLayout.CENTER);
    topPan.add(topLowerPan, BorderLayout.SOUTH);
    add(topPan, BorderLayout.CENTER);
    add(bottomPan, BorderLayout.SOUTH);

    buttonListener listener = new buttonListener();
    okButton.addActionListener(listener);

    pack();
    setResizable(false);
    setLocation(400, 200);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    getRootPane().setDefaultButton(okButton);
    setVisible(true);
  }

  public class buttonListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == okButton) {
        // only close window if user has entered a name
        if (getInput().compareTo("") != 0) {
          // Send name to server
          try {
            Siege.sendToServer("NAME: " + getInput());
            System.out.println("Sending player name (" + getInput()
                + ") to server..");
          } catch (Exception e1) {
            System.out.println("ERROR: SENDING NAME TO SERVER");
          }
          setVisible(false);
        }
      }
    }
  }

  public String getInput() {
    return inputField.getText();
  }
}
