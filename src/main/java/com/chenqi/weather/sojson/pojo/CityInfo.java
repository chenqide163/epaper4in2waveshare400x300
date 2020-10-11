package com.chenqi.weather.sojson.pojo;

public class CityInfo {
    private String city;
    private String citykey;
    private String parent;
    private String updateTime;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitykey() {
        return citykey;
    }

    public void setCitykey(String citykey) {
        this.citykey = citykey;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CityInfo{" +
                "city='" + city + '\'' +
                ", citykey='" + citykey + '\'' +
                ", parent='" + parent + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
