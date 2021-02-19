package com.roeticvampire.UniversalClipboard;

import java.util.ArrayList;
import java.util.List;

public class ClipboardDataset {
    private int count;
    private List<String> clipboardItems;
    public ClipboardDataset(int count, List<String> clipboardItems) {
        this.count = count;
        this.clipboardItems = clipboardItems;
    }
    public ClipboardDataset() {
        clipboardItems= new ArrayList<>();
        count=0;
    }
    public ClipboardDataset(List<String> clipboardItems) {
        this.clipboardItems = clipboardItems;
        this.count=clipboardItems.size();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getClipboardItems() {
        return clipboardItems;
    }

   public void updateData(String new_item){
        for(int i=count-2;i>=0;i--)
            clipboardItems.set(i+1,clipboardItems.get(i));
        clipboardItems.set(0,new_item);
   }
//Okay we need a counter, and a list of Strings
    //we also need functions to update the object with a new entry
public void addData(String new_item){
        clipboardItems.add(0,new_item);
        count++;
}


}
