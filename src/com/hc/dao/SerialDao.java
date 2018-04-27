package com.hc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hc.util.CastUtil;

@Repository("com.hc.dao.SerialDao")
public class SerialDao implements ISerialDao {

	public SerialDao() {

	}

	@Autowired
	private SessionFactory sessionFactory;

	Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public List<Map> getSerialRuleList(String serialEk, String orgCd) {
		String sql = "SELECT A.* FROM hc_serial_rule A WHERE A.DATA_STATE='1'   ";
		sql += " AND A.serial_ek='" + serialEk + "'";
		sql += " AND A.org_cd='" + orgCd + "'";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = CastUtil.castList(sqlQuery.list());
		return list;
	}

	public long getSerialNum(final String serialEk, final String serialPrefix, final int cdNum, final String orgCd) {
		return getSession().doReturningWork(new ReturningWork<Long>() {
			public Long execute(Connection connection) throws SQLException {
				long serialNum = 0l;
				CallableStatement cstmt = null;
				try {
					cstmt = connection.prepareCall("{ call p_get_serial_cd(?,?,?,?,?) }");
					cstmt.setString(1, serialEk);
					cstmt.setString(2, serialPrefix);
					cstmt.setInt(3, cdNum);
					cstmt.setString(4, orgCd);
					cstmt.registerOutParameter(5, Types.INTEGER);
					cstmt.execute();
					serialNum = cstmt.getLong(5);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (cstmt != null) {
							cstmt.close();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return serialNum;
			}
		});

	}
}
