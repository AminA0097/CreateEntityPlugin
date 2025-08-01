package com.plugin.Ui;

import com.intellij.openapi.ui.DialogWrapper;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

import javax.swing.*;
import java.awt.*;

public class CreateEntityUi extends DialogWrapper {

    private JTextField entityNameField;
    private JCheckBox inheritCheckbox;

    public CreateEntityUi() {
        super(true);
        init();
        setTitle("Create Entity");
    }
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        entityNameField = new JTextField();
        inheritCheckbox = new JCheckBox("Inherit from BaseEntity");
        panel.add(new JLabel("Enter entity name:"));
        panel.add(entityNameField);
        panel.add(inheritCheckbox);

        return panel;
    }
    public String getEntityName() {
        return entityNameField.getText().trim();
    }

    public boolean isInheritChecked() {
        return inheritCheckbox.isSelected();
    }
}
