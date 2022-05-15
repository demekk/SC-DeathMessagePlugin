package pl.bonzai.death.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Config {

    private static Config instance;
    public Messages messages;
    public ProgressBar bar;

    public Config(){
        messages = new Messages();
        bar = new ProgressBar();
    }

    public static class Messages{
        public String deathMessageTitle = "&cZabil cie &4{KILLER}";
        public String deathMessageSubTitle = "{HEALTH} &7(&e{ABS}&6❤&7)";
    }

    public static class ProgressBar{
        public String completedBarColor = "&c";
        public String notCompletedBarColor = "&7";
    }

    public static Config getInstance() {
        if (Config.instance == null) {
            Config.instance = fromDefaults();
        }
        return Config.instance;
    }

    public static void init(File filePath){
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    public static void load(final File file) {
        Config.instance = fromFile(file);
        if (Config.instance == null) {
            Config.instance = fromDefaults();
        }
    }

    public static void load(final String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        final Config config = new Config();
        return config;
    }

    public void toFile(final String file) {
        this.toFile(new File(file));
    }

    public void toFile(final File file) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String jsonconfig = gson.toJson(this);
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        encoder.onMalformedInput(CodingErrorAction.REPORT);
        encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),encoder));
            writer.write(jsonconfig);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Config fromFile(final File configFile) {
        try {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
            return gson.fromJson(reader, Config.class);
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
