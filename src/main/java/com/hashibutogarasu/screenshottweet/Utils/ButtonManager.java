package com.Hashibutogarasu.screenshottweet.Utils;

import io.github.cottonmc.cotton.gui.widget.WButton;

import java.util.ArrayList;

public class ButtonManager {
    public ArrayList<WButton> ButtonLists;

    public ButtonManager(){
        ButtonLists = new ArrayList<>();
    }

    public void add(WButton button){
        ButtonLists.add(button);
    }

    public void setEnabled(boolean param){
        for (WButton button:ButtonLists) {
            button.setEnabled(param);
        }
    }
    public WButton[] getEnabled(){
        WButton[] buttons = new WButton[]{};

        for (WButton button:ButtonLists) {
            if(button.isEnabled()){
                buttons[(int) ButtonLists.stream().count()] = button;
            }
        }

        return buttons;
    }
}
