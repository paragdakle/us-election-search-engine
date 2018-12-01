package core.io;

import java.util.Map;

public interface IIOHandler<S, T> {

    Map<S, T> read();

    boolean write(S key, T value);
}
