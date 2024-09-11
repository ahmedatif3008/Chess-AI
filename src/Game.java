import Adversarial.*;
import Pawntastic.Board;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Game {
    public void PlayGame(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("\nPawntastic!\n");

        System.out.println("Choose your game: ");

        System.out.println("4. Tiny 4x4 Pawntastic");
        System.out.println("5. Very small 5x5 Pawntastic");
        System.out.println("6. Small 6x6 Pawntastic");
        System.out.println("8. Standard 8x8 Pawntastic");
        System.out.println("Or a enter an number n for a board of size n");

        boolean validSize = false;

        int size = 0;

        while (!validSize) {
            System.out.print("\nGame Size(4+) : ");

            try {
                size = scanner.nextInt();

                scanner.nextLine();

                if (size < 4) {
                    System.out.println("Invalid Size!");
                }
                else {
                    validSize = true;
                }
            }

            catch(InputMismatchException e){
                System.out.println("Invalid Size!");
                scanner.nextLine();
            }
        }

        System.out.println("\nChoose your opponent:");

        System.out.println("1. An agent that plays randomly");
        System.out.println("2. An agent that uses MINIMAX");
        System.out.println("3. An agent that uses MINIMAX with alpha-beta pruning");
        System.out.println("4. An agent that uses H-MINIMAX with alpha-beta pruning");

        boolean validGameChoice = false;

        int opponentChoice = 0;

        while (!validGameChoice) {
            System.out.print("\nOpponent (1-4) : ");

            try {
                opponentChoice = scanner.nextInt();

                if (opponentChoice < 1 || opponentChoice > 4) {
                    System.out.println("Invalid Game Choice");
                } else {
                    validGameChoice = true;
                }
            }

            catch(InputMismatchException e){
                System.out.println("Invalid Game Choice");
                scanner.nextLine();
            }
        }

        int depth = -1;

        boolean validDepth = false;

        if (opponentChoice == 4) {
            while (!validDepth) {
                System.out.println("\nEnter cutoff depth: ");
                System.out.print("Cutoff Depth (1+) : ");

                try {
                    depth = scanner.nextInt();

                    if (depth < 1) {
                        System.out.println("Invalid Cutoff Depth");
                    } else {
                        validDepth = true;
                    }
                }

                catch(InputMismatchException e){
                    System.out.println("Invalid Cutoff Depth");
                    scanner.nextLine();
                }

            }
        }

        boolean validPlayerChoice = false;

        char userColor = 0;

        scanner.nextLine();

        while (!validPlayerChoice) {
            System.out.print("\nWhich player do you want to play as WHITE (W) or BLACK (B):  ");

                String input = scanner.nextLine();

                userColor = 'e';

                switch (input) {
                    case ("W"), ("w") -> {
                        userColor = 'w';
                        validPlayerChoice = true;
                    }
                    case ("B"), ("b") -> {
                        userColor = 'b';
                        validPlayerChoice = true;
                    }
                    default -> System.out.println("Invalid Color");
                }
        }

        Board board = new Board(size, userColor);
        board.gameDisplay();

        String AI_MOVE;

        while (true) {

            if (board.isUserMove()) {
                System.out.print("\n\nYour Move (Example: a2-b3): ");
                String move = scanner.nextLine();

                if(board.symbolConverter(move)!=null) {
                    board.playGame(board.getCurrentPlayer(), move);
                    board.gameDisplay();

                    if (board.checkWin() == board.getUser()) break;
                }
            }

            else {
                AI_MOVE = getAIMove(opponentChoice, depth, board);

                System.out.println("\n\nMove by AI IS " + AI_MOVE);
                if (AI_MOVE.equals("None")) {
                    System.out.println("Game's a Tie!");
                    break;
                }

                board.playGame(board.getCurrentPlayer(), AI_MOVE);
                board.gameDisplay();
                if (board.checkWin() == board.getAI()) break;
            }
        }

        System.out.println("\n\n Game Over!");
    }

    public static String getAIMove(int opponentChoice, int depth, Board board){
        String move = "";
        Action a;

        if(opponentChoice == 1){
            move = board.randomMover(board.getAI());
        }

        if(opponentChoice == 2){
            State s = new State(board);
            a = Minimax.miniMaxSearch(s);
            move = a.get_first();
        }

        if(opponentChoice == 3){
            AlphaBeta s = new AlphaBeta(board);
            a = MinimaxAB.miniMaxSearchAB(s);
            move = a.get_first();
        }

        if(opponentChoice == 4){
            AlphaBeta s = new AlphaBeta(board);
            a = HeuristicMinimaxAB.heuristicMinimaxAB(s, depth);
            move = a.get_first();
        }

        return move;
    }
}
