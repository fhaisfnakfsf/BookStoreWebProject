public final class SanPhamKhuyenMai extends SanPham {
    private final int phanTramGiam;

    public SanPhamKhuyenMai(String ma, String ten, long gia, int ton, int phanTramGiam) {
        super(ma, ten, gia, ton);
        if (phanTramGiam < 1 || phanTramGiam > 70)
            throw new IllegalArgumentException("Mức giảm phải từ 1% đến 70%");
        this.phanTramGiam = phanTramGiam;
    }

    public int getPhanTramGiam() { return phanTramGiam; }
    @Override public String getLoai() { return "KHUYEN_MAI"; }

    @Override public long tinhDonGia(int soLuongMua) {
        if (soLuongMua <= 0) throw new IllegalArgumentException("Số lượng phải dương");
        return Math.max(1L, getGiaGoc() * (100 - phanTramGiam) / 100);
    }
}
