package me.yeojoy.microdustwarning.entity;

import java.util.List;

/**
 * Created by yeojoy on 15. 3. 19..
 */
public class DtoList {

    private List<AllStateDustInfoDto> list;

    private AllStateDustInfoDto parm;

    private AllStateDustInfoDto ArpltnInforInqireSvcVo;

    private int totalCount;

    public List<AllStateDustInfoDto> getList() {
        return list;
    }

    public void setList(List<AllStateDustInfoDto> list) {
        this.list = list;
    }

    public AllStateDustInfoDto getParm() {
        return parm;
    }

    public void setParm(AllStateDustInfoDto parm) {
        this.parm = parm;
    }

    public AllStateDustInfoDto getArpltnInforInqireSvcVo() {
        return ArpltnInforInqireSvcVo;
    }

    public void setArpltnInforInqireSvcVo(AllStateDustInfoDto arpltnInforInqireSvcVo) {
        ArpltnInforInqireSvcVo = arpltnInforInqireSvcVo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
