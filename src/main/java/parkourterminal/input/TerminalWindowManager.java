package parkourterminal.input;

import parkourterminal.global.GlobalConfig;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class TerminalWindowManager {
    private JFrame terminalFrame;
    private boolean isClosing = false;
    private JTextArea outputArea;

    // 窗口管理入口方法
    public void createWindow() {
        if (terminalFrame != null && terminalFrame.isVisible()) {
            terminalFrame.toFront();
            return;
        }
        initializeFrame();
        buildTitleBar();
        buildTerminalComponents();
        setupOpenAnimation();
        terminalFrame.setVisible(true);
    }

    // 初始化框架基础设置
    private void initializeFrame() {
        terminalFrame = new JFrame();
        terminalFrame.setUndecorated(true);
        terminalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        terminalFrame.setLayout(new BorderLayout());
        terminalFrame.setSize(50, 50);
        terminalFrame.setLocationRelativeTo(null);

        try {
            terminalFrame.setOpacity(0f);
        } catch (UnsupportedOperationException e) {
            System.out.println("Transparency not supported");
        }
    }

    // 构建标题栏
    private void buildTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(28, 33, 39));
        titleBar.setPreferredSize(new Dimension(terminalFrame.getWidth(), 30));

        // 标题标签
        JLabel titleLabel = new JLabel("Parkour Terminal");
        titleLabel.setForeground(new Color(201, 209, 217));
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // 关闭按钮
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(new Color(240, 246, 252));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isClosing) startCloseAnimation();
            }
        });

        // 拖动功能实现
        MouseAdapter dragAdapter = new MouseAdapter() {
            private Point dragOffset = new Point();

            public void mousePressed(MouseEvent e) {
                dragOffset.setLocation(e.getPoint());
            }

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                terminalFrame.setLocation(
                        currCoords.x - dragOffset.x,
                        currCoords.y - dragOffset.y
                );
            }
        };

        titleBar.addMouseListener(dragAdapter);
        titleBar.addMouseMotionListener(dragAdapter);

        // 组装标题栏
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);
        terminalFrame.add(titleBar, BorderLayout.NORTH);
    }

    // 构建终端主内容区
    private void buildTerminalComponents() {
        // 输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(28, 33, 39));
        outputArea.setForeground(new Color(201, 209, 217));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 18));

        // 滚动条定制
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(61, 66, 72);
                this.trackColor = new Color(28, 33, 39);
            }

            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }

            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });

        // 输入区域
        final JTextField inputField = new JTextField();
        inputField.setBackground(new Color(38, 43, 49));
        inputField.setForeground(new Color(240, 246, 252));
        inputField.setCaretColor(new Color(240, 246, 252));
        inputField.setFont(new Font("Consolas", Font.PLAIN, 18));
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processCommand(inputField);
            }
        });

        // 添加组件
        terminalFrame.add(scrollPane, BorderLayout.CENTER);
        terminalFrame.add(inputField, BorderLayout.SOUTH);
    }

    // 命令处理逻辑
    private void processCommand(JTextField inputField) {
        String input = inputField.getText();
        inputField.setText("");
        outputArea.append("> " + input + "\n");

        String response = TerminalCommandHandler.handleCommand(input);
        if (response.isEmpty()) {
            outputArea.setText("");
            return;
        }

        if (GlobalConfig.animation) {
            animateTextOutput(response + "\n");
        } else {
            outputArea.append(response + "\n");
        }
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    // 文本渐显动画
    private void animateTextOutput(final String text) {
        final Timer timer = new Timer(10, null);
        final int[] index = {0};

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index[0] < text.length()) {
                    outputArea.append(String.valueOf(text.charAt(index[0])));
                    outputArea.setCaretPosition(outputArea.getDocument().getLength());
                    index[0]++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    // 窗口打开动画
    private void setupOpenAnimation() {
        final int targetWidth = 1024;
        final int targetHeight = 768;
        final Timer timer = new Timer(10, null);
        final int[] step = {0};
        final int totalSteps = 20;

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (step[0] <= totalSteps) {
                    float progress = (float) step[0] / totalSteps;
                    float eased = progress * progress;

                    int width = 50 + (int)((targetWidth - 50) * eased);
                    int height = 50 + (int)((targetHeight - 50) * eased);

                    terminalFrame.setSize(width, height);
                    terminalFrame.setLocationRelativeTo(null);

                    try {
                        terminalFrame.setOpacity(eased);
                    } catch (UnsupportedOperationException ex) {
                        // 忽略不支持透明的情况
                    }

                    step[0]++;
                } else {
                    terminalFrame.setSize(targetWidth, targetHeight);
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    // 窗口关闭动画
    private void startCloseAnimation() {
        isClosing = true;
        final int initialWidth = terminalFrame.getWidth();
        final int initialHeight = terminalFrame.getHeight();
        final Timer timer = new Timer(10, null);
        final int[] step = {0};
        final int totalSteps = 20;

        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (step[0] <= totalSteps) {
                    float progress = (float) step[0] / totalSteps;
                    float eased = 1 - (1 - progress) * (1 - progress);

                    int width = initialWidth - (int)((initialWidth - 50) * eased);
                    int height = initialHeight - (int)((initialHeight - 50) * eased);

                    terminalFrame.setSize(width, height);
                    terminalFrame.setLocationRelativeTo(null);

                    try {
                        terminalFrame.setOpacity(1 - eased);
                    } catch (UnsupportedOperationException ex) {
                    }

                    step[0]++;
                } else {
                    terminalFrame.dispose();
                    isClosing = false;
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    public void focusWindow() {
        if (terminalFrame != null) terminalFrame.toFront();
    }

    public boolean isWindowVisible() {
        return terminalFrame != null && terminalFrame.isVisible();
    }
}