package com.my_project.domain;


public class FullName {
    private final String firstName;
    private final String lastName;
    private final String middleName;

    public FullName(final String firstName, final String lastName, final String middleName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FullName fullName = (FullName) o;
        return firstName.equals(fullName.firstName) &&
                lastName.equals(fullName.lastName) &&
                middleName.equals(fullName.middleName);
    }

    @Override
    public int hashCode() {
        return 31 * firstName.hashCode() + lastName.hashCode() + middleName.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(" firstName: " + firstName)
                .append(" lastName: " + lastName)
                .append(" middleName: " + middleName)
                .append(" ").toString();
    }
}
