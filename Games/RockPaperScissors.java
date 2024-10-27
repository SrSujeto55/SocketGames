package Games;
import java.util.Random;

public class RockPaperScissors {

    // Returns the welcome message of the game, with the rules to play
    public String welcomeMessage(){
        return "Welcome to Rock Paper Scissors game!\nType 'rock', 'paper' or 'scissors' to play, exit to quit";
    }

    // Make a move, inmediatly after the player make a move, the computer selects a random move bettwen the three possible 
    // options and determine the game results
    public String play(String playerMove){
        if (!playerMove.equals("rock") && !playerMove.equals("paper") && !playerMove.equals("scissors")){
            return "Invalid move";
        }
        String[] moves = {"rock", "paper", "scissors"};
        Random random = new Random();
        int index = random.nextInt(moves.length);
        String computerMove = moves[index];
        if(playerMove.equals(computerMove)){
            return "(" + computerMove + ") It's a tie!";
        }
        if(playerMove.equals("rock")){
            if(computerMove.equals("scissors")){
                return "(" + computerMove + ") You win!";
            }else{
                return "(" + computerMove + ") You lose!";
            }
        }

        if(playerMove.equals("paper")){
            if(computerMove.equals("rock")){
                return "(" + computerMove + ") You win!";
            }else{
                return "(" + computerMove + ") You lose!";
            }
        }

        if(playerMove.equals("scissors")){
            if(computerMove.equals("paper")){
                return "(" + computerMove + ") You win!";
            }else{
                return "(" + computerMove + ") You lose!";
            }
        }

        return "Invalid move";
    }
}
