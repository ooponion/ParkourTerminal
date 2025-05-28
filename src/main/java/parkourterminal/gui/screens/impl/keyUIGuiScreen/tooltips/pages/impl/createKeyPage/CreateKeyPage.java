package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.component.fontRenderer.ConsolaFontRenderer;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.DisableDiv;
import parkourterminal.util.SystemOutHelper;
import parkourterminal.util.listener.EventBus;

public class CreateKeyPage  extends AbstractToolTipPage {
    private BindingMouseHereDiv bindingMouseHereDiv ;
    private StartBindingButton startBindingButton;
    public CreateKeyPage(EventBus eventBus, final KeyUITooltips container, PageManager pageManager1) {
        super(eventBus, container, pageManager1);
        bindingMouseHereDiv =new BindingMouseHereDiv(60,60);
        startBindingButton=new StartBindingButton(60,30,0xFFFF0000,0xFF00FF00,0xFFFFFFFF,2);
        addComponent(new Tipdiv(60,40));
        addComponent(startBindingButton);
    }

    @Override
    public void registerListeners() {
        eventBus.register("bindingEvent",this);
        eventBus.register("startBindingEvent",this);
        eventBus.register("bindingCancelEvent",this);
    }

    @Override
    public String getIdentifier() {
        return "createKeyPage";
    }
    public void onEvent(String eventType, Object data){
        if(eventType.equals("bindingEvent")&&data instanceof Integer){
            for (KeyBinding binding : Minecraft.getMinecraft().gameSettings.keyBindings) {
                if (binding.getKeyCode() == (Integer) data) {
                    ToolTipManager.getInstance().getEventBus().emit("createKeyEvent",new CreateKeyStruc(binding,container.getX(),container.getY()));
                    return;
                }
            }
        }
        else if(eventType.equals("startBindingEvent")){
            deleteComponent(bindingMouseHereDiv);
            addComponent(bindingMouseHereDiv);
        }
        else if(eventType.equals("bindingCancelEvent")){
            deleteComponent(bindingMouseHereDiv);
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        startBindingButton.reset();
    }

    public void onExit(){
        super.onExit();
        deleteComponent(bindingMouseHereDiv);
    };
}