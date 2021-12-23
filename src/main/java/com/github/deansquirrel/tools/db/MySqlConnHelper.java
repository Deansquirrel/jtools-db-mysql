package com.github.deansquirrel.tools.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.lang.NonNull;

import java.text.MessageFormat;

public class MySqlConnHelper extends ABaseConn {

    private static final String connStr = "jdbc:mysql://{0}:{1}/{2}?connectTimeout=10000&serverTimezone=Asia/Shanghai&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=CONVERT_TO_NULL";

    private String server;
    private String dbName;
    private String userName;
    private String password;
    private Integer port;
    private String sConnStr;

    protected MySqlConnHelper(String name) {
        super(name);
    }

    public static MySqlConnHelper builder(@NonNull String name) {
        return new MySqlConnHelper(name);
    }

    public MySqlConnHelper setConnStr(String connStr) {
        this.sConnStr = connStr;
        return this;
    }

    public MySqlConnHelper setServer(String server) {
        this.server = server;
        return this;
    }

    public MySqlConnHelper setPort(Integer port) {
        this.port = port;
        return this;
    }

    public MySqlConnHelper setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public MySqlConnHelper setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public MySqlConnHelper setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public DruidDataSource getDataSource() {
        DruidDataSource ds = new DruidDataSource();
        if(this.getName() != null && "".equals(ds.getName().trim())) {
            ds.setName(this.getName().trim());
        }
        if(this.sConnStr == null || this.sConnStr.equals("")) {
            ds.setUrl(MessageFormat.format(MySqlConnHelper.connStr,
                    this.server, this.port == null ? Integer.toString(3306) : this.port.toString(), this.dbName));
        } else {
            ds.setUrl(this.sConnStr);
        }
        ds.setUsername(this.userName);
        ds.setPassword(this.password);
        this.setSourceAttributes(ds);
        return ds;
    }

    @Override
    public DruidDataSource getDataSource(Integer queryTimeout, Integer maxActive) {
        return super.getDataSource(queryTimeout, maxActive);
    }
}
