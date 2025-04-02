package parkourterminal.global.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import parkourterminal.global.GlobalConfig;
import parkourterminal.data.ColorData.ColorData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TerminalJsonConfig {
    private static TerminalJsonRoot root = new TerminalJsonRoot();
    public static void ReadConfig() {
        Gson gson = new Gson();
        try {
            if (!new File(GlobalConfig.getConfigDir() + "/config.json").exists()) {
                WriteConfig();
            }
            FileReader reader = new FileReader(GlobalConfig.getConfigDir() + "/config.json");

            root = gson.fromJson(reader, TerminalJsonRoot.class);
            if(root==null){
                root=new TerminalJsonRoot();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void WriteConfig(){
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            FileWriter writer = new FileWriter(GlobalConfig.getConfigDir()+ "/config.json");
            gson.toJson(root, writer);
            writer.flush();
            System.out.printf("JSON file has been saved successfully.%s\n",root==null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static HashMap<String, LabelJson> getUsedLabelJsons() {
        return root.getUsedLabels();
    }

    public static void setLabelList(HashMap<String, LabelJson> labels) {
        root.setUsedLabels(labels);
        WriteConfig();
    }
    public static void saveLabels(){
        LabelManager.saveConfigUsedLabels();
    }
    public static LandBlockJson getLandBlockJson(){
        return root.getLandBlock();
    }
    public static void setLandBlockJson(LandBlockJson landBlockJson){
        root.setLandBlock(landBlockJson);
    }
    public static ColorData getColors(){
        return root.getColorData();
    }
}
