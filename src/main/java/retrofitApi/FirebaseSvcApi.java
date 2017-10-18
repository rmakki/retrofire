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

	// Path to update User node in firebase. Only Sent fields will be updated
	//String FB_PATCH_SVC_PATH = "/users";
	String FB_PATCH_SVC_PATH = ".json";

	// Path to update User node in firebase
	//String USER_POST_SVC_PATH_DB = "/users.json";

	// Required extension by firebase
	String FB_POST_SVC_PATH = ".json";

	//@POST(USER_POST_SVC_PATH_DB)
	//Call<ResponseBody> post(@Body HashMap<String, Object> data);

	//@PATCH("/" + "{path}" + FB_PATCH_SVC_PATH)
	//Call<ResponseBody> patch(@Path("path") String path, @Body Object data);

	@PATCH("/" + "{path}" + FB_PATCH_SVC_PATH)
	Call<ResponseBody> patch(@Path("path") String path, @Body Object data);

	@PATCH("/" + "{path}" + FB_PATCH_SVC_PATH)
	Call<ResponseBody> patch(@Path("path") String path, @Body RequestBody body);

	@POST("/" + "{path}" + FB_POST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body Object data);

	// For posting raw json
	@POST("/" + "{path}" + FB_POST_SVC_PATH)
	Call<ResponseBody> post(@Path("path") String path, @Body RequestBody body);

}



