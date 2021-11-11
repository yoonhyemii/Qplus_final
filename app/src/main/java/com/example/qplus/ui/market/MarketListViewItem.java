package com.example.qplus.ui.market;

import android.widget.ImageView;

public class MarketListViewItem {
    private String iconDrawable;
    private String titleStr;
    private String descStr;

    public void setIcon(String icon) { iconDrawable = icon; }

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setDesc(String desc) {
        descStr = desc;
    }

    public String getIcon() { return this.iconDrawable; }

    public String getTitle() {
        return this.titleStr;
    }

    public String getDesc() {
        return this.descStr;
    }
}