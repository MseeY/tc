package com.mitenotc.dao.base;

import java.io.Serializable;
import java.util.List;
/**
 * 所有的实体操作的接口的公共部分
 * @author mitenotc
 *
 * @param <M>
 */
public interface DAO<M> {
	/**
	 * 增加
	 * @param m
	 * @return
	 */
	long insert(M m);
	/**
	 * 修改
	 * @param m
	 * @return
	 */
	int update(M m);
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	int delete(Serializable id);//String long int----JPA
	/**
	 * 查询
	 * @return
	 */
	List<M> findAll();
	
	public M getInstance();
}


