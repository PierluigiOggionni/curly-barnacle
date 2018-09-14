package com.atlantic.demo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;

import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String url = cc.urlConverter(url_a);


        //equest.get
        //  String eurl = URLEncoder.encode(url,"UTF-8");
        // String url= "https://biesseit.sharepoint.com/_api/web/GetFileByServerRelativeUrl%28%27/docportal/BIESSE_AMERICA/QUOTE/WOOD/2018/08/IWF%2018131-18_B/1/IWF18131-18_INTEGRITY%20CUSTOM%20CABINETS_ROVER%20B%20FT%201536.pdf%27%29/$value";
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url)
                .get()
                .addHeader("Authorization", "Bearer " + act)
                .build();
        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful()) {
                throw new IOException("Unexpected code " + res + " response:" + res);
            }
            String filename = null;
            String contentDisposition = res.header("Content-Disposition");
            if (contentDisposition != null && !"".equals(contentDisposition)) {
                // Get filename from the Content-Disposition header.
                Pattern pattern = Pattern.compile("filename=['\"]?([^'\"\\s]+)['\"]?");
                Matcher matcher = pattern.matcher(contentDisposition);
                if (matcher.find()) {
                    filename = (matcher.group(1));
                }
            }
            //  InputStream in = res.body().byteStream();
            //BufferedSource source = res.body().source();

            // Sink out = Okio.sink("pp.pdf");

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + filename);

            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            InputStream in = res.body().byteStream();
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            log("done");
        }
    }







}