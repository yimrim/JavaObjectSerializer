package de.yimrim;

import java.io.*;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DBConnection {

    private Connection con = null;

    public DBConnection(String dbURL, String dbUser, String dbPassword) throws SQLException {
        con = getConnection(dbURL, dbUser, dbPassword);
    }

    public void insert(ByteArrayOutputStream bos) throws SQLException {//inserts object
        try {
            byte[] array = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            PreparedStatement statement = con.prepareStatement("insert into objects (object) values (?)");//TODO make sql string dynamic
            statement.setBlob(1, bis);
            statement.executeUpdate();
            statement.close();
            con.close();
            bis.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void insert(ByteArrayOutputStream bos,String customID) throws SQLException {//inserts object with customID
        try {
            byte[] array = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            PreparedStatement statement = con.prepareStatement("insert into objects (object,customID) values (?,?)");//TODO make sql string dynamic
            statement.setBlob(1, bis);
            statement.setString(2,customID);
            statement.executeUpdate();
            statement.close();
            con.close();
            bis.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getLatestPK() throws SQLException {//gets the primary key from the newest object in the database
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("select max(object_key) from objects");
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public ByteArrayInputStream extract(int id) {//gets Object by primary key
        try {
            Blob blob = null;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select object from objects where object_key=" + String.valueOf(id));
            if (rs.next()) {
                blob = rs.getBlob(1);
            }
            statement.close();
            con.close();
            return (ByteArrayInputStream) blob.getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ByteArrayInputStream extract(String customID) {//gets Objects by customID
        try {
            Blob blob = null;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select object from objects where customID=" + customID);
            if (rs.next()) {
                blob = rs.getBlob(1);
            }
            statement.close();
            con.close();
            return (ByteArrayInputStream) blob.getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}