package com.study.portaljdbc;


import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class PortalDriver implements Driver {
    static {
        PortalDriver portalDriver = new PortalDriver();
        try {
            DriverManager.registerDriver(portalDriver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        System.out.println("Portal try to connect to database");
        String[] urlAndPort = url.split(":");
        try {
            Socket socket = new Socket(urlAndPort[0], Integer.parseInt(urlAndPort[1]));
            return new PortalConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
