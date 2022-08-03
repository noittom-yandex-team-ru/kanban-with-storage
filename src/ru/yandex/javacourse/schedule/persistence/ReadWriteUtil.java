package ru.yandex.javacourse.schedule.persistence;

import java.io.*;

public class ReadWriteUtil {
    private ReadWriteUtil() {
    }

    public static <T> T read(File path) {
        try {
            FileInputStream fileIn = new FileInputStream(path);

            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            objectIn.close();
            return (T) obj;
        } catch (Exception e) {
            System.err.println("Can not read from: " + path.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void write(String path, T obj) {
        try {
            File f = new File(path);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (IOException e) {
            System.err.println("can not create file, path: " + path);
            e.printStackTrace();
        }
    }

}
