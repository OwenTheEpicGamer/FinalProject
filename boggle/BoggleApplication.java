/*
 * Application Class (GUI and Main) Boggle Culimating Project
 * Purpose: Run a complete Boggle application for users
 * Contributors: Sarina, Kian, Owen
 * Date: June 6 - June 20
 * Description: Includes code for front-end GUI, as well as interaction with the AI class for a complete application.
 */

package boggle;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BoggleApplication extends JFrame implements ActionListener {
    // create object algorithm
    BoggleAI algorithm = new BoggleAI();
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
    JSlider sldrTimed = new JSlider(10, 60, 15);
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
    JButton btnShuffle = new JButton("Shake Up the Board");
    JLabel lblWordSumbitted = new JLabel(" ");
    JLabel lblResult = new JLabel(" ");
    int timesPassed = 0;
    boolean isWinner = false;

    // timer components and variables
    Timer gameTimer = new Timer(1000, this);
    JButton btnPauseTimer = new JButton("Pause");
    JLabel lblTimer = new JLabel();
    int currentTime;
    Time time;
    SimpleDateFormat timerFormat = new SimpleDateFormat("mm:ss");
    boolean bellRung;
    
    // initialize play scores panel components
    JPanel pnlPlayScores = new JPanel();
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
    int timeLimit = 15;
    int compDifficulty = 0;
    int minWordLength = 3;
    
    // sound components
    Clip clipAudio;
    Clip clip;
    Timer musicTimer = new Timer(1000, this);
    int timeInSeconds;
    long clipLength;
    
    // initialize layouts
    BoxLayout lytBoxPage = new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS);
    BoxLayout lytBoxX = new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS);
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
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // constructor
    public BoggleApplication() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
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
        setSize(screenSize.width, screenSize.height); // set restored down size
        setExtendedState(JFrame.MAXIMIZED_BOTH);

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
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            pnlInstructions.setVisible(true);
        }
        // switch from instructions panel to main panel
        else if (source == btnBack) {
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
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
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            pnlMainTop.setVisible(false);
            pnlMainBtm.setVisible(false);
            pnlSettings.setVisible(true);
        }
        // if state of music check box in settings is changed
        else if (source == chkMusic) {
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            if (chkMusic.isSelected()) { // show option to set genre if user wants music
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
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
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
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
    
            // store user configurations
            hasSoundEffects = chkSound.isSelected();
            hasMusic = chkMusic.isSelected();
            musicGenre = sldrMusic.getValue();
            playTimed = chkTimed.isSelected();
            timeLimit = sldrTimed.getValue();
            compDifficulty = sldrDifficulty.getValue();
            minWordLength = sldrMinWordLength.getValue();
            
            if (hasMusic) { // play music of chosen genre if music is chosen
                try {
                    clip.stop();
                    playBackgroundMusic(musicGenre);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            else { // stop background music if music is not chosen
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
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
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
                resetScores();
                addPlayBoard();
            }
        }
        // shake up the board
        else if (source == btnShuffle) {
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            addPlayBoard();
        }
        // switch from play panel to main panel
        else if (source == btnQuit) {
            resetOccupied = true;
            
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            gameTimer.stop();
            
            // hide current game played
            pnlPlayScores.setVisible(false);
            pnlBoggleGrid.setVisible(false);
            pnlPlayActions.setVisible(false);
            
            // set up confirmation to quit the game after it ends
            if (isWinner) {
                if (whosTurn == 1 && multiPlayer) {
                    lblConfirm.setText("Player 1 wins! [" + pointsP1 + " vs. " + pointsP2 + "]");
                }
                else if (whosTurn == 2 && multiPlayer) {
                    lblConfirm.setText("Player 2 wins! [" + pointsP1 + " vs. " + pointsP2 + "]");
                }
                else if (whosTurn == 1) {
                    lblConfirm.setText("You win! [" + pointsP1 + " vs. " + pointsP2 + "]");
                }
                else {
                    lblConfirm.setText("Computer wins! [" + pointsP1 + " vs. " + pointsP2 + "]");
                }
                
                btnYes.setText("Back to Main Menu");
                btnYes.setVisible(true);
                
                btnContinue.setVisible(false);
                btnNo.setVisible(false);
            }
            // set up confirmation to quit game before it ends
            else {
                lblConfirm.setText("Are you sure you want to quit the game?");
                btnYes.setText("Yes, quit");
                btnYes.setVisible(true);
    
                btnContinue.setVisible(false);
                btnNo.setVisible(true);
            }
            setLayout(lytBoxPage);
            pnlPlayBuffer.setVisible(true);
        }
        else if (source == btnYes) { // switch from play to main menu
            resetOccupied = true;
            
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            pnlPlayBuffer.setVisible(false); // hide buffer panel
            
            // remove panels from frame
            remove(pnlPlayScores);
            remove(pnlBoggleGrid);
            remove(pnlPlayActions);
            remove(pnlPlayBuffer);
            
            // set main menu as visible
            pnlMainTop.setVisible(true);
            pnlMainBtm.setVisible(true);
        }
        else if (source == btnNo) { // user does not want to quit game
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            setLayout(lytBoxX); // revert frame layout
            
            pnlPlayBuffer.setVisible(false);
            pnlPlayScores.setVisible(true);
            pnlBoggleGrid.setVisible(true);
            pnlPlayActions.setVisible(true);
    
            gameTimer.start(); // continue timer
    
        }
        // if user wants to enter a word on play board
        else if (source == btnEnterWord) {
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            isValidWord = (algorithm.getWordList()).contains(wordEntered); // determine if word is in the valid word list

            if (isValidWord && wordEntered.length() >= minWordLength) {
                // hide time passed
                timesPassed = 0; // reset number of times passed
                btnShuffle.setVisible(false);
                
                // reset timer
                if (playTimed) {
                    currentTime = timeLimit;
                    time = new Time(currentTime* 1000L); // create time object
                    lblTimer.setText(timerFormat.format(time)); // format the time in mm:ss
                }
    
                algorithm.usedWord(wordEntered);
                switchPlayers();
                
                if (isWinner) {
                    btnQuit.doClick();
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
    
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
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
            
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/bellsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            lblResult.setText("Turn skipped.");
            lblWordSumbitted.setText(" ");
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
                currentTime = timeLimit;
                time = new Time(currentTime* 1000L); // create time object
                lblTimer.setText(timerFormat.format(time)); // format the time in mm:ss
                btnSkip.doClick();
            }
        }
        // if timer is paused
        else if (source == btnPauseTimer) {
            resetOccupied = true;
    
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            gameTimer.stop();
            
            setLayout(lytBoxPage);
            
            // hide current game played
            pnlPlayScores.setVisible(false);
            pnlBoggleGrid.setVisible(false);
            pnlPlayActions.setVisible(false);
    
            // set up panel to unpause game
            lblConfirm.setText("Game paused at " + lblTimer.getText() + ".");
            btnContinue.setVisible(true);
            btnYes.setVisible(false);
            btnNo.setVisible(false);
            pnlPlayBuffer.setVisible(true);
        }
        // if game is continued after being paused
        else if (source == btnContinue) {
            resetOccupied = true;
            
            // play button clicked sound effect
            if (hasSoundEffects) {
                try {
                    playSound("Sounds/buttonsound.wav");
                }
                catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                    ex.printStackTrace();
                }
            }
            
            setLayout(lytBoxX); // revert layout
    
            pnlPlayBuffer.setVisible(false);
            pnlPlayScores.setVisible(true);
            pnlBoggleGrid.setVisible(true);
            pnlPlayActions.setVisible(true);
            
            gameTimer.start();
        }
        // loop music
        else if (source == musicTimer) {
            timeInSeconds++;
        
            if (timeInSeconds == clipLength) {
                musicTimer.stop();
                timeInSeconds = 0;
                try {
                    if (hasMusic) {
                        clip.stop();
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
    
                        // play button clicked sound effect
                        if (hasSoundEffects) {
                            try {
                                playSound("Sounds/buttonsound.wav");
                            }
                            catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
                                ex.printStackTrace();
                            }
                        }
    
                        lblResult.setText(" "); // clear results label
                        lblWordSumbitted.setText(" "); // clear word submitted label
                        
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
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,(int)(screenSize.height*0.2)))); // center the elements vertically
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
        pnlMainTop.add(Box.createRigidArea(new Dimension(0,(int)(screenSize.height*0.1))));
        
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
        // set up components for settings menu
        // settings title
        lblSettings.setFont(fontTitle);
        lblSettings.setForeground(colourDarkBlue);
        lblSettings.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSettings.setBorder(new EmptyBorder(0,10, 0, 0));
        
        // check box for sound effects
        chkSound.setFont(fontSubtitle);
        chkSound.setForeground(colourDarkBlue);
        chkSound.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // check box for background music
        chkMusic.setFont(fontSubtitle);
        chkMusic.setForeground(colourDarkBlue);
        chkMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMusic.addActionListener(this);
        
        // label for setting music genre
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
        
        // slider to select music genre
        sldrMusic.setBorder(new EmptyBorder(5, 0, 20,0));
        sldrMusic.setMajorTickSpacing(1);
        sldrMusic.setFont(fontText);
        sldrMusic.setPaintTicks(true);
        sldrMusic.setLabelTable(hashLblMusic);
        sldrMusic.setPaintLabels(true);
        
        // check box for playing timed
        chkTimed.setFont(fontSubtitle);
        chkTimed.setForeground(colourDarkBlue);
        chkTimed.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkTimed.addActionListener(this);
        
        // label for setting time limit
        lblTimed.setFont(fontText);
        lblTimed.setForeground(colourNavy);
        lblTimed.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // slider to set time limit
        sldrTimed.setBorder(new EmptyBorder(5, 0, 20,0));
        sldrTimed.setMajorTickSpacing(10);
        sldrTimed.setMinorTickSpacing(1);
        sldrTimed.setFont(fontText);
        sldrTimed.setPaintTicks(true);
        sldrTimed.setPaintLabels(true);
        
        // label for setting difficulty
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
        
        // slider for choosing difficulty level of computer
        sldrDifficulty.setMajorTickSpacing(1);
        sldrDifficulty.setFont(fontText);
        sldrDifficulty.setPaintTicks(true);
        sldrDifficulty.setLabelTable(hashLblDifficulty);
        sldrDifficulty.setPaintLabels(true);
        
        // label for setting the minimum word length
        lblminWordLength.setFont(fontSubtitle);
        lblminWordLength.setForeground(colourDarkBlue);
        lblminWordLength.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // slider to choose minimum word length
        sldrMinWordLength.setMajorTickSpacing(1);
        sldrMinWordLength.setFont(fontText);
        sldrMinWordLength.setPaintTicks(true);
        sldrMinWordLength.setPaintLabels(true);
        
        // button to save settings
        btnSave.setFont(fontText);
        btnSave.setBorder(borderButton);
        btnSave.setForeground(colourNavy);
        btnSave.setBackground(colourPeach);
        btnSave.addActionListener(this);
        
        // add components to settings panel in proper format
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
        
        lblWordSumbitted.setFont(fontText);
        lblWordSumbitted.setForeground(colourNavy);
        lblWordSumbitted.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lblConfirm.setFont(fontSubtitle);
        lblConfirm.setForeground(colourDarkBlue);
        lblConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        btnContinue.setFont(fontText);
        btnContinue.setBorder(borderButton);
        btnContinue.setForeground(colourNavy);
        btnContinue.setBackground(colourPeach);
        btnContinue.addActionListener(this);
        btnContinue.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        btnYes.setFont(fontSubtitle);
        btnYes.setBorder(borderButton);
        btnYes.setForeground(colourNavy);
        btnYes.setBackground(colourPeach);
        btnYes.addActionListener(this);
        btnYes.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        btnNo.setFont(fontSubtitle);
        btnNo.setBorder(borderButton);
        btnNo.setForeground(colourNavy);
        btnNo.setBackground(colourPeach);
        btnNo.addActionListener(this);
        btnNo.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    }
    
    // reset scores
    public void resetScores() {
        pointsP1 = 0;
        pointsP2 = 0;
        lblP1Score.setText("0");
        lblP2Score.setText("0");
        isWinner = false;
    }
    
    // add play board components to frame
    public void addPlayBoard() {
        // clear previous board
        pnlPlayScores.removeAll();
        pnlBoggleGrid.removeAll();
        pnlPlayActions.removeAll();
        pnlPlayBuffer.removeAll();
        resetWordEntered();
        currentTime = timeLimit;
        lblResult.setText(" ");
        
        // generate random board of characters
        algorithm.generateGrid(boardLetters, 5);
        
        algorithm.generateWordlist(boardLetters); // generate wordlist for new board
        
        // set visibility of play panels
        pnlPlayScores.setVisible(true);
        pnlBoggleGrid.setVisible(true);
        pnlPlayActions.setVisible(true);
        pnlPlayBuffer.setVisible(false);
    
        setLayout(lytBoxX);
        
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
        if (playTimed) { // add timer if playing timed
            pnlPlayScores.add(lblTimer);
            pnlPlayScores.add(Box.createRigidArea(new Dimension(90,5)));
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
        pnlPlayActions.add(Box.createRigidArea(new Dimension(20,5)));
        pnlPlayActions.add(lblWordSumbitted);
        
        btnShuffle.setVisible(false); // set default invisible
        
        // add components to play buffer panel in proper format
        pnlPlayBuffer.setLayout(new BoxLayout(pnlPlayBuffer, BoxLayout.PAGE_AXIS));
        pnlPlayBuffer.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlPlayBuffer.setAlignmentY(Component.CENTER_ALIGNMENT);
        pnlPlayBuffer.setBorder(new EmptyBorder(270, 0, 0, 0));
        pnlPlayBuffer.add(lblConfirm);
        pnlPlayBuffer.add(Box.createRigidArea(new Dimension(0,30)));
        pnlPlayBuffer.add(btnContinue);
        pnlPlayBuffer.add(Box.createRigidArea(new Dimension(0,10)));
        pnlPlayBuffer.add(btnYes);
        pnlPlayBuffer.add(Box.createRigidArea(new Dimension(0,10)));
        pnlPlayBuffer.add(btnNo);
        pnlPlayBuffer.add(Box.createRigidArea(new Dimension(0,10)));
        
        // add play board panels to frame
        add(pnlPlayScores);
        add(pnlBoggleGrid);
        add(pnlPlayActions);
        add(pnlPlayBuffer);
    }
    
    // reset the board after a word is entered
    public void resetWordEntered() {
        // clear word entered
        wordEntered = "";
        lblWordEntered.setText("__ __ __ __ __ __");
        
        // reset board colours
        for (int i = 0; i < letters.length; i++) {
            for (int j = 0; j < letters[i].length; j++) {
                letters[i][j].setBackground(colourBlue);
            }
        }
    }
    
    // add points to current player and switch to the next player's turn
    public void switchPlayers() {
        pointsEarned = algorithm.pointsEarned(wordEntered); // store points earned for the word
        System.out.println(tournScore);
        
        if (whosTurn == 1 && multiPlayer) { // currently Player 1's turn
            pointsP1 += pointsEarned; // add points
    
            if (pointsP1 >= tournScore) { // user won game
                isWinner = true;
            }
            else if (pointsEarned > 0) { // user earned points
                // display points earned
                lblP1Score.setText(Integer.toString(pointsP1));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to player 2
                whosTurn = 2;
                lblWhosTurn.setText("PLAYER 2'S TURN");
            }
            else { // user did not earn points
                // switch to player 2
                whosTurn = 2;
                lblWhosTurn.setText("PLAYER 2'S TURN");
            }
            
        }
        else if (whosTurn == 2 && multiPlayer) { // currently Player 2's turn
            pointsP2 += pointsEarned; // add points
            
            if (pointsP2 >= tournScore) { // user won game
                isWinner = true;
            }
            else if (pointsEarned > 0) { // user earned points
                // display points earned
                lblP2Score.setText(Integer.toString(pointsP2));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to player 1
                whosTurn = 1;
                lblWhosTurn.setText("PLAYER 1'S TURN");
            }
            else { // user did not earn points
                // switch to player 1
                whosTurn = 1;
                lblWhosTurn.setText("PLAYER 1'S TURN");
            }
            
        }
        else if (whosTurn == 1) { // currently single player's turn
            pointsP1 += pointsEarned; // add points
    
            if (pointsP1 >= tournScore) { // user won game
                isWinner = true;
            }
            else if (pointsEarned > 0) { // user earned points
                // display points earned
                lblP1Score.setText(Integer.toString(pointsP1));
                lblResult.setText(pointsEarned + " point(s) earned!");
    
                // switch to computer
                whosTurn = 2;
                lblWhosTurn.setText("COMPUTER'S  TURN");
                compTurn(); // run computer's move
            }
            else { // user did not earn points
                // switch to computer
                whosTurn = 2;
                lblWhosTurn.setText("COMPUTER'S  TURN");
                compTurn(); // run computer's move
            }

        }
        else { // currently computer's turn
            pointsP2 += pointsEarned; // add points
    
           if (pointsP2 >= tournScore) { // user won game
                isWinner = true;
            }
            else if (pointsEarned > 0) { // user earned points
                // display score and word played
                lblP2Score.setText(Integer.toString(pointsP2));
                lblResult.setText("Computer earned " + pointsEarned + " point(s)!");
                lblWordSumbitted.setText("Played word " + wordEntered);
    
                // switch to the single player
                whosTurn = 1;
                lblWhosTurn.setText("    YOUR TURN    ");
            }
            else { // user did not earn points
                // switch to the single player
                whosTurn = 1;
                lblWhosTurn.setText("    YOUR TURN    ");
            }
        }
        
    }
    
    // play computer's turn
    public void compTurn() {
        wordEntered = algorithm.computerGetsWord(compDifficulty, minWordLength);
        
        if ((int)(Math.random()*4) == 0) { // one in four chances that computer will pass
            btnSkip.doClick();
            lblResult.setText("Computer skipped turn.");
        }
        else { // three in four chances that computer will enter a word
            btnEnterWord.doClick();
        }
    }
    
    // play background music
    public void playBackgroundMusic(int musicGenre) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        musicTimer = new Timer(1000, this);
        musicTimer.start();
        
        // randomly choose a song within the selected genre
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
        
        // play audio
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        clipLength = clip.getMicrosecondLength()/1000000;
    }
    
    // play sound effects
    public void playSound(String fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
        clipAudio = AudioSystem.getClip();
        clipAudio.open(audioInputStream);
        clipAudio.start();
    }
    
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        BoggleApplication myBoggle = new BoggleApplication(); // instantiate application
    }
}