package com.artsourcing.common;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@NoArgsConstructor
public class ApiRequest {

    private String url;
    private Map<String, String> requestHeaders;
    private String requestBody;
    List<NameValuePair> urlEncodedFormParams;
    private String methodType;

    public ApiRequest(String url, String methodType){
        this.url = url;
        this.requestHeaders = new HashMap<String,String>();
        this.requestBody = "";
        this.methodType = methodType;
        this.urlEncodedFormParams = new ArrayList<NameValuePair>();

    }

    public void encodeUrl() {
        try {
            this.url = URLEncoder.encode(this.url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패",e);
        }
    }



    public void putReqestHeader(String headerName, String headerValue){
        this.requestHeaders.put(headerName, headerValue);
    }

    public void putRequestBody(String body){
        this.requestBody = body;
    }

    public void putRequestBody(NameValuePair param) {
        this.urlEncodedFormParams.add(param);
    }

    public String getResponseText() {
        if(!requestHeaders.containsKey("Content-Type")){
            this.requestHeaders.put("Content-Type", "charset=UTF-8");
        }

        return get();
    }

    public Map<String,Object> getResponseJson() {
        String responseText = getResponseText();
        return Parser.jsonText2Map(responseText);
    }

    private String get(){
        HttpURLConnection con = connect(this.url);
        try {
            //method
            con.setRequestMethod(this.methodType);
            //header
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            //body string
            if(!requestBody.equals("")){
                con.setDoOutput(true);
                try(OutputStream os = con.getOutputStream()){
                    byte[] requestData = requestBody.getBytes(StandardCharsets.UTF_8);
                    os.write(requestData);
                    os.close();
                }catch (Exception e){
                    throw new RuntimeException("request body 설정 실패",e);
                }
            }

            //body urlEncodedForm
            if(!this.urlEncodedFormParams.isEmpty()){

                System.out.println("------Before Send");
                this.urlEncodedFormParams.forEach(nameValuePair -> {
                    System.out.println(nameValuePair.getName()+" = "+nameValuePair.getValue());
                });

                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(this.urlEncodedFormParams,StandardCharsets.UTF_8);
                try(OutputStream os = con.getOutputStream()){
                    entity.writeTo(os);
                    os.close();
                }catch (Exception e){
                    throw new RuntimeException("request body 설정 실패",e);
                }
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        if(body == null){
            return "";
        }
        InputStreamReader streamReader = null;
        try {
            streamReader = new InputStreamReader(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("응답을 인코딩하는 데 실패했습니다.", e);
        }


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("응답을 읽는데 실패했습니다.", e);
        }
    }
}
