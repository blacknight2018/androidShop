package com.example.fuck2.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import static com.example.fuck2.utils.Utils.EmptyString;

public class HttpRequest {
    private static class DeleteMethodWithBody extends EntityEnclosingMethod {

        @Override
        public String getName() {
            return "DELETE";
        }

        public DeleteMethodWithBody() {
        }

        public DeleteMethodWithBody(String uri) {
            super(uri);
        }
    }

    public static String doDelete(String url, String params, String Cookie) {
        String responseString = EmptyString;
        try {
            if (params != null) {
                DeleteMethodWithBody postMethod = new DeleteMethodWithBody(url + "?" + params);
                RequestEntity se = new StringRequestEntity(params);
                postMethod.setRequestEntity(se);
                postMethod.setRequestHeader("cookie", Cookie);
                HttpClient httpclient = new HttpClient();
                httpclient.executeMethod(postMethod);
                InputStream inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuffer = new StringBuilder();
                String str = EmptyString;
                while ((str = br.readLine()) != null) {
                    stringBuffer.append(str);
                }
                responseString = stringBuffer.toString();
            } else {
                DeleteMethod d = new DeleteMethod(url);
                HttpClient httpclient = new HttpClient();
                httpclient.executeMethod(d);
                responseString = d.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseString;
    }

    static String sendGet(String url, String param) {
        String result = EmptyString;
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            Map<String, List<String>> map = connection.getHeaderFields();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = EmptyString;
        try {
            URL realUrl = new URL(url);

            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.193 Safari/537.36");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(param);

            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    static String sendGetWithCookie(String url, String param, String cookie) {
        String result = EmptyString;
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);

            URLConnection connection = realUrl.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("cookie", cookie);
            connection.connect();

            Map<String, List<String>> map = connection.getHeaderFields();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    static String sendPostWithCookie(String url, String param, String cookie) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = EmptyString;
        try {
            URL realUrl = new URL(url);

            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.193 Safari/537.36");
            conn.setRequestProperty("cookie", cookie);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(param);

            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    static Response sendPostWithMultiRes(String url, String param) {
        Response response = new Response();
        PrintWriter out = null;
        BufferedReader in = null;
        String result = EmptyString;
        try {
            URL realUrl = new URL(url);

            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.193 Safari/537.36");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());

            out.print(param);

            out.flush();
            response.setHeaders(conn.getHeaderFields());
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        response.setBody(result);
        return response;
    }

    public static class Response {
        private Map<String, List<String>> Headers;
        private String Body;

        public Map<String, List<String>> getHeaders() {
            return Headers;
        }

        public void setHeaders(Map<String, List<String>> headers) {
            Headers = headers;
        }

        public String getBody() {
            return Body;
        }

        public void setBody(String body) {
            Body = body;
        }
    }
}