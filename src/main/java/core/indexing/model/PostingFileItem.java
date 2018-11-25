package core.indexing.model;

import java.util.BitSet;

public class PostingFileItem {

    private Integer docId;

    private int termFrequency;

    private BitSet code;

    public PostingFileItem() {
        this.docId = -1;
        this.termFrequency = 0;
    }

    public PostingFileItem(int docId) {
        this.docId = docId;
        this.termFrequency = 1;
    }

    PostingFileItem(int docId, int termFrequency) {
        this.docId = docId;
        this.termFrequency = termFrequency;
    }

    public Integer getDocId() {
        return this.docId;
    }

    public int getTermFrequency() {
        return this.termFrequency;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public void incrementTermFrequency() {
        this.termFrequency++;
    }

    public void computeGap(int previousDocId) {
        this.docId -= previousDocId;
    }

    public void setCode(boolean[] booleanCode) {
        code = new BitSet(booleanCode.length);
        for(int counter = 0; counter < booleanCode.length; counter++) {
            code.set(counter, booleanCode[counter]);
        }
    }

    public BitSet getCode() {
        return this.code;
    }

    @Override
    public int hashCode() {
        return this.docId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof PostingFileItem)) {
            return false;
        }

        PostingFileItem item = (PostingFileItem) o;

        return this.docId.equals(item.docId);
    }
}
