package com.scp.view.index;


public  class ModuleItem implements java.io.Serializable {
	
	private java.lang.String id;
	
	private java.lang.String name;
	
	private java.lang.String icon;
	
	private java.lang.String url;
	
	private Module module;
	
	public ModuleItem() {
        
    }

    public ModuleItem(String id, String name, String icon, String url,
            Module module) {
        super();
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.url = url;
        this.module = module;
    }

   
	public java.lang.String getId() {
		return this.id;
	}

	
	public void setId(java.lang.String value) {
		this.id = value;
	}

	
	public java.lang.String getName() {
		return this.name;
	}

	
	public void setName(java.lang.String value) {
		this.name = value;
	}

	
	public java.lang.String getIcon() {
		return this.icon;
	}

	
	public void setIcon(java.lang.String value) {
		this.icon = value;
	}

	
	public java.lang.String getUrl() {
		return this.url;
	}

	
	public void setUrl(java.lang.String value) {
		this.url = value;
	}

	public Module getModule() {
		return this.module;
	}

	/** 
	 * @generated
	 */
	public void setModule(Module value) {
		this.module = value;
	}

}
