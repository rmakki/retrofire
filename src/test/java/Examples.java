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
            // The example below Deletes the root (clears data from previous tests)
            FirebaseResponse firebaseResponse = fbSvc.delete("");

            // There are three ways you can pass data to PUT POST and PATCH requests:
            // 1- pass the path and any object
            // 2- pass the path and a MAP object
            // 3- pass the path and raw json data

            // PUT request passing path and any object
            UserDetails user1 = new UserDetails("uid1",100,150,70, "I love travel and discovering new cultures");

            fbSvc.put("userDetails/uid1",user1);

            // PUT request passing a path and raw json data
            // let's add another user
            String rawjson = "{\"userUID\":\"uid2\",\"nb_followers\":\"4000\",\"nb_following\":\"1000\",\"nb_posts\":\"300\"}";

            fbSvc.put("userDetails/uid2",rawjson);

            //UserDetails user2 = new UserDetails("uid2",4000,1000,300, "Musician/Band");
            // LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
            // user2.put("uid2",)
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
