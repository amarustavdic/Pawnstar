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


    private int toBitIndex(int row, int col) {
        return row * 8 + col;
    }


    public boolean makeMove(int fromSquare, int toSquare) {
        long orgSquareBit = 1L << fromSquare;
        long dstSquareBit = 1L << toSquare;

        System.out.println(Long.toBinaryString(orgSquareBit));

        for (int i = 0; i < 12; i++) {
            // If there is bitboard that has that peace
            if ((bitboards[i] & orgSquareBit) != 0) {

                // Remove from origin
                setBitboard(i, bitboards[i] ^ orgSquareBit);

                // This means that it is white pawns
                if (i == 0) {
                    System.out.println("Yes it is white pawn");
                    long validMask = (orgSquareBit << 7) | (orgSquareBit << 9);
                    System.out.println(Long.toHexString(validMask));
                }

                // Based of index you know which peace it is
                switch (i) {
                    case 0:

                        if ((33L ^ toSquare) > 0) {
                            // Make move
                            // TODO: ofc not handling the case if something is on there on dst sqr
                            setBitboard(i, bitboards[i] | orgSquareBit);
                            return true;
                        } else {
                            return false;
                        }
                    case 1:
                        break;
                }
            };
        }
        return false;
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

    public long getWhitePawns() {
        return bitboards[0];
    }

    public long getBlackPawns() {
        return bitboards[6];
    }

    public long getBlackPieces() {
        return bitboards[6] | bitboards[7] | bitboards[8] | bitboards[9] | bitboards[10] | bitboards[11];
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

    private String getAlgebraicNotation(int squareIndex) {
        int file = squareIndex % 8;
        int rank = squareIndex / 8;
        return "" + (char) ('a' + file) + (rank + 1);
    }

    private int calculateMaterial(int start, int end) {
        int materialValue = 0;
        int[] pieceValues = {1, 3, 3, 5, 9, 0}; // Pawn, Knight, Bishop, Rook, Queen, King
        for (int i = start; i <= end; i++) {
            materialValue += Long.bitCount(bitboards[i]) * pieceValues[i % 6];
        }
        return materialValue;
    }

    /**
     * Prints the current board state to the console in a human-readable format
     * with centered and filled Unicode chess pieces.
     */
    public void print(boolean showSquareNumbers, boolean showStateInfo) {
        String[] info = new String[8];

        // Turn information
        info[0] = "Turn: " + (isWhiteToMove() ? "White" : "Black");

        // Castling rights
        info[1] = "Castling Rights: " +
                ((stateInfo & 0b0001) != 0 ? "K" : "") +
                ((stateInfo & 0b0010) != 0 ? "Q" : "") +
                ((stateInfo & 0b0100) != 0 ? "k" : "") +
                ((stateInfo & 0b1000) != 0 ? "q" : "");

        // En passant target square
        int enPassantSquare = (int) ((stateInfo >> 5) & 0b111111);
        info[2] = "En Passant Target: " + (enPassantSquare != 0 ? getAlgebraicNotation(enPassantSquare) : "None");

        // Fifty-move rule counter
        info[3] = "Fifty-Move Counter: " + ((stateInfo >> 11) & 0b111111);

        // Full move counter
        info[4] = "Full Move Counter: " + ((stateInfo >> 17) & 0b111111111111111);

        // Material balance
        int whiteMaterial = calculateMaterial(0, 5);
        int blackMaterial = calculateMaterial(6, 11);
        info[5] = "Material Balance: White " + whiteMaterial + " - " + blackMaterial + " Black";


        StringBuilder sb = new StringBuilder();
        for (int rank = 8; rank > 0; rank--) {
            sb.append(rank).append(' '); // Rank label on the left

            for (int file = 0; file < 8; file++) {

                int square = 8 * rank - file - 1; // Square number (63-0)
                long bit = 1L << square; // Mask

                String piece = "   "; // Default to empty square (blank)
                String color = ""; // Default color
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
                sb.append(background).append(color).append(piece).append("\u001B[0m");
            }

            if (showSquareNumbers) {
                sb.append("   ");

                for (int file = 0; file < 8; file++) {
                    int square = 8 * rank - file - 1; // Square number (63-0)
                    String squareNumber = square > 9 ? " " + square : "  " + square;

                    // Alternate background colors for a checkerboard effect
                    String background = ((rank + file) % 2 == 0) ? "\u001B[48;5;232m" : "\u001B[48;5;235m";

                    sb.append(background).append(squareNumber).append("\u001B[0m");
                }
            }

            if (showStateInfo) {
                int infoIndex = 8 - rank;
                if (info[infoIndex] != null) {
                    sb.append("   ").append(info[infoIndex]);
                }
            }
            sb.append("\n");
        }
        sb.append("   a  b  c  d  e  f  g  h\n");
        System.out.print(sb);
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
            case 0 -> '♟'; // White Pawn
            case 1 -> '♞'; // White Knight
            case 2 -> '♝'; // White Bishop
            case 3 -> '♜'; // White Rook
            case 4 -> '♛'; // White Queen
            case 5 -> '♚'; // White King
            case 6 -> '♟'; // Black Pawn
            case 7 -> '♞'; // Black Knight
            case 8 -> '♝'; // Black Bishop
            case 9 -> '♜'; // Black Rook
            case 10 -> '♛'; // Black Queen
            case 11 -> '♚'; // Black King
            default -> ' '; // Empty square
        };
    }

}
