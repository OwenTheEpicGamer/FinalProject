package boggle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class BoggleGUI extends JFrame implements ActionListener {
    // create object algorithm
    alg algorithm = new alg();
    boolean[][] occupied = new boolean[5][5];
    int currentRow = 0;
    int currentCol = 0;
    boolean resetOccupied = false;
    
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
    JTextArea txtaPointsTable = new JTextArea();
    JButton btnBack = new JButton("◀ Back");
    
    // initialize settings panel components
    JPanel pnlSettings = new JPanel();
    JLabel lblSettings = new JLabel("ⓢⓔⓣⓣⓘⓝⓖⓢ");
    JCheckBox chkSound = new JCheckBox("Sound effects", true);
    JCheckBox chkMusic = new JCheckBox("Music", true);
    JLabel lblMusic = new JLabel("Set the genre:");
    JSlider sldrMusic = new JSlider(0, 2, 0);
    Hashtable<Integer, JLabel> hashLblMusic = new Hashtable<Integer, JLabel>();
    JCheckBox chkTimed = new JCheckBox("Play timed", true);
    JLabel lblTimed = new JLabel("Set time limit (seconds):");
    JSlider sldrTimed = new JSlider(10, 60, 45);
    JLabel lblDifficulty = new JLabel("Set computer difficulty:");
    JSlider sldrDifficulty = new JSlider(0, 2, 0);
    Hashtable<Integer, JLabel> hashLblDifficulty = new Hashtable<Integer, JLabel>();
    JLabel lblminWordLength = new JLabel("Set minimum word length:");
    JSlider sldrMinWordLength = new JSlider(1, 4, 3);
    JButton btnSave = new JButton("Save and Exit");
    
    // initialize boggle grid panel components
    JPanel pnlBoggleGrid = new JPanel();
    JButton[][] letters = new JButton[5][5];
    char[][] boardLetters = new char[5][5];
    
    // initialize play actions panel components
    JPanel pnlPlayActions = new JPanel();
    JLabel lblWhosTurn = new JLabel();
    JLabel lblWordEntered = new JLabel("__ __ __ __ __ __");
    String wordEntered = ""; // stores the word entered
    boolean isValidWord;
    JButton btnEnterWord = new JButton("Enter Word");
    JButton btnClearWord = new JButton("Clear Word");
    JButton btnSkip = new JButton("Skip Turn");
    int skipCount = 0;
    JButton btnShuffle = new JButton("Shake Up the Board");
    JLabel lblResult = new JLabel(" ");
    int timesPassed = 0;
    boolean isWinner = false;
    // initialize play scores panel components
    JPanel pnlPlayScores = new JPanel();
    
    Timer gameTimer = new Timer(1000, this);
    JButton btnPauseTimer = new JButton("Pause/Play");
    JLabel lblTimer = new JLabel();
    int currentTime;
    Time time;
    SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss");
    boolean bellRung;
    
    JLabel lblP1 = new JLabel();
    JLabel lblP2 = new JLabel();
    JLabel lblP1Score = new JLabel("0");
    JLabel lblP2Score = new JLabel("0");
    JButton btnQuit = new JButton("◀ Quit");
    int pointsP1 = 0; // points of player 1
    int pointsP2 = 0; // points of player 2
    int pointsEarned;
    int whosTurn; // stores the value of the current player (1 for player 1, 2 for player 2)
    
    // initialize components for buffer panel
    JPanel pnlPlayBuffer = new JPanel();
    JLabel lblConfirm = new JLabel();
    JButton btnContinue = new JButton("Continue");
    JButton btnYes = new JButton("Yes, quit");
    JButton btnNo = new JButton("No");
    
    // initialize variables to store user configurations
    boolean multiPlayer = false;
    int tournScore;
    boolean validTournScore = false;
    boolean hasSoundEffects = true;
    boolean hasMusic = true;
    int musicGenre = 0;
    boolean playTimed = true;
    int timeLimit = 45;
    int compDifficulty = 0;
    int minWordLength = 3;
    
    // sound
    Clip clipAudio;
    Clip clip;
    Timer musicTimer = new Timer(1000, this);
    int timeInSeconds;
    long clipLength;
    
    // initialize layouts
    BoxLayout lytBoxPage = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS);
    
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
    Color colourDarkBlue = new Color(13, 72, 128);
    Color colourPeach = new Color(252, 209, 154);
    Color colourBlue = new Color(166, 217, 241);
    
    // constructor
    public BoggleGUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        // store randomly generated letters
        for (int i = 0; i < boardLetters.length; i++) {
            for (int j = 0; j < boardLetters[i].length; j++) {
                this.boardLetters[i][j] = boardLetters[i][j];
            }
        }
        
        // set up frame
        setTitle("Boggle");
        setLayout(lytBoxPage);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1380,740); // set restored down size
        
        // set up panels
        buildMainMenu(); // build main menu
        buildInstructionsMenu(); // build instructions menu
        buildSettingsMenu(); // build settings menu
        buildPlayBoard(); // build play board
    
        playBackgroundMusic(musicGenre);
    
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // switch from main to instructions panel
        if (source == btnInstructions) {
            resetOccupied = true;
            
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            pnlInstructions.setVisible(true);
        }
        // switch from instructions panel to main panel
        else if (source == btnBack) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            // hide instructions panel
            pnlInstructions.setVisible(false);
            
            // set main manu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        // switch from main to settings panel
        else if (source == btnSettings) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            pnlSettings.setVisible(true);
        }
        // if state of music check box in settings is changed
        else if (source == chkMusic) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            if (chkMusic.isSelected()) { // show option to set  genre if user wants music
                lblMusic.setVisible(true);
                sldrMusic.setVisible(true);
            }
            else { // hide option to set genre if user does not want music
                lblMusic.setVisible(false);
                sldrMusic.setVisible(false);
            }
        }
        // if state of timer check box in settings is changed
        else if (source == chkTimed) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
    
            if (chkTimed.isSelected()) { // show option to set time limit if user wants to play timed
                lblTimed.setVisible(true);
                sldrTimed.setVisible(true);
            }
            else { // hide option to set time limit if user wants to play untimed
                lblTimed.setVisible(false);
                sldrTimed.setVisible(false);
            }
        }
        // switch from settings panel to main panel
        else if (source == btnSave) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
    
            // store user configurations
            hasSoundEffects = chkSound.isSelected();
            hasMusic = chkMusic.isSelected();
            musicGenre = sldrMusic.getValue();
            playTimed = chkTimed.isSelected();
            timeLimit = sldrTimed.getValue();
            compDifficulty = sldrDifficulty.getValue();
            minWordLength = sldrMinWordLength.getValue();
            
            if (hasMusic) {
                try {
                    clip.stop();
                    playBackgroundMusic(musicGenre);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                clip.stop();
            }
            
            // hide settings panel
            pnlSettings.setVisible(false);
            
            // set main manu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        // switch from main to play panel
        else if (source == btnPlay) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
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
                    // set user as first player
                    whosTurn = 1;
                    lblWhosTurn.setText("    YOUR TURN   ");
                }
                
                lblStartPlay.setText("Once the modes above have been set, click play to start!");
                
                // switch to play panel
                pnlMainTop.setVisible(false);
                pnlMainBtm.setVisible(false);
                addPlayBoard();
            }
        }
        // shake up the board
        else if (source == btnShuffle) {
            addPlayBoard();
        }
        // switch from play panel to main panel
        else if (source == btnQuit) {
            resetOccupied = true;
            
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            gameTimer.stop();
            
            // quit current game played
            pnlPlayScores.setVisible(false);
            pnlBoggleGrid.setVisible(false);
            pnlPlayActions.setVisible(false);
            remove(pnlPlayScores);
            remove(pnlBoggleGrid);
            remove(pnlPlayActions);
            
            setLayout(lytBoxPage); // revert frame layout
            
            // set main manu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        // if user wants to enter a word on play board
        else if (source == btnEnterWord) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            isValidWord = (algorithm.getWordList()).contains(wordEntered); // determine if word is in the valid word list

            if (isValidWord && wordEntered.length() >= minWordLength) {
                // hide time passed
                timesPassed = 0; // reset number of times passed
                btnShuffle.setVisible(false);
                
                // reset timer
                if (playTimed) {
                    time = new Time(currentTime*1000L); // create time object
                    lblTimer.setText(timerFormat.format(time)); // show time in format mm:ss
                    gameTimer.start(); // start timer
                }
    
                algorithm.usedWord(wordEntered);
                switchPlayers();
                
                if (isWinner) {
                    btnQuit.doClick();
                    if (whosTurn == 1 && multiPlayer) {
                        lblStartPlay.setText("Player 1 wins!");
                    }
                    else if (whosTurn == 2 && multiPlayer) {
                        lblStartPlay.setText("Player 2 wins!");
                    }
                    else if (whosTurn == 1) {
                        lblStartPlay.setText("You win!");
                    }
                    else {
                        lblStartPlay.setText("Computer wins!");
                    }
                }
                else {
                    resetWordEntered();
                }
            }
            else {
                lblResult.setText("Invalid word, try again.");
                resetWordEntered();
            }
        }
        // if used wants to clear the word on play board
        else if (source == btnClearWord) {
            resetOccupied = true;
    
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
            
            resetWordEntered();
        }
        // if user wants to skip their turn
        else if (source == btnSkip) {
            resetOccupied = true;
            timesPassed++;
            if (timesPassed >= 2) {
                btnShuffle.setVisible(true);
            }
            try {
                playSound("Sounds/buttonsound.wav");
            }
            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                ex.printStackTrace();
            }
    
            lblResult.setText("Turn skipped.");
            resetWordEntered();
            switchPlayers();
        }
        // if timer is on
        else if (source == gameTimer && gameTimer.isRunning()) {
            if (currentTime > 0) {
                currentTime--;
                time = new Time(currentTime* 1000L); // create time object
                lblTimer.setText(timerFormat.format(time)); // format the time in mm:ss
            }
            else {
                if(!bellRung) {
                    try {
                        playSound("Sounds/bellsound.wav");
                    } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                        ex.printStackTrace();
                    }
                    bellRung = true;
                }
                currentTime = timeLimit;
                time = new Time(currentTime* 1000L); // create time object
                lblTimer.setText(timerFormat.format(time)); // format the time in mm:ss
                btnSkip.doClick();
                bellRung = false;
            }
        }
        // if timer is paused
        else if (source == btnPauseTimer && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        // if timer is played
        else if (source == btnPauseTimer && !gameTimer.isRunning()) {
            gameTimer.start();
        }
        else if (source == musicTimer) {
            timeInSeconds++;
            System.out.println(timeInSeconds);
        
            if(timeInSeconds == clipLength) {
                System.out.println("boop");
                musicTimer.stop();
                timeInSeconds = 0;
                try {
                    if (hasMusic) {
                        playBackgroundMusic(musicGenre);
                    }
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        // if user clicks a letter on Boggle grid
        else {
            // name outer loop
            for (int i = 0; i < letters.length; i++) {
                for (int j = 0; j < letters[i].length; j++) {
                    if (source == letters[i][j]) { // if the letter is clicked on
                        try {
                            playSound("Sounds/buttonsound.wav");
                        }
                        catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                            ex.printStackTrace();
                        }
    
                        lblResult.setText(" "); // clear results label
                        
                        if (occupiedEmpty()) {
                            wordEntered += boardLetters[i][j]; // add it to the word
                            // display on board
                            lblWordEntered.setText(wordEntered);
                            letters[i][j].setBackground(colourPeach);
    
                            currentRow = i;
                            currentCol = j;
                            occupied[i][j] = true;
                        }
                        
                        if (occupied[i][j]) {
                            return;
                        }
                        for (int[] n : algorithm.returnNeighbours(i, j)) {
                            // if the current cell is found in the neighbours of the clicked cell
                            if ((n[0] == currentRow && n[1] == currentCol)) {
                                wordEntered += boardLetters[i][j]; // add it to the word
                                lblWordEntered.setText(wordEntered);
                                letters[i][j].setBackground(colourPeach);
                                currentRow = i;
                                currentCol = j;
                                occupied[i][j] = true;
                            }
                        }
                    }
                }
            }
        }
        
        if (resetOccupied) {
            occupied = new boolean[5][5];
            resetOccupied = false;
        }
    }
    
    public boolean occupiedEmpty() {
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                if (occupied[i][j]) {
                    return false;
                }
            }
        }
        return true;
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
        lblInstructions.setBorder(new EmptyBorder(0,40, 0, 0));
        
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
        
        txtaPointsTable.setText("""
                WORD LENGTH            POINTS EARNED
                1 to 4                           1
                5                                  2
                6                                  3
                7                                  5
                8+                                11""");
        txtaPointsTable.setFont(fontText);
        txtaPointsTable.setForeground(colourNavy);
        txtaPointsTable.setBounds(100, 300, 100,300);
        txtaPointsTable.setEditable(false);
        txtaPointsTable.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colourDarkBlue),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        txtaPointsTable.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnBack.setFont(fontText);
        btnBack.setBorder(borderButton);
        btnBack.setForeground(colourNavy);
        btnBack.setBackground(colourPeach);
        btnBack.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        btnBack.addActionListener(this);
        
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
        pnlInstructions.add(txtaPointsTable);
        pnlInstructions.add(Box.createRigidArea(new Dimension(0,25)));
        pnlInstructions.add(btnBack);
        
        pnlInstructions.setVisible(false);
        add(pnlInstructions);
    }
    
    // set components for settings menu and add to frame
    public void buildSettingsMenu() {
        
        lblSettings.setFont(fontTitle);
        lblSettings.setForeground(colourDarkBlue);
        lblSettings.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSettings.setBorder(new EmptyBorder(0,10, 0, 0));
        
        chkSound.setFont(fontSubtitle);
        chkSound.setForeground(colourDarkBlue);
        chkSound.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        chkMusic.setFont(fontSubtitle);
        chkMusic.setForeground(colourDarkBlue);
        chkMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMusic.addActionListener(this);
        
        lblMusic.setFont(fontText);
        lblMusic.setForeground(colourNavy);
        lblMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // set up music genre slider labels and add to hashtable
        JLabel[] lblGenres = new JLabel[3];
        lblGenres[0] = new JLabel("Classical");
        lblGenres[1] = new JLabel("Upbeat");
        lblGenres[2] = new JLabel("Relaxing");
        for (int i = 0; i < lblGenres.length; i++) {
            lblGenres[i].setFont(fontText);
            hashLblMusic.put(i, lblGenres[i]);
        }
        
        sldrMusic.setBorder(new EmptyBorder(5, 0, 20,0));
        sldrMusic.setMajorTickSpacing(1);
        sldrMusic.setFont(fontText);
        sldrMusic.setPaintTicks(true);
        sldrMusic.setLabelTable(hashLblMusic);
        sldrMusic.setPaintLabels(true);
        
        chkTimed.setFont(fontSubtitle);
        chkTimed.setForeground(colourDarkBlue);
        chkTimed.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkTimed.addActionListener(this);
        
        lblTimed.setFont(fontText);
        lblTimed.setForeground(colourNavy);
        lblTimed.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sldrTimed.setBorder(new EmptyBorder(5, 0, 20,0));
        sldrTimed.setMajorTickSpacing(10);
        sldrTimed.setMinorTickSpacing(1);
        sldrTimed.setFont(fontText);
        sldrTimed.setPaintTicks(true);
        sldrTimed.setPaintLabels(true);
        
        lblDifficulty.setFont(fontSubtitle);
        lblDifficulty.setForeground(colourDarkBlue);
        lblDifficulty.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // set up difficulty slider labels and add to hashtable
        JLabel[] lblDifficulties = new JLabel[3];
        lblDifficulties[0] = new JLabel("Easy");
        lblDifficulties[1] = new JLabel("Medium");
        lblDifficulties[2] = new JLabel("Hard");
        for (int i = 0; i < lblDifficulties.length; i++) {
            lblDifficulties[i].setFont(fontText);
            hashLblDifficulty.put(i, lblDifficulties[i]);
        }
        
        sldrDifficulty.setMajorTickSpacing(1);
        sldrDifficulty.setFont(fontText);
        sldrDifficulty.setPaintTicks(true);
        sldrDifficulty.setLabelTable(hashLblDifficulty);
        sldrDifficulty.setPaintLabels(true);
        
        lblminWordLength.setFont(fontSubtitle);
        lblminWordLength.setForeground(colourDarkBlue);
        lblminWordLength.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sldrMinWordLength.setMajorTickSpacing(1);
        sldrMinWordLength.setFont(fontText);
        sldrMinWordLength.setPaintTicks(true);
        sldrMinWordLength.setPaintLabels(true);
        
        btnSave.setFont(fontText);
        btnSave.setBorder(borderButton);
        btnSave.setForeground(colourNavy);
        btnSave.setBackground(colourPeach);
        btnSave.addActionListener(this);
        
        pnlSettings.setLayout(new BoxLayout(pnlSettings, BoxLayout.PAGE_AXIS));
        pnlSettings.setMaximumSize(new Dimension(430, 700));
        pnlSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlSettings.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnlSettings.add(lblSettings);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSettings.add(chkSound);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSettings.add(chkMusic);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 7)));
        pnlSettings.add(lblMusic);
        pnlSettings.add(sldrMusic);
        pnlSettings.add(chkTimed);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 7)));
        pnlSettings.add(lblTimed);
        pnlSettings.add(sldrTimed);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 20)));
        pnlSettings.add(lblDifficulty);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSettings.add(sldrDifficulty);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlSettings.add(lblminWordLength);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSettings.add(sldrMinWordLength);
        pnlSettings.add(Box.createRigidArea(new Dimension(0, 30)));
        pnlSettings.add(btnSave);
        
        pnlSettings.setVisible(false);
        add(pnlSettings);
    }
    
    // set formatting for play board components
    public void buildPlayBoard() {
        // set up play instructions panel components
        lblTimer.setFont(fontTitle);
        lblTimer.setForeground(colourNavy);
        lblTimer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnPauseTimer.setFont(fontText);
        btnPauseTimer.setForeground(colourNavy);
        btnPauseTimer.setBackground(colourPeach);
        btnPauseTimer.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        btnPauseTimer.addActionListener(this);
        
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
        
        btnQuit.setFont(fontText);
        btnQuit.setBorder(borderButton);
        btnQuit.setForeground(colourNavy);
        btnQuit.setBackground(colourPeach);
        btnQuit.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        btnQuit.addActionListener(this);
        
        // set up boggle grid buttons
        pnlBoggleGrid.setLayout(new GridLayout(5, 5));
        pnlBoggleGrid.setBorder(new EmptyBorder(40, 0, 0, 0));
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                // set format
                letters[i][j] = new JButton();
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
    
        lblResult.setFont(fontSubtitle);
        lblResult.setForeground(colourDarkBlue);
        lblResult.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        btnContinue.setFont(fontText);
        btnContinue.setBorder(borderButton);
        btnContinue.setForeground(colourNavy);
        btnContinue.setBackground(colourPeach);
        btnContinue.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContinue.addActionListener(this);
    
        btnYes.setFont(fontText);
        btnYes.setBorder(borderButton);
        btnYes.setForeground(colourNavy);
        btnYes.setBackground(colourPeach);
        btnYes.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnYes.addActionListener(this);
    }
    
    // add play board components to frame
    public void addPlayBoard() {
        // clear previously played game
        pnlPlayScores.removeAll();
        pnlBoggleGrid.removeAll();
        pnlPlayActions.removeAll();
        pointsP1 = 0;
        pointsP2 = 0;
        lblP1Score.setText("0");
        lblP2Score.setText("0");
        resetWordEntered();
        currentTime = timeLimit;
        lblResult.setText(" ");
        
        // generate random board of characters
        algorithm.generateGrid(boardLetters, 5);
        
        algorithm.generateWordlist(boardLetters); // generate wordlist for new board
        
        pnlPlayScores.setVisible(true);
        pnlBoggleGrid.setVisible(true);
        pnlPlayActions.setVisible(true);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        
        // start timer
        if (playTimed) {
            time = new Time(currentTime*1000L); // create time object
            lblTimer.setText(timerFormat.format(time)); // show time in format mm:ss
            gameTimer.start(); // start timer
        }
        
        // add components to play scores panel in proper format
        pnlPlayScores.setLayout(new BoxLayout(pnlPlayScores, BoxLayout.PAGE_AXIS));
        pnlPlayScores.setAlignmentY(Component.CENTER_ALIGNMENT);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,20)));
        
        if (playTimed) {
            pnlPlayScores.add(lblTimer);
            pnlPlayScores.add(btnPauseTimer);
        }
        
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,80)));
        pnlPlayScores.add(lblP1);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,10)));
        pnlPlayScores.add(lblP1Score);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,50)));
        pnlPlayScores.add(lblP2);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,10)));
        pnlPlayScores.add(lblP2Score);
        pnlPlayScores.add(Box.createRigidArea(new Dimension(90,160)));
        pnlPlayScores.add(btnQuit);
        
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
        
        btnShuffle.setVisible(false);
        
        // add components to play buffer panel in proper format
        pnlPlayBuffer.setLayout(new BoxLayout(pnlPlayBuffer, BoxLayout.PAGE_AXIS));
        pnlPlayBuffer.add(lblConfirm);
        pnlPlayBuffer.add(btnContinue);
        pnlPlayBuffer.add(btnYes);
        pnlPlayBuffer.add(btnNo);
    
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
        pointsEarned = algorithm.pointsEarned(wordEntered);
        
        if (whosTurn == 1 && multiPlayer) { // currently Player 1's turn
            // see if winner
            if (pointsEarned + pointsP1 >= tournScore) {
                isWinner = true;
            }
            // display points earned
            else if (pointsEarned != 0) {
                pointsP1 += pointsEarned;
                lblP1Score.setText(Integer.toString(pointsP1));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to player 2
                whosTurn = 2;
                lblWhosTurn.setText("PLAYER 2'S TURN");
            }
        }
        else if (whosTurn == 2 && multiPlayer) { // currently Player 2's turn
            // see if winner
            if (pointsEarned + pointsP2 >= tournScore) {
                isWinner = true;
            }
            // display points earned
            else if (pointsEarned != 0) {
                pointsP2 += pointsEarned;
                lblP2Score.setText(Integer.toString(pointsP2));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to player 1
                whosTurn = 1;
                lblWhosTurn.setText("PLAYER 1'S TURN");
            }
        }
        else if (whosTurn == 1) { // currently single player's turn
            // see if winner
            if (pointsEarned + pointsP2 >= tournScore) {
                isWinner = true;
            }
            // display points earned
            else if (pointsEarned != 0) {
                pointsP1 += pointsEarned;
                lblP1Score.setText(Integer.toString(pointsP1));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to computer
                whosTurn = 2;
                lblWhosTurn.setText("COMPUTER'S  TURN");
                compTurn();
            }
        }
        else { // currently computer's turn
            // see if winner
            if (pointsEarned + pointsP2 >= tournScore) {
                isWinner = true;
            }
            // display points earned
            else if (pointsEarned != 0) {
                pointsP2 += pointsEarned;
                lblP2Score.setText(Integer.toString(pointsP2));
                lblResult.setText("<html>Computer played " + wordEntered + ", earning " + pointsEarned + " points.<html>");
    
                // switch to the single player
                whosTurn = 1;
                lblWhosTurn.setText("    YOUR TURN    ");
            }
        }
        
    }
    
    // play computer's word
    public void compTurn() {
        wordEntered = algorithm.computerGetsWord(compDifficulty, minWordLength);
        btnEnterWord.doClick();
    }
    
    public void playBackgroundMusic(int musicGenre) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        musicTimer = new Timer(1000, this);
        musicTimer.start();
        
        String soundName = "";
         Random rand = new Random();
         if(musicGenre == 0) {
             int num = rand.nextInt(3) + 1;
             soundName = "Sounds/classical" + num + ".wav";
         }
         if(musicGenre == 1) {
             int num = rand.nextInt(3) + 1;
             soundName = "Sounds/upbeat" + num + ".wav";
         }
         if(musicGenre == 2) {
             int num = rand.nextInt(6) + 1;
             soundName = "Sounds/relaxing" + num + ".wav";
         }
         
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
         clip = AudioSystem.getClip();
         clip.open(audioInputStream);
         clip.start();
         clipLength = clip.getMicrosecondLength()/1000000;
    }
    
    public void playSound(String fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
        clipAudio = AudioSystem.getClip();
        clipAudio.open(audioInputStream);
        clipAudio.start();
    }
    
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        BoggleGUI myBoggle = new BoggleGUI();
    }
}