package com.hashibutogarasu.screenshottweet.Custom;

import io.github.cottonmc.cotton.gui.widget.WTextField;

public class CustomTextField extends WTextField {
    public void clear(){
        for(int c = this.getText().length(); c>=0; c--){
            this.setText(this.getText().substring(0,c));
        }
    }
}
