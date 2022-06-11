import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class alg {
    private final int SIZE;
    private boolean[][] occupied;
    private char[][] grid;
    private Set<String> dict;
    private Set<String> wordList;

    private alg(int SIZE) {
        // constructor method, makes all the variables and calls generateGrid();
        this.SIZE = SIZE;
        occupied = new boolean[SIZE][SIZE];
        grid = new char[SIZE][SIZE];
        generateGrid();
        generateDict();
        generateWordlist();

    }

    private void generateGrid() {
        // makes grid for the first time randomly
        String[] dice = new String[]{
                "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
                "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
                "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR",
                "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
                "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"};

        Random rand = new Random();

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                int index = (row * SIZE + column) % 25;
                // index will iterate through dice array and loop back to start when out of bounds
                int randomInt = rand.nextInt(6);
                grid[row][column] = dice[index].charAt(randomInt);
            }
        }
    }

    private void generateDict() {
        dict = new HashSet<String>();

        try {
            File dictFile = new File("dictionary.txt");
            Scanner dictReader = new Scanner(dictFile);
            dict.add(dictReader.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        if (dict.contains(word)) {
            wordList.add(word);
        }

        // 2d array holding coords of all surrounding cells
        int[][] neighbours = {
                {row + 1, column}, {row - 1, column},
                {row, column + 1}, {row, column - 1},
                {row + 1, column + 1}, {row - 1, column - 1},
                {row + 1, column - 1}, {row - 1, column + 1}};

        for (int[] coord : neighbours) {
            // next means the value for the next pass
            int nextRow = coord[0];
            int nextColumn = coord[1];
            char nextLetter = grid[nextRow][nextColumn];

            checkPossibilities(word + nextLetter, nextRow, nextColumn);
        }
    }

}



