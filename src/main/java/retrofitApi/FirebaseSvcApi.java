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
	Call<ResponseBody> patch(@Path("path") String path, @Body Object data, @QueryMap Map<String,String> queryMap);

	// For raw json
	@PATCH("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> patch(@Path("path") String path, @Body RequestBody body, @QueryMap Map<String,String> queryMap);

	@POST("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body Object data, @QueryMap Map<String,String> queryMap);

	// For raw json
	@POST("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body RequestBody body, @QueryMap Map<String,String> queryMap);

	@PUT("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> put(@Path("path") String path, @Body Object data, @QueryMap Map<String,String> queryMap);

	// For raw json
	@PUT("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> put(@Path("path") String path, @Body RequestBody body, @QueryMap Map<String,String> queryMap);

	@DELETE("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> delete(@Path("path") String path, @QueryMap Map<String,String> queryMap);

	@GET("/" + "{path}" + FB_REST_SVC_PATH)
	Call<ResponseBody> get(@Path("path") String path, @QueryMap Map<String,String> queryMap, @HeaderMap Map<String,String> headerMap);

}