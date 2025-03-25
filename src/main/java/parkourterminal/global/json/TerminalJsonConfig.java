package parkourterminal.global.json;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class TerminalJsonConfig {
    private static TerminalJsonRoot root = new TerminalJsonRoot();
    public static void ReadConfig() {
        Gson gson = new Gson();
        try {
            if (new File(GlobalConfig.getConfigDir() + "/config.json").exists()) {
                WriteConfig();
            }
            FileReader reader = new FileReader(GlobalConfig.getConfigDir() + "/config.json");
            Type rootType = new TypeToken<TerminalJsonRoot>(){}.getType();

            if (root != null) {
                root = gson.fromJson(reader, rootType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void WriteConfig(){
        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(GlobalConfig.getConfigDir()+ "/config.json");
            gson.toJson(root, writer);
            System.out.println("JSON file has been saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static HashMap<String, LabelJson> getUsedLabelJsons() {
        if(root==null){
            return new HashMap<String, LabelJson>();
        }
        return root.getUsedLabelJsons();
    }

    public static void setLabelList(HashMap<String, LabelJson> labels) {
        if(root==null){
            root = new TerminalJsonRoot();
        }
        root.setUsedLabelJsons(labels);
        WriteConfig();
    }
}
