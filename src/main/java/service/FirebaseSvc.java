package service;

import model.FirebaseResponse;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofitApi.FirebaseSvcApi;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Created by raniamakki on 9/15/17.
 */
public class FirebaseSvc {

    private String FIREBASE_REF;
    private String oAuth2Token=null;
    private String firebaseIDToken=null;
    private FirebaseSvcApi firebaseSvcApi;
    private FirebaseSvcApi firebaseSvcApiNoConverter;
    private OkHttpClient.Builder okHttpBuilder;

    /**
     * Constructor
     *
     * @param FIREBASE_REF firebase baseURL
     */

    public FirebaseSvc(String FIREBASE_REF, boolean httpFullLogging) {

        this.FIREBASE_REF = FIREBASE_REF;

        this.setOkHttpBuilder();

        if (httpFullLogging) {
            this.addHttpFullLogging();
        }

        // Initialise reusable okhttp and Retrofit instance with json converter
        this.firebaseSvcApi = firebaseSvcApi();

        // Initialise reusable okhttp and Retrofit instance without json converter
        this.firebaseSvcApiNoConverter = firebaseSvcApiNoConverter();
    }


    /**
     * Constructor
     *
     * @param FIREBASE_REF    firebase baseURL
     * @param firebaseIDToken To send authenticated requests to firebase REST API using
     *                        firebase ID tokens
     *                        For more details:
     *                        https://firebase.google.com/docs/database/rest/auth#authenticate_with_an_id_token
     */

    public FirebaseSvc(String FIREBASE_REF, String firebaseIDToken, boolean httpFullLogging) {

        this.firebaseIDToken = firebaseIDToken;

        this.FIREBASE_REF = FIREBASE_REF;

        this.setOkHttpBuilder();

        if (httpFullLogging) {
            this.addHttpFullLogging();
        }

        // Initialise reusable okhttp and Retrofit instance with json converter
        this.firebaseSvcApi = firebaseSvcApi();

        // Initialise reusable okhttp and Retrofit instance without json converter
        this.firebaseSvcApiNoConverter = firebaseSvcApiNoConverter();


    }

    /**
     * PATCH data on the path relative to the baseURL
     * <p>
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

        Call<ResponseBody> call = this.firebaseSvcApi.patch(path, data);

        Response<ResponseBody> response = call.execute();

        firebaseResponse = processResponse(response);

        return firebaseResponse;

    }

    /**
     * PATCH data on the path relative to the baseURL
     * <p>
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

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.patch(path, body);

        Response<ResponseBody> response = call.execute();

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
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse post(String path, Object data) throws Exception {

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

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.post(path, body);

        Response<ResponseBody> response = call.execute();

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

        Call<ResponseBody> call = this.firebaseSvcApi.put(path, data);

        Response<ResponseBody> response = call.execute();

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

        Call<ResponseBody> call = this.firebaseSvcApiNoConverter.put(path, body);

        Response<ResponseBody> response = call.execute();

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

        Call<ResponseBody> call = this.firebaseSvcApi.delete(path);

        Response<ResponseBody> response = call.execute();

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

        Call<ResponseBody> call = this.firebaseSvcApi.get(path);

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
     * Process Firebase Response
     */

    private FirebaseResponse processResponse(Response<ResponseBody> response) {

        FirebaseResponse firebaseResponse;

        String strRawResponse = fromInputDataToString(response);

        if (!response.isSuccessful()) {
            // Error from firebase with possible codes 403, 404, 417
            // for more details https://www.firebase.com/docs/rest/api/#section-error-conditions
            System.out.println("error " + strRawResponse);
            firebaseResponse = new FirebaseResponse(response.code(), false, strRawResponse);

        } else {
            // firebase returned 200 success but null body: no change happened in firebase
            // It may happen for example if you send null data to be updated
            if (fromInputDataToString(response).equals("null")) {

                System.out.println("can't update null " + strRawResponse);
                firebaseResponse = new FirebaseResponse(response.code(), false, strRawResponse);

            } else { // success
                firebaseResponse = new FirebaseResponse(response.code(), true, strRawResponse);
            }
        }

        return firebaseResponse;
    }

    /**
     *
     * Add intereceptor to include the firebase ID Token as a query String parameter
     * auth=<ID_TOKEN> in all requests
     *
    **/

    private OkHttpClient.Builder addQuery(OkHttpClient.Builder httpClient) {

        httpClient.addInterceptor(new

        Interceptor() {
            @Override
            public okhttp3.Response intercept (Chain chain)throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("auth", firebaseIDToken)
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

    private void addHttpFullLogging () {

        // for logging/testing purposes, do not use in production environment
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        this.okHttpBuilder.addInterceptor(loggingInterceptor);

    }

    /**
     * --------------------
     * Private Constructors
     * --------------------
     */

    private void setOkHttpBuilder () {

        this.okHttpBuilder = new OkHttpClient.Builder();

        if (this.firebaseIDToken != null) {

            okHttpBuilder = this.addQuery(okHttpBuilder);

        }

    }

    // Think about any possible need to make a public getter for OkHttpBuilder
    //private OkHttpClient.Builder getOkHttpBuilder () {
    //    return this.okHttpBuilder;
    //}

    private FirebaseSvcApi firebaseSvcApi () {

        FirebaseSvcApi api = new Retrofit.Builder()
                .baseUrl(this.FIREBASE_REF)
                .addConverterFactory(GsonConverterFactory.create())
                .client(this.okHttpBuilder.build())
                .build()
                .create(FirebaseSvcApi.class);

        return api;
    }

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
