package com.example.qplus;

import android.graphics.drawable.Drawable;

public class CouponListViewItem {
    private String couponSectionStr;
    private String couponNameStr;
    private String couponDateStr;


    public void setCouponSection(String couponSection) { couponSectionStr = couponSection; }

    public void setCouponName(String couponName) {
        couponNameStr = couponName;
    }

    public void setCouponDate(String couponDate) {
        couponDateStr = couponDate;
    }

    public String getCouponSection() {
        return this.couponSectionStr;
    }

    public String getCouponName() {
        return this.couponNameStr;
    }

    public String getCouponDate() {
        return this.couponDateStr;
    }
}
