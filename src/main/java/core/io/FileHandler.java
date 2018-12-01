package core.io;


import core.filter.IFilter;
import core.ranking.model.Graph;
import core.ranking.model.Node;
import core.utils.Formatter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHandler implements IIOHandler<String, String> {

    private String filePath;

    private IFilter filter;

    private boolean doFormatInput = false;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    public FileHandler(String filePath, IFilter filter) {
        this.filePath = filePath;
        this.filter = filter;
    }

    public FileHandler(String filePath, IFilter filter, boolean doFormatInput) {
        this.filePath = filePath;
        this.filter = filter;
        this.doFormatInput = doFormatInput;
    }

    @Override
    public Map<String, String> read() {
        if(filePath.equals("")) {
            return null;
        }
        File file = new File(filePath);
        if(file.isDirectory()) {
            return readDirFiles();
        }
        else {
            return readFile();
        }
    }

    @Override
    public boolean write(String key, String value) {
        return false;
    }

    private Map<String, String> readDirFiles() {
        Map<String, String> content = new HashMap<String, String>();
        try {
            File dir = new File(filePath);
            for(File file: dir.listFiles()) {
                if(file.isFile()) {
                    content.put(file.getName(), readFileContents(file));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private Map<String, String> readFile() {
        Map<String, String> content = new HashMap<>();
        try {
            File file = new File(filePath);
            content.put(file.getName().replace(".txt", ""), readFileContents(file));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public String readFileContents(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if(doFormatInput) {
                line = Formatter.formatInput(line);
            }
            builder.append(line.trim()).append(" ");
        }
        reader.close();
        if(filter == null) {
            return builder.toString();
        }
        else {
            return filter.filter(builder.toString());
        }
    }

    public List<String> readFileContent() {
        List<String> content = null;
        if(!filePath.isEmpty()) {
            File file = new File(filePath);
            if(file.isFile()) {
                content = readFileContent(file);
            }
        }
        return content;
    }

    public List<String> readFileContent(File file) {
        List<String> content = null;
        try {
            content = new ArrayList<>();
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
            bufferedReader.close();
            reader.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

    public void writeGraphToFile(Graph graph, boolean doSimpleWrite) {
        if(graph != null && graph.getNodes() != null) {
            Map<String, Node> nodes = graph.getNodes();
            File file = new File(filePath);
            if(!file.isDirectory()) {
                try {
                    FileWriter writer = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    for(String key: nodes.keySet()) {
                        Node node = nodes.get(key);
                        if(doSimpleWrite) {
                            bufferedWriter.write(key + " " + node.getWeight() + "\n");
                        }
                        else if(node.getType() == node.TYPE_HUB) {
                            bufferedWriter.write(key + " " + node.getWeight() + " 0\n");
                        }
                        else {
                            bufferedWriter.write(key + " 0 " + node.getWeight() + "\n");
                        }
                    }
                    bufferedWriter.close();
                    writer.close();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
