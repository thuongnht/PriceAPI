package core;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.soap.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core.BulkRequest;
import core.Price;
import core.Message;


public class SoapServer extends HttpServlet {

    public SoapServer() {}

    public void init() {
        System.out.println("init");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        System.out.println("Get");

        res.setContentType("text/xml;charset=UTF-8");
        PrintWriter writer = res.getWriter();

        String id = parseId(req);
        List<Price> prices = getPrices(id);
        Message m = new Message();
        try {
            SOAPMessage soapMessageResponse = m.makeSoapMessage(prices);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapMessageResponse.writeTo(out);
            String strMsg = new String(out.toByteArray());

            writer.write(strMsg.toString());
        } catch (SOAPException se) {
            se.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        System.out.println("Post");
        doGet(req, res);
    }

    public void destroy() {}

    private String parseId(HttpServletRequest req) throws IOException {

        String id = "";
        try
        {

            MessageFactory messageFactory = MessageFactory.newInstance();
            InputStream inStream = req.getInputStream();
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(), inStream);

            SOAPBody body = soapMessage.getSOAPBody();
            id = body.getElementsByTagName("id").item(0).getTextContent();

        }
        catch (SOAPException se)
        {
            se.printStackTrace();
        }

        return id;

    }

    private List<Price> getPrices(String id) {
        System.out.println("Before-PriceAPI");
        BulkRequest br = new BulkRequest();
        JSONObject res = br.processRequest("0" + id);
//        System.out.println(res.toString());
        System.out.println("After-PriceAPI");

        return parseJsonToPrice(res);
    }

    private List<Price> parseJsonToPrice(JSONObject json) {

        List<Price> prices = new ArrayList<Price>();
        JSONArray products = json.getJSONArray("products");

//        System.out.println(products.length());
        for(int i=0; i<products.length(); i++)
        {
            JSONObject product = products.getJSONObject(i);
            JSONArray offers = product.getJSONArray("offers");
//            System.out.println(offers.length());

            for(int j=0; j<offers.length(); j++)
            {
                JSONObject offer = offers.getJSONObject(j);
                Price price = new Price();

                price.setCurrency(offer.getString("currency"));
                price.setPrice(Double.parseDouble(offer.getString("price")));
                price.setPrice_with_shipping(Double.parseDouble(offer.getString("price_with_shipping")));
                price.setShop_name(offer.getString("shop_name"));
                price.setShop_url(offer.getString("shop_url"));
                price.setUrl(offer.getString("url"));

//                System.out.println(price.toString());
                prices.add(price);
            }

        }

        return prices;

    }

}
