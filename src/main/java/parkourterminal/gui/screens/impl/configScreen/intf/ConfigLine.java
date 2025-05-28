package parkourterminal.gui.screens.impl.configScreen.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.component.ToggleSwitch;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.configScreen.intf.handlers.SettingModifier;

public class ConfigLine extends UIComponent{
    private final SettingModifier modificator;
    private final String settingName;
    private final ConfigType configType;
    private final UIComponent component;
    private final int nameSpace=100;
    private final int height=20;
    private final int minCompWidth=30;
    private final int ButtonsWidth=100;
    private int marginWidth=80;
    private CustomButton withdrawButton=new CustomButton(0,0,ButtonsWidth/2,height,0xee7f9ba7,0xee2e96c3,4,"withdraw");
    private CustomButton defaultButton=new CustomButton(0,0,ButtonsWidth/2,height,0xee7f9ba7,0xee2e96c3,4,"default");

    public ConfigLine(String settingName,ConfigType configType,SettingModifier modificator){
        this.configType=configType;
        this.settingName=settingName;
        if(configType==ConfigType.TextField){
            component=new CustomGuiTextField(0, Minecraft.getMinecraft().fontRendererObj, 0,0,minCompWidth,height);
        } else {
            component=new ToggleSwitch(minCompWidth,height,"");
        }
        this.modificator=modificator;
    }
    @Override
    public void Update(){
        GuiScreen guiScreen=Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        int compWidth= Math.max(guiScreen.width-marginWidth*2-nameSpace-ButtonsWidth,minCompWidth);
        int totalWidth=compWidth+nameSpace+ButtonsWidth;
        this.setSize(totalWidth,height);
        marginWidth=(guiScreen.width-getEntryWidth())/2;
        this.setX(marginWidth);
        component.setSize(compWidth,height);
        component.setX(marginWidth+ nameSpace);

    };
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(configType==ConfigType.TextField){
            withdrawButton.setEnabled(!((CustomGuiTextField)component).getText().equals(String.valueOf(modificator.getConfigValue())));
            defaultButton.setEnabled(!((CustomGuiTextField)component).getText().equals(String.valueOf(modificator.getDefaultValue())));
        } else if(configType==ConfigType.Switcher){
            withdrawButton.setEnabled(!modificator.getConfigValue().equals(((ToggleSwitch)component).isOn()));
            defaultButton.setEnabled(!modificator.getDefaultValue().equals(((ToggleSwitch)component).isOn()));
        }
        FontRenderer renderer =Minecraft.getMinecraft().fontRendererObj;
        withdrawButton.draw(mouseX, mouseY, partialTicks);
        defaultButton.draw(mouseX, mouseY, partialTicks);
        component.draw(mouseX, mouseY, partialTicks);
        renderer.drawString(settingName,marginWidth,getEntryTop()+(getEntryHeight()- renderer.FONT_HEIGHT)/2,0xFFFFFFFF);
        if(configType==ConfigType.TextField&& !modificator.ifSatisfied(((CustomGuiTextField)component).getText())){
            renderer.drawString("!",marginWidth+nameSpace,getEntryTop()+(getEntryHeight()- renderer.FONT_HEIGHT)/2,0xFFed3e15);
        }
    }
    @Override
    public void setY(int y){
        super.setY(y);
        component.setY(getEntryTop());
        withdrawButton.setY(getEntryTop());
        defaultButton.setY(getEntryTop());
    }
    @Override
    public void setPosition(int x,int y){
        super.setPosition(x,y);
        component.setX(getEntryLeft()+ nameSpace);
        component.setY(getEntryTop());
        withdrawButton.setPosition(getEntryRight()-ButtonsWidth,getEntryTop());
        defaultButton.setPosition(getEntryLeft()-ButtonsWidth/2,getEntryTop());
    }
    @Override
    public void setX(int x){
        super.setX(x);
        component.setX(getEntryLeft()+ nameSpace);
        withdrawButton.setX(getEntryRight()-ButtonsWidth);
        defaultButton.setX(getEntryRight()-ButtonsWidth/2);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(withdrawButton.mouseClicked(mouseX, mouseY, mouseButton)){
            SyncFromConfig();
        }else if(defaultButton.mouseClicked(mouseX, mouseY, mouseButton)){
            WithDraw();
        }
        return component.mouseClicked(mouseX,mouseY,mouseButton);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        component.mouseReleased(mouseX, mouseY, state);
    }
    public void keyTyped(char typedChar, int keyCode){
        if(configType==ConfigType.TextField){
            ( (CustomGuiTextField)component).textboxKeyTyped(typedChar, keyCode);
        }
    }
    public void SaveConfig(){
        if(configType==ConfigType.TextField){
            modificator.setConfigValue(((CustomGuiTextField)component).getText());
            ((CustomGuiTextField)component).setText(String.valueOf(modificator.getConfigValue()));
        } else if(configType==ConfigType.Switcher){
            modificator.setConfigValue(String.valueOf(((ToggleSwitch)component).isOn()));
            ((ToggleSwitch)component).setOn((Boolean)modificator.getConfigValue());
        }
    }
    public void SyncFromConfig(){
        if(configType==ConfigType.TextField){
            ((CustomGuiTextField)component).setText(String.valueOf(modificator.getConfigValue()));
        } else if(configType==ConfigType.Switcher){
            ((ToggleSwitch)component).setOn((Boolean)modificator.getConfigValue());
        }
    }
    public void WithDraw(){
        if(configType==ConfigType.TextField){
            ((CustomGuiTextField)component).setText(String.valueOf(modificator.getDefaultValue()));
        } else if(configType==ConfigType.Switcher){
            ((ToggleSwitch)component).setOn((Boolean)modificator.getDefaultValue());
        }
    }
}
