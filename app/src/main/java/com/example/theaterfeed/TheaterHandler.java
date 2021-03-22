package com.example.theaterfeed;

import android.os.StrictMode;
import android.widget.ArrayAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class TheaterHandler {
    ArrayList<Theater> theatres = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();
    ArrayList<String> films = new ArrayList<>();
    TheaterHandler() {
        String url = "https://www.finnkino.fi/xml/TheatreAreas/";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(url);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Name");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    locations.add(n.getTextContent());
                }
                System.out.println(locations.get(i));
            }
            nList = doc.getDocumentElement().getElementsByTagName("ID");
            for (int i = 0; i < nList.getLength(); i++) {
                Node n = nList.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    theatres.add(new Theater(Integer.parseInt(n.getTextContent()), locations.get(i)));
                }
            }
            for (Theater t : theatres) {
                System.out.println(t.getId() + t.getLocation());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> getFilmData(int i) {
        films.clear();
        int id = theatres.get(i).getId();
        System.out.println(id);
        try {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String url = "https://www.finnkino.fi/xml/Schedule/?area=%d&dt=%s";
            DocumentBuilder builder = null;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(String.format(url, id, sdf.format(date)));
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            System.out.println(doc.getDocumentElement().getLastChild().getNodeName());
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Title");
            for (int it = 0; it < nList.getLength(); it++) {
                Node n = nList.item(it);
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    films.add(n.getTextContent());
                }
                System.out.println(films.get(it));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return films;
    }

    ArrayList<String> getTheaters() {
        return locations;
    }
}
