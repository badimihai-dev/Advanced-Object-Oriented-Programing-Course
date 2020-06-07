package com.assets.services;

import com.assets.databaseconnection.DatabaseConnection;
import com.assets.derivedtasks.General;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceGeneral {
    private static final String SELECT_SINGLE = "SELECT * FROM `general` WHERE `index`=?";
    private static final String SELECT_ALL = "SELECT * FROM `general` ORDER BY `title`";
    private static final String SELECT_ALL_BY_IMP = "SELECT * FROM `general` ORDER BY `importancy` DESC";
    private static final String SELECT_ALL_UNCOMPLETED = "SELECT * FROM `general` WHERE `status` = 0 ORDER BY `title`";

    private static final String INSERT_TASK = "INSERT INTO `general` (`title`, `status`, `importancy`) VALUES (?, ?, ?)";

    private static final String UPDATE_TASK_TITLE = "UPDATE `general` SET `title` = ? WHERE `index` = ?";
    private static final String UPDATE_TASK_STATUS = "UPDATE `general` SET `status` = ? WHERE `index` = ?";
    private static final String UPDATE_TASK_IMPORTANCY = "UPDATE `general` SET `importancy` = ? WHERE `index` = ?";

    private static final String DELETE_TASK = "DELETE FROM `general` WHERE `index` = ?";

    private static ArrayList<General> get_all(String query){
        ArrayList<General> arr = new ArrayList<General>();
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    String title = result.getString("title");
                    boolean status = result.getInt("status") == 1;
                    int imp = result.getInt("importancy");

                    General obj = new General(title, status, imp);
                    obj.setIndex(result.getInt("index"));

                    arr.add(obj);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return arr;
    }

    public static General get_single(int index){
        General obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_SINGLE)) {
            statement.setInt(1,index);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                String title = result.getString("title");
                boolean status = result.getInt("status") == 1;
                int imp = result.getInt("importancy");

                obj = new General(title, status, imp);
                obj.setIndex(result.getInt("index"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public static ArrayList<General> get_all_tasks(){
        return get_all(SELECT_ALL);
    }

    public static ArrayList<General> get_all_tasks_by_imp(){
        return get_all(SELECT_ALL_BY_IMP);
    }

    public static ArrayList<General> get_all_tasks_uncompleted(){
        return get_all(SELECT_ALL_UNCOMPLETED);
    }

    public static General add_task(General task) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_TASK)) {
            statement.setString(1, task.getTitle());
            statement.setInt(2, task.getStatus() ? 1 : 0);
            statement.setInt(3, task.getImportancy());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("General Task added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("General Task: " + e.getMessage());
            return new General();
        }
        Audit.logAction("Inserted General Task");
        return task;
    }

    public static void update_task_title(int index, String title) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_TITLE)) {
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

        System.out.println("Couldn't find General Task.");
        Audit.logAction("Updated General Task");
    }

    public static void update_task_status(int index, boolean status) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_STATUS)) {
            statement.setInt(1, status?1:0);
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

        System.out.println("Couldn't find General Task.");
        Audit.logAction("Updated General Task");
    }

    public static void update_task_importancy(int index, int importancy) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_IMPORTANCY)) {
            statement.setInt(1, importancy);
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

        System.out.println("Couldn't find General Task.");
        Audit.logAction("Updated General Task");
    }

    public static void delete_task(int index) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(DELETE_TASK)) {
            statement.setInt(1, index);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("General Task successfully deleted!");
            }

        } catch (SQLException e) {
            System.out.println("General Task: " + e.getMessage());
            Audit.logAction("Deleted General Task");
        }
    }
}
