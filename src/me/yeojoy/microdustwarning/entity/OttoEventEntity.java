package me.yeojoy.microdustwarning.entity;

/**
 * Created by yeojoy on 2014. 6. 12..
 */
public class OttoEventEntity {
    public String rawString;
    public String measureTime;
    public COMMAND command;
    public boolean on_off;

    public enum COMMAND {
        GET_DATA, REFRESH, ON_OFF
    }

    public OttoEventEntity(COMMAND c) {
        command = c;
    }

    public void setData(String time, String str) {
        measureTime = time;
        rawString = str;
    }

    public void setBoolean(boolean on_off) {
        this.on_off = on_off;
    }
}
