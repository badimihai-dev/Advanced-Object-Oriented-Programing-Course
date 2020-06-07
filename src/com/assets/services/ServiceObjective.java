package com.assets.services;

import com.assets.TodoList;
import com.assets.databaseconnection.DatabaseConnection;
import com.assets.derivedtasks.Objective;
import com.assets.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public class ServiceObjective {

    // LIST QUERIES
    private static final String SELECT_SINGLE = "SELECT * FROM `objective` LEFT JOIN `o-tasks` USING(`index`) WHERE `index` = ?";


    private static final String INSERT_OBJECTIVE = "INSERT INTO `objective` (`title`, `status`, `date`) VALUES (?, ?, ?)";

    private static final String UPDATE_OBJECTIVE_TITLE = "UPDATE `objective` SET `title` = ? WHERE `index` = ?";
    private static final String UPDATE_OBJECTIVE_STATUS = "UPDATE `objective` SET `status` = ? WHERE `index` = ?";
    private static final String UPDATE_OBJECTIVE_DATE = "UPDATE `objective` SET `date` = ? WHERE `index` = ?";

    private static final String DELETE_OBJECTIVE = "DELETE `o-tasks`, `objective` FROM `o-tasks` INNER JOIN `objective` USING(`index`) WHERE `index` = ?";

    //SUBTASKS QUERIES
    private static final String INSERT_TASK = "INSERT INTO `o-tasks` (`index`, `title`, `status`) VALUES (?, ?, ?)";
    private static final String SELECT_TASK = "SELECT * FROM `o-tasks` WHERE `index`=?";
    private static final String UPDATE_TASK_TITLE = "UPDATE `o-tasks` SET `title` = ? WHERE `taskid` = ?";
    private static final String UPDATE_TASK_STATUS = "UPDATE `o-tasks` SET `status` = ? WHERE `taskid` = ?";
    private static final String DELETE_TASK = "DELETE FROM `o-tasks` WHERE `taskid` = ?";

    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        return new java.sql.Date(uDate.getTime());
    }

    public static Objective add_objective(Objective objective) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_OBJECTIVE)) {
            statement.setString(1, objective.getTitle());
            statement.setInt(2, 0);
            statement.setDate(3, convertUtilToSql(objective.getHappeningDate()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Objective added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("Objective: " + e.getMessage());
            return new Objective();
        }
        Audit.logAction("Inserted Objective Task");
        return objective;
    }

    public static Objective get_single(int index){
        Objective obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_SINGLE)) {
            statement.setInt(1,index);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                ResultSetMetaData rsmd = result.getMetaData();
                obj = new Objective(result.getString(2), result.getDate(4));
                obj.setIndex(result.getInt(1));
                if(rsmd.getColumnCount() > 4 && result.getString(5) != null){
                    obj.addSubtask(result.getString(5), result.getInt(6) == 1, result.getInt("taskid"));
                }

                while (result.next()) {
                    obj.addSubtask(result.getString(5), result.getInt(6) == 1, result.getInt("taskid"));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

    public static void update_objective_title(int index, String title) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_OBJECTIVE_TITLE)) {
            statement.setString(1, title);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Change successfully posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        Audit.logAction("Updated Objective Task");
        System.out.println("Couldn't find Objective Task.");
    }

    public static void update_objective_status(int index, boolean status) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_OBJECTIVE_STATUS)) {
            statement.setInt(1, status?1:0);
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Change successfully posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        Audit.logAction("Updated Objective Task");
        System.out.println("Couldn't find Objective Task.");
    }

    public static void update_objective_date(int index, Date date) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(UPDATE_OBJECTIVE_DATE)) {
            statement.setDate(1, convertUtilToSql(date));
            statement.setInt(2, index);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Change successfully posted!");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        Audit.logAction("Updated Objective Task");
        System.out.println("Couldn't find Objective Task.");
    }

    public static void delete_objective(int index) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(DELETE_OBJECTIVE)) {
            statement.setInt(1, index);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Objective successfully deleted!");
            }

        } catch (SQLException e) {
            System.out.println("Objective Task: " + e.getMessage());
        }
        Audit.logAction("Deleted Objective Task");
    }

    //SUBTASKS FUNCTIONS
    public static void add_task(int index, String title, boolean status) throws Exception {
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(INSERT_TASK)) {
            statement.setInt(1, index);
            statement.setString(2, title);
            statement.setInt(3, status?1:0);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Subtask added successfully!");
            }
        }
        catch (SQLException e) {
            System.out.println("Objective: " + e.getMessage());
        }
        Audit.logAction("Inserted Objective Subask");
    }

    public static Task get_task(int index){
        Task obj = null;
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(SELECT_TASK)) {
            statement.setInt(1,index);
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                String title = result.getString("title");
                boolean status = result.getInt("status") == 1;

                obj = new Task(title, status);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
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
        Audit.logAction("Updated Objective Subask");
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
        Audit.logAction("Updated Objective Subask");
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
        Audit.logAction("Deleted Objective Subask");
    }
}
