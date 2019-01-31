package com.vfreiman.mytwolevelcache.backend.businesslogic.services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    public static void serialize(final String path, final Object obj) {
        try(final OutputStream fos = Files.newOutputStream(Paths.get(path));
            final ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(final String path) {
        try (final InputStream fis = Files.newInputStream(Paths.get(path));
             final ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
