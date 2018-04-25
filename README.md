# Retrofire
A thin Java wrapper to firebase REST api using Retrofit and Okhttp

Supports GET, PUT, PATCH, POST and DELETE requests with Query parameters

Supports Dynamic Headers for GET requests (other requests coming soon)

# Why Retrofire

1 - You don't want to use the Firebase android/java api because perhaps you are operating in
an environment where you would rather execute synchronous requests, don't want to cache any data or
leave any socket connections open

2 - You found out that Firebase provides a REST api and you decided you want to use it
 https://firebase.google.com/docs/reference/rest/database/

 BUT!!!

3- You are allergic to boiler plate code and you are already using the awesome Retrofit and okhttp libraries and
you wished if there is a java wrapper to this Firebase REST api using Retrofit and okhttp under the hood

If you agree with 1 + 2 + 3 then Retrofire is for you!

# Setup
Beta release -
Maven and Gradle usage coming soon

# Usage
Retrieve or save data to Firebase with 3 easy steps

1- Create an instance of the retrofire/firebase service

        String FIREBASE_REF = "https://yourFirebaseReference.firebaseio.com/";
        FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF, true);

2- Execute an http request by simply passing the path and your data in any of the below formats:

                    a- Pass data as an object (could be a class you have defined or a data structure)
                    b- Pass data as a MAP
                    c- Pass data as a raw json

            // Example 1
            // PUT request passing path and a user defined object, in this case UserDetails class
            UserDetails user1 = new UserDetails("uid1",100,150,70, "I love traveling and discovering new cultures");

            fbSvc.put("userDetails/uid1",user1);

            // Example 2
            // PUT request passing raw json
            // let's add another user
            String rawjson = "{\"userUID\":\"uid2\",\"nb_followers\":\"4000\",\"nb_following\":\"1000\",\"nb_posts\":\"300\"}";

            fbSvc.put("userDetails/uid2",rawjson);

            // Example 3
            // PATCH request passing a MAP
            // Let's add bio information to the user we just added (uid2)
            LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
            datamap.put("bio", "Musician/Band");

            fbSvc.patch("userDetails/uid2",datamap);

            // Example 4
            // GET with query parameters
            // Let's retrieve users with nb_followers equal or greater than 500
            fbSvc.addQueryParam("orderBy", "\"nb_followers\"");
            fbSvc.addQueryParam("startAt","500");

            firebaseResponse = fbSvc.get("userDetails");

            System.out.println("Users with at least 500 followers: " + firebaseResponse.toString());
            // Sample output
            // Users with at least 500 followers: FirebaseResponse{success=true, code=200, message=OK, body='{"uid2":{"bio":"Musician/Band","nb_followers":"4000","nb_following":"1000","nb_posts":"300","userUID":"uid2"}}'}

            // Example 5
            // POST
            // User uid1 posts a photo, let's save the info
            datamap.clear();
            datamap.put("description","Enjoying this beautiful sunset, where are you traveling to next?");
            datamap.put("imageLink","https://myclouddatastorage/image231");

            fbSvc.post("userPosts/uid1",datamap);


3- Retrieve the Firebase response

            Retrofire service encapsulates the response returned from Firebase in FirebaseResponse
            from which you can retrieve the success status, code, message and data returned from firebase

            // Example
            // GET
            // Let's retrieve the bio of uid1
            UserDetails getUser;
            firebaseResponse = fbSvc.get("userDetails/uid1/");

            if (firebaseResponse.isSuccess()) {
                // Check if information found (firebase returns success status with null body if path not found)
                if (!firebaseResponse.getBody().equals("null")) {
                    Gson gson = new Gson();
                    getUser = gson.fromJson(firebaseResponse.getBody(), UserDetails.class);
                    System.out.println("uid1 Bio : " + getUser.getBio());
                    // Sample output: uid1 Bio : I love traveling and discovering new cultures

                } else {
                    System.out.println("GET path not found");
                }
            }

# Authenticated requests

You can add authentication to Retrofire requests by:

1- Pass the authentication parameters once to the Retrofire constructor and Retrofire will add
 it to each http request you make:

        a- If you are using Firebase ID Tokens
        FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF, "auth", "<FirebaseIDToken>", false);

        b- If you are using Google OAuth2 access token
        FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF, "access_token", "<GoogleOAuth2Token>", false);



2- Add the authentication parameters as a parameter to each Retrofire request

        a- If you are using Firebase ID Tokens
        fbSvc.addQueryParam("auth","<FirebaseIDToken>");

        b- If you are using Google OAuth2 access token
        fbSvc.addQueryParam("access_token","<GoogleOAuth2Token>");

For more information about Firebase REST authenticated requests and how to generate tokens check out
https://firebase.google.com/docs/database/rest/auth

# More
 Check [FirebaseSvc.java](/src/main/java/service/FirebaseSvc.java) for a detailed description of every
 method Retrofire provides

 Check [Examples.java](/src/test/java/Examples.java) for more detailed examples