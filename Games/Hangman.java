package Games;

//* ServerSide
public class Hangman {
    private String SecretWord;
    private StringBuilder GuessedWord;
    private int attempts = 0;
    private String[] words = {
        "apple", "banana", "grape", "orange", "mango", "peach", "melon", "berry", "kiwi", "lemon",
        "pear", "plum", "cherry", "fig", "date", "coconut", "papaya", "passionfruit", "apricot", "nectarine",
        "carrot", "potato", "tomato", "onion", "garlic", "pepper", "cucumber", "pumpkin", "radish", "zucchini",
        "broccoli", "spinach", "lettuce", "cabbage", "cauliflower", "asparagus", "beet", "eggplant", "artichoke", "turnip",
        "dog", "cat", "fish", "bird", "rabbit", "hamster", "turtle", "frog", "lizard", "horse",
        "elephant", "giraffe", "zebra", "lion", "tiger", "bear", "wolf", "fox", "eagle", "shark",
        "car", "bike", "bus", "train", "plane", "boat", "scooter", "skateboard", "truck", "motorcycle",
        "computer", "tablet", "phone", "camera", "television", "radio", "printer", "speaker", "headphones", "monitor",
        "piano", "guitar", "drum", "violin", "flute", "trumpet", "saxophone", "harp", "clarinet", "accordion",
        "beach", "mountain", "forest", "river", "lake", "desert", "island", "valley", "hill", "canyon"
    };

    /**
        Constructor of the game, initializes the secret word and the guessed word
        The secret word is randomly selected from the words array
        The guessed word is initialized with underscores, defining the length of the secret word for the player
    */
    public Hangman(){
        SecretWord = words[(int)(Math.random() * words.length)];
        GuessedWord = new StringBuilder();
        for (int i = 0; i < SecretWord.length(); i++){
            GuessedWord.append("_");
        }
    }
    
    // Returns the welcome message of the game, with the lengyth of the secret word
    public String welcomeMessage(){
        return "Welcome to Hangman! > GUESS THE WORD: " + GuessedWord.toString();
    }

    // Guess only one character, if the char in contained in the secret word, it will be added to the guessed word
    // And will be returned with the updated guessed word and the attempts left
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

        String attempsLeft = "Attempts left: " + (6 - attempts);
        return GuessedWord.toString() + " (" + attempsLeft + ")";
    }

    // To Guess the entire word, this is necesary to win, if the word is correct, the game will end
    // if not, then the attempts will be increased
    public boolean guessWord(String word){
        word = word.toLowerCase();
        if (word.equals(SecretWord)){
           return true;
        }
        attempts++;
        return false;
    }

    // Checks if the game is over, by checking if the attempts are greater than 6
    public boolean endGame(){
        return attempts >= 6;
    }
}
