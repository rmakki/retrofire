package model;

/**
 * Created by raniamakki on 9/22/17.
 */
public class FirebaseResponse {

    private final int code; // code returned by Firebase
    private final String message; // message returned by Firebase
    private boolean success; // True if Http request successfully complete
    private String body; // Will either contain data or null or error details sent back from Firebase


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







