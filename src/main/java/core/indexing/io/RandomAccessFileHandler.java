package core.indexing.io;

import core.indexing.model.IndexItem;
import core.indexing.model.PostingFileItem;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RandomAccessFileHandler implements IIOHandler<String, LinkedList<PostingFileItem>>{

    private String filePath;

    private RandomAccessFile file;

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

    public boolean open() {
        try {
            file = new RandomAccessFile(filePath, "r");
            return true;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean close() {
        try {
            file.close();
            return true;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private Map<String, LinkedList<PostingFileItem>> readIndex() {
        Map<String, LinkedList<PostingFileItem>> content = new HashMap<>();
        try {
            file = new RandomAccessFile(filePath, "r");
            int byteCounter = 0;
            while(byteCounter <= file.length()) {
                IndexItem indexItem = readOneRecord(file);
                if(indexItem != null) {
                    content.put(indexItem.getTerm(), indexItem.getPostingFileItems());
                    byteCounter += indexItem.getSize();
                }
            }
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Map<String, Short> readIndexHeaders() {
        Map<String, Short> contentHeaders = new HashMap<>();
        try {
            file = new RandomAccessFile(filePath, "r");
            int byteCounter = 0;
            while(byteCounter < file.length()) {
                IndexItem indexItem = readOneRecordHeader(file);
                if(indexItem != null) {
                    contentHeaders.put(indexItem.getTerm(), indexItem.getDf());
                    byteCounter += indexItem.getSize();
                }
            }
            file.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contentHeaders;
    }

    public IndexItem readOneRecord() {
        try {
            if (file != null) {
                return readOneRecord(file);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new IndexItem();
    }

    private IndexItem readOneRecord(RandomAccessFile file) throws IOException {
        if(file.getFilePointer() < file.length() - Short.BYTES) {
            byte termByte;
            StringBuilder term = new StringBuilder();
            while ((termByte = file.readByte()) != ' ') {
                term.append((char)termByte);
            }

            short df = file.readShort();

            IndexItem indexItem = new IndexItem(term.toString(), df);

            //1 byte for space which is used as a separator after term in index file
            indexItem.incrementSize(1);

            for (int counter = 0; counter < df; counter++) {
                indexItem.addPostingEntry(file.readInt(), file.readInt());
            }
            return indexItem;
        }
        return null;
    }

    private IndexItem readOneRecordHeader(RandomAccessFile file) throws IOException {
        if(file.getFilePointer() < file.length()) {
            byte termByte;
            StringBuilder term = new StringBuilder();
            while ((termByte = file.readByte()) != ' ') {
                term.append((char)termByte);
            }

            short df = file.readShort();

            IndexItem indexItem = new IndexItem(term.toString(), df);
            indexItem.incrementSize(1);
            int delta = Integer.BYTES * 2 * df;
            indexItem.incrementSize(delta);
            file.seek(file.getFilePointer() + delta);
            return indexItem;
        }
        return null;
    }

    public void write(Map<String, LinkedList<PostingFileItem>> index) {
        try {
            file = new RandomAccessFile(filePath, "rw");
            if (index != null && index.size() > 0) {
                index.forEach(this::write);
            }
            file.close();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean write(String key, LinkedList<PostingFileItem> value) {
        writeTermAndPostingToRandomAccessFile(file, key, value);
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
