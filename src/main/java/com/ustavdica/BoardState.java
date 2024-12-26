package com.ustavdica;

/**
 * Represents the state of a chessboard using bitboards and additional game state information.
 */
public class BoardState {

    /**
     * Array of 12 bitboards representing the positions of all pieces.
     * Index mapping:
     * 0-5: White pieces (Pawns, Knights, Bishops, Rooks, Queens, King).
     * 6-11: Black pieces (Pawns, Knights, Bishops, Rooks, Queens, King).
     */
    private final long[] bitboards;

    /**
     * Encodes additional game state information:
     * - Bit 0: Whose turn it is (1 = white to move, 0 = black to move).
     * - Bits 1-4: Castling rights (1 = white kingside, 2 = white queenside, 4 = black kingside, 8 = black queenside).
     * - Bits 5-10: En passant target square (6 bits for square index or 0 if none).
     * - Bits 11-16: Fifty-move rule counter.
     * - Bits 17-31: Full move counter.
     */
    private long stateInfo;

    /**
     * Constructs a new BoardState with the initial chessboard setup.
     */
    public BoardState() {
        this.bitboards = new long[12];
        this.stateInfo = 0;
        initializeBoard();
    }

    /**
     * Constructs a deep copy of another BoardState.
     *
     * @param other The BoardState to copy.
     */
    public BoardState(BoardState other) {
        this.bitboards = other.bitboards.clone();
        this.stateInfo = other.stateInfo;
    }

    /**
     * Initializes the board to the standard starting chess position.
     */
    private void initializeBoard() {
        // White pieces
        bitboards[0] = 0x000000000000FF00L; // Pawns
        bitboards[1] = 0x0000000000000042L; // Knights
        bitboards[2] = 0x0000000000000024L; // Bishops
        bitboards[3] = 0x0000000000000081L; // Rooks
        bitboards[4] = 0x0000000000000008L; // Queens
        bitboards[5] = 0x0000000000000010L; // King

        // Black pieces
        bitboards[6] = 0x00FF000000000000L; // Pawns
        bitboards[7] = 0x4200000000000000L; // Knights
        bitboards[8] = 0x2400000000000000L; // Bishops
        bitboards[9] = 0x8100000000000000L; // Rooks
        bitboards[10] = 0x0800000000000000L; // Queens
        bitboards[11] = 0x1000000000000000L; // King

        stateInfo = 0b1111L; // Both sides can castle initially
    }

    /**
     * Retrieves the bitboard for a specific piece type.
     *
     * @param index The index of the piece type (0-11).
     * @return The bitboard representing the positions of the specified piece type.
     */
    public long getBitboard(int index) {
        return bitboards[index];
    }

    /**
     * Updates the bitboard for a specific piece type.
     *
     * @param index The index of the piece type (0-11).
     * @param value The new bitboard value.
     */
    public void setBitboard(int index, long value) {
        bitboards[index] = value;
    }

    /**
     * Checks whose turn it is.
     *
     * @return True if it is white's turn, false if it is black's turn.
     */
    public boolean isWhiteToMove() {
        return (stateInfo & 1) != 0;
    }

    /**
     * Toggles the turn between white and black.
     */
    public void toggleTurn() {
        stateInfo ^= 1; // Flip the first bit
    }

    /**
     * Computes a bitboard representing all occupied squares on the board.
     *
     * @return A bitboard with bits set for all occupied squares.
     */
    public long getAllOccupiedSquares() {
        long occupied = 0L;
        for (long bitboard : bitboards) {
            occupied |= bitboard;
        }
        return occupied;
    }

    /**
     * Prints the current board state to the console in a human-readable format
     * with centered and filled Unicode chess pieces.
     */
    public void print() {
        StringBuilder boardStr = new StringBuilder();

        for (int rank = 7; rank >= 0; rank--) {
            boardStr.append(rank + 1).append(" "); // Add row label on the left
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                long bit = 1L << square;

                String piece = "   "; // Default to empty square (blank)
                String color = "";  // Default color
                for (int i = 0; i < 12; i++) {
                    if ((bitboards[i] & bit) != 0) {
                        piece = " " + getUnicodePiece(i) + " "; // Centered with spaces
                        color = i < 6 ? "\u001B[1;29m" : "\u001B[1;30m"; // White (bold white) or Black (bold black)
                        break;
                    }
                }

                // Alternate background colors for a checkerboard effect
                String background = ((rank + file) % 2 == 0) ? "\u001B[48;5;94m" : "\u001B[48;5;101m";

                // Append colored piece or empty square
                boardStr.append(background).append(color).append(piece).append("\u001B[0m");
            }
            boardStr.append("\n"); // Move to the next row
        }

        // Add column labels at the bottom
        boardStr.append("   a  b  c  d  e  f  g  h\n");

        System.out.print(boardStr);
    }

    /**
     * Maps a piece index to its corresponding Unicode chess character.
     *
     * @param index The index of the piece type (0-11).
     * @return A Unicode character representing the chess piece.
     */
    private char getUnicodePiece(int index) {
        // TODO: This can be simplified since I am using same characters for both set of peaces
        return switch (index) {
            case 0 -> '\u265F'; // White Pawn
            case 1 -> '\u265E'; // White Knight
            case 2 -> '\u265D'; // White Bishop
            case 3 -> '\u265C'; // White Rook
            case 4 -> '\u265B'; // White Queen
            case 5 -> '\u265A'; // White King
            case 6 -> '\u265F'; // Black Pawn
            case 7 -> '\u265E'; // Black Knight
            case 8 -> '\u265D'; // Black Bishop
            case 9 -> '\u265C'; // Black Rook
            case 10 -> '\u265B'; // Black Queen
            case 11 -> '\u265A'; // Black King
            default -> ' '; // Empty square
        };
    }


}
