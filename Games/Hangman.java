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

    public Hangman(){
        SecretWord = words[(int)(Math.random() * words.length)];
        GuessedWord = new StringBuilder();
        for (int i = 0; i < SecretWord.length(); i++){
            GuessedWord.append("_");
        }
    }
    
    
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

        String attempsLeft = "Attempts left: " + (6 - attempts);
        return GuessedWord.toString() + " (" + attempsLeft + ")";
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
