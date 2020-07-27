package de.yimrim.main;

import java.io.*;
import java.sql.*;

import static java.sql.DriverManager.getConnection;


public class DBConnection {

    private Connection con;

    public DBConnection(String dbURL, String dbUser, String dbPassword) throws SQLException {
        con = getConnection(dbURL, dbUser, dbPassword);
    }

    public void insert(ByteArrayOutputStream bos) {//inserts object
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

    public void insert(ByteArrayOutputStream bos, String customID) {//inserts object with customID
        try {
            byte[] array = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(array);
            PreparedStatement statement = con.prepareStatement("insert into objects (customID, object) values (?,?)");//TODO make sql string dynamic
            statement.setString(1, customID);
            statement.setBlob(2, bis);
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
            PreparedStatement statement = con.prepareStatement("select object from objects where object_key=?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob(1);
            }
            statement.close();
            con.close();
            assert blob != null;
            return (ByteArrayInputStream) blob.getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ByteArrayInputStream extract(String customID) {//gets Objects by customID
        try {
            Blob blob = null;
            PreparedStatement statement = con.prepareStatement("select object from objects where customID=?");
            statement.setString(1, customID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob(1);
            }
            statement.close();
            con.close();
            assert blob != null;
            return (ByteArrayInputStream) blob.getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}