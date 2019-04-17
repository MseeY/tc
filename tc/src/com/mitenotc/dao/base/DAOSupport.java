package com.mitenotc.dao.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mitenotc.dao.DBHelper;
import com.mitenotc.dao.annotation.Column;
import com.mitenotc.dao.annotation.ID;
import com.mitenotc.dao.annotation.TableName;

/**
 * 所有的实体操作实现类基类
 * @author mitenotc
 * 
 * @param <M>
 */
public abstract class DAOSupport<M> implements DAO<M> {
	private static final String TAG = "DAOSupport";
	protected SQLiteDatabase database;
	protected Context context;
	protected DBHelper helper;
	private String id_key;

	public DAOSupport(Context context) {
		super();
		this.context = context;
		helper = new DBHelper(context);

		database = helper.getWritableDatabase();
	}

	@Override
	public long insert(M m) {// 如果主键已经存在,则不是 添加 数据而是 更改数据
		String id_val = getId(m);
		if (id_val != null) {
			List<M> list = findByCondition(id_key + "=?",new String[] { id_val }, null);
			if (list != null) {
				return update(m);
			}
		}

		ContentValues values = new ContentValues();// map

		setValues(m, values);// 参数的位置：数据源，设置的目标

		return database.insert(getTableName(), null, values);
	}

	@Override
	public int update(M m) {
		ContentValues values = new ContentValues();
		setValues(m, values);

		return database.update(getTableName(), values,
				DBHelper.TABLE_ID + "=?", new String[] { getId(m) });
	}

	@Override
	public int delete(Serializable id) {
		return database.delete(getTableName(), DBHelper.TABLE_ID + "=?",
				new String[] { id.toString() });
	}

	@Override
	public List<M> findAll() {
		List<M> result;// List<M> result;
		Cursor query = database.query(getTableName(), null, null, null, null,
				null, null);
		if (query != null) {
			result = new ArrayList<M>();
			while (query.moveToNext()) {
				// News news = new News();// M m=new M();
				M m = getInstance();
				// 设置一个实体的数据
				setField(query, m);

				result.add(m);
			}
			query.close();// 可以开启多少个Cursor
			return result;
		}
		return null;
	}

	public List<M> findByCondition(String selection, String[] selectionArgs,
			String orderBy) {
		return findByCondition(null, selection, selectionArgs, null, null,
				orderBy);
	}

	public List<M> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		List<M> result;// List<M> result;
		Cursor query = database.query(getTableName(), columns, selection,
				selectionArgs, groupBy, having, orderBy);
		if (query != null) {
			result = new ArrayList<M>();
			while (query.moveToNext()) {
				// News news = new News();// M m=new M();
				M m = getInstance();
				// 设置一个实体的数据
				setField(query, m);

				result.add(m);
			}
			query.close();// 可以开启多少个Cursor
			return result;
		}
		return null;
	}

	// 问题一：表名的获取:当前操作的是哪个实体
	public String getTableName() {
		M m = getInstance();
		TableName tableName = m.getClass().getAnnotation(TableName.class);
		if (tableName != null) {
			return tableName.value();
		}
		return "";
	}

	// 问题二：如何设置实体中的数据到数据库的表中对应的列上
	public void setValues(M m, ContentValues values) {
		// values.put(DBHelper.TABLE_NEWS_TITLE, news.getTitle());
		// 数据库表中列的信息与实体中字段的对应关系

		Field[] fields = m.getClass().getDeclaredFields();// 10个20个
		for (Field item : fields) {
			item.setAccessible(true);// 暴力反射
			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				String key = column.value();
				// 遗留问题：关于主键+自增，如果是自增的，需要交由系统区设置数据
				ID id = item.getAnnotation(ID.class);
				if (id != null) {
					if (id.autoincrement()) {
						// 需要交由系统区设置数据
					} else {
						try {
							String value = item.get(m).toString();// m.title
							values.put(key, value);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						String value = item.get(m).toString();// m.title
						values.put(key, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

			}
		}

	}

	// 问题三：实体中主键信息的获取
	private String getId(M m) {
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);
			ID id = item.getAnnotation(ID.class);
			if (id != null) {
				id_key = id.value();
				try {
					return item.get(m).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	// 问题四：数据库表中列里面放置的信息，如何设置到实体，找好对应关系
	private void setField(Cursor query, M m) {
		// int index = query.getColumnIndex(DBHelper.TABLE_NEWS_TITLE);
		// String title = query.getString(index);
		//
		// news.setTitle(title);
		// 获取到m中所有的field，找到有Column注解
		Field[] fields = m.getClass().getDeclaredFields();
		for (Field item : fields) {
			item.setAccessible(true);

			Column column = item.getAnnotation(Column.class);
			if (column != null) {
				int index = query.getColumnIndex(column.value());
				String info = query.getString(index);

				try {
					// 判断一下类型，如果不是String
					if (item.getType() == int.class) {
						item.set(m, Integer.parseInt(info));
					} else if (item.getType() == long.class) {
						item.set(m, Long.parseLong(info));
					} else {
						item.set(m, info);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}

	}

	// 问题五：实体对象的创建
	public M getInstance() {
		// ①获取到实际运行子类
		Class clazz = super.getClass();
		Log.i(TAG, clazz.getName());
		// ②获取实际运行子类的父类，支持泛型的父类
		// Class superclass = clazz.getSuperclass();
		// Log.i(TAG, superclass.getName());
		// DAOSupport<cn.ithm.dbhm21.dao.domain.News>
		Type genericSuperclass = clazz.getGenericSuperclass();
		// Log.i(TAG, ((Class) genericSuperclass).getName());

		if (genericSuperclass instanceof ParameterizedType) {
			Type[] typeArguments = ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments();// 泛型中所有的参数类型
			// ③获取到泛型中的参数信息
			// ④创建实例
			Type type = typeArguments[0];
			try {
				return (M) ((Class) type).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}


