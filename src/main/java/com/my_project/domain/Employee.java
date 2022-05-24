package com.my_project.domain;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Employee {
    private final BigInteger id;
    private final FullName fullName;
    private final Position position;
    private final LocalDate hired;
    private final BigDecimal salary;
    private final BigInteger managerId;
    private final BigInteger departmentId;


    public Employee(final BigInteger id,
                    final FullName fullName,
                    final Position position,
                    final LocalDate hired,
                    final BigDecimal salary,
                    final BigInteger managerId,
                    final BigInteger departmentId) {
        this.id = id;
        this.fullName = fullName;
        this.position = position;
        this.hired = hired;
        this.salary = salary.setScale(5, RoundingMode.HALF_UP);
        this.managerId = managerId;
        this.departmentId = departmentId;
    }

    public BigInteger getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Position getPosition() {
        return position;
    }

    public LocalDate getHired() {
        return hired;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public BigInteger getManagerId() {
        return managerId;
    }

    public BigInteger getDepartmentId() {
        return departmentId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Employee employee = (Employee) o;
        return id.equals(employee.id) &&
                fullName.equals(employee.fullName) &&
                position == employee.position &&
                hired.equals(employee.hired) &&
                salary.equals(employee.salary) &&
                managerId.equals(employee.managerId) &&
                departmentId.equals(employee.departmentId);
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + fullName.hashCode() + position.hashCode() + hired.hashCode()
                + salary.hashCode() + managerId.hashCode() + departmentId.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(" id: " + id)
                .append(" fullName :" + fullName)
                .append(" position :" + position)
                .append(" hired :" + hired)
                .append(" salary: " + salary)
                .append(" managerId: " + managerId)
                .append(" departmentId: " + departmentId)
                .append(" ").toString();
    }


}
