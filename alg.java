import java.util.Random;

public class alg {
    private final int SIZE;
    private boolean[][] occupied;
    private char[][] grid;

    private alg(int SIZE) {
        // constructor method, makes all the variables and calls generateGrid();
        this.SIZE = SIZE;
        occupied = new boolean[SIZE][SIZE];
        grid = new char[SIZE][SIZE];
        generateGrid();
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
}
