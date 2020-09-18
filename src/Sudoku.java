import java.util.Arrays;
import java.util.Scanner;

class Sudoku {

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(",");

        while(scanner.hasNextLine()){
            String[] inputs = scanner.nextLine().split(",");
            String sudokuString = inputs[1];
            int[][] board = convertStringToBoard(sudokuString);
            System.out.printf("%s,%s\n", inputs[0], solveSudoku(board) ? convertBoardToString(board) : "");
        }
        scanner.close();
    }

    /**
     * Convert input String to 9x9 2D Integer Array representation of Sudoku board
     *
     * @param sudokuString String to be converted
     * @return 9x9 2D Array representation of Sudoku board
     */
    private static int[][] convertStringToBoard(String sudokuString) {
        return Arrays.stream(sudokuString.split("(?<=\\G.{9})"))
                .map(s -> (Arrays.stream(s.split("(?<=\\G.)")).mapToInt(Integer::parseInt).toArray()))
                .toArray(int[][]::new);
    }

    /**
     * Convert 9x9 2D Array into a single String representing the solution
     *
     * @param board The 9x9 2D Array representing the solved puzzle
     * @return The solved puzzle as a single String stripped of any other non-numeric values
     */
    private static String convertBoardToString(int[][] board) {
        return Arrays.deepToString(board)
                    .replace(" ", "")
                    .replace(",", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();
    }

    /**
     * Solve Sudoku puzzle that is passed as a 9x9 2D Array
     *
     * @param board the Sudoku board to solve
     * @return if the board was solved we return true, if not then return false
     */
    private static boolean solveSudoku(int[][] board) {
        int row = 0;
        int column = 0;
        boolean isFilled = true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    isFilled = false;
                    row = i;
                    column = j;
                    break;
                }
            }
            if (!isFilled) {
                break;
            }
        }

        if (isFilled) {
            // if there are no more zeros / empty spaces return truthy
            return true;
        }

        for (int num = 1; num <= board.length; num++) {
            if (isSafe(board, row, column, num)) {
                board[row][column] = num;
                if (solveSudoku(board)) {
                    return true;
                }
                else {
                    board[row][column] = 0;
                }
            }
        }
        return false;
    }

    /**
     * Check specified row, column and the associated square to see if there are any number clashes
     *
     * @param board the Sudoku board to be checked
     * @param row the row to check
     * @param column the column to check
     * @param number the number we want to check
     * @return true if no clashes, or false if there are clashes
     */
    private static boolean isSafe(int[][] board, int row, int column, int number) {
        // check the row
        for (int d = 0; d < board.length; d++) {
            if (board[row][d] == number) {
                return false;
            }
        }

        // check the column
        for (int r = 0; r < board.length; r++) {
            if (board[r][column] == number) {
                return false;
            }
        }

        // check the square
        int sqrt = (int)Math.sqrt(board.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = column - column % sqrt;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++) {
            for (int d = boxColStart; d < boxColStart + sqrt; d++) {
                if (board[r][d] == number) {
                    return false;
                }
            }
        }
        return true;
    }
}
