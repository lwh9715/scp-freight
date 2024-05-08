Ext.form.CKEditor = function (config) {
	this.config = config;
	Ext.form.CKEditor.superclass.constructor.call(this, config);
};
//Ext.form.CKEditor.CKEDITOR_CONFIG = "/pages/public/ckeditor/config.js";
//Ext.form.CKEditor.CKEDITOR_TOOLBAR = "Default";
Ext.extend ( Ext.form.CKEditor, Ext.form.TextArea, {
	onRender : function (ct, position) {
		if (!this.el) {
			this.defaultAutoCreate = {
				tag : "textarea",
				autocomplete : "off"
			};
		}
		Ext.form.TextArea.superclass.onRender.call (this, ct, position);
		var config = {
			//customConfig : Ext.form.CKEditor.CKEDITOR_CONFIG,
			//toolbar : Ext.form.CKEditor.CKEDITOR_TOOLBAR
		};
		Ext.apply (config, this.config.CKConfig);
		var editor = CKEDITOR.replace (this.id, config);
	},
	onDestroy : function() {
		if (CKEDITOR.instances[this.id]) {
			delete CKEDITOR.instances[this.id];
		}
	},
	setValue : function (value) {
		var _val = "";
		if (value) {
			//delete the <script...>...</script> section.
			_val = value.replace(/\<script[^>]*>[\s\S]*?\<\/[^>]*script>/ig, '').replace(/\<noscript[^>]*>[\s\S]*?\<\/[^>]*noscript>/ig, '');
		}
		Ext.form.TextArea.superclass.setValue.apply (this, [_val]);
		CKEDITOR.instances[this.id].setData (_val);
	},
	getValue : function() {
		CKEDITOR.instances[this.id].updateElement();
		var value = CKEDITOR.instances[this.id].getData();
		if (value) {
			//delete the <script...>...</script> section.
			value = value.replace(/\<script[^>]*>[\s\S]*?\<\/[^>]*script>/ig, '').replace(/\<noscript[^>]*>[\s\S]*?\<\/[^>]*noscript>/ig, '');
		}
		Ext.form.TextArea.superclass.setValue.apply(this, [value]);
		return Ext.form.TextArea.superclass.getValue.apply(this);
	},
	getRawValue : function() {
		CKEDITOR.instances[this.id].updateElement();
		return Ext.form.TextArea.superclass.getRawValue(this);
	},
	isDirty : function() {
		if (this.disabled) {
			return false;
		}
		var value = String(this.getValue()).replace(/\s/g, '');
		value = (value == "<br />" || value == "<br/>" ? "" : value);
		this.originalValue = this.originalValue || "";
		this.originalValue = this.originalValue.replace(/\s/g, '');
		return String(value) !== String(this.originalValue) ? String(value) !== "<p>" + String(this.originalValue) + "</p>" : false;
	}
} );
Ext.reg ("ckeditor", Ext.form.CKEditor);

// Add methods to the BasicForm that clears the isDirty flag to return "false" again
Ext.override ( Ext.form.BasicForm, {
	/**
	 * clear the value of all items in BasicForm and set originalValue to ''
	 * @param {Object} o
	 */
	clearValues : function(o) {
		o = o || this;
		o.items.each(function(f) {
			if(f.items) {
				this.clearValues(f);
			} else if(f.setValue) {
				f.setValue('');
				// ckeditor needs being treated specially, or an error will appear in IE
				if (f.getXType() == "ckeditor") {
					f.originalValue = '';
				} else if (f.getValue) {
					f.originalValue = f.getValue();
				}
			}
		}, this);
		this.clearInvalid();
	},
	/**
	 * clear isDirty flag of all items of BasicForm
	 * @param {Object} o
	 *
	 * reference: http://www.extjs.com/forum/showthread.php?t=40568
	 */
	clearDirty : function(o) {
		o = o || this;
		o.items.each(function(f){
			if(f.items) {
				this.clearDirty(f);
			} else if(typeof (f.originalValue) != "undefined" && f.getValue) {
				f.originalValue = f.getValue();
			}
		}, this);
	},
	setValues : function(values) {
		if(Ext.isArray(values)) {
			for (var i = 0, len = values.length; i < len; i = i + 1) {
				var v = values[i];
				var f = this.findField(v.id);
				if(f) {
					f.setValue(v.value);
					//ckeditor is special
					if (f.getXType == "ckeditor") {
						f.originalValue = v.value;
					}
					//checkboxgroup.originalValue won't be set, or it may cause a bug when reset
					else if (this.trackResetOnLoad && typeof(f.originalValue) != "undefined" && f.getValue) {
						f.originalValue = f.getValue();
					}
				}
			}
		} else {// object hash
			var field, id;
			for (id in values) {
				if (typeof values[id] != "function" && (field == this.findField(id))) {
					field.setValue(values[id]);
					if (this.trackResetOnLoad) {
						if (field.getXType() == "ckeditor") {
							field.originalValue = values[id];
						} else if (typeof(field.originalValue) != "undefined" && field.getValue) {
							field.originalValue = field.getValue();
						}
					}
				}
			}
		}
		return this;
	},
	findField : function(id) {
		var field = this.items.get(id);
		if(!field) {
			this.items.each(function(f){
				if(f.isXType("radiogroup") || f.isXType("checkboxgroup")) {
					if(f.isXType("radiogroup"))
						f.unitedValue = true;
					f.items.each(function(c){
						if(c.isFormField && (c.dataIndex == id || c.id == id || c.getName() == id)) {
							field = f.unitedValue ? f : c;
							if (typeof(f.trackResetOnLoad) == "undefined")
								f.trackResetOnLoad = this.trackResetOnLoad;
							return false;
						}
					}, this);
				}
				if (f.isFormField && (f.dataIndex == id || f.id == id || f.getName() == id)) {
					field = f;
					return false;
				}
			}, this);
		}
		return field || null;
	}
} );

/**
 * 简单构建CKEditor的方法
 * @param elid DOM元素的ID，必须的
 * @param w 控件宽度，默认：700(px)
 * @param h 控件高度，默认：320(px)
 */
Ext.form.CKEditor.create = function(elid, w, h) {
	var _width = 1980, _height = 700;
	if (w) _width = w;
	if (h) _height = h;
	var _editor = new Ext.form.CKEditor({
  		id : elid,
  		plugins : new Ext.ux.plugins.FieldValidatePlugin(),
  		height : _height,
  		width : _width,
  		msgTarget: "qtip"
  	});
	var currObj = Ext.get(elid);
  	if(currObj) {
  		_editor.applyToMarkup(elid);
  	}
  	return _editor;
}