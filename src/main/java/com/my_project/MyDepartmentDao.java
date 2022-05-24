package com.my_project;

import com.my_project.domain.Department;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyDepartmentDao {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String SELECT_FROM_DEPARTMENT = "SELECT * FROM department;";
    private static final String SELECT_FROM_DEPARTMENT_WHERE_ID = "SELECT * FROM department WHERE id=?;";
    private static final String INSERT_INTO_DEPARTMENT_VALUES = "INSERT INTO department VALUES (?, ?, ?);";
    private static final String UPDATE_DEPARTMENT_SET_NAME_LOCATION_WHERE_ID = "UPDATE department SET name=?, " +
            "location=? WHERE id=?;";
    private static final String DELETE_FROM_DEPARTMENT_WHERE_ID = "DELETE FROM department WHERE id = ?;";
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private PreparedStatement stmtForFindDepartment;
    private ResultSet resSetForFindDepartment;

    public MyDepartmentDao() {
    }


    public Optional<Department> getById(BigInteger id) {
        List<Department> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_DEPARTMENT_WHERE_ID);
            statement.setInt(1, id.intValue());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getDepartment(resultSet));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();
        }
        return result.stream().findAny();
    }



    public List<Department> getAll() {
        List<Department> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_DEPARTMENT);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getDepartment(resultSet));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();

        }
        return result;
    }


    public Department save(Department department) {
        try {
            setStateForConnect(SELECT_FROM_DEPARTMENT_WHERE_ID);
            statement.setInt(1, department.getId().intValue());
            resSetForFindDepartment = statement.executeQuery();

            if (resSetForFindDepartment.next()) {
                stmtForFindDepartment = connection.prepareStatement(UPDATE_DEPARTMENT_SET_NAME_LOCATION_WHERE_ID);
                setStateOfUpdate(department, stmtForFindDepartment);
            } else {
                stmtForFindDepartment = connection.prepareStatement(INSERT_INTO_DEPARTMENT_VALUES);
                setStateOfInsert(department, stmtForFindDepartment);
            }
            stmtForFindDepartment.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            try {
                exception.printStackTrace();
                connection.rollback();
                throw new RuntimeException(exception);
            } catch (SQLException except) {
                except.printStackTrace();
                throw new RuntimeException(exception);
            }
        } finally {
            tryCloseAll();
        }
        return department;
    }

    private void setStateOfUpdate(Department department, PreparedStatement statement) throws SQLException {
        int count = 1;
        statement.setString(count, department.getName());
        ++count;
        statement.setString(count, department.getLocation());
        ++count;
        statement.setInt(count, department.getId().intValue());
    }

    private void setStateOfInsert(Department department, PreparedStatement statement) throws SQLException {
        int count = 1;
        statement.setInt(count, department.getId().intValue());
        ++count;
        statement.setString(count, department.getName());
        ++count;
        statement.setString(count, department.getLocation());
    }



    public void delete(Department department) {
        try {
            setStateForConnect(DELETE_FROM_DEPARTMENT_WHERE_ID);
            statement.setInt(1, department.getId().intValue());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            try {
                exception.printStackTrace();
                connection.rollback();
                throw new RuntimeException(exception);
            } catch (SQLException except) {
                except.printStackTrace();
                throw new RuntimeException(exception);
            }
        } finally {
            tryCloseAll();
        }
    }

    private void setStateForConnect(String constant) throws SQLException {
        connection =  DriverManager.getConnection("dbc:hsqldb:mem:myDb", "sa", "sa");
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        statement = connection.prepareStatement(constant);
    }

    public Department getDepartment(ResultSet resultSet) throws SQLException {
        return new Department(
                new BigInteger(String.valueOf(resultSet.getInt(ID))),
                resultSet.getString(NAME),
                resultSet.getString(LOCATION));
    }

    private void tryCloseAll() {
        tryClose(connection);
        tryClose(statement);
        tryClose(stmtForFindDepartment);
        tryClose(resultSet);
        tryClose(resSetForFindDepartment);
    }

    public void tryClose(AutoCloseable conOrStmtOrResSet) {
        if (conOrStmtOrResSet != null) {
            try {
                conOrStmtOrResSet.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
