package flowforge.core;

import com.google.gson.*;


public class DataManager {
    private ProgramPanel programPanel;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DataManager(ProgramPanel programPanel) {
        this.programPanel = programPanel;
    }

    public void saveProgram(String filePath) {

    }

    public void loadProgram(String filePath) {

    }

}