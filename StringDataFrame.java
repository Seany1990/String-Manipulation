package com.mycompany.stringdata;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

public class StringDataFrame extends JFrame
{
    private static final int F_WIDTH = 1280;
    private static final int F_HEIGHT = 720;
    private final StringDataParser parser;
    private String textFieldText;
    private int comboSelected;
    private String filePath;
    private boolean isFileValid;
    private ArrayList<String> fileLines;
    private GridBagConstraints gbc;
    private JLabel label;
    private JTextField field;
    private JTextArea area;
    private JScrollPane scroll;
    private JComboBox box;
    private JButton button;
    private JButton readButton;
    private JButton saveButton;
    private JCheckBox checkBox;
    
    public StringDataFrame()
    {
        setSize(F_WIDTH, F_HEIGHT);
        parser = new StringDataParser();
        createPanel();
    }
    public final void createPanel()
    {
       JPanel panel = new JPanel();
       panel.setLayout(new GridBagLayout());
       gbc = new GridBagConstraints();
       
       gbc.fill = GridBagConstraints.BOTH;
       panel.add(createTextArea(),gbc);
       gbc.fill = GridBagConstraints.NONE;
       panel.add(createTextFieldPanel(),gbc);       
       panel.add(createDropDown(),gbc);
       panel.add(createButtonPanel(), gbc);
       panel.add(createLabel("No file selected",0,4),gbc);
       add(panel, BorderLayout.NORTH);
    }
    public JTextField createTextField()
    {
        field = new JTextField(30);
        
        class FieldListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                textFieldText = field.getText();
                comboSelected = box.getSelectedIndex();
                performGUIOperations(textFieldText, comboSelected);
            }
        }
        field.addActionListener(new FieldListener());
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridx = 1;
        gbc.gridy = 1;
        return field;        
    }
    public JScrollPane createTextArea()
    {
        area = new JTextArea(30,20);       
        area.setEditable(false);
        scroll = new JScrollPane(area);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return scroll;
    }
    public JComboBox createDropDown()
    {
        box = new JComboBox();
        box.addItem("Reverse String");
        box.addItem("Pig Latin");
        box.addItem("Vowel Count");
        box.addItem("Word Count");
        box.addItem("Palindrome?");
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.gridx=0;
        gbc.gridy=2;
        return box;
    }
    public JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createFileBrowserButton("Choose File", 0, 0));
        buttonPanel.add(createReadFileButton("Read File", 1,0));
        buttonPanel.add(createSaveFileButton("Save File", 2,0));
        buttonPanel.add(createCheckBox("Use Syntax Hightling?", 3, 0));
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 3;
        return buttonPanel;
    }
    public JButton createFileBrowserButton(String name, int x, int y)
    {
        button = new JButton(name);
        isFileValid = false;
        class AddButtonListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String docsPath = System.getProperty("user.home");
                JFileChooser chooser = new JFileChooser(docsPath + "/Documents");
                int returnValue = chooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = chooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    isFileValid = parser.fileCheck(filePath);
                    label.setText(filePath);
                }
            }
        }
        button.addActionListener(new AddButtonListener());
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.gridx=x;
        gbc.gridy=y;
        return button;       
    }
    public JButton createReadFileButton(String name, int x, int y) 
    {
        fileLines = new ArrayList();
        readButton = new JButton(name);

        class AddButtonListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try
                {
                    if (isFileValid)
                    {
                        fileLines = parser.readFile(filePath, checkBox.isSelected());
                        updateTextArea();
                    }
                }
                catch (IOException ioe)
                {
                        
                }
                
            }
        }
        readButton.addActionListener(new AddButtonListener());
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.gridx=x;
        gbc.gridy=y;
        return readButton;       
    }
    public JButton createSaveFileButton(String name, int x, int y)
    {
        saveButton = new JButton(name);
        class AddButtonListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    String docsPath = System.getProperty("user.home");
                    JFileChooser chooser = new JFileChooser(docsPath + "/Documents");                
                    int returnValue = chooser.showSaveDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION)
                    {
                        File selectedFile = chooser.getSelectedFile();
                        filePath = selectedFile.getAbsolutePath();
                        fileLines.clear();
                        for (String line : area.getText().split("\\n"))
                        {
                            fileLines.add(line);
                        }
                        parser.saveFile(fileLines,filePath);
                    }
                }
                catch (IOException ioe)
                {
                            
                }
            }
        }
    
        saveButton.addActionListener(new AddButtonListener());
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.gridx=x;
        gbc.gridy=y;
        return saveButton;       
    }
    public JCheckBox createCheckBox(String title, int x, int y)
    {
        checkBox = new JCheckBox(title);
        checkBox.setSelected(false);
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridx = x;
        gbc.gridy = y;
        return checkBox;
    }
    public JLabel createLabel(String text, int x, int y)
    {
        label = new JLabel(text);
        gbc.gridx=x;
        gbc.gridy=y;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        return label;
    }
    public JPanel createTextFieldPanel()
    {
        JPanel textPanel = new JPanel();
        textPanel.add(createLabel("Enter String (Return to confirm):",0,0));
        textPanel.add(createTextField());
        gbc.gridx=0;
        gbc.gridy=1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        return textPanel;
    }
    public void performGUIOperations(String text, int input)
    {
        parser.setString(text);
        int[] vowelsCount;
        int count;
        boolean isPal;
        String newString = " ";
        switch (input)
        {
            case 0:
                newString = parser.reverseString();
                System.out.println(newString);
                break;
            case 1:
                newString = parser.pigLatin();
                break;
            case 2:
                vowelsCount = parser.countVowels();
                break;
            case 3:
                count = parser.countWords();
                break;
            case 4:
                isPal = parser.palindrome(text);
                System.out.println(isPal);
                break;
            case 5:
                break;
        }
    }
    public void updateTextArea()
    {
        area.setText("");
        area.setEditable(true);
        for (int i = 0; i < fileLines.size(); i++)
        {
            area.append(fileLines.get(i));
        }
    }

}

