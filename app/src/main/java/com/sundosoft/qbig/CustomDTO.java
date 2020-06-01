package com.sundosoft.qbig;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class CustomDTO {
    private int resId;
    private String title;
    private String content;
    private String date;
    private String name;
    private String qnum;
    private Bitmap img;
    private String bogi1, bogi2, bogi3, bogi4;
    private int ck1, ck2, ck3, ck4;
    private String isStudy;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCk1() {
        return ck1;
    }

    public void setCk1(int ck1) {
        this.ck1 = ck1;
    }

    public int getCk2() {
        return ck2;
    }

    public void setCk2(int ck2) {
        this.ck2 = ck2;
    }

    public int getCk3() {
        return ck3;
    }

    public void setCk3(int ck3) {
        this.ck3 = ck3;
    }

    public int getCk4() {
        return ck4;
    }

    public void setCk4(int ck4) {
        this.ck4 = ck4;
    }

    public String getQnum() {
        return qnum;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setQnum(String qnum) {
        this.qnum = qnum;
    }

    public String getBogi1() {
        return bogi1;
    }

    public void setBogi1(String bogi1) {
        this.bogi1 = bogi1;
    }

    public String getBogi2() {
        return bogi2;
    }

    public void setBogi2(String bogi2) {
        this.bogi2 = bogi2;
    }

    public String getBogi3() {
        return bogi3;
    }

    public void setBogi3(String bogi3) {
        this.bogi3 = bogi3;
    }

    public String getBogi4() {
        return bogi4;
    }

    public void setBogi4(String bogi4) {
        this.bogi4 = bogi4;
    }

    public String getIsStudy() {
        return isStudy;
    }

    public void setIsStudy(String isStudy) {
        this.isStudy = isStudy;
    }
}
