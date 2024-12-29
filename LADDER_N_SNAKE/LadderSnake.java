import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class LadderSnake {

    static class Player {
        String name;
        int currentPosition;      // curr position will range from 0 to (N^2 -1)

        Player(String name, int currentPosition) {
            this.name = name;
            this.currentPosition = currentPosition;
        }
    }


    static class Board {
        Cell[][] cells;

        Board(int n,int snakesCount,int laddersCount){
            initializeBoard(n);
            addSnakesLadder(n,snakesCount,laddersCount);
        }

        void initializeBoard(int n){
            cells = new Cell[n][n];
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    Cell cellObj = new Cell();
                    cells[i][j]=cellObj;
                }
            }
        }
        void addSnakesLadder(int n, int snakesCount,int laddersCount){
            for(int i=0;i<snakesCount;i++){
                Boolean placed = false;
                while (!placed) {
                    int start = ThreadLocalRandom.current().nextInt(n, n * n);
                    if (cells[start / n][start % n].jump == null) {
                        int end = ThreadLocalRandom.current().nextInt(0, start);
                        Jump jump = new Jump(start, end);
                        cells[start / n][start % n].jump = jump;
                        placed = true;
                    }
                }
            }
            for(int i=0;i<laddersCount;i++){
                Boolean placed = false;
                while (!placed) {
                    int start = ThreadLocalRandom.current().nextInt(0, n * (n - 1));
                    if (cells[start / n][start % n].jump == null) {
                        int end = ThreadLocalRandom.current().nextInt(start + 1, n * n);
                        Jump jump = new Jump(start, end);
                        cells[start / n][start % n].jump = jump;
                        placed = true;
                    }
                }
            }
        }

        Cell getCell(int playerPosition) {
            int boardRow = playerPosition / cells.length;
            int boardColumn = (playerPosition % cells.length);
            return cells[boardRow][boardColumn];
        }    
    }


    static class Cell{
        Jump jump;
    }


    static class Jump{
        int start;
        int end;

        Jump(int start,int end){
            this.start=start;
            this.end=end;
        }
    }


    static class Dice {
        int diceCount;

        Dice(int diceCount) {
            this.diceCount = diceCount;
        }

        public int rollDice() {
            int randomInt = ThreadLocalRandom.current().nextInt(1, 7);
            return randomInt;
        }
    }

    
    public static boolean isValidEntry(int smaller, int greater) {
        return smaller <= greater;
    }

    
    static class Game {
        Board board;
        Dice dice;
        Deque<Player> playersList = new LinkedList<>();
        Boolean winner = false;

        Game(Scanner buffer) {
            initializeGame(buffer);
        }

        public void initializeGame(Scanner buffer) {

            System.out.println("Enter the Size of N*N Board");
            int n = buffer.nextInt();
            System.out.println("Enter the number of Snakes you want in board");
            int snakesCount = buffer.nextInt();
            if(!isValidEntry(snakesCount,(n*n)/4)){
                System.out.println("Invalid entry, Total Snakes should not Exceed 1/4th of Board Size");
                return;
            }
            System.out.println("Enter the number of Ladders you want in board");
            int laddersCount = buffer.nextInt();
            if(!isValidEntry(laddersCount,(n*n)/4)){
                System.out.println("Invalid entry, Total Ladders should not Exceed 1/4th of Board Size");
                return;
            }
            if(!isValidEntry(snakesCount+laddersCount,(n*n)/4)){
                System.out.println("Invalid entry, Total Snakes + Ladders should not Exceed 1/4th of Board Size");
                return;
            }

            board = new Board(n,snakesCount,laddersCount);

            System.out.println("Enter the number of Dices you want to play with");
            int diceCount = buffer.nextInt();

            dice = new Dice(diceCount);

            addPlayers(buffer);
        }

        public void addPlayers(Scanner buffer) {
            System.out.println("Enter the name of Player1");
            String player1Name = buffer.next();
            System.out.println("Enter the name of Player2");
            String player2Name = buffer.next();

            Player player1 = new Player(player1Name, 0);
            Player player2 = new Player(player2Name, 0);
            playersList.add(player1);
            playersList.add(player2);
        }

        public void start(Scanner buffer) {
            while(!winner){
                Player currentPlayer = playersList.removeFirst();
                playersList.addLast(currentPlayer); 
                System.out.println("Player:" + currentPlayer.name + " your current position is: " + currentPlayer.currentPosition);
                System.out.println("Player:" + currentPlayer.name + " Enter 1 to roll the Dice");
                while(true){
                    int value = buffer.nextInt();
                    if(value==1){
                        System.out.println("Dice Rolling Begins");
                        break;
                    }
                    else{
                        System.out.println("Player:" + currentPlayer.name + " Enter 1 to roll the Dice");
                    }
                }

                int diceNumber = dice.rollDice();
                System.out.println("Player:" + currentPlayer.name + " dice number is: " + diceNumber);
                int newPosition = currentPlayer.currentPosition + diceNumber;
                newPosition = jumpCheck(newPosition);
                currentPlayer.currentPosition = newPosition;
                System.out.println("Player:" + currentPlayer.name + " your new position is: " + newPosition);
                if(newPosition >= board.cells.length * board.cells.length-1){
                    System.out.println("Congrats:" + currentPlayer.name + " You win ");
                    winner = true;
                }
            }
        }

        private int jumpCheck (int playerNewPosition) {

            if(playerNewPosition > board.cells.length * board.cells.length-1 ){
                return playerNewPosition;
            }
    
            Cell cell = board.getCell(playerNewPosition);
            if(cell.jump != null && cell.jump.start == playerNewPosition) {
                String jumpBy = (cell.jump.start < cell.jump.end)? "ladder" : "snake";
                System.out.println("jump done by: " + jumpBy);
                return cell.jump.end;
            }
            return playerNewPosition;
        }
    
    }


    public static void main(String args[]) {
        Scanner buffer = new Scanner(System.in);
        Game game = new Game(buffer);
        game.start(buffer);
        buffer.close();
    }
}
