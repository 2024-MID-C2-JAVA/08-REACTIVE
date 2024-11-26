package co.com.sofka.cuentaflex.libs.infrastructure.entry_points.din;

public final class DinRequest<BodyType> {
    private DinHeader dinHeader;
    private BodyType dinBody;

    public DinRequest() {
    }

    public DinRequest(DinHeader dinHeader, BodyType dinBody) {
        this.dinHeader = dinHeader;
        this.dinBody = dinBody;
    }

    public DinHeader getDinHeader() {
        return dinHeader;
    }

    public void setDinHeader(DinHeader dinHeader) {
        this.dinHeader = dinHeader;
    }

    public BodyType getDinBody() {
        return dinBody;
    }

    public void setDinBody(BodyType dinBody) {
        this.dinBody = dinBody;
    }
}
