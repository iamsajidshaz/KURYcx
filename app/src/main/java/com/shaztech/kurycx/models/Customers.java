package com.shaztech.kurycx.models;

public class Customers {

    private String name, phone, is_draw, draw_week, address, guid;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Customers() {
    }


        public Customers(String name, String phone, String is_draw, String draw_week, String address, String guid) {
        this.name = name;
        this.phone = phone;
        this.is_draw = is_draw;
        this.draw_week = draw_week;
        this.address = address;
        this.guid=guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_draw() {
        return is_draw;
    }

    public void setIs_draw(String is_draw) {
        this.is_draw = is_draw;
    }

    public String getDraw_week() {
        return draw_week;
    }

    public void setDraw_week(String draw_month) {
        this.draw_week = draw_month;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
