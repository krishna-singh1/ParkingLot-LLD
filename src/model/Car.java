package model;

public class Car {
    String registrationNo;
    String color;

    public Car(String registrationNo, String color) {
        this.registrationNo = registrationNo;
        this.color = color;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getColor() {
        return color;
    }
}
