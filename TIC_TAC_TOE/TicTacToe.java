import java.util.*;

public class TicTacToe{

    public enum PieceType{
        X,
        O
    }

    public static class PlayingPiece {
        public PieceType symbol;

        PlayingPiece(PieceType symbol){
            this.symbol = symbol;
        }

        public String toString() {
            return symbol.toString();
        }
    }

    public static class PlayingPieceX extends PlayingPiece{

        public PlayingPieceX(){
            super(PieceType.X);
        }
    }

    public static class PlayingPieceO extends PlayingPiece{

        public PlayingPieceO(){
            super(PieceType.O);
        }
    }

    public static class Player{
        private String name;
        private PlayingPiece piece;

        public Player(String name,PlayingPiece piece){
            this.name = name;
            this.piece = piece;
        } 

        public String getName(){
            return name;
        }

        public PlayingPiece getPlayingPiece(){
            return piece;
        }
    } 

    public static class Board {
        public int size;
        public PlayingPiece[][] board;

        public Board(int size){
            this.size=size;
            board = new PlayingPiece[size][size];
        }

        public boolean hasFreeCell(){
            for(PlayingPiece[] row: board){
                for(PlayingPiece cell:row){
                    if(cell==null) return true;
                }
            }
            return false;
        }

        public void printBoard(){
            for(PlayingPiece[] row : board){
                for(PlayingPiece cell : row){
                    if(cell!=null)
                    System.out.print(cell+" ");
                    else 
                    System.out.print(". ");
                }
                System.out.println();
            }
        }

        public boolean addPiece(int row,int col,PlayingPiece piece){
            if(row<0||row>2||col<0||col>2||board[row][col]!=null) return false;
            board[row][col]=piece;
            return true;
        }
    }

    Deque<Player> players;
    Board gameBoard;


    public void initializeGame(String name1,String name2){
       players = new LinkedList<>();
        PlayingPieceX xPiece = new PlayingPieceX();
        Player player1 = new Player(name1, xPiece);

        PlayingPieceO oPiece = new PlayingPieceO();
        Player player2 = new Player(name2, oPiece);

        players.add(player1);
        players.add(player2);

        gameBoard = new Board(3);
    }


    public void startGame(Scanner input){
       boolean winner = false;
       while(!winner){
        Player currentPlayer = players.removeFirst();
        if(gameBoard.hasFreeCell()){
            System.out.println("Player: "+currentPlayer.name+ " Enter row and col to place your piece");
            int row = input.nextInt();
            int col = input.nextInt();
            if(!gameBoard.addPiece(row, col, currentPlayer.piece)){
                players.addFirst(currentPlayer);
                System.out.println("Invalid move, Please enter valid move");
                continue;
            }
            gameBoard.printBoard();
            if(isWinner(currentPlayer.piece)){
                System.out.println("Congrats: "+currentPlayer.name+ " You are the Winner");
                winner = true;
            }
            players.addLast(currentPlayer);
        }
        else{
            System.out.println("Game Draw, no chance left to play");
            break;
        }
       }
    }
    
    public boolean isWinner(PlayingPiece piece){
        int ans = 0;
        for(PlayingPiece[] row:gameBoard.board){
            for(PlayingPiece cell: row){
                if(cell==piece) ans++;
            }
            if(ans==3) return true;
            ans=0;
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(gameBoard.board[j][i]==piece) ans++;
            }
            if(ans==3) return true;
            ans=0;
        }
    
        for(int i=0;i<3;i++){
            if(gameBoard.board[i][i]==piece) ans++;
        }
        if(ans==3) return true;
        ans=0;
        for(int i=0;i<3;i++){
            if(gameBoard.board[2-i][i]==piece) ans++;
        }
        if(ans==3) return true;
        return false;
    }
    

    public static void main(String[] args){
        TicTacToe game = new TicTacToe();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name Player1");
        String name1=input.next();
        System.out.print("Enter the name Player2");
        String name2=input.next();
        game.initializeGame(name1,name2);
        game.startGame(input);   
    }
}