(function() {
	IE7 = {
		toString : function() {
			return "IE7 version 2.0 (beta3)"
		}
	};
	var k = IE7.appVersion = navigator.appVersion.match(/MSIE (\d\.\d)/)[1];
	if (/ie7_off/.test(top.location.search) || k < 5)
		return;
	var Q = bG();
	var C = document.compatMode != "CSS1Compat";
	var bm = document.documentElement, v, s;
	var bA = "!";
	var G = ":link{ie7-link:link}:visited{ie7-link:visited}";
	var cj = /^[\w\.]+[^:]*$/;
	function W(a, b) {
		if (cj.test(a))
			a = (b || "") + a;
		return a
	}
	;
	function bn(a, b) {
		a = W(a, b);
		return a.slice(0, a.lastIndexOf("/") + 1)
	}
	;
	var bB = document.scripts[document.scripts.length - 1];
	var ck = bn(bB.src);
	try {
		var H = new ActiveXObject("Microsoft.XMLHTTP")
	} catch (e) {
	}
	var X = {};
	function cl(a, b) {
		try {
			a = W(a, b);
			if (!X[a]) {
				H.open("GET", a, false);
				H.send();
				if (H.status == 0 || H.status == 200) {
					X[a] = H.responseText
				}
			}
		} catch (e) {
		} finally {
			return X[a] || ""
		}
	}
	;
	if (k < 5.5) {
		undefined = Q();
		bA = "HTML:!";
		var cm = /(g|gi)$/;
		var cn = String.prototype.replace;
		String.prototype.replace = function(a, b) {
			if (typeof b == "function") {
				if (a && a.constructor == RegExp) {
					var c = a;
					var d = c.global;
					if (d == null)
						d = cm.test(c);
					if (d)
						c = new RegExp(c.source)
				} else {
					c = new RegExp(bb(a))
				}
				var f, g = this, h = "";
				while (g && (f = c.exec(g))) {
					h += g.slice(0, f.index) + b.apply(this, f);
					g = g.slice(f.index + f[0].length);
					if (!d)
						break
				}
				return h + g
			}
			return cn.apply(this, arguments)
		};
		Array.prototype.pop = function() {
			if (this.length) {
				var a = this[this.length - 1];
				this.length--;
				return a
			}
			return undefined
		};
		Array.prototype.push = function() {
			for ( var a = 0; a < arguments.length; a++) {
				this[this.length] = arguments[a]
			}
			return this.length
		};
		var co = this;
		Function.prototype.apply = function(a, b) {
			if (a === undefined)
				a = co;
			else if (a == null)
				a = window;
			else if (typeof a == "string")
				a = new String(a);
			else if (typeof a == "number")
				a = new Number(a);
			else if (typeof a == "boolean")
				a = new Boolean(a);
			if (arguments.length == 1)
				b = [];
			else if (b[0] && b[0].writeln)
				b[0] = b[0].documentElement.document || b[0];
			var c = "#ie7_apply", d;
			a[c] = this;
			switch (b.length) {
			case 0:
				d = a[c]();
				break;
			case 1:
				d = a[c](b[0]);
				break;
			case 2:
				d = a[c](b[0], b[1]);
				break;
			case 3:
				d = a[c](b[0], b[1], b[2]);
				break;
			case 4:
				d = a[c](b[0], b[1], b[2], b[3]);
				break;
			case 5:
				d = a[c](b[0], b[1], b[2], b[3], b[4]);
				break;
			default:
				var f = [], g = b.length - 1;
				do
					f[g] = "a[" + g + "]";
				while (g--);
				eval("r=o[$](" + f + ")")
			}
			if (typeof a.valueOf == "function") {
				delete a[c]
			} else {
				a[c] = undefined;
				if (d && d.writeln)
					d = d.documentElement.document || d
			}
			return d
		};
		Function.prototype.call = function(a) {
			return this.apply(a, bC.apply(arguments, [ 1 ]))
		};
		G += "address,blockquote,body,dd,div,dt,fieldset,form,"
				+ "frame,frameset,h1,h2,h3,h4,h5,h6,iframe,noframes,object,p,"
				+ "hr,applet,center,dir,menu,pre,dl,li,ol,ul{display:block}"
	}
	var bC = Array.prototype.slice;
	var cJ = /%([1-9])/g;
	var cp = /^\s\s*/;
	var cq = /\s\s*$/;
	var cr = /([\/()[\]{}|*+-.,^$?\\])/g;
	var bD = /\bbase\b/;
	var bE = [ "constructor", "toString" ];
	var Y;
	function z() {
	}
	;
	z.extend = function(a, b) {
		Y = true;
		var c = new this;
		ba(c, a);
		Y = false;
		var d = c.constructor;
		function f() {
			if (!Y)
				d.apply(this, arguments)
		}
		;
		c.constructor = f;
		f.extend = arguments.callee;
		ba(f, b);
		f.prototype = c;
		return f
	};
	z.prototype.extend = function(a) {
		return ba(this, a)
	};
	var bo = "#";
	var Z = "~";
	var cs = /\\./g;
	var ct = /\(\?[:=!]|\[[^\]]+\]/g;
	var cu = /\(/g;
	var D = z.extend( {
		constructor : function(a) {
			this[Z] = [];
			this.merge(a)
		},
		exec : function(g) {
			var h = this, p = this[Z];
			return String(g)
					.replace(
							new RegExp(this, this.ignoreCase ? "gi" : "g"),
							function() {
								var a, b = 1, c = 0;
								while ((a = h[bo + p[c++]])) {
									var d = b + a.length + 1;
									if (arguments[b]) {
										var f = a.replacement;
										switch (typeof f) {
										case "function":
											return f.apply(h, bC.call(
													arguments, b, d));
										case "number":
											return arguments[b + f];
										default:
											return f
										}
									}
									b = d
								}
							})
		},
		add : function(a, b) {
			if (a instanceof RegExp) {
				a = a.source
			}
			if (!this[bo + a])
				this[Z].push(String(a));
			this[bo + a] = new D.Item(a, b)
		},
		merge : function(a) {
			for ( var b in a)
				this.add(b, a[b])
		},
		toString : function() {
			return "(" + this[Z].join(")|(") + ")"
		}
	}, {
		IGNORE : "$0",
		Item : z.extend( {
			constructor : function(a, b) {
				a = a instanceof RegExp ? a.source : String(a);
				if (typeof b == "number")
					b = String(b);
				else if (b == null)
					b = "";
				if (typeof b == "string" && /\$(\d+)/.test(b)) {
					if (/^\$\d+$/.test(b)) {
						b = parseInt(b.slice(1))
					} else {
						var c = /'/.test(b.replace(/\\./g, "")) ? '"' : "'";
						b = b.replace(/\n/g, "\\n").replace(/\r/g, "\\r")
								.replace(
										/\$(\d+)/g,
										c + "+(arguments[$1]||" + c + c + ")+"
												+ c);
						b = new Function("return " + c
								+ b.replace(/(['"])\1\+(.*)\+\1\1$/, "$1") + c)
					}
				}
				this.length = D.count(a);
				this.replacement = b;
				this.toString = bG(a)
			}
		}),
		count : function(a) {
			a = String(a).replace(cs, "").replace(ct, "");
			return I(a, cu).length
		}
	});
	function ba(a, b) {
		if (a && b) {
			var c = (typeof b == "function" ? Function : Object).prototype;
			var d = bE.length, f;
			if (Y)
				while (f = bE[--d]) {
					var g = b[f];
					if (g != c[f]) {
						if (bD.test(g)) {
							bF(a, f, g)
						} else {
							a[f] = g
						}
					}
				}
			for (f in b)
				if (c[f] === undefined) {
					var g = b[f];
					if (a[f] && typeof g == "function" && bD.test(g)) {
						bF(a, f, g)
					} else {
						a[f] = g
					}
				}
		}
		return a
	}
	;
	function bF(c, d, f) {
		var g = c[d];
		c[d] = function() {
			var a = this.base;
			this.base = g;
			var b = f.apply(this, arguments);
			this.base = a;
			return b
		}
	}
	;
	function cv(a, b) {
		if (!b)
			b = a;
		var c = {};
		for ( var d in a)
			c[d] = b[d];
		return c
	}
	;
	function i(c) {
		var d = arguments;
		var f = new RegExp("%([1-" + arguments.length + "])", "g");
		return String(c).replace(f, function(a, b) {
			return b < d.length ? d[b] : a
		})
	}
	;
	function I(a, b) {
		return String(a).match(b) || []
	}
	;
	function bb(a) {
		return String(a).replace(cr, "\\$1")
	}
	;
	function cK(a) {
		return String(a).replace(cp, "").replace(cq, "")
	}
	;
	function bG(a) {
		return function() {
			return a
		}
	}
	;
	var bH = D.extend( {
		ignoreCase : true
	});
	var cw = /\x01(\d+)/g, cx = /'/g, cy = /^\x01/, cz = /\\([\da-fA-F]{1,4})/g;
	var bp = [];
	var cA = new bH( {
		"<!\\-\\-|\\-\\->" : "",
		"\\/\\*[^*]*\\*+([^\\/][^*]*\\*+)*\\/" : "",
		"@(namespace|import)[^;\\n]+[;\\n]" : "",
		"'(\\\\.|[^'\\\\])*'" : bJ,
		'"(\\\\.|[^"\\\\])*"' : bJ,
		"\\s+" : " "
	});
	function cB(a) {
		return cA.exec(a)
	}
	;
	function bI(c) {
		return c.replace(cw, function(a, b) {
			return bp[b - 1]
		})
	}
	;
	function bJ(c) {
		return "\x01" + bp.push(c.replace(cz, function(a, b) {
			return eval("'\\u" + "0000".slice(b.length) + b + "'")
		}).slice(1, -1).replace(cx, "\\'"))
	}
	;
	function cC(a) {
		return cy.test(a) ? bp[a.slice(1) - 1] : a
	}
	;
	var cD = new D( {
		Width : "Height",
		width : "height",
		Left : "Top",
		left : "top",
		Right : "Bottom",
		right : "bottom",
		onX : "onY"
	});
	function A(a) {
		return cD.exec(a)
	}
	;
	var bK = [];
	function bq(a) {
		cF(a);
		w(window, "onresize", a)
	}
	;
	function w(a, b, c) {
		a.attachEvent(b, c);
		bK.push(arguments)
	}
	;
	function cE(a, b, c) {
		try {
			a.detachEvent(b, c)
		} catch (ignore) {
		}
	}
	;
	w(window, "onunload", function() {
		var a;
		while (a = bK.pop()) {
			cE(a[0], a[1], a[2])
		}
	});
	function R(a, b, c) {
		if (!a.elements)
			a.elements = {};
		if (c)
			a.elements[b.uniqueID] = b;
		else
			delete a.elements[b.uniqueID];
		return c
	}
	;
	w(window, "onbeforeprint", function() {
		if (!IE7.CSS.print)
			new bw("print");
		IE7.CSS.print.recalc()
	});
	var bL = /^\d+(px)?$/i;
	var J = /^\d+%$/;
	var E = function(a, b) {
		if (bL.test(b))
			return parseInt(b);
		var c = a.style.left;
		var d = a.runtimeStyle.left;
		a.runtimeStyle.left = a.currentStyle.left;
		a.style.left = b || 0;
		b = a.style.pixelLeft;
		a.style.left = c;
		a.runtimeStyle.left = d;
		return b
	};
	var br = "ie7-";
	var bM = z.extend( {
		constructor : function() {
			this.fixes = [];
			this.recalcs = []
		},
		init : Q
	});
	var bs = [];
	function cF(a) {
		bs.push(a)
	}
	;
	IE7.recalc = function() {
		IE7.HTML.recalc();
		IE7.CSS.recalc();
		for ( var a = 0; a < bs.length; a++)
			bs[a]()
	};
	function bc(a) {
		return a.currentStyle["ie7-position"] == "fixed"
	}
	;
	function bt(a, b) {
		return a.currentStyle[br + b] || a.currentStyle[b]
	}
	;
	function K(a, b, c) {
		if (a.currentStyle[br + b] == null) {
			a.runtimeStyle[br + b] = a.currentStyle[b]
		}
		a.runtimeStyle[b] = c
	}
	;
	function bN(a) {
		var b = document.createElement(a || "object");
		b.style.cssText = "position:absolute;padding:0;display:block;border:none;clip:rect(0 0 0 0);left:-9999";
		b.ie7_anon = true;
		return b
	}
	;
	function B(a, b, c) {
		if (!be[a]) {
			F = [];
			var d = "";
			var f = T.escape(a).split(",");
			for ( var g = 0; g < f.length; g++) {
				o = m = x = 0;
				S = f.length > 1 ? 2 : 0;
				var h = T.exec(f[g]) || "if(0){";
				if (o) {
					h += i("if(e%1.nodeName!='!'){", m)
				}
				var p = S > 1 ? bV : "";
				h += i(p + bW, m);
				h += Array(I(h, /\{/g).length + 1).join("}");
				d += h
			}
			eval(i(bX, F) + T.unescape(d) + "return s?null:r}");
			be[a] = _h
		}
		return be[a](b || document, c)
	}
	;
	var bd = k < 6;
	var bO = /^(href|src)$/;
	var bu = {
		"class" : "className",
		"for" : "htmlFor"
	};
	IE7._5 = 1;
	IE7._e = function(a, b) {
		var c = a.all[b] || null;
		if (!c || c.id == b)
			return c;
		for ( var d = 0; d < c.length; d++) {
			if (c[d].id == b)
				return c[d]
		}
		return null
	};
	IE7._f = function(a, b) {
		if (b == "src" && a.pngSrc)
			return a.pngSrc;
		var c = bd ? (a.attributes[b] || a.attributes[bu[b.toLowerCase()]]) : a
				.getAttributeNode(b);
		if (c && (c.specified || b == "value")) {
			if (bO.test(b)) {
				return a.getAttribute(b, 2)
			} else if (b == "class") {
				return a.className.replace(/\sie7_\w+/g, "")
			} else if (b == "style") {
				return a.style.cssText
			} else {
				return c.nodeValue
			}
		}
		return null
	};
	var bP = "colSpan,rowSpan,vAlign,dateTime,accessKey,tabIndex,encType,maxLength,readOnly,longDesc";
	ba(bu, cv(bP.toLowerCase().split(","), bP.split(",")));
	IE7._a = function(a) {
		while (a && (a = a.nextSibling)
				&& (a.nodeType != 1 || a.nodeName == "!"))
			continue;
		return a
	};
	IE7._b = function(a) {
		while (a && (a = a.previousSibling)
				&& (a.nodeType != 1 || a.nodeName == "!"))
			continue;
		return a
	};
	var cG = /([\s>+~,]|[^(]\+|^)([#.:\[])/g, cH = /(^|,)([^\s>+~])/g, cI = /\s*([\s>+~(),]|^|$)\s*/g, bQ = /\s\*\s/g;
	var bR = D
			.extend( {
				constructor : function(a) {
					this.base(a);
					this.sorter = new D;
					this.sorter.add(/:not\([^)]*\)/, D.IGNORE);
					this.sorter
							.add(
									/([ >](\*|[\w-]+))([^: >+~]*)(:\w+-child(\([^)]+\))?)([^: >+~]*)/,
									"$1$3$6$4")
				},
				ignoreCase : true,
				escape : function(a) {
					return this.optimise(this.format(a))
				},
				format : function(a) {
					return a.replace(cI, "$1").replace(cH, "$1 $2").replace(cG,
							"$1*$2")
				},
				optimise : function(a) {
					return this.sorter.exec(a.replace(bQ, ">* "))
				},
				unescape : function(a) {
					return bI(a)
				}
			});
	var bS = {
		"" : "%1!=null",
		"=" : "%1=='%2'",
		"~=" : /(^| )%1( |$)/,
		"|=" : /^%1(-|$)/,
		"^=" : /^%1/,
		"$=" : /%1$/,
		"*=" : /%1/
	};
	var bT = {
		"first-child" : "!IE7._b(e%1)",
		"link" : "e%1.currentStyle['ie7-link']=='link'",
		"visited" : "e%1.currentStyle['ie7-link']=='visited'"
	};
	var bv = "var p%2=0,i%2,e%2,n%2=e%1.";
	var bU = "e%1.sourceIndex";
	var bV = "var g=" + bU + ";if(!p[g]){p[g]=1;";
	var bW = "r[r.length]=e%1;if(s)return e%1;";
	var bX = "var _h=function(e0,s){IE7._5++;var r=[],p={},reg=[%1],d=document;";
	var F;
	var m;
	var o;
	var x;
	var S;
	var be = {};
	var T = new bR(
			{
				" (\\*|[\\w-]+)#([\\w-]+)" : function(a, b, c) {
					o = false;
					var d = "var e%2=IE7._e(d,'%4');if(e%2&&";
					if (b != "*")
						d += "e%2.nodeName=='%3'&&";
					d += "(e%1==d||e%1.contains(e%2))){";
					if (x)
						d += i("i%1=n%1.length;", x);
					return i(d, m++, m, b.toUpperCase(), c)
				},
				" (\\*|[\\w-]+)" : function(a, b) {
					S++;
					o = b == "*";
					var c = bv;
					c += (o && bd) ? "all" : "getElementsByTagName('%3')";
					c += ";for(i%2=0;(e%2=n%2[i%2]);i%2++){";
					return i(c, m++, x = m, b.toUpperCase())
				},
				">(\\*|[\\w-]+)" : function(a, b) {
					var c = x;
					o = b == "*";
					var d = bv;
					d += c ? "children" : "childNodes";
					if (!o && c)
						d += ".tags('%3')";
					d += ";for(i%2=0;(e%2=n%2[i%2]);i%2++){";
					if (o) {
						d += "if(e%2.nodeType==1){";
						o = bd
					} else {
						if (!c)
							d += "if(e%2.nodeName=='%3'){"
					}
					return i(d, m++, x = m, b.toUpperCase())
				},
				"\\+(\\*|[\\w-]+)" : function(a, b) {
					var c = "";
					if (o)
						c += "if(e%1.nodeName!='!'){";
					o = false;
					c += "e%1=IE7._a(e%1);if(e%1";
					if (b != "*")
						c += "&&e%1.nodeName=='%2'";
					c += "){";
					return i(c, m, b.toUpperCase())
				},
				"~(\\*|[\\w-]+)" : function(a, b) {
					var c = "";
					if (o)
						c += "if(e%1.nodeName!='!'){";
					o = false;
					S = 2;
					c += "while(e%1=e%1.nextSibling){if(e%1.ie7_adjacent==IE7._5)break;if(";
					if (b == "*") {
						c += "e%1.nodeType==1";
						if (bd)
							c += "&&e%1.nodeName!='!'"
					} else
						c += "e%1.nodeName=='%2'";
					c += "){e%1.ie7_adjacent=IE7._5;";
					return i(c, m, b.toUpperCase())
				},
				"#([\\w-]+)" : function(a, b) {
					o = false;
					var c = "if(e%1.id=='%2'){";
					if (x)
						c += i("i%1=n%1.length;", x);
					return i(c, m, b)
				},
				"\\.([\\w-]+)" : function(a, b) {
					o = false;
					F.push(new RegExp("(^|\\s)" + bb(b) + "(\\s|$)"));
					return i("if(e%1.className&&reg[%2].test(e%1.className)){",
							m, F.length - 1)
				},
				"\\[([\\w-]+)\\s*([^=]?=)?\\s*([^\\]]*)\\]" : function(a, b, c,
						d) {
					var f = bu[b] || b;
					if (c) {
						var g = "e%1.getAttribute('%2',2)";
						if (!bO.test(b)) {
							g = "e%1.%3||" + g
						}
						b = i("(" + g + ")", m, b, f)
					} else {
						b = i("IE7._f(e%1,'%2')", m, b)
					}
					var h = bS[c || ""] || "0";
					if (h && h.source) {
						F.push(new RegExp(i(h.source, bb(T.unescape(d)))));
						h = "reg[%2].test(%1)";
						d = F.length - 1
					}
					return "if(" + i(h, b, d) + "){"
				},
				":+([\\w-]+)(\\(([^)]+)\\))?" : function(a, b, c, d) {
					b = bT[b];
					return "if(" + (b ? i(b, m, d || "") : "0") + "){"
				}
			});
	var bY = /a(#[\w-]+)?(\.[\w-]+)?:(hover|active)/i;
	var bZ = /\s*\{\s*/, ca = /\s*\}\s*/, cb = /\s*\,\s*/;
	var cc = /(.*)(:first-(line|letter))/;
	var y = document.styleSheets;
	IE7.CSS = new (bM.extend( {
		parser : new bH,
		screen : "",
		print : "",
		styles : [],
		rules : [],
		pseudoClasses : k < 7 ? "first\\-child" : "",
		dynamicPseudoClasses : {
			toString : function() {
				var a = [];
				for ( var b in this)
					a.push(b);
				return a.join("|")
			}
		},
		init : function() {
			var a = "^\x01$";
			var b = "\\[class=?[^\\]]*\\]";
			var c = [];
			if (this.pseudoClasses)
				c.push(this.pseudoClasses);
			var d = this.dynamicPseudoClasses.toString();
			if (d)
				c.push(d);
			c = c.join("|");
			var f = k < 7 ? [ "[>+~[(]|([:.])\\w+\\1" ] : [ b ];
			if (c)
				f.push(":(" + c + ")");
			this.UNKNOWN = new RegExp(f.join("|") || a, "i");
			var g = k < 7 ? [ "\\[[^\\]]+\\]|[^\\s(\\[]+\\s*[+~]" ] : [ b ];
			var h = g.concat();
			if (c)
				h.push(":(" + c + ")");
			n.COMPLEX = new RegExp(h.join("|") || a, "ig");
			if (this.pseudoClasses)
				g.push(":(" + this.pseudoClasses + ")");
			L.COMPLEX = new RegExp(g.join("|") || a, "i");
			L.MATCH = new RegExp(d ? "(.*):(" + d + ")(.*)" : a, "i");
			this.createStyleSheet();
			this.refresh()
		},
		addEventHandler : function() {
			w.apply(null, arguments)
		},
		addFix : function(a, b) {
			this.parser.add(a, b)
		},
		addRecalc : function(c, d, f, g) {
			d = new RegExp("([{;\\s])" + c + "\\s*:\\s*" + d + "[^;}]*");
			var h = this.recalcs.length;
			if (g)
				g = c + ":" + g;
			this.addFix(d, function(a, b) {
				return (g ? b + g : a) + ";ie7-" + a.slice(1) + ";ie7_recalc"
						+ h + ":1"
			});
			this.recalcs.push(arguments);
			return h
		},
		apply : function() {
			this.getInlineStyles();
			new bw("screen");
			this.trash()
		},
		createStyleSheet : function() {
			this.styleSheet = document.createStyleSheet();
			this.styleSheet.ie7 = true;
			this.styleSheet.owningElement.ie7 = true;
			this.styleSheet.cssText = G
		},
		getInlineStyles : function() {
			var a = document.getElementsByTagName("style"), b;
			for ( var c = a.length - 1; (b = a[c]); c--) {
				if (!b.disabled && !b.ie7) {
					this.styles.push(b.innerHTML)
				}
			}
		},
		getText : function(a, b) {
			try {
				var c = a.cssText
			} catch (e) {
				c = ""
			}
			if (H)
				c = cl(a.href, b) || c;
			return c
		},
		recalc : function() {
			this.screen.recalc();
			var a = /ie7_recalc\d+/g;
			var b = G.match(/[{,]/g).length;
			var c = b + (this.screen.cssText.match(/\{/g) || "").length;
			var d = this.styleSheet.rules, f;
			var g, h, p, t, q, j, u, l;
			for (q = b; q < c; q++) {
				f = d[q];
				var r = f.style.cssText;
				if (f && (g = r.match(a))) {
					p = B(f.selectorText);
					if (p.length)
						for (j = 0; j < g.length; j++) {
							l = g[j];
							h = IE7.CSS.recalcs[l.slice(10)][2];
							for (u = 0; (t = p[u]); u++) {
								if (t.currentStyle[l])
									h(t, r)
							}
						}
				}
			}
		},
		refresh : function() {
			this.styleSheet.cssText = G + this.screen + this.print
		},
		trash : function() {
			for ( var a = 0; a < y.length; a++) {
				if (!y[a].ie7) {
					try {
						var b = y[a].cssText
					} catch (e) {
						b = ""
					}
					if (b)
						y[a].cssText = ""
				}
			}
		}
	}));
	var bw = z.extend( {
		constructor : function(a) {
			this.media = a;
			this.load();
			IE7.CSS[a] = this;
			IE7.CSS.refresh()
		},
		createRule : function(a, b) {
			if (IE7.CSS.UNKNOWN.test(a)) {
				var c;
				if (bf && (c = a.match(bf.MATCH))) {
					return new bf(c[1], c[2], b)
				} else if (c = a.match(L.MATCH)) {
					if (!bY.test(c[0]) || L.COMPLEX.test(c[0])) {
						return new L(a, c[1], c[2], c[3], b)
					}
				} else
					return new n(a, b)
			}
			return a + " {" + b + "}"
		},
		getText : function() {
			var h = [].concat(IE7.CSS.styles);
			var p = /@media\s+([^{]*)\{([^@]+\})\s*\}/gi;
			var t = /\ball\b|^$/i, q = /\bscreen\b/i, j = /\bprint\b/i;
			function u(a, b) {
				l.value = b;
				return a.replace(p, l)
			}
			;
			function l(a, b, c) {
				b = r(b);
				switch (b) {
				case "screen":
				case "print":
					if (b != l.value)
						return "";
				case "all":
					return c
				}
				return ""
			}
			;
			function r(a) {
				if (t.test(a))
					return "all";
				else if (q.test(a))
					return (j.test(a)) ? "all" : "screen";
				else if (j.test(a))
					return "print"
			}
			;
			var N = this;
			function O(a, b, c, d) {
				var f = "";
				if (!d) {
					c = r(a.media);
					d = 0
				}
				if (c == "all" || c == N.media) {
					if (d < 3) {
						for ( var g = 0; g < a.imports.length; g++) {
							f += O(a.imports[g], bn(a.href, b), c, d + 1)
						}
					}
					f += cB(a.href ? cg(a, b) : h.pop() || "");
					f = u(f, N.media)
				}
				return f
			}
			;
			var bl = {};
			function cg(a, b) {
				var c = W(a.href, b);
				if (bl[c])
					return "";
				bl[c] = (a.disabled) ? "" : ci(IE7.CSS.getText(a, b), bn(
						a.href, b));
				return bl[c]
			}
			;
			var ch = /(url\s*\(\s*['"]?)([\w\.]+[^:\)]*['"]?\))/gi;
			function ci(a, b) {
				return a.replace(ch, "$1" + b.slice(0, b.lastIndexOf("/") + 1)
						+ "$2")
			}
			;
			for ( var P = 0; P < y.length; P++) {
				if (!y[P].disabled && !y[P].ie7) {
					this.cssText += O(y[P])
				}
			}
		},
		load : function() {
			this.cssText = "";
			this.getText();
			this.parse();
			this.cssText = bI(this.cssText);
			X = {}
		},
		parse : function() {
			this.cssText = IE7.CSS.parser.exec(this.cssText);
			var a = IE7.CSS.rules.length;
			var b = this.cssText.split(ca), c;
			var d, f, g, h;
			for (g = 0; g < b.length; g++) {
				c = b[g].split(bZ);
				d = c[0].split(cb);
				f = c[1];
				for (h = 0; h < d.length; h++) {
					d[h] = f ? this.createRule(d[h], f) : ""
				}
				b[g] = d.join("\n")
			}
			this.cssText = b.join("\n");
			this.rules = IE7.CSS.rules.slice(a)
		},
		recalc : function() {
			var a, b;
			for (b = 0; (a = this.rules[b]); b++)
				a.recalc()
		},
		toString : function() {
			return "@media " + this.media + "{" + this.cssText + "}"
		}
	});
	var bf;
	var n = IE7.Rule = z.extend( {
		constructor : function(a, b) {
			this.id = IE7.CSS.rules.length;
			this.className = n.PREFIX + this.id;
			a = a.match(cc) || a || "*";
			this.selector = a[1] || a;
			this.selectorText = this.parse(this.selector) + (a[2] || "");
			this.cssText = b;
			this.MATCH = new RegExp("\\s" + this.className + "(\\s|$)", "g");
			IE7.CSS.rules.push(this);
			this.init()
		},
		init : Q,
		add : function(a) {
			a.className += " " + this.className
		},
		recalc : function() {
			var a = B(this.selector);
			for ( var b = 0; b < a.length; b++)
				this.add(a[b])
		},
		parse : function(a) {
			var b = a.replace(n.CHILD, " ").replace(n.COMPLEX, "");
			if (k < 7)
				b = b.replace(n.MULTI, "");
			var c = I(b, n.TAGS).length - I(a, n.TAGS).length;
			var d = I(b, n.CLASSES).length - I(a, n.CLASSES).length + 1;
			while (d > 0 && n.CLASS.test(b)) {
				b = b.replace(n.CLASS, "");
				d--
			}
			while (c > 0 && n.TAG.test(b)) {
				b = b.replace(n.TAG, "$1*");
				c--
			}
			b += "." + this.className;
			d = Math.min(d, 2);
			c = Math.min(c, 2);
			var f = -10 * d - c;
			if (f > 0) {
				b = b + "," + n.MAP[f] + " " + b
			}
			return b
		},
		remove : function(a) {
			a.className = a.className.replace(this.MATCH, "$1")
		},
		toString : function() {
			return i("%1 {%2}", this.selectorText, this.cssText)
		}
	}, {
		CHILD : />/g,
		CLASS : /\.[\w-]+/,
		CLASSES : /[.:\[]/g,
		MULTI : /(\.[\w-]+)+/g,
		PREFIX : "ie7_class",
		TAG : /^\w+|([\s>+~])\w+/,
		TAGS : /^\w|[\s>+~]\w/g,
		MAP : {
			1 : "html",
			2 : "html body",
			10 : ".ie7_html",
			11 : "html.ie7_html",
			12 : "html.ie7_html body",
			20 : ".ie7_html .ie7_body",
			21 : "html.ie7_html .ie7_body",
			22 : "html.ie7_html body.ie7_body"
		}
	});
	var L = n.extend( {
		constructor : function(a, b, c, d, f) {
			this.attach = b || "*";
			this.dynamicPseudoClass = IE7.CSS.dynamicPseudoClasses[c];
			this.target = d;
			this.base(a, f)
		},
		recalc : function() {
			var a = B(this.attach), b;
			for ( var c = 0; b = a[c]; c++) {
				var d = this.target ? B(this.target, b) : [ b ];
				if (d.length)
					this.dynamicPseudoClass.apply(b, d, this)
			}
		}
	});
	var cd = z.extend( {
		constructor : function(a, b) {
			this.name = a;
			this.apply = b;
			this.instances = {};
			IE7.CSS.dynamicPseudoClasses[a] = this
		},
		register : function(a) {
			var b = a[2];
			a.id = b.id + a[0].uniqueID;
			if (!this.instances[a.id]) {
				var c = a[1], d;
				for (d = 0; d < c.length; d++)
					b.add(c[d]);
				this.instances[a.id] = a
			}
		},
		unregister : function(a) {
			if (this.instances[a.id]) {
				var b = a[2];
				var c = a[1], d;
				for (d = 0; d < c.length; d++)
					b.remove(c[d]);
				delete this.instances[a.id]
			}
		}
	});
	if (k < 7) {
		var U = new cd("hover", function(a) {
			var b = arguments;
			IE7.CSS.addEventHandler(a,
					k < 5.5 ? "onmouseover" : "onmouseenter", function() {
						U.register(b)
					});
			IE7.CSS.addEventHandler(a, k < 5.5 ? "onmouseout" : "onmouseleave",
					function() {
						U.unregister(b)
					})
		});
		w(document, "onmouseup", function() {
			var a = U.instances;
			for ( var b in a)
				if (!a[b][0].contains(event.srcElement))
					U.unregister(a[b])
		})
	}
	IE7.CSS.addRecalc("[\\w-]+", "inherit", function(c, d) {
		var f = d.match(/[\w-]+\s*:\s*inherit/g);
		for ( var g = 0; g < f.length; g++) {
			var h = f[g].replace(/ie7\-|\s*:\s*inherit/g, "").replace(
					/\-([a-z])/g, function(a, b) {
						return b.toUpperCase()
					});
			c.runtimeStyle[h] = c.parentElement.currentStyle[h]
		}
	});
	IE7.HTML = new (bM.extend( {
		fixed : {},
		init : Q,
		addFix : function() {
			this.fixes.push(arguments)
		},
		apply : function() {
			for ( var a = 0; a < this.fixes.length; a++) {
				var b = B(this.fixes[a][0]);
				var c = this.fixes[a][1];
				for ( var d = 0; d < b.length; d++)
					c(b[d])
			}
		},
		addRecalc : function() {
			this.recalcs.push(arguments)
		},
		recalc : function() {
			for ( var a = 0; a < this.recalcs.length; a++) {
				var b = B(this.recalcs[a][0]);
				var c = this.recalcs[a][1], d;
				var f = Math.pow(2, a);
				for ( var g = 0; (d = b[g]); g++) {
					var h = d.uniqueID;
					if ((this.fixed[h] & f) == 0) {
						d = c(d) || d;
						this.fixed[h] |= f
					}
				}
			}
		}
	}));
	if (k < 7) {
		document.createElement("abbr");
		IE7.HTML.addRecalc("label", function(a) {
			if (!a.htmlFor) {
				var b = B("input,textarea", a, true);
				if (b) {
					w(a, "onclick", function() {
						b.click()
					})
				}
			}
		})
	}
	var V = "[.\\d]";
	new function(_) {
		var layout = IE7.Layout = this;
		G += "*{boxSizing:content-box}";
		IE7.hasLayout = k < 5.5 ? function(a) {
			return a.clientWidth
		} : function(a) {
			return a.currentStyle.hasLayout
		};
		layout.boxSizing = function(a) {
			if (!IE7.hasLayout(a)) {
				a.style.height = "0cm";
				if (a.currentStyle.verticalAlign == "auto")
					a.runtimeStyle.verticalAlign = "top";
				collapseMargins(a)
			}
		};
		function collapseMargins(a) {
			if (a != s && a.currentStyle.position != "absolute") {
				collapseMargin(a, "marginTop");
				collapseMargin(a, "marginBottom")
			}
		}
		;
		function collapseMargin(a, b) {
			if (!a.runtimeStyle[b]) {
				var c = a.parentElement;
				if (c && IE7.hasLayout(c)
						&& !IE7[b == "marginTop" ? "_b" : "_a"](a))
					return;
				var d = B(">*:" + (b == "marginTop" ? "first" : "last")
						+ "-child", a, true);
				if (d && d.currentStyle.styleFloat == "none"
						&& IE7.hasLayout(d)) {
					collapseMargin(d, b);
					margin = _9(a, a.currentStyle[b]);
					childMargin = _9(d, d.currentStyle[b]);
					if (margin < 0 || childMargin < 0) {
						a.runtimeStyle[b] = margin + childMargin
					} else {
						a.runtimeStyle[b] = Math.max(childMargin, margin)
					}
					d.runtimeStyle[b] = "0px"
				}
			}
		}
		;
		function _9(a, b) {
			return b == "auto" ? 0 : E(a, b)
		}
		;
		var UNIT = /^[.\d][\w%]*$/, AUTO = /^(auto|0cm)$/;
		var applyWidth, applyHeight;
		IE7.Layout.borderBox = function(a) {
			applyWidth(a);
			applyHeight(a)
		};
		var fixWidth = function(g) {
			applyWidth = function(a) {
				if (!J.test(a.currentStyle.width))
					h(a);
				collapseMargins(a)
			};
			function h(a, b) {
				if (!a.runtimeStyle.fixedWidth) {
					if (!b)
						b = a.currentStyle.width;
					a.runtimeStyle.fixedWidth = (UNIT.test(b)) ? Math.max(0, q(
							a, b)) : b;
					K(a, "width", a.runtimeStyle.fixedWidth)
				}
			}
			;
			function p(a) {
				if (!bc(a)) {
					var b = a.offsetParent;
					while (b && !IE7.hasLayout(b))
						b = b.offsetParent
				}
				return (b || s).clientWidth
			}
			;
			function t(a, b) {
				if (J.test(b))
					return parseInt(parseFloat(b) / 100 * p(a));
				return E(a, b)
			}
			;
			var q = function(a, b) {
				var c = a.currentStyle["box-sizing"] == "border-box";
				var d = 0;
				if (C && !c)
					d += j(a) + u(a, "padding");
				else if (!C && c)
					d -= j(a) + u(a, "padding");
				return t(a, b) + d
			};
			function j(a) {
				return a.offsetWidth - a.clientWidth
			}
			;
			function u(a, b) {
				return t(a, a.currentStyle[b + "Left"])
						+ t(a, a.currentStyle[b + "Right"])
			}
			;
			G += "*{minWidth:none;maxWidth:none;min-width:none;max-width:none}";
			layout.minWidth = function(a) {
				if (a.currentStyle["min-width"] != null) {
					a.style.minWidth = a.currentStyle["min-width"]
				}
				if (R(arguments.callee, a, a.currentStyle.minWidth != "none")) {
					layout.boxSizing(a);
					h(a);
					l(a)
				}
			};
			eval("IE7.Layout.maxWidth="
					+ String(layout.minWidth).replace(/min/g, "max"));
			function l(a) {
				var b = a.getBoundingClientRect();
				var c = b.right - b.left;
				if (a.currentStyle.minWidth != "none"
						&& c <= q(a, a.currentStyle.minWidth)) {
					a.runtimeStyle.width = a.currentStyle.minWidth
				} else if (a.currentStyle.maxWidth != "none"
						&& c >= q(a, a.currentStyle.maxWidth)) {
					a.runtimeStyle.width = a.currentStyle.maxWidth
				} else {
					a.runtimeStyle.width = a.runtimeStyle.fixedWidth
				}
			}
			;
			function r(a) {
				if (R(r, a, /^(fixed|absolute)$/.test(a.currentStyle.position)
						&& bt(a, "left") != "auto" && bt(a, "right") != "auto"
						&& AUTO.test(bt(a, "width")))) {
					N(a);
					IE7.Layout.boxSizing(a)
				}
			}
			;
			IE7.Layout.fixRight = r;
			function N(a) {
				var b = t(a, a.runtimeStyle._c || a.currentStyle.left);
				var c = p(a) - t(a, a.currentStyle.right) - b - u(a, "margin");
				if (parseInt(a.runtimeStyle.width) == c)
					return;
				a.runtimeStyle.width = "";
				if (bc(a) || g || a.offsetWidth < c) {
					if (!C)
						c -= j(a) + u(a, "padding");
					if (c < 0)
						c = 0;
					a.runtimeStyle.fixedWidth = c;
					K(a, "width", c)
				}
			}
			;
			var O = 0;
			bq(function() {
				if (!s)
					return;
				var a, b = (O < s.clientWidth);
				O = s.clientWidth;
				var c = layout.minWidth.elements;
				for (a in c) {
					var d = c[a];
					var f = (parseInt(d.runtimeStyle.width) == q(d,
							d.currentStyle.minWidth));
					if (b && f)
						d.runtimeStyle.width = "";
					if (b == f)
						l(d)
				}
				var c = layout.maxWidth.elements;
				for (a in c) {
					var d = c[a];
					var f = (parseInt(d.runtimeStyle.width) == q(d,
							d.currentStyle.maxWidth));
					if (!b && f)
						d.runtimeStyle.width = "";
					if (b != f)
						l(d)
				}
				for (a in r.elements)
					N(r.elements[a])
			});
			if (C) {
				IE7.CSS.addRecalc("width", V, applyWidth)
			}
			if (k < 7) {
				IE7.CSS.addRecalc("min-width", V, layout.minWidth);
				IE7.CSS.addRecalc("max-width", V, layout.maxWidth);
				IE7.CSS.addRecalc("right", V, r)
			}
		};
		eval("var fixHeight=" + A(fixWidth));
		fixWidth();
		fixHeight(true)
	};
	var bg = W("blank.gif", ck);
	var bh = "DXImageTransform.Microsoft.AlphaImageLoader";
	var bx = "progid:" + bh + "(src='%1',sizingMethod='%2')";
	var bi;
	var M = [];
	function by(a) {
		if (bi.test(a.src)) {
			var b = new Image(a.width, a.height);
			b.onload = function() {
				a.width = b.width;
				a.height = b.height;
				b = null
			};
			b.src = a.src;
			a.pngSrc = a.src;
			bz(a)
		}
	}
	;
	if (k >= 5.5 && k < 7) {
		IE7.CSS.addFix(
				/background(-image)?\s*:\s*([^};]*)?url\(([^\)]+)\)([^;}]*)?/,
				function(a, b, c, d, f) {
					d = cC(d);
					return bi.test(d) ? "filter:" + i(bx, d, "crop")
							+ ";zoom:1;background" + (b || "") + ":"
							+ (c || "") + "none" + (f || "") : a
				});
		IE7.HTML.addRecalc("img,input", function(a) {
			if (a.tagName == "INPUT" && a.type != "image")
				return;
			by(a);
			w(a, "onpropertychange", function() {
				if (!bj && event.propertyName == "src"
						&& a.src.indexOf(bg) == -1)
					by(a)
			})
		});
		var bj = false;
		w(window, "onbeforeprint", function() {
			bj = true;
			for ( var a = 0; a < M.length; a++)
				ce(M[a])
		});
		w(window, "onafterprint", function() {
			for ( var a = 0; a < M.length; a++)
				bz(M[a]);
			bj = false
		})
	}
	function bz(a, b) {
		var c = a.filters[bh];
		if (c) {
			c.src = a.src;
			c.enabled = true
		} else {
			a.runtimeStyle.filter = i(bx, a.src, b || "scale");
			M.push(a)
		}
		a.src = bg
	}
	;
	function ce(a) {
		a.src = a.pngSrc;
		a.filters[bh].enabled = false
	}
	;
	new function(_) {
		if (k >= 7)
			return;
		IE7.CSS.addRecalc("position", "fixed", _6, "absolute");
		IE7.CSS.addRecalc("background(-attachment)?", "[^};]*fixed", _2);
		var $viewport = C ? "body" : "documentElement";
		function _3() {
			if (v.currentStyle.backgroundAttachment != "fixed") {
				if (v.currentStyle.backgroundImage == "none") {
					v.runtimeStyle.backgroundRepeat = "no-repeat";
					v.runtimeStyle.backgroundImage = "url(" + bg + ")"
				}
				v.runtimeStyle.backgroundAttachment = "fixed"
			}
			_3 = Q
		}
		;
		var _0 = bN("img");
		function _1(a) {
			return a ? bc(a) || _1(a.parentElement) : false
		}
		;
		function _d(a, b, c) {
			setTimeout("document.all." + a.uniqueID
					+ ".runtimeStyle.setExpression('" + b + "','" + c + "')", 0)
		}
		;
		function _2(a) {
			if (R(_2, a, a.currentStyle.backgroundAttachment == "fixed"
					&& !a.contains(v))) {
				_3();
				bgLeft(a);
				bgTop(a);
				_8(a)
			}
		}
		;
		function _8(a) {
			_0.src = a.currentStyle.backgroundImage.slice(5, -2);
			var b = a.canHaveChildren ? a : a.parentElement;
			b.appendChild(_0);
			setOffsetLeft(a);
			setOffsetTop(a);
			b.removeChild(_0)
		}
		;
		function bgLeft(a) {
			a.style.backgroundPositionX = a.currentStyle.backgroundPositionX;
			if (!_1(a)) {
				_d(a, "backgroundPositionX",
						"(parseInt(runtimeStyle.offsetLeft)+document."
								+ $viewport + ".scrollLeft)||0")
			}
		}
		;
		eval(A(bgLeft));
		function setOffsetLeft(a) {
			var b = _1(a) ? "backgroundPositionX" : "offsetLeft";
			a.runtimeStyle[b] = getOffsetLeft(a, a.style.backgroundPositionX)
					- a.getBoundingClientRect().left - a.clientLeft + 2
		}
		;
		eval(A(setOffsetLeft));
		function getOffsetLeft(a, b) {
			switch (b) {
			case "left":
			case "top":
				return 0;
			case "right":
			case "bottom":
				return s.clientWidth - _0.offsetWidth;
			case "center":
				return (s.clientWidth - _0.offsetWidth) / 2;
			default:
				if (J.test(b)) {
					return parseInt((s.clientWidth - _0.offsetWidth)
							* parseFloat(b) / 100)
				}
				_0.style.left = b;
				return _0.offsetLeft
			}
		}
		;
		eval(A(getOffsetLeft));
		function _6(a) {
			if (R(_6, a, bc(a))) {
				K(a, "position", "absolute");
				K(a, "left", a.currentStyle.left);
				K(a, "top", a.currentStyle.top);
				_3();
				IE7.Layout.fixRight(a);
				_4(a)
			}
		}
		;
		function _4(a, b) {
			positionTop(a, b);
			positionLeft(a, b, true);
			if (!a.runtimeStyle.autoLeft && a.currentStyle.marginLeft == "auto"
					&& a.currentStyle.right != "auto") {
				var c = s.clientWidth - getPixelWidth(a, a.currentStyle.right)
						- getPixelWidth(a, a.runtimeStyle._c) - a.clientWidth;
				if (a.currentStyle.marginRight == "auto")
					c = parseInt(c / 2);
				if (_1(a.offsetParent))
					a.runtimeStyle.pixelLeft += c;
				else
					a.runtimeStyle.shiftLeft = c
			}
			clipWidth(a);
			clipHeight(a)
		}
		;
		function clipWidth(a) {
			var b = a.runtimeStyle.fixWidth;
			a.runtimeStyle.borderRightWidth = "";
			a.runtimeStyle.width = b ? getPixelWidth(a, b) : "";
			if (a.currentStyle.width != "auto") {
				var c = a.getBoundingClientRect();
				var d = a.offsetWidth - s.clientWidth + c.left - 2;
				if (d >= 0) {
					a.runtimeStyle.borderRightWidth = "0px";
					d = Math.max(E(a, a.currentStyle.width) - d, 0);
					K(a, "width", d);
					return d
				}
			}
		}
		;
		eval(A(clipWidth));
		function positionLeft(a, b) {
			if (!b && J.test(a.currentStyle.width)) {
				a.runtimeStyle.fixWidth = a.currentStyle.width
			}
			if (a.runtimeStyle.fixWidth) {
				a.runtimeStyle.width = getPixelWidth(a, a.runtimeStyle.fixWidth)
			}
			a.runtimeStyle.shiftLeft = 0;
			a.runtimeStyle._c = a.currentStyle.left;
			a.runtimeStyle.autoLeft = a.currentStyle.right != "auto"
					&& a.currentStyle.left == "auto";
			a.runtimeStyle.left = "";
			a.runtimeStyle.screenLeft = getScreenLeft(a);
			a.runtimeStyle.pixelLeft = a.runtimeStyle.screenLeft;
			if (!b && !_1(a.offsetParent)) {
				_d(a, "pixelLeft",
						"runtimeStyle.screenLeft+runtimeStyle.shiftLeft+document."
								+ $viewport + ".scrollLeft")
			}
		}
		;
		eval(A(positionLeft));
		function getScreenLeft(a) {
			var b = a.offsetLeft, c = 1;
			if (a.runtimeStyle.autoLeft) {
				b = s.clientWidth - a.offsetWidth
						- getPixelWidth(a, a.currentStyle.right)
			}
			if (a.currentStyle.marginLeft != "auto") {
				b -= getPixelWidth(a, a.currentStyle.marginLeft)
			}
			while (a = a.offsetParent) {
				if (a.currentStyle.position != "static")
					c = -1;
				b += a.offsetLeft * c
			}
			return b
		}
		;
		eval(A(getScreenLeft));
		function getPixelWidth(a, b) {
			return J.test(b) ? parseInt(parseFloat(b) / 100 * s.clientWidth)
					: E(a, b)
		}
		;
		eval(A(getPixelWidth));
		function _g() {
			var a = _2.elements;
			for ( var b in a)
				_8(a[b]);
			a = _6.elements;
			for (b in a) {
				_4(a[b], true);
				_4(a[b], true)
			}
			_7 = 0
		}
		;
		var _7;
		bq(function() {
			if (!_7)
				_7 = setTimeout(_g, 0)
		})
	};
	var bk = {
		backgroundColor : "transparent",
		backgroundImage : "none",
		backgroundPositionX : null,
		backgroundPositionY : null,
		backgroundRepeat : null,
		borderTopWidth : 0,
		borderRightWidth : 0,
		borderBottomWidth : 0,
		borderLeftStyle : "none",
		borderTopStyle : "none",
		borderRightStyle : "none",
		borderBottomStyle : "none",
		borderLeftWidth : 0,
		height : null,
		marginTop : 0,
		marginBottom : 0,
		marginRight : 0,
		marginLeft : 0,
		width : "100%"
	};
	IE7.CSS.addRecalc("overflow", "visible", function(a) {
		if (a.parentNode.ie7_wrapped)
			return;
		if (IE7.Layout && a.currentStyle["max-height"] != "auto") {
			IE7.Layout.maxHeight(a)
		}
		if (a.currentStyle.marginLeft == "auto")
			a.style.marginLeft = 0;
		if (a.currentStyle.marginRight == "auto")
			a.style.marginRight = 0;
		var b = document.createElement(bA);
		b.ie7_wrapped = a;
		for ( var c in bk) {
			b.style[c] = a.currentStyle[c];
			if (bk[c] != null) {
				a.runtimeStyle[c] = bk[c]
			}
		}
		b.style.display = "block";
		b.style.position = "relative";
		a.runtimeStyle.position = "absolute";
		a.parentNode.insertBefore(b, a);
		b.appendChild(a)
	});
	function cf() {
		var f = "xx-small,x-small,small,medium,large,x-large,xx-large"
				.split(",");
		for ( var g = 0; g < f.length; g++) {
			f[f[g]] = f[g - 1] || "0.67em"
		}
		IE7.CSS.addFix(/(font(-size)?\s*:\s*)([\w.-]+)/, function(a, b, c, d) {
			return b + (f[d] || d)
		});
		if (k < 6) {
			var h = /^\-/, p = /(em|ex)$/i;
			var t = /em$/i, q = /ex$/i;
			E = function(a, b) {
				if (bL.test(b))
					return parseInt(b) || 0;
				var c = h.test(b) ? -1 : 1;
				if (p.test(b))
					c *= u(a);
				j.style.width = (c < 0) ? b.slice(1) : b;
				v.appendChild(j);
				b = c * j.offsetWidth;
				j.removeNode();
				return parseInt(b)
			};
			var j = bN();
			function u(a) {
				var b = 1;
				j.style.fontFamily = a.currentStyle.fontFamily;
				j.style.lineHeight = a.currentStyle.lineHeight;
				while (a != v) {
					var c = a.currentStyle["ie7-font-size"];
					if (c) {
						if (t.test(c))
							b *= parseFloat(c);
						else if (J.test(c))
							b *= (parseFloat(c) / 100);
						else if (q.test(c))
							b *= (parseFloat(c) / 2);
						else {
							j.style.fontSize = c;
							return 1
						}
					}
					a = a.parentElement
				}
				return b
			}
			;
			IE7.CSS.addFix(/cursor\s*:\s*pointer/, "cursor:hand");
			IE7.CSS.addFix(/display\s*:\s*list-item/, "display:block")
		}
		function l(a) {
			if (k < 5.5)
				IE7.Layout.boxSizing(a.parentElement);
			var b = a.parentElement;
			var c = b.offsetWidth - a.offsetWidth - r(b);
			var d = (a.currentStyle["ie7-margin"] && a.currentStyle.marginRight == "auto")
					|| a.currentStyle["ie7-margin-right"] == "auto";
			switch (b.currentStyle.textAlign) {
			case "right":
				c = d ? parseInt(c / 2) : 0;
				a.runtimeStyle.marginRight = c + "px";
				break;
			case "center":
				if (d)
					c = 0;
			default:
				if (d)
					c /= 2;
				a.runtimeStyle.marginLeft = parseInt(c) + "px"
			}
		}
		;
		function r(a) {
			return E(a, a.currentStyle.paddingLeft)
					+ E(a, a.currentStyle.paddingRight)
		}
		;
		IE7.CSS.addRecalc("margin(-left|-right)?", "[^};]*auto", function(a) {
			if (R(l, a, a.parentElement && a.currentStyle.display == "block"
					&& a.currentStyle.marginLeft == "auto"
					&& a.currentStyle.position != "absolute")) {
				l(a)
			}
		});
		bq(function() {
			for ( var a in l.elements) {
				var b = l.elements[a];
				b.runtimeStyle.marginLeft = b.runtimeStyle.marginRight = "";
				l(b)
			}
		})
	}
	;
	IE7.loaded = true;
	(function() {
		try {
			bm.doScroll("left")
		} catch (e) {
			setTimeout(arguments.callee, 1);
			return
		}
		try {
			eval(bB.innerHTML)
		} catch (e) {
		}
		bi = new RegExp(bb(typeof IE7_PNG_SUFFIX == "string" ? IE7_PNG_SUFFIX
				: "-trans.png")
				+ "$", "i");
		v = document.body;
		s = C ? v : bm;
		v.className += " ie7_body";
		bm.className += " ie7_html";
		if (C)
			cf();
		IE7.CSS.init();
		IE7.HTML.init();
		IE7.HTML.apply();
		IE7.CSS.apply();
		IE7.recalc()
	})()
})();