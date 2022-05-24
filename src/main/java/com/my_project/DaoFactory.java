package com.my_project;

public class DaoFactory {
    public MyEmployeeDao employeeDAO() {
        return new MyEmployeeDao();
    }

    public MyDepartmentDao departmentDAO() {
        return new MyDepartmentDao();
    }
}
