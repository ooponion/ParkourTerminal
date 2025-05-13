package parkourterminal.gui.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.Padding;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

import java.lang.reflect.Field;

public class CustomGuiTextField extends TextField {
    // 颜色优化
    private int backgroundColor = 0x60000000;   // 背景颜色（半透明深灰色）
    private int textColor = 0x80EEEEEE;         // 文本颜色（柔和白）
    private int disabledTextColor = 0x80999999; // 禁用时文本颜色（浅灰色）
    private int currentBorderColor = 0x60FFFFFF;

    private FontRenderer fontRendererObj;

    // 动画变量
    private int animatedCursorX = 0;

    public CustomGuiTextField(int id, FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(id, fontRenderer, x, y, width, height);
        this.fontRendererObj = fontRenderer;
        setPadding(new Padding(6,0,6,0));
        setEnableBackgroundDrawing(false);
        setSize(width,height);
    }
    public void Update(){
        scrollBar.UpdateContentSize(this.fontRendererInstance.getStringWidth(this.getText())+6);
    }


    public void setCurrentBorderColor(int color) {
        this.currentBorderColor = color;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // 禁用纹理和启用混合
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        int radius = 10;

        ShapeDrawer.drawRoundedRectBorder(getX(), getY() ,
                getWidth() , getHeight() ,
                currentBorderColor, radius);
        ShapeDrawer.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), backgroundColor, radius);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        scrollBar.Update();
        ScissorHelper.EnableScissor(getEntryLeft(),getEntryTop(),getEntryWidth(),getEntryHeight());
        GlStateManager.pushMatrix();
        GlStateManager.translate(-scrollBar.getInterpolatingContentOffset(),0,0);
        drawTextBox();
        GlStateManager.popMatrix();
        ScissorHelper.DisableScissor();
//        // 让文本居中
//        int textX = getEntryLeft();
//        int textY = getEntryTop() + (getEntryHeight() - fontRendererObj.FONT_HEIGHT) / 2;
//        int textWidth=getEntryWidth();
//        //String fullText = this.getText().substring(lineScrollOffset);
//        String fullText = this.getText().substring(0);
//        String visibleText = this.fontRendererObj.trimStringToWidth(fullText, getEntryWidth());
//
//        // 选中区域
//        if (this.isFocused() && this.getCursorPosition() != this.getSelectionEnd()) {
//            int selStart = Math.min(this.getCursorPosition(), this.getSelectionEnd());
//            int selEnd = Math.max(this.getCursorPosition(), this.getSelectionEnd());
//
////            int renderStart = Math.max(selStart, lineScrollOffset);
////            int renderEnd = Math.min(selEnd, lineScrollOffset + visibleText.length());
//            int renderStart = Math.max(selStart, 0);
//            int renderEnd = Math.min(selEnd, visibleText.length());
//
//            if (renderStart < renderEnd) {
//                //String beforeStart = fullText.substring(lineScrollOffset, renderStart);
//                String beforeStart = fullText.substring(0, renderStart);
//                String selectedText = fullText.substring(renderStart, renderEnd);
//
//                int selX = textX + this.fontRendererObj.getStringWidth(beforeStart);
//                int selWidth = this.fontRendererObj.getStringWidth(selectedText);
//                int selY = textY - 1;
//                int selHeight = fontRendererObj.FONT_HEIGHT + 1;
//
//                int selectionColor = 0x8055AACC;
//
//                GlStateManager.disableTexture2D();
//                GlStateManager.enableBlend();
//                ShapeDrawer.drawRect(selX, selY, selX + selWidth, selY + selHeight, selectionColor);
//                GlStateManager.disableBlend();
//                GlStateManager.enableTexture2D();
//            }
//        }
//
//        // 绘制文本
//        fontRendererObj.drawString(visibleText, textX, textY, textColor);
//
//        // 光标
//        if (this.isFocused()) {
//            int cursorPos = this.getCursorPosition();
//            //int cursorOffset = this.fontRendererObj.getStringWidth(fullText.substring(lineScrollOffset, cursorPos));
//            int cursorOffset = this.fontRendererObj.getStringWidth(fullText.substring(0, cursorPos));
//            int targetCursorX = textX + cursorOffset;
//
//            int dx = (int)((targetCursorX - animatedCursorX) * 0.2);
//            animatedCursorX += dx;
//
//            if (dx == 0 && Math.abs(targetCursorX - animatedCursorX) > 0) {
//                animatedCursorX = targetCursorX;
//            }
//
//            int cursorX = animatedCursorX;
//            int cursorY = textY;
//            int cursorHeight = fontRendererObj.FONT_HEIGHT - 1;
//
//            long currentTime = System.currentTimeMillis();
//            float blinkProgress = (float) Math.sin(currentTime / 200.0);
//            int alpha = (int) (128 + 127 * blinkProgress);
//            int cursorColor = (alpha << 24) | 0xFFFFFF;
//
//            GlStateManager.disableTexture2D();
//            GlStateManager.enableBlend();
//            ShapeDrawer.drawRect(cursorX, cursorY - 1, cursorX + 1, cursorY + cursorHeight, cursorColor);
//            GlStateManager.disableBlend();
//            GlStateManager.enableTexture2D();
//            scrollBar.Update();
////            this.lineScrollOffset= (int) (scrollBar.getInterpolatingContentOffset()*1.0D/scrollBar.getWidth()*this.getText().length());
////            System.out.printf("lineScrollOffsetcu:%s,%s,%d,%d\n",scrollBar.getInterpolatingContentOffset(),lineScrollOffset,this.getText().length(),scrollBar.getWidth());
////            lineScrollOffset=1;
//        }

    }
}
