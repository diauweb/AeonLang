package xiaodai.aeon;

public final class AeonOp {

    public static final byte OP_NOP = 0;  // A=0 P=0
    public static final byte OP_LOAD = 1; // A=1 P=1
    public static final byte OP_CALL = 2; // A=1 P=-a+1
    public static final byte OP_CLR = 3; // A=1 P=X
    public static final byte OP_FIELD = 4;
    public static final byte OP_SEG = 5;
    public static final byte OP_LLOAD = 100; // A=4 P=1
    public static final String[] OP_NAMES = { "nop", "load", "call", "clr", "field", "seg" };


    public static AeonOp of(byte opcode, byte ...operands) {
        var self = new AeonOp();
        self.opcode = opcode;
        self.operands = operands;
        return self;
    }

    public byte opcode;
    public byte[] operands;

    public String getName()  {
        if (opcode < OP_NAMES.length) {
            return OP_NAMES[opcode];
        } else {
            return "???";
        }
    }

    public byte[] toBytes() {
        var b = new byte[1 + operands.length];
        b[0] = opcode;
        System.arraycopy(operands, 0, b, 1, operands.length);
        return b;
    }
}
