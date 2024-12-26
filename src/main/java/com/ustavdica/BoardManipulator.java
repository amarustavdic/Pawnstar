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
