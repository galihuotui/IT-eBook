package com.cky;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.DaoGenerator;

public class CkyDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.cky.greendao");
        addBook(schema);

        new DaoGenerator().generateAll(schema, "/Users/cuikangyuan/Desktop/work/android_workspace/Rx/app/src/main/java-gen");
    }

    private static void addBook(Schema schema) {
        Entity book = schema.addEntity("Book");

        book.addIdProperty();
        book.addStringProperty("book_name");
        book.addStringProperty("book_isbn");
        book.addStringProperty("book_id");
        book.addStringProperty("request_id");
        book.addStringProperty("download_status");
    }

}
