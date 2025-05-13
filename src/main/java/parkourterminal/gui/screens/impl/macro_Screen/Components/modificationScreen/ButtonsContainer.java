package parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen;

import parkourterminal.gui.component.CustomButton;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;

public class ButtonsContainer extends Container {
    private final CustomButton duplicateLineButton;
    private final CustomButton deleteLineButton;
    private final CustomButton newLineButton;
    private final CustomButton deleteMacroButton;
    public ButtonsContainer(int x,int y,int width,int height){
        super(new Margin(0),new Padding(0),new noWarpLinearLayout(LayoutDirection.VERTICAL,5), ScrollDirection.VERTICAL);
        setSize(width, height);
        setPosition(x,y);
        float buttonsH=(height-15)/4.0f;
        duplicateLineButton=new CustomButton(x,y,width, (int) buttonsH,0xee7f9ba7,0xee2e96c3,4,"duplicate Line");
        deleteLineButton=new CustomButton(x,y,width, (int) buttonsH,0xee7f9ba7,0xee2e96c3,4,"delete Line");
        newLineButton=new CustomButton(x,y,width, (int) buttonsH,0xee7f9ba7,0xee2e96c3,4,"add new Line");
        deleteMacroButton=new CustomButton(x,y,width, (int) buttonsH,0xee7f9ba7,0xeece481f,4,"delete macro");
        addComponent( duplicateLineButton );
        addComponent( deleteLineButton );
        addComponent( newLineButton );
        addComponent( deleteMacroButton );
    }
    public int ClickedButton(){
        if(duplicateLineButton.isClicked()){
            return 0;
        }
        if(deleteLineButton.isClicked()){
            return 1;
        }
        if(newLineButton.isClicked()){
            return 2;
        }
        if(deleteMacroButton.isClicked()){
            return 3;
        }
        return -1;
    }
}
