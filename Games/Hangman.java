package Games;

//* ServerSide
public class Hangman {
    private String SecretWord = "network"; //TODO Crear lista de palabras o palabras precedurales
    private StringBuilder GuessedWord = new StringBuilder("_______");
    private int attempts = 0;
    
    public String welcomeMessage(){
        return "Welcome to Hangman! > GUESS THE WORD: " + GuessedWord.toString();
    }

    public String guessChar(char letter){
        letter = Character.toLowerCase(letter);
        if (SecretWord.indexOf(letter) == -1){
            attempts++;
        }else{
            for (int i = 0; i < SecretWord.length(); i++){
                if (SecretWord.charAt(i) == letter){
                    GuessedWord.setCharAt(i, letter);
                }
            }
        }
        return GuessedWord.toString();
    }

    public boolean guessWord(String word){
        word = word.toLowerCase();
        if (word.equals(SecretWord)){
           return true;
        }
        attempts++;
        return false;
    }

    public boolean endGame(){
        return attempts >= 6;
    }
}
