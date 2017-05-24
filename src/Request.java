import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by Irindul on 24/05/2017.
 */
public class Request {
    private String method;
    private String file;

    public Request(String line) {
        try {
            if (line != null) {

                //Parsing request
                StringTokenizer parse = new StringTokenizer(line);

                method = parse.nextToken().toUpperCase();
                file = parse.nextToken().toLowerCase();
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error : " + e.getMessage());
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
}
