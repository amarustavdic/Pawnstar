package com.ustavdica.engine.search.strategies.mcts;

import com.ustavdica.BoardState;
import com.ustavdica.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TreeNode {

    private final double C = Math.sqrt(2);

    private int visits;
    private double value;
    private final BoardState state;
    private final TreeNode parent;
    private final List<TreeNode> children;

    public TreeNode(BoardState state, TreeNode parent) {
        this.visits = 0;
        this.value = 0;
        this.state = state;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public void expand() {

    }

    private double uct() {
        return visits == 0 ? Double.MAX_VALUE : (value / visits) + C * Math.sqrt(Math.log(parent.visits) / visits);
    }

    public TreeNode getBestChild() {
        return children.stream().max(Comparator.comparingDouble(TreeNode::uct)).orElse(null);
    }

    // TODO: Figure this out yet...
    public void addValue(double value) {
        this.value += this.state.isWhiteToMove() ? value : -value;
    }

    public boolean isSimulated() {
        return false;
    }

    public BoardState getState() {
        return state;
    }

    public TreeNode getRandomChild() {
        Collections.shuffle(children);
        return children.getFirst();
    }

    public void incrementVisits() {
        this.visits++;
    }

    public TreeNode getParent() {
        return parent;
    }

    public Move getBestMove() {
        return null;
    }

    public boolean hasChildren() {
        // return !children.isEmpty();
        return false;
    }

}
