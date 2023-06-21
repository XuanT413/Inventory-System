package components;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public final String ACCOUNT_SID = "NA";
    public final String AUTH_TOKEN = "NA";
    private final String MyNumber = "+122345";
    private final String HughNumber = "+12345";
    public void sendMessage(String text){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber(HughNumber),
                        new PhoneNumber("+1234567"),
                        "Hello Anh Hieu, This is a test message from Xuan Anh." +
                        "These items needs to be restocked " + text)
                .create();
        System.out.println(message.getSid());

    }
}

