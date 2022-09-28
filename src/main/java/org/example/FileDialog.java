package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Objects;


public class FileDialog extends JFrame {

    private String fileString = "";

    private LinkedList statements = new LinkedList();
    private JButton bDateioeffnen = new JButton();
    private JButton bStringwandeln = new JButton();

    public FileDialog() {
        super();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        int frameWidth = 600;
        int frameHeight = 200;
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setTitle("FileDialogBeispiel");
        setResizable(false);
        Container cp = getContentPane();
        cp.setLayout(null);

        bDateioeffnen.setBounds(25, 25, 150, 50);
        bDateioeffnen.setText("Datei öffnen");
        bDateioeffnen.setMargin(new Insets(2, 2, 2, 2));
        bDateioeffnen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bDateioeffnen_ActionPerformed(evt);
            }
        });

        bStringwandeln.setBounds(200, 25, 150, 50);
        bStringwandeln.setText("String wandeln");
        bStringwandeln.setMargin(new Insets(2, 2, 2, 2));
        bStringwandeln.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bStringwandeln_ActionPerformed(evt);
            }
        });
        cp.add(bDateioeffnen);
        cp.add(bStringwandeln);

        setVisible(true);
    }

    public static void main(String[] args) {
        new FileDialog();
    } // end of main

    public void bDateioeffnen_ActionPerformed(ActionEvent evt) {
        java.awt.FileDialog fd = new java.awt.FileDialog(this, "Datei wählen", java.awt.FileDialog.LOAD);
        fd.setDirectory("C:\\");
        fd.setFile("*.txt");
        fd.setVisible(true);
        String filename = fd.getDirectory() + fd.getFile();
        if (filename == null)
            System.out.println("You cancelled the choice");
        else {
            System.out.println("You chose " + filename);
            try {
                File fil = new File(filename);
                BufferedReader br = new BufferedReader(new FileReader(fil));
                System.out.println("file content: ");
                int r = 0;
                fileString = "";
                while ((r = br.read()) != -1) {
                    fileString += ((char) r);
                }
                System.out.println(fileString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void bStringwandeln_ActionPerformed(ActionEvent evt) {
        fileString = sanitize(fileString);
        ;
        String[] stringByLine = fileString.split("\\n");
        for (String line : stringByLine) {
            if (Objects.equals(line, stringByLine[0])) {
                setDatabaseName(line);
                continue;
            }
            setTableStructure(line);
        }
        System.out.println(statements);
    }

    private void setDatabaseName(String databaseName) {
        statements.add("CREATE DATABASE IF EXISTS " + databaseName + ";");
        statements.add("USE DATABASE " + databaseName + ";");

    }

    private void setTableStructure(String tableStatement) {
        String[] twoPartString = tableStatement.split("\\(");

        String[] attributes = twoPartString[1].split(",");

        for (String attribute : attributes
             ) {
            if(attribute.contains("PK#")) {
                attribute = handleManyToMany(attribute);
                System.out.println("M2M: " + attribute);
                continue;
            }
            if(attribute.contains("PK")) {
                attribute = handlePrimaryKeyFrom(attribute);
                System.out.println("P-Set:" + attribute);
                continue;
            }
            if(attribute.contains("#")) {
                attribute = handleForeignKeyFrom(attribute);
                System.out.println(attribute);
                continue;
            }
            handleAttribute(attribute);

        }
        statements.add("CREATE TABLE " + twoPartString[0] + "( " + attributes + ")");
    }

    private String handleAttribute(String attribute) {
        return attribute + " TEXT, ";
    }

    private String handleForeignKeyFrom(String attribute) {
        attribute = attribute.replace("#", "");
        return attribute + " INT, FOREIGN KEY('" + attribute + "'), ";
    }

    private String handlePrimaryKeyFrom(String attribute ) {
        attribute = attribute.replace("PK", "");

        return attribute + " INT NOT NULL AUTO_INCREMENT, PRIMARY KEY('" + attribute + "'), ";
    }
    private String handleManyToMany(String attribute ) {
        attribute = attribute.replace("PK#", "");

        return attribute + " INT NOT NULL, PRIMARY KEY('" + attribute + "'), FOREIGN KEY('" + attribute + "') ";
    }
    private String sanitize(String string ) {
        return string.replace(" ", "").replace("\r", "").replace(")", "");
    }
}