package com.ustavdica.engine.search.strategies;

import com.ustavdica.engine.Position;
import com.ustavdica.engine.Move;

public interface SearchStrategy {

    Move findBestMove(Position position, long timeLimit);

}
