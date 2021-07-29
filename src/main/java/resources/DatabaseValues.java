package main.java.resources;

public class DatabaseValues {

    public static final String DERBY_REPAY_DB_CREATE_TRUE = "jdbc:derby:repayDB;create=true";
    public static final String DERBY_REPAY_DB_CREATE_FALSE = "jdbc:derby:repayDB;create=false";

    public static final String getDebtsTableCreateString = "CREATE TABLE debts (\n" +
            "\tdebtName VARCHAR(20) NOT NULL,\n" +
            "\tmonthly_payment DOUBLE PRECISION NOT NULL,\n" +
            "\tapr DOUBLE PRECISION NOT NULL,\n" +
            "\tbalance DOUBLE PRECISION NOT NULL,\n" +
            "    username VARCHAR(20) NOT NULL\n" +
            ")";

    public static final String getGetDebtTableCreateString = "CREATE TABLE debt (\n" +
            "\tdebtName VARCHAR(20) NOT NULL,\n" +
            "\tmonthly_payment DOUBLE PRECISION NOT NULL,\n" +
            "\tapr DOUBLE PRECISION NOT NULL,\n" +
            "\tbalance DOUBLE PRECISION NOT NULL,\n" +
            "    username VARCHAR(20) NOT NULL,\n" +
            "    PRIMARY KEY (debtname, username)\n" +
            ")";

    public static final String getGetBillsTableCreateString = "CREATE TABLE bills (\n" +
            "\tdebtName VARCHAR(20) NOT NULL,\n" +
            "\tmonthly_payment DOUBLE PRECISION NOT NULL,\n" +
            "    username VARCHAR(20) NOT NULL\n" +
            ")";

    public static final String getGetGetBillTableCreateString = "CREATE TABLE bill (\n" +
            "\tdebtName VARCHAR(20) NOT NULL,\n" +
            "\tmonthly_payment DOUBLE PRECISION NOT NULL,\n" +
            "    username VARCHAR(20) NOT NULL,\n" +
            "    PRIMARY KEY (debtname, username)\n" +
            ")";
}
