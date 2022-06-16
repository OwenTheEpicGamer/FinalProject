import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class BoggleGUI extends JFrame implements ActionListener {
    
    // start panel 1 components
    JPanel pnlStart = new JPanel();
    JLabel lblBoggleT = new JLabel("ⓑ ⓞ ⓖ ⓖ ⓛ ⓔ");
    JRadioButton rbtnPlayer = new JRadioButton("Player vs. Player  ");
    JRadioButton rbtnComp = new JRadioButton("You vs. Computer");
    ButtonGroup groupMode = new ButtonGroup();
    JTextField txtTournScore = new JTextField("Enter Tournament Score (#)");
    JLabel lblStartPlay = new JLabel("Once the modes above have been set, click play to start!");
    JButton btnPlay = new JButton("Play");
    
    // start panel 2 components (bottom)
    JPanel pnlStart2 = new JPanel();
    JButton btnInstructions = new JButton("Instructions");
    JButton btnSettings = new JButton("Settings");
    
    // boggle grid panel components
    JPanel pnlBoggleGrid = new JPanel();
    JButton[][] letters = new JButton[5][5];
    
    // play actions panel components
    JPanel pnlPlayActions = new JPanel();
    JLabel lblWordEntered = new JLabel("_________");
    String wordEntered = "";
    JButton btnEnterWord = new JButton("Enter Word");
    JButton btnClearWord = new JButton("Clear Word");
    JButton btnSkip = new JButton("Skip Turn");
    JButton btnShuffle = new JButton("Shake Up the Board");
    JLabel lblResult = new JLabel();
    JLabel lblWhosTurn = new JLabel();
    
    // play information panel components
    JPanel pnlPlayInfo = new JPanel();
    JButton btnGoBack = new JButton(" <- Back to Main Menu");
    JLabel lblP1 = new JLabel();
    JLabel lblP2 = new JLabel();
    JLabel lblP1Score = new JLabel();
    JLabel lblP2Score = new JLabel();
    
    // instructions panel components
    
    // settings panel components
    
    // controls for switching from main to play panel
    boolean pvp = false;
    boolean pvc = false;
    boolean validTournScore = false;
    int tournScore;
    
    // borders
    Border borderButton = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#212E4E")),
            BorderFactory.createEmptyBorder(8, 12, 8, 12));
    
    // fonts
    Font fontTitle = new Font("MS UI Gothic", Font.PLAIN, 80);
    Font fontSubtitle = new Font("MS UI Gothic",Font.BOLD, 24);
    Font fontText = new Font("MS UI Gothic",Font.PLAIN, 17);
    
    // colour rgb codes
    Color colourNavy = new Color(36, 43, 62);
    Color colourDarkBlue = new Color(21, 72, 120);
    Color colourPeach = new Color(252, 209, 154);
    Color colourBlue = new Color(166, 217, 241);
    
    public BoggleGUI() {
        // set up JFrame
        setTitle("Boggle");
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900,700); // set restored down size
        setExtendedState(JFrame.MAXIMIZED_BOTH); // set default size as maximized
        
        // set up start panel 1 components
        lblBoggleT.setFont(new Font("MS UI Gothic", Font.BOLD, 70));
        lblBoggleT.setForeground(colourDarkBlue);
        lblBoggleT.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBoggleT.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    
        rbtnPlayer.setFont(fontText);
        rbtnPlayer.setForeground(colourNavy);
        rbtnPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupMode.add(rbtnPlayer);
    
        rbtnComp.setFont(fontText);
        rbtnComp.setForeground(colourNavy);
        rbtnComp.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupMode.add(rbtnComp);
        
        txtTournScore.setMaximumSize(new Dimension(170,40));
        
        btnPlay.setFont(fontSubtitle);
        btnPlay.setBorder(borderButton);
        btnPlay.setForeground(colourNavy);
        btnPlay.setBackground(colourPeach);
        btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlay.addActionListener(this);
    
        lblStartPlay.setFont(new Font("MS UI Gothic",Font.ITALIC, 16));
        lblStartPlay.setForeground(colourDarkBlue);
        lblStartPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStartPlay.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    
        // add components to first start panel in proper format
        pnlStart.setLayout(new BoxLayout(pnlStart, BoxLayout.PAGE_AXIS));
        pnlStart.add(Box.createRigidArea(new Dimension(0,230))); // center the elements vertically
        pnlStart.add(lblBoggleT);
        pnlStart.add(Box.createRigidArea(new Dimension(0,15)));
        pnlStart.add(rbtnPlayer);
        pnlStart.add(rbtnComp);
        pnlStart.add(Box.createRigidArea(new Dimension(0,10)));
        pnlStart.add(txtTournScore);
        pnlStart.add(Box.createRigidArea(new Dimension(0,33)));
        pnlStart.add(lblStartPlay);
        pnlStart.add(Box.createRigidArea(new Dimension(0,18)));
        pnlStart.add(btnPlay);
        add(pnlStart);
    
        // set up start panel 2 components
        btnInstructions.setFont(fontText);
        btnInstructions.setBorder(borderButton);
        btnInstructions.setForeground(colourNavy);
        btnInstructions.setBackground(colourBlue);
        btnInstructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInstructions.addActionListener(this);
    
        btnSettings.setFont(fontText);
        btnSettings.setBorder(borderButton);
        btnSettings.setForeground(colourNavy);
        btnSettings.setBackground(colourBlue);
        btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSettings.addActionListener(this);
        
        // add components to second start panel in proper format
        pnlStart.add(Box.createRigidArea(new Dimension(0,120)));
        pnlStart2.add(btnInstructions);
        pnlStart2.add(Box.createRigidArea(new Dimension(0,10)));
        pnlStart2.add(btnSettings);
        
        // set up boggle grid and add to panel in proper format
        pnlBoggleGrid.setLayout(new GridLayout(5, 5));
        pnlBoggleGrid.setBorder(new EmptyBorder(40, 0, 0, 0));
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                // set format
                letters[i][j] = new JButton("A");
                letters[i][j].setFont(fontTitle);
                letters[i][j].setBackground(colourBlue);
                letters[i][j].setForeground(colourNavy);
                
                // add button to panel
                letters[i][j].setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createEmptyBorder(10, 30, 10, 30)));
                pnlBoggleGrid.add(letters[i][j]);
            }
        }
        
        // set up components for play actions panel
        lblWordEntered.setFont(fontTitle);
        lblWordEntered.setForeground(colourDarkBlue);
        lblWordEntered.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblWordEntered.setBorder(BorderFactory.createEmptyBorder(0, 100, 20, 100));
    
        btnEnterWord.setFont(fontSubtitle);
        btnEnterWord.setBorder(borderButton);
        btnEnterWord.setForeground(colourNavy);
        btnEnterWord.setBackground(colourPeach);
        btnEnterWord.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEnterWord.addActionListener(this);
        
        btnClearWord.setFont(fontText);
        btnClearWord.setBorder(borderButton);
        btnClearWord.setForeground(colourNavy);
        btnClearWord.setBackground(colourPeach);
        btnClearWord.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnClearWord.addActionListener(this);
    
        btnSkip.setFont(fontText);
        btnSkip.setBorder(borderButton);
        btnSkip.setForeground(colourNavy);
        btnSkip.setBackground(colourPeach);
        btnSkip.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSkip.addActionListener(this);
    
        btnShuffle.setFont(fontText);
        btnShuffle.setBorder(borderButton);
        btnShuffle.setForeground(colourNavy);
        btnShuffle.setBackground(colourPeach);
        btnShuffle.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShuffle.addActionListener(this);
        
        // add components to play actions panel in proper format
        pnlPlayActions.setLayout(new BoxLayout(pnlPlayActions, BoxLayout.PAGE_AXIS));
        pnlPlayActions.add(lblWordEntered);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(btnEnterWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(btnClearWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(btnSkip);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(btnShuffle);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(lblResult);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(100,10)));
        pnlPlayActions.add(lblWhosTurn);
        
        add(pnlStart2);
        add(pnlBoggleGrid);
        add(pnlPlayActions);
        
        pnlBoggleGrid.setVisible(false);
        pnlPlayActions.setVisible(false);
        setVisible(true);
        
        
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.equals("Play")) {
            // determine if the tournament score entered is valid
            if (txtTournScore.getText().matches("-?\\d+")) { // if tournament score is an integer
                tournScore = Integer.parseInt(txtTournScore.getText());
                
                if(tournScore > 0 ) { // if tournament score is greater than 0
                    validTournScore = true;
                }
            }
            if (!rbtnPlayer.isSelected() && !rbtnComp.isSelected() && !validTournScore) { // if invalid tournament score and no mode selected
                lblStartPlay.setText("Please choose a mode and enter a valid tournament score.");
            }
            else if (!rbtnPlayer.isSelected() && !rbtnComp.isSelected()) { // if no mode selected
                lblStartPlay.setText("Please choose a mode.");
            }
            else if (!validTournScore) { // if invalid tournament score
                lblStartPlay.setText("Please enter a valid tournament score.");
            }
            else { // if all input is valid
                // determine player mode chosen
                if (rbtnPlayer.isSelected()) {
                    pvp = true;
                }
                else {
                    pvc = true;
                }
                
                // switch to play panel
                setLayout(new FlowLayout());
                pnlStart.setVisible(false);
                pnlStart2.setVisible(false);
                pnlBoggleGrid.setVisible(true);
                pnlPlayActions.setVisible(true);
            }
        }
    }
    
    public static void main(String[] args) {
        new BoggleGUI();
    }
}