package parkourterminal.gui.screens.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.component.Slider.SliderImpl;
import parkourterminal.gui.component.Slider.SliderValueChangedListener;
import parkourterminal.gui.component.fontRenderer.DDFontRenderer;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.gui.screens.impl.InGameMenuGui.IngameMenuGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.BlurRenderer;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;
import parkourterminal.util.SystemOutHelper;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class CustomIngameMenu extends GuiIngameMenu implements InstantiationScreen {
    // 旋转动画
    private float rotationAngle = 0.0f;
    private long lastUpdateTime = System.currentTimeMillis();
    private final DDFontRenderer DDfontRendererObj = new DDFontRenderer(Minecraft.getMinecraft(),3f);
    private final SliderImpl<Interpolatingfloat> slider=new SliderImpl<Interpolatingfloat>(
            new Interpolatingfloat(0),
            new Interpolatingfloat(100),
            new Interpolatingfloat(40),
            16, 100, 30,
            30, 300, new SliderValueChangedListener<Interpolatingfloat>() {
                @Override
                public void onValueChanged(Interpolatingfloat newValue) {
                    SystemOutHelper.printf("newValue %s", newValue.getValue());
                }
    }
    );
    @Override
    public void initGui() {
        BlurRenderer.initSharedBlurShader(10.0f);
        super.initGui();
        slider.setEnabled(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 绘制原版菜单界面
        drawSuperScreen(mouseX, mouseY, partialTicks);
        String string="Parkour Terminal";
        DDfontRendererObj.drawStringWithShadow(string, (this.width / 2.0f - DDfontRendererObj.getStringWidth(string) / 2f), 40, 0xFFFFFFFF);

        // 定义图标和外圈矩形的位置与尺寸
        int iconSize = 32;           // 图标大小为 32x32 像素
        int iconX = 10;              // 图标绘制在屏幕左上角（x 坐标 10 像素）
        int iconY = 10;              // 图标绘制在屏幕顶部（y 坐标 10 像素）

        int blurColor = 0x40FFFFFF;  // 25% 透明的白色，防止背景变黑
        int rectRadius = 5;          // 圆角半径设为 5 像素
        float blurIntensity = 10.0f; // 与 initGui 中的模糊强度保持一致

        // 先绘制一个带毛玻璃效果的外圈矩形
        BlurRenderer.drawBlurredRoundedRect(iconX, iconY, iconSize, iconSize, blurColor, rectRadius, blurIntensity, partialTicks);

        // 计算动画
        boolean isHovered = mouseX >= iconX && mouseX <= iconX + iconSize &&
                mouseY >= iconY && mouseY <= iconY + iconSize;

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 500.0f; // 计算时间差（秒）
        lastUpdateTime = currentTime;

        if (isHovered) {
            if (rotationAngle < 360.0f)
                rotationAngle += 360.0f * deltaTime; // 让图标顺时针旋转
            else
                rotationAngle = 360.0f;
        } else {
            // 当鼠标移开时，逐渐恢复到 0°
            rotationAngle *= 0.9f;
        }

        // 计算矩形动画
        float expandProgress = rotationAngle / 360.0f; // 归一化到 0-1
        String fullText = "Settings";
        int maxTextWidth = DDfontRendererObj.getStringWidth(fullText) + 10; // 最大矩形宽度
        int textRectWidth = (int) (expandProgress * maxTextWidth);
        int textRectHeight = iconSize;
        int texticonX = iconX + iconSize + 3; // 齿轮右侧
        int textRectY = iconY;

        // 绘制展开的圆角矩形
        if(textRectWidth>6) {
            BlurRenderer.drawBlurredRoundedRect(texticonX, textRectY, textRectWidth, textRectHeight, blurColor, 3, blurIntensity, partialTicks);
        }

        // 绑定自定义图标纹理
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("parkourterminal", "textures/gui/settings.png"));
        // 确保混合模式正确
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();  // 防止深度测试影响

        // 确保颜色状态正确
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 旋转图标
        GlStateManager.pushMatrix();
        GlStateManager.translate(iconX + iconSize / 2.0, iconY + iconSize / 2.0, 0); // 平移到中心
        GlStateManager.rotate(rotationAngle, 0, 0, 1); // 旋转
        GlStateManager.translate(-iconSize / 2.0, -iconSize / 2.0, 0); // 还原偏移

        // 绘制图标
        ShapeDrawer.drawScaledCustomSizeModalRect(0, 0, 0, 0, 64, 64, iconSize, iconSize, 64, 64);

        GlStateManager.popMatrix(); // 恢复 OpenGL 变换

        // 逐渐显示 "Settings"
        ScissorHelper.EnableScissor(texticonX,textRectY,textRectWidth,textRectHeight);

        DDfontRendererObj.drawString(fullText, texticonX + 5, textRectY + 6, 0xFFFFFFFF);

        ScissorHelper.DisableScissor();


        // 恢复 OpenGL 状态
        GlStateManager.enableDepth();
        slider.draw(mouseX, mouseY, partialTicks);

    }


    @Override
    public void onGuiClosed() {
        BlurRenderer.cleanupBlurResources();
        super.onGuiClosed();
        try {
            super.mouseClicked(0,0,0);
        } catch (IOException ignored) {
        }
        SystemOutHelper.printf("guiClosed");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // 计算图标的区域
        int iconSize = 32;
        int iconX = 10;
        int iconY = 10;

        boolean isClicked = mouseX >= iconX && mouseX <= iconX + iconSize &&
                mouseY >= iconY && mouseY <= iconY + iconSize;

        if (isClicked && mouseButton == 0) { // 左键点击
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(null); // 关闭当前 GUI，回到游戏界面
            mc.displayGuiScreen(new IngameMenuGui()); // 打开自定义 IngameMenuGui
        }

        slider.mouseClicked(mouseX, mouseY, mouseButton);
    }
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        slider.mouseReleased(mouseX, mouseY, state);

    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        slider.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        super.keyTyped(typedChar, keyCode); // 调用父类处理（如文本框输入）
        slider.keyTyped(typedChar, keyCode);
    }

    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    @Override
    public ScreenID getScreenID() {
        return new ScreenID("CustomInGameMenu");
    }
    private void drawSuperScreen(int mouseX, int mouseY, float partialTicks){
        this.drawDefaultBackground();
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY);
        }

        for (int j = 0; j < this.labelList.size(); ++j)
        {
            ((GuiLabel)this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
        }
    }
}