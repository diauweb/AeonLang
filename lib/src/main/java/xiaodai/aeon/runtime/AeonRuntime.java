package xiaodai.aeon.runtime;

import java.util.ArrayList;
import java.util.LinkedList;

import xiaodai.aeon.AeonOp;
import xiaodai.aeon.AeonProgram;
import xiaodai.aeon.Segment;
import xiaodai.aeon.library.Libraries;

public class AeonRuntime {
    private AeonProgram program;

    public AeonRuntime(AeonProgram prog) {
        this.program = prog;
    }

    public void run() {
        var stack = new LinkedList<AeonObject>();
        stack.push(AeonObject.ofNull());
        var resolver = Libraries.makeRootContext();
        runSegment(program.getSegments().get(0), stack, resolver);
    }

    private AeonObject runSegment(Segment seg, LinkedList<AeonObject> stack, IContextResolver resolver) {
        var ops = seg.getOps();
        for (int pc = 0; pc < ops.size(); pc++) {
            var op = ops.get(pc);
            switch (op.opcode) {
                case AeonOp.OP_NOP:
                    break;
                case AeonOp.OP_LOAD:
                    var slot = op.operands[0];
                    stack.push(AeonObject.of(program.getLiterals().get(slot)));
                    break;
                case AeonOp.OP_CALL: {
                    var argc = op.operands[0];
                    LinkedList<AeonObject> args = new LinkedList<>();
                    for (int i = argc - 1; i >= 0; i--) {
                        args.push(stack.pop());
                    }

                    var callee = stack.pop();
                    if (callee.getType().equals("string")) {
                        callee = resolver.resolve((String) callee.getObject());
                    }

                    if (callee.getType().indexOf("function") == -1) {
                        throw new AeonRuntimeException("object is not callable");
                    }

                    var in = stack.pop();
                    var out = ((IRunnable) callee.getObject()).run(this, in, args);
                    stack.push(out);
                    break;
                }
                case AeonOp.OP_CLR:
                    stack.clear();
                    stack.push(AeonObject.ofNull());
                    break;
                case AeonOp.OP_FIELD:
                    var fid = op.operands[0];
                    stack.push(resolver.resolve(program.getLiterals().get(fid)));
                    break;
                case AeonOp.OP_SEG: {
                    var sid = op.operands[0];
                    var fseg = program.getSegments().get(sid);

                    IRunnable function = (runtime, in, args) -> {
                        var fstack = new LinkedList<AeonObject>();
                        fstack.push(in);
                        IContextResolver fres = (String name) -> {
                            var ai = fseg.getCaptures().indexOf(name);
                            if (ai != -1) {
                                return args.get(ai);
                            } else {
                                return resolver.resolve(name);
                            }
                        };
                        return runtime.runSegment(fseg, fstack, fres);
                    };

                    stack.push(AeonObject.of("function", function));
                    break;
                }
            }
        }
        return stack.pop();
    }
}
