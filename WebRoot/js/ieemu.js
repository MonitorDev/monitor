/*
 *  IE Emu
 *	for iTASK5.0
 *
 *  A emulation of Internet Explorer DHTML Object Model for Mozilla  
 *
 */

extendEventObject();
emulateAttachEvent();
emulateEventHandlers(["click", "dblclick", "mouseover", "mouseout",
						"mousedown", "mouseup", "mousemove", "input",
						"keydown", "keypress", "keyup", "dragstart"]);
emulateStyle();
emulateHTMLModel()
emulateXMLDocumentModel();
extendElementModel();
emulateAllModel();
emulateDFish();


// It is better to use a constant for event.button
Event.LEFT = 0;
Event.MIDDLE = 1;
Event.RIGHT = 2;

/*
 * Extends the event object with srcElement, cancelBubble, returnValue,
 * fromElement and toElement
 */
function extendEventObject() {
	Event.prototype.__defineSetter__("returnValue", function (b) {
		if (!b) this.preventDefault();
		return b;
	});

	Event.prototype.__defineSetter__("cancelBubble", function (b) {
		if (b) this.stopPropagation();
		return b;
	});

	Event.prototype.__defineGetter__("srcElement", function () {
		var node = this.target;
		while (node.nodeType != 1) node = node.parentNode;
		return node;
	});

	Event.prototype.__defineGetter__("fromElement", function () {
		var node;
		if (this.type == "mouseover")
			node = this.relatedTarget;
		else if (this.type == "mouseout")
			node = this.target;
		if (!node) return;
		while (node.nodeType != 1) node = node.parentNode;
		return node;
	});

	Event.prototype.__defineGetter__("toElement", function () {
		var node;
		if (this.type == "mouseout")
			node = this.relatedTarget;
		else if (this.type == "mouseover")
			node = this.target;
		if (!node) return;
		while (node.nodeType != 1) node = node.parentNode;
		return node;
	});

	Event.prototype.__defineGetter__("offsetX", function () {
		return this.layerX;
	});
	Event.prototype.__defineGetter__("offsetY", function () {
		return this.layerY;
	});
	if ( window.constructor != Object ) {
		window.constructor.prototype.__defineGetter__("screenLeft", function () {
			return this.screenX;
		});
		
		window.constructor.prototype.__defineGetter__("screenTop", function () {
			return this.screenY;
		});
	}

}

/*
 * Emulates element.attachEvent as well as detachEvent
 */
function emulateAttachEvent() {
	window.attachEvent =
	HTMLDocument.prototype.attachEvent =
	HTMLElement.prototype.attachEvent = function (sType, fHandler, bCapture) {
		var shortTypeName = sType.replace(/^on/, "");
		this.addEventListener(shortTypeName, fHandler, bCapture == null ? false : bCapture);
	};
	window.detachEvent =
	HTMLDocument.prototype.detachEvent =
	HTMLElement.prototype.detachEvent = function (sType, fHandler, bCapture) {
		var shortTypeName = sType.replace(/^on/, "");
		this.removeEventListener(shortTypeName, fHandler, bCapture == null ? false : bCapture);
	};
	HTMLElement.prototype.fireEvent = function( a, b ) {
		var evt = document.createEvent( b || 'HTMLEvents' );
		evt.initEvent( a.replace(/^on/, ""), true, true );
		this.dispatchEvent( evt );
	}
}

/*
 * This function binds the event object passed along in an
 * event to window.event
 */
function emulateEventHandlers(eventNames) {
	if ( ! Br.chm ) {
		for (var i = 0; i < eventNames.length; i++) {
			document.addEventListener(eventNames[i], function (e) {
				window.event = e;
			}, true);	// using capture
		}
	}
	if (document.readyState == null) {
		document.readyState = 1;
		document.addEventListener('load', function () {
			document.readyState = 'complete';
		}, false);
	}
}

