package com.my_project;

import com.my_project.domain.Department;
import com.my_project.domain.Employee;
import com.my_project.domain.FullName;
import com.my_project.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MyEmployeeDao {
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String MIDDLENAME = "middlename";
    private static final String POSITION = "position";
    private static final String HIREDATE = "hiredate";
    private static final String SALARY = "salary";
    private static final String MANAGER = "manager";
    private static final String DEPARTMENT = "department";
    private static final String SELECT_FROM_EMPLOYEE = "SELECT * FROM employee;";
    private static final String SELECT_FROM_EMPLOYEE_WHERE_ID = "SELECT * FROM employee WHERE id=?;";
    private static final String INSERT_INTO_EMPLOYEE_VALUES = "INSERT INTO employee " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_EMPLOYEE = "UPDATE employee SET firstname=?, lastname=?, middlename=?, " +
            "position=?, manager=?, hiredate=?, salary=?, department=? WHERE ID=?;";
    private static final String DELETE_FROM_EMPLOYEE_WHERE_ID = "DELETE FROM employee WHERE id=?;";
    private static final String SELECT_FROM_EMPLOYEE_WHERE_DEPARTMENT = "SELECT * FROM employee WHERE department=?;";
    private static final String SELECT_FROM_EMPLOYEE_WHERE_MANAGER = "SELECT * FROM employee WHERE manager=?;";
    private Connection connection;
    private PreparedStatement statement;
    private PreparedStatement stmtForUpdateOrInsert;
    private ResultSet resultSet;
    private ResultSet resSetForFindEmployee;

    public MyEmployeeDao() {
    }


    public List<Employee> getByDepartment(Department department) {
        List<Employee> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_EMPLOYEE_WHERE_DEPARTMENT);
            statement.setInt(1, department.getId().intValue());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getEmployee(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();
        }
        return result;
    }


    public List<Employee> getByManager(Employee employee) {
        List<Employee> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_EMPLOYEE_WHERE_MANAGER);
            statement.setInt(1, employee.getId().intValue());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getEmployee(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();
        }
        return result;
    }



    public Optional<Employee> getById(BigInteger id) {
        List<Employee> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_EMPLOYEE_WHERE_ID);
            statement.setInt(1, id.intValue());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getEmployee(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();
        }
        return result.stream().findAny();
    }


    public List<Employee> getAll() {
        List<Employee> result = new ArrayList<>();
        try {
            setStateForConnect(SELECT_FROM_EMPLOYEE);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getEmployee(resultSet));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        } finally {
            tryCloseAll();
        }
        return result;
    }


    public Employee save(Employee employee) {
        try {
            setStateForConnect(SELECT_FROM_EMPLOYEE_WHERE_ID);
            statement.setInt(1, employee.getId().intValue());
            resSetForFindEmployee = statement.executeQuery();
            if (resSetForFindEmployee.next()) {
                stmtForUpdateOrInsert = connection.prepareStatement(UPDATE_EMPLOYEE);
                setStateOfUpdate(employee, stmtForUpdateOrInsert);
            } else {
                stmtForUpdateOrInsert = connection.prepareStatement(INSERT_INTO_EMPLOYEE_VALUES);
                setStateOfInsert(employee, stmtForUpdateOrInsert);
            }
            stmtForUpdateOrInsert.executeUpdate();
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
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
        return employee;
    }


    public void delete(Employee employee) {
        try {
            setStateForConnect(DELETE_FROM_EMPLOYEE_WHERE_ID);
            statement.setInt(1, employee.getId().intValue());
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

    private void setStateOfUpdate(Employee employee, PreparedStatement statement) throws SQLException {
        int count = 1;
        statement.setString(count, employee.getFullName().getFirstName());
        ++count;
        statement.setString(count, employee.getFullName().getLastName());
        ++count;
        statement.setString(count, employee.getFullName().getMiddleName());
        ++count;
        statement.setString(count, employee.getPosition().toString());
        ++count;
        statement.setInt(count, employee.getManagerId().intValue());
        ++count;
        statement.setDate(count, java.sql.Date.valueOf(employee.getHired()));
        ++count;
        statement.setDouble(count, employee.getSalary().doubleValue());
        ++count;
        statement.setInt(count, employee.getDepartmentId().intValue());
        ++count;
        statement.setInt(count, employee.getId().intValue());
    }

    private void setStateOfInsert(Employee employee, PreparedStatement statement) throws SQLException {
        int count = 1;
        statement.setInt(count, employee.getId().intValue());
        ++count;
        statement.setString(count, employee.getFullName().getFirstName());
        ++count;
        statement.setString(count, employee.getFullName().getLastName());
        ++count;
        statement.setString(count, employee.getFullName().getMiddleName());
        ++count;
        statement.setString(count, employee.getPosition().toString());
        ++count;
        statement.setInt(count, employee.getManagerId().intValue());
        ++count;
        statement.setDate(count, java.sql.Date.valueOf(employee.getHired()));
        ++count;
        statement.setDouble(count, employee.getSalary().doubleValue());
        ++count;
        statement.setInt(count, employee.getDepartmentId().intValue());
    }

    private void setStateForConnect(String constant) throws SQLException {
        connection = DriverManager.getConnection("dbc:hsqldb:mem:myDb", "sa", "sa");
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        statement = connection.prepareStatement(constant);
    }

    public Employee getEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                new BigInteger(String.valueOf(resultSet.getInt(ID))),
                new FullName(resultSet.getString(FIRSTNAME),
                        resultSet.getString(LASTNAME),
                        resultSet.getString(MIDDLENAME)),
                Position.valueOf(resultSet.getString(POSITION)),
                resultSet.getDate(HIREDATE).toLocalDate(),
                BigDecimal.valueOf(resultSet.getDouble(SALARY)),
                new BigInteger(String.valueOf(resultSet.getInt(MANAGER))),
                new BigInteger(String.valueOf(resultSet.getInt(DEPARTMENT))));
    }


    private void tryCloseAll() {
        tryClose(connection);
        tryClose(statement);
        tryClose(stmtForUpdateOrInsert);
        tryClose(resultSet);
        tryClose(resSetForFindEmployee);
    }

    public void tryClose(AutoCloseable conOrStmtOrResSet) {
        if (conOrStmtOrResSet != null) {
            try {
                conOrStmtOrResSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}