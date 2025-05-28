package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.guiPage;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.component.fontRenderer.ConsolaFontRenderer;
import parkourterminal.gui.component.fontRenderer.DDFontRenderer;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.DisableDiv;
import parkourterminal.util.SystemOutHelper;
import parkourterminal.util.listener.EventBus;

public class GuiPage extends AbstractToolTipPage{
    private final DisableDiv disableDiv=new DisableDiv();
    private final CustomGuiTextField textField;
    public GuiPage(final EventBus eventBus,final KeyUITooltips container, PageManager pageManager1) {
        super(eventBus, container, pageManager1);
        CustomButton button=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Reset size");
        button.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
                if(keyUI==null){
                    return;
                }
                keyUI.resetSize();
            }
        });
        disableDiv.setDisableListener(new DisableDiv.DisableListener() {
            @Override
            public void onDisable() {
                setBindingKeyUIEnabled(false);
            }

            @Override
            public void onEnable() {
                setBindingKeyUIEnabled(true);
            }
        });

        final PageManager pageManager_=pageManager;
        CustomButton colorSettings=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Color Settings");
        colorSettings.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager_.switchTo("ColorSettingsPage");
            }
        });
        textField=new CustomGuiTextField(0, ConsolaFontRenderer.newInstance(),0,0,60,30);
        textField.setMaxStringLength(KeyUI.MAX_NAME_LENGTH);
        textField.setTextChangeListener(new  CustomGuiTextField.TextChangeListener() {
            @Override
            public void onTextChange(String text) {
                KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
                if(keyUI==null){
                    return;
                }
                keyUI.setActionName(text);
            }}
        );
        CustomButton deleteButton=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Delete Button");
        deleteButton.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                eventBus.emit("deleteKeyUI",null);
            }
        });
        addComponent(disableDiv);
        addComponent(button);
        addComponent(colorSettings);
        addComponent(textField);
        addComponent(deleteButton);
    }

    @Override
    public void registerListeners() {
        eventBus.register("rightClickKeyUI",this);
        eventBus.register("deleteKeyUI",this);
    }

    @Override
    public String getIdentifier() {
        return "guiPage";
    }
    private void setBindingKeyUIEnabled(boolean enabled) {
        KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
        if(keyUI==null){
            return;
        }
        keyUI.setEnabled(enabled);
    }
    public void onEvent(String eventType, Object data){
        if(eventType.equals("rightClickKeyUI")&&data instanceof Boolean){
            disableDiv.setDisabled(!((Boolean) data));
            KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
            if(keyUI==null){
                return;
            }
            textField.setText(keyUI.getActionName());
        }else if(eventType.equals("deleteKeyUI")){
            KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
            if(keyUI==null){
                return;
            }
            KeyUIManager.getInstance().removeKeyUI(keyUI);
            container.setFocused(false);
            container.setEnabled(false);
        }
    }
}