/*
 * Simple emulation of document.all
 * this one is far from complete. Be cautious
 */

function emulateAllModel() {
	var allGetter = function ( id ) {
		var a = this.getElementsByTagName("*");
		var node = this;
		a.tags = function (sTagName) {
			return node.getElementsByTagName(sTagName);
		};
		return a;
	};
	HTMLDocument.prototype.__defineGetter__("all", allGetter);
	HTMLElement.prototype.__defineGetter__("all", allGetter);
}

function extendElementModel() {
	HTMLElement.prototype.__defineGetter__("parentElement", function () {
		if (this.parentNode == this.ownerDocument) return null;
		return this.parentNode;
	});

	HTMLElement.prototype.__defineGetter__("children", function () {
		var tmp = [];
		var j = 0;
		var n;
		for (var i = 0; i < this.childNodes.length; i++) {
			n = this.childNodes[i];
			if (n.nodeType == 1) {
				tmp[j++] = n;
				if (n.name) {	// named children
					if (!tmp[n.name])
						tmp[n.name] = [];
					tmp[n.name][tmp[n.name].length] = n;
				}
				if (n.id)		// child with id
					tmp[n.id] = n
			}
		}
		return tmp;
	});

	HTMLElement.prototype.contains = function (oEl) {
		if (oEl == this) return true;
		if (oEl == null) return false;
		return this.contains(oEl.parentNode);
	};
		
}

function emulateStyle() {
	HTMLElement.prototype.__defineGetter__("currentStyle", function () {
		return this.ownerDocument.defaultView.getComputedStyle(this, null);
	});
	CSSStyleDeclaration.prototype.__defineSetter__("posWidth", function (n) {
		this.width = n + 'px';
	});
	CSSStyleDeclaration.prototype.__defineGetter__("posWidth", function () {
		return parseFloat(this.width);
	});
	CSSStyleDeclaration.prototype.__defineSetter__("posHeight", function (n) {
		this.height = n + 'px';
	});
	CSSStyleDeclaration.prototype.__defineGetter__("posHeight", function () {
		return parseFloat(this.height);
	});
	CSSStyleDeclaration.prototype.__defineSetter__("posTop", function (n) {
		this.top = n + 'px';
	});
	CSSStyleDeclaration.prototype.__defineGetter__("posTop", function () {
		return parseFloat(this.top);
	});
	CSSStyleDeclaration.prototype.__defineSetter__("posLeft", function (n) {
		this.left = n + 'px';
	});
	CSSStyleDeclaration.prototype.__defineGetter__("posLeft", function () {
		return parseFloat(this.left);
	});
	CSSStyleDeclaration.prototype.__defineSetter__("styleFloat", function (n) {
		this.cssFloat = n;
	});
	CSSStyleDeclaration.prototype.__defineGetter__("styleFloat", function () {
		return this.cssFloat;
	});
}

