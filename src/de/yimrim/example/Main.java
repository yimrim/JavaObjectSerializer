package de.yimrim.example;

import de.yimrim.main.Serializer;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        //Serialization process:
        Person michael = new Person("Michael", 22);
        Serializer s = new Serializer("jdbc:mysql://localhost:3306/objects", "root", "");
        s.databaseSerialize(michael, "michael");

        //Deserialization process:
        Person michael2 = (Person) s.databaseDeserialize("michael");
        System.out.println(michael2.getName());

    }

}
