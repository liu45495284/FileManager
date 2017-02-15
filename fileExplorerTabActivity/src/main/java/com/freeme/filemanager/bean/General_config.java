package com.freeme.filemanager.bean;

//*/ add by droi liuhaoran for add customized configuration file on 20160428
public class  General_config {

    private int id;
    private String isHideFTP;
    private int memoryCardInfo;
    private String isDaMi;
    private String isFeiMa;
    private String isNeedRingTone;
    private String isTest;
    
    public General_config(){
        super();
    }
    public General_config(int id, String isHideFTP,int memoryCardInfo,String isDaMi,String isFeiMa,
                          String isNeedRingTone , String isTest) {
        super();
        this.id = id;
        this.isHideFTP = isHideFTP;
        this.memoryCardInfo = memoryCardInfo;
        this.isDaMi = isDaMi;
        this.isFeiMa = isFeiMa;
        this.isNeedRingTone = isNeedRingTone;
        this.isTest = isTest;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getIsHideFTP() {
        return isHideFTP;
    }
    public void setIsHideFTP(String isHideFTP) {
        this.isHideFTP = isHideFTP;
    }
    public int getMemoryCardInfo() {
        return memoryCardInfo;
    }
    public void setMemoryCardInfo(int memoryCardInfo) {
        this.memoryCardInfo = memoryCardInfo;
    }
    public String getIsDaMi() {
        return isDaMi;
    }
    public void setIsDaMi(String isDaMi) {
        this.isDaMi = isDaMi;
    }
    public String getIsFeiMa() {
        return isFeiMa;
    }
    public void setIsFeiMa(String isFeiMa) {
        this.isFeiMa = isFeiMa;
    }
    public String getIsNeedRingTone() {
        return isNeedRingTone;
    }
    public void setIsNeedRingTone(String isNeedRingTone) {
        this.isNeedRingTone = isNeedRingTone;
    }
    public String getIsTest() {
        return isTest;
    }
    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }
}
//*/