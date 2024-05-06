import java.util.Scanner;
class Wordle{
    public static void main(String[] args){

        final String BG_GREEN = "\u001b[42m";
        final String BG_YELLOW = "\u001b[43m";
        final String RESET = "\u001b[0m";


        System.out.println("WORDLE!");

        String [] words = {"FOYER", "LOUSE", "GLOAT", "CODON", "FINCH", "PLANE", "VOILA", "SWEAT", "TASTY", "MOUSE", "PIXIE", "ANTIC", "LEMON", "HONEY", "QUEUE", "SKIRT", "HELLO", "WORLD", "EXCEL", "BROWN", "CACHE", "WATER"};

        int index = (int)(Math.random()* words.length);
        String correct = words[index];

        Scanner reader = new Scanner(System.in);
        String guess;

        int attemps = 0;
        //6 guesses
        while(attemps < 6){
            System.out.print("Make your guess: ");
            guess = reader.nextLine().toUpperCase();

            if(guess.length()!=5){
                System.out.println("Please write a 5 letter word!");
                continue;
            }
            else{
                attemps++;
            }

            for(int j = 0; j < 5; j++){
                //letter matches
                if(guess.substring(j,j+1).equals(correct.substring(j,j+1))){
                    System.out.print(BG_GREEN + guess.charAt(j) + RESET);
                }
                //leter on the wrong place
                else if(correct.contains(guess.substring(j, j + 1))) {
                    System.out.print(BG_YELLOW + guess.charAt(j) + RESET);
                }
                //letter doesn't exist
                else{
                    System.out.print(guess.charAt(j));
                }
            }
            System.out.println("");
            if(guess.equals(correct)){
                System.out.println("YOU WON!");
                break;
            }
        }
    }
}

