import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class BoggleGUI extends JFrame implements ActionListener {
    // initialize top main panel components
    JPanel pnlMainTop = new JPanel();
    JLabel lblBoggleT = new JLabel("ⓑ ⓞ ⓖ ⓖ ⓛ ⓔ");
    JRadioButton rbtnPlayer = new JRadioButton("Player vs. Player  ");
    JRadioButton rbtnComp = new JRadioButton("You vs. Computer");
    ButtonGroup groupMode = new ButtonGroup();
    JTextField txtTournScore = new JTextField("Enter Tournament Score (#)");
    JLabel lblStartPlay = new JLabel("Once the modes above have been set, click play to start!");
    JButton btnPlay = new JButton("Play");
    
    // initialize bottom main panel components
    JPanel pnlMainBtm = new JPanel();
    JButton btnInstructions = new JButton("Instructions");
    JButton btnSettings = new JButton("Settings");
    
    // initialize boggle grid panel components
    JPanel pnlBoggleGrid = new JPanel();
    JButton[][] letters = new JButton[5][5];
    char[][] boardLetters = new char[5][5];
    
    // initialize play actions panel components
    JPanel pnlPlayActions = new JPanel();
    JLabel lblWhosTurn = new JLabel("PLAYER 1'S TURN");
    JLabel lblWordEntered = new JLabel("_________");
    String wordEntered = "";
    JButton btnEnterWord = new JButton("Enter Word");
    JButton btnClearWord = new JButton("Clear Word");
    JButton btnSkip = new JButton("Skip Turn");
    JButton btnShuffle = new JButton("Shake Up the Board");
    JLabel lblResult = new JLabel("1 point earned!");
    
    // initialize play information panel components
    JPanel pnlPlayScores = new JPanel();
    JLabel lblP1 = new JLabel("Player 1");
    JLabel lblP2 = new JLabel("Computer");
    JLabel lblP1Score = new JLabel("0");
    JLabel lblP2Score = new JLabel("0");
    
    // initialize instructions panel components
    
    // initialize settings panel components
    
    // initialize back panel components
    JPanel pnlBackBtn = new JPanel();
    JButton btnBack = new JButton("◀ Back");
    
    // initialize variables to store user configurations
    boolean pvp = false;
    boolean pvc = false;
    int tournScore;
    boolean validTournScore = false;
    
    
    // initialize layouts
    BoxLayout lytMainMenu = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS);
    
    // initialize borders
    Border borderButton = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#212E4E")),
            BorderFactory.createEmptyBorder(8, 12, 8, 12));
    
    // initialize fonts
    Font fontButton = new Font("MS UI Gothic", Font.PLAIN, 80);
    Font fontTitle = new Font("MS UI Gothic", Font.PLAIN, 50);
    Font fontSubtitle = new Font("MS UI Gothic",Font.BOLD, 24);
    Font fontText = new Font("MS UI Gothic",Font.BOLD, 17);
    
    // initialize colour rgb codes
    Color colourNavy = new Color(36, 43, 62);
    Color colourDarkBlue = new Color(21, 72, 120);
    Color colourPeach = new Color(252, 209, 154);
    Color colourBlue = new Color(166, 217, 241);
    
    // constructor
    public BoggleGUI(char[][] boardLetters) {
        // store randomly generated letters
        for (int i = 0; i < boardLetters.length; i++) {
            for (int j = 0; j < boardLetters[i].length; j++) {
                this.boardLetters[i][j] = boardLetters[i][j];
            }
        }
        
        // set up JFrame
        setTitle("Boggle");
        setLayout(lytMainMenu);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1380,740); // set restored down size
        // setExtendedState(JFrame.MAXIMIZED_BOTH); // set default size as maximized
        
        buildMainMenu(); // build main menu
        buildPlayBoard(); // build play board
        
        // set up back button
        btnBack.setFont(fontText);
        btnBack.setBorder(borderButton);
        btnBack.setForeground(colourNavy);
        btnBack.setBackground(colourPeach);
        btnBack.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        btnBack.addActionListener(this);
        pnlBackBtn.setBorder(BorderFactory.createEmptyBorder(620, 0, 20, 0));
        //pnlBackBtn.add(btnBack);
        //add(pnlBackBtn);
        pnlBackBtn.setVisible(false);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object command = e.getSource();
        
        if (command == btnPlay) {
            // determine if the tournament score entered is valid
            if (txtTournScore.getText().matches("-?\\d+")) { // if tournament score is an integer
                tournScore = Integer.parseInt(txtTournScore.getText()); // store value
                
                // flag as valid tournament score if it is greater than 0
                if (tournScore > 0 ) {
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
                pnlMainTop.setVisible(false);
                pnlMainBtm.setVisible(false);
                //pnlBackBtn.setVisible(true);
                addPlayBoard();
            }
        }
        
        // switch from current panel to main panel
        else if (command == btnBack) {
            // quit current game played
            remove(pnlPlayScores);
            remove(pnlBoggleGrid);
            remove(pnlPlayActions);
            
            // hide instructions and settings panels
    
            //pnlBackBtn.setVisible(false); // hide back button
            setLayout(lytMainMenu);
            
            // set main manu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        
        else {
            rowLoop: // name outer loop
            for (int i = 0; i < letters.length; i++) {
                for (int j = 0; j < letters[i].length; j++) {
                    if (command == letters[i][j]) {
                        letters[i][j].setBackground(colourPeach);
                        wordEntered = wordEntered + boardLetters[i][j];
                        break rowLoop; // break out of inner and outer for loop
                    }
                }
            }
        }
    }
    
    // set components for main menu and add to frame
    public void buildMainMenu() {
        // set up main panel components
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
        
        // add components to first start panel in proper format
        pnlMainTop.setLayout(new BoxLayout(pnlMainTop, BoxLayout.Y_AXIS));
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,230))); // center the elements vertically
        pnlMainTop.add(lblBoggleT);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,15)));
        pnlMainTop.add(rbtnPlayer);
        pnlMainTop.add(rbtnComp);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,10)));
        pnlMainTop.add(txtTournScore);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,33)));
        pnlMainTop.add(lblStartPlay);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,18)));
        pnlMainTop.add(btnPlay);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,100)));
        
        // add components to second start panel in proper format
        pnlMainBtm.add(btnInstructions);
        pnlMainBtm.add(Box.createRigidArea(new Dimension(0,10)));
        pnlMainBtm.add(btnSettings);
        
        // add main menu panels to frame
        add(pnlMainTop);
        add(pnlMainBtm);
    }
    
    // set formatting for play board components
    public void buildPlayBoard() {
        // set up play instructions panel components
        lblP1.setFont(fontSubtitle);
        lblP1.setForeground(colourDarkBlue);
        lblP1.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        lblP1Score.setFont(fontTitle);
        lblP1Score.setForeground(colourNavy);
        lblP1Score.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        lblP2.setFont(fontSubtitle);
        lblP2.setForeground(colourDarkBlue);
        lblP2.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        lblP2Score.setFont(fontTitle);
        lblP2Score.setForeground(colourNavy);
        lblP2Score.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // set up boggle grid buttons
        pnlBoggleGrid.setLayout(new GridLayout(5, 5));
        pnlBoggleGrid.setBorder(new EmptyBorder(40, 0, 0, 0));
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                // set format
                letters[i][j] = new JButton("A");
                letters[i][j].setFont(fontButton);
                letters[i][j].setBackground(colourBlue);
                letters[i][j].setForeground(colourNavy);
                letters[i][j].setBorder(new CompoundBorder(BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                letters[i][j].addActionListener(this);
            }
        }
        
        // set up play actions panel components
        lblWhosTurn.setFont(fontSubtitle);
        lblWhosTurn.setForeground(colourDarkBlue);
        lblWhosTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblWhosTurn.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
        
        lblWordEntered.setFont(fontTitle);
        lblWordEntered.setForeground(colourDarkBlue);
        lblWordEntered.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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
        
        lblResult.setFont(fontSubtitle);
        lblResult.setForeground(colourDarkBlue);
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);
        
    }
    
    // add play board components to frame
    public void addPlayBoard() {
        // clear previously played game
        pnlPlayScores.removeAll();
        pnlBoggleGrid.removeAll();
        pnlPlayActions.removeAll();
        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
    
        // add components to play scores panel in proper format
        pnlPlayScores.setLayout(new BoxLayout(pnlPlayScores, BoxLayout.PAGE_AXIS));
        pnlPlayScores.setAlignmentY(Component.CENTER_ALIGNMENT);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,200)));
        pnlPlayScores.add(lblP1);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,10)));
        pnlPlayScores.add(lblP1Score);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,50)));
        pnlPlayScores.add(lblP2);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,10)));
        pnlPlayScores.add(lblP2Score);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,160)));
        pnlPlayScores.add(btnBack);
        
        // add buttons to Boggle grid
        pnlBoggleGrid.setLayout(new GridLayout(5, 5));
        pnlBoggleGrid.setBorder(new EmptyBorder(50, 70, 50, 0));
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                letters[i][j].setText(String.valueOf(boardLetters[i][j]));
                pnlBoggleGrid.add(letters[i][j]);
            }
        }
        
        // add components to play actions panel in proper format
        pnlPlayActions.setLayout(new BoxLayout(pnlPlayActions, BoxLayout.PAGE_AXIS));
        pnlPlayActions.add(lblWhosTurn);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,10)));
        pnlPlayActions.add(lblWordEntered);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,30)));
        pnlPlayActions.add(btnEnterWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,10)));
        pnlPlayActions.add(btnClearWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,10)));
        pnlPlayActions.add(btnSkip);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,10)));
        pnlPlayActions.add(btnShuffle);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(40,30)));
        pnlPlayActions.add(lblResult);
        
        // add play board panels to frame
        add(pnlPlayScores);
        add(pnlBoggleGrid);
        add(pnlPlayActions);
    }
    
    public static void main(String[] args) {
        char[][] letters =
                {{'a', 'b','c', 'd', 'e'},
                        {'a', 'b','c', 'd', 'e'},
                        {'a', 'b','c', 'd', 'e'},
                        {'a', 'b','c', 'd', 'e'},
                        {'a', 'b','c', 'd', 'e'}};
        BoggleGUI myBoggle = new BoggleGUI(letters);
    }
}