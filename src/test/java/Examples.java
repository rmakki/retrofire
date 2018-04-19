import com.google.gson.Gson;
import model.*;
import service.FirebaseSvc;
import java.io.IOException;
import java.util.LinkedHashMap;

public class Examples {

    public static void main(String[] args) {

        // change to your firebase reference url
        String FIREBASE_REF = "https://smartplayTest.firebaseio.com/";

        // Create firebase reference with Full logging. Make sure you set to false in production
        FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF, true);

        // examples

        try {
            // Delete
            // The example below Deletes the root (clears data from previous tests)
            fbSvc.delete("");

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

            FirebaseResponse firebaseResponse = fbSvc.post("userPosts/uid1",datamap);

            // Firebase generates a unique ID for each post request and returns it in the response
            // Below is a sample code in case you need to retrieve this unique ID from the FirebaseResponse
            // body field - which will contain a json string in the form of {"name":"<firebase unique id>"}
            // ex: {"name":"-L8v6-NuOg_9W6feuE6y"}

            FirebaseUID newPostUID=null;

            if (firebaseResponse.isSuccess()) {
                Gson gson = new Gson();
                newPostUID = gson.fromJson(firebaseResponse.getBody(), FirebaseUID.class);
                System.out.println("Unique ID generated by firebase for this POST request : " + newPostUID.getName());
            }

            // someone comments on the photo, let's save the comment under the node we just generated above
            datamap.clear();
            datamap.put("comment","Amazing shot!");
            fbSvc.post("userPosts/uid1/" + newPostUID.getName() + "/comments",datamap);


            // GET
            // Passing an empty string will retrieve all info under your firebase Root
            fbSvc.get("");

            // GET
            // Let's retrieve the bio of uid1
            UserDetails getUser;
            firebaseResponse = fbSvc.get("userDetails/uid1/");

            if (firebaseResponse.isSuccess()) {
                // Check if information found (firebase returns success with null body if path not found)
                if (!firebaseResponse.getBody().equals("null")) {
                    Gson gson = new Gson();
                    getUser = gson.fromJson(firebaseResponse.getBody(), UserDetails.class);
                    System.out.println("uid1 Bio : " + getUser.getBio());

                } else {
                    System.out.println("GET path not found");
                }
            }

            // GET with query parameters
            // Let's retrieve users with nb_followers equal or greater than 100
            fbSvc.addQueryParam("orderBy", "\"nb_followers\"");
            fbSvc.addQueryParam("startAt","500");
            firebaseResponse = fbSvc.get("userDetails");
            System.out.println("Users with at least 500 followers: " + firebaseResponse.toString());


            // GET - customized call example with POJO
            // You will have to modify FirebaseSvcApi and FirebaseSvc to match your use case
            model.UserDetails userDetails = fbSvc.getUserDetails("userDetails/uid1/");
            if (!(userDetails == null)) {
                // Found info
                System.out.print("Retrieving uid1 Bio directly : " + userDetails.getBio());
            }
            else {
                // Did not find info(we passed a non existing path), firebase returned null
                System.out.println("not found");
            }

        }

        catch (IOException e) { // Network error (for example timeouts)
            System.out.println("Network error " + e );

        }
        catch (Exception e) { // Unexpected/parse error (thrown by Retrofit for example if you pass null data to PATCH)
            System.out.println("Unexpected/parse error " + e );

        }

    }

}
