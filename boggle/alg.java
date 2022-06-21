package boggle;

import java.io.*;
import java.util.*;

public class alg {
    private final int SIZE = 5;
    private boolean[][] occupied;
    private char[][] grid;
    private Set<String> dict;
    private Set<String> prefixes;
    private Set<String> wordList;
    private int player1Points;
    private int minimumLength;

    public alg() {
        // constructor method, makes all the variables and calls generateGrid();
        occupied = new boolean[SIZE][SIZE];
        grid = new char[SIZE][SIZE];
        generateDict();
        openPrefixes();
        System.out.println("dict size: " + dict.size());
        System.out.println("prefix size: " + prefixes.size());

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }

        //System.out.println("ENTIRE DICTIONARY");
        //printSet(dict);
        //System.out.println("ALL POSSIBLE WORDS");
        //printSet(wordList);
    }

    public void printSet(Set<String> set) {
        for (String s : set) {
            System.out.println(s);
        }

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

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                int randomLetter = rand.nextInt(6);
                int randomDice = rand.nextInt(dice.size());
                grid[row][column] = dice.get(randomDice).charAt(randomLetter);
            }
        }
    }

    private void generateDict() {
        //dict = new HashSet<String>();
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
        wordList = new HashSet<String>();
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                String startLetter = String.valueOf(grid[row][column]);
                checkPossibilities(startLetter, row, column, grid);
            }
        }
    }

    private void checkPossibilities(String word, int row, int column, char[][] grid) {
        if (word.length() > 15) {
            return;
        }
        if (word.length() < 9 && word.length() > 1 && !prefixes.contains(word)) {
            return;
        }

        //System.out.println(word);
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
        if (word.length() == 0) {
            return 0;
        }
        else if (word.length() <= 4) {
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
    
    public String computerGetsWord(int d) {
        String easy = "aaaaaaaaaaaaaaaa";
        String med = "a";
        String hard = "a";
        
        for (String s : wordList) {
            if (d == 3 && s.length() > hard.length()) {
                hard = s;
            }
            if (d == 1 && s.length() < easy.length()) {
                easy = s;
            }
            if (d == 4 && (s.length() == 4 || s.length() == 3 || s.length() == 5)) {
                med = s;
            }
        }
        return switch (d) {
            case 1 -> easy;
            case 2 -> med;
            case 3 -> hard;
            default -> "nothing";
        };
        
    }
    
    // get word list
    public Set<String> getWordList() {
        return wordList;
    }

}



