package com.scp.view.base;

import java.util.ArrayList;
import java.util.List;

import com.scp.util.StrUtils;

public class TreeNode{
	private String name;
	private String id;
	private String url;
	private String icon;
	
	private String check;
	
	private Long pid = 0L;
	
	private List<TreeNode> childrens;
	
	private int level = 1;

	public TreeNode(String name, String id ,String url , String icon , String check) {
		super();
		this.name = name;
		this.id = id;
		this.url = url;
		this.icon = icon;
		this.check = check;
		childrens = new ArrayList<TreeNode>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<TreeNode> getChildrens() {
		return childrens;
	}

	public void addChildrens(TreeNode children) {
		childrens.add(children);
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getIcon() {
		if(StrUtils.isNull(icon))return "../main/img/expprocess.gif";
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}