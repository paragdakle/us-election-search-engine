package indexing.model;

import java.util.LinkedList;

public class IndexItem {

    private String term;

    private short df;

    private LinkedList<PostingFileItem> postingFileItems;

    private int size;

    public IndexItem() {
        this.term = "";
        postingFileItems = null;
        postingFileItems = new LinkedList<>();
    }

    public IndexItem(String term, short df) {
        this.term = term;
        this.df = df;
        this.size = term.length() + Short.BYTES;
    }

    public String getTerm() {
        return term;
    }

    public short getDf() {
        return df;
    }

    public int getSize() {
        return size;
    }

    public LinkedList<PostingFileItem> getPostingFileItems() {
        return postingFileItems;
    }

    public void setPostingFileItems(LinkedList<PostingFileItem> postingFileItems) {
        this.postingFileItems = postingFileItems;
    }

    public void addPostingEntry(int docId, int frequency) {
        if(postingFileItems == null) {
            postingFileItems = new LinkedList<>();
        }
        postingFileItems.add(new PostingFileItem(docId, frequency));
        size += (Integer.BYTES * 2);
    }
}
