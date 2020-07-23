/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.manager.db.handler;

import com.zetyun.streamtau.runtime.ScriptFormat;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScriptFormatHandler implements TypeHandler<ScriptFormat> {
    @Override
    public void setParameter(
        PreparedStatement ps,
        int index,
        ScriptFormat format,
        JdbcType jdbcType
    ) throws SQLException {
        ps.setString(index, format.getValue());
    }

    @Override
    public ScriptFormat getResult(ResultSet rs, String key) throws SQLException {
        return ScriptFormat.fromString(rs.getString(key));
    }

    @Override
    public ScriptFormat getResult(ResultSet resultSet, int index) throws SQLException {
        return ScriptFormat.fromString(resultSet.getString(index));
    }

    @Override
    public ScriptFormat getResult(CallableStatement cs, int index) throws SQLException {
        return ScriptFormat.fromString(cs.getString(index));
    }
}
