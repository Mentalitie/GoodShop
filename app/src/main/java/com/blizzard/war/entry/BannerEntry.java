package com.blizzard.war.entry;

/**
 * 功能描述:
 * Banner 模型类
 *
 * @auther: ma
 * @param: BannerEntry
 * @date: 2019/4/17 18:33
 */
public class BannerEntry {
    public String title;
    public String link;
    public String img;

    public BannerEntry(String title, String link, String img) {
        this.title = title;
        this.link = link;
        this.img = img;
    }

}
