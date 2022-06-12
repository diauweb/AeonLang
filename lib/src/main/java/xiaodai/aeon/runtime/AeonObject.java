package xiaodai.aeon.runtime;

public class AeonObject {
    private String type;
    private Object object;
    
    public static AeonObject of(String s) {
        var ret = new AeonObject();
        ret.type = "string";
        ret.object = s;
        return ret;
    }

    public static AeonObject of(String type, Object o) {
        var ret = new AeonObject();
        ret.type = type;
        ret.object = o;
        return ret;
    }

    public static AeonObject ofNull () {
        var ret = new AeonObject();
        ret.type = "object";
        return ret;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    
}
