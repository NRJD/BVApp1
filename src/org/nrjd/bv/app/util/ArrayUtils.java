/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ArrayUtils {
    private ArrayUtils() {
    }

    public static Set<String> convertToSet(String... values) {
        Set<String> set = null;
        if (values != null) {
            for (String value : values) {
                if (set == null) {
                    set = new HashSet<String>();
                }
                set.add(value);
            }
        }
        return set;
    }

    public static Set<String> convertToSet(List<String> values) {
        Set<String> set = null;
        if (values != null) {
            if (set == null) {
                set = new HashSet<String>();
            }
            set.addAll(values);
        }
        return set;
    }
}
