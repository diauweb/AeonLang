package xiaodai.aeon.library;

import java.lang.reflect.Modifier;
import java.util.HashMap;

import xiaodai.aeon.runtime.AeonObject;
import xiaodai.aeon.runtime.AeonRuntimeException;
import xiaodai.aeon.runtime.BasicContextResolver;
import xiaodai.aeon.runtime.IContextResolver;
import xiaodai.aeon.runtime.IRunnable;

public final class Libraries {
    public static HashMap<String, AeonObject> makeFunctions(Class<?> clazz) {
        try {
            
            var fields = clazz.getFields();
            var ret = new HashMap<String, AeonObject>();
            for (var m : fields) {
                if (!Modifier.isStatic(m.getModifiers())) continue;
                
                LibFunction annotation = m.getAnnotation(LibFunction.class);
                if (annotation == null) continue;
                
                String funcName = annotation.value();
                AeonObject object = new AeonObject();
                object.setType("builtin function");
                object.setObject((IRunnable) m.get(null));
                ret.put(funcName, object);
            }

            return ret;
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException e) {
            throw new AeonRuntimeException("cannot make library function");
        }
    }

    public static IContextResolver makeRootContext() {
        var bs = new BasicContextResolver();
        // add prelude functions only
        var funcs = makeFunctions(Prelude.class);
        
        for (var kv : funcs.entrySet()) {
            bs.bindField(kv.getKey(), kv.getValue());
        }
        return bs;
    }
}
