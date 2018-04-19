package service;

import model.FirebaseResponse;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitApi.FirebaseSvcApi;
import model.UserDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raniamakki on 9/15/17.
 */
public class FirebaseSvc {

    private String FIREBASE_REF;
    private FirebaseSvcApi firebaseSvcApi;
    private FirebaseSvcApi firebaseSvcApiNoConverter;
    private OkHttpClient.Builder okHttpBuilder;
    private Map<String, String> queryParam;
    /**
     * Constructor
     *
     * @param FIREBASE_REF firebase baseURL
     * @param httpFullLogging pass true for full http log. ONLY for testing purposes, make sure
     *                        you pass false in production code
     */

    public FirebaseSvc(String FIREBASE_REF, boolean httpFullLogging) {

        this.FIREBASE_REF = FIREBASE_REF;

        this.okHttpBuilder = new OkHttpClient.Builder();
        this.queryParam = new HashMap();

        if (httpFullLogging) {
            this.addHttpFullLogging();
        }

        // Initialize reusable okhttp and Retrofit instance with json converter
        this.firebaseSvcApi = firebaseSvcApi();

        // Initialize reusable okhttp and Retrofit instance without json converter
        this.firebaseSvcApiNoConverter = firebaseSvcApiNoConverter();
    }


    /**
     * Constructor
     *
     * @param FIREBASE_REF      firebase baseURL
     * @param secureTokenParam  To send authenticated requests to firebase REST API using one of the
     *                          following methods:
     *
     *                          1- pass "auth" if you are using firebase ID tokens to authenticate
     *                          For more details:
     *                          https://firebase.google.com/docs/database/rest/auth#authenticate_with_an_id_token
     *
     *                          OR
     *
     *                          2- pass "access_token" if you are using Google OAuth2 access token
     *                          For more details:
     *                          https://firebase.google.com/docs/database/rest/auth#authenticate_with_an_access_token
     *
     * @param secureTokenValue  pass the value of the secureToken (firebase ID token or Google OAuth2 token)
     *
     * @param httpFullLogging pass true for full http log. ONLY for testing purposes, make sure
     *                        you pass false in production code
     *
     */

    public FirebaseSvc(String FIREBASE_REF, String secureTokenParam, String secureTokenValue, boolean httpFullLogging) {


        this.FIREBASE_REF = FIREBASE_REF;

        this.okHttpBuilder = new OkHttpClient.Builder();
        this.queryParam = new HashMap();

        // adds auth=<secureTokenValue> or access_token=<secureTokenValue> as query string parameters to all http requests
        this.okHttpBuilder = this.addQuery(okHttpBuilder, secureTokenParam, secureTokenValue);

        if (httpFullLogging) {
            this.addHttpFullLogging();
        }

        // Initialize reusable okhttp and Retrofit instance with json converter
        this.firebaseSvcApi = firebaseSvcApi();

        // Initialize reusable okhttp and Retrofit instance without json converter
        this.firebaseSvcApiNoConverter = firebaseSvcApiNoConverter();

    }

    /**
     * PATCH data on the path relative to the baseURL
     *
     * Firebase will only update the fields passed.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path if empty/null, data will be updated under the root of the baseURL
     *             if not null, data will be updated relative to the baseURL
     * @param data -if null Retrofit will throw a "Body parameter value must not be null"
     *             error
     *             if you pass an empty object Firebase will return a success
     *             but the call will not change the state of your firebase instance
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse patch(String path, Object data) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.patch(path, data, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     * PATCH data on the path relative to the baseURL - Update
     *
     * Named children in the data being written with PATCH are overwritten,
     * but omitted children are not deleted.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path if empty/null, data will be updated under the root of the baseURL
     *             if not null, data will be updated relative to the baseURL
     * @param data -if null Retrofit will throw a "Body parameter value must not be null"
     *             error
     *             if you pass an empty object Firebase will return a success
     *             but the call will not change the state of your firebase instance
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse patch(String path, Map<String, Object> data) throws Exception {

        return this.patch(path, (Object) data);

    }


    /**
     * PATCH RAW json data relative to the baseURL
     * <p>
     * Firebase will only update the fields passed.
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path    if empty/null, data will be updated under the root of the baseURL
     *                if not null, data will be updated relative to the baseURL
     * @param rawdata if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *                exception.
     *                if you pass an empty String okhttp3 will throw a java.lang.NullPointerException
     *                with Info "No data supplied."
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse patch(String path, String rawdata) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), rawdata);

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.patch(path, body, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     * POST data relative to the baseURL
     * <p>
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     *
     * @param path if empty/null, data will be posted under the root
     *             if not null, data will be inserted relative to the baseURL
     * @param data -if null Retrofit will throw a "Body parameter value must not be null"
     *             error
     *             if you pass an empty object beware:
     *             Firebase will not create an empty node with a generated key
     *             if that is your expectation. Firebase will return a success
     *             but the call will not change the state of your firebase instance
     *             (as if you did not make a call, no data posted)
     * @return {@link FirebaseResponse} (if the request is successful the .body of FirebaseResponse
     * will contain the child name of the new data specified in the POST request. See Examples.java for usage
     * )
     */

