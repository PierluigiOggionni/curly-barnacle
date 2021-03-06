package com.atlantic.demo;

import java.io.*;
import java.net.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/pdfdownload")
public class HelloDemo  extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /***** This Method Is Called By The Servlet Container To Process A 'GET' Request *****/
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        handleRequest(request, response);
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Client cc = new Client();
        String act = cc.retrieveAccessToken();
        String url_a = request.getParameter("url");
        try {

            String url = cc.urlConverter(url_a);
        log(url_a);
        URL  biesse_doc = new URL(url);
        HttpURLConnection  bc = (HttpURLConnection)biesse_doc.openConnection();

        bc.setRequestMethod("GET");
        bc.setRequestProperty("Authorization",  "Bearer " + act);
        InputStream inputStream = bc.getInputStream();
        BufferedInputStream reader = new BufferedInputStream(inputStream);
        String filename ;
        filename= cc.getFilename();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename="+filename);

        OutputStream out = response.getOutputStream();

        BufferedOutputStream writer = new BufferedOutputStream(out);

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, bytesRead);
        }

        writer.close();
        reader.close();

        } catch (MalformedURLException e) {
            System.out.println("The specified URL is malformed: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An I/O error occurs: " + e.getMessage());
        }


        //equest.get
        //  String eurl = URLEncoder.encode(url,"UTF-8");
        // String url= "https://biesseit.sharepoint.com/_api/web/GetFileByServerRelativeUrl%28%27/docportal/BIESSE_AMERICA/QUOTE/WOOD/2018/08/IWF%2018131-18_B/1/IWF18131-18_INTEGRITY%20CUSTOM%20CABINETS_ROVER%20B%20FT%201536.pdf%27%29/$value";
       // String url = "https%3A%2F%2Fbiesseit.sharepoint.com%2F_api%2Fweb%2FGetFileByServerRelativeUrl%28%26%2339%3B%2Fdocportal%2FBIESSE_AMER%0AICA%2FQUOTE%2FWOOD%2F2018%2F08%2FIWF%2018131-18_B%2F1%2FIWF18131-18_INTEGRITY%20CUSTOM%0ACABINETS_ROVER%20B%20FT%201536.pdf%26%2339%3B%29%2F%24value";


        // OkHttpClient client = new OkHttpClient();
        //Request req = new Request.Builder().url(url)
        //.get()
        //.addHeader("Authorization", "Bearer " + act)
        //.build();
        //try {
        //
        //Response res = client.newCall(req).execute();
        //if (!res.isSuccessful()) {
        //response.sendError(HttpServletResponse.SC_NOT_FOUND);
        //}
        //String filename = null;
        //filename= cc.getFilename();
        //response.setContentType("application/octet-stream");
        //response.addHeader("Content-Disposition", "attachment; filename="+filename);
        //
        //OutputStream out = response.getOutputStream();
        //byte[] buffer = new byte[4096];
        //
        //InputStream in = res.body().byteStream();
        //if (in == null) {
        //response.sendError(HttpServletResponse.SC_NOT_FOUND);
        //}
        //int read;
        //
        //while ((read = in.read(buffer)) != -1) {
        //out.write(buffer, 0, read);
        //}
        //log("done");
        //} catch(Exception e) {
        //response.sendError(HttpServletResponse.SC_NOT_FOUND);
        //}
        //finally {
        //
        //
        //}
        //
    }







}