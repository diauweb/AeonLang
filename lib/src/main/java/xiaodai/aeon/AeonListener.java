package xiaodai.aeon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import xiaodai.aeon.parser.AeonBaseListener;
import xiaodai.aeon.parser.AeonParser.DereferenceContext;
import xiaodai.aeon.parser.AeonParser.ExprFuncContext;
import xiaodai.aeon.parser.AeonParser.ExprIdContext;
import xiaodai.aeon.parser.AeonParser.ExpressionContext;
import xiaodai.aeon.parser.AeonParser.FunctionArgsContext;
import xiaodai.aeon.parser.AeonParser.FunctionContext;
import xiaodai.aeon.parser.AeonParser.IdentifierContext;
import xiaodai.aeon.parser.AeonParser.ProgramContext;
import xiaodai.aeon.parser.AeonParser.StatementContext;

public class AeonListener extends AeonBaseListener {

    private ArrayList<String> literalPool = new ArrayList<>();
    private List<Segment> segmentsPool = new ArrayList<>();
    private int literalIndex = -1;
    private int segmentIndex = -1;

    private Segment segment;

    private LinkedList<Integer> segmentsStack = new LinkedList<>();

    @Override
    public void enterProgram(ProgramContext ctx) {
        // Create `entry` program segment #0
        this.segment = new Segment();
        segmentsPool.add(this.segment);
        segmentIndex++;
        segmentsStack.push(segmentIndex);
        assert(segmentIndex == 0);
    }

    @Override
    public void enterFunction(FunctionContext ctx) {
        this.segment = new Segment();
        segmentsPool.add(this.segment);
        segmentIndex++;
        segmentsStack.push(segmentIndex);
    }

    @Override
    public void enterFunctionArgs(FunctionArgsContext ctx) {
        this.segment.setCaptures(ctx.ID().stream().map(t -> t.getText()).collect(Collectors.toList()));
    }

    @Override
    public void exitExprFunc(ExprFuncContext ctx) {
        // public void exitFunction(FunctionContext ctx) {
        // probably ok to switch context here
        // as Function syntax cannot be elsewhere
        var current = segmentsStack.pop();
        this.segment = this.segmentsPool.get(segmentsStack.peek());
        this.segment.addOp(AeonOp.OP_SEG, (byte)(int) current);
    }

    @Override
    public void exitProgram(ProgramContext ctx) {
        segmentsStack.pop();
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        int popcount = ctx.getChildCount() - 1;
        assert(popcount < 256);
        segment.addOp(AeonOp.OP_CALL, (byte) popcount);
    }

    @Override
    public void exitExprId(ExprIdContext ctx) {
        assert(literalIndex < 256);
        // todo: long load
        segment.addOp(AeonOp.OP_LOAD, (byte) literalIndex);
    }

    @Override
    public void enterIdentifier(IdentifierContext ctx) {
        String id;
        if(ctx.ID() != null) {
            id = ctx.ID().getText();
        } else {
            id = ctx.QUOTED().getText();
            id = id.substring(1, id.length() - 1);
        }
        
        literalIndex++;
        literalPool.add(id);
    }
    

    @Override
    public void enterDereference(DereferenceContext ctx) {
        literalIndex++;
        literalPool.add(ctx.ID(0).getText());

        // todo long index
        segment.addOp(AeonOp.OP_FIELD, (byte) literalIndex);
    }

    @Override
    public void exitStatement(StatementContext ctx) {
        // Clear stack
        segment.addOp(AeonOp.OP_CLR);
    }

    public AeonProgram toProgram () {
        var prog = new AeonProgram();
        prog.setLiterals(literalPool);
        prog.setSegments(segmentsPool);
        return prog;
    }
}
