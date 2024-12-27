package com.ustavdica;

public class Main {
    public static void main(String[] args) {

        BoardManipulator manipulator = new BoardManipulator();
        BoardState boardState = new BoardState();

        boardState.print(true, false);
        boolean moveStatus = boardState.makeMove(8, 17);
        System.out.println(moveStatus);
        boardState.print(true, false);


        manipulator.generatePawnAttacks(boardState);
    }

}