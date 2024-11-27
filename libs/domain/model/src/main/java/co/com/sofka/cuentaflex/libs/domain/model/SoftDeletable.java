package co.com.sofka.cuentaflex.libs.domain.model;

public interface SoftDeletable {
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
