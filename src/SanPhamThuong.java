public final class SanPhamThuong extends SanPham {
    public SanPhamThuong(String ma, String ten, long gia, int ton) {
        super(ma, ten, gia, ton);
    }

    @Override public String getLoai() { return "THUONG"; }

    @Override public long tinhDonGia(int soLuongMua) {
        if (soLuongMua <= 0) throw new IllegalArgumentException("Số lượng phải dương");
        return getGiaGoc();
    }
}
