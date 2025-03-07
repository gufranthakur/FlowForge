package flowforge.nodes;

import java.util.Map;

public interface SerializableNode {
    void addCustomProperties(Map<String, Object> properties);
    void restoreCustomProperties(Map<String, Object> properties);
}