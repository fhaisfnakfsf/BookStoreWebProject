/** Interface 1: các thao tác quản lý danh sách đối tượng. */
public interface IQuanLy<T> {
    boolean them(T doiTuong);
    boolean sua(String ma, T doiTuongMoi);
    boolean xoa(String ma);
    T timTheoMa(String ma);
    T[] layDanhSach();
}
