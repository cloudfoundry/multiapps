package com.sap.cloud.lm.sl.persistence.dialects;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgreSQLDatabaseDialect extends DefaultDatabaseDialect implements DatabaseDialect {
    
    @Override
    public String getSequenceNextValueSyntax(String sequenceName) {
        return "nextval('" + sequenceName + "')";
    }
    
    @Override
    public void setBigInteger(PreparedStatement ps, int index, BigInteger bi) throws SQLException {
        ps.setLong(index, bi.longValue());
    }

}
