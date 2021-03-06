package com.atlantic.demo;

import java.io.*;

//import okhttp3.*;

import java.net.*;
import java.util.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.*;
import sun.misc.BASE64Decoder;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;



public class Client {
    private String accessToken;
    private static HashMap<String, String> urlParameters = new HashMap<String,String>();
    private String filename;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }




    public Client() {
        urlParameters.put("grant_type","client_credentials");
        urlParameters.put("client_id","3f990da5-d8ac-46eb-a2b1-ab822ef504ce@29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0");
        urlParameters.put("client_secret","4xMpwigIfn4uqHbGcDoLE6Y3lBtN0/54vli5m4ULVBc=");
        urlParameters.put("resource","00000003-0000-0ff1-ce00-000000000000/biesseit.sharepoint.com@29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0");
        try {
            accessToken = retrieveAccessToken();
        } catch (IOException e) {

        }
    }

    public String getAccessToken() {
        return accessToken;
    }


    private String getDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }




    public String retrieveAccessToken() throws IOException {
       //  OkHttpClient client = new OkHttpClient();
        URL url = new URL("https://accounts.accesscontrol.windows.net/29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0/tokens/OAuth/2");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        Map<String,Object> params = new LinkedHashMap<String, Object>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", "3f990da5-d8ac-46eb-a2b1-ab822ef504ce@29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0");
        params.put("client_secret", "4xMpwigIfn4uqHbGcDoLE6Y3lBtN0/54vli5m4ULVBc=");
        params.put("resource", "00000003-0000-0ff1-ce00-000000000000/biesseit.sharepoint.com@29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0");
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
      //        params.put("query", "adewewewe");

        //String parameters= getDataString(urlParameters);
        /*
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body =
                RequestBody.create(mediaType, parameters);
                        Request request = new Request.Builder()
                                .url("https://accounts.accesscontrol.windows.net/29f69b92-a6f3-4bdc-a7df-7a5ed4afbfe0/tokens/OAuth/2")
                                .post(body)
                                .build();

        Response response = client.newCall(request).execute();
       // System.out.println(response.body().string());
        */
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
       // conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder data = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            data.append((char)c);
        }
        String jsonData = data.toString();
       // String jsonData = response.body().string();
        JSONObject jsonObject= new JSONObject(jsonData);
        this.accessToken = jsonObject.getString("access_token");





        return jsonObject.getString("access_token");
    }

    /*public String getSharepointDoc(String url) throws IOException  {
       // String token = accessToken;
        String eurl = URLEncoder.encode(url,"UTF-8");

                OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .get()
                .addHeader("Authorization", "Bearer "+retrieveAccessToken())
                .build();
        try  {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + request +" response:"+ response);
            }


            return response.body().toString();

        }


    }
    */
    public String decode(String s) {
        return StringUtils.newStringUtf8(Base64.decodeBase64(s));
    }
    public String encode(String s) {
        return Base64.encodeBase64String(StringUtils.getBytesUtf8(s));
    }

    public String urlConverter(String url) throws UnsupportedEncodingException, MalformedURLException {
        //String tmpString = "https://biesseit.sharepoint.com/docportal/SitePages/downloader.aspx?file=";

        String  base64String = url.replace("https://biesseit.sharepoint.com/docportal/SitePages/downloader.aspx?file=","");
        String  base64Decode= this.decode(base64String);
        URL  url_a =new URL(base64Decode);
        this.filename = FilenameUtils.getName(url_a.getPath()).replaceAll("[^a-zA-Z0-9.-]", "_");
        String removeHttp= base64Decode.replace("https://biesseit.sharepoint.com","");
        String finalString = "https://biesseit.sharepoint.com/_api/web/GetFileByServerRelativeUrl('"+ removeHttp+"')/$value";

        return(finalString);


    }





}
