package service;

import model.FirebaseResponse;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitApi.FirebaseSvcApi;
import unsafe.UnsafeHttpsClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by raniamakki on 9/15/17.
 */
public class FirebaseSvc {

    private String FIREBASE_REF;
    private FirebaseSvcApi firebaseSvcApi;
    private FirebaseSvcApi firebaseSvcApiNoConverter;



    /**
     *
     * Constructor
     * @param FIREBASE_REF firebase baseURL
     *
     */

    public FirebaseSvc(String FIREBASE_REF) {

            this.FIREBASE_REF = FIREBASE_REF;

            // Initialise reusable okhttp and Retrofit instance with json converter
            this.firebaseSvcApi = firebaseSvcApi();

            // Initialise reusable okhttp and Retrofit instance without json converter
            this.firebaseSvcApiNoConverter = firebaseSvcApiNoConverter();
    }


    /**
     *
     * PATCH data on the path relative to the baseURL
     *
     * Firebase will only update the fields passed.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path  if empty/null, data will be updated under the root of the baseURL
     *              if not null, data will be updated relative to the baseURL
     *
     * @param data  -if null Retrofit will throw a "Body parameter value must not be null"
     *              error
     *              if you pass an empty object Firebase will return a success
     *              but the call will not change the state of your firebase instance
     *
     * @return {@link FirebaseResponse}
     *
     */

    public FirebaseResponse patch(String path, Object data ) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.patch(path, data);

        Response<ResponseBody> response = call.execute();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     *
     * PATCH data on the path relative to the baseURL
     *
     * Firebase will only update the fields passed.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path  if empty/null, data will be updated under the root of the baseURL
     *              if not null, data will be updated relative to the baseURL
     *
     * @param data  -if null Retrofit will throw a "Body parameter value must not be null"
     *              error
     *              if you pass an empty object Firebase will return a success
     *              but the call will not change the state of your firebase instance
     *
     * @return {@link FirebaseResponse}
     *
     */

    public FirebaseResponse patch(String path, Map <String, Object> data ) throws Exception {

        return this.patch(path,(Object)data);

    }


    /**
     *
     * PATCH RAW json data relative to the baseURL
     *
     * Firebase will only update the fields passed.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path  if empty/null, data will be updated under the root of the baseURL
     *              if not null, data will be updated relative to the baseURL
     *
     * @param rawdata
     *              if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *              exception.
     *              if you pass an empty String okhttp3 will throw a java.lang.NullPointerException
     *              with Info "No data supplied."
     *
     * @return {@link FirebaseResponse}

     *
     */

    public FirebaseResponse patch(String path, String rawdata) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),rawdata);

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.patch(path, body);

        Response<ResponseBody> response = call.execute();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     *
     * POST data relative to the baseURL
     *
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     * @param path  if empty/null, data will be posted under the root
     *              if not null, data will be inserted relative to the baseURL
     *
     * @param data  -if null Retrofit will throw a "Body parameter value must not be null"
     *              error
     *              if you pass an empty object beware:
     *              Firebase will not create an empty node with a generated key
     *              if that is your expectation. Firebase will return a success
     *              but the call will not change the state of your firebase instance
     *              (as if you did not make a call, no data posted)
     * @return {@link FirebaseResponse}
     *
     */

    public FirebaseResponse post(String path, Object data ) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.post(path, data);

        Response<ResponseBody> response = call.execute();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     * POST data relative to the baseURL
     *
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     * @param path  if empty/null, data will be posted under the root
     *              if not null, data will be inserted relative to the baseURL
     *
     * @param data  if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *              exception.
     *              if you pass an empty object beware:
     *              Firebase will not create an empty node with a generated key
     *              if that is your expectation. Firebase will return a success
     *              but the call will not change the state of your firebase instance
     *              (as if you did not make a call, no data posted)
     * @return {@link FirebaseResponse}
     *
     *
     * Note: Each element in the Map will be inserted under a new firebase generated key. The map
     * key will translate to a parent node and the fields in the object will translate to individual elements
     * under the parent node
     *
     */

    public FirebaseResponse post(String path, Map <String, Object> data) throws Exception {

        return this.post(path,(Object)data);

    }


    /**
     *
     * POST RAW json data relative to the baseURL
     *
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     * @param path  if empty/null, data will be posted under the root
     *              if not null, data will be inserted relative to the baseURL
     *
     * @param rawdata
     *              if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *              exception.
     *              if you pass an empty String okhttp3 will throw a java.lang.NullPointerException
     *              with Info "No data supplied."
     * @return {@link FirebaseResponse}

    *
    */

    public FirebaseResponse post(String path, String rawdata) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),rawdata);

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.post(path, body);

        Response<ResponseBody> response = call.execute();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }



    /**
     *  -----------
     *  PRIVATE API
     *  -----------
     */

    /**
     *
     * Process Firebase Response
     *
     */

    private FirebaseResponse processResponse (Response<ResponseBody> response) {

        FirebaseResponse firebaseResponse = null;

        String strRawResponse = fromInputDataToString(response);

        if (!response.isSuccessful()) {
            // Error from firebase with possible codes 403, 404, 417
            // for more details https://www.firebase.com/docs/rest/api/#section-error-conditions
            // Throwable parameter to encapsulate the Response in case a calling method needs to decode/print the exact cause
            System.out.println("error " + strRawResponse);
            firebaseResponse = new FirebaseResponse(response.code(), false, strRawResponse);

            //throw new UserDAOException(ErrorCodes.FIREBASE.name() + " " + fromInputDataToString(response));
        } else {
            // firebase returned 200 but no update. It may happen if user was empty
            if (fromInputDataToString(response).equals("null")) {
                // throw new UserDAOException(ErrorCodes.FAILED_ADD_FB_USER.name());
                System.out.println("can't update null " + strRawResponse);
                firebaseResponse = new FirebaseResponse(response.code(), false, strRawResponse);

            }
        }

        // Success
        firebaseResponse = new FirebaseResponse(response.code(), true, strRawResponse);

        return firebaseResponse;
    }


    private FirebaseSvcApi firebaseSvcApi () {

        OkHttpClient.Builder okHttpBuilder = UnsafeHttpsClient.getUnsafeOkHttpClient();

        FirebaseSvcApi api = new Retrofit.Builder()
                .baseUrl(this.FIREBASE_REF)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuilder.build())
                .build()
                .create(FirebaseSvcApi.class);

        return api;

    }

    private FirebaseSvcApi firebaseSvcApiNoConverter () {

        OkHttpClient.Builder okHttpBuilder = UnsafeHttpsClient.getUnsafeOkHttpClient();

        FirebaseSvcApi api = new Retrofit.Builder()
                .baseUrl(this.FIREBASE_REF)
                //.addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuilder.build())
                .build()
                .create(FirebaseSvcApi.class);

        return api;

    }

    /**
     * Method to convert Retrofit response to raw data
     *
     * @param response
     * @return String
     */

    private String fromInputDataToString(Response<ResponseBody> response) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
