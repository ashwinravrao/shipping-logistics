/*
 * Developed by Ashwin Rao in June 2017.
 * Last Modified 9/7/18 12:43 PM.
 * Copyright (c) 2018. All rights reserved.
 */

package Catalog;

import Exceptions.BrokenLoaderFieldsException;
import Exceptions.PathNotFoundException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class CatalogLoader {

    private Catalog catalog;



    public CatalogLoader(String filename) throws BrokenLoaderFieldsException, PathNotFoundException {
        loadXML(filename);
    }

    public void loadXML(String filename) throws BrokenLoaderFieldsException, PathNotFoundException {

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xml = new File(filename);
            if (!xml.exists()) {
                throw new PathNotFoundException(filename);
            }

            Document doc = db.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList itemEntries = doc.getDocumentElement().getChildNodes();
            HashMap<String, String> catalogMap = new HashMap<>();
            for (int i = 0; i < itemEntries.getLength(); i++) {
                if (itemEntries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }

                String entryName = itemEntries.item(i).getNodeName();
                if(!entryName.equals("Item")) {
                    System.err.println("Unexpected node found: " + entryName);
                    return;
                }

                Element element = (Element) itemEntries.item(i);
                String id = element.getElementsByTagName("Id").item(0).getTextContent();
                String price = element.getElementsByTagName("Price").item(0).getTextContent();

                catalogMap.put(id, price);

            }

            catalog = new Catalog(catalogMap);
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.err.println(e.getStackTrace());
        }
    }

    public Catalog getCatalog() {
        return catalog;
    }
}
