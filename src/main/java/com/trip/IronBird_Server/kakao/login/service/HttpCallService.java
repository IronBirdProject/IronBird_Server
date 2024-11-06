package com.trip.IronBird_Server.kakao.login.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class HttpCallService {

    public String executeHttpRequest(String method, String reqURL, String header, String param) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Authorization", header);

            if (param != null && ("POST".equals(method) || "PUT".equals(method))) {
                conn.setDoOutput(true);
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                    bw.write(param);
                    bw.flush();
                }
            }

            int responseCode = conn.getResponseCode();
            InputStream stream;
            if (responseCode >= 200 && responseCode < 300) {
                stream = conn.getInputStream();
            } else {
                stream = conn.getErrorStream();
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return result.toString();
    }

    public String executeHttpRequestWithToken(String method, String reqURL, String access_Token) {
        return executeHttpRequest(method, reqURL, "Bearer " + access_Token, null);
    }

    public String executeHttpRequestWithToken(String method, String reqURL, String access_Token, String param) {
        String header = "Bearer " + access_Token;
        return executeHttpRequest(method, reqURL, header, param);
    }
}
