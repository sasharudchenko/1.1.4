package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() throws SQLException {
        try (Statement statement = Util.getConnection().createStatement()){
            Util.getConnection().setAutoCommit(false);
            statement.executeUpdate("create table if not exists user (id integer not null auto_increment, name varchar(45), lastname varchar(45), age int," +
                    "primary key (id))");
            System.out.println("Таблица создана");

        } catch (SQLException e) {
            Util.getConnection().rollback();
        }

    }

    public void dropUsersTable() throws SQLException {
        try (Statement statement = Util.getConnection().createStatement()) {
            Util.getConnection().setAutoCommit(false);
            statement.executeUpdate("drop table if exists user");

            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            Util.getConnection().rollback();
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement("insert into user values " +
                "(id, ?, ?, ?)")) {
           // preparedStatement.setString(1, id);
            Util.getConnection().setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
        } catch (SQLException e) {
            Util.getConnection().rollback();
//            System.out.println("Не удалось сохранить данные");
        }
    }

    public void removeUserById(long id) throws SQLException {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement("delete from user where id = ?")) {
            Util.getConnection().setAutoCommit(false);
            preparedStatement.setLong(1,  id);
            preparedStatement.execute();
        } catch (SQLException e) {
            Util.getConnection().rollback();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Statement statement = Util.getConnection().createStatement()) {
            Util.getConnection().setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery("select * from user");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge((resultSet.getByte("age")));
                list.add(user);

            }
        } catch (SQLException e) {
            Util.getConnection().rollback();
        }
        list.forEach(System.out::println);

        return list;
    }

    public void cleanUsersTable() throws SQLException {
        try (Statement statement = Util.getConnection().createStatement()) {
            Util.getConnection().setAutoCommit(false);
            statement.executeUpdate("delete  from user");
            System.out.println("Таблица очищена");;

        } catch (SQLException e) {
            Util.getConnection().rollback();
        }

    }
}
