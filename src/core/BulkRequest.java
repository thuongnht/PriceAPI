package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 645 on 8/24/2016.
 */
public class BulkRequest {
    private String token; // use set token to get token from context OR you can set here yr token
    private Integer refreshInterval = 1000;

    public BulkRequest() {
        setToken(); // omit this if you set yr token directly
    }

    public JSONObject processRequest(String value) {
//        System.out.println(value);
        JSONObject response = null;
        BulkRequest bulk = new BulkRequest();
        JSONObject bulkStatus = bulk.request(value,
                "google-shopping", "de", "gtin");
        String jobId = "";
        try {
            jobId = (String) bulkStatus.get("job_id");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return null;
        }

        Boolean done = false;
        while (!done) {
            try {
                Thread.sleep(refreshInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bulkStatus = bulk.getStatus(jobId);

            Boolean isComplete = false;
            try {
                String status = (String) bulkStatus.get("status");
                isComplete = status.equals("finished");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isComplete) {
                response = bulk.getResults(jobId, "json");
                done = true;
            }
        }

        return response;
//        return {id: value};
    }

    public JSONObject request(String values, String source, String country,
                              String key) {
        String st = "token=%s&source=%s&country=%s&key=%s&values=%s";
        String query = String.format(st, token, source, country, key, values);
//        System.out.println(query);

        String response = postRequest("/jobs", query);

//        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getStatus(String jobId) {
        String response = getRequest("/jobs/" + jobId, "token=" + token);

//        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject getResults(String jobId, String format) {
        String response = getRequest("/products/bulk/" + jobId + '.' + format,
                "token=" + token);

//        System.out.println(response);
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getRequest(String path, String query) {
        String response = "";

        try {
            URI uri = new URI("https", "api.priceapi.com", path, query, null);
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response += inputLine;
            }
            br.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String postRequest(String path, String query) {
        String response = "";

        try {
            URI uri = new URI("https", "api.priceapi.com", path, null);
            URL url = uri.toURL();
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(
                    conn.getOutputStream());
            writer.write(query);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response += inputLine;
            }
            reader.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void setToken() {
        try {
            InitialContext initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:/comp/env");
            String ctx = (String) environmentContext.lookup("priceAPI");
            token = ctx;
        }
        catch (NamingException ex) {
            System.err.println(ex);
        }
    }
}
