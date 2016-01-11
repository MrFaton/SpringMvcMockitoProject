package com.nixsolutions.ponarin.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@Component
@ContextConfiguration(locations = ("classpath:/app-context-test.xml"))
public class DbTestHelper {
    protected static final String SCHEMA = "TRAINEESHIP_DB";

    @Autowired
    private DataSource dataSource;

    public void fill(String dataSetPath) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            IDataSet dataSet = new FlatXmlDataSetBuilder()
                    .build(Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(getIDBConnection(connection),
                    dataSet);
            connection.commit();
        } catch (SQLException sqlEx) {
            rollBackConnection(connection, sqlEx);
            throw sqlEx;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public ITable getTableFromSchema(String tableName) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            IDataSet dataSet = getIDBConnection(connection).createDataSet();
            connection.commit();
            return dataSet.getTable(tableName);
        } catch (SQLException sqlEx) {
            rollBackConnection(connection, sqlEx);
            throw sqlEx;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public ITable getTableFromFile(String tableName, String xmlFilePath)
            throws Exception {
        FlatXmlDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(xmlFilePath));
        return dataSet.getTable(tableName);
    }

    private IDatabaseConnection getIDBConnection(Connection connection)
            throws Exception {
        DatabaseConnection databaseConnection = new DatabaseConnection(
                connection, SCHEMA);
        DatabaseConfig databaseConfig = databaseConnection.getConfig();
        databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                new H2DataTypeFactory());
        return databaseConnection;
    }

    private void rollBackConnection(Connection connection, Exception ex)
            throws SQLException {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.addSuppressed(ex);
                throw e;
            }
        }
    }
}