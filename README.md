# Retrofire
A thin Java wrapper to firebase REST api using Retrofit and Okhttp under the hood

Supports GET, PUT, PATCH, POST and DELETE requests with Query parameters

Supports Dynamic Headers for GET requests (other requests coming soon)

Supports Asynchronous GET requests with Query parameters and dynamic headers

# Why Retrofire

1 - You have evaluated the Firebase java admin sdk and decided that your project may not be the right candidate for it:
perhaps you are operating in a server environment where you can't or you would rather not leave any persistent socket connections open.


2 - You found out that Firebase provides a REST api and you decided you want to use it
 https://firebase.google.com/docs/reference/rest/database/

 BUT!!!

3- You are allergic to boiler plate code and you wished if there is a java wrapper to the
above Firebase REST api that makes your Http calls easier to write, read, and debug

If you agree with 1 + 2 + 3 then Retrofire is for you!

# Setup
Beta release -
Maven and Gradle usage coming soon

# Usage
Retrieve or save data to Firebase with 3 easy steps

1- Create an instance of the Retrofire/Firebase service

        String FIREBASE_REF = "https://yourFirebaseReference.firebaseio.com/";
        RetrofireSvc rfSvc = new RetrofireSvc(FIREBASE_REF, false);

2- Execute an http request by simply passing the path and your data in any of the below formats:

                    a- Pass data as an object (could be a class you have defined or a data structure)
                    b- Pass data as a MAP
                    c- Pass data as a raw json

            // Example 1
            // PUT request passing path and a user defined object, in this case UserDetails class
            UserDetails user1 = new UserDetails("uid1",100,150,70, "I love traveling and discovering new cultures");

            rfSvc.put("userDetails/uid1",user1);

            // Example 2
            // PUT request passing raw json
            // let's add another user
            String rawjson = "{\"userUID\":\"uid2\",\"nbFollowers\":\"4000\",\"nbFollowing\":\"1000\",\"nbPosts\":\"300\"}";

            rfSvc.put("userDetails/uid2",rawjson);

            // Example 3
            // PATCH request passing a MAP
            // Let's add bio information to the user we just added (uid2)
            LinkedHashMap<String, Object> datamap = new LinkedHashMap<String, Object>();
            datamap.put("bio", "Musician/Band");

            rfSvc.patch("userDetails/uid2",datamap);

            // Example 4
            // GET with query parameters
            // Let's retrieve users with nb_followers equal or greater than 500
            rfSvc.addQueryParam("orderBy", "\"nbFollowers\"");
            rfSvc.addQueryParam("startAt","500");

            firebaseResponse = rfSvc.get("userDetails");

            System.out.println("Users with at least 500 followers: " + firebaseResponse.toString());
            // Sample output
            // Users with at least 500 followers: FirebaseResponse{success=true, code=200, message=OK, body='{"uid2":{"bio":"Musician/Band","nbFollowers":"4000","nbFollowing":"1000","nbPosts":"300","userUID":"uid2"}}'}

            // Example 5
            // POST
            // User uid1 posts a photo, let's save the info
            datamap.clear();
            datamap.put("description","Enjoying this beautiful sunset, where are you traveling to next?");
            datamap.put("imageLink","https://myclouddatastorage/image231");

            rfSvc.post("userPosts/uid1",datamap);


3- Retrieve the Firebase response

            Retrofire service encapsulates the response returned from Firebase in FirebaseResponse
            from which you can retrieve the success status, code, message and data returned from firebase

            // Example
            // GET
            // Let's retrieve the bio of uid1
            UserDetails getUser;
            firebaseResponse = rfSvc.get("userDetails/uid1/");

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

# Asynchronous Requests

Retrofire provides asynchronous calls to Firebase. These calls are more efficient than the blocking synchronous version since
they use okhttp/retrofit asynchronous calls under the hood

            // Asynchronous GET method call

            NetworkRequestListener networkRequestListener = new NetworkRequestListener<FirebaseResponse>() {
                @Override
                public void onExecuted(FirebaseResponse firebaseResponse) {

                    // Request Executed
                    if (firebaseResponse.isSuccess()) {
                        System.out.println("Users with at least 500 followers ASYNC: " + firebaseResponse.toString());
                    } else
                        System.out.print("Firebase returned an error ASYNC: " + firebaseResponse.getMessage());

                }

                @Override
                public void onFailure(Throwable t) {
                    // Network request failed
                    System.out.print("Retrofit ASYNC network call failed " + t.getMessage());
                }

            };

            rfSvc.getAsync("userDetails", networkRequestListener);


# Authenticated requests

You can add authentication to Retrofire requests by either:

1- Passing the authentication parameters once to the Retrofire constructor and Retrofire will add
 it to each http request you make:

        a- If you are using Firebase ID Tokens
        RetrofireSvc rfSvc = new RetrofireSvc(FIREBASE_REF, "auth", "<FirebaseIDToken>", false);

        b- If you are using Google OAuth2 access token
        RetrofireSvc rfSvc = new RetrofireSvc(FIREBASE_REF, "access_token", "<GoogleOAuth2Token>", false);

OR

2- Adding the authentication parameters as a parameter to each Retrofire request you make:

        a- If you are using Firebase ID Tokens
        rfSvc.addQueryParam("auth","<FirebaseIDToken>");

        b- If you are using Google OAuth2 access Tokens
        rfSvc.addQueryParam("access_token","<GoogleOAuth2Token>");

For more information about Firebase REST authenticated requests and how to generate tokens check out
https://firebase.google.com/docs/database/rest/auth

# More
 Check [RetrofireSvcApi.java](/src/main/java/service/RetrofireSvcApi.java) for a detailed description of every
 method Retrofire provides

 Check [Examples.java](/src/test/java/Examples.java) for more detailed examples