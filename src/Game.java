import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private int[][] gameBoard = new int[4][4];
//    private int[][] gameBoard = {{2,4,2,4},{4,2,4,2},{2,4,2,4},{4,2,4,2}};
    private int score = 0;

    public void play() {
        System.out.println("Begin game: ");
        fillNewCell(gameBoard);
        fillNewCell(gameBoard);
        printBoard(gameBoard);

        Scanner scanner = new Scanner(System.in);
        while (!isGameOver(gameBoard)) {
            String action = scanner.next();
            System.out.println(action);

            // TODO: need to test if a move is possible before accepting it
            switch (action.toLowerCase()) {
                case "w" -> {
                    rotateBoardCounterClockwise(gameBoard);
                    shiftCells(gameBoard);
                    mergeCells(gameBoard);
                    rotateBoardClockwise(gameBoard);
                }
                case "a" -> {
                    shiftCells(gameBoard);
                    mergeCells(gameBoard);
                }
                case "s" -> {
                    rotateBoardClockwise(gameBoard);
                    shiftCells(gameBoard);
                    mergeCells(gameBoard);
                    rotateBoardCounterClockwise(gameBoard);
                }
                case "d" -> {
                    reverseBoard(gameBoard);
                    shiftCells(gameBoard);
                    mergeCells(gameBoard);
                    reverseBoard(gameBoard);
                }
                default -> System.out.println("Unsupported action: " + action);
            }

            fillNewCell(gameBoard);
            printBoard(gameBoard);
        }
    }

    private boolean isGameOver(int[][] gameBoard) {
        if (score == 2048) {
            System.out.println("You win!!!");
            return true;
        }

        if (!(canMoveLeft(gameBoard) && canMoveRight(gameBoard) && canMoveUp(gameBoard) && canMoveDown(gameBoard))) {
            System.out.println("Game over.");
            return true;
        }

        return false;
    }

    private boolean canMoveLeft(int[][] gameBoard) {

        shiftCells(gameBoard);
        mergeCells(gameBoard);

        // if boards are not equal after comparison then there are possible moves left
        return !compareBoards(gameBoard, this.gameBoard);
    }

    private boolean canMoveRight(int[][] gameBoard) {
        reverseBoard(gameBoard);
        shiftCells(gameBoard);
        mergeCells(gameBoard);
        reverseBoard(gameBoard);

        // if boards are not equal after comparison then there are possible moves left
        return !compareBoards(gameBoard, this.gameBoard);
    }

    private boolean canMoveUp(int[][] gameBoard) {
        rotateBoardCounterClockwise(gameBoard);
        shiftCells(gameBoard);
        mergeCells(gameBoard);
        rotateBoardClockwise(gameBoard);

        // if boards are not equal after comparison then there are possible moves left
        return !compareBoards(gameBoard, this.gameBoard);
    }

    private boolean canMoveDown(int[][] gameBoard) {
        rotateBoardClockwise(gameBoard);
        shiftCells(gameBoard);
        mergeCells(gameBoard);
        rotateBoardCounterClockwise(gameBoard);

        // if boards are not equal after comparison then there are possible moves left
        return !compareBoards(gameBoard, this.gameBoard);
    }

    private boolean compareBoards(int[][] gameBoard, int[][] testGameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[i][j] != testGameBoard[i][j])
                    return false;
            }
        }
        return true;
    }

    private void fillNewCell(int[][] gameBoard) {
        List<String> emptyCells = new ArrayList<>();
        // add all cells with 0 value to list
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] == 0) {
                    emptyCells.add(i + "," + j);
                }
            }
        }

        if (!emptyCells.isEmpty()) {
            // select random cell from list and add 2, enhancement add a chance to add a 4
            Random random = new Random();
            String[] indexes = emptyCells.get(random.nextInt(emptyCells.size())).split(",");
            gameBoard[Integer.parseInt(indexes[0])][Integer.parseInt(indexes[1])] = 2;
        }
    }

    private void shiftCells(int[][] gameBoard) {
        int[][] newGameBoard = new int[4][4];

        for (int i = 0; i < gameBoard.length; i++) {
            int nextIndex = 0;
            for (int j = 0; j < gameBoard[i].length; j++) {
                if (gameBoard[i][j] != 0) {
                    newGameBoard[i][nextIndex] = gameBoard[i][j];
                    nextIndex++;
                }
            }
        }

        this.gameBoard = newGameBoard;
    }

    private void mergeCells(int[][] gameBoard) {
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length - 1; j++) {
                if (gameBoard[i][j] == gameBoard[i][j + 1]) {
                    gameBoard[i][j] = gameBoard[i][j] + gameBoard[i][j + 1];
                    gameBoard[i][j + 1] = 0;
                    score += gameBoard[i][j];
                }
                shiftCells(gameBoard);
            }
        }
    }

    private void reverseBoard(int[][] gameBoard) {
        for (int[] row : gameBoard) {
            int i = 0;
            int j = row.length - 1;
            while (i < j) {
                int temp = row[i];
                row[i] = row[j];
                row[j] = temp;
                i++;
                j--;
            }
        }
    }

    private void rotateBoardClockwise(int[][] gameBoard) {
        int X = gameBoard.length;
        int Y = gameBoard[0].length;
        int[][] newGameBoard = new int[X][Y];
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                newGameBoard[j][X - 1 - i] = gameBoard[i][j];
            }
        }

        this.gameBoard = newGameBoard;
    }

    private void rotateBoardCounterClockwise(int[][] gameBoard) {
        int X = gameBoard.length;
        int Y = gameBoard[0].length;
        int[][] newGameBoard = new int[X][Y];
        for (int i = 0; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                newGameBoard[X - 1 - j][i] = gameBoard[i][j];
            }
        }

        this.gameBoard = newGameBoard;
    }

    private void printBoard(int[][] gameBoard) {
        System.out.println("score: " + this.score);
        for (int[] row : gameBoard) {
            System.out.println(Arrays.toString(row));
        }
    }
}
