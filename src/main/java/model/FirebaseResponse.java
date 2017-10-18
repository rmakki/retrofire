package model;

/**
 * Created by raniamakki on 9/22/17.
 */
public class FirebaseResponse {

    private final int code;
    private boolean success;
    private final String rawBody;

    public FirebaseResponse(int code, boolean success, String rawBody ) {
        this.code = code;
        this.success = success;
        this.rawBody = rawBody;
    }

    public int getCode() {
        return code;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRawBody() {
        return rawBody;
    }


    @Override
    public String toString() {
        return FirebaseResponse.class.getSimpleName() + "{" +
                "success=" + success +
                ", code=" + code +
                ", rawBody='" + rawBody + '\'' +
                '}';
    }
}







