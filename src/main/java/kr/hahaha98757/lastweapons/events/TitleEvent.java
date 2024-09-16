package kr.hahaha98757.lastweapons.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class TitleEvent extends Event {
    private final String title;

    public TitleEvent(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}