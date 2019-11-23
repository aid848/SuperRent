package ca.ubc.cs304.model;

public class Customer {
    //instance of customer tuple abstraction
    private long cellphone;
    private String name;
    private String address;
    private int dlicense;

    public Customer(long cellphone, String name, String address, int dlicense) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public Customer() { //TODO remove for release
        this.cellphone = 0;
        this.name = null;
        this.address = null;
        this.dlicense = 0;
    }

    public long getCellphone() {
        return cellphone;
    }

    public void setCellphone(int cellphone) {
        this.cellphone = cellphone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDlicense() {
        return dlicense;
    }

    public void setDlicense(int dlicense) {
        this.dlicense = dlicense;
    }
}