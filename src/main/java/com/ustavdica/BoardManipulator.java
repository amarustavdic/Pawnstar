package com.ustavdica;

import java.util.ArrayList;
import java.util.List;

public class BoardManipulator {

    /**
     * Generates all legal moves for the current player in the given board state.
     * Legal moves ensure the king is not in check after the move.
     *
     * @param boardState The current board state.
     * @return A list of all legal moves for the current player.
     */
    public List<Move> generateLegalMoves(BoardState boardState) {

        List<Move> moves = new ArrayList<>();

        // Get occupied squares
        long allOccupied = boardState.getAllOccupiedSquares();

        // Generate one-step forward moves
        long oneStep = (boardState.getWhitePawns() << 8) & ~allOccupied;

        // Generate two-step moves (only for pawns on the starting rank)
        long startingRank = 0x000000000000FF00L; // Rank 2 for white pawns
        long twoSteps = ((boardState.getWhitePawns() & startingRank) << 16) & ~allOccupied & ~(allOccupied << 8);

        // Generate captures
        long enemyPieces = boardState.getBlackPieces();
        long leftCaptures = (boardState.getWhitePawns() << 7) & enemyPieces & ~0x8080808080808080L; // Exclude wraparounds
        long rightCaptures = (boardState.getWhitePawns() << 9) & enemyPieces & ~0x0101010101010101L; // Exclude wraparounds

        // Add moves to the list
        addMovesToList(moves, oneStep, 8); // One-step forward
        addMovesToList(moves, twoSteps, 16); // Two-steps forward
        addMovesToList(moves, leftCaptures, 7); // Left captures
        addMovesToList(moves, rightCaptures, 9); // Right captures

        // TODO: Handle en passant and promotion

        return moves;

    }

    public void generateAttacks() {

    }

    public long generatePawnAttacks(BoardState boardState) {
        long pawnAttacks = 0L;
        final long FILE_A = 0x0101010101010101L; // Mask for the a-file
        final long FILE_H = 0x8080808080808080L; // Mask for the h-file

        if (boardState.isWhiteToMove()) {
            // Generate black pawn attacks
            long positions = boardState.getBlackPawns();
            pawnAttacks |= (positions & ~FILE_H) >> 7; // Left diagonal attacks
            pawnAttacks |= (positions & ~FILE_A) >> 9; // Right diagonal attacks
        } else {
            // Generate white pawn attacks
            long positions = boardState.getWhitePawns();
            pawnAttacks |= (positions & ~FILE_A) << 7; // Left diagonal attacks
            pawnAttacks |= (positions & ~FILE_H) << 9; // Right diagonal attacks
        }

        printAttacks(pawnAttacks);
        return pawnAttacks;
    }


    private void printAttacks(long bitboard) {
        StringBuilder sb = new StringBuilder();
        for (int rank = 8; rank > 0; rank--) {
            sb.append(rank).append(' '); // Rank label on the left

            for (int file = 0; file < 8; file++) {
                int square = 8 * rank - file - 1; // Square number (0-63)
                long mask = 1L << square; // Bit mask for the current square

                // Determine background color
                String background = ((rank + file) % 2 == 0) ? "\u001B[48;5;232m" : "\u001B[48;5;235m";

                // Highlight with red if this square is attacked (bitboard has a 1)
                if ((bitboard & mask) != 0) {
                    background = ((rank + file) % 2 == 0) ? "\u001B[48;5;124m" : "\u001B[48;5;160m"; // Dark red and light red
                }

                // Append the background
                sb.append(background).append("   ").append("\u001B[0m");
            }
            sb.append("\n");
        }
        sb.append("   a  b  c  d  e  f  g  h\n");
        System.out.print(sb);
    }



    private static void addMovesToList(List<Move> moves, long toBitboard, int shift) {
        while (toBitboard != 0) {
            long lsb = toBitboard & -toBitboard; // Isolate the least significant bit
            int toSquare = Long.numberOfTrailingZeros(lsb);
            int fromSquare = toSquare - shift;
            moves.add(new Move(fromSquare, toSquare));
            toBitboard &= toBitboard - 1; // Clear the bit
        }
    }

    // Move application

    // Move validation

    // Undo moves (memento pattern)

    // Some other special chess rules

}
