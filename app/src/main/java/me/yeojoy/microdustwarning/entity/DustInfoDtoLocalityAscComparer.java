package me.yeojoy.microdustwarning.entity;

import java.util.Comparator;

/**
 * Locality로 오름차순으로 정렬
 * Created by yeojoy on 14. 12. 23..
 */
public class DustInfoDtoLocalityAscComparer implements Comparator<DustInfoDto> {
    @Override
    public int compare(DustInfoDto lhs, DustInfoDto rhs) {
        return lhs.getLocality().compareTo(rhs.getLocality());
    }
}
