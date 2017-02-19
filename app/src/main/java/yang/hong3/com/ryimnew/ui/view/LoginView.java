package yang.hong3.com.ryimnew.ui.view;

/**
 * Created by hong3 on 2017-2-18.
 */

public interface LoginView {

    public void loginSuccess(String userId,String token);
    public void loginFails(String str);
}
