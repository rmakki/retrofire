package model;

/**
 * Created by raniamakki on 9/22/17.
 */
public class FirebaseResponse {

    private final int code;
    private final String message;
    private boolean success;
    private String body;


    public FirebaseResponse(int code, String message, boolean success, String body ) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getBody() {
        return body;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return FirebaseResponse.class.getSimpleName() + "{" +
                "success=" + success +
                ", code=" + code +
                ", message=" + message +
                ", body='" + body + '\'' +
                '}';
    }
}







