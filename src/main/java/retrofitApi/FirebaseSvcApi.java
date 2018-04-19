package retrofitApi;

import model.UserDetails;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;


/**
 *   
 * @author raniamakki
 *
 */
public interface FirebaseSvcApi {

	// Required extension by firebase
	String FB_REST_SVC_PATH = ".json";

	@PATCH("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> patch(@Path("path") String path, @Body Object data);

	// For raw json
	@PATCH("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> patch(@Path("path") String path, @Body RequestBody body);

	@POST("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body Object data);

	// For raw json
	@POST("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body RequestBody body);

	@PUT("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> put(@Path("path") String path, @Body Object data);

	// For raw json
	@PUT("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> put(@Path("path") String path, @Body RequestBody body);

	@DELETE("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> delete(@Path("path") String path, @QueryMap Map<String,String> queryMap);

	@GET("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> get(@Path("path") String path, @QueryMap Map<String,String> queryMap);

	// test
	@GET("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> get(@Path("path") String path,@Query("orderBy") String orderBy, @Query("startAt") String equalTo);
	/**
	 * I have attempted to make this library generic enough to serve most use cases, but you can easily
	 * customize it to your need if you wish to specify the POJO expected to be returned from firebase.
	 * In this case Retrofit will under the hood do the json to object translation for you
	 * In the sample method below, you can replace UserDetails by a class that you use in your project,
	 * or you can just use the generic format in the previous get method above and opt to do the json to
	 * object translation using Gson by yourself
	 */
	@GET("/" + "{path}" + FB_REST_SVC_PATH)
	Call<UserDetails> getUserDetails(@Path("path") String path, @QueryMap Map<String,String> queryMap);


}