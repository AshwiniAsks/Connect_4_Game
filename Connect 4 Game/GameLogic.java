public class GameLogic {
    private char[][] board;
    private final int ROWS = 6;
    private final int COLS = 7;

    public GameLogic() {
        board = new char[ROWS][COLS];
        // Fill board with empty spaces
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = ' ';
            }
        }
    }

    // Drop piece into column (returns row index or -1 if column is full)
    public int dropPiece(int col, char symbol) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == ' ') {
                board[row][col] = symbol;
                return row;
            }
        }
        return -1; // column full
    }

    // Check for a win
    public boolean checkWin(int row, int col, char symbol) {
        return (checkDirection(row, col, symbol, 1, 0)    // vertical
             || checkDirection(row, col, symbol, 0, 1)    // horizontal
             || checkDirection(row, col, symbol, 1, 1)    // diagonal /
             || checkDirection(row, col, symbol, 1, -1)); // diagonal \
    }

    // Count consecutive symbols in both directions
    private boolean checkDirection(int row, int col, char symbol, int dr, int dc) {
        int count = 1;

        // Forward direction
        int r = row + dr, c = col + dc;
        while (isValid(r, c) && board[r][c] == symbol) {
            count++;
            r += dr; c += dc;
        }

        // Backward direction
        r = row - dr; c = col - dc;
        while (isValid(r, c) && board[r][c] == symbol) {
            count++;
            r -= dr; c -= dc;
        }

        return count >= 4;
    }

    // Check board bounds
    private boolean isValid(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }

    // Check for tie
    public boolean isBoardFull() {
        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == ' ') return false;
        }
        return true;
    }
}