    public FirebaseResponse post(String path, Object data) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.post(path, data, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     * POST data relative to the baseURL
     * <p>
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     *
     * @param path if empty/null, data will be posted under the root
     *             if not null, data will be inserted relative to the baseURL
     * @param data if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *             exception.
     *             if you pass an empty object beware:
     *             Firebase will not create an empty node with a generated key
     *             if that is your expectation. Firebase will return a success
     *             but the call will not change the state of your firebase instance
     *             (as if you did not make a call, no data posted)
     * @return {@link FirebaseResponse}
     * <p>
     * <p>
     * Note: Each element in the Map will be inserted under a new firebase generated key. The map
     * key will translate to a parent node and the fields in the object will translate to individual elements
     * under the parent node
     */

    public FirebaseResponse post(String path, Map<String, Object> data) throws Exception {

        return this.post(path, (Object) data);

    }


    /**
     * POST RAW json data relative to the baseURL
     * <p>
     * Firebase will insert data under the baseURL but associated with a new Firebase
     * generated key
     *
     * @param path    if empty/null, data will be posted under the root
     *                if not null, data will be inserted relative to the baseURL
     * @param rawdata if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *                exception.
     *                if you pass an empty String okhttp3 will throw a java.lang.NullPointerException
     *                with Info "No data supplied."
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse post(String path, String rawdata) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), rawdata);

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.post(path, body, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     * PUT data on the path relative to the baseURL : create or delete
     * <p>
     * If there is existing data at the path, the data you pass will overwrite it
     * If you pass empty data, any data existing at the path will be deleted
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path if empty/null, data will be overwritten/created under the root of the baseURL
     *             Be careful with this as this can clear all the data under your root and
     *             replace it with the object you are passing
     *             if not null, data will be overwritten/created relative to the baseURL
     * @param data if null object Firebase will return a success
     *             but the call will not change the state of your firebase instance
     *             if empty object, data of the fields sent will be cleared relative to the baseURL
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse put(String path, Object data) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.put(path, data, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     * PUT data on the path relative to the baseURL : create or delete
     * <p>
     * If there is existing data at the path, the data you pass will overwrite it
     * If you pass empty data, any data existing at the path will be deleted
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path if empty/null, data will be overwritten/created under the root of the baseURL
     *             Be careful with this as this can clear all the data under your root and
     *             replace it with the object you are passing
     *             if not null, data will be overwritten/created relative to the baseURL
     * @param data if null object Firebase will return a success
     *             but the call will not change the state of your firebase instance
     *             if empty object, data of the fields sent will be cleared relative to the baseURL
     * @return {@link FirebaseResponse}
     */


    public FirebaseResponse put(String path, Map<String, Object> data) throws Exception {

        return this.put(path, (Object) data);

    }

    /**
     * PUT raw json data on the path relative to the baseURL : create or delete
     * <p>
     * If there is existing data at the path, the data you pass will overwrite it
     * If you pass empty data, any data existing at the path will be deleted
     * If the fields passed do not exist, they will be added to firebase
     *
     * @param path    if empty/null, data will be overwritten/created under the root of the baseURL
     *                Be careful with this as this can clear all the data under your root and
     *                replace it with the object you are passing
     *                if not null, data will be overwritten/created relative to the baseURL
     * @param rawdata if null Retrofit will throw a IllegalArgumentException: Body parameter value must not be null
     *                exception.
     *                if you pass an empty String okhttp3 will throw a java.lang.NullPointerException
     *                with Info "No data supplied."
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse put(String path, String rawdata) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), rawdata);

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.put(path, body, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     * DELETE data on the path relative to the baseURL
     *
     * @param path if empty/null, data will be deleted under the root of the baseURL
     *             Be careful with this as this can clear all the data under your root
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse delete(String path) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        Call<ResponseBody> call = this.firebaseSvcApi.delete(path, this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     * GET data from the path relative to the baseURL
     *
     * @param path if empty/null, all data under your root will be fetched
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse get(String path) throws Exception {

        if (path == null) {
            path = "";
        }

        FirebaseResponse firebaseResponse;

        //Call<ResponseBody> call = this.firebaseSvcApi.get(path,"\"nb_followers\"","500");
        //Call<ResponseBody> call = this.firebaseSvcApi.get(path,null,null);

         Call<ResponseBody> call = this.firebaseSvcApi.get(path,this.queryParam);

        Response<ResponseBody> response = call.execute();

        this.queryParam.clear();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }


    /**
     * GET using a POJO
     * See Examples.java and FirebaseSvcApi.java for usage
     *
     */

    public UserDetails getUserDetails(String path) throws Exception {

        if (path == null) {
            path = "";
        }

        Call<UserDetails> call = this.firebaseSvcApi.getUserDetails(path,this.queryParam);

        UserDetails response = call.execute().body();

        this.queryParam.clear();

        return response;


    }

    /**
     *  Add a query string parameter in the form param=<value> to an http Request
     *  Check out firebase documentation for possible REST query parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addQueryParam(Map<String,String> map) {

        if (this.queryParam == null) {
            this.queryParam = new HashMap();
        }
        if (!this.queryParam.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParam.entrySet()) {
                this.queryParam.put(entry.getKey(),entry.getValue());
            }

        } else {
            this.queryParam = map;
        }

    }

    /**
     *  Add a query string parameter in the form param=<value> to an http Request
     *  Check out firebase documentation for possible REST query parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addQueryParam(String param, String value) {

        if (this.queryParam == null) {
            this.queryParam = new HashMap();
        }

        this.queryParam.put(param, value);

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

    private FirebaseResponse processResponse(Response<ResponseBody> response) {

        FirebaseResponse firebaseResponse;

        if (!response.isSuccessful()) {
            // Error from firebase, example codes 401, 403, 404, 417
            // for more details https://www.firebase.com/docs/rest/api/#section-error-conditions
            // Sample body: {  "error" : "Could not parse auth token."}

            firebaseResponse = new FirebaseResponse(response.code(), response.message(), false, fromInputDataToString(response.errorBody()));
            System.out.println(firebaseResponse.toString());

        } else {
            // firebase returned 200 success but null body: no change happened in firebase
            // It may happen for example if you send null data to be updated

            /* if (fromInputDataToString(response.body()).equals("null")) {

                System.out.println("can't update null " + response.raw().toString());
                firebaseResponse = new FirebaseResponse(response.code(), response.message(), false, "null");
                System.out.println("null case " + firebaseResponse.toString());


            } else { */
                        // success
                        // Sample success body: {"name":"-L6xn9vZmRMWX9Qwj3KK"} // returns key of posted data

                firebaseResponse = new FirebaseResponse(response.code(), response.message(), true, fromInputDataToString(response.body()));
                System.out.println(firebaseResponse.toString());

            //}
        }

