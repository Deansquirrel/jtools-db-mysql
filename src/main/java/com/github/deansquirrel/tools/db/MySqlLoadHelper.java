package com.github.deansquirrel.tools.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/***
 * MySql配置加载帮助类
 */

@Component
public class MySqlLoadHelper {

    private static final Logger logger = LoggerFactory.getLogger(MySqlLoadHelper.class);

    private final IToolsDbHelper iToolsDbHelper;

    public MySqlLoadHelper(IToolsDbHelper iToolsDbHelper) {
        this.iToolsDbHelper = iToolsDbHelper;
    }

    /**
     * 加载MySQL数据库连接配置
     * @param connName 数据库连接名称
     * @param connStr 数据库连接配置，格式为 IP|端口|数据库|用户名|密码
     */
    public void addMySQLConn(String connName, String connStr) throws Exception {
        this.addMySQLConn(connName, connStr, null, null);
    }

    public void addMySQLConn(String connName, String connStr, Integer queryTimeout, Integer maxActive) throws Exception {
        if(connName == null || "".equals(connName) || connStr == null || "".equals(connStr)) {
            throw new Exception("连接地址或名称不允许为空");
        }
        if(iToolsDbHelper.isExistDataSource(connName)) {
            throw new Exception(MessageFormat.format("连接名称[{0}]已存在", connName));
        }
        String[] configList = connStr.split("\\|");
        if(configList.length < 5) {
            throw new Exception(MessageFormat.format("conn[{0}]配置异常, exp 5 act {1}",
                    connName,configList.length));
        }
        if("".equals(configList[1])) {
            configList[1] = "3306";
        }
        MySqlConnHelper conn = MySqlConnHelper.builder(connName)
                .setServer(configList[0])
                .setPort(Integer.valueOf(configList[1]))
                .setDbName(configList[2])
                .setUserName(configList[3])
                .setPassword(configList[4]);
        iToolsDbHelper.addDataSource(conn.getName(), conn.getDataSource(queryTimeout, maxActive));
    }

}
