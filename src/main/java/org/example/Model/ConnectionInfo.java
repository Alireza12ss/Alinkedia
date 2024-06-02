package org.example.Model;

import java.util.Date;

public class ConnectionInfo {
    private String userLink;
    private String email;
    private String phoneNumber;
    private String phoneType;
    private String address;
    private Date birthday;
    private String birthDayAccess;//limited access
    private String otherWay;

    public ConnectionInfo(String userLink, String email, String phoneNumber, String phoneType
            , String address, Date birthday, String birthDayAccess, String otherWay) {

        this.userLink = userLink;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.address = address;
        this.birthday = birthday;
        this.birthDayAccess = birthDayAccess;
        this.otherWay = otherWay;
    }

    @Override
    public String toString(){
        return "Profile URL : " + userLink
                +"\nEmail : " + email +
                "\n" + phoneNumber + " (" + phoneType + ")\n"+
                address + "\n" + birthday + "\n" + otherWay;
    }
}
