package parkourterminal.global;

import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.util.Properties;

public class GlobalConfig {
    private static final String CONFIG_DIR = "config/parkourTerminal";
    private static final String CONFIG_FILE = CONFIG_DIR + "/globalConfig.properties";
    private static Properties properties = new Properties();

    public static boolean animation = false;
    public static int precision = 5;
    private static EnumChatFormatting labelColor=EnumChatFormatting.GOLD;
    private static EnumChatFormatting ValueColor=EnumChatFormatting.WHITE;
    public static EnumChatFormatting getLabelColor(){
        return labelColor;
    }

    public static EnumChatFormatting getValueColor() {
        return ValueColor;
    }

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
        try {
            InputStream input = new FileInputStream(CONFIG_FILE);
            properties.load(input);

            // 读取配置项
            animation = Boolean.parseBoolean(properties.getProperty("animation", String.valueOf(animation)));
            precision = Integer.parseInt(properties.getProperty("precision", String.valueOf(precision)));
        } catch (FileNotFoundException e) {
            System.err.println("Config file not found. Using default values.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            OutputStream output = new FileOutputStream(CONFIG_FILE);

            // 更新配置项
            properties.setProperty("animation", String.valueOf(animation));
            properties.setProperty("precision", String.valueOf(precision));

            // 读取配置项
            properties.store(output, "Application Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateConfig(String key, String value) {
        properties.setProperty(key, value);

        if (key.equals("animation"))
            animation = Boolean.valueOf(value);
        else if (key.equals("precision"))
            precision = Integer.valueOf(value);
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }
}
