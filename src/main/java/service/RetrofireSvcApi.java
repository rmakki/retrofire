package service;

import model.FirebaseResponse;
import model.NetworkRequestListener;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by raniamakki on 9/15/17.
 */
public interface RetrofireSvcApi {


    /**
     * PATCH data on the path relative to the baseURL (Update) - Synchronous
     *
     * Firebase will only update the fields passed
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

    public FirebaseResponse patch(String path, Object data) throws Exception;

    /**
     * PATCH data on the path relative to the baseURL (Update) - Synchronous
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

    public FirebaseResponse patch(String path, Map<String, Object> data) throws Exception;


    /**
     * PATCH RAW json data relative to the baseURL (Update) - Synchronous
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

    public FirebaseResponse patch(String path, String rawdata) throws Exception;

    /**
     * POST data relative to the baseURL (Insert) - Synchronous
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

    public FirebaseResponse post(String path, Object data) throws Exception;


    /**
     * POST data relative to the baseURL (Insert) - Synchronous
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

    public FirebaseResponse post(String path, Map<String, Object> data) throws Exception;

    /**
     * POST RAW json data relative to the baseURL (Insert) - Synchronous
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

    public FirebaseResponse post(String path, String rawdata) throws Exception;

    /**
     * PUT data on the path relative to the baseURL : create or delete - Synchronous
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

    public FirebaseResponse put(String path, Object data) throws Exception;


    /**
     * PUT data on the path relative to the baseURL : create or delete - Synchronous
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


    public FirebaseResponse put(String path, Map<String, Object> data) throws Exception;

    /**
     * PUT raw json data on the path relative to the baseURL : create or delete - Synchronous
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

    public FirebaseResponse put(String path, String rawdata) throws Exception;


    /**
     * DELETE data on the path relative to the baseURL - Synchronous
     *
     * @param path if empty/null, data will be deleted under the root of the baseURL
     *             Be careful with this as this can clear all the data under your root
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse delete(String path) throws Exception;

    /**
     * GET data from the path relative to the baseURL - Synchronous
     *
     * @param path if empty/null, all data under your root will be fetched
     * @return {@link FirebaseResponse}
     */

    public FirebaseResponse get(String path) throws Exception;


    /**
     *
     * GET data from the path relative to the baseURL - Asynchronous
     *
     * @param path if empty/null, all data under your root will be fetched
     * @param listener NetworkRequestListener
     *
     */

    public void getAsync(String path, final NetworkRequestListener listener) throws Exception;


    /**
     *  Add a query string parameter in the form param=<value> to an http Request
     *  Check out firebase documentation for possible REST query parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addQueryParam(Map<String,String> map);


    /**
     *  Add a query string parameter in the form param=<value> to an http Request
     *  Check out firebase documentation for possible REST query parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addQueryParam(String param, String value);


    /**
     *  Add multiple Header parameters to an http Request by passing a MAP
     *  Check out firebase documentation for possible REST header parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addHeaderParam(Map<String,String> map);


    /**
     *  Add one Header parameter to an http Request
     *  Check out firebase documentation for possible REST header parameters
     *  https://firebase.google.com/docs/reference/rest/database/
     *
     */

    public void addHeaderParam(String param, String value);

}