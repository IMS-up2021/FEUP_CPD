import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


class Main{
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        server.getWords("assign2/resources/words.txt");
        int count = Player.count;
        server.startGame(count);

        System.out.println("Exiting");
    }
}

