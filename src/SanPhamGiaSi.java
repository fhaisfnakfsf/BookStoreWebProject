/** Giá sỉ giảm 10% khi mua ít nhất 10 sản phẩm giống nhau. */
public final class SanPhamGiaSi extends SanPham {
    public static final int SO_LUONG_TOI_THIEU = 10;
    public static final int PHAN_TRAM_GIAM = 10;

    public SanPhamGiaSi(String ma, String ten, long gia, int ton) {
        super(ma, ten, gia, ton);
    }

    @Override public String getLoai() { return "GIA_SI"; }

    @Override public long tinhDonGia(int soLuongMua) {
        if (soLuongMua <= 0) throw new IllegalArgumentException("Số lượng phải dương");
        return soLuongMua >= SO_LUONG_TOI_THIEU
                ? Math.max(1L, getGiaGoc() * (100 - PHAN_TRAM_GIAM) / 100)
                : getGiaGoc();
    }
}
