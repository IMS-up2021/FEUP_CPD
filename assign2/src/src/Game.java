import java.util.Random;
import java.util.Scanner;
public class Game {
    private final String[] WORDS = {"Foyer", "Louse", "Gloat", "Codon", "Finch", "Plane", "Voila", "Sweat", "Tasty", "Mouse", "Pixie", "Antic","Lemon", "Honey", "Queue", "Skirt", "Hello", "World", "Excel", "Brown", "Cache", "Water"};
    private final int MAX_ATTEMPS = 6;
    private String targetWord;
    private int attemptsLeft;

    public Game(){
        Random random = new Random();
        targetWord = WORDS[random.nextInt(WORDS.length)];
        attemptsLeft = MAX_ATTEMPS;
    }

    public void play(){
        Scanner scanner = new Scanner(System.in);
        boolean wordGuessed = false;
        while(attemptsLeft > 0 && !wordGuessed){
            String guess = scanner.nextLine().toLowerCase();
            if(guess.length()!=5){
                continue;
            }
            if(!guess.matches("[a-z]+")){
                continue;
            }
            if(guess.equals(targetWord)){
                wordGuessed = true;
            }
            else{
                attemptsLeft--;
            }
        }
        scanner.close();
        if(wordGuessed){
            System.out.println("YOU WON!");
        }
        else{
            System.out.println("YOU LOST");
        }
    }

}
