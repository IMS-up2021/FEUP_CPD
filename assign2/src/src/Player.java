import java.net.Socket;

public class Player {
    private Socket socket;
    private String name;

    public Player(Socket socket) {
        this.socket = socket;
        // Assume player name is received from the client
        this.name = "Player " + socket.getPort();
    }

    public String getName() {
        return name;
    }
}

