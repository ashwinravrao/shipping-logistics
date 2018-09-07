/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Orders;

import Exceptions.BrokenLoaderFieldsException;
import Exceptions.PathNotFoundException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import sun.plugin.dom.core.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class OrderLoader {

    private ArrayList<Order> ordersList = new ArrayList<>();

    public OrderLoader(String filename) throws BrokenLoaderFieldsException {
        loadXML(filename);
    }

    private void loadXML(String filename) throws BrokenLoaderFieldsException {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(filename);
            if (!xml.exists()) {
                System.err.println("**** XML File '" + filename + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList orderEntries = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < orderEntries.getLength(); i++) {
                if (orderEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }

                String entryName = orderEntries.item(i).getNodeName();
                if (!entryName.equals("Order")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }

                // get node attribute
                NamedNodeMap aMap = orderEntries.item(i).getAttributes();
                String orderId = aMap.getNamedItem("Id").getNodeValue();

                // get named nodes
                Element elem = (Element) orderEntries.item(i);
                int orderTime = Integer.parseInt(elem.getElementsByTagName("OrderTime").item(0).getTextContent());
                String destination = elem.getElementsByTagName("Destination").item(0).getTextContent();

                HashMap<String, String> orderItemMap = new HashMap<>();
                NodeList itemList = elem.getElementsByTagName("Item");
                for (int j = 0; j < itemList.getLength(); j++) {
                    if (itemList.item(j).getNodeType() == Node.TEXT_NODE) {
                        continue;
                    }

                    entryName = itemList.item(j).getNodeName();
                    if (!entryName.equals("Item")) {
                        System.err.println("Unexpected node found: " + entryName);
                        return;
                    }

                    elem = (Element) itemList.item(j);
                    String itemId = elem.getElementsByTagName("Id").item(0).getTextContent();
                    String itemQuantity = elem.getElementsByTagName("Quantity").item(0).getTextContent();

                    orderItemMap.put(itemId, itemQuantity);
                }

                ordersList.add(new Order(orderId, orderTime, destination, orderItemMap));
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.err.println(e.getStackTrace());
        }
    }

    public ArrayList<Order> getOrdersList() {
        return ordersList;
    }
}