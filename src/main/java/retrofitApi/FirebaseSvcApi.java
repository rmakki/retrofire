package retrofitApi;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


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
	Call<ResponseBody> delete(@Path("path") String path);


}



