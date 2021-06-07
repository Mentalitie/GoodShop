package com.blizzard.war.service;

import java.util.List;

public class MessageWrap {

    public String message;
    public int status;
    public List list;

    public MessageWrap(String message) {
        this.message = message;
    }

    public MessageWrap(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public MessageWrap(List list, int status) {
        this.list = list;
        this.status = status;
    }


//    public MessageWrap(List<AudioEntry> musicList) {
//        this.musicList = musicList;
//    }

//    public final int status;
//    public final List<ReadEntry> readList;

//
//    public MessageWrap(String message) {
//        this.message = message;
//        this.status = 0;
//    }
//
//    public MessageWrap(String message, int status) {
//        this.message = message;
//        this.status = status;
//    }
//
////    public MessageWrap(List<ReadEntry> readList) {
////        this.message = "";
////        this.status = 0;
////        this.readList = readList;
////    }
//
//    public MessageWrap(String message, int status, List<ReadEntry> readList) {
//        this.message = message;
//        this.status = status;
////        this.readList = readList;
//    }
}