        return firebaseResponse;
    }

    /**
     *
     *  Add interceptor to add a query string parameter in the form param=<value> to all http requests:
     *  Used to add authentication parameters only
     *
    **/

    private OkHttpClient.Builder addQuery(OkHttpClient.Builder httpClient, String param, String value) {

        httpClient.addInterceptor(new

        Interceptor() {
            @Override
            public okhttp3.Response intercept (Chain chain)throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                System.out.println("original url: " + originalHttpUrl.toString());

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(param, value)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        }

        );

        return httpClient;
    }

    /**
     *
     *  Create Retrofit/okhttp api instance with okhttp and json converter
     *
     */

    private FirebaseSvcApi firebaseSvcApi () {

        FirebaseSvcApi api = new Retrofit.Builder()
                .baseUrl(this.FIREBASE_REF)
                .addConverterFactory(GsonConverterFactory.create())
                .client(this.okHttpBuilder.build())
                .build()
                .create(FirebaseSvcApi.class);

        return api;
    }

    /**
     *
     *  Create Retrofit/okhttp api instance with no converter, used when no need
     *  to convert the json passed to an object
     *
     */

    private FirebaseSvcApi firebaseSvcApiNoConverter () {

        FirebaseSvcApi api = new Retrofit.Builder()
                .baseUrl(this.FIREBASE_REF)
                .client(this.okHttpBuilder.build())
                .build()
                .create(FirebaseSvcApi.class);

        return api;

    }

    /**
     * Method to convert Retrofit response to raw data
     *
     * @param response
     * @return String
     *
     */


    private String fromInputDataToString(ResponseBody response) {

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(response.byteStream()));
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

    /**
     *
     *  Add interceptor to turn on full logging
     *
     */

    private void addHttpFullLogging () {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.okHttpBuilder.addInterceptor(loggingInterceptor);

    }

}
