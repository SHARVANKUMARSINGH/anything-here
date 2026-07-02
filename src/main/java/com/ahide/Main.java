package com.ahide;

import com.formdev.flatlaf.FlatDarkLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {

    public Main() {
        // Main Window Setup
        setTitle("ANYTHING HERE (AH) - V0.001 PLAYTEST");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Use a JSplitPane to divide the window (Explorer on left, Editor on right)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250); // Explorer width

        // 1. Setup File Explorer (Dummy data for V0.001)
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("AH Workspace");
        root.add(new DefaultMutableTreeNode("Main.java"));
        root.add(new DefaultMutableTreeNode("Engine.java"));
        root.add(new DefaultMutableTreeNode("config.xml"));
        
        JTree fileExplorer = new JTree(root);
        JScrollPane explorerScroll = new JScrollPane(fileExplorer);
        
        // 2. Setup the Powerful Editor
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        
        // Default text for the playtest
        textArea.setText("public class Main {\n" +
                         "    public static void main(String[] args) {\n" +
                         "        System.out.println(\"Hello from ANYTHING HERE (AH)!\");\n" +
                         "    }\n" +
                         "}");

        // Apply a dark theme to the text area
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream(
                    "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(textArea);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Add line numbers and scrolling to the editor
        RTextScrollPane editorScroll = new RTextScrollPane(textArea);

        // Assemble the UI
        splitPane.setLeftComponent(explorerScroll);
        splitPane.setRightComponent(editorScroll);

        add(splitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Setup the modern dark theme before starting the UI
        FlatDarkLaf.setup();

        // Launch the IDE
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}