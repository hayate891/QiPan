package io.nibby.qipan.ogs;

import org.apache.commons.io.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Rest {

    public static Response post(String uri, String ... kwargs) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> params = new ArrayList<>();
        if (kwargs.length > 0) {
            for (int i = 0; i < kwargs.length; i += 2) {
                params.add(new BasicNameValuePair(kwargs[i], kwargs[i + 1]));
            }
        }
        post.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        Header encodingHeader = entity.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
        String jsonRaw = EntityUtils.toString(entity, encoding);
        JSONObject json = new JSONObject(jsonRaw);

        Response r = new Response();
        r.httpResponse = response;
        r.rawString = jsonRaw;
        r.json = json;
        httpClient.close();

        return r;
    }

    public static class Response {
        private String rawString;
        private JSONObject json;
        private HttpResponse httpResponse;

        public String getRawString() {
            return rawString;
        }

        public JSONObject getJson() {
            return json;
        }

        public HttpResponse getHttpResponse() {
            return httpResponse;
        }
    }
}
