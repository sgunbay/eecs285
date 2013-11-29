package com.eecs285.siegegame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GetNameDialog extends JDialog {

<<<<<<< HEAD
    JTextField inputField;
    JButton okButton;
    private static final long serialVersionUID = 1L;
=======
  JTextField inputField;
  JButton okButton;
  private static final long serialVersionUID = 1L;
  
  public GetNameDialog(JFrame mainFrame){
    super(mainFrame, "Siege", true);
    ImageIcon icon = new ImageIcon("src/Resources/castleIcon3.png");
    setIconImage(icon.getImage());
    
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
    
    topPan.add(new JLabel(title), BorderLayout.NORTH);
    topPan.add(topUpperPan, BorderLayout.CENTER);
    topPan.add(topLowerPan, BorderLayout.SOUTH);
    add(topPan, BorderLayout.CENTER);
    add(bottomPan, BorderLayout.SOUTH);
    
    buttonListener listener = new buttonListener();
    okButton.addActionListener(listener);
>>>>>>> 81907405a7c8e98662e189fdb1301b1e4e625e99

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
                    
                    //if(nameTaken(getInput())) {
                    //    System.out.println("NAME ALREADY TAKEN");
                    //    // don't let player choose this name again
                    //}                        
                    
                    // Send name to server
                    try {
                        Siege.sendToServer("NAME: " + getInput());
                        System.out.println("Name sending to server...");
                    } catch (IOException e1) {
                        System.out.println("ERROR: SENDING NAME TO SERVER");
                    }
                    setVisible(false);
                }
            }
        }

        private boolean nameTaken(String input) {
            String[] temp = Server.playerNames;
            System.out.println("In nameTaken");
            for(int i = 0; i < 4; i++) {
                System.out.println("input: " + input);
                System.out.println("i = " + i + ": " + temp[i]);
                if(temp[i].contentEquals(input))
                    return true;
            }            
            return false;
        }
    }

    public String getInput() {
        return inputField.getText();
    }
}
