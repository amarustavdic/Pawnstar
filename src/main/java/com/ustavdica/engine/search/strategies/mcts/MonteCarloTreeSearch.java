package com.ustavdica.engine.search.strategies.mcts;

import com.ustavdica.BoardState;
import com.ustavdica.Move;
import com.ustavdica.engine.search.strategies.SearchStrategy;
import com.ustavdica.engine.search.strategies.mcts.rollout.RolloutStrategy;

/**
 *  Implements the Monte Carlo Tree Search (MCTS) algorithm for determining the best move
 *  in a given chess position. MCTS operates by iteratively building a search tree
 *  through selection, expansion, simulation (rollout), and backpropagation.
 *  <p>
 *  This class utilizes a {@link RolloutStrategy} to customize the simulation phase, allowing
 *  flexibility in how rollouts are conducted (e.g., random play, heuristic evaluation).
 */
public class MonteCarloTreeSearch implements SearchStrategy {

    private TreeNode root;
    private RolloutStrategy rolloutStrategy;

    /**
     * Constructs a MonteCarloTreeSearch instance with the specified rollout strategy.
     *
     * @param rolloutStrategy The strategy for performing rollouts during the simulation phase.
     */
    public MonteCarloTreeSearch(RolloutStrategy rolloutStrategy) {
        this.rolloutStrategy = rolloutStrategy;
    }

    /**
     * Finds the best move for the current player using MCTS within the given time limit.
     *
     * @param state     The initial board state from which the search begins.
     * @param timeLimit The maximum time (in milliseconds) to perform the search.
     * @return The best move determined by MCTS, or null if no move could be found.
     */
    @Override
    public Move findBestMove(BoardState state, long timeLimit) {

        long start = System.currentTimeMillis();
        root = new TreeNode(state, null);

        while (System.currentTimeMillis() - start < timeLimit) {
            TreeNode selectedNode = select();
            if (selectedNode == null) {
                // TODO: Take a deeper look why can you even have empty node...?
                return null;
            }
            TreeNode expandedNode = expand(selectedNode);
            double rolloutResult = rollout(expandedNode);
            backpropagate(expandedNode, rolloutResult);
        }
        return root.getBestMove();
    }

    /**
     * Selects a promising node in the tree by following the best child nodes
     * according to a defined selection policy UCT.
     *
     * @return The selected node, or null if selection cannot proceed.
     */
    private TreeNode select() {
        TreeNode node = root;
        while (node.hasChildren()) {
            node = node.getBestChild();
        }
        return null;
    }

    /**
     *  Expands a given node by generating and adding its children.
     *  If the node has already been simulated or is terminal, expansion is skipped.
     *
     *  @param node The node to expand.
     *  @return A randomly selected child of the expanded node, or null if expansion is not possible.
     */
    private TreeNode expand(TreeNode node) {
        if (node.isSimulated() && !node.getState().isTerminal()) {
            node.expand();
            return node.getRandomChild();
        }
        return null;
    }

    // TODO: Expand function might benefit from strategy pattern, for different expanding strategies...

    /**
     *  Performs a rollout (simulation) on the given node using the configured {@link RolloutStrategy}.
     *
     *  @param treeNode The node to simulate from.
     *  @return The evaluation score of the simulated game.
     */
    private double rollout(TreeNode treeNode) {
        return rolloutStrategy.rollout(treeNode);
    }

    /**
     *  Backpropagates the result of a simulation (rollout) up the tree,
     *  updating the visit count and value for all nodes along the path to the root.
     *
     *  @param node          The node where the simulation ended.
     *  @param rolloutResult The result of the simulation (evaluation score).
     */
    private void backpropagate(TreeNode node, double rolloutResult) {
        while (node != null) {
            node.incrementVisits();
            node.addValue(rolloutResult);
            node = node.getParent();
        }
    }
}
