package kendzi.josm.plugin.tomb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

public class OverpassService {
    static String OVERPASS_URL = "http://www.overpass-api.de/api/interpreter";

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
        String q = "<osm-script>"
                + " <query into=\"_\" type=\"relation\">"
                + " <has-kv k=\"type\" v=\"person\" />"
                + " <has-kv k=\"name\" regv=\"[Cc]zernik\" />"
                + " </query>"
                + " <print from=\"_\" limit=\"\" mode=\"meta\" order=\"id\"/>"
                + " </osm-script>";

        String xml = findQuery(q);


        System.out.println(xml);

    }
    static String findQuery(String query) {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(OVERPASS_URL);

        StringBuffer sb = new StringBuffer();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("data",
                    query));


            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
