package com.haul;

import com.vaadin.ui.Window;


public class EditWindow extends Window {

    /*
    *
    * С помощью этого окна обеспечиваем модальность экранов редактирования
    *
    * */

    public EditWindow() {

        center();
        setClosable(true);
        setModal(true);

    }
}