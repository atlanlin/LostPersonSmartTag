package com.yelling.lostpersonsmarttag;

/**
 * Created by Yelling on 26/10/15.
 */
public class EmergencyEvent {
    protected String eventId, wardId, wardName, guardianDesc, finderDesc, cookie;
    protected int isApproved, isOngoing;

    public EmergencyEvent(String eventId, String wardId, String wardName, int isApproved, int isOngoing,
                          String guardianDesc, String finderDesc, String cookie){
        this.eventId = eventId;
        this.wardId = wardId;
        this.wardName = wardName;
        this.guardianDesc = guardianDesc;
        this.finderDesc = finderDesc;
        this.cookie = cookie;
        this.isApproved = isApproved;
        this.isOngoing = isOngoing;
    }
}
