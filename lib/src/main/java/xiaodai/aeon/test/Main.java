package xiaodai.aeon.test;

import xiaodai.aeon.AeonCompiler;
import xiaodai.aeon.runtime.AeonRuntime;

public class Main {
    static String src = """
                with 'Hello World!' | print;
                readline | split ' ' | take 2 | reduce 0 [v: add $v] | print;
            """;

    public static void main(String[] args) {
        AeonCompiler compiler = new AeonCompiler(src);
        var program = compiler.compile();
        var str = program.debugString();
        var rt = new AeonRuntime(program);
        rt.run();
        System.out.println(str);
    }
}
