package com.prototype.project.testrun69.Model;

import java.util.List;


public class ChildData {

    private String childName;
    private String childDeviceModel;
    private String childAppName;
    private String childPkgApps;

    public ChildData() {
    }

    public ChildData(String childName, String childDeviceModel) {
        this.childName = childName;
        this.childDeviceModel = childDeviceModel;
    }

    public ChildData(String childName, String childDeviceModel, String childAppName, String childPkgApps) {
        this.childName = childName;
        this.childDeviceModel = childDeviceModel;
        this.childAppName = childAppName;
        this.childPkgApps = childPkgApps;
    }

    public String getChildName() {
        return childName;
    }

    public String getChildDeviceModel() {
        return childDeviceModel;
    }

    public String getChildAppName() {
        return childAppName;
    }

    public String getChildPkgApps() {
        return childPkgApps;
    }
}
