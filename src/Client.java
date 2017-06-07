import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Shauny on 24-May-17.
 */
public class Client implements Runnable {
    private String adr;
    private int port;
    private String url;
    private Socket socket;
    private View view;

    private Map<String, String> cookies;

    public Client(View view) {
        this.view = view;
        cookies = new HashMap<>();
    }

    @Override
    public void run() {

        try {
            socket = new Socket(InetAddress.getByName(adr), port);
            PrintWriter pw;
            pw = new PrintWriter(socket.getOutputStream());
            pw.println("GET " + url + " HTTP/1.1");
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                pw.println("Cookie: " + entry.getKey() + "=" + entry.getValue());
                System.out.println("Cookie sent");
            }

            pw.println();
            pw.flush();

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            //Reading header
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null && line.length() > 0) {
                sb.append(line).append("\n");
            }
            String header = sb.toString();
            parseHeader(header);
            view.notifyHeader(header);

            //Reading content
            sb = new StringBuilder();
            while ((line = br.readLine()) != null && line.length() > 0) {
                sb.append(line).append("\n");
            }

            view.notifySuccess(sb.toString());

            br.close();
        } catch (IOException e) {
            view.notifyError();
            System.err.println("Impossible de se connecter à l'adresse " + adr + ":" + port + " : " + e.getMessage());
        }


    }

    // TODO: 07/06/2017 Refactor in static class CookieHandler.
    private void parseHeader(String header) {
        StringTokenizer parse = new StringTokenizer(header);
        while (parse.hasMoreTokens()) {
            String s = parse.nextToken();
            if (s.equals("Set-Cookie:")) {
                if (cookies == null)
                    cookies = new HashMap<>();
                String cookie = parse.nextToken();
                cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
            }
        }
    }

    private void readUrl() {
        Scanner sc = new Scanner(System.in);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void readAdr() {
        System.out.println("Enter the Host's IP Address");

        Scanner S = new Scanner(System.in);
        adr = S.nextLine();
    }

    public void readPort() {
        System.out.println("Enter the Host's Port");
        Scanner S = new Scanner(System.in);
        port = S.nextInt();
    }


}
