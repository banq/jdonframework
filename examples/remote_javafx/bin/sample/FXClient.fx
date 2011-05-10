package sample;

import javafx.ui.*;
import java.lang.System;

class Hello {
    attribute name:String;
    attribute str:String;
}

var helloModel = new Hello();

HelloworldClient.setServerUrl(ARGUMENTS:String);

Frame {
    width: 300
    height: 200
    title: "Jdon Helloworld Sample"
    centerOnScreen: true
    onClose: operation(){ System.exit(0);}
    content: GridPanel {
        border: EmptyBorder {
            top: 30
            left: 30
            bottom: 30
            right: 30
        }
        rows: 3
        columns: 1
        vgap: 10
        cells:
            [TextField {
            value: bind helloModel.name
        },
        Button {
            text: "Say Hello!"
            action: operation() {
                helloModel.str = HelloworldClient.CLIENT.hello(helloModel.name);
            }
        },
        Label {
            text: bind "Server says: {helloModel.str}"
        }]
    }
    visible: true
};