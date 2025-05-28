package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages;

import java.util.HashMap;
import java.util.Map;

public class PageManager {
    private Page currentPage;
    private Map<String,Page> pages =new HashMap<String,Page>();
    public void switchTo(Page newPage) {
        if (currentPage != null) currentPage.onExit();
        currentPage = newPage;
        currentPage.onEnter();
    }
    public void switchTo(String Identifier) {
        Page newPage=getPageByName(Identifier);
        if(newPage==null){
            return;//failed
        }
        if (currentPage != null) currentPage.onExit();
        currentPage = newPage;
        currentPage.onEnter();
    }
    public Page getCurrentPage() {
        return currentPage;
    }
    public void registerPage(Page newPage) {
        pages.put(newPage.getIdentifier(), newPage);
        newPage.registerListeners();
    }
    public Page getPageByName(String Identifier){
        return pages.get(Identifier);
    }
}
