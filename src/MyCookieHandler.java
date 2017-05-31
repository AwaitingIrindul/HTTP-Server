import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.*;

/**
 * Created by Irindul on 31/05/2017.
 */
public class MyCookieHandler extends CookieHandler {
    private final Map<String, List<String>> cookies =
            new HashMap<>();

    @Override public Map<String, List<String>> get(URI uri,
                                                   Map<String, List<String>> requestHeaders) throws IOException {
        Map<String, List<String>> ret = new HashMap<>();
        synchronized (cookies) {
            List<String> store = cookies.get(uri.getHost());
            if (store != null) {
                store = Collections.unmodifiableList(store);
                ret.put("Cookie", store);
            }
        }
        return Collections.unmodifiableMap(ret);
    }

    @Override public void put(URI uri, Map<String, List<String>> responseHeaders)
            throws IOException {
        List<String> newCookies = responseHeaders.get("Set-Cookie");
        if (newCookies != null) {
            synchronized (cookies) {
                List<String> store = cookies.computeIfAbsent(uri.getHost(), k -> new ArrayList<>());
                store.addAll(newCookies);
            }
        }
    }
}
