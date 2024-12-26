package com.ustavdica;

public class Main {
    public static void main(String[] args) {

        BoardManipulator manipulator = new BoardManipulator();
        BoardState boardState = new BoardState();

        boardState.print(true, true);
        boardState.makeMove(8, 17);
        boardState.print(false, true);

    }

}