package com.ustavdica.engine.search.strategies;

import com.ustavdica.BoardState;
import com.ustavdica.Move;

public interface SearchStrategy {

    Move findBestMove(BoardState boardState, long timeLimit);

}
