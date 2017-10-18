/**
 * Created by raniamakki on 9/14/17.
 */

import model.FirebaseResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import service.FirebaseSvc;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertTrue;


public class Testsuite1 {
    String FIREBASE_REF = "https://smartplayTest.firebaseio.com/";
    FirebaseSvc fbSvc = new FirebaseSvc(FIREBASE_REF);


    @BeforeClass
    public static void TestBefore() {
        System.out.println("testbefore");
    }
/*
    @Test
    public void TestPOST(){

        String rawjson = "{\"userName\":\"user1\",\"password\":\"postpassword\"}";
        UserF userF = new UserF("user1","password");
        LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
        FirebaseResponse firebaseResponse = null;
        dataMap.put("subnode",userF);
        // fbSvc.post(dataMap);

        try {
            // this will add userF data under the Root of your firebase instance
            // format: <firebasekey>/subnode/user1 & <firebasekey>/subnode/password
            //firebaseResponse = fbSvc.post("", dataMap);

            // This will add a node under users/<firebasekey>/subnode/user1 & users/<firebasekey>/subnode/password
            //firebaseResponse = fbSvc.post("users", dataMap);

            //This will add userF data under the root of your firebase instance
            // format: <firebasekey>/user1 & <firebasekey>/password
            //firebaseResponse = fbSvc.post(null, userF);

            //This will add userF data under the users/ node your firebase instance
            // format: users/<firebasekey>/user1 & users/<firebasekey>/password
            //firebaseResponse = fbSvc.post("users", userF);

            //This will add rawjson
            firebaseResponse = fbSvc.post("", rawjson);


        }

        catch (IOException e) { // Network error like connecting to fb with http instead of https
            // or wrong instance name (causes timeout)
            System.out.println("Network error " + e );

        }
        catch (Exception e) { // Unexpected/parse error example you pass a null to the Retrofit Path parameter
            System.out.println("Unexpected/parse error " + e );

        }

        assertTrue(firebaseResponse.isSuccess());
        System.out.println("End of TestPOST");
    }

*/
    @Test
    public void TestPATCH(){

        String rawjson = "{\"userName\":\"userpatch\",\"password\":\"rawjsonpatchpassword\"}";

        UserF userF = new UserF("userpatch","mappatchpassword");
        LinkedHashMap<String, Object> dataMap = new LinkedHashMap<String, Object>();
        FirebaseResponse firebaseResponse = null;
        //dataMap.put("subnode",userF);
        dataMap.put("-KvcV06UQENpcyPdzCKm",userF);

        try {

            // This will update userF data
            // format: users/<firebasekey>/userpatch & users/<firebasekey>/password
            // firebaseResponse = fbSvc.patch("users", userF);

            // This will update userF data using a Map
            // This will update a node under users/<firebasekey>/userpatch & users/<firebasekey>/patchpassword
            // firebaseResponse = fbSvc.patch("users", dataMap);

            // This will update userF data by sending rawjson
             firebaseResponse = fbSvc.patch("users/-KvcV06UQENpcyPdzCKm", rawjson);

        }

        catch (IOException e) { // Network error like connecting to fb with http instead of https
            // or wrong instance name (causes timeout)
            System.out.println("Network error " + e );

        }
        catch (Exception e) { // Unexpected/parse error example you pass a null to the Retrofit Path parameter
            System.out.println("Unexpected/parse error " + e );

        }

        assertTrue(firebaseResponse.isSuccess());
        System.out.println("End of TestPATCH");
    }

    @AfterClass
    public static void TestAfter() {
        System.out.println("testafter");
    }

}
