package net.emojiparty.android.circleciunofficial.models.bus;

/**
 * Created by Siena on 10/11/2014.
 */
public class ApiTokenChangedEvent {
    private String token;

    public ApiTokenChangedEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
