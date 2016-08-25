package core;

import java.io.FileOutputStream;
import java.util.*;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import core.Price;


public class Message {

    public Message() {}

    public SOAPMessage makeSoapMessage(List<Price> prices) {
        SOAPMessage soapMsg = null;
        try{
            MessageFactory factory = MessageFactory.newInstance();
            soapMsg = factory.createMessage();
            SOAPPart part = soapMsg.getSOAPPart();

            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            header.addTextNode("Price Details");

            Iterator<Price> i = prices.iterator();
            while (i.hasNext()) {
                Price price = (Price) i.next();
                SOAPBodyElement element = body.addBodyElement(envelope.createName("price_detail"));
                element.addChildElement("shop_name").addTextNode(price.getShop_name());
                element.addChildElement("shop_url").addTextNode(price.getShop_url());
                element.addChildElement("url").addTextNode(price.getUrl());
                element.addChildElement("price").addTextNode(price.getPrice() + " " + price.getCurrency());
                element.addChildElement("price_with_shipping").addTextNode(price.getPrice_with_shipping() + " " + price.getCurrency());
            }

//            soapMsg.writeTo(System.out);
            System.out.println("SOAP msg created");

        }catch(Exception e){
            e.printStackTrace();
        }

        return soapMsg;
    }
}
