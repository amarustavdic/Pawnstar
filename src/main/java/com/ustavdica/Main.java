package com.ustavdica;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        BoardState boardState = new BoardState();

        boardState.printBoard();

    }

//    public static List<Move> generatePawnMoves(long bitboard, BoardState boardState) {
//        List<Move> moves = new ArrayList<>();
//
//        // Get occupied squares
//        long allOccupied = boardState.getAllOccupiedSquares();
//
//        // Generate one-step forward moves
//        long oneStep = (bitboard << 8) & ~allOccupied;
//
//        // Generate two-step moves (only for pawns on the starting rank)
//        long startingRank = 0x000000000000FF00L; // Rank 2 for white pawns
//        long twoSteps = ((bitboard & startingRank) << 16) & ~allOccupied & ~(allOccupied << 8);
//
//        // Generate captures
//        long enemyPieces = boardState.getBlackPawns() | boardState.getBlackKnights() |
//                boardState.getBlackBishops() | boardState.getBlackRooks() |
//                boardState.getBlackQueens() | boardState.getBlackKing();
//        long leftCaptures = (bitboard << 7) & enemyPieces & ~0x8080808080808080L; // Exclude wraparounds
//        long rightCaptures = (bitboard << 9) & enemyPieces & ~0x0101010101010101L; // Exclude wraparounds
//
//        // Add moves to the list
//        addMovesToList(moves, bitboard, oneStep, 8); // One-step forward
//        addMovesToList(moves, bitboard, twoSteps, 16); // Two-steps forward
//        addMovesToList(moves, bitboard, leftCaptures, 7); // Left captures
//        addMovesToList(moves, bitboard, rightCaptures, 9); // Right captures
//
//        // TODO: Handle en passant and promotion
//
//        return moves;
//    }

    private static void addMovesToList(List<Move> moves, long fromBitboard, long toBitboard, int shift) {
        while (toBitboard != 0) {
            long lsb = toBitboard & -toBitboard; // Isolate the least significant bit
            int toSquare = Long.numberOfTrailingZeros(lsb);
            int fromSquare = toSquare - shift;
            moves.add(new Move(fromSquare, toSquare));
            toBitboard &= toBitboard - 1; // Clear the bit
        }
    }


    public static class Move {
        int from;
        int to;

        public Move(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "Move{ " +
                    "from=" + from +
                    ", to=" + to +
                    " }";
        }
    }
}