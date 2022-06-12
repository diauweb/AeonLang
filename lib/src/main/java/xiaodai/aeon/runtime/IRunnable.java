package xiaodai.aeon.runtime;

import java.util.List;

public interface IRunnable {
    AeonObject run(AeonRuntime runtime, AeonObject in, List<AeonObject> arguments);
}
