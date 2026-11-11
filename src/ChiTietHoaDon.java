public final class ChiTietHoaDon {
    private final String maSanPham;
    private final String tenSanPham;
    private final int soLuong;
    private final long donGia;

    public ChiTietHoaDon(String maSanPham, String tenSanPham, int soLuong, long donGia) {
        if (maSanPham == null || maSanPham.isBlank() || tenSanPham == null || tenSanPham.isBlank())
            throw new IllegalArgumentException("Thiếu thông tin sản phẩm");
        if (soLuong <= 0 || donGia <= 0)
            throw new IllegalArgumentException("Số lượng và đơn giá phải dương");
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        tinhThanhTien();
    }

    public long tinhThanhTien() { return Math.multiplyExact(donGia, soLuong); }
    public String getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public int getSoLuong() { return soLuong; }
    public long getDonGia() { return donGia; }
}
