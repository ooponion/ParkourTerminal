package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages;

import parkourterminal.gui.layout.Container;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.util.listener.EventBus;
import parkourterminal.util.listener.EventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractToolTipPage  implements Page, EventListener {
    protected final EventBus eventBus;
    protected final KeyUITooltips container;
    protected final PageManager pageManager;
    private boolean inPage=false;
    private final List<UIComponent> pageComponents=new ArrayList<UIComponent>();
    public AbstractToolTipPage(EventBus eventBus, KeyUITooltips container, PageManager pageManager) {
        this.eventBus = eventBus;
        this.container = container;
        this.pageManager = pageManager;
    }
    public void onEnter(){
        container.addComponents(pageComponents);
        container.Update();
        inPage=true;
    };
    public void onExit(){
        container.deleteComponents(pageComponents);
        container.Update();
        inPage=false;
    };
    public List<UIComponent> pageComponents() {
        return pageComponents;
    }
    public void addComponent(UIComponent component){
        pageComponents.add(component);
        if(inPage){
            container.addComponent(component);
            container.Update();
        }
    }
    public void deleteComponent(UIComponent component){
        pageComponents.remove(component);
        if(inPage){
            container.deleteComponent(component);
            container.Update();
        }
    }
}
