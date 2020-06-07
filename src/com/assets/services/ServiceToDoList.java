package com.assets.services;

import com.assets.TodoList;
import com.assets.databaseconnection.DatabaseConnection;
import com.assets.derivedtasks.General;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceToDoList {
    // LIST QUERIES
    private static final String SELECT_SINGLE = "SELECT * FROM `lists` LEFT JOIN `l-tasks` USING(`index`) WHERE `index` = ?";
    private static final String SELECT_ALL = "SELECT * FROM `lists` LEFT JOIN `l-tasks` USING(`index`)";
    private static final String INSERT_LIST = "INSERT INTO `lists` (`title`) VALUES (?)";
    private static final String UPDATE_LIST_TITLE = "UPDATE `lists` SET `title` = ? WHERE `index` = ?";
    private static final String DELETE_LIST = "DELETE `l-tasks`, `lists` FROM `l-tasks` INNER JOIN `lists` USING(`index`) WHERE `index` = ?";

    //SUBTASKS QUERIES
    private static final String INSERT_TASK = "INSERT INTO `l-tasks` (`index`, `title`, `status`, `importancy`) VALUES (?, ?, ?, ?)";
    private static final String SELECT_TASK = "SELECT * FROM `l-tasks` WHERE `index`=?";
    private static final String UPDATE_TASK_TITLE = "UPDATE `l-tasks` SET `title` = ? WHERE `taskid` = ?";
    private static final String UPDATE_TASK_STATUS = "UPDATE `l-tasks` SET `status` = ? WHERE `taskid` = ?";
    private static final String UPDATE_TASK_IMPORTANCY = "UPDATE `l-tasks` SET `importancy` = ? WHERE `taskid` = ?";
    private static final String DELETE_TASK = "DELETE FROM `l-tasks` WHERE `taskid` = ?";

    //LISTS FUNCTIONS
    public static TodoList get_single(int index){
        TodoList obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_SINGLE)) {
            statement.setInt(1,index);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                ResultSetMetaData rsmd = result.getMetaData();
                obj = new TodoList(result.getString(2));
                if(rsmd.getColumnCount() > 2 && result.getString(3) != null){
                    obj.addTasks(result.getString(3), result.getInt("status") == 1, result.getInt("importancy"), result.getInt("taskid"));
                }
                while (result.next()) {
                    obj.addTasks(result.getString(3), result.getInt("status") == 1, result.getInt("importancy"), result.getInt("taskid"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public static ArrayList<TodoList> get_all(){
        ArrayList<TodoList> arr = new ArrayList<TodoList>();
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_ALL)) {
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                ResultSetMetaData rsmd = result.getMetaData();

                int prevIndex = result.getInt("index");
                TodoList obj = new TodoList(result.getString(2));
                obj.setIndex(result.getInt(1));

                if(rsmd.getColumnCount() > 2 && result.getString(3) != null){
                    obj.addTasks(result.getString(3), result.getInt("status") == 1, result.getInt("importancy"), result.getInt("taskid"));
                }
                while (result.next()) {
                    ResultSetMetaData rsmdIn = result.getMetaData();

                    if(result.getInt("index") != prevIndex){
                        arr.add(obj);
                        obj = new TodoList(result.getString(2));
                        obj.setIndex(result.getInt(1));
                        prevIndex = result.getInt("index");
                    }
                    if(rsmdIn.getColumnCount() > 2 && result.getString(3) != null){
                        obj.addTasks(result.getString(3), result.getInt("status") == 1, result.getInt("importancy"), result.getInt("taskid"));
                    }
                }
                arr.add(obj);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return arr;
    }

    public static TodoList add_list(TodoList list) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_LIST)) {
            statement.setString(1, list.getListName());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("List added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("TODO List: " + e.getMessage());
            return new TodoList();
        }
        Audit.logAction("Inserted list");
        return list;
    }

    public static void update_list_title(int index, String title) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_LIST_TITLE)) {
            statement.setString(1, title);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Unavailable change!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Couldn't find List Task.");
        Audit.logAction("Updated list");
    }

    public static void delete_list(int index) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(DELETE_LIST)) {
            statement.setInt(1, index);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("List successfully deleted!");
            }

        } catch (SQLException e) {
            System.out.println("TODO List: " + e.getMessage());
        }

        Audit.logAction("Deleted list");
    }
    //SUBTASKS FUNCTIONS
    public static General get_task(int index){
        General obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_TASK)) {
            statement.setInt(1,index);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                String title = result.getString("title");
                boolean status = result.getInt("status") == 1;
                int imp = result.getInt("importancy");

                obj = new General(title, status, imp);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public static void add_task(int index, String title, boolean status, int imp) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_TASK)) {
            statement.setInt(1, index);
            statement.setString(2, title);
            statement.setInt(3, status?1:0);
            statement.setInt(4, imp);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Subtask added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("TODO List: " + e.getMessage());
        }

        Audit.logAction("Inserted list subtask");
    }

    public static void update_task_title(int index, String title) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_TITLE)) {
            statement.setString(1, title);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update successfuly posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Couldn't find List Task.");
        Audit.logAction("Updated list subtask");
    }

    public static void update_task_status(int index, boolean status) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_STATUS)) {
            statement.setInt(1, status?1:0);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update successfuly posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Couldn't find List Task.");
        Audit.logAction("Updated list subtask");
    }

    public static void update_task_importancy(int index, int importancy) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_IMPORTANCY)) {
            statement.setInt(1, importancy);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Update successfuly posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Couldn't find List Task.");
        Audit.logAction("Updated list subtask");
    }

    public static void delete_task(int index) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(DELETE_TASK)) {
            statement.setInt(1, index);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("List Task successfully deleted!");
            }

        } catch (SQLException e) {
            System.out.println("List Task: " + e.getMessage());
        }
        Audit.logAction("Deleted list subtask");
    }
}
