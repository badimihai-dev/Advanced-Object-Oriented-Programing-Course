package com.assets.store;

import com.assets.Task;
import com.assets.TodoList;
import com.assets.derivedtasks.General;
import com.assets.derivedtasks.Objective;
import com.assets.derivedtasks.Planned;

import java.util.ArrayList;
import java.util.List;

public abstract class Store {
    protected static String[] storeTypes = {"General", "Objective", "Planned", "TodoList"};

    protected static List<Task> allTasks = new ArrayList<Task>();
    protected static List<TodoList> allList = new ArrayList<TodoList>();
}
