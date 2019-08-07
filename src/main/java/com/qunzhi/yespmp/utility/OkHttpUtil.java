package com.qunzhi.yespmp.utility;

import com.google.common.io.ByteStreams;
import lombok.experimental.UtilityClass;
import okhttp3.*;
import okhttp3.Request.Builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Derrick on 2017/12/6.
 * <br>
 * 内部服务调用不推荐此方式
 */
@UtilityClass
public class OkHttpUtil {


  public static final OkHttpClient CLIENT = new OkHttpClient.Builder()
      .connectTimeout(100L, TimeUnit.SECONDS).retryOnConnectionFailure(true)
      .build();

  /**
   * common http get request
   *
   * @param url request url
   * @param params request params
   * @return response
   */
  public static Response get(String url, Map<String, String> params) {
    try {
      HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
      if (params != null) {
        params.forEach((k, v) -> builder.addQueryParameter(k, v));
      }
      Request request = new Request.Builder().addHeader("Connection", "close").url(builder.build()).build();
      return CLIENT.newCall(request).execute();
    } catch (Exception e) {
      throw new RuntimeException("Get Request Fail", e);
    }
  }

  /**
   * get string with get request, ignore the request is success or failure
   * @param url     request url
   * @param params  request params
   * @return        string of response body
   */
  public static String getString(String url, Map<String, String> params) {
    Response response = get(url, params);
    try {
      return response.body().string();
    } catch (Exception e) {
      throw new RuntimeException("Response to String Fail", e);
    } finally {
      response.close();
    }
  }

  /**
   * get string with post request, ignore the request is success or failure
   * @param url     request url
   * @param params  request params
   * @return        string of response body
   */
  public static String postString(String url, Map<String, String> params) {
    Response response = post(url, params,null);
    try {
      return response.body().string();
    } catch (Exception e) {
      throw new RuntimeException("Response to String Fail", e);
    } finally {
      response.close();
    }
  }

  /**
   * download file, no exception would be throw out
   *
   * @param url request url
   * @param params request params
   * @param path file save path, parent would not be checked here!
   * @return file length if success, -1 if fail
   */
  public static long download(@Nonnull String url, @Nullable Map<String, String> params,
                              @Nonnull String path) {
    Response response = get(url, params);
    if (response.isSuccessful()) {
      try {
        return ByteStreams.copy(response.body().byteStream(), new FileOutputStream(path));
      } catch (IOException e) {
        e.printStackTrace();
        return -1;
      } finally {
        response.close();
      }
    }
    return -1;
  }

  public static Response post(String url, Map<String, String> params,Map<String,String> headers) {
    try {
      FormBody.Builder builder = new FormBody.Builder();
      if (params != null && params.size() > 0) {
        params.forEach((k,v) -> builder.add(k,v));
      }
      Builder post = new Builder().url(url).post(builder.build());
      if (headers != null && headers.size() > 0)
        headers.forEach((k,v) -> post.addHeader(k,v));
      Request request = post.build();
      return CLIENT.newCall(request).execute();
    } catch (Exception e) {
      throw new RuntimeException("Post Request Fail", e);
    }
  }

  /**
   * 请求并返回String
   *
   * @author Yin
   * @param request request body
   * @return        string of response body
   * @throws IOException
   */
  public static String executeString(Request request) throws IOException {
    Response rep = CLIENT.newCall(request).execute();
    String body = rep.body().string();
    if (!rep.isSuccessful())
      throw new RuntimeException(body);
    return body;
  }
}
