package me.yeojoy.microdustwarning.entity;

/**
 * Created by yeojoy on 2014. 6. 12..
 */
public class OttoEventEntity {
    public String rawString;
    public String measureTime;

    public OttoEventEntity(String time, String str) {
        measureTime = time;
        rawString = str;
    }
}
