package xiaodai.aeon;

import java.util.List;

public class AeonProgram {
    
    private final byte[] magic = new byte[]{ (byte) 0xAE, 0x07, (byte) 0xB1, (byte) 0xC0 };

    private List<Segment> segments;
    private List<String> literals;

    public static AeonProgram deserialize(byte[] b) {
        return null;
    }

    public byte[] serialize() {
        return new byte[0];
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public void setLiterals(List<String> literals) {
        this.literals = literals;
    }

    public String debugString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[AeonProgram]\n");
        sb.append("  [Segments]\n");
        for (int i = 0; i < segments.size(); i++) {
            sb.append("    [Segment #"); 
            sb.append(i);
            sb.append("]\n");
            sb.append("      [Captures:");
            var seg = segments.get(i);
            for (String cap : seg.getCaptures()) {
                sb.append(" ");
                sb.append(cap);
            }
            sb.append("]\n");
            
            var ops = seg.getOps();
            for (int j = 0; j < ops.size(); j++) {
                var op = ops.get(j);
                sb.append("      ");
                sb.append(op.getName());
                for (int k = 0; k < op.operands.length; k++) {
                    sb.append(" ");
                    sb.append(op.operands[k]);
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        sb.append("  [ConstantPool]\n");
        for (int i = 0; i < literals.size(); i++) {
            sb.append("    [");
            sb.append(i);
            sb.append("]: ");
            sb.append(literals.get(i).strip());
            sb.append("\n");
        }
        return sb.toString();
    }
}
