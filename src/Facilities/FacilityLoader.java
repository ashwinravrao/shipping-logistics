/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Facilities;

import Exceptions.PathNotFoundException;
import Exceptions.UnknownFacilityTypeException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import Exceptions.BrokenLoaderFieldsException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FacilityLoader {

    private ArrayList<FacilityInterface> facilities = new ArrayList<>();

    public FacilityLoader(String filename) throws BrokenLoaderFieldsException, UnknownFacilityTypeException, PathNotFoundException {
        loadXML(filename);
    }

    private void loadXML(String filename) throws BrokenLoaderFieldsException, UnknownFacilityTypeException, PathNotFoundException {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(filename);
            if (!xml.exists()) {
                throw new PathNotFoundException(filename);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList facilityEntries = doc.getDocumentElement().getChildNodes();

            for (int i = 0; i < facilityEntries.getLength(); i++) {
                if (facilityEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }

                String entryName = facilityEntries.item(i).getNodeName();
                if (!entryName.equals("Facility")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }

                // get node attribute
                NamedNodeMap aMap = facilityEntries.item(i).getAttributes();
                String id = aMap.getNamedItem("id").getNodeValue();

                // gets all named nodes
                Element element = (Element) facilityEntries.item(i);
                String warehouseName = element.getElementsByTagName("FName").item(0).getTextContent();
                String items = element.getElementsByTagName("Items").item(0).getTextContent();
                int pRate = Integer.parseInt(items);
                String cost = element.getElementsByTagName("Cost").item(0).getTextContent();
                double pCost = Double.parseDouble(cost);

                // gets all nodes named Link and Item
                HashMap<String, String> links = new HashMap<>();
                HashMap<String, String> inventoryMap = new HashMap<>();

                NodeList linkList = element.getElementsByTagName("Link");
                NodeList itemList = element.getElementsByTagName("Item");

                for (int j = 0; j < linkList.getLength(); j++) {
                    for (int k = 0; k < itemList.getLength(); k++) {
                        if ((linkList.item(j).getNodeType() == Node.TEXT_NODE) || (itemList.item(k).getNodeType() == Node.TEXT_NODE)) {
                            continue;
                        }

                        entryName = linkList.item(j).getNodeName();
                        String entryName2 = itemList.item(k).getNodeName();
                        if (!entryName.equals("Link")) {
                            System.err.println("Unexpected node found: " + entryName);
                            return;
                        }
                        if (!entryName2.equals("Item")) {
                            System.err.println("Unexpected node found: " + entryName2);
                            return;
                        }

                        // gets named nodes within Link tag
                        element = (Element) linkList.item(j);
                        String linkName = element.getElementsByTagName("Name").item(0).getTextContent();
                        String linkDistance = element.getElementsByTagName("Distance").item(0).getTextContent();

                        links.put(warehouseName + " : " + linkName, linkDistance);

                        element = (Element) itemList.item(k);
                        String itemId = element.getElementsByTagName("Id").item(0).getTextContent();
                        String itemQ = element.getElementsByTagName("Quantity").item(0).getTextContent();

                        inventoryMap.put(itemId, itemQ);
                    }
                }

                facilities.add(FacilityFactory.build(id, warehouseName, pRate, pCost, links, inventoryMap));
            }
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.err.println(e.getStackTrace());
        }
    }


    public ArrayList<FacilityInterface> getFacilities() {
        return facilities;
    }

}
