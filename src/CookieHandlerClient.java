/**
 * Created by Irindul on 07/06/2017.
 */
public class CookieHandlerClient extends CookieHandler {


    @Override
    protected boolean header(String s) {
        return "Set-Cookie:".equals(s);
    }

    @Override
    protected String type() {
        return "Cookie: ";
    }
}
