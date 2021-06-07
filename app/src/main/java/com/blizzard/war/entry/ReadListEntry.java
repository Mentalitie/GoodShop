package com.blizzard.war.entry;

import java.util.ArrayList;
import java.util.List;

public class ReadListEntry {
    private List<ReadEntry> list = new ArrayList<>();


    public List<ReadEntry> getList() {
        return list;
    }

    public void setList(List<ReadEntry> list) {
        this.list = list;
    }

    public void add(ReadEntry readEntry) {
        list.add(readEntry);
    }
}
