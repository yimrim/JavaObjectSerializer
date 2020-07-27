package de.yimrim;

import java.io.*;
import java.sql.SQLException;

public class Serializer {

    /* USE THIS TABLE STRUCTURE IN YOUR DATABASE

                create table objects
        (
            object_key int auto_increment
                primary key,
            customID   varchar(30) null,
            object     blob        null,
            constraint objects_customID_uindex
                unique (customID)
        );

       YOUR CLASS HAS TO IMPLEMENT SERIALIZATION

     */


    private String dbURL = null;
    private String dbUser = null;
    private String dbPassword = null;

    public Serializer() {
    }

    public Serializer(String dbURL, String dbUser, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public void setDbURL(String dbURL) {
        this.dbURL = dbURL;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public void fileSerialize(Object o, String filepath) throws IOException { //serializes an object into an file
        FileOutputStream fos = new FileOutputStream(filepath + ".ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(o);
        oos.close();
        fos.close();
    }

    public Object fileDeserialize(String filepath) throws IOException, ClassNotFoundException { //returns an object from a file
        FileInputStream fis = new FileInputStream(filepath + ".ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }

    public int databaseSerialize(Object o) throws IOException, SQLException { //serializes an object into an database with database credentials given in the constructor

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);

        DBConnection dbConnection = new DBConnection(dbURL, dbUser, dbPassword);
        dbConnection.insert(bos);

        oos.close();
        bos.close();
        return dbConnection.getLatestPK(); //returns the primary key of the inserted object, for future identification
    }

    public void databaseSerialize(Object o, String customID) throws IOException, SQLException { //serializes an object into an database with database credentials given in the constructor and customID
        //TODO add parameter for table and column name
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);

        DBConnection dbConnection = new DBConnection(dbURL, dbUser, dbPassword);
        dbConnection.insert(bos);

        oos.close();
        bos.close();
    }

    public Object databaseDeserialize(int ID) throws IOException, ClassNotFoundException, SQLException {// returns an object from the database, by given ID(primary key from the database)
        DBConnection dbConnection = new DBConnection(dbURL, dbUser, dbPassword);
        ObjectInputStream ois = new ObjectInputStream(dbConnection.extract(ID));
        Object object = ois.readObject();
        return object;
    }

    public Object databaseDeserialize(String customID) throws IOException, ClassNotFoundException, SQLException {// returns an object from the database, by given CustomID(primary key from the database)
        DBConnection dbConnection = new DBConnection(dbURL, dbUser, dbPassword);
        ObjectInputStream ois = new ObjectInputStream(dbConnection.extract(customID));
        Object object = ois.readObject();
        return object;
    }


}
