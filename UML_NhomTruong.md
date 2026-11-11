# Sơ đồ các thành phần nhóm trưởng trong phiên bản demo

> Có thể dán khối Mermaid vào Mermaid Live Editor hoặc Markdown Viewer để xem. Đây **không phải** sơ đồ lớp gốc do nhóm gửi; chỉ thể hiện quan hệ đang được cài đặt trong source.

```mermaid
classDiagram
    class Quyen {
      <<enumeration>>
      QUAN_LY
      BAN_HANG
      KHO
    }
    class IQuanLy~T~ {
      <<interface>>
      +them(T)
      +sua(ma, T)
      +xoa(ma)
      +timTheoMa(ma)
      +layDanhSach()
    }
    class ILuuFile {
      <<interface>>
      +docFile()
      +ghiFile()
    }
    class IThongKe {
      <<interface>>
      +tinhDoanhThu()
      +tongSoLuongTon()
    }
    class TaiKhoan
    class CuaHang
    class KiemTra
    class TienIch
    class KhoiTaoDuLieu
    class ChuongTrinh
    class Menu {
      <<abstract>>
      +chay()
      #hienThi()*
      #xuLy(int)*
    }
    class MenuQuanLy
    class MenuBanHang
    class MenuKho
    class SanPham {
      <<abstract>>
      +tinhDonGia(int)*
    }
    class SanPhamThuong
    class SanPhamKhuyenMai
    class SanPhamGiaSi
    class HoaDon
    class ChiTietHoaDon
    CuaHang ..|> IQuanLy~T~
    CuaHang ..|> ILuuFile
    CuaHang ..|> IThongKe
    Menu <|-- MenuQuanLy
    Menu <|-- MenuBanHang
    Menu <|-- MenuKho
    SanPham <|-- SanPhamThuong
    SanPham <|-- SanPhamKhuyenMai
    SanPham <|-- SanPhamGiaSi
    CuaHang o-- "many" SanPham
    CuaHang o-- "many" TaiKhoan
    CuaHang o-- "many" HoaDon
    HoaDon *-- "many" ChiTietHoaDon
    TaiKhoan --> Quyen
    Menu --> CuaHang
    ChuongTrinh --> KhoiTaoDuLieu
    ChuongTrinh --> Menu
    Menu ..> KiemTra
    Menu ..> TienIch
```
