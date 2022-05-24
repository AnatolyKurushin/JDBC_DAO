package com.my_project.domain;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;

public class Department {

    private final BigInteger id;
    private final String name;
    private final String location;

    public Department(final BigInteger id,
                    final String name,
                      final String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public BigInteger getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Department that = (Department) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode() + name.hashCode() + location.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(" id: " + id)
                .append(" name: " + name)
                .append(" location:" + location)
                .append(" ").toString();
    }


}
