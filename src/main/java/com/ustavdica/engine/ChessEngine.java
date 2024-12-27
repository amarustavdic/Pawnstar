package com.ustavdica.engine;

import com.ustavdica.engine.search.Search;
import com.ustavdica.engine.search.strategies.mcts.MonteCarloTreeSearch;
import com.ustavdica.engine.search.strategies.mcts.rollout.RandomRollout;

public class ChessEngine {

    private Search search;

    public ChessEngine() {
        // Default to MCTS with RandomRollout
        this.search = new Search(new MonteCarloTreeSearch(new RandomRollout()));
    }

    public void setMCTSSearchWithRandomRollout() {
        this.search.setSearchStrategy(new MonteCarloTreeSearch(new RandomRollout()));
    }

    public Move findBestMove(Position position, long timeLimit) {
        return search.findBestMove(position, timeLimit);
    }
}
