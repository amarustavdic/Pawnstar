package com.ustavdica;

public class Move {

    private final int fromSquare;
    private final int toSquare;

    public Move(int fromSquare, int toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    @Override
    public String toString() {
        return "Move { from=" + fromSquare + ", to=" + toSquare + " }";
    }
}
