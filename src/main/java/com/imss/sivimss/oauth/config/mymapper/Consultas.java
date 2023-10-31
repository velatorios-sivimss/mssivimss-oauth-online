package com.imss.sivimss.oauth.config.mymapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

@Repository
public interface Consultas {

	static class PureSqlProvider {
		public String sql(String sql) {
			return sql;
		}
		
	}

	@SelectProvider(type = PureSqlProvider.class, method = "sql")
	public List<Map<String, Object>> selectHashMap(String sql);

	@InsertProvider(type = PureSqlProvider.class, method = "sql")
	@SelectKey(statement="select LAST_INSERT_ID() as id", keyProperty = "out.id", before=false, resultType=int.class)
	public int insert(String sql, @Param("out") IdDto out);
	
	@UpdateProvider(type = PureSqlProvider.class, method = "sql")
	public Boolean actualizar(String sql);
}
