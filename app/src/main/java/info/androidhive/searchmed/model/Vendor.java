package info.androidhive.searchmed.model;

/**
 * Created by Administrator on 08/12/2015.
 */
public class Vendor {

    private String VENDORID;
    private String VENDORNAME;
    private String VENDORAREA;
    private String VENDORCONTACTNAME;
    private String VENDORADDRESS;
    private String VENDORMOBILE;
    private String VENDORLANDLINE;
    private String CITY;
    private String STATE;
    private String VENDOREMAIL;
    private String AREANAME;
    private int AREAID;
    private int VEN_TYPE;
    private String VEN_DELSTATE;
    private String FROM_TIME;
    private String TO_TIME;
    private String OFF_DAY;
    private String PINCODE;
    private String favStatus;


    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getPINCODE() {
        return PINCODE;
    }

    public void setPINCODE(String PINCODE) {
        this.PINCODE = PINCODE;
    }

    public String getFROM_TIME() {
        return FROM_TIME;
    }

    public void setFROM_TIME(String FROM_TIME) {
        this.FROM_TIME = FROM_TIME;
    }

    public String getTO_TIME() {
        return TO_TIME;
    }

    public void setTO_TIME(String TO_TIME) {
        this.TO_TIME = TO_TIME;
    }

    public String getOFF_DAY() {
        return OFF_DAY;
    }

    public void setOFF_DAY(String OFF_DAY) {
        this.OFF_DAY = OFF_DAY;
    }


    public int getAREAID() {
        return AREAID;
    }

    public void setAREAID(int AREAID) {
        this.AREAID = AREAID;
    }
    public int getVEN_TYPE() {
        return VEN_TYPE;
    }
    public String getVEN_DELSTATE() {
        return VEN_DELSTATE;
    }

    public void setVEN_DELSTATE(String VEN_DELSTATE) {
        this.VEN_DELSTATE = VEN_DELSTATE;
    }

    public void setVEN_TYPE(int VEN_TYPE) {
        this.VEN_TYPE = VEN_TYPE;
    }


    public String getVENDORID() {
        return VENDORID;
    }

    public void setVENDORID(String VENDORID) {
        this.VENDORID = VENDORID;
    }

    public String getVENDORNAME() {
        return VENDORNAME;
    }


    public void setVENDORNAME(String VENDORNAME) {
        this.VENDORNAME = VENDORNAME;
    }
    public void setVENDORAREA(String VENDORAREA){
        this.VENDORAREA = VENDORAREA;
    }
    public String getVENDORAREA() {
        return VENDORAREA;
    }

    public String getVENDORCONTACTNAME() {
        return VENDORCONTACTNAME;
    }

    public void setVENDORCONTACTNAME(String VENDORCONTACTNAME) {
        this.VENDORCONTACTNAME = VENDORCONTACTNAME;
    }

    public String getVENDORADDRESS() {
        return VENDORADDRESS;
    }


    public void setVENDORADDRESS(String VENDORADDRESS) {
        this.VENDORADDRESS = VENDORADDRESS;
    }


    public String getVENDORMOBILE() {
        return VENDORMOBILE;
    }


    public void setVENDORMOBILE(String VENDORMOBILE) {
        this.VENDORMOBILE = VENDORMOBILE;
    }

    public String getVENDORLANDLINE() {
        return VENDORLANDLINE;
    }

    public void setVENDORLANDLINE(String VENDORLANDLINE) {
        this.VENDORLANDLINE = VENDORLANDLINE;
    }

    public String getCITY() {
        return CITY;
    }


    public void setCITY(String CITY) {
        this.CITY = CITY;
    }


    public String getSTATE() {
        return STATE;
    }


    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }


    public String getVENDOREMAIL() {
        return VENDOREMAIL;
    }

    public void setVENDOREMAIL(String VENDOREMAIL) {
        this.VENDOREMAIL = VENDOREMAIL;
    }


    public String getAREANAME() {
        return AREANAME;
    }


    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

}