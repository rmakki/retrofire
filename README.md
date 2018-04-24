# Retrofire
A thin Java wrapper to firebase REST api using Retrofit and Okhttp
Supports GET, PUT, PATCH, POST and DELETE requests with Query parameters
(Support for Headers and Streaming coming soon)

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

                    1- Pass data as an object (could be a class you have defined or a data structure)
                    2- Pass data as a MAP
                    3- Pass data as raw json

            // Example 1
            // PUT request passing path and a user defined object, in this case UserDetails class
            UserDetails user1 = new UserDetails("uid1",100,150,70, "I love travel and discovering new cultures");

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

            // Example 5
            // POST
            // User uid1 posts a photo, let's save the info
            datamap.clear();
            datamap.put("description","Enjoying this beautiful sunset, where are you traveling next?");
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

                } else {
                    System.out.println("GET path not found");
                }
            }


# More
 For more samples check out Examples.java. Also check FirebaseSvc.java for a detailed description of every
 method Retrofire provides
