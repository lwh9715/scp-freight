package com.scp.view.index;

import java.util.HashSet;
import java.util.Set;



public class Module implements java.io.Serializable {

	private Long id;
	
	private Long pid;

	private String name;

	private String icon;
	
	private Set<Module> module = new HashSet<Module>();
	
	private Set<ModuleItem> moduleItems = new HashSet<ModuleItem>();
	
	
	public Module() {
		super();
	}
	
	public Module(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Module(Long id, String name , Set<Module> module) {
		super();
		this.id = id;
		this.name = name;
		this.module = module;
	}

	
	public Module(Long id, String name, String icon) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	
	public Module(Long id, String name, String icon,
			Set<ModuleItem> moduleItems) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.moduleItems = moduleItems;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long value) {
		this.id = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String value) {
		this.icon = value;
	}

	public Set<ModuleItem> getModuleItems() {
		return this.moduleItems;
	}

	public void setModuleItems(
			Set<ModuleItem> value) {
		this.moduleItems = value;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Set<Module> getModule() {
		return module;
	}

	public void setModule(Set<Module> module) {
		this.module = module;
	}
	
	
}
