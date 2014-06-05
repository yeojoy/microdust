package me.yeojoy.microdustwarning.util;

import me.yeojoy.microdustwarning.DustConstants;
import android.content.Context;
import android.text.TextUtils;

public class DustUtils implements DustConstants {
    
    public static void sendNotification(Context context) {
        
    }
    
    
    public enum STATUS {
        GOOD, NORMAL, BAD, WORSE, WORST, NONE
    };
    
    /**
     * 받은 data로 미세먼지 확인 후 Notification으로 알려준다.
     * @param data
     */
    public static STATUS[] analyzeMicroDust(String data) {
        String[] array = data.split(" ");
                
        STATUS[] status = new STATUS[6];
        status[0] = getMicroDustDegree("");
        status[1] = getOzonDegree("");
        status[2] = getNO2Degree("");
        status[3] = getCODegree("");
        status[4] = getSO2Degree("");
        status[5] = getTotalDegree("");
        
        return status;
    }
    
    /**
     * 미세먼지 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getMicroDustDegree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float microDustValue = Float.parseFloat(value);
        
        if (microDustValue > MICRO_DUST_WORSE)
            return STATUS.WORST;
        else if (microDustValue > MICRO_DUST_BAD)
            return STATUS.WORSE;
        else if (microDustValue > MICRO_DUST_NORMAL)
            return STATUS.BAD;
        else if (microDustValue > MICRO_DUST_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 오존 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getOzonDegree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float ozonValue = Float.parseFloat(value);
        
        if (ozonValue > O3_WORSE)
            return STATUS.WORST;
        else if (ozonValue > O3_BAD)
            return STATUS.WORSE;
        else if (ozonValue > O3_NORMAL)
            return STATUS.BAD;
        else if (ozonValue > O3_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 이산화질소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getNO2Degree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float no2Value = Float.parseFloat(value);
        
        if (no2Value > NO2_WORSE)
            return STATUS.WORST;
        else if (no2Value > NO2_BAD)
            return STATUS.WORSE;
        else if (no2Value > NO2_NORMAL)
            return STATUS.BAD;
        else if (no2Value > NO2_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 일산화탄소 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getCODegree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float coValue = Float.parseFloat(value);
        
        if (coValue > CO_WORSE)
            return STATUS.WORST;
        else if (coValue > CO_BAD)
            return STATUS.WORSE;
        else if (coValue > CO_NORMAL)
            return STATUS.BAD;
        else if (coValue > CO_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 아황산가스 수치가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getSO2Degree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float so2Value = Float.parseFloat(value);
        
        if (so2Value > SO2_WORSE)
            return STATUS.WORST;
        else if (so2Value > SO2_BAD)
            return STATUS.WORSE;
        else if (so2Value > SO2_NORMAL)
            return STATUS.BAD;
        else if (so2Value > SO2_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
    
    /**
     * 통합대기지수가 괜찮은지
     * @param value
     * @return
     */
    private static STATUS getTotalDegree(String value) {
        if (TextUtils.isEmpty(value)) return STATUS.NONE;
        
        float totalValue = Float.parseFloat(value);
        
        if (totalValue > TOTAL_DEGREE_WORSE)
            return STATUS.WORST;
        else if (totalValue > TOTAL_DEGREE_BAD)
            return STATUS.WORSE;
        else if (totalValue > TOTAL_DEGREE_NORMAL)
            return STATUS.BAD;
        else if (totalValue > TOTAL_DEGREE_GOOD)
            return STATUS.NORMAL;
        
        return STATUS.GOOD;
    }
}
