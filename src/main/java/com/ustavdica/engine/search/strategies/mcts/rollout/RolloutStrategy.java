package com.ustavdica.engine.search.strategies.mcts.rollout;

import com.ustavdica.engine.search.strategies.mcts.TreeNode;

/**
 *  Defines a strategy for performing rollouts (simulations) in Monte Carlo Tree Search (MCTS).
 *  A rollout involves simulating a game from the current node to a terminal state or
 *  a predefined depth and returning an evaluation score based on the resulting state.
 */
public interface RolloutStrategy {
    /**
     * Executes a rollout (simulation) from the given node and returns an evaluation score.
     *
     * <p>The implementation of this method determines how the rollout is conducted, which could be:
     * - Random play (choosing legal moves randomly until a terminal state or depth limit is reached).
     * - Heuristic evaluation at an intermediate or terminal state.
     * - Neural network evaluation for state scoring, etc...
     *
     * @param node The tree node containing the current board state from which the rollout starts.
     *             The board state represents the position to simulate from.
     * @return The evaluation score of the rollout:
     *         - A normalized score (e.g., between -1 and 1) representing the favorability of the position
     *           for the current player.
     *         - 1.0 for a win, -1.0 for a loss, and a value in-between for other positions.
     */
    double rollout(TreeNode node);
}