function emulateHTMLModel() {

	// This function is used to generate a html string for the text properties/methods
	// It replaces '\n' with "<BR"> as well as fixes consecutive white spaces
	// It also repalaces some special characters
	function convertTextToHTML(s) {
		s = new String(s).replace(/\&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g, "<BR>");
		while (/\s\s/.test(s))
			s = s.replace(/\s\s/, "&nbsp; ");
		return s.replace(/\s/g, " ");
	}

	HTMLElement.prototype.insertAdjacentHTML = function (sWhere, sHTML) {
		var df;	// : DocumentFragment
		var r = this.ownerDocument.createRange();

		switch (String(sWhere).toLowerCase()) {
			case "beforebegin":
				r.setStartBefore(this);
				df = r.createContextualFragment(sHTML);
				this.parentNode.insertBefore(df, this);
				break;

			case "afterbegin":
				r.selectNodeContents(this);
				r.collapse(true);
				df = r.createContextualFragment(sHTML);
				this.insertBefore(df, this.firstChild);
				break;

			case "beforeend":
				r.selectNodeContents(this);
				r.collapse(false);
				df = r.createContextualFragment(sHTML);
				this.appendChild(df);
				break;

			case "afterend":
				r.setStartAfter(this);
				df = r.createContextualFragment(sHTML);
				this.parentNode.insertBefore(df, this.nextSibling);
				break;
		}
	};

	HTMLElement.prototype.__defineSetter__("outerHTML", function (sHTML) {
	   var r = this.ownerDocument.createRange();
	   r.setStartBefore(this);
	   var df = r.createContextualFragment(sHTML);
	   this.parentNode.replaceChild(df, this);

	   return sHTML;
	});

	HTMLElement.prototype.__defineGetter__("canHaveChildren", function () {
		switch (this.tagName) {
			case "AREA":
			case "BASE":
			case "BASEFONT":
			case "COL":
			case "FRAME":
			case "HR":
			case "IMG":
			case "BR":
			case "INPUT":
			case "ISINDEX":
			case "LINK":
			case "META":
			case "PARAM":
				return false;
		}
		return true;
	});
	
	if ( Br.fox ) {
		HTMLElement.prototype.__defineGetter__("outerHTML", function () {
			var attr, attrs = this.attributes;
			var str = "<" + this.tagName;
			for (var i = 0; i < attrs.length; i++) {
				attr = attrs[i];
				if (attr.specified)
					str += " " + attr.name + '="' + attr.value + '"';
			}
			if ( ! this.canHaveChildren )
				return str + ">";
	
			return str + ">" + this.innerHTML + "</" + this.tagName + ">";
		});

		HTMLElement.prototype.__defineSetter__("innerText", function (sText) {
			this.innerHTML = convertTextToHTML(sText);
			return sText;
		});
	
		var tmpGet;
		HTMLElement.prototype.__defineGetter__("innerText", tmpGet = function () {
			var r = this.ownerDocument.createRange();
			r.selectNodeContents(this);
			return r.toString();
		});
		HTMLElement.prototype.__defineSetter__("outerText", function (sText) {
			this.outerHTML = convertTextToHTML(sText);
			return sText;
		});
		HTMLElement.prototype.__defineGetter__("outerText", tmpGet);
		
		HTMLElement.prototype.insertAdjacentText = function (sWhere, sText) {
			this.insertAdjacentHTML(sWhere, convertTextToHTML(sText));
		};
	
	}

	HTMLElement.prototype.removeNode = function ( a ) {
		if ( this.parentNode ) {
			if ( a == false ) {
				while ( this.childNodes.length )
					this.parentNode.appendChild( this.childNodes[ 0 ] );
			}
			this.parentNode.removeChild( this );
		}
	};
	if ( ! HTMLElement.prototype.getBoundingClientRect ) {
		HTMLElement.prototype.getBoundingClientRect = function () {
			var a = this,
				b = { left : a.offsetLeft, top : a.offsetTop,
					width : this.offsetWidth, height : this.offsetHeight };
			b.right  = b.left + b.width;
			b.bottom = b.top  + b.height;
			return b;
		};
	}

	Range.prototype.inRange = function( a ) {
		return ( this.compareBoundaryPoints( Range.START_TO_START, a ) > -1 && this.compareBoundaryPoints( Range.END_TO_END, a ) < 1 )
	}

	Range.prototype.__defineGetter__( "text", function() {
		return this.toString();
	} );
	
	Range.prototype.__defineSetter__( "text", function( a ) {
		this.deleteContents();
		this.insertNode( document.createTextNode( a ) );
	} );
	
	// @a -> start index, b -> end index
	// @fixme: startContainer��ǩ����Ƕ��ʱ��Ҫ�ݹ鴦��
	Range.prototype.movePoint = function( a, b ) {
		var c = this.startContainer.firstChild || this.startContainer,
			d,
			i = a, l = 0;
		do {
			this.selectNodeContents( c );
			l = this.toString().length;
			i -= l;
		} while ( i > 0 && ( c = c.nextSibling ) );
		c = d = this.startContainer;
		i += l;
		var j = b - a + i, l = 0;
		do {
			this.selectNodeContents( d );
			l = this.toString().length;
			j -= l;
		} while ( j > 0 && ( d = d.nextSibling ) );
		this.setStart( c.firstChild || c, i );
		if ( d ) {
			var e = d;
			while ( e.firstChild )
				e = e.firstChild;
			this.setEnd( e, l + j );
		}
	}

}

