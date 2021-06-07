package com.blizzard.war.entry;

import java.util.ArrayList;
import java.util.List;

public class AudioListEntry {
    private List<AudioEntry> list = new ArrayList<>();


    public List<AudioEntry> getList() {
        return list;
    }

    public void setList(List<AudioEntry> list) {
        this.list = list;
    }

    public void add(AudioEntry audioEntry) {
        list.add(audioEntry);
    }
}
