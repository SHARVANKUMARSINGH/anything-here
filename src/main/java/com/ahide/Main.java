package com.ahide;

import com.formdev.flatlaf.FlatDarkLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {

    public Main() {
        // Main Window Setup
        setTitle("ANYTHING HERE (AH) - V0.002 PLAYTEST");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // VERTICAL split: Explorer on top, Editor on the bottom
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300); // Height of the top explorer area

        // --- 1. SETUP FILE EXPLORER & TOOLBAR ---
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Setup Toolbar with Icons
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton addFolderBtn = new JButton("📁 Add Sub-Folder");
        JButton addFileBtn = new JButton("📄 Add File");
        toolBar.add(addFolderBtn);
        toolBar.add(addFileBtn);

        // Setup Tree Model (Allows us to dynamically add/remove items)
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("AH Workspace");
        root.setAllowsChildren(true);
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        
        JTree fileExplorer = new JTree(treeModel);
        fileExplorer.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane explorerScroll = new JScrollPane(fileExplorer);

        topPanel.add(toolBar, BorderLayout.NORTH);
        topPanel.add(explorerScroll, BorderLayout.CENTER);

        // --- 2. ADD BUTTON LOGIC ---
        
        // Add Sub-Folder Logic
        addFolderBtn.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileExplorer.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getAllowsChildren()) {
                String folderName = JOptionPane.showInputDialog(this, "Enter folder name:", "New Folder", JOptionPane.PLAIN_MESSAGE);
                if (folderName != null && !folderName.trim().isEmpty()) {
                    DefaultMutableTreeNode newFolder = new DefaultMutableTreeNode(folderName);
                    newFolder.setAllowsChildren(true); // Tell the tree this is a folder
                    treeModel.insertNodeInto(newFolder, selectedNode, selectedNode.getChildCount());
                    fileExplorer.expandPath(new TreePath(selectedNode.getPath()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a folder to add a sub-folder into.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add File Logic
        addFileBtn.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileExplorer.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getAllowsChildren()) {
                String fileName = JOptionPane.showInputDialog(this, "Enter file name (e.g., Main.java):", "New File", JOptionPane.PLAIN_MESSAGE);
                if (fileName != null && !fileName.trim().isEmpty()) {
                    DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(fileName);
                    newFile.setAllowsChildren(false); // Tell the tree this is a file (will get file icon)
                    treeModel.insertNodeInto(newFile, selectedNode, selectedNode.getChildCount());
                    fileExplorer.expandPath(new TreePath(selectedNode.getPath()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a folder to add the file into.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- 3. SETUP THE EDITOR ---
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        
        textArea.setText("public class Main {\n" +
                         "    public static void main(String[] args) {\n" +
                         "        System.out.println(\"Hello from ANYTHING HERE (AH)!\");\n" +
                         "    }\n" +
                         "}");

        // Apply dark theme
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(textArea);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        RTextScrollPane editorScroll = new RTextScrollPane(textArea);

        // --- Assemble the UI ---
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(editorScroll);

        add(splitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}