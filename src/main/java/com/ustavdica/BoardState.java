package com.ustavdica;

public class BoardState {

    private long whitePawns;
    private long whiteKnights;
    private long whiteBishops;
    private long whiteRooks;
    private long whiteQueens;
    private long whiteKing;

    private long blackPawns;
    private long blackKnights;
    private long blackBishops;
    private long blackRooks;
    private long blackQueens;
    private long blackKing;

    private boolean whiteToMove;

    public BoardState() {
        this.whiteToMove = true;

    }

    private void initializeBoard() {
        whitePawns = 0x000000000000FF00L;
        whiteKnights = 0x0000000000000042L;
        whiteBishops = 0x0000000000000024L;
        whiteRooks = 0x0000000000000081L;
        whiteQueens = 0x0000000000000008L;
        whiteKing = 0x0000000000000010L;

        blackPawns = 0x00FF000000000000L;
        blackKnights = 0x4200000000000000L;
        blackBishops = 0x2400000000000000L;
        blackRooks = 0x8100000000000000L;
        blackQueens = 0x0800000000000000L;
        blackKing = 0x1000000000000000L;
    }

    // Getters for bitboards
    public long getWhitePawns() {
        return whitePawns;
    }

    public long getWhiteKnights() {
        return whiteKnights;
    }

    public long getWhiteBishops() {
        return whiteBishops;
    }

    public long getWhiteRooks() {
        return whiteRooks;
    }

    public long getWhiteQueens() {
        return whiteQueens;
    }

    public long getWhiteKing() {
        return whiteKing;
    }

    public long getBlackPawns() {
        return blackPawns;
    }

    public long getBlackKnights() {
        return blackKnights;
    }

    public long getBlackBishops() {
        return blackBishops;
    }

    public long getBlackRooks() {
        return blackRooks;
    }

    public long getBlackQueens() {
        return blackQueens;
    }

    public long getBlackKing() {
        return blackKing;
    }

    // Method to check if it is white's turn
    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    // Toggle the turn
    public void toggleTurn() {
        whiteToMove = !whiteToMove;
    }

    // Utility method to print the bitboard in a readable format
    public static void printBitboard(long bitboard) {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                System.out.print(((bitboard & (1L << square)) != 0) ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Method to get all occupied squares
    public long getAllOccupiedSquares() {
        return whitePawns | whiteKnights | whiteBishops | whiteRooks | whiteQueens | whiteKing |
                blackPawns | blackKnights | blackBishops | blackRooks | blackQueens | blackKing;
    }

}
