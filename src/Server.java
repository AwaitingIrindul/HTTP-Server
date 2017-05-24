import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Irindul on 24/05/2017.
 */
public class Server implements Runnable {

    private ServerSocket serverSocket;

    public Server() {
        this(8080); //Default port is 80
    }

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error when opening socket on " + port + " : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                Socket socket = serverSocket.accept();

                //New connection
                Connection connection = new Connection(socket);
                new Thread(connection).start();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) {
        Server s = new Server(8080);
        new Thread(s).start();
    }
}
