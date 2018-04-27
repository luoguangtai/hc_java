package com.hc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 树状菜单辅助类
 * @author 陈顺华
 */
public class TreeUtil {

	/**
	 * 寻找tree的根节点
	 * 
	 * @param list list
	 * @param idName id属性名称
	 * @param pidName pid属性的名称
	 * @return 根节点的id
	 */
	public static String getRootId(List list, String idName, String pidName) {
		List<Map> sourceList = BeanUtil.listBean2listMap(list);
		int i = 0, j = 0, size = sourceList.size();
		String root_id = null, pid = null, id = null;
		for (i = 0; i < size; i++) {
			pid = (String) (sourceList.get(i).get(pidName));
			for (j = 0; j < size; j++) {
				id = (String) (sourceList.get(j).get(idName));
				if (id.equals(pid)) {
					break;
				} else if (j == size - 1) {
					root_id = pid;
				}
			}
			if (root_id != null) {
				break;
			}
		}
		return root_id;
	}

	/**
	 * 根据List生成Tree
	 * @param list list
	 * @param root_id 根节点
	 * @return 树
	 */
	public static Map getTree(List list, String root_id) {
		return getTree(list, root_id, "id", "parent_id");
	}
	
	/**
	 * 根据List生成Tree
	 * @param list
	 * @param idName id属性名称
	 * @param idLength 值长度，传值new int[]{2,4,7},表示0101001这样的编码规则
	 * @return
	 */
	public static Map getTree(List list, String root_id, String idName, int[] idLength){
		List<Map> sourceList = BeanUtil.listBean2listMap(list);
		
		String id = null;
		String pidName = "___PID___";
		int idLen = 0;
		Map obj = null;
		for(int i=0; i<sourceList.size(); i++){
			obj = sourceList.get(i);
			id = (String)obj.get(idName);
			idLen = id.length();
			
			for(int j=0; j<idLength.length; j++){
				if(idLen==idLength[j] && j>0){
					obj.put(pidName, id.substring(0, idLength[j-1]));
					break;
				}
			}
		}
		
		return getTree(sourceList, root_id, idName, pidName);
	}

	/**
	 * 根据List生成Tree
	 * @param list
	 * @param root_id
	 * @param idName
	 * @param pidName
	 * @return
	 */
	public static Map getTree(List list, String root_id, String idName,
			String pidName) {
		List<Map> sourceList = BeanUtil.listBean2listMap(list);
		List<Map> rootMapList = new ArrayList<Map>();
		// 找根节点
		for (int i = 0; i < sourceList.size(); i++) {
			if (StringUtils.equals(root_id, (String)sourceList.get(i).get(idName))){
				rootMapList.add(sourceList.get(i));
			}
		}
		
		//如果没找到根节点,则直接通过root_id找子节点
		List<Map> treeList = new ArrayList<Map>();
		if(rootMapList.isEmpty()){
			treeList = getChildNodesList(sourceList, root_id, idName, pidName);
		}
		//找到根节点则循环找子节点
		else{
			for(int i=0; i<rootMapList.size(); i++){
				Map rootMap = rootMapList.get(i);
				List rootMapChildren = getChildNodesList(sourceList, (String)rootMap.get(idName), idName, pidName);
				if (rootMapChildren.size() > 0) {
					rootMap.put("children", rootMapChildren);
				} else {
					rootMap.put("leaf", true);
				}
				treeList.add(rootMap);
			}
		}
		
		//构造TREE返回
		Map rootMap = null;
		if(treeList.size()==1){
			rootMap = treeList.get(0);
		}
		else if(treeList.size()>1){
			rootMap = new HashMap();
			rootMap.put(idName, root_id);
			rootMap.put("children", treeList);
		}
		return rootMap;
	}

	private static List<Map> getChildNodesList(List<Map> list,
			String parent_id, String idName, String pidName) {
		List<Map> childNodes = new ArrayList<Map>();
		for (Map m : list) {
			if(StringUtils.equals(parent_id, (String)m.get(pidName))){
				List<Map> cl = getChildNodesList(list, (String) m.get(idName),
						idName, pidName);
				if (cl.size() > 0) {
					m.put("children", cl);
				} else {
					m.put("leaf", true);
				}
				childNodes.add(m);
			}
		}
		return childNodes;
	}

}
