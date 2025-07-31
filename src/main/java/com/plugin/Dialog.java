package com.plugin;

import com.github.weisj.jsvg.D;
import com.intellij.openapi.ui.DialogWrapper;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

import javax.swing.*;

public class Dialog extends DialogWrapper {
    public Dialog(){
        super(true);
        init();
        setTitle("Creating Entity!");

    }
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return null;
    }
}
