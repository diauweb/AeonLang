package xiaodai.aeon;

import java.util.ArrayList;

import xiaodai.aeon.parser.AeonBaseListener;
import xiaodai.aeon.parser.AeonParser.ExprIdContext;
import xiaodai.aeon.parser.AeonParser.ExpressionContext;
import xiaodai.aeon.parser.AeonParser.IdentifierContext;
import xiaodai.aeon.parser.AeonParser.ProgramContext;

public class AeonListener extends AeonBaseListener {

    private ArrayList<String> literalPool = new ArrayList<>();
    private ArrayList<ArrayList<AeonOp>> segmentsPool = new ArrayList<>();
    private int literalLen = 0;
    private int segmentLen = 0;

    private ArrayList<AeonOp> segment;

    @Override
    public void enterProgram(ProgramContext ctx) {
        // Create `entry` program segment #0
        this.segment = new ArrayList<AeonOp>();
        segmentsPool.add(this.segment);
        segmentLen++;
        assert(segmentLen == 1);
    }

    @Override
    public void exitExpression(ExpressionContext ctx) {
        int popcount = ctx.getChildCount();
        assert(popcount < 256);
        segment.add(AeonOp.of(AeonOp.OP_CALL, (byte) popcount));
    }

    @Override
    public void exitExprId(ExprIdContext ctx) {
        assert(literalLen < 256);
        // todo: long load
        segment.add(AeonOp.of(AeonOp.OP_LOAD, (byte) literalLen));
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
        
        literalLen++;
        literalPool.add(id);
    }
}
