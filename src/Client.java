import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Shauny on 24-May-17.
 */
public class Client
{
    private String adr;
    private int port;
    private String url;
    private Socket socket;

    private Map<String, String> cookies;

    public Client() {

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.readAdr();
        client.readPort();
        client.readUrl();

        try{
            client.socket = new Socket(InetAddress.getByName(client.adr), client.port);
            PrintWriter pw;
            pw = new PrintWriter(client.socket.getOutputStream());
            pw.println("GET " + client.url + " HTTP/1.1");
            pw.flush();


            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
            String t;
            while ((t = br.readLine()) != null) System.out.println(t);

            br.close();
        }
        catch (IOException e){
            System.err.println("Impossible de se connecter à l'adresse " + client.adr + ":" + client.port + " : " + e.getMessage());
        }


    }

    private void readUrl() {
        Scanner  sc = new Scanner(System.in);
        System.out.println("Enter the url requested");
        url = sc.nextLine();
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void readAdr(){
        System.out.println("Enter the Host's IP Address");

        Scanner S=new Scanner(System.in);
        adr=S.nextLine();
    }

    public void readPort(){
        System.out.println("Enter the Host's Port");
        Scanner S=new Scanner(System.in);
        port=S.nextInt();
    }


}