function emulateXMLDocumentModel(sExpr) {
	if ( ! Br.ie11 ) {
		Attr.prototype.selectNodes =
		Element.prototype.selectNodes =
		XMLDocument.prototype.selectNodes = function(sExpr) { 
			var doc = this.nodeType == 9 ? this : this.ownerDocument;
			var nsRes = doc.createNSResolver(this.nodeType == 9 ? this.documentElement : this);
			var xpRes = doc.evaluate(sExpr,this,nsRes,5,null);
			var res=[];
			var item;
			while ( (item = xpRes.iterateNext()) ) res.push(item);
			res.expr = sExpr;
			return res;
		};
	
		Attr.prototype.selectSingleNode =
		Element.prototype.selectSingleNode =
		XMLDocument.prototype.selectSingleNode = function(sExpr) {
			var doc=this.nodeType == 9 ? this : this.ownerDocument;
			var nsRes = doc.createNSResolver(this.nodeType == 9 ? this.documentElement : this);
			var xpRes = doc.evaluate(sExpr,this,nsRes,9,null);
			return xpRes.singleNodeValue;
		};
	}
	Element.prototype.__defineGetter__( "text", function() {
		return this.textContent;
	} );

	Element.prototype.__defineSetter__( "text", function( a ) {
		this.textContent = a;
	} );

	XMLDocument.prototype.loadXML = 
	Document.prototype.loadXML = function (s) {
		
		// parse the string to a new doc	
		var doc2 = (new DOMParser()).parseFromString(s, "text/xml");
		
		// remove all initial children
		while (this.hasChildNodes())
			this.removeChild(this.lastChild);
			
		// insert and import nodes
		for (var i = 0; i < doc2.childNodes.length; i++) {
			this.appendChild(this.importNode(doc2.childNodes[i], true));
		}
	};
	
	var tmp = function () {
			return (new XMLSerializer()).serializeToString(this);
	};		
	Element.prototype.__defineGetter__("xml", tmp);
	XMLDocument.prototype.__defineGetter__("xml", tmp);	
	Document.prototype.__defineGetter__("xml", tmp);
}

function emulateDFish() {
	
	DFish.emu = {
		mx_file : {
			// for firefox only
			over : function( a ) {
				var b = a.id.replace( '-lnk', '' ),
					x = VM.MX.File._a[ b ].x,
					c = VM( x.vmid ).fmul(),
					d = c.firstChild.childNodes,
					e = x.n + '-',
					f = js.find( d, '!v.value&&v.name.indexOf("' + e + '")==0', null, true );
				
				if ( f < 0 )
					$.append( c.firstChild, '<input type=file name=' + e + js.uid() + ' mxid=' + x.mxid + ' onchange=top.VM.MX.File.cat(this,"' + b + '") onmouseout=top.DFish.emu.mx_file.out("' + c.name + '")>' );
				var h = $( c.name ),
					k = $.bcr( a );
				with ( h.style ) {
					width  = ( k.width + 20 ) + 'px';
					height = '23px';
					top  = ( k.top - 3 ) + 'px';
					left = ( k.left - 16 ) + 'px';
					position = 'absolute';
				}
				h.contentWindow.document.body.scrollTop = ( f < 0 ? d.length - 1 : f ) * 22;
			},
			out : function( a ) {
				var h = $( a );
				h.style.height = '0px';
				h.style.top = h.style.left = '-1px';
			}
		}
	}
		
}