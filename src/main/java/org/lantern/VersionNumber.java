package org.lantern;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using=VersionNumberSerializer.class)
@JsonDeserialize(using=VersionNumberDeserializer.class)
public class VersionNumber implements Serializable, Comparable<Object> {
    private static final long serialVersionUID = -9030368123761098914L;

    private final int[] components;

    public VersionNumber(String number) {
        String[] parts = number.split("\\.");
        components = new int[parts.length];
        for (int i = 0; i < parts.length; ++i) {
            components[i] = Integer.parseInt(parts[i]);
        }
    }

    public int getMajor() {
        return getComponentOrZero(0);
    }

    public int getMinor() {
        return getComponentOrZero(1);
    }

    public int getPatch() {
        return getComponentOrZero(2);
    }

    public int getComponentOrZero(int component) {
        if (components.length > component) {
            return components[component];
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < components.length - 1; ++i) {
            out += components[i];
            out += ".";
        }
        out += components[components.length - 1];
        return out;
    }

    @Override
    public int compareTo(Object other) {
        VersionNumber vn;
        if (other instanceof String) {
            vn = new VersionNumber((String) other);
        } else if (other instanceof VersionNumber) {
            vn = (VersionNumber) other;
        } else {
            throw new RuntimeException("Cannot compare " + other
                    + " to VersionNumber");
        }
        int mostComponents = Math.max(components.length, vn.components.length);
        for (int i = 0; i < mostComponents; ++i) {
            int diff = getComponentOrZero(i) - vn.getComponentOrZero(i);
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof VersionNumber)) {
            return false;
        }
        VersionNumber vnOther = (VersionNumber) other;

        return compareTo(vnOther) == 0;
    }

    @Override
    public int hashCode() {
        int x = 0;
        for (int i = 0; i < 5; ++i) {
            x *= 100;
            x += getComponentOrZero(i);
        }
        return x;
    }
}
