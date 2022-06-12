package xiaodai.aeon.runtime;

import java.util.HashMap;

public class BasicContextResolver implements IContextResolver {
    private HashMap<String, AeonObject> ctx = new HashMap<>();

    @Override
    public AeonObject resolve(String name) {
        var obj = ctx.get(name);
        if (obj == null) {
            throw new AeonRuntimeException("null field");
        }
        return obj;
    }

    public void bindField(String name, AeonObject obj) {
        if (ctx.containsKey(name)) {
            throw new AeonRuntimeException("can't bind field twice");
        }
        ctx.put(name, obj);
    }

    public void unbindField(String name) {
        ctx.remove(name);
    }
}
