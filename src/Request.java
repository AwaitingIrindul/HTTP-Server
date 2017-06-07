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
    private Map<String, String> cookies;

    public Request(String line) {
        try {
            if (line != null) {

                //Parsing request
                StringTokenizer parse = new StringTokenizer(line);

                method = parse.nextToken().toUpperCase();
                file = parse.nextToken().toLowerCase();
                cookies = new HashMap<>();
            } else {
                method = "GET";
                file = "/";
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error : " + e.getMessage());
        }
    }

    public void setCookies(String line){
        StringTokenizer parse = new StringTokenizer(line);
        while(parse.hasMoreTokens()){
            String s = parse.nextToken();
            if(s.equals("Cookie:")){
                if(cookies == null)
                    cookies = new HashMap<>();
                String cookie = parse.nextToken();
                cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
            }
        }
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
        return cookies;
    }
}
