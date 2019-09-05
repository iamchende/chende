package cn.suse.utils;

import java.io.IOException;
import java.net.SocketException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {

    private static final CloseableHttpClient httpClient;
    
    private static final String CHARACTER_SET    = "UTF-8";

    static {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(10000).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(5);
        KeepAliveStrategy keepAliveStrategy = new KeepAliveStrategy();
        RequestRetryHandler retryHandler = new RequestRetryHandler();
        httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                .setKeepAliveStrategy(keepAliveStrategy).setRetryHandler(retryHandler).build();
    }

    public static String doGet(String uri) {
        log.info("doGet,uri:{}", uri);
        ResponseHandler<String> responseHandler = (HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity responseEntity = response.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity, CHARACTER_SET) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        HttpGet httpGet = new HttpGet(uri);
        String responseContent = null;
        try {
            responseContent = httpClient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            log.error("doGet failed.uri:{}", uri, e);
            throw new RuntimeException(e);
        }
        return responseContent;
    }

    public static String doJsonPost(String uri, String jsonContent) {
        log.info("doJsonPost,uri:{},jsonContent:{}", uri, jsonContent);
        ResponseHandler<String> responseHandler = (HttpResponse response) -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity responseEntity = response.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity, CHARACTER_SET) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        StringEntity requestEntity = new StringEntity(jsonContent, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(requestEntity);
        String responseContent = null;
        try {
            responseContent = httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            log.error("doJsonPost failed.uri:{},jsonContent{},response:{}", uri, jsonContent, responseContent, e);
            throw new RuntimeException(e);
        }
        return responseContent;
    }
    public static void main(String[] args) {
		String url = "http://www.baidu.com";
		System.out.println(doGet(url));
		
		url = "http://cnnuo.com/api.php/Weather/query";
		System.out.println(doJsonPost(url, "{\"cityid\":\"24\"}"));
	}
}


class KeepAliveStrategy implements ConnectionKeepAliveStrategy {

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        final HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            final HeaderElement he = it.nextElement();
            final String param = he.getName();
            final String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch (final NumberFormatException ignore) {
                }
            }
        }
        return 5 * 1000;
    }

}

@Slf4j
class RequestRetryHandler extends DefaultHttpRequestRetryHandler {

    @Override
    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount > getRetryCount()) {
            return false;
        }
        if (exception instanceof SocketException && exception.getMessage().contains("Connection reset")) {
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            log.warn("connection reset occured. try one more time. uri:{}", request.getRequestLine().getUri());
            return true;
        } else {
            return super.retryRequest(exception, executionCount, context);
        }
    }

}
