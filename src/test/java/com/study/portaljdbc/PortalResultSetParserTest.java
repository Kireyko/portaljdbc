package com.study.portaljdbc;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class PortalResultSetParserTest {

    public String loadFile() throws IOException {
        String line;
        String name = "table2.xml";
        StringBuilder stringBuilder= new StringBuilder();
        BufferedReader sourceStream = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(name)));
        while ((line = sourceStream.readLine()) != null ) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    @Test
    public void readXmlContent() throws SQLException {
        PortalResultSetParser parser = new PortalResultSetParser();

        try {
            parser.parseXml(loadFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}