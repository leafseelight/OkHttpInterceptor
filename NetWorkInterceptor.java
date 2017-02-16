
import android.util.Log;
import java.io.IOException;
import java.net.URLDecoder;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Okhttp请求拦截
 * 打印出请求的链接到logcat
 * 链接已经拼接好GET请求可以直接链接
 */
public class NetWorkInterceptor implements Interceptor {

    public static String TAG = "NetWorkInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (BuildConfig.DEBUG) {
            String methodName = request.method();
            if (methodName.equalsIgnoreCase("GET")) {
                Log.i(TAG, "-url--" + methodName + "--" + request.url());
            } else if (methodName.equalsIgnoreCase("POST")) {
                RequestBody mRequestBody = request.body();
                if (mRequestBody != null) {
                    String msg = "-url--" + methodName + "--" + request.url() + "?";
                    String content;
                    if (msg.contains("uploadFile")) {
                        content = "--上传文件内容--";
                    } else {
                        content = getParam(mRequestBody);
                    }
                    Log.i(TAG, msg + content);
                }
            }
        }
        Response response = chain.proceed(request);
        return response;
    }

    /**
     * 读取参数
     *
     * @param requestBody
     * @return
     */
    private String getParam(RequestBody requestBody) {
        Buffer buffer = new Buffer();
        String logparm;
        try {
            requestBody.writeTo(buffer);
            logparm = buffer.readUtf8();
            logparm = URLDecoder.decode(logparm, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return logparm;
    }
}
