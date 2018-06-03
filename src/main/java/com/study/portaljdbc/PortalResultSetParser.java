package com.study.portaljdbc;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortalResultSetParser {
    private List<String> headersList;
    private List<List<String>> rows;
    private int rowCount;

    public PortalResultSet parseXml(String xml) throws SQLException {
        //set
        rowCount = readXmlContent(xml);
        PortalResultSet portalResultSet = new PortalResultSet();

        PortalResultSetMetaData resultSetMetaData = new PortalResultSetMetaData();
        resultSetMetaData.setColumns(headersList);

        portalResultSet.setResultSetMetaData(resultSetMetaData);
        portalResultSet.setValues(rows);

        System.out.println(headersList);
        System.out.println(rows);

        return portalResultSet;
    }

    int readXmlContent(String xml) throws SQLException {
        System.out.println( xml);//!!remove
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
            doc.getDocumentElement().normalize();

            String response = doc.getDocumentElement().getNodeName();
            if(response.equalsIgnoreCase("table")){
                System.out.println("good response");
                readSelectResponse(doc);
            }else if (response.equalsIgnoreCase("information")){
                String message = doc.getElementsByTagName("message").item(0).getTextContent();
                System.out.println("Message from response: "+message);//somehow handle it
                String value = doc.getElementsByTagName("rowCount").item(0).getTextContent();
                System.out.println("value:"+value);
                return Integer.parseInt(value);
            }else if (response.equalsIgnoreCase("error")){
                throw new SQLException(doc.getDocumentElement().getTextContent(), "abort", -1);
            }else{
                throw new SQLException("not known type of response was arrived from the DB: "+response, "abort", -1);
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    private void readSelectResponse(Document doc) {
        headersList = new ArrayList<>();
        NodeList nodeColumn = doc.getElementsByTagName("column");
        for (int i = 0; i < nodeColumn.getLength(); i++) {
            NodeList params = nodeColumn.item(i).getChildNodes();
            for (int c = 0; c < params.getLength(); c++) {
                if (params.item(c).getNodeType() == Node.ELEMENT_NODE) {
                    if ("name".equals(params.item(c).getNodeName())) {
                        String name = params.item(c).getTextContent();
                        headersList.add(name);
//                            System.out.println("name:" + name);
                    }
                }
            }
        }
//            System.out.println("------------------------------------------------");

        rows = new ArrayList<>();
        NodeList nodeRow = doc.getElementsByTagName("row");
        for (int i = 0; i < nodeRow.getLength(); i++) {
            List<String> row = new ArrayList<>();
            NodeList columns = nodeRow.item(i).getChildNodes();
            for (int c = 0; c < columns.getLength(); c++) {
                if (columns.item(c).getNodeType() == Node.ELEMENT_NODE) {
                    String content = columns.item(c).getTextContent();
//                        System.out.println(columns.item(c).getNodeName() + " : "+ content);
                    row.add(content);
                }
            }
            rows.add(row);
//                System.out.println("------------------------------------------------");
        }

    }


}
