package xiaodai.aeon;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import xiaodai.aeon.runtime.AeonRuntime;

class AeonCompileTest {
    @Test void compileTest () throws IOException, URISyntaxException {
        var res = this.getClass().getResource("/test.aeon");
        var f = Files.readString(Path.of(res.toURI()));
        AeonCompiler compiler = new AeonCompiler(f);
        var program = compiler.compile();
        var str = program.debugString();
        var rt = new AeonRuntime(program);
        // rt.run();
        System.out.println(str);
    }
}
