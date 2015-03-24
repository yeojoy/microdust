package me.yeojoy.microdustwarning.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yeojoy on 15. 3. 19..
 */
public class AllStateDustInfoDto implements Parcelable {
    private String _returnType;
    private String coGrade;
    private String no2Grade;
    private String o3Grade;
    private String pm10Grade;
    private String so2Grade;
    private String khaiGrade;
    private String coValue;
    private String no2Value;
    private String o3Value;
    private String pm10Value;
    private String so2Value;
    private String khaiValue;
    private String numOfRows;
    private String rnum;
    private String resultCode;
    private String resultMsg;
    private String stationCode;
    private String serviceKey;
    private String pageNo;
    private String dataTerm;
    private String dataTime;
    private String sidoName;
    private String stationName;
    private String totalCount;

    public AllStateDustInfoDto() {}

    public AllStateDustInfoDto(Parcel in) {
        readToParcel(in);
    }

    public void set_returnType(String _returnType) {
        this._returnType = _returnType;
    }

    public String get_returnType() {
            return _returnType;
    }

    public String getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(String coGrade) {
        this.coGrade = coGrade;
    }

    public String getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(String no2Grade) {
        this.no2Grade = no2Grade;
    }

    public String getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(String o3Grade) {
        this.o3Grade = o3Grade;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(String so2Grade) {
        this.so2Grade = so2Grade;
    }

    public String getKhaiGrade() {
        return khaiGrade;
    }

    public void setKhaiGrade(String khaiGrade) {
        this.khaiGrade = khaiGrade;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getKhaiValue() {
        return khaiValue;
    }

    public void setKhaiValue(String khaiValue) {
        this.khaiValue = khaiValue;
    }

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getRnum() {
        return rnum;
    }

    public void setRnum(String rnum) {
        this.rnum = rnum;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getDataTerm() {
        return dataTerm;
    }

    public void setDataTerm(String dataTerm) {
        this.dataTerm = dataTerm;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AllStateDustInfoDto createFromParcel(Parcel in) {
            return new AllStateDustInfoDto(in);
        }

        public AllStateDustInfoDto[] newArray(int size) {
            return new AllStateDustInfoDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_returnType);
        dest.writeString(coGrade);
        dest.writeString(no2Grade);
        dest.writeString(o3Grade);
        dest.writeString(pm10Grade);
        dest.writeString(so2Grade);
        dest.writeString(khaiGrade);
        dest.writeString(coValue);
        dest.writeString(no2Value);
        dest.writeString(o3Value);
        dest.writeString(pm10Value);
        dest.writeString(so2Value);
        dest.writeString(khaiValue);
        dest.writeString(numOfRows);
        dest.writeString(rnum);
        dest.writeString(resultCode);
        dest.writeString(resultMsg);
        dest.writeString(stationCode);
        dest.writeString(serviceKey);
        dest.writeString(pageNo);
        dest.writeString(dataTerm);
        dest.writeString(dataTime);
        dest.writeString(sidoName);
        dest.writeString(stationName);
        dest.writeString(totalCount);
    }

    public void readToParcel(Parcel in) {
        _returnType = in.readString();
        coGrade = in.readString();
        no2Grade = in.readString();
        o3Grade = in.readString();
        pm10Grade = in.readString();
        so2Grade = in.readString();
        khaiGrade = in.readString();
        coValue = in.readString();
        no2Value = in.readString();
        o3Value = in.readString();
        pm10Value = in.readString();
        so2Value = in.readString();
        khaiValue = in.readString();
        numOfRows = in.readString();
        rnum = in.readString();
        resultCode = in.readString();
        resultMsg = in.readString();
        stationCode = in.readString();
        serviceKey = in.readString();
        pageNo = in.readString();
        dataTerm = in.readString();
        dataTime = in.readString();
        sidoName = in.readString();
        stationName = in.readString();
        totalCount = in.readString();
    }

    @Override
    public String toString() {
        return "AllStateDustInfoDto{" +
                "_returnType='" + _returnType + '\'' +
                ", coGrade='" + coGrade + '\'' +
                ", no2Grade='" + no2Grade + '\'' +
                ", o3Grade='" + o3Grade + '\'' +
                ", pm10Grade='" + pm10Grade + '\'' +
                ", so2Grade='" + so2Grade + '\'' +
                ", khaiGrade='" + khaiGrade + '\'' +
                ", coValue='" + coValue + '\'' +
                ", no2Value='" + no2Value + '\'' +
                ", o3Value='" + o3Value + '\'' +
                ", pm10Value='" + pm10Value + '\'' +
                ", so2Value='" + so2Value + '\'' +
                ", khaiValue='" + khaiValue + '\'' +
                ", numOfRows='" + numOfRows + '\'' +
                ", rnum='" + rnum + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", serviceKey='" + serviceKey + '\'' +
                ", pageNo='" + pageNo + '\'' +
                ", dataTerm='" + dataTerm + '\'' +
                ", dataTime='" + dataTime + '\'' +
                ", sidoName='" + sidoName + '\'' +
                ", stationName='" + stationName + '\'' +
                ", totalCount='" + totalCount + '\'' +
                '}';
    }
}
