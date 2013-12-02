package org.virtual.sr.utils;

import org.virtualrepository.Property;

public class Constants {

    public static final String ownerName = "owner";
    public static final String pseudoNS = "s:r/";

    public static Property ownerProperty(String value) {
        return new Property(ownerName, value);
    }

}
