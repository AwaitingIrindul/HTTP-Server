/**
 * Created by Irindul on 07/06/2017.
 */
public interface View {

    void notifyError();
    void notifySuccess(String result);
    void notifyHeader(String header);
}
