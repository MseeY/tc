package com.mitenotc.test;

import java.util.List;
import android.test.AndroidTestCase;
import com.mitenotc.bean.HallItemBean;
import com.mitenotc.dao.impl.AddressDaoImpl;
import com.mitenotc.dao.impl.HallItemDaoImpl;

public class DaoTest extends AndroidTestCase{

	public void HallItemDaoTest(){
		HallItemDaoImpl dao = new HallItemDaoImpl(getContext());
		HallItemBean bean = new HallItemBean();
		bean.setDesc("hall item description");
		bean.setIconPath("hall item icon path");
		bean.setIssue("hall item issue");
		bean.setPrizeIcons("icon1,icon2");
		bean.setTitle("hall item title");
		dao.insert(bean);
		
		////System.out.println("0-------------");
		
		List<HallItemBean> findAll = dao.findAll();
		for (HallItemBean hallItemBean : findAll) {
			String desc = hallItemBean.getDesc();
			////System.out.println("desc = "+desc);
			
		}
	}
	
	public void AddressDaoText(){
		AddressDaoImpl impl = new AddressDaoImpl();
		List<String> list = impl.findProvince();
		for (int i = 0; i < list.size(); i++) {
			////System.out.println(list.get(i));
		}
	}
	
	public void AddressDaoText2(){
		AddressDaoImpl impl = new AddressDaoImpl();
		List<String> list = impl.findCityByName("上海");
		for (int i = 0; i < list.size(); i++) {
			
			////System.out.println(list.get(i));
			
		}
	}
}
