package model;

/**
 * Created by raniamakki on 5/2/18.
 */
 public interface NetworkRequestListener<FirebaseResponse> {

        void onExecuted(FirebaseResponse firebaseResponse); // Retrofit network call executed
        void onFailure(Throwable t); // Retrofit network call failed

}

