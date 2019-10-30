/*
MainActivity.java
Andrew Lambropoulos
Sean Fox
Homework4
 */

package com.example.andyl.homework4;

import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewsParser {
    public static class NewsSAXParser extends DefaultHandler {
        ArrayList<NewsObject> newsObjects;
        NewsObject newsObject;
        StringBuilder innerXml;
        boolean insideItem = false;
        boolean gotURL = false;

        static public ArrayList<NewsObject>parseNewsObject(InputStream inputStream, Boolean isBussiness) throws IOException, SAXException {
            NewsSAXParser parser = new NewsSAXParser();
            if (isBussiness == false) {
                Xml.parse(inputStream, Xml.Encoding.UTF_8, parser);
            }
            else {
                Xml.parse(inputStream, Xml.Encoding.ISO_8859_1, parser);
            }

            return parser.newsObjects;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            this.newsObjects = new ArrayList<>();
            innerXml = new StringBuilder();
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);

            if (localName.equals("item")) {
                newsObject = new NewsObject();
                insideItem = true;
            }
            if (localName.equals("content") && gotURL == false) {
                newsObject.urlToImage = attributes.getValue("url");
                gotURL = true;
            }
            if (localName.equals("thumbnail")) {
                newsObject.urlToImage = attributes.getValue("url");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);

            if (localName.equals("title") && insideItem == true) {
                newsObject.title = innerXml.toString();
            }
            else if(localName.equals("description") && insideItem == true) {
                int endDescription = 0;
                endDescription = innerXml.indexOf("<");
                newsObject.description = innerXml.toString().substring(0, endDescription);
            }
            else if(localName.equals("link") && insideItem == true) {
            //else if(localName.equals("guid") && insideItem == true){
                newsObject.url = innerXml.toString();
            }
            else if(localName.equals("pubDate") && insideItem == true) {
                newsObject.publishedAt = innerXml.toString();
            }
            else if(localName.equals("item") && insideItem == true) {
                newsObjects.add(newsObject);
                insideItem = false;
            }
            else if (localName.equals("group")) {
                gotURL = false;
            }


            innerXml.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            innerXml.append(ch, start, length);
        }
    }
}
