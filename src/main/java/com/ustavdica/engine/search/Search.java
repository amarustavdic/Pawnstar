package com.ustavdica.engine.search;

import com.ustavdica.engine.Position;
import com.ustavdica.engine.Move;
import com.ustavdica.engine.search.strategies.SearchStrategy;

public class Search {

    private SearchStrategy searchStrategy;

    public Search(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public Move findBestMove(Position position, long timeLimit) {
        return searchStrategy.findBestMove(position, timeLimit);
    }

}
