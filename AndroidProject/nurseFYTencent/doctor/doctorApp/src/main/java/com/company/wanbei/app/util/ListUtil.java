package com.company.wanbei.app.util;

import com.company.wanbei.app.bean.DoctorBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtil {
	
	public static  void sortList(List<DoctorBean> list){
		List<DoctorBean> _List = new ArrayList<DoctorBean>();
		Collections.sort(list, new PinyinComparator());
		DoctorBean dataBean = new DoctorBean(getFirstCharacter(list.get(0).getItem_en()), "","","","","","","", DoctorBean.TYPE_CHARACTER);
		String currentCharacter = getFirstCharacter(list.get(0).getItem_en());
		_List.add(dataBean);
		_List.add(list.get(0));
		for(int i=1;i<list.size();i++){
			if(getFirstCharacter(list.get(i).getItem_en()).compareTo(currentCharacter)!=0){
				currentCharacter = getFirstCharacter(list.get(i).getItem_en());
				dataBean = new DoctorBean(currentCharacter, "","","","","","","", DoctorBean.TYPE_CHARACTER);
				_List.add(dataBean);
			}
			_List.add(list.get(i));
		}
		list.clear();
		for(DoctorBean bean:_List){
			list.add(bean);
		}
	}
	
	public static String getFirstCharacter(String str){
		return str.substring(0, 1);
	}
	
	
}
