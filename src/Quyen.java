public enum Quyen {
    QUAN_LY("Quản lý"),
    BAN_HANG("Bán hàng"),
    KHO("Thủ kho");

    private final String ten;

    Quyen(String ten) {
        this.ten = ten;
    }

    public String getTen() {
        return ten;
    }
}
