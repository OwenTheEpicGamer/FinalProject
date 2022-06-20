package boggle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class BoggleGUI extends JFrame implements ActionListener {
    // initialize top main panel components
    JPanel pnlMainTop = new JPanel();
    JLabel lblBoggleTitle = new JLabel("ⓑ ⓞ ⓖ ⓖ ⓛ ⓔ");
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
    
    // initialize instructions panel components
    JPanel pnlInstructions = new JPanel();
    JLabel lblInstructions = new JLabel("ⓘⓝⓢⓣⓡⓤⓒⓣⓘⓞⓝⓢ");
    JButton btnObjectiveTitle = new JButton("Objectives");
    JLabel lblObjectiveText = new JLabel();
    JButton btnGameplayTitle = new JButton("Gameplay");
    JLabel lblGameplayText = new JLabel();
    JTextArea lblPointsTable = new JTextArea();
    
    // initialize settings panel components
    
    // initialize boggle grid panel components
    JPanel pnlBoggleGrid = new JPanel();
    JButton[][] letters = new JButton[5][5];
    char[][] boardLetters = new char[5][5];
    
    // initialize play actions panel components
    JPanel pnlPlayActions = new JPanel();
    JLabel lblWhosTurn = new JLabel();
    JLabel lblWordEntered = new JLabel("__ __ __ __ __ __");
    String wordEntered = "";
    JButton btnEnterWord = new JButton("Enter Word");
    JButton btnClearWord = new JButton("Clear Word");
    JButton btnSkip = new JButton("Skip Turn");
    JButton btnShuffle = new JButton("Shake Up the Board");
    JLabel lblResult = new JLabel("1 point earned!");
    
    // initialize play information panel components
    JPanel pnlPlayScores = new JPanel();
    JLabel lblP1 = new JLabel();
    JLabel lblP2 = new JLabel();
    JLabel lblP1Score = new JLabel("0");
    JLabel lblP2Score = new JLabel("0");
    int pointsP1 = 0;
    int pointsP2 = 0;
    int whosTurn;
    
    // initialize back panel components
    JButton btnBack = new JButton("◀ Back");
    
    // initialize variables to store user configurations
    boolean multiPlayer = false;
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
    Font fontText = new Font("MS UI Gothic",Font.PLAIN, 17);
    
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
        buildInstructionsMenu();
        buildPlayBoard(); // build play board
        
        // set up back button
        btnBack.setFont(fontText);
        btnBack.setBorder(borderButton);
        btnBack.setForeground(colourNavy);
        btnBack.setBackground(colourPeach);
        btnBack.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        btnBack.addActionListener(this);
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object command = e.getSource();
        
        if (command == btnInstructions) {
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            pnlInstructions.setVisible(true);
        }
        else if (command == btnSettings) {
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            // pnlSettings.setVisible(true);
        }
        else if (command == btnPlay) {
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
                    lblP1.setText("Player 1");
                    lblP2.setText("Player 2");
                    multiPlayer = true; // flag game mode as multiplayer
                    
                    // randomly pick first plauer
                    whosTurn = (int)(Math.random()*2)+1;
                    if (whosTurn == 1) {
                        lblWhosTurn.setText("PLAYER 1'S TURN");
                    }
                    else {
                        lblWhosTurn.setText("PLAYER 2'S TURN");
                    }
    
                }
                else {
                    lblP1.setText("You");
                    lblP2.setText("Computer");
                    multiPlayer = false; // set game mode as single player
                    // randomly pick first plauer
                    whosTurn = (int)(Math.random()*2)+1;
                    if (whosTurn == 1) {
                        lblWhosTurn.setText("    YOUR TURN   ");
                    }
                    else {
                        lblWhosTurn.setText("COMPUTER'S TURN");
                    }
    
                }
                
                // switch to play panel
                pnlMainTop.setVisible(false);
                pnlMainBtm.setVisible(false);
                //pnlBackBtn.setVisible(true);
                addPlayBoard();
            }
        }
        else if (command == btnBack) { // switch from current panel to main panel
            // quit current game played
            pnlPlayScores.setVisible(false);
            pnlBoggleGrid.setVisible(false);
            pnlPlayActions.setVisible(false);
            remove(pnlPlayScores);
            remove(pnlBoggleGrid);
            remove(pnlPlayActions);
            
            // hide instructions and settings panels
            pnlInstructions.setVisible(false);
            
            
            setLayout(lytMainMenu);
            
            // set main manu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        else if (command == btnEnterWord) {
            switchPlayers();
            resetWordEntered();
        }
        else if (command == btnClearWord) {
            resetWordEntered();
        }
        else if (command == btnSkip) {
            resetWordEntered();
            switchPlayers();
        }
        else {
            rowLoop: // name outer loop
            for (int i = 0; i < letters.length; i++) {
                for (int j = 0; j < letters[i].length; j++) {
                    if (command == letters[i][j]) { // if the letter is clicked on
                        wordEntered = wordEntered + boardLetters[i][j]; // add it to the word
                        
                        // display on board
                        lblWordEntered.setText(wordEntered);
                        letters[i][j].setBackground(colourPeach);
                        
                        break rowLoop; // break out of inner and outer for loop
                    }
                }
            }
        }
    }
    
    // set components for main menu and add to frame
    public void buildMainMenu() {
        // set up main panel components
        lblBoggleTitle.setFont(new Font("MS UI Gothic", Font.BOLD, 70));
        lblBoggleTitle.setForeground(colourDarkBlue);
        lblBoggleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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
        pnlMainTop.add(lblBoggleTitle);
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,35)));
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
    
    // set components for instructions menu and add to frame
    public void buildInstructionsMenu() {
        // set up components for instructions panel
        lblInstructions.setFont(fontTitle);
        lblInstructions.setForeground(colourDarkBlue);
        lblInstructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblInstructions.setBorder(new EmptyBorder(0,30, 0, 0));
        
        btnObjectiveTitle.setFont(fontSubtitle);
        btnObjectiveTitle.setBorder(borderButton);
        btnObjectiveTitle.setForeground(colourDarkBlue);
        btnObjectiveTitle.setBackground(colourPeach);
        btnObjectiveTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblObjectiveText.setText("<html>Find as many words as possible from a grid of random letters to earn points." +
                " The first person to reach the Tournament Score wins the game!</html>"); // use html to display line breaks
        lblObjectiveText.setFont(fontText);
        lblObjectiveText.setForeground(colourNavy);
        lblObjectiveText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnGameplayTitle.setFont(fontSubtitle);
        btnGameplayTitle.setBorder(borderButton);
        btnGameplayTitle.setForeground(colourDarkBlue);
        btnGameplayTitle.setBackground(colourBlue);
        btnGameplayTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblGameplayText.setText("<html>Starting from a randomly selected player, take turns forming words by pressing the letters on the Boggle Board. " +
                "The letters must be connected horizontally, vertically, or diagonally adjacently, and each letter can only be used once. <br/><br/>" +
                "Every two turns, you will be provided with the option to 'Shake Up The Board', " +
                "which will generate new, randomized letters on the Boggle Board. <br/><br/>" +
                "Points will be awarded for any valid word input found within the time limit and in the English dictionary. " +
                "Proper nouns do not count!</html>");
        lblGameplayText.setFont(fontText);
        lblGameplayText.setForeground(colourNavy);
        lblGameplayText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        lblPointsTable.setText("""
                WORD LENGTH            POINTS EARNED
                1 to 4                           1
                5                                  2
                6                                  3
                7                                  5
                8+                                11""");
        lblPointsTable.setFont(fontText);
        lblPointsTable.setForeground(colourNavy);
        lblPointsTable.setBounds(100, 300, 100,300);
        lblPointsTable.setEditable(false);
        lblPointsTable.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colourDarkBlue),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        lblPointsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        pnlInstructions.setLayout(new BoxLayout(pnlInstructions, BoxLayout.PAGE_AXIS));
        pnlInstructions.setMaximumSize(new Dimension(700, 700));
        pnlInstructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlInstructions.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnlInstructions.add(lblInstructions);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,30)));
        pnlInstructions.add(btnObjectiveTitle);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,15)));
        pnlInstructions.add(lblObjectiveText);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,30)));
        pnlInstructions.add(btnGameplayTitle);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,15)));
        pnlInstructions.add(lblGameplayText);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,20)));
        pnlInstructions.add(lblPointsTable);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,25)));
        pnlInstructions.add(btnBack);
        
        pnlInstructions.setVisible(false);
        add(pnlInstructions);
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
        lblWhosTurn.setForeground(colourNavy);
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
        pnlPlayScores.setVisible(true);
        pnlBoggleGrid.setVisible(true);
        pnlPlayActions.setVisible(true);
        
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
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,30)));
        pnlPlayActions.add(lblWordEntered);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,20)));
        pnlPlayActions.add(btnEnterWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,10)));
        pnlPlayActions.add(btnClearWord);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,10)));
        pnlPlayActions.add(btnSkip);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,10)));
        pnlPlayActions.add(btnShuffle);
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,30)));
        pnlPlayActions.add(lblResult);
        
        // add play board panels to frame
        add(pnlPlayScores);
        add(pnlBoggleGrid);
        add(pnlPlayActions);
    }
    
    // reset the board after a word is entered
    public void resetWordEntered() {
        // clear word entered
        wordEntered = "";
        lblWordEntered.setText("__ __ __ __ __ __");
        
        // reset board
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                letters[i][j].setBackground(colourBlue);
            }
        }
    }
    
    // add points to current player and switch to the next player
    public void switchPlayers() {
        System.out.println(wordEntered);
        
        if (whosTurn == 1 && multiPlayer) { // currently Player 1's turn
            // calculate and display points earned
            pointsP1 = alg.addPoints(wordEntered, pointsP1);
            lblP1Score.setText(Integer.toString(pointsP1));
            
            // switch to player 2
            whosTurn = 2;
            lblWhosTurn.setText("PLAYER 2'S TURN");
        }
        else if (whosTurn == 2 && multiPlayer) { // currently Player 2's turn
            // calculate and display points earned
            pointsP2 = alg.addPoints(wordEntered, pointsP2);
            lblP2Score.setText(Integer.toString(pointsP2));
            
            // switch to player 1
            whosTurn = 1;
            lblWhosTurn.setText("PLAYER 1'S TURN");
        }
        else if (whosTurn == 1 && !multiPlayer) { // currently single player's turn
            // calculate and display points earned
            pointsP1 = alg.addPoints(wordEntered, pointsP1);
            lblP1Score.setText(Integer.toString(pointsP1));
    
            // switch to computer
            whosTurn = 2;
            lblWhosTurn.setText("COMPUTER'S TURN");
        }
        else { // currently computer's turn
            // calculate and display points earned
            pointsP2 = alg.addPoints(wordEntered, pointsP2);
            lblP2Score.setText(Integer.toString(pointsP2));
    
            // switch to the single player
            whosTurn = 1;
            lblWhosTurn.setText("    YOUR TURN    ");
        }
        
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