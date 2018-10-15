package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Rest {

    private static final int TYPE_POST_PARAMS = 0;
    private static final int TYPE_POST_BODY = 1;

    public static Response post(String uri, boolean auth, HttpEntity entity, int type) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        System.out.println("AUTH BEFORE IF: " + auth);
        if (auth) {
            System.out.println("- INSIDE IF STATEMENT: " + auth);
            if (!Settings.ogsAuth.tokenExists() || Settings.ogsAuth.isTokenExpired())
                throw new RuntimeException("Cannot add authentication header when token is invalid!");
            String authMethod = Settings.ogsAuth.getTokenType();
            String authToken = Settings.ogsAuth.getAuthToken();
            System.out.println("- ADDING HEADERS: " + auth);
            post.addHeader("Authorization", authMethod + " " + authToken);
        }

        if (type == TYPE_POST_BODY) {
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");
        }

        HttpResponse response = httpClient.execute(post);
        HttpEntity e = response.getEntity();
        Header encodingHeader = e.getContentEncoding();
        Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
        String jsonRaw = EntityUtils.toString(e, encoding);
        JSONObject json = null;
        try {
            json = new JSONObject(jsonRaw);
        } catch(JSONException ee) {
            json = null;
        }

        Response r = new Response();
        r.httpResponse = response;
        r.rawString = jsonRaw;
        r.json = json;
        httpClient.close();
        System.out.println("HAS AUTH? " + auth);
        System.out.println(httpPostToString(post));
        return r;
    }

    public static Response postParams(String uri, boolean auth, String ... kwargs) throws IOException {
        List<NameValuePair> params = new ArrayList<>();
        if (kwargs.length > 0) {
            for (int i = 0; i < kwargs.length; i += 2) {
                params.add(new BasicNameValuePair(kwargs[i], kwargs[i + 1]));
            }
        }

        return post(uri, auth, new UrlEncodedFormEntity(params), TYPE_POST_PARAMS);
    }

    public static Response postBody(String uri, boolean auth, String body) throws IOException {
        System.out.println("AUTH IN POST BODY: " + auth);
        StringEntity entity = new StringEntity(body);
        return post(uri, auth, entity, TYPE_POST_BODY);
    }


    public static Response get(String uri, boolean auth, String ... kwargs) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringBuilder params = new StringBuilder();

        if (kwargs.length > 0) {
            params.append("?");
            for (int i = 0; i < kwargs.length; i += 2) {
                params.append(kwargs[i]).append("=").append(kwargs[i + 1]);
                if (i + 2 < kwargs.length) {
                    params.append("&");
                }
            }
        }
        uri += params.toString();
        HttpGet get = new HttpGet(uri);
        if (auth) {
            if (!Settings.ogsAuth.tokenExists() || Settings.ogsAuth.isTokenExpired())
                throw new RuntimeException("Cannot add authentication header when token is invalid!");
            String authMethod = Settings.ogsAuth.getTokenType();
            String authToken = Settings.ogsAuth.getAuthToken();
            get.addHeader("Authorization", authMethod + " " + authToken);
        }

        HttpResponse response = httpClient.execute(get);
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

    public static String httpPostToString(HttpPost httppost) {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestLine:");
        sb.append(httppost.getRequestLine().toString());

        int i = 0;
        for(Header header : httppost.getAllHeaders()){
            if(i == 0){
                sb.append("\nHeader:");
            }
            i++;
            for(HeaderElement element : header.getElements()){
                for(NameValuePair nvp :element.getParameters()){
                    sb.append(nvp.getName());
                    sb.append("=");
                    sb.append(nvp.getValue());
                    sb.append(";");
                }
            }
        }
        HttpEntity entity = httppost.getEntity();

        String content = "";
        if(entity != null){
            try {

                content = IOUtils.toString(entity.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("\nContent:");
        sb.append(content);


        return sb.toString();
    }
}
