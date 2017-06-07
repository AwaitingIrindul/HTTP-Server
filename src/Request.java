import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by Irindul on 24/05/2017.
 */
public class Request {
    private String method;
    private String file;
    private CookieHandler cookieHandler;

    public Request(String line) {
        try {
            if (line != null) {

                //Parsing request
                StringTokenizer parse = new StringTokenizer(line);

                method = parse.nextToken().toUpperCase();
                file = parse.nextToken().toLowerCase();

                cookieHandler = new CookieHandler();
            } else {
                method = "GET";
                file = "/";
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error : " + e.getMessage());
        }
    }

    public void setCookies(String line){
        cookieHandler.store(line);
    }

    public String getMethod() {
        return method;
    }

    public String getFile() {
        return file;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Map<String, String> getCookies() {
        return cookieHandler.getCookies();
    }
}
