# JavaObjectSerializer
A Java library for object de-/serialization with database and file support

      USE THIS TABLE STRUCTURE IN YOUR DATABASE

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

       USE FOLLOWING STRUCTURE FOR dbURL: "jdbc:mysql://ADRESS:PORT/DATABASENAME"
        
        You need to use a suitable database library (i used the official mysql connector)
