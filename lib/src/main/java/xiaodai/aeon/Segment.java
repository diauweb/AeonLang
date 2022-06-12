package xiaodai.aeon;

import java.util.ArrayList;
import java.util.List;

public class Segment {
    private List<String> captures = new ArrayList<>();
    private List<AeonOp> ops = new ArrayList<>();

    public List<String> getCaptures() {
        return captures;
    }
    public void setCaptures(List<String> captures) {
        this.captures = captures;
    }
    public List<AeonOp> getOps() {
        return ops;
    }
    public void setOps(List<AeonOp> ops) {
        this.ops = ops;
    }

    public void addOp(byte op, byte ...operands) {
        this.ops.add(AeonOp.of(op, operands));
    }

}
