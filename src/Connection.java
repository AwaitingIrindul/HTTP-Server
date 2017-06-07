import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irindul on 24/05/2017.
 */
public class Connection implements Runnable{

    private final String HTTP_VERSION = "1.1";
    private final int TIMEOUT = 60000; // timeout after one minute


    private final String ROOT = "/Users/Irindul/Documents/www"; //Root of the sources for the server

    private Socket socket;
    private PrintWriter out;

    HashMap<Integer, String> errors = new HashMap<>();

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            socket.setSoTimeout(TIMEOUT);
            setErrors();
        } catch (SocketException e) {
            System.out.println("Socket timed out : " + e.getMessage());
        }
    }

    private void setErrors() {
        errors.put(100, "Continue");
        errors.put(200, "OK");
        errors.put(400, "Bad Request");
        errors.put(403, "Forbidden");
        errors.put(404, "Not Found");
        errors.put(408, "Request Time-out");
        errors.put(500, "Internal Server Error");
        errors.put(501, "Not Implemented");
        errors.put(505, "HTTP Version not supported");
    }

    private void httpHeader(int errorCode){
        out.println("HTTP/" + HTTP_VERSION + " " + errorCode + " " + errors.get(errorCode));
        out.println("Server: MathieuSimon");
        out.println("Date: " + new Date());
        out.println("Connection: close");
    }

    private void writeError(int errorCode) {
        httpHeader(errorCode);
        out.println("Content-Type: text/html");
        out.println();
        out.println("<HTML>");
        out.println("<HEAD><TITLE>" + errors.get(errorCode) + "</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("<H2>Error " + errorCode + ": " + errors.get(errorCode) + ". </H2>");
        out.println("</BODY>");
        out.println ("</HTML>");
        out.flush();
    }


    @Override
    public void run() {
        BufferedReader in = null;
        try{

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String req = in.readLine();
            Request request = new Request(req);
            StringBuilder sb = new StringBuilder();

            String line;
            while( (line = in.readLine() )!= null && line.length() > 0) {
                sb.append(line).append("\n");
            }
            request.setCookies(sb.toString());
            if(request.getMethod().equals("GET")){
                get(in, request);
            } else {
                warnUser(request);
            }

        } catch (SocketTimeoutException e) {
            System.err.println("Time Out : " + e.getMessage());
            writeError(408);
        } catch (IOException ex) {
            System.err.println("Erreur : " + ex.getMessage());
            writeError(500);
        } finally {
            close(in);
            close(out);
            close(socket);
        }
    }

    private void warnUser(Request request) {
        // Other method such as POST are not implemented
        // We notify the client
        if (request.getMethod().equals("HEAD")
                || request.getMethod().equals("PUT")
                || request.getMethod().equals("POST")
                || request.getMethod().equals("DELETE"))
            writeError(501);
        else
            writeError(400);
        // Otherwise it's a Bad Request
    }

    public void close(Object stream) {
        if (stream == null) {
            return;
        }
        try {
            if (stream instanceof Reader) {
                ((Reader) stream).close();
            } else if (stream instanceof Writer) {
                ((Writer) stream).close();
            } else if (stream instanceof InputStream) {
                ((InputStream) stream).close();
            } else if (stream instanceof OutputStream) {
                ((OutputStream) stream).close();
            } else if (stream instanceof Socket) {
                ((Socket) stream).close();
            } else {
                System.err.println("Unable to close object: " + stream);
            }
        } catch (Exception e) {
            System.err.println("Error closing stream: " + e);
        }
    }

    public String getContentType(String file) {
        if (file.endsWith(".htm") || file.endsWith(".html")) {
            return "text/html";
        } else if (file.endsWith(".gif")) {
            return "image/gif";
        } else if (file.endsWith(".jpg") || file.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (file.endsWith(".png")) {
            return "image/png";
        } else if (file.endsWith(".class") || file.endsWith(".jar")) {
            return "applicaton/octet-stream";
        }else if(file.endsWith(".pdf")){
            return "application/pdf";
        } else {
            return "text/plain";
        }
    }

    private void get(BufferedReader in, Request request) {
        if(request.getFile().endsWith("/"))
            //We return the index.html
            request.setFile(request.getFile() + "index.html");

        //Getting the requested file
        File file = new File(ROOT, request.getFile());
        System.out.println("Requested : " + file.getAbsolutePath());

        readCookies(request);

        byte[] fileData = new byte[(int) file.length()];
        try{
            FileInputStream fis = new FileInputStream(file);

            //Filling fileData with the content of the file
            fis.read(fileData);

            close(fis); //Closing stream

            httpHeader(200);
            out.println("Content-Type: "+ getContentType(request.getFile()));
            out.println("Content-length: " + fileData.length);
            out.println("Set-Cookie: ref=1234"); //Arbitrary cookie
            out.println(); // Extra line in the header (mandatory)
            out.flush();


            //Sending the file :
            BufferedOutputStream outData = new BufferedOutputStream(socket.getOutputStream());
            outData.write(fileData, 0, fileData.length);
            outData.flush();
            close(outData);

        } catch (FileNotFoundException ex) {
            writeError(404);

        } catch (IOException ex) {
            writeError(500);
        }
    }

    private void readCookies(Request request) {

        Map<String, String> cookies = request.getCookies();
        if(cookies != null){
            cookies.forEach((key, value) -> System.out.println("Cookie was sent from client: " + key + " set to " + value));
        } 
        
    }
}
