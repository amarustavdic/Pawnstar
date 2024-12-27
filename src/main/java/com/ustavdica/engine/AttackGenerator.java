package com.ustavdica.engine;

public class AttackGenerator {



    /**
     * Generates an attack mask for all black pieces.
     *
     * @param position The current state of the board.
     * @return A bitboard with bits set for all squares attacked by black pieces.
     */
    public long getBlackAttackMask(Position position) {
        long attacks = 0L;

        // Generate attacks for each piece type
        attacks |= generatePawnAttacks(position.getBlackPawns(), false);
        attacks |= generateKnightAttacks(position.getBitboard(7)); // Black Knights
        attacks |= generateSlidingAttacks(position.getBitboard(8), position.getAllOccupiedSquares(), true); // Black Bishops
        attacks |= generateSlidingAttacks(position.getBitboard(9), position.getAllOccupiedSquares(), false); // Black Rooks
        attacks |= generateSlidingAttacks(position.getBitboard(10), position.getAllOccupiedSquares(), true); // Black Queen
        attacks |= generateKingAttacks(position.getBitboard(11)); // Black King

        return attacks;
    }



    /**
     * Generates an attack mask for all white pieces.
     *
     * @param position The current state of the board.
     * @return A bitboard with bits set for all squares attacked by white pieces.
     */
    public long getWhiteAttackMask(Position position) {
        long attacks = 0L;

        // Generate attacks for each piece type
        attacks |= generatePawnAttacks(position.getWhitePawns(), true);
        attacks |= generateKnightAttacks(position.getBitboard(1)); // White Knights
        attacks |= generateSlidingAttacks(position.getBitboard(2), position.getAllOccupiedSquares(), true); // White Bishops
        attacks |= generateSlidingAttacks(position.getBitboard(3), position.getAllOccupiedSquares(), false); // White Rooks
        attacks |= generateSlidingAttacks(position.getBitboard(4), position.getAllOccupiedSquares(), true); // White Queen
        attacks |= generateKingAttacks(position.getBitboard(5)); // White King

        return attacks;
    }


    // ---- Piece-specific attack generation ----

    /**
     * Generates an attack mask for pawns.
     *
     * @param pawns   The bitboard representing pawns of one color.
     * @param isWhite True if the pawns are white, false for black pawns.
     * @return A bitboard with bits set for all squares attacked by the pawns.
     */
    private long generatePawnAttacks(long pawns, boolean isWhite) {
        if (isWhite) {
            return ((pawns << 7) & ~0x8080808080808080L) | ((pawns << 9) & ~0x0101010101010101L);
        } else {
            return ((pawns >> 7) & ~0x0101010101010101L) | ((pawns >> 9) & ~0x8080808080808080L);
        }
    }

    /**
     * Generates an attack mask for knights.
     *
     * @param knights The bitboard representing knights of one color.
     * @return A bitboard with bits set for all squares attacked by the knights.
     */
    private long generateKnightAttacks(long knights) {
        long attacks = 0L;
        while (knights != 0) {
            long knight = Long.lowestOneBit(knights);
            attacks |= generateSingleKnightAttacks(knight);
            knights &= knights - 1; // Remove the processed knight
        }
        return attacks;
    }

    /**
     * Generates attacks for a single knight.
     *
     * @param knight The bitboard with a single knight.
     * @return A bitboard with bits set for all squares attacked by the knight.
     */
    private long generateSingleKnightAttacks(long knight) {
        long l1 = (knight & ~0x8080808080808080L) >>> 1;
        long l2 = (knight & ~0xC0C0C0C0C0C0C0C0L) >>> 2;
        long r1 = (knight & ~0x0101010101010101L) << 1;
        long r2 = (knight & ~0x0303030303030303L) << 2;
        long h1 = l1 | r1;
        long h2 = l2 | r2;
        return (h1 << 16) | (h1 >>> 16) | (h2 << 8) | (h2 >>> 8);
    }

    /**
     * Generates an attack mask for sliding pieces (bishops, rooks, queens).
     *
     * @param pieces    The bitboard representing the sliding pieces of one type.
     * @param occupied  The bitboard of all occupied squares on the board.
     * @param isDiagonal True for bishops/queens (diagonal moves), false for rooks/queens (straight moves).
     * @return A bitboard with bits set for all squares attacked by the sliding pieces.
     */
    private long generateSlidingAttacks(long pieces, long occupied, boolean isDiagonal) {
        long attacks = 0L;
        while (pieces != 0) {
            long piece = Long.lowestOneBit(pieces);
            attacks |= generateSingleSlidingAttacks(piece, occupied, isDiagonal);
            pieces &= pieces - 1; // Remove the processed piece
        }
        return attacks;
    }

    /**
     * Generates attacks for a single sliding piece (bishop, rook, queen).
     *
     * @param piece     The bitboard with a single sliding piece.
     * @param occupied  The bitboard of all occupied squares on the board.
     * @param isDiagonal True for bishops/queens, false for rooks/queens.
     * @return A bitboard with bits set for all squares attacked by the piece.
     */
    private long generateSingleSlidingAttacks(long piece, long occupied, boolean isDiagonal) {
        // Placeholder for sliding attack calculation using ray attacks or magic bitboards
        return 0L; // Implement using your preferred method (e.g., magic bitboards)
    }

    /**
     * Generates an attack mask for kings.
     *
     * @param king The bitboard representing a king of one color.
     * @return A bitboard with bits set for all squares attacked by the king.
     */
    private long generateKingAttacks(long king) {
        long attacks = (king << 1) & ~0x0101010101010101L; // Left
        attacks |= (king >>> 1) & ~0x8080808080808080L;    // Right
        long h = king | attacks; // Horizontal moves
        attacks |= (h << 8) | (h >>> 8); // Vertical moves
        return attacks;
    }


}
