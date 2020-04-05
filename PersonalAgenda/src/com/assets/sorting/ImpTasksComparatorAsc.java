package com.assets.sorting;

import com.assets.Task;
import com.assets.derivedtasks.General;

import java.util.Comparator;

public class ImpTasksComparatorAsc implements Comparator<General> {

    @Override
    public int compare(General t1, General t2){
        return Integer.compare(t1.getImportancy(), t2.getImportancy());
    }
}


