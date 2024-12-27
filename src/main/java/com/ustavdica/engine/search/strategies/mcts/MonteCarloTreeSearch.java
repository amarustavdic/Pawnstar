package com.ustavdica.engine.search.strategies.mcts;

import com.ustavdica.BoardState;
import com.ustavdica.Move;
import com.ustavdica.engine.search.strategies.SearchStrategy;

public class MonteCarloTreeSearch implements SearchStrategy {

    private TreeNode root;

    @Override
    public Move findBestMove(BoardState boardState, long timeLimit) {
        return null;
    }

    private TreeNode select() {
        return null;
    }

    private TreeNode expand() {
        return null;
    }

    // this is basically where I have different options, I can evaluate state in many different ways
    private double simulate() {
        return 0;
    }

    private void backpropagate() {

    }
}
