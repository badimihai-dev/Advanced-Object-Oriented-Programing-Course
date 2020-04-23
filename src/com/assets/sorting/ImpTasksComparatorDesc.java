package com.assets.sorting;

import com.assets.derivedtasks.General;

import java.util.Comparator;

public class ImpTasksComparatorDesc implements Comparator<General> {

    @Override
    public int compare(General t1, General t2){
        return (-1) * Integer.compare(t1.getImportancy(), t2.getImportancy());
    }
}