/**
 * Algorithm Class for Boggle Culminating Project
 * Purpose: Manage the algorithmic and AI-based functions for the GUI
 * Contributors: Kian, Owen, Sarina
 * Date: June 10 - June 20
 * Handles searching capabilities and grid manipulation
 */

package boggle;

import java.io.*;
import java.util.*;

public class BoggleAI {
    private final int SIZE = 5;
    private boolean[][] occupied;
    private char[][] grid;
    // hashsets must be globally declared
    private Set<String> dict;
    private Set<String> wordList;
    private Set<String> prefixes;

    public BoggleAI() {
        // constructor method, makes all the variables and calls generateGrid();
        occupied = new boolean[SIZE][SIZE];
        grid = new char[SIZE][SIZE];
        generateDict();
        openPrefixes();
    }
    
    public void generateGrid(char[][] grid, int size) {
        // makes grid for the first time randomly
        ArrayList<String> dice = new ArrayList<String>(List.of(
                "aaafrs", "aaeeee", "aafirs", "adennn", "aeeeem",
                "aeegmu", "aegmnn", "afirsy", "bjkqxz", "ccnstw",
                "ceiilt", "ceilpt", "ceipst", "ddlnor", "dhhlor",
                "dhhnot", "dhlnor", "eiiitt", "emottt", "ensssu",
                "fiprsy", "gorrvw", "hiprry", "nootuw", "ooottu"));

        Random rand = new Random();
        // creates the grid using randomized dice and removes dice after single use
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                int randomLetter = rand.nextInt(6);
                int randomDice = rand.nextInt(dice.size());
                grid[row][column] = dice.get(randomDice).charAt(randomLetter);
                dice.remove(randomDice);
            }
        }
    }

    private void generateDict() {
        //deserializes hashset from file
        try {
            FileInputStream readFile = new FileInputStream("dictHashSet");
            ObjectInputStream fileInputStream = new ObjectInputStream(readFile);

            dict = (HashSet<String>) fileInputStream.readObject();

            fileInputStream.close();
            readFile.close();
        } catch (IOException | ClassNotFoundException e) {
            e.getStackTrace();
        }
    }

    private void openPrefixes() {
        //deserializes prefixes from file
        try {
            FileInputStream readFile = new FileInputStream("prefixesHashSet");
            ObjectInputStream fileInputStream = new ObjectInputStream(readFile);

            prefixes = (HashSet<String>) fileInputStream.readObject();

            fileInputStream.close();
            readFile.close();
        } catch (IOException | ClassNotFoundException e) {
            e.getStackTrace();
        }
    }

    public void generateWordlist(char[][] grid) {
        // runs the recursive call on every letter on the board
        wordList = new HashSet<String>();
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                String startLetter = String.valueOf(grid[row][column]);
                checkPossibilities(startLetter, row, column, grid);
            }
        }
    }

    private void checkPossibilities(String word, int row, int column, char[][] grid) {
        // terminates words too long or whos prefixes cannot exist
        if (word.length() > 15) {
            return;
        }
        if (word.length() < 9 && word.length() > 1 && !prefixes.contains(word)) {
            return;
        }

        occupied[row][column] = true;
        if (dict.contains(word)) {
            wordList.add(word);
        }

        int[][] neighbours = returnNeighbours(row, column);
        for (int[] coord : neighbours) {
            int nextRow = coord[0];
            int nextColumn = coord[1];
            String newWord = word + grid[nextRow][nextColumn];
            checkPossibilities(newWord, nextRow, nextColumn, grid);
        }

        occupied[row][column] = false;
    }


    public int[][] returnNeighbours(int row, int column) {
        // returns all valid neighbours for a cell
        ArrayList<int[]> neighbours = new ArrayList<int[]>(Arrays.asList(
                new int[]{row + 1, column}, new int[]{row - 1, column},
                new int[]{row, column + 1}, new int[]{row, column - 1},
                new int[]{row + 1, column + 1}, new int[]{row - 1, column - 1},
                new int[]{row + 1, column - 1}, new int[]{row - 1, column + 1}));

        for (int i = 0; i < neighbours.size(); i++) {
            int r = neighbours.get(i)[0];
            int c = neighbours.get(i)[1];
            if (r >= SIZE || c >= SIZE || r < 0 || c < 0 || occupied[r][c]) {
                neighbours.remove(i);
                i--;
            }
        }

        int[][] valid = new int[neighbours.size()][2];
        for (int i = 0; i < neighbours.size(); i++) {
            for (int j = 0; j < 2; j++) {
                valid[i][j] = neighbours.get(i)[j];
            }
        }
        return valid;
    }

    public int pointsEarned(String word) {
        // calculates the total of points earned by a word
        if (word.length() == 0) {
            return 0;
        } else if (word.length() <= 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    public String computerGetsWord(int d, int minLength) {
        // returns a word chosen by the computer
        String word = d == 0 ? "aaaaaaaaaaaaaaa" : "a";
        int sL;
        int wL;

        for (String s : wordList) {
            sL = s.length();
            wL = word.length();

            if (d == 2 && sL > wL) {
                word = s;
            }
            if (d == 0 && sL < wL && sL >= minLength) {
                word = s;
            }
            if (d == 1 && (sL == 3 || sL == 4) && sL >= minLength) {
                return s;
            }
        }
        return word;
    }

    // getter method
    public Set<String> getWordList() {
        return wordList;
    }

    // remove used word from word list
    public void usedWord(String word) {
        wordList.remove(word);
    }

}



