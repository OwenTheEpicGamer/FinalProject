package boggle;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class alg {
    private final int SIZE;
    private boolean[][] occupied;
    private char[][] grid;
    private Set<String> dict;
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
        generateWordlist();

        System.out.println(grid);
        System.out.println(wordList);
        System.out.println(wordList.toArray());
        System.out.println(dict);
        System.out.println(dict.toArray());

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
                int index = (row * SIZE + column) % dice.length;
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

    private void checkPossibilities(String startLetter, int row, int column) {
        Deque<String> q = new ArrayDeque<String>();
        q.addLast(String.valueOf(startLetter));

        while (!q.isEmpty()) {
            String word = q.pop();
            if (dict.contains(word)) {
                wordList.add(word);
            }

            // 2d array holding coords of all surrounding cells
            int[][] neighbours = returnNeighbours(row, column);

            for (int[] coord : neighbours) {
                // next means the value for the next pass
                int nextRow = coord[0];
                int nextColumn = coord[1];

                char nextLetter = grid[nextRow][nextColumn];
                checkPossibilities(word + nextLetter, nextRow, nextColumn);
                }
            }
        }


    private int[][] returnNeighbours(int row, int column) {
        ArrayList<int[]> neighbours = (ArrayList<int[]>) Arrays.asList(
                new int[]{row + 1, column}, new int[]{row - 1, column},
                new int[]{row, column + 1}, new int[]{row, column - 1},
                new int[]{row + 1, column + 1}, new int[]{row - 1, column - 1},
                new int[]{row + 1, column - 1}, new int[]{row - 1, column + 1});

        for (int i = 0; i < neighbours.size(); i++) {
            int r = neighbours.get(i)[0];
            int c = neighbours.get(i)[1];
            if (r < SIZE && c < SIZE && r >= 0 && c >= 0){
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
        if (word.length() == 0) { // turn is skipped
           return pointsCount;
        }
        else if (word.length() <= 4) {
            return pointsCount + 1;
        }
        else if(word.length() == 5) {
            return pointsCount + 2;
        }
        else if(word.length() == 6) {
            return pointsCount + 3;
        }
        else if(word.length() == 7) {
            return  pointsCount + 5;
        }
        else {
            return pointsCount + 11;
        }
    }

}



