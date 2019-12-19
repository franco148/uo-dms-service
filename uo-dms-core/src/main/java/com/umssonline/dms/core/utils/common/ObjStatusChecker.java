package com.umssonline.dms.core.utils.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class ObjStatusChecker {

    private ObjStatusChecker() {

    }

    /**
     * This method returns true if the collection is null or is empty.
     *
     * @param collection
     *            the collection
     * @return true | false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the map is null or is empty.
     *
     * @param map
     *            the map
     * @return true | false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns false if the map is null or is empty.
     *
     * @param map
     *            the map
     * @return true | false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * This method returns true if the list is null or is empty.
     *
     * @param list
     *            the list
     * @return true | false
     */
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns false if the list is null or is empty.
     *
     * @param list
     *            the list
     * @return true | false
     */
    public static boolean isNotEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * This method returns true if the object is null.
     *
     * @param object
     *            the object
     * @return true | false
     */
    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the object is not null.
     *
     * @param object
     *            the object
     * @return true | false
     */
    public static boolean isNotNull(Object object) {
        if (object == null) {
            return false;
        }
        return true;
    }

    /**
     * This method returns true if the input array is null or its length is
     * zero.
     *
     * @param array
     *            the array
     * @return true | false
     */
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input string is null or its length is
     * zero.
     *
     * @param string
     *            the string
     * @return true | false
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input string is null or its length is
     * zero.
     *
     * @param string
     *            the string
     * @return true | false
     */
    public static boolean isNotEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        return true;
    }
}
