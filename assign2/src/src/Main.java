import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
class Main{
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        Game.updateRanking("john_doe123",Game.rank);
        int count = Player.count;
        server.startGame(count);
    }
}

