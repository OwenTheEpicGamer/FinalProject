package boggle;

import java.io.*;
import java.util.*;

public class alg {
    private final int SIZE;
    private final int MAX_WORD_SIZE = 11;
    private boolean[][] occupied;
    private char[][] grid;
    private Set<String> dict;
    private Set<String> prefixes;
    private Set<String> wordList;
    private int player1Points;
    private int minimumLength;

    public alg(int SIZE) {
        // constructor method, makes all the variables and calls generateGrid();
        this.SIZE = SIZE;
        occupied = new boolean[SIZE][SIZE];
        grid = new char[SIZE][SIZE];
        generateGrid();
        generateDict();
        openPrefixes();
        generateWordlist();

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }

        //System.out.println("ENTIRE DICTIONARY");
        //printSet(dict);
        System.out.println("ALL POSSIBLE WORDS");
        printSet(wordList);
    }

    private void printSet(Set<String> set) {
        for (String s : set) {
            System.out.println(s);
        }

    }

    private void generateGrid() {
        // makes grid for the first time randomly
        String[] dice = new String[]{
                "s", "e", "s", "n", "m",
                "u", "n", "y", "z", "w",
                "t", "t", "t", "r", "r",
                "t", "r", "t", "t", "u",
                "y", "w", "y", "w", "u"};

        Random rand = new Random();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                int index = (row * SIZE + column) % dice.length;
                // index will iterate through dice array and loop back to start when out of bounds
                int randomInt = rand.nextInt(dice[0].length());
                grid[row][column] = dice[index].charAt(randomInt);
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

    private void generateWordlist() {
        wordList = new HashSet<String>();
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                String startLetter = String.valueOf(grid[row][column]);
                checkPossibilities(startLetter, row, column);
            }
        }
    }

    private void checkPossibilities(String word, int row, int column) {
        if (word.length() > MAX_WORD_SIZE) {
            return;
        }
        /*if (word.length() <= 4 && word.length() > 1 && !prefixes.contains(word)) {
            return;
        }*/

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
            checkPossibilities(newWord, nextRow, nextColumn);
        }

        occupied[row][column] = false;
    }


    private int[][] returnNeighbours(int row, int column) {
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

    public static int addPoints(String word, int pointsCount) {
        if (word.length() <= 4) {
            return pointsCount + 1;
        } else if (word.length() == 5) {
            return pointsCount + 2;
        } else if (word.length() == 6) {
            return pointsCount + 3;
        } else if (word.length() == 7) {
            return pointsCount + 5;
        } else {
            return pointsCount + 11;
        }
    }
}



