import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Shauny on 24-May-17.
 */
public class Client implements Runnable {
    private String adr;
    private int port;
    private String url;
    private Socket socket;
    private View view;

    private CookieHandler cookieHandler;
    private boolean image;

    public Client(View view) {
        this.view = view;
        cookieHandler = new CookieHandlerClient();
    }

    @Override
    public void run() {

        try {
            socket = new Socket(InetAddress.getByName(adr), port);
            PrintWriter pw;
            pw = new PrintWriter(socket.getOutputStream());
            pw.println("GET " + url + " HTTP/1.1");

            pw.println(cookieHandler.parse());

            pw.println();
            pw.flush();

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;

            //Reading header
            StringBuilder sb = new StringBuilder();

            int j = 0;
            while ((line = br.readLine()) != null && line.length() > 0) {
                sb.append(line).append("\n");
            }
            String header = sb.toString();
            cookieHandler.store(header);

            int length = parseHeader(header);
            view.notifyHeader(image);
           // br.close();
            //Reading content according to type
            if(image){
                byte[] bytes = new byte[length];
                System.out.println(length);
               /* int c;
                int i = 0;
                while( (c = br.read()) >= 0){
                    bytes[i++] = (byte) c;
                }*/

               // System.out.println(Arrays.toString(bytes));
                if(url.startsWith("/")){
                    url = url.substring(1);
                }

               InputStream inputStream = socket.getInputStream();
               inputStream.read(bytes);
               System.out.println(Arrays.toString(bytes));

                File file = new File(url);
                file.getParentFile().mkdirs();

                FileOutputStream fos =  new FileOutputStream(file);
                fos.write(bytes);

                inputStream.close();
                fos.close();

                view.notifySuccess(bytes);
            } else {
                sb = new StringBuilder();
                while ((line = br.readLine()) != null && line.length() > 0) {
                    sb.append(line).append("\n");
                }
                view.notifySuccess(sb.toString());
            }



           // br.close();
            pw.close();
        } catch (IOException e) {
            view.notifyError();
            System.err.println("Impossible de se connecter à l'adresse " + adr + ":" + port + " : ");
            e.printStackTrace();
        }


    }

    private int parseHeader(String header) {
        StringTokenizer parse = new StringTokenizer(header);
        while (parse.hasMoreTokens()) {
            String s = parse.nextToken();
            if ("Content-Type:".equals(s)) {
                String type = parse.nextToken();
                image = type.contains("image");
            }
            if("Content-length:".equals(s)){
                String lengthString = parse.nextToken();
                return Integer.parseInt(lengthString);
            }
        }
        return 0;
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
