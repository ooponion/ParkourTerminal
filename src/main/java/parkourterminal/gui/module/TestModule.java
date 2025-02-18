package parkourterminal.gui.module;

import parkourterminal.gui.component.ModDetailGui;
import parkourterminal.gui.component.ToggleSwitch;
import parkourterminal.gui.layout.LayoutDirection;
import parkourterminal.gui.layout.LinearLayout;

import java.io.IOException;

public class TestModule extends ModDetailGui {

    private ToggleSwitch toggleSwitch;
    private ToggleSwitch toggleSwitch2;
    private ToggleSwitch toggleSwitch3;
    private ToggleSwitch toggleSwitch4;
    private ToggleSwitch toggleSwitch5;
    private ToggleSwitch toggleSwitch6;
    private ToggleSwitch toggleSwitch7;
    private ToggleSwitch toggleSwitch8;
    private ToggleSwitch toggleSwitch9;
    private ToggleSwitch toggleSwitch10;

    public TestModule() {
        super("Test");
        // 初始化 ToggleSwitch，例如宽 15，高 6，右侧显示 "Toggle" 标签
        toggleSwitch = new ToggleSwitch(15, 6, "Toggle");
        // 将 toggleSwitch 添加到 detailContainer 中
        detailContainer.addComponent(toggleSwitch);

        toggleSwitch2 = new ToggleSwitch(15, 6, "Toggle 2");
        detailContainer.addComponent(toggleSwitch2);

        toggleSwitch3 = new ToggleSwitch(15, 6, "Toggle 3");
        toggleSwitch4 = new ToggleSwitch(15, 6, "Toggle 4");
        toggleSwitch5 = new ToggleSwitch(15, 6, "Toggle 5");
        toggleSwitch6 = new ToggleSwitch(15, 6, "Toggle 6");
        toggleSwitch7 = new ToggleSwitch(15, 6, "Toggle 7");
        toggleSwitch8 = new ToggleSwitch(15, 6, "Toggle 8");
        toggleSwitch9 = new ToggleSwitch(15, 6, "Toggle 9");
        toggleSwitch10 = new ToggleSwitch(15, 6, "Toggle X");

        detailContainer.addComponent(toggleSwitch3);
        detailContainer.addComponent(toggleSwitch4);
        detailContainer.addComponent(toggleSwitch5);
        detailContainer.addComponent(toggleSwitch6);
        detailContainer.addComponent(toggleSwitch7);
        detailContainer.addComponent(toggleSwitch8);
        detailContainer.addComponent(toggleSwitch9);
        detailContainer.addComponent(toggleSwitch10);


        // 设置 detailContainer 的布局管理器，使用水平布局并设置间距（例如 5 像素）
        detailContainer.setLayoutManager(new LinearLayout(LayoutDirection.HORIZONTAL, 5));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks, int width, int height) {
        // 调用父类方法，更新 detailContainer 的位置与尺寸，并绘制退出按钮等
        super.drawScreen(mouseX, mouseY, partialTicks, width, height);
        // 自动排版 detailContainer 内的组件，无需手动设置组件位置
        detailContainer.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (super.mouseClicked(mouseX, mouseY, mouseButton))
            return true;

        // 检测鼠标点击是否作用于 toggleSwitch
        if (toggleSwitch.isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            toggleSwitch.toggle();
        }
        return false;
    }
}
