package com.ustavdica;

import com.ustavdica.engine.ChessEngine;
import com.ustavdica.engine.Move;
import com.ustavdica.engine.Position;

public class Main {
    public static void main(String[] args) {


        ChessEngine engine = new ChessEngine();

        Position position = new Position();

        position.print(false, false);

        engine.setMCTSSearchWithRandomRollout();

        // Find the best move with a 5-second time limit
        long timeLimit = 5000;
        Move bestMove = engine.findBestMove(position, timeLimit);

        System.out.println(bestMove);

    }

}