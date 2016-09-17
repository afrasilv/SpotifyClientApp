package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public static void main(String[] args){
        try{
            Schema schema = new Schema(1, "com.afrasilv.spotifyclientapp.model");

            addTables(schema);

            new DaoGenerator().generateAll(schema, OUT_DIR);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void addTables(Schema schema){
        // Entities
        addToken(schema);
    }

    private static void addToken(Schema schema){
        Entity token = schema.addEntity("DBToken");
        token.addIdProperty().primaryKey().autoincrement();
        token.addStringProperty("tokenvalue").notNull();
    }
}
