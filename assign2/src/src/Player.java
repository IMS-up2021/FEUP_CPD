import java.net.Socket;


public class Player {
    private Socket socket;
    private String name;

    public static int count=0;

    public Player(Socket socket) {
        this.socket = socket;
        // Assume player name is received from the client
        this.name = "Player " + socket.getPort();
        count++;
    }

    public String getName() {
        return name;
    }
}

