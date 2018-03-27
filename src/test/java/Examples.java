import model.FirebaseResponse;
import service.FirebaseSvc;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Examples {

    public static void main(String[] args) {

        // change to your firebase reference url
        String FIREBASE_REF = "https://smartplayTest.firebaseio.com/";

        // Create firebase reference with Full logging. Make sure you set to false in production
        FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF,true);

        // examples

        try {
            // Delete
            FirebaseResponse firebaseResponse = fbSvc.delete(""); // Delete root (clears data from previous tests)


            // PUT

            // deletes the key and everything under it
            // firebaseResponse = fbSvc.delete("users/-KvcV06UQENpcyPdzCKm");

        }

        catch (IOException e) { // Network error
            System.out.println("Network error " + e );

        }
        catch (Exception e) { // Unexpected/parse error
            System.out.println("Unexpected/parse error " + e );

        }

    }

}
