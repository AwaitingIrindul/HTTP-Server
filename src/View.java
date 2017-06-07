/**
 * Created by Irindul on 07/06/2017.
 */
public interface View {

    void notifyError(String s);
    void notifySuccess(String result);
    void notifyHeader(boolean image);
    void notifySuccess(byte[] result);
}
