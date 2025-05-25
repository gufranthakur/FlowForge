package flowforge.core;

import com.google.gson.*;
import flowforge.ui.panels.ProgramPanel;
import flowforge.nodes.Node;
import flowforge.nodes.StartNode;
import flowforge.nodes.flownodes.*;
import flowforge.nodes.flownodes.arithmetic.*;
import flowforge.nodes.flownodes.comparators.*;
import flowforge.nodes.flownodes.logicgates.LogicGateNode;
import flowforge.nodes.flownodes.utils.RouteNode;
import flowforge.nodes.variables.*;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/*
    The DataManager class is responsible for saving and loading applications into a JSON File
    The Hierarchy for the data is as Follows :

                  nodes ArrayList  -------------- in ProgramPanel.java
                        ↓
                       node        -------------- Loops through all individual nodes
                       ↙  ↘
             Input Nodes   Output Nodes  --------- Then within each node, there are four ArrayLists
             InputX Node   OutputX Nodes           Then the nodes stored in these arraylists are stored
 */

public class DataManager {
    private ProgramPanel programPanel;
    private Gson gson;

    public DataManager(ProgramPanel programPanel) {
        this.programPanel = programPanel;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Node.class, new NodeSerializer())
                .registerTypeAdapter(Node.class, new NodeDeserializer())
                .create();
    }

    /*
        > Saves the entire program in a .json file
        > Using a Swing Worker to avoid freezing the UI
     */
    public void saveProgram(String filePath) {
        SwingWorker<Void, Void> dataSaverWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){

                try (FileWriter writer = new FileWriter(filePath)) {
                    JsonObject programData = new JsonObject();

                    // Store all nodes with their positions, connections and properties
                    JsonArray nodesArray = new JsonArray();
                    for (Node node : programPanel.nodes) {
                        JsonObject nodeObj = new JsonObject();

                        // Store basic node metadata
                        nodeObj.addProperty("id", programPanel.nodes.indexOf(node));
                        nodeObj.addProperty("type", node.getClass().getSimpleName());
                        nodeObj.addProperty("title", node.getTitle());
                        nodeObj.addProperty("x", node.getNodeX());
                        nodeObj.addProperty("y", node.getNodeY());

                        nodeObj.addProperty("isHighlighted", node.isHighlighted);
                        nodeObj.addProperty("isMinimized", node.isMinimized);
                        nodeObj.addProperty("isCommented", node.isCommented);
                        nodeObj.addProperty("comment", node.comment);

                        // Store primary output connections (flow path)
                        JsonArray outputConnections = new JsonArray();
                        for (Node outputNode : node.outputNodes) {
                            outputConnections.add(programPanel.nodes.indexOf(outputNode));
                        }
                        nodeObj.add("outputConnections", outputConnections);

                        // Store secondary output connections (alternate flow path)
                        JsonArray outputXConnections = new JsonArray();
                        for (Node outputXNode : node.outputXNodes) {
                            outputXConnections.add(programPanel.nodes.indexOf(outputXNode));
                        }
                        nodeObj.add("outputXConnections", outputXConnections);

                        // Store node-specific configuration values
                        JsonObject properties = new JsonObject();
                        saveNodeProperties(node, properties);
                        nodeObj.add("properties", properties);

                        nodesArray.add(nodeObj);

                        // Special handling for BranchNode connections
                        if (node instanceof BranchNode) {
                            BranchNode branchNode = (BranchNode) node;

                            // Save true branch connections
                            JsonArray trueConnections = new JsonArray();
                            for (Node trueNode : branchNode.getTrueNodes()) {
                                trueConnections.add(programPanel.nodes.indexOf(trueNode));
                            }
                            nodeObj.add("trueConnections", trueConnections);

                            // Save false branch connections
                            JsonArray falseConnections = new JsonArray();
                            for (Node falseNode : branchNode.getFalseNodes()) {
                                falseConnections.add(programPanel.nodes.indexOf(falseNode));
                            }
                            nodeObj.add("falseConnections", falseConnections);
                        }
                    }
                    programData.add("nodes", nodesArray);

                    // Save all program variables by type
                    JsonObject variables = new JsonObject();

                    // Integer variables
                    JsonObject integers = new JsonObject();
                    for (Map.Entry<String, Integer> entry : programPanel.integers.entrySet()) {
                        integers.addProperty(entry.getKey(), entry.getValue());
                    }
                    variables.add("integers", integers);

                    // String variables
                    JsonObject strings = new JsonObject();
                    for (Map.Entry<String, String> entry : programPanel.strings.entrySet()) {
                        strings.addProperty(entry.getKey(), entry.getValue());
                    }
                    variables.add("strings", strings);

                    // Boolean variables
                    JsonObject booleans = new JsonObject();
                    for (Map.Entry<String, Boolean> entry : programPanel.booleans.entrySet()) {
                        booleans.addProperty(entry.getKey(), entry.getValue());
                    }
                    variables.add("booleans", booleans);

                    // Float variables
                    JsonObject floats = new JsonObject();
                    for (Map.Entry<String, Float> entry : programPanel.floats.entrySet()) {
                        floats.addProperty(entry.getKey(), entry.getValue());
                    }
                    variables.add("floats", floats);

                    programData.add("variables", variables);

                    // Write the complete program structure to file
                    writer.write(gson.toJson(programData));
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Fatal Error while saving program", JOptionPane.ERROR_MESSAGE);
                }

                return null;
            }

        };
        dataSaverWorker.execute();
    }

    /**
     * Extracts and stores node-specific properties based on node type.
     * Different node types have different configurable properties that need saving.
     * @param node The node whose properties need to be saved
     * @param properties JSON object to store the properties in
     */
    private void saveNodeProperties(Node node, JsonObject properties) {
        if (node instanceof PrintNode) {
            PrintNode printNode = (PrintNode) node;
            properties.addProperty("text", printNode.textField.getText());
        } else if (node instanceof DelayNode delayNode) {
            properties.addProperty("delay", delayNode.delaySpinner.getValue().toString());
        } else if (node instanceof LogicGateNode logicNode) {
            properties.addProperty("gateType", logicNode.getGateType());
        } else if (node instanceof LoopNode loopNode) {
            properties.addProperty("loops", loopNode.loopSpinner.getValue().toString());
            if (loopNode.getIterationValue() != null) {
                properties.addProperty("iterationValue", loopNode.getIterationValue());
            }
        } else if (node instanceof InputNode inputNode) {
            properties.addProperty("inputString", inputNode.inputField.getText());
        } else if (node instanceof EvalNode evalNode) {
            properties.addProperty("expression", evalNode.expressionField.getText());
        }


        else if (node instanceof IntegerNode) {
            IntegerNode intNode = (IntegerNode) node;
            properties.addProperty("name", intNode.getTitle());
            properties.addProperty("value", intNode.getValue());
        } else if (node instanceof StringNode) {
            StringNode strNode = (StringNode) node;
            properties.addProperty("name", strNode.getTitle());
            properties.addProperty("value", strNode.getValue());
        } else if (node instanceof BooleanNode) {
            BooleanNode boolNode = (BooleanNode) node;
            properties.addProperty("name", boolNode.getTitle());
            properties.addProperty("value", boolNode.getValue());
        }
        // Additional node types can be handled here as needed
    }

    /**
     * Loads a program from a JSON file.
     * Reconstructs the entire program state including nodes, connections and variables.
     * Uses a two-pass approach: first create all nodes, then establish connections.
     * @param filePath Path to the program file to load
     */
    public void loadProgram(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            // Reset current program state before loading
            programPanel.clearAll();

            JsonObject programData = JsonParser.parseReader(reader).getAsJsonObject();

            // First restore variables since nodes might depend on them
            if (programData.has("variables")) {
                JsonObject variables = programData.getAsJsonObject("variables");

                if (variables.has("integers")) {
                    JsonObject integers = variables.getAsJsonObject("integers");
                    for (Map.Entry<String, JsonElement> entry : integers.entrySet()) {
                        programPanel.integers.put(entry.getKey(), entry.getValue().getAsInt());
                    }
                }

                if (variables.has("strings")) {
                    JsonObject strings = variables.getAsJsonObject("strings");
                    for (Map.Entry<String, JsonElement> entry : strings.entrySet()) {
                        programPanel.strings.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }

                if (variables.has("booleans")) {
                    JsonObject booleans = variables.getAsJsonObject("booleans");
                    for (Map.Entry<String, JsonElement> entry : booleans.entrySet()) {
                        programPanel.booleans.put(entry.getKey(), entry.getValue().getAsBoolean());
                    }
                }

                if  (variables.has("floats")) {
                    JsonObject floats = variables.getAsJsonObject("floats");
                    for (Map.Entry<String, JsonElement> entry : floats.entrySet()) {
                        programPanel.floats.put(entry.getKey(), entry.getValue().getAsFloat());
                    }
                }
                programPanel.flowForge.controlPanel.updateVariableTree();
            }

            // Track nodes for connection reconstruction
            List<Node> newNodes = new ArrayList<>();
            Map<Integer, Node> idToNodeMap = new HashMap<>();

            if (programData.has("nodes")) {
                JsonArray nodesArray = programData.getAsJsonArray("nodes");

                // First pass: create all nodes without connections
                for (JsonElement nodeElement : nodesArray) {
                    JsonObject nodeObj = nodeElement.getAsJsonObject();
                    int id = nodeObj.get("id").getAsInt();
                    String type = nodeObj.get("type").getAsString();
                    String title = nodeObj.get("title").getAsString();
                    int x = nodeObj.get("x").getAsInt();
                    int y = nodeObj.get("y").getAsInt();

                    try {
                        boolean isHighlighted = nodeObj.get("isHighlighted").getAsBoolean();
                        boolean isCommented = nodeObj.get("isCommented").getAsBoolean();
                        boolean isMinimized = nodeObj.get("isMinimized").getAsBoolean();

                        String comment = null;

                        if (isCommented) comment = nodeObj.get("comment").getAsString();

                        JsonObject properties = nodeObj.getAsJsonObject("properties");

                        // Create the node with its specific type and properties
                        Node node = createNode(type, title, properties);

                        if (node != null) {
                            if (!node.isMinimized) node.restoreDimensions(isMinimized, x, y);
                            newNodes.add(node);
                            idToNodeMap.put(id, node);

                            if (isCommented) {
                                node.isCommented = true;
                                node.comment = comment;
                                node.setToolTipText(comment);
                            }
                            if (isHighlighted) {
                                node.isHighlighted = true;
                                node.restoreBorder();
                            }
                            if (isMinimized) {
                                node.isMinimized = true;
                            }
                        }
                    } catch (NullPointerException e) {
                        JOptionPane.showMessageDialog(null, "The file you opened was for a " + "\n" + "older version of FlowForge",
                                "Fatal file error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                }

                // Add all nodes to the program panel
                for (Node node : newNodes) {
                    programPanel.addNode(node);
                }

                // Second pass: establish connections between nodes
                for (JsonElement nodeElement : nodesArray) {
                    JsonObject nodeObj = nodeElement.getAsJsonObject();
                    int id = nodeObj.get("id").getAsInt();
                    Node currentNode = idToNodeMap.get(id);

                    if (currentNode == null) {
                        continue;
                    }

                    // Restore primary output connections
                    if (nodeObj.has("outputConnections")) {
                        JsonArray outputConnections = nodeObj.getAsJsonArray("outputConnections");
                        for (JsonElement connectionElement : outputConnections) {
                            int targetId = connectionElement.getAsInt();
                            Node targetNode = idToNodeMap.get(targetId);

                            if (targetNode != null) {
                                // Establish connection and update UI state
                                currentNode.connectTo(targetNode);
                                currentNode.outputButton.setSelected(true);
                                targetNode.inputButton.setSelected(true);
                            }
                        }
                    }

                    // Restore secondary (X) output connections
                    if (nodeObj.has("outputXConnections")) {
                        JsonArray outputXConnections = nodeObj.getAsJsonArray("outputXConnections");
                        for (JsonElement connectionElement : outputXConnections) {
                            int targetId = connectionElement.getAsInt();
                            Node targetNode = idToNodeMap.get(targetId);

                            if (targetNode != null) {
                                currentNode.connectToX(targetNode);
                                currentNode.outputXButton.setSelected(true);
                                targetNode.inputXButton.setSelected(true);
                            }
                        }
                    }

                    // Restore branch node-specific connections
                    if (currentNode instanceof BranchNode) {
                        BranchNode branchNode = (BranchNode) currentNode;

                        // Restore true branch connections
                        if (nodeObj.has("trueConnections")) {
                            JsonArray trueConnections = nodeObj.getAsJsonArray("trueConnections");
                            for (JsonElement connectionElement : trueConnections) {
                                int targetId = connectionElement.getAsInt();
                                Node targetNode = idToNodeMap.get(targetId);

                                if (targetNode != null) {
                                    branchNode.addTrueNode(targetNode);
                                }
                            }
                        }

                        // Restore false branch connections
                        if (nodeObj.has("falseConnections")) {
                            JsonArray falseConnections = nodeObj.getAsJsonArray("falseConnections");
                            for (JsonElement connectionElement : falseConnections) {
                                int targetId = connectionElement.getAsInt();
                                Node targetNode = idToNodeMap.get(targetId);

                                if (targetNode != null) {
                                    branchNode.addFalseNode(targetNode);
                                }
                            }
                        }
                    }
                }
            }

            programPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the startNode reference in ProgramPanel for execution
        for (Node node : programPanel.nodes) {
            if (node instanceof StartNode) {
                programPanel.startNode = (StartNode) node;
                break;
            }
        }
    }

    /**
     * Creates a node of the specified type with its properties.
     * Factory method to instantiate various node types with their specific configurations.
     * @param type The class name of the node to create
     * @param title The display title for the node
     * @param properties Node-specific configuration values
     * @return A properly configured node instance or null if creation fails
     */
    private Node createNode(String type, String title, JsonObject properties) {
        try {
            switch (type) {
                case "StartNode":
                    return new StartNode(title, programPanel);
                case "PrintNode":
                    PrintNode printNode = new PrintNode(title, programPanel);
                    if (properties.has("text")) {
                        printNode.textField.setText(properties.get("text").getAsString());
                    }
                    return printNode;

                case "BranchNode":
                    return new BranchNode(title, programPanel);

                case "InputNode":
                    InputNode inputNode = new InputNode(title, programPanel);
                    if (properties.has("inputString")) {
                        inputNode.inputValue = (properties.get("inputString").getAsString());
                        inputNode.inputField.setText(inputNode.inputValue);
                    }
                    return inputNode;

                case "DelayNode":
                    DelayNode delayNode = new DelayNode(title, programPanel);
                    if (properties.has("delay")) {
                        delayNode.delaySpinner.setValue(Integer.parseInt(properties.get("delay").getAsString()));
                    }
                    return delayNode;

                case "LoopNode":
                    LoopNode loopNode = new LoopNode(title, programPanel);
                    if (properties.has("loops")) {
                        loopNode.loopSpinner.setValue(Integer.parseInt(properties.get("loops").getAsString()));
                    }
                    if (properties.has("iterationValue")) {
                        loopNode.setIterationValue(properties.get("iterationValue").getAsInt());
                    }
                    return loopNode;

                case "ConditionalLoopNode":
                    return new ConditionalLoopNode(title, programPanel);

                case "EqualToNode":
                    return new EqualToNode(title, programPanel);

                case "GreaterThanNode":
                    return new GreaterThanNode(title, programPanel);

                case "LessThanNode":
                    return new LessThanNode(title, programPanel);

                case "GreaterThanOrEqualNode":
                    return new GreaterThanOrEqualNode(title, programPanel);

                case "LessThanOrEqualNode":
                    return new LessThanOrEqualNode(title, programPanel);

                case "NotEqualToNode":
                    return new NotEqualToNode(title, programPanel);

                case "LogicGateNode":
                    String gateType = properties.has("gateType") ?
                            properties.get("gateType").getAsString() : "AND";
                    return new LogicGateNode(title, programPanel, gateType);

                case "AddNode":
                    return new AddNode(title, programPanel);

                case "SubtractNode":
                    return new SubtractNode(title, programPanel);

                case "MultiplyNode":
                    return new MultiplyNode(title, programPanel);

                case "DivideNode" :
                    return new DivideNode(title, programPanel);

                case "ModulusNode" :
                    return new ModulusNode(title, programPanel);

                case "RandomNode" :
                    return new RandomNode(title, programPanel);

                case "RouteNode" :
                    return new RouteNode(title, programPanel);

                case "EvalNode" :
                    EvalNode evalNode = new EvalNode("EvalNode", programPanel);
                    if (properties.has("expression")) {
                        evalNode.expressionField.setText(properties.get("expression").getAsString());
                    }
                    return evalNode;

                case "IntegerNode":
                    String intName = properties.get("name").getAsString();
                    int intValue = properties.get("value").getAsInt();
                    IntegerNode intNode = new IntegerNode(intName, programPanel, intValue);
                    intNode.spinner.setValue(intValue); // Update the spinner with the loaded value
                    return intNode;

                case "StringNode":
                    String strName = properties.get("name").getAsString();
                    String strValue = properties.get("value").getAsString();
                    StringNode strNode = new StringNode(strName, programPanel, strValue);
                    strNode.textField.setText(strValue);
                    return strNode;

                case "BooleanNode":
                    String boolName = properties.get("name").getAsString();
                    boolean boolValue = properties.get("value").getAsBoolean();
                    BooleanNode boolNode = new BooleanNode(boolName, programPanel, boolValue);
                    boolNode.checkBox.setSelected(boolValue);
                    return boolNode;

                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Custom serializer for Node objects to prevent circular reference issues.
     * Simplifies node serialization by only including essential identification information.
     */
    private static class NodeSerializer implements JsonSerializer<Node> {
        @Override
        public JsonElement serialize(Node src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            // Only serialize basic identity information to avoid circular references
            result.addProperty("id", System.identityHashCode(src));
            result.addProperty("title", src.getTitle());
            return result;
        }
    }

    /**
     * Custom deserializer for Node objects.
     * Actual node creation is handled by the createNode method, this is just a placeholder.
     */
    private static class NodeDeserializer implements JsonDeserializer<Node> {
        @Override
        public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            // Actual node creation happens in createNode method
            return null;
        }
    }
}