package xiaodai.aeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class AeonOp {

    public static final byte OP_NOP = 0;  // A=0 P=0
    public static final byte OP_LOAD = 1; // A=1 P=1
    public static final byte OP_CALL = 2; // A=1 P=-a+1
    public static final byte OP_CLR = 3; // A=1 P=X
    public static final byte OP_LLOAD = 100; // A=4 P=1

    public static AeonOp of(byte opcode, byte ...operands) {
        var self = new AeonOp();
        self.opcode = opcode;
        IntStream.range(0, operands.length).forEach(e -> self.operands.add(operands[e]));
        return self;
    }

    public byte opcode;
    public ArrayList<Byte> operands = new ArrayList<>();


    public byte[] toBytes() {
        var b = new byte[1 + operands.size()];
        b[0] = opcode;
        for (int i = 0; i < operands.size(); i++) {
            b[i + 1] = operands.get(i);
        }
        return b;
    }
}
