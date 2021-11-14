package com.myspecialist.Adapter;

public class DoctorListAdapter {
    String Name;
    String Duty;
    String Department;
    String Image;
    String Location;

    public DoctorListAdapter() {
    }

    public DoctorListAdapter(String name, String duty, String department, String image, String location) {
        Name = name;
        Duty = duty;
        Department = department;
        Image = image;
        Location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDuty() {
        return Duty;
    }

    public void setDuty(String duty) {
        Duty = duty;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
