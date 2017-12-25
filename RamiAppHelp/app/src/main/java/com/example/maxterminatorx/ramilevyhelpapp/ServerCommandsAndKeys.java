package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 04-Dec-17.
 */

public class ServerCommandsAndKeys {

    //server keys
    public static final int SERVER_PORT = 3000;

    public static final String MY_SERVER_IP = "192.168.1.128";

    public static final String SERVER_IP = "192.168.50.67";

    //server commands
    public static final byte CMD_CHECK_IS_PARENT_EXIST = 0X0A;
    public static final byte CMD_ADD_NEW_PARENT = 0X0B;

    public static final byte CMD_CHECK_IS_CHILD_EXIST = 0X1A;
    public static final byte CMD_ADD_NEW_CHILD = 0X1B;

    public static final byte CMD_GET_CHILD_LIST = 0X2A;

}
