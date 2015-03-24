package me.yeojoy.microdustwarning.entity;

import java.util.List;

/**
 * Created by yeojoy on 2014. 6. 12..
 */
public class OttoEventEntity {
    public DtoList dtoList;
    public COMMAND command;
    public boolean on_off;

    public enum COMMAND {
        GET_DATA, GET_DATA_WITH_DTO, REFRESH, ON_OFF
    }

    public OttoEventEntity(COMMAND c) {
        command = c;
    }

    public void setData(DtoList dtoList) { this.dtoList = dtoList; }

    public void setBoolean(boolean on_off) {
        this.on_off = on_off;
    }
}
