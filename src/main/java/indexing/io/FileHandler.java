package indexing.io;


import filter.IFilter;
import utils.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    if(doFormatInput) {
                        line = Formatter.formatInput(line);
                    }
                    if (filter == null) {
                        builder.append(line.trim());
                    } else {
                        line = filter.filter(line.trim());
                        if(!line.equals("")) {
                            builder.append(filter.filter(line.trim()));
                        }
                    }
                    builder.append(" ");
                }
                reader.close();
                content.put(file.getName(), builder.toString());
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
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if(doFormatInput) {
                    line = Formatter.formatInput(line);
                }
                if (filter == null) {
                    builder.append(line.trim());
                } else {
                    line = filter.filter(line.trim());
                    if(!line.equals("")) {
                        builder.append(filter.filter(line.trim()));
                    }
                }
                builder.append(" ");
            }
            reader.close();
            content.put(file.getName(), builder.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
