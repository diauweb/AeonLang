package xiaodai.aeon.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import xiaodai.aeon.runtime.AeonObject;
import xiaodai.aeon.runtime.AeonRuntime;
import xiaodai.aeon.runtime.AeonRuntimeException;
import xiaodai.aeon.runtime.IRunnable;

public final class Prelude {
    
    @LibFunction("import")
    public static final IRunnable importFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        return in;
    };

    @LibFunction("print")
    public static final IRunnable printFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        System.out.println(in.getObject());
        return AeonObject.ofNull();
    };

    @LibFunction("readline")
    public static final IRunnable readlineFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        Scanner scanner = new Scanner(System.in);
        var ret = AeonObject.of(scanner.nextLine());
        scanner.close();
        return ret;
    };

    @LibFunction("split")
    public static final IRunnable splitFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        var ret = new AeonObject();
        ret.setType("list");
        
        if (in.getObject() instanceof String s) {
            var sp = s.split((String) args.get(0).getObject());
            ret.setObject(Arrays.stream(sp).map(AeonObject::of).collect(Collectors.toList()));
            return ret;
        } else {
            throw new AeonRuntimeException("type mismatch, type string wanted");
        }
    };

    @LibFunction("take")
    public static final IRunnable takeFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        var e = args.get(0);
        int cnt;
        if (e == null) cnt = 0;
        else {
            cnt = Integer.parseInt(e.getObject().toString());
        }

        if (in.getObject() instanceof List<?> c) {
            var it = c.iterator();
            var ret = new ArrayList<AeonObject>();
            for (int i = 0; i < cnt; i++) {
                if (!it.hasNext()) break;
                ret.add((AeonObject) it.next());
            }
            return AeonObject.of("list", ret);
        } else {
            throw new AeonRuntimeException("type mismatch, collection wanted");
        }
    };

    @LibFunction("add")
    public static final IRunnable addFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        var adder = Integer.parseInt(args.get(0).getObject().toString());
        var added = Integer.parseInt(in.getObject().toString());
        return AeonObject.of("int", added + adder);
    };

    @LibFunction("reduce")
    public static final IRunnable reduceFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        var initValue = args.get(0);
        var func = args.get(1);
        
        if (func.getObject() instanceof IRunnable r) {
            var li = (List<?>) in.getObject();
            for (var e : li) {
                initValue = r.run(rt, initValue, List.of((AeonObject) e));
            }
            return initValue;
        } else {
            throw new AeonRuntimeException("reduce function mismatch");
        }
    };
    
    @LibFunction("with")
    public static final IRunnable withFunc = (AeonRuntime rt, AeonObject in, List<AeonObject> args) -> {
        return args.get(0);
    };
}
