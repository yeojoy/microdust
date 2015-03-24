package me.yeojoy.microdustwarning.entity;

import java.util.Comparator;

/**
 * Locality로 오름차순으로 정렬
 * Created by yeojoy on 14. 12. 23..
 */
public class AllStateDustInfoDtoLocalityAscComparer implements Comparator<AllStateDustInfoDto> {
    @Override
    public int compare(AllStateDustInfoDto lhs, AllStateDustInfoDto rhs) {
        return lhs.getStationName().compareTo(rhs.getStationName());
    }
}
