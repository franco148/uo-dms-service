package com.umssonline.dms.core.dal.jcrutils;

/**
 * Created by franco.arratia@umssonline.com
 */
public class QueryConstants {

    //Query Parameters' name
    public static final String PARAMETER_NAME_IS_DELETED = "IsDeleted";

    //Query constants
    public static final String SELECT_QUERY = "select * from [%1$s] as b";
    public static final String SELECT_ALL_QUERY = "select * from [%1$s] as b where b.[%2$s]='%3$s'";
    public static final String SELECT_NODE_QUERY = "select * from [%1$s] as b where b.[%2$s]='%3$s'";

    //Query Attachor
    public static final String ADD_QUERY_CONDITION = " where b.[%1$s]='%2$s'";
    public static final String ADD_QUERY_AND_CONDITION = " and b.[%1$s]='%2$s'";
    public static final String ADD_QUERY_OR_CONDITION = " or b.[%1$s]='%2$s'";

    //Query parameters values
    public static final String PARAMETER_VALUE_IS_NOT_DELETED = "N";
    public static final String PARAMETER_VALUE_IS_DELETED = "Y";
}
