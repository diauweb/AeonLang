package xiaodai.aeon;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import xiaodai.aeon.parser.AeonLexer;
import xiaodai.aeon.parser.AeonParser;

public class AeonCompiler {

    public String src;
    
    public AeonCompiler(String src) {
        this.src = src;
    }

    public AeonProgram compile () {
        AeonLexer lexer = new AeonLexer(CharStreams.fromString(src));
        AeonParser parser = new AeonParser(new CommonTokenStream(lexer));
        AeonListener listener = new AeonListener();
        ParseTreeWalker.DEFAULT.walk(listener, parser.program());
        return listener.toProgram();
    }
}
