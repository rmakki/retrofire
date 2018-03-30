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
            // 1- pass the path and an object (could be a class you have defined or a data structure)
            // 2- pass the path and a MAP object
            // 3- pass the path and raw json data

            // PUT request passing path and an object
            // Object to be passed
            UserDetails user1 = new UserDetails("uid1",100,150,70, "I love travel and discovering new cultures");
            fbSvc.put("userDetails/uid1",user1);

            // PUT request passing raw json data
            // let's add another user
            String rawjson = "{\"userUID\":\"uid2\",\"nb_followers\":\"4000\",\"nb_following\":\"1000\",\"nb_posts\":\"300\"}";
            fbSvc.put("userDetails/uid2",rawjson);

            // PATCH request passing a MAP
            // Let's add bio information to the user we just added (uid2)
            LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
            datamap.put("bio", "Musician/Band");
            fbSvc.patch("userDetails/uid2",datamap);

            // POST
            // User uid1 posts a photo, let's save the info
            datamap.clear();
            datamap.put("description","Enjoying this beautiful sunset, where are you traveling next?");
            datamap.put("imageLink","https://myclouddatastorage/image231");
            FirebaseResponse response = fbSvc.post("userPosts/uid1",datamap);

            /*
            // Retrieve the firebase unique id generated with the POST so that we use it in the next example
            if (response.isSuccess()) {
                response.getBody().

            }

            // POST
            // someone comments on the previous post, let's save the comment
            datamap.clear();
            datamap.put("comment","Amazing shot!");
            fbSvc.post("userPosts/uid1/comments",datamap);
            */

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
