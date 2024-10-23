package Games;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe {
    private char[][] board;
    private Random rand;
    private List<Integer> availableMoves;

    public TicTacToe(){
        board = new char[3][3];
        rand = new Random();
        availableMoves = new ArrayList<Integer>();
        for (int i = 1; i < 10; i++){
            availableMoves.add(i);
        }
        for (int ii = 0; ii < 3; ii++){
            for (int iii = 0; iii < 3; iii++){
                board[ii][iii] = '+';
            }
        }
    }

    public String welcomeMessage(){
        return "Welcome to Tic Tac Toe!, select your number";
    }

    public String checkWinner(){
        for(int i = 0; i < 3; i++){
            if(board[i][0] == board[i][1] && board[i][1] == board[i][2]){
                if(board[i][0] == 'X'){
                    return "You win!";
                }else if(board[i][0] == 'O'){
                    return "You lose!";
                }
            }
            if(board[0][i] == board[1][i] && board[1][i] == board[2][i]){
                if(board[0][i] == 'X'){
                    return "You win!";
                }else if(board[0][i] == 'O'){
                    return "You lose!";
                }
            }
        }
        if(board[0][0] == board[1][1] && board[1][1] == board[2][2]){
            if(board[0][0] == 'X'){
                return "You win!";
            }else if(board[0][0] == 'O'){
                return "You lose!";
            }
        }
        if(board[0][2] == board[1][1] && board[1][1] == board[2][0]){
            if(board[0][2] == 'X'){
                return "You win!";
            }else if(board[0][2] == 'O'){
                return "You lose!";
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(board[i][j] == '+'){
                    return "NONE";
                }
            }
        }

        return "It's a tie!";
    }
    
    public String printTableIndex(){
        return " 1 | 2 | 3 \n"
             + "---|---|---\n"
             + " 4 | 5 | 6 \n"
             + "---|---|---\n"
             + " 7 | 8 | 9 \n";
    } 

    public String printBoard(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                sb.append(" " + board[i][j] + "");
                if(j < 2){
                    sb.append(" | ");
                }
            }
            sb.append("\n");
            if(i < 2){
                sb.append("---|----|---\n");
            }
        }
        return sb.toString();
    }

    private void randMove(){
        if (availableMoves.isEmpty()){
            return;
        }
        int number = rand.nextInt(availableMoves.size());
        number = availableMoves.get(number);
        int row = (number - 1) / 3;
        int col = (number - 1) % 3;
        board[row][col] = 'O';
        availableMoves.remove(Integer.valueOf(number));
    }

    public String playerMove(String numb){
        try{
            int number = Integer.parseInt(numb);
            if (number < 1 || number > 9){
                return "Invalid number, try again";
            }
            int row = (number - 1) / 3;
            int col = (number - 1) % 3;
            if(board[row][col] == '+'){
                board[row][col] = 'X';
                availableMoves.remove(Integer.valueOf(number));
                randMove();
                return printBoard();
            }
            return "Invalid move, try again";
        }catch(NumberFormatException e){
            return "Invalid number, try again";
        }
    }
}
