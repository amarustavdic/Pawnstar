package com.ustavdica.engine.search.strategies.mcts;

import com.ustavdica.engine.MoveGenerator;
import com.ustavdica.engine.Position;
import com.ustavdica.engine.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a node in the Monte Carlo Tree Search (MCTS) tree.
 * Each node corresponds to a specific board state and tracks its visits, value, and children.
 */
public class TreeNode {

    private static final double EXPLORATION_PARAMETER = Math.sqrt(2); // UCT exploration constant

    private int visits;
    private double value;
    private final Position position;
    private final TreeNode parent;
    private final List<TreeNode> children;

    /**
     * Constructs a TreeNode for the given board state and parent.
     *
     * @param position The board state represented by this node.
     * @param parent   The parent node in the tree, or null if this is the root.
     */
    public TreeNode(Position position, TreeNode parent) {
        this.visits = 0;
        this.value = 0;
        this.position = position;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    /**
     * Expands the current node by generating all possible legal moves
     * and creating child nodes for each resulting board state.
     */
    public void expand() {
        MoveGenerator moveGenerator = new MoveGenerator();
        List<Move> legalMoves = moveGenerator.generateLegalMoves(position);

        System.out.println(legalMoves);

        // Create child nodes for each legal move
        for (Move move : legalMoves) {
            Position clonedPosition = new Position(position);
            clonedPosition.makeMove(move);
            TreeNode child = new TreeNode(clonedPosition, this);
            children.add(child);
        }
    }

    /**
     * Calculates the Upper Confidence Bound for Trees (UCT) value of this node.
     *
     * @return The UCT value, or Double.MAX_VALUE if the node has not been visited.
     */
    private double uct() {
        return visits == 0 ?
                Double.MAX_VALUE
                : (value / visits) + EXPLORATION_PARAMETER * Math.sqrt(Math.log(parent.visits) / visits);
    }

    /**
     * Selects the best child node based on the UCT value.
     *
     * @return The child node with the highest UCT value, or null if no children exist.
     */
    public TreeNode getBestChild() {
        return children.stream()
                .max(Comparator.comparingDouble(TreeNode::uct))
                .orElse(null);
    }

    // TODO: Figure this out yet... this addValue

    /**
     * Adds a value to this node for backpropagation.
     *
     * @param value The value to add, adjusted for the current player's perspective.
     */
    public void addValue(double value) {
        this.value += this.position.isWhiteToMove() ? value : -value;
    }

    /**
     * Checks if this node has been fully simulated.
     *
     * @return True if the node has been simulated, false otherwise.
     */
    public boolean isSimulated() {
        return visits > 0;
    }

    /**
     * Retrieves the current board state represented by this node.
     *
     * @return The board state.
     */
    public Position getState() {
        return position;
    }

    /**
     * Retrieves a random child node for exploration purposes.
     *
     * @return A randomly selected child node, or null if no children exist.
     */
    public TreeNode getRandomChild() {
        if (children.isEmpty()) return null;
        Collections.shuffle(children);
        return children.getFirst();
    }

    /**
     * Increments the visit count of this node.
     */
    public void incrementVisits() {
        this.visits++;
    }

    /**
     * Retrieves the parent node of this node.
     *
     * @return The parent node, or null if this is the root node.
     */
    public TreeNode getParent() {
        return parent;
    }

    /**
     * Retrieves the best move based on child visit counts.
     *
     * @return The move corresponding to the child with the highest visit count, or null if no children exist.
     */
    public Move getBestMove() {
        if (children.isEmpty()) return null;

//        return children.stream()
//                .max(Comparator.comparingInt(child -> child.visits))
//                .map(child -> {
//                    Position clonedPosition = new Position(position);
//                })
        return null;

        // TODO: to be figured out yet
    }

    /**
     * Checks if this node has children.
     *
     * @return True if the node has children, false otherwise.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

}
