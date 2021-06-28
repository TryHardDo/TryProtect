package eu.playerunion.tryprotect.utils;


import eu.playerunion.tryprotect.TryProtect;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.stream.Stream;

public class IOUtils {
    public static JSONObject readJSONFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                TryProtect.log("Could not create protections.yml.", Level.SEVERE);
                e.printStackTrace();
            }
        }

        JSONObject obj = null;
        StringBuilder builder = new StringBuilder("");

        try (Stream<String> stream = Files.lines(file.toPath())) {
            stream.forEach(s -> builder.append(s));
            obj = new JSONObject(builder.toString());
        } catch (Exception e) {
            TryProtect.log("Could not load JSON file.", Level.SEVERE);
            e.printStackTrace();
        }

        return obj;
    }

    public static void writeJSONFile(JSONObject obj, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                TryProtect.log("Could not write JSON file.", Level.SEVERE);
                e.printStackTrace();
            }
        }

        FileWriter fw;

        try {
            fw = new FileWriter(file);
            fw.write(obj.toString(4));
            fw.close();
        } catch (Exception e) {
            TryProtect.log("Could not save JSON file.", Level.SEVERE);
            e.printStackTrace();
        }
    }
}
