package parkourterminal.global;

import parkourterminal.global.json.TerminalJsonConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class GlobalConfig {
    private static final String CONFIG_DIR = "config/parkourTerminal";
    private static final String CONFIG_FILE = CONFIG_DIR + "/globalConfig.properties";

    private static Properties properties = new Properties();



    public static void configInit() {
        ensureConfigDirectoryExists();
        loadConfig();
    }

    private static void ensureConfigDirectoryExists() {
        File configDir = new File(CONFIG_DIR);

        if (!configDir.exists()) {
            boolean created = configDir.mkdirs();

            if (!created)
                System.err.println("Failed to create configuration directory: " + CONFIG_DIR);
        }
    }

    private static void loadConfig() {
        TerminalJsonConfig.ReadConfig();
        try {
            InputStream input = new FileInputStream(CONFIG_FILE);
            properties.load(input);

            // 读取配置项
//            animation = Boolean.parseBoolean(properties.getProperty("animation", String.valueOf(animation)));
//            precision = Integer.parseInt(properties.getProperty("precision", String.valueOf(precision)));
        } catch (FileNotFoundException e) {
            System.err.println("Config file not found. Using default values.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveConfig() {
        try {
            OutputStream output = Files.newOutputStream(Paths.get(CONFIG_FILE));

            // 更新配置项
//            properties.setProperty("animation", String.valueOf(animation));
//            properties.setProperty("precision", String.valueOf(precision));

            // 读取配置项
            properties.store(output, "Application Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateConfig(String key, String value) {
        properties.setProperty(key, value);

//        if (key.equals("animation"))
//            animation = Boolean.valueOf(value);
//        else if (key.equals("precision"))
//            precision = Integer.valueOf(value);
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }
    public static String getConfigDir(){
        return CONFIG_DIR;
    }
}
