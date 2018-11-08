package indexing.io;

import indexing.model.PostingFileItem;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RandomAccessFileHandler implements IIOHandler<String, LinkedList<PostingFileItem>>{

    private String filePath;

    public RandomAccessFileHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Map<String, LinkedList<PostingFileItem>> read() {
        if(filePath.equals("")) {
            return null;
        }
        return readIndex();
    }

    private Map<String, LinkedList<PostingFileItem>> readIndex() {
        Map<String, LinkedList<PostingFileItem>> content = new HashMap<>();
        try {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            int byteCounter = 0;
            while(byteCounter <= file.length()) {
                byte termByte;
                StringBuilder term = new StringBuilder();
                while ((termByte = file.readByte()) != ' ') {
                    term.append(termByte);
                    byteCounter++;
                }
                byteCounter++;

                short df = file.readShort();
                byteCounter += Short.BYTES;

                LinkedList<PostingFileItem> postingList = new LinkedList<>();
                for(int counter = 0; counter < df; counter++) {
                    PostingFileItem postingFileItem = new PostingFileItem();
                    postingFileItem.setDocId(file.readInt());
                    postingFileItem.setTermFrequency(file.readInt());
                    postingList.add(postingFileItem);
                    byteCounter += (Integer.BYTES * 2);
                }
                content.put(term.toString(), postingList);
            }
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public boolean write(Map<String, LinkedList<PostingFileItem>> index) {
        if(index != null && index.size() > 0) {
            index.forEach(this::write);
        }
        return false;
    }

    @Override
    public boolean write(String key, LinkedList<PostingFileItem> value) {
        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(filePath, "rw");
            writeTermAndPostingToRandomAccessFile(randomAccessFile, key, value);
            randomAccessFile.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private void writeTermAndPostingToRandomAccessFile(RandomAccessFile randomAccessFile, String term, LinkedList<PostingFileItem> postingList) {
        try {
            randomAccessFile.write(term.getBytes());
            randomAccessFile.write(' ');
            randomAccessFile.writeShort(postingList.size());
            for(PostingFileItem postingFileItem: postingList) {
                randomAccessFile.writeInt(postingFileItem.getDocId());
                randomAccessFile.writeInt(postingFileItem.getTermFrequency());
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
