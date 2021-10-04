package model.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	@Override
	public void insert(Department obj) {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.prepareStatement("insert into department(Name) "
					+ "values(?) ", 
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getName());
			int rowsAffect = stmt.executeUpdate();
			if(rowsAffect > 0) {
				rs = stmt.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}else {
					throw new DbException("Ocorreram erros na inserção!");
				}
			}
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Department obj) {
		
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement("update department "
					+ "set Name = ? "
					+ "where id = ? ",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getName());
			stmt.setInt(2, obj.getId());
			
			stmt.executeUpdate();
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement stmt = null;
		try {
			
			stmt = conn.prepareStatement("delete from department "
					+ "where id = ? ");
			stmt.setInt(1, id);
			
			stmt.executeUpdate();
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			stmt = conn.prepareStatement("SELECT * FROM department "
					+ "WHERE id = ? ");
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				Department dep = instanteDepartment(rs);
				return dep;
			}else {
				return null;
			}
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);
		}
	}

	private Department instanteDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}
	@Override
	public List<Department> findAll() {
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			
			stmt = conn.prepareStatement("SELECT * FROM department ");
			rs = stmt.executeQuery();
			List<Department> list = new ArrayList<>();
			
			while(rs.next()) {
				Department dep = instanteDepartment(rs);
				list.add(dep);
			}
			return list;
			
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(stmt);
			DB.closeResultSet(rs);;
		}
	}

}
