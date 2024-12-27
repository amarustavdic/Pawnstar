package com.ustavdica.engine.search.strategies.mcts.rollout;

import com.ustavdica.engine.Position;
import com.ustavdica.engine.Move;
import com.ustavdica.engine.search.strategies.mcts.TreeNode;

import java.util.List;
import java.util.Random;

public class RandomRollout implements RolloutStrategy {

    private static final int MAX_DEPTH = 50;
    private final Random random = new Random();

    @Override
    public double rollout(TreeNode node) {

        // Clone BoardState to avoid modifying the original one
        Position state = new Position(node.getState());
        int depth = 0;

        // Perform random moves up to the maximum depth or until the game ends
        while (!state.isTerminal() && depth < MAX_DEPTH) {

            // TODO: Here need to get all legal moves for the given state
            List<Move> legalMoves = null;
            if (legalMoves.isEmpty()) {
                break; // No legal moves -> game is over (stalemate or checkmate)
            }

            // Select a random move and apply it
            Move randomMove = legalMoves.get(random.nextInt(legalMoves.size()));
            // state.makeMove(randomMove);
            depth++;
        }

        // Evaluate the game state at the end of the rollout
        return random.nextDouble();
    }

    /**
     * Evaluates the game state at the end of the rollout.
     *
     * @param state The board state to evaluate.
     * @return A normalized score between -1 and 1:
     * - 1.0 for a win for the current player.
     * - -1.0 for a loss for the current player.
     * - 0.5 for a draw.
     * - A heuristic evaluation scaled to [-1, 1] for intermediate states.
     */
    private double evaluateGameState(Position state) {

//        if (boardState.isWinningState()) {
//            return 1.0; // Current player wins
//        } else if (boardState.isLosingState()) {
//            return -1.0; // Current player loses
//        } else if (boardState.isDraw()) {
//            return 0.5; // Draw
//        }
//
//        // Use a heuristic evaluation for intermediate states
//        HeuristicEvaluator evaluator = new HeuristicEvaluator();
//        double rawScore = evaluator.evaluate(boardState);
//
//        // Normalize the score to [-1, 1]
         return normalizeScore(93.23094);
    }

    /**
     * Normalizes a raw evaluation score to the range [-1, 1].
     *
     * @param score The raw score from the evaluator.
     * @return The normalized score.
     */
    private double normalizeScore(double score) {
        double maxScore = 100.0;
        double minScore = -100.0;

        return (score - minScore) / (maxScore - minScore) * 2 - 1;
    }

}
