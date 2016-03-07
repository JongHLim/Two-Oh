package jonghoonlim.two_oh.dataStructures;

/**
 * Created by jhl2298 on 3/6/2016.
 * Data structure to help display items on the ListViews.
 */
public class Item {

    private String utTag;
    private String checkInDate;
    private String checkOutDate;
    private String machineType;
    private String operatingSystem;
    private String checkedIn;

    public Item() {
        utTag = "";
        checkInDate = "";
        checkOutDate = "";
        machineType = "";
        operatingSystem = "";
        checkedIn = "";
    }

    public void setUtTag(String utTag) {this.utTag = utTag;}

    public String getUtTag() {return this.utTag;}

    public void setCheckInDate(String checkInDate) {this.checkInDate = checkInDate;}

    public String getCheckInDate() {return this.checkInDate;}

    public void setCheckOutDate(String checkOutDate) {this.checkOutDate = checkOutDate;}

    public String getCheckOutDate() {return this.checkOutDate;}

    public void setMachineType(String machineType) {this.machineType = machineType;}

    public String getMachineType() {return this.machineType;}

    public void setOperatingSystem(String operatingSystem) {this.operatingSystem = operatingSystem;}

    public String getOperatingSystem() {return this.operatingSystem;}

    public void setCheckedIn(String checkedIn) {this.checkedIn = checkedIn;}

    public String getCheckedIn() {return this.checkedIn;}
}
