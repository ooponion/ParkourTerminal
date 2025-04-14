package parkourterminal.gui.screens.impl.configScreen.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.component.CustomButton;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.configScreen.intf.handlers.*;

public class ConfigContainer extends Container {
    private final static int spacing =7;
    public ConfigContainer(){
        super(new Margin(0),new Padding(0),new noWarpLinearLayout(LayoutDirection.VERTICAL,spacing));
        addComponent(new ConfigLine("display landblock",ConfigType.Switcher,new LandblockDisplayModifier()));
        addComponent(new ConfigLine("send chat pb",ConfigType.Switcher,new LandblockPBModifier()));
        addComponent(new ConfigLine("send chat offset",ConfigType.Switcher,new LandblockOffsetModifier()));
        addComponent(new ConfigLine("show labels",ConfigType.Switcher,new ShowLabelsModifier()));
        addComponent(new ConfigLine("trim zeros",ConfigType.Switcher,new TrimZerosModifier()));
        addComponent(new ConfigLine("coords precision",ConfigType.TextField,new DFModifier()));
        addComponent(new ConfigLine("color label",ConfigType.TextField,new ColorLabelModifier()));
        addComponent(new ConfigLine("color value",ConfigType.TextField,new ColorValueModifier()));
        addComponent(new ConfigLine("prefix",ConfigType.TextField,new PrefixModifier()));
        addComponent(new ConfigLine("landblock color",ConfigType.TextField,new LandBlockColorModifier()));
        addComponent(new ConfigLine("lb cond color",ConfigType.TextField,new LandBlockCondColorModifier()));
    }
    @Override
    public void Update(){
        for(UIComponent component:getComponents()){
            component.Update();
        }
    }
    public void keyTyped(char typedChar, int keyCode){
        for(UIComponent component:getComponents()){
            ((ConfigLine) component).keyTyped(typedChar, keyCode);
        }
    }
    public void SaveConfig(){
        for(UIComponent component:getComponents()){
            ((ConfigLine) component).SaveConfig();
        }
    }
    public void WithDraw(){
        for(UIComponent component:getComponents()){
            ((ConfigLine) component).WithDraw();
        }
    }
    public void SyncFromConfig(){
        for(UIComponent component:getComponents()){
            ((ConfigLine) component).SyncFromConfig();
        }
    }
}
