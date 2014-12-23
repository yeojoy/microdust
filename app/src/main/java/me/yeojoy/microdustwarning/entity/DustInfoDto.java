package me.yeojoy.microdustwarning.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeojoy on 14. 12. 18..
 */
public class DustInfoDto implements Parcelable {

    public DustInfoDto() { }

    public DustInfoDto(Parcel in) {
        readFromParcel(in);
    }

    public DustInfoDto(String date, String locality, String pm10,
               String pm10Index, String pm24, String pm24Index, String pm25,
               String pm25Index, String ozone, String ozoneIndex,
               String nitrogen, String nitrogenIndex, String carbon,
               String carbonIndex, String sulfurous, String sulfurousIndex,
               String maxIndex, String material, String degree) {
        this.date = date;
        this.locality = locality;
        this.pm10 = pm10;
        this.pm10Index = pm10Index;
        this.pm24 = pm24;
        this.pm24Index = pm24Index;
        this.pm25 = pm25;
        this.pm25Index = pm25Index;
        this.ozone = ozone;
        this.ozoneIndex = ozoneIndex;
        this.nitrogen = nitrogen;
        this.nitrogenIndex = nitrogenIndex;
        this.carbon = carbon;
        this.carbonIndex = carbonIndex;
        this.sulfurous = sulfurous;
        this.sulfurousIndex = sulfurousIndex;
        this.maxIndex = maxIndex;
        this.material = material;
        this.degree = degree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(String maxIndex) {
        this.maxIndex = maxIndex;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm10Index() {
        return pm10Index;
    }

    public void setPm10Index(String pm10Index) {
        this.pm10Index = pm10Index;
    }

    public String getPm24() { return pm24; }

    public void setPm24(String pm24) { this.pm24 = pm24; }

    public String getPm24Index() {
        return pm24Index;
    }

    public void setPm24Index(String pm24Index) {
        this.pm24Index = pm24Index;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm25Index() {
        return pm25Index;
    }

    public void setPm25Index(String pm25Index) {
        this.pm25Index = pm25Index;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public String getOzoneIndex() {
        return ozoneIndex;
    }

    public void setOzoneIndex(String ozoneIndex) {
        this.ozoneIndex = ozoneIndex;
    }

    public String getNitrogen() {
        return nitrogen;
    }

    public void setNitrogen(String nitrogen) {
        this.nitrogen = nitrogen;
    }

    public String getNitrogenIndex() {
        return nitrogenIndex;
    }

    public void setNitrogenIndex(String nitrogenIndex) {
        this.nitrogenIndex = nitrogenIndex;
    }

    public String getCarbon() {
        return carbon;
    }

    public void setCarbon(String carbon) {
        this.carbon = carbon;
    }

    public String getCarbonIndex() {
        return carbonIndex;
    }

    public void setCarbonIndex(String carbonIndex) {
        this.carbonIndex = carbonIndex;
    }

    public String getSulfurous() {
        return sulfurous;
    }

    public void setSulfurous(String sulfurous) {
        this.sulfurous = sulfurous;
    }

    public String getSulfurousIndex() {
        return sulfurousIndex;
    }

    public void setSulfurousIndex(String sulfurousIndex) {
        this.sulfurousIndex = sulfurousIndex;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
    
    /** 측정 날짜 시각 */
    private String date;
    /** 구역. xx구 */
    private String locality;
    /** 미세먼지 */
    private String pm10;
    private String pm10Index;
    /** ??? 뭘까? */
    private String pm24;
    private String pm24Index;
    /** 초미세 먼지 */
    private String pm25;
    private String pm25Index;
    /** 오존 */
    private String ozone;
    private String ozoneIndex;
    /** 이산화 질소 */
    private String nitrogen;
    private String nitrogenIndex;
    /** 일산화탄소 */
    private String carbon;
    private String carbonIndex;
    /** 아황산가스 */
    private String sulfurous;
    private String sulfurousIndex;
    /** index로 환산한 것 중 가장 높은 index, 통합지수 */
    private String maxIndex;
    /** 지수 결정 물질 */
    private String material;
    /** 등급 */
    private String degree;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(locality);
        dest.writeString(pm10);
        dest.writeString(pm10Index);
        dest.writeString(pm24);
        dest.writeString(pm24Index);
        dest.writeString(pm25);
        dest.writeString(pm25Index);
        dest.writeString(ozone);
        dest.writeString(ozoneIndex);
        dest.writeString(nitrogen);
        dest.writeString(nitrogenIndex);
        dest.writeString(carbon);
        dest.writeString(carbonIndex);
        dest.writeString(sulfurous);
        dest.writeString(sulfurousIndex);
        dest.writeString(maxIndex);
        dest.writeString(material);
        dest.writeString(degree);
    }

    private void readFromParcel(Parcel in) {
        date = in.readString();
        locality = in.readString();
        pm10 = in.readString();
        pm10Index = in.readString();
        pm24 = in.readString();
        pm24Index = in.readString();
        pm25 = in.readString();
        pm25Index = in.readString();
        ozone = in.readString();
        ozoneIndex = in.readString();
        nitrogen = in.readString();
        nitrogenIndex = in.readString();
        carbon = in.readString();
        carbonIndex = in.readString();
        sulfurous = in.readString();
        sulfurousIndex = in.readString();
        maxIndex = in.readString();
        material = in.readString();
        degree = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DustInfoDto createFromParcel(Parcel in) {
            return new DustInfoDto(in);
        }

        public DustInfoDto[] newArray(int size) {
            return new DustInfoDto[size];
        }
    };

    @Override
    public String toString() {
        return "DustInfoDto{" +
                "date='" + date + '\'' +
                ", locality='" + locality + '\'' +
                ", pm10='" + pm10 + '\'' +
                ", pm10Index='" + pm10Index + '\'' +
                ", pm24='" + pm24 + '\'' +
                ", pm24Index='" + pm24Index + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", pm25Index='" + pm25Index + '\'' +
                ", ozone='" + ozone + '\'' +
                ", ozoneIndex='" + ozoneIndex + '\'' +
                ", nitrogen='" + nitrogen + '\'' +
                ", nitrogenIndex='" + nitrogenIndex + '\'' +
                ", carbon='" + carbon + '\'' +
                ", carbonIndex='" + carbonIndex + '\'' +
                ", sulfurous='" + sulfurous + '\'' +
                ", sulfurousIndex='" + sulfurousIndex + '\'' +
                ", maxIndex='" + maxIndex + '\'' +
                ", material='" + material + '\'' +
                ", degree='" + degree + '\'' +
                '}';
    }
}
