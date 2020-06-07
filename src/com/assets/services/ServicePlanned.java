package com.assets.services;

import com.assets.databaseconnection.DatabaseConnection;
import com.assets.derivedtasks.General;
import com.assets.derivedtasks.Planned;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ServicePlanned {
    private static final String SELECT_SINGLE = "SELECT * FROM `planned` WHERE `index` = ?";
    private static final String SELECT_ALL = "SELECT * FROM `planned` ORDER BY `title`";
    private static final String SELECT_ALL_BY_DATE = "SELECT * FROM `planned` ORDER BY `date`";
    private static final String SELECT_ALL_UNCOMPLETED = "SELECT * FROM `planned` WHERE `status` = 0 ORDER BY `date`";

    private static final String INSERT_TASK = "INSERT INTO `planned` (`title`, `status`, `date`) VALUES (?, ?, ?)";

    private static final String UPDATE_TASK_TITLE = "UPDATE `planned` SET `title` = ? WHERE `index` = ?";
    private static final String UPDATE_TASK_STATUS = "UPDATE `planned` SET `status` = ? WHERE `index` = ?";
    private static final String UPDATE_TASK_DATE = "UPDATE `planned` SET `date` = ? WHERE `index` = ?";

    private static final String DELETE_TASK = "DELETE FROM `planned` WHERE `index` = ?";

    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        return new java.sql.Date(uDate.getTime());
    }

    private static ArrayList<Planned> get_all(String query){
        ArrayList<Planned> arr = new ArrayList<Planned>();
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    String title = result.getString("title");
                    boolean status = result.getInt("status") == 1;
                    Date date = result.getDate("date");

                    Planned obj = new Planned(title, status, date);
                    obj.setIndex(result.getInt("index"));

                    arr.add(obj);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return arr;
    }

    public static Planned get_single(int index){
        Planned obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_SINGLE)) {
            statement.setInt(1, index);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                String title = result.getString("title");
                boolean status = result.getInt("status") == 1;
                Date date = result.getDate("date");

                obj = new Planned(title, status, date);
            }
        } catch (Exception e) {
            System.out.println("asga");
            System.out.println(e);
        }
        return obj;
    }

    public static ArrayList<Planned> get_all_tasks(){
        return get_all(SELECT_ALL);
    }

    public static ArrayList<Planned> get_all_tasks_by_imp(){
        return get_all(SELECT_ALL_BY_DATE);
    }

    public static ArrayList<Planned> get_all_tasks_uncompleted(){
        return get_all(SELECT_ALL_UNCOMPLETED);
    }

    public static Planned add_task(Planned task) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_TASK)) {
            statement.setString(1, task.getTitle());
            statement.setInt(2, task.getStatus() ? 1 : 0);
            statement.setDate(3, convertUtilToSql(task.getHappeningDate()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Planned Task added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("General Task: " + e.getMessage());
            return new Planned();
        }
        Audit.logAction("Inserted Planned Task");
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

        Audit.logAction("Updated Planned Task");
        System.out.println("Couldn't find General Task.");
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

        Audit.logAction("Updated Planned Task");
        System.out.println("Couldn't find General Task.");
    }

    public static void update_task_date(int index, Date date) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_TASK_DATE)) {
            statement.setDate(1, convertUtilToSql(date));
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

        Audit.logAction("Updated Planned Task");
        System.out.println("Couldn't find General Task.");
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
        }
        Audit.logAction("Deleted Planned Task");
    }
}
