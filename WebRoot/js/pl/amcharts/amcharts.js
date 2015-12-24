if (!AmCharts) var AmCharts = {};
AmCharts.themes = {};
AmCharts.inheriting = {};
AmCharts.Class = function(a) {
	var b = function() {
		arguments[0] !== AmCharts.inheriting && (this.events = {},
		this.construct.apply(this, arguments))
	};
	a.inherits ? (b.prototype = new a.inherits(AmCharts.inheriting), b.base = a.inherits.prototype, delete a.inherits) : (b.prototype.createEvents = function() {
		for (var a = 0,
		b = arguments.length; a < b; a++) this.events[arguments[a]] = []
	},
	b.prototype.listenTo = function(a, b, c) {
		this.removeListener(a, b, c);
		a.events[b].push({
			handler: c,
			scope: this
		})
	},
	b.prototype.addListener = function(a, b, c) {
		this.removeListener(this, a, b);
		this.events[a].push({
			handler: b,
			scope: c
		})
	},
	b.prototype.removeListener = function(a, b, c) {
		if (a && a.events) for (a = a.events[b], b = a.length - 1; 0 <= b; b--) a[b].handler === c && a.splice(b, 1)
	},
	b.prototype.fire = function(a, b) {
		for (var c = this.events[a], g = 0, h = c.length; g < h; g++) {
			var k = c[g];
			k.handler.call(k.scope, b)
		}
	});
	for (var c in a) b.prototype[c] = a[c];
	return b
};
AmCharts.charts = [];
AmCharts.addChart = function(a) {
	AmCharts.charts.push(a)
};
AmCharts.removeChart = function(a) {
	for (var b = AmCharts.charts,
	c = b.length - 1; 0 <= c; c--) b[c] == a && b.splice(c, 1)
};
AmCharts.IEversion = 0;
AmCharts.isModern = !0;
AmCharts.navigator = navigator.userAgent.toLowerCase(); - 1 != AmCharts.navigator.indexOf("msie") && (AmCharts.IEversion = parseInt(AmCharts.navigator.split("msie")[1]), document.documentMode && (AmCharts.IEversion = Number(document.documentMode)), 9 > AmCharts.IEversion && (AmCharts.isModern = !1));
AmCharts.dx = 0;
AmCharts.dy = 0;
if (document.addEventListener || window.opera) AmCharts.isNN = !0,
AmCharts.isIE = !1,
AmCharts.dx = 0.5,
AmCharts.dy = 0.5;
document.attachEvent && (AmCharts.isNN = !1, AmCharts.isIE = !0, AmCharts.isModern || (AmCharts.dx = 0, AmCharts.dy = 0));
window.chrome && (AmCharts.chrome = !0);
AmCharts.handleResize = function() {
	for (var a = AmCharts.charts,
	b = 0; b < a.length; b++) {
		var c = a[b];
		c && c.div && c.handleResize()
	}
};
AmCharts.handleMouseUp = function(a) {
	for (var b = AmCharts.charts,
	c = 0; c < b.length; c++) {
		var d = b[c];
		d && d.handleReleaseOutside(a)
	}
};
AmCharts.handleMouseMove = function(a) {
	for (var b = AmCharts.charts,
	c = 0; c < b.length; c++) {
		var d = b[c];
		d && d.handleMouseMove(a)
	}
};
AmCharts.resetMouseOver = function() {
	for (var a = AmCharts.charts,
	b = 0; b < a.length; b++) {
		var c = a[b];
		c && (c.mouseIsOver = !1)
	}
};
AmCharts.onReadyArray = [];
AmCharts.ready = function(a) {
	AmCharts.onReadyArray.push(a)
};
AmCharts.handleLoad = function() {
	AmCharts.isReady = !0;
	for (var a = AmCharts.onReadyArray,
	b = 0; b < a.length; b++)(0, a[b])()
};
AmCharts.useUTC = !1;
AmCharts.updateRate = 40;
AmCharts.uid = 0;
AmCharts.getUniqueId = function() {
	AmCharts.uid++;
	return "AmChartsEl-" + AmCharts.uid
};
AmCharts.isNN && (document.addEventListener("mousemove", AmCharts.handleMouseMove, !0), window.addEventListener("resize", AmCharts.handleResize, !0), document.addEventListener("mouseup", AmCharts.handleMouseUp, !0), window.addEventListener("load", AmCharts.handleLoad, !0));
AmCharts.isIE && (document.attachEvent("onmousemove", AmCharts.handleMouseMove), window.attachEvent("onresize", AmCharts.handleResize), document.attachEvent("onmouseup", AmCharts.handleMouseUp), window.attachEvent("onload", AmCharts.handleLoad));
AmCharts.clear = function() {
	var a = AmCharts.charts;
	if (a) for (var b = 0; b < a.length; b++) a[b].clear();
	AmCharts.charts = null;
	AmCharts.isNN && (document.removeEventListener("mousemove", AmCharts.handleMouseMove, !0), window.removeEventListener("resize", AmCharts.handleResize, !0), document.removeEventListener("mouseup", AmCharts.handleMouseUp, !0), window.removeEventListener("load", AmCharts.handleLoad, !0));
	AmCharts.isIE && (document.detachEvent("onmousemove", AmCharts.handleMouseMove), window.detachEvent("onresize", AmCharts.handleResize), document.detachEvent("onmouseup", AmCharts.handleMouseUp), window.detachEvent("onload", AmCharts.handleLoad))
};
AmCharts.makeChart = function(a, b) {
	var c = b.type,
	d = b.theme;
	AmCharts.isString(d) && (d = AmCharts.themes[d], b.theme = d);
	var e;
	switch (c) {
	case "serial":
		e = new AmCharts.AmSerialChart(d);
		break;
	case "xy":
		e = new AmCharts.AmXYChart(d);
		break;
	case "pie":
		e = new AmCharts.AmPieChart(d);
		break;
	case "radar":
		e = new AmCharts.AmRadarChart(d);
		break;
	case "gauge":
		e = new AmCharts.AmAngularGauge(d);
		break;
	case "funnel":
		e = new AmCharts.AmFunnelChart(d);
		break;
	case "map":
		e = new AmCharts.AmMap(d);
		break;
	case "stock":
		e = new AmCharts.AmStockChart(d)
	}
	AmCharts.extend(e, b);
	AmCharts.isReady ? e.write(a) : AmCharts.ready(function() {
		e.write(a)
	});
	return e
};
AmCharts.toBoolean = function(a, b) {
	if (void 0 === a) return b;
	switch (String(a).toLowerCase()) {
	case "true":
	case "yes":
	case "1":
		return ! 0;
	case "false":
	case "no":
	case "0":
	case null:
		return ! 1;
	default:
		return Boolean(a)
	}
};
AmCharts.removeFromArray = function(a, b) {
	var c;
	for (c = a.length - 1; 0 <= c; c--) a[c] == b && a.splice(c, 1)
};
AmCharts.getDecimals = function(a) {
	var b = 0;
	isNaN(a) || (a = String(a), -1 != a.indexOf("e-") ? b = Number(a.split("-")[1]) : -1 != a.indexOf(".") && (b = a.split(".")[1].length));
	return b
};
AmCharts.wrappedText = function(a, b, c, d, e, f, g, h, k) {
	var l = AmCharts.text(a, b, c, d, e, f, g),
	m = "\n";
	AmCharts.isModern || (m = "<br>");
	if (10 < k) return l;
	if (l) {
		var n = l.getBBox();
		if (n.width > h) {
			l.remove();
			for (var l = [], p = 0; - 1 < (index = b.indexOf(" ", p));) l.push(index),
			p = index + 1;
			for (var r = Math.round(b.length / 2), u = 1E3, q, p = 0; p < l.length; p++) {
				var t = Math.abs(l[p] - r);
				t < u && (q = l[p], u = t)
			}
			if (isNaN(q)) {
				h = Math.ceil(n.width / h);
				for (p = 1; p < h; p++) q = Math.round(b.length / h * p),
				b = b.substr(0, q) + m + b.substr(q);
				return AmCharts.text(a, b, c, d, e, f, g)
			}
			b = b.substr(0, q) + m + b.substr(q + 1);
			return AmCharts.wrappedText(a, b, c, d, e, f, g, h, k + 1)
		}
		return l
	}
};
AmCharts.getStyle = function(a, b) {
	var c = "";
	document.defaultView && document.defaultView.getComputedStyle ? c = document.defaultView.getComputedStyle(a, "").getPropertyValue(b) : a.currentStyle && (b = b.replace(/\-(\w)/g,
	function(a, b) {
		return b.toUpperCase()
	}), c = a.currentStyle[b]);
	return c
};
AmCharts.removePx = function(a) {
	return Number(a.substring(0, a.length - 2))
};
AmCharts.getURL = function(a, b) {
	if (a) if ("_self" != b && b) if ("_top" == b && window.top) window.top.location.href = a;
	else if ("_parent" == b && window.parent) window.parent.location.href = a;
	else {
		var c = document.getElementsByName(b)[0];
		c ? c.src = a: window.open(a)
	} else window.location.href = a
};
AmCharts.ifArray = function(a) {
	return a && 0 < a.length ? !0 : !1
};
AmCharts.callMethod = function(a, b) {
	var c;
	for (c = 0; c < b.length; c++) {
		var d = b[c];
		if (d) {
			if (d[a]) d[a]();
			var e = d.length;
			if (0 < e) {
				var f;
				for (f = 0; f < e; f++) {
					var g = d[f];
					if (g && g[a]) g[a]()
				}
			}
		}
	}
};
AmCharts.toNumber = function(a) {
	return "number" == typeof a ? a: Number(String(a).replace(/[^0-9\-.]+/g, ""))
};
AmCharts.toColor = function(a) {
	if ("" !== a && void 0 !== a) if ( - 1 != a.indexOf(",")) {
		a = a.split(",");
		var b;
		for (b = 0; b < a.length; b++) {
			var c = a[b].substring(a[b].length - 6, a[b].length);
			a[b] = "#" + c
		}
	} else a = a.substring(a.length - 6, a.length),
	a = "#" + a;
	return a
};
AmCharts.toCoordinate = function(a, b, c) {
	var d;
	void 0 !== a && (a = String(a), c && c < b && (b = c), d = Number(a), -1 != a.indexOf("!") && (d = b - Number(a.substr(1))), -1 != a.indexOf("%") && (d = b * Number(a.substr(0, a.length - 1)) / 100));
	return d
};
AmCharts.fitToBounds = function(a, b, c) {
	a < b && (a = b);
	a > c && (a = c);
	return a
};
AmCharts.isDefined = function(a) {
	return void 0 === a ? !1 : !0
};
AmCharts.stripNumbers = function(a) {
	return a.replace(/[0-9]+/g, "")
};
AmCharts.roundTo = function(a, b) {
	if (0 > b) return a;
	var c = Math.pow(10, b);
	return Math.round(a * c) / c
};
AmCharts.toFixed = function(a, b) {
	var c = String(Math.round(a * Math.pow(10, b)));
	if (0 < b) {
		var d = c.length;
		if (d < b) {
			var e;
			for (e = 0; e < b - d; e++) c = "0" + c
		}
		d = c.substring(0, c.length - b);
		"" === d && (d = 0);
		return d + "." + c.substring(c.length - b, c.length)
	}
	return String(c)
};
AmCharts.formatDuration = function(a, b, c, d, e, f) {
	var g = AmCharts.intervals,
	h = f.decimalSeparator;
	if (a >= g[b].contains) {
		var k = a - Math.floor(a / g[b].contains) * g[b].contains;
		"ss" == b && (k = AmCharts.formatNumber(k, f), 1 == k.split(h)[0].length && (k = "0" + k)); ("mm" == b || "hh" == b) && 10 > k && (k = "0" + k);
		c = k + "" + d[b] + "" + c;
		a = Math.floor(a / g[b].contains);
		b = g[b].nextInterval;
		return AmCharts.formatDuration(a, b, c, d, e, f)
	}
	"ss" == b && (a = AmCharts.formatNumber(a, f), 1 == a.split(h)[0].length && (a = "0" + a)); ("mm" == b || "hh" == b) && 10 > a && (a = "0" + a);
	c = a + "" + d[b] + "" + c;
	if (g[e].count > g[b].count) for (a = g[b].count; a < g[e].count; a++) b = g[b].nextInterval,
	"ss" == b || "mm" == b || "hh" == b ? c = "00" + d[b] + "" + c: "DD" == b && (c = "0" + d[b] + "" + c);
	":" == c.charAt(c.length - 1) && (c = c.substring(0, c.length - 1));
	return c
};
AmCharts.formatNumber = function(a, b, c, d, e) {
	a = AmCharts.roundTo(a, b.precision);
	isNaN(c) && (c = b.precision);
	var f = b.decimalSeparator;
	b = b.thousandsSeparator;
	var g;
	g = 0 > a ? "-": "";
	a = Math.abs(a);
	var h = String(a),
	k = !1; - 1 != h.indexOf("e") && (k = !0);
	0 <= c && !k && (h = AmCharts.toFixed(a, c));
	var l = "";
	if (k) l = h;
	else {
		var h = h.split("."),
		k = String(h[0]),
		m;
		for (m = k.length; 0 <= m; m -= 3) l = m != k.length ? 0 !== m ? k.substring(m - 3, m) + b + l: k.substring(m - 3, m) + l: k.substring(m - 3, m);
		void 0 !== h[1] && (l = l + f + h[1]);
		void 0 !== c && 0 < c && "0" != l && (l = AmCharts.addZeroes(l, f, c))
	}
	l = g + l;
	"" === g && !0 === d && 0 !== a && (l = "+" + l); ! 0 === e && (l += "%");
	return l
};
AmCharts.addZeroes = function(a, b, c) {
	a = a.split(b);
	void 0 === a[1] && 0 < c && (a[1] = "0");
	return a[1].length < c ? (a[1] += "0", AmCharts.addZeroes(a[0] + b + a[1], b, c)) : void 0 !== a[1] ? a[0] + b + a[1] : a[0]
};
AmCharts.scientificToNormal = function(a) {
	var b;
	a = String(a).split("e");
	var c;
	if ("-" == a[1].substr(0, 1)) {
		b = "0.";
		for (c = 0; c < Math.abs(Number(a[1])) - 1; c++) b += "0";
		b += a[0].split(".").join("")
	} else {
		var d = 0;
		b = a[0].split(".");
		b[1] && (d = b[1].length);
		b = a[0].split(".").join("");
		for (c = 0; c < Math.abs(Number(a[1])) - d; c++) b += "0"
	}
	return b
};
AmCharts.toScientific = function(a, b) {
	if (0 === a) return "0";
	var c = Math.floor(Math.log(Math.abs(a)) * Math.LOG10E);
	Math.pow(10, c);
	mantissa = String(mantissa).split(".").join(b);
	return String(mantissa) + "e" + c
};
AmCharts.randomColor = function() {
	return "#" + ("00000" + (16777216 * Math.random() << 0).toString(16)).substr( - 6)
};
AmCharts.hitTest = function(a, b, c) {
	var d = !1,
	e = a.x,
	f = a.x + a.width,
	g = a.y,
	h = a.y + a.height,
	k = AmCharts.isInRectangle;
	d || (d = k(e, g, b));
	d || (d = k(e, h, b));
	d || (d = k(f, g, b));
	d || (d = k(f, h, b));
	d || !0 === c || (d = AmCharts.hitTest(b, a, !0));
	return d
};
AmCharts.isInRectangle = function(a, b, c) {
	return a >= c.x - 5 && a <= c.x + c.width + 5 && b >= c.y - 5 && b <= c.y + c.height + 5 ? !0 : !1
};
AmCharts.isPercents = function(a) {
	if ( - 1 != String(a).indexOf("%")) return ! 0
};
AmCharts.findPosX = function(a) {
	var b = a,
	c = a.offsetLeft;
	if (a.offsetParent) {
		for (; a = a.offsetParent;) c += a.offsetLeft;
		for (; (b = b.parentNode) && b != document.body;) c -= b.scrollLeft || 0
	}
	return c
};
AmCharts.findPosY = function(a) {
	var b = a,
	c = a.offsetTop;
	if (a.offsetParent) {
		for (; a = a.offsetParent;) c += a.offsetTop;
		for (; (b = b.parentNode) && b != document.body;) c -= b.scrollTop || 0
	}
	return c
};
AmCharts.findIfFixed = function(a) {
	if (a.offsetParent) for (; a = a.offsetParent;) if ("fixed" == AmCharts.getStyle(a, "position")) return ! 0;
	return ! 1
};
AmCharts.findIfAuto = function(a) {
	return a.style && "auto" == AmCharts.getStyle(a, "overflow") ? !0 : a.parentNode ? AmCharts.findIfAuto(a.parentNode) : !1
};
AmCharts.findScrollLeft = function(a, b) {
	a.scrollLeft && (b += a.scrollLeft);
	return a.parentNode ? AmCharts.findScrollLeft(a.parentNode, b) : b
};
AmCharts.findScrollTop = function(a, b) {
	a.scrollTop && (b += a.scrollTop);
	return a.parentNode ? AmCharts.findScrollTop(a.parentNode, b) : b
};
AmCharts.formatValue = function(a, b, c, d, e, f, g, h) {
	if (b) {
		void 0 === e && (e = "");
		var k;
		for (k = 0; k < c.length; k++) {
			var l = c[k],
			m = b[l];
			void 0 !== m && (m = f ? AmCharts.addPrefix(m, h, g, d) : AmCharts.formatNumber(m, d), a = a.replace(RegExp("\\[\\[" + e + "" + l + "\\]\\]", "g"), m))
		}
	}
	return a
};
AmCharts.formatDataContextValue = function(a, b) {
	if (a) {
		var c = a.match(/\[\[.*?\]\]/g),
		d;
		for (d = 0; d < c.length; d++) {
			var e = c[d],
			e = e.substr(2, e.length - 4);
			void 0 !== b[e] && (a = a.replace(RegExp("\\[\\[" + e + "\\]\\]", "g"), b[e]))
		}
	}
	return a
};
AmCharts.massReplace = function(a, b) {
	for (var c in b) if (b.hasOwnProperty(c)) {
		var d = b[c];
		void 0 === d && (d = "");
		a = a.replace(c, d)
	}
	return a
};
AmCharts.cleanFromEmpty = function(a) {
	return a.replace(/\[\[[^\]]*\]\]/g, "")
};
AmCharts.addPrefix = function(a, b, c, d, e) {
	var f = AmCharts.formatNumber(a, d),
	g = "",
	h,
	k,
	l;
	if (0 === a) return "0";
	0 > a && (g = "-");
	a = Math.abs(a);
	if (1 < a) for (h = b.length - 1; - 1 < h; h--) {
		if (a >= b[h].number && (k = a / b[h].number, l = Number(d.precision), 1 > l && (l = 1), c = AmCharts.roundTo(k, l), l = AmCharts.formatNumber(c, {
			precision: -1,
			decimalSeparator: d.decimalSeparator,
			thousandsSeparator: d.thousandsSeparator
		}), !e || k == c)) {
			f = g + "" + l + "" + b[h].prefix;
			break
		}
	} else for (h = 0; h < c.length; h++) if (a <= c[h].number) {
		k = a / c[h].number;
		l = Math.abs(Math.round(Math.log(k) * Math.LOG10E));
		k = AmCharts.roundTo(k, l);
		f = g + "" + k + "" + c[h].prefix;
		break
	}
	return f
};
AmCharts.remove = function(a) {
	a && a.remove()
};
AmCharts.recommended = function() {
	var a = "js";
	document.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1") || swfobject && swfobject.hasFlashPlayerVersion("8") && (a = "flash");
	return a
};
AmCharts.getEffect = function(a) {
	">" == a && (a = "easeOutSine");
	"<" == a && (a = "easeInSine");
	"elastic" == a && (a = "easeOutElastic");
	return a
};
AmCharts.getObjById = function(a, b) {
	var c, d;
	for (d = 0; d < a.length; d++) {
		var e = a[d];
		e.id == b && (c = e)
	}
	return c
};
AmCharts.applyTheme = function(a, b, c) {
	b || (b = AmCharts.theme);
	b && b[c] && AmCharts.extend(a, b[c])
};
AmCharts.isString = function(a) {
	return "string" == typeof a ? !0 : !1
};
AmCharts.extend = function(a, b, c) {
	for (var d in b) c ? a.hasOwnProperty(d) || (a[d] = b[d]) : a[d] = b[d];
	return a
};
AmCharts.copyProperties = function(a, b) {
	for (var c in a) a.hasOwnProperty(c) && "events" != c && void 0 !== a[c] && "function" != typeof a[c] && "cname" != c && (b[c] = a[c])
};
AmCharts.processObject = function(a, b, c) { ! 1 === a instanceof b && (a = AmCharts.extend(new b(c), a));
	return a
};
AmCharts.fixNewLines = function(a) {
	var b = RegExp("\\n", "g");
	a && (a = a.replace(b, "<br />"));
	return a
};
AmCharts.fixBrakes = function(a) {
	if (AmCharts.isModern) {
		var b = RegExp("<br>", "g");
		a && (a = a.replace(b, "\n"))
	} else a = AmCharts.fixNewLines(a);
	return a
};
AmCharts.deleteObject = function(a, b) {
	if (a) {
		if (void 0 === b || null === b) b = 20;
		if (0 !== b) if ("[object Array]" === Object.prototype.toString.call(a)) for (var c = 0; c < a.length; c++) AmCharts.deleteObject(a[c], b - 1),
		a[c] = null;
		else if (a && !a.tagName) try {
			for (c in a) a[c] && ("object" == typeof a[c] && AmCharts.deleteObject(a[c], b - 1), "function" != typeof a[c] && (a[c] = null))
		} catch(d) {}
	}
};
AmCharts.bounce = function(a, b, c, d, e) {
	return (b /= e) < 1 / 2.75 ? 7.5625 * d * b * b + c: b < 2 / 2.75 ? d * (7.5625 * (b -= 1.5 / 2.75) * b + 0.75) + c: b < 2.5 / 2.75 ? d * (7.5625 * (b -= 2.25 / 2.75) * b + 0.9375) + c: d * (7.5625 * (b -= 2.625 / 2.75) * b + 0.984375) + c
};
AmCharts.easeInSine = function(a, b, c, d, e) {
	return - d * Math.cos(b / e * (Math.PI / 2)) + d + c
};
AmCharts.easeOutSine = function(a, b, c, d, e) {
	return d * Math.sin(b / e * (Math.PI / 2)) + c
};
AmCharts.easeOutElastic = function(a, b, c, d, e) {
	a = 1.70158;
	var f = 0,
	g = d;
	if (0 === b) return c;
	if (1 == (b /= e)) return c + d;
	f || (f = 0.3 * e);
	g < Math.abs(d) ? (g = d, a = f / 4) : a = f / (2 * Math.PI) * Math.asin(d / g);
	return g * Math.pow(2, -10 * b) * Math.sin(2 * (b * e - a) * Math.PI / f) + d + c
};
AmCharts.AxisBase = AmCharts.Class({
	construct: function(a) {
		this.viY = this.viX = this.y = this.x = this.dy = this.dx = 0;
		this.axisThickness = 1;
		this.axisColor = "#000000";
		this.axisAlpha = 1;
		this.gridCount = this.tickLength = 5;
		this.gridAlpha = 0.15;
		this.gridThickness = 1;
		this.gridColor = "#000000";
		this.dashLength = 0;
		this.labelFrequency = 1;
		this.showLastLabel = this.showFirstLabel = !0;
		this.fillColor = "#FFFFFF";
		this.fillAlpha = 0;
		this.labelsEnabled = !0;
		this.labelRotation = 0;
		this.autoGridCount = !0;
		this.valueRollOverColor = "#CC0000";
		this.offset = 0;
		this.guides = [];
		this.visible = !0;
		this.counter = 0;
		this.guides = [];
		this.ignoreAxisWidth = this.inside = !1;
		this.minHorizontalGap = 75;
		this.minVerticalGap = 35;
		this.titleBold = !0;
		this.minorGridEnabled = !1;
		this.minorGridAlpha = 0.07;
		this.autoWrap = !1;
		AmCharts.applyTheme(this, a, "AxisBase")
	},
	zoom: function(a, b) {
		this.start = a;
		this.end = b;
		this.dataChanged = !0;
		this.draw()
	},
	fixAxisPosition: function() {
		var a = this.position;
		"H" == this.orientation ? ("left" == a && (a = "bottom"), "right" == a && (a = "top")) : ("bottom" == a && (a = "left"), "top" == a && (a = "right"));
		this.position = a
	},
	draw: function() {
		var a = this.chart;
		this.allLabels = [];
		this.counter = 0;
		this.destroy();
		this.fixAxisPosition();
		this.labels = [];
		var b = a.container,
		c = b.set();
		a.gridSet.push(c);
		this.set = c;
		b = b.set();
		a.axesLabelsSet.push(b);
		this.labelsSet = b;
		this.axisLine = new this.axisRenderer(this);
		this.autoGridCount ? ("V" == this.orientation ? (a = this.height / this.minVerticalGap, 3 > a && (a = 3)) : a = this.width / this.minHorizontalGap, this.gridCountR = Math.max(a, 1)) : this.gridCountR = this.gridCount;
		this.axisWidth = this.axisLine.axisWidth;
		this.addTitle()
	},
	setOrientation: function(a) {
		this.orientation = a ? "H": "V"
	},
	addTitle: function() {
		var a = this.title;
		if (a) {
			var b = this.chart,
			c = this.titleColor;
			void 0 === c && (c = b.color);
			var d = this.titleFontSize;
			isNaN(d) && (d = b.fontSize + 1);
			this.titleLabel = AmCharts.text(b.container, a, c, b.fontFamily, d, "middle", this.titleBold)
		}
	},
	positionTitle: function() {
		var a = this.titleLabel;
		if (a) {
			var b, c, d = this.labelsSet,
			e = {};
			0 < d.length() ? e = d.getBBox() : (e.x = 0, e.y = 0, e.width = this.viW, e.height = this.viH);
			d.push(a);
			var d = e.x,
			f = e.y;
			AmCharts.VML && (this.rotate ? d -= this.x: f -= this.y);
			var g = e.width,
			e = e.height,
			h = this.viW,
			k = this.viH,
			l = 0,
			m = a.getBBox().height / 2,
			n = this.inside;
			switch (this.position) {
			case "top":
				b = h / 2;
				c = f - 10 - m;
				break;
			case "bottom":
				b = h / 2;
				c = f + e + 10 + m;
				break;
			case "left":
				b = d - 10 - m;
				n && (b -= 5);
				c = k / 2;
				l = -90;
				break;
			case "right":
				b = d + g + 10 + m - 3,
				n && (b += 7),
				c = k / 2,
				l = -90
			}
			this.marginsChanged ? (a.translate(b, c), this.tx = b, this.ty = c) : a.translate(this.tx, this.ty);
			this.marginsChanged = !1;
			0 !== l && a.rotate(l)
		}
	},
	pushAxisItem: function(a, b) {
		var c = a.graphics();
		0 < c.length() && (b ? this.labelsSet.push(c) : this.set.push(c)); (c = a.getLabel()) && this.labelsSet.push(c)
	},
	addGuide: function(a) {
		this.guides.push(a)
	},
	removeGuide: function(a) {
		var b = this.guides,
		c;
		for (c = 0; c < b.length; c++) b[c] == a && b.splice(c, 1)
	},
	handleGuideOver: function(a) {
		clearTimeout(this.chart.hoverInt);
		var b = a.graphics.getBBox(),
		c = b.x + b.width / 2,
		b = b.y + b.height / 2,
		d = a.fillColor;
		void 0 === d && (d = a.lineColor);
		this.chart.showBalloon(a.balloonText, d, !0, c, b)
	},
	handleGuideOut: function(a) {
		this.chart.hideBalloon()
	},
	addEventListeners: function(a, b) {
		var c = this;
		a.mouseover(function() {
			c.handleGuideOver(b)
		});
		a.mouseout(function() {
			c.handleGuideOut(b)
		})
	},
	getBBox: function() {
		var a = this.labelsSet.getBBox();
		AmCharts.VML || (a = {
			x: a.x + this.x,
			y: a.y + this.y,
			width: a.width,
			height: a.height
		});
		return a
	},
	destroy: function() {
		AmCharts.remove(this.set);
		AmCharts.remove(this.labelsSet);
		var a = this.axisLine;
		a && AmCharts.remove(a.set);
		AmCharts.remove(this.grid0)
	}
});
AmCharts.ValueAxis = AmCharts.Class({
	inherits: AmCharts.AxisBase,
	construct: function(a) {
		this.cname = "ValueAxis";
		this.createEvents("axisChanged", "logarithmicAxisFailed", "axisSelfZoomed", "axisZoomed");
		AmCharts.ValueAxis.base.construct.call(this, a);
		this.dataChanged = !0;
		this.stackType = "none";
		this.position = "left";
		this.unitPosition = "right";
		this.recalculateToPercents = this.includeHidden = this.includeGuidesInMinMax = this.integersOnly = !1;
		this.durationUnits = {
			DD: "d. ",
			hh: ":",
			mm: ":",
			ss: ""
		};
		this.scrollbar = !1;
		this.baseValue = 0;
		this.radarCategoriesEnabled = !0;
		this.gridType = "polygons";
		this.useScientificNotation = !1;
		this.axisTitleOffset = 10;
		this.minMaxMultiplier = 1;
		AmCharts.applyTheme(this, a, this.cname)
	},
	updateData: function() {
		0 >= this.gridCountR && (this.gridCountR = 1);
		this.totals = [];
		this.data = this.chart.chartData;
		var a = this.chart;
		"xy" != a.type && (this.stackGraphs("smoothedLine"), this.stackGraphs("line"), this.stackGraphs("column"), this.stackGraphs("step"));
		this.recalculateToPercents && this.recalculate();
		this.synchronizationMultiplier && this.synchronizeWith ? (AmCharts.isString(this.synchronizeWith) && (this.synchronizeWith = a.getValueAxisById(this.synchronizeWith)), this.synchronizeWith && (this.synchronizeWithAxis(this.synchronizeWith), this.foundGraphs = !0)) : (this.foundGraphs = !1, this.getMinMax())
	},
	draw: function() {
		AmCharts.ValueAxis.base.draw.call(this);
		var a = this.chart,
		b = this.set;
		"duration" == this.type && (this.duration = "ss"); ! 0 === this.dataChanged && (this.updateData(), this.dataChanged = !1);
		if (this.logarithmic && (0 >= this.getMin(0, this.data.length - 1) || 0 >= this.minimum)) this.fire("logarithmicAxisFailed", {
			type: "logarithmicAxisFailed",
			chart: a
		});
		else {
			this.grid0 = null;
			var c, d, e = a.dx,
			f = a.dy,
			g = !1,
			h = this.logarithmic;
			if (isNaN(this.min) || isNaN(this.max) || !this.foundGraphs || Infinity == this.min || -Infinity == this.max) g = !0;
			else {
				var k = this.labelFrequency,
				l = this.showFirstLabel,
				m = this.showLastLabel,
				n = 1,
				p = 0,
				r = Math.round((this.max - this.min) / this.step) + 1,
				u; ! 0 === h ? (u = Math.log(this.max) * Math.LOG10E - Math.log(this.minReal) * Math.LOG10E, this.stepWidth = this.axisWidth / u, 2 < u && (r = Math.ceil(Math.log(this.max) * Math.LOG10E) + 1, p = Math.round(Math.log(this.minReal) * Math.LOG10E), r > this.gridCountR && (n = Math.ceil(r / this.gridCountR)))) : this.stepWidth = this.axisWidth / (this.max - this.min);
				var q = 0;
				1 > this.step && -1 < this.step && (q = AmCharts.getDecimals(this.step));
				this.integersOnly && (q = 0);
				q > this.maxDecCount && (q = this.maxDecCount);
				var t = this.precision;
				isNaN(t) || (q = t);
				this.max = AmCharts.roundTo(this.max, this.maxDecCount);
				this.min = AmCharts.roundTo(this.min, this.maxDecCount);
				var v = {};
				v.precision = q;
				v.decimalSeparator = a.numberFormatter.decimalSeparator;
				v.thousandsSeparator = a.numberFormatter.thousandsSeparator;
				this.numberFormatter = v;
				var s, w = this.guides,
				A = w.length;
				if (0 < A) {
					c = this.fillAlpha;
					for (d = this.fillAlpha = 0; d < A; d++) {
						var C = w[d],
						y = NaN,
						z = C.above;
						isNaN(C.toValue) || (y = this.getCoordinate(C.toValue), s = new this.axisItemRenderer(this, y, "", !0, NaN, NaN, C), this.pushAxisItem(s, z));
						var J = NaN;
						isNaN(C.value) || (J = this.getCoordinate(C.value), s = new this.axisItemRenderer(this, J, C.label, !0, NaN, (y - J) / 2, C), this.pushAxisItem(s, z));
						isNaN(y - J) || (s = new this.guideFillRenderer(this, J, y, C), this.pushAxisItem(s, z), s = s.graphics(), C.graphics = s, C.balloonText && this.addEventListeners(s, C))
					}
					this.fillAlpha = c
				}
				w = !1;
				for (d = p; d < r; d += n) A = AmCharts.roundTo(this.step * d + this.min, q),
				-1 != String(A).indexOf("e") && (w = !0, String(A).split("e"));
				this.duration && (this.maxInterval = AmCharts.getMaxInterval(this.max, this.duration));
				var q = this.step,
				I, A = this.minorGridAlpha;
				this.minorGridEnabled && (I = this.getMinorGridStep(q, this.stepWidth * q));
				for (d = p; d < r; d += n) if (p = q * d + this.min, p = AmCharts.roundTo(p, this.maxDecCount + 1), !this.integersOnly || Math.round(p) == p) if (isNaN(t) || Number(AmCharts.toFixed(p, t)) == p) { ! 0 === h && (0 === p && (p = this.minReal), 2 < u && (p = Math.pow(10, d)), w = -1 != String(p).indexOf("e") ? !0 : !1);
					this.useScientificNotation && (w = !0);
					this.usePrefixes && (w = !1);
					w ? (s = -1 == String(p).indexOf("e") ? p.toExponential(15) : String(p), c = s.split("e"), s = Number(c[0]), c = Number(c[1]), s = AmCharts.roundTo(s, 14), 10 == s && (s = 1, c += 1), s = s + "e" + c, 0 === p && (s = "0"), 1 == p && (s = "1")) : (h && (s = String(p).split("."), v.precision = s[1] ? s[1].length: -1), s = this.usePrefixes ? AmCharts.addPrefix(p, a.prefixesOfBigNumbers, a.prefixesOfSmallNumbers, v, !0) : AmCharts.formatNumber(p, v, v.precision));
					this.duration && (s = AmCharts.formatDuration(p, this.duration, "", this.durationUnits, this.maxInterval, v));
					this.recalculateToPercents ? s += "%": (c = this.unit) && (s = "left" == this.unitPosition ? c + s: s + c);
					Math.round(d / k) != d / k && (s = void 0);
					if (0 === d && !l || d == r - 1 && !m) s = " ";
					c = this.getCoordinate(p);
					this.labelFunction && (s = this.labelFunction(p, s, this).toString());
					s = new this.axisItemRenderer(this, c, s);
					this.pushAxisItem(s);
					if (p == this.baseValue && "radar" != a.type) {
						var B, Z, z = this.viW,
						y = this.viH;
						s = this.viX;
						C = this.viY;
						"H" == this.orientation ? 0 <= c && c <= z + 1 && (B = [c, c, c + e], Z = [y, 0, f]) : 0 <= c && c <= y + 1 && (B = [0, z, z + e], Z = [c, c, c + f]);
						B && (c = AmCharts.fitToBounds(2 * this.gridAlpha, 0, 1), c = AmCharts.line(a.container, B, Z, this.gridColor, c, 1, this.dashLength), c.translate(s, C), this.grid0 = c, a.axesSet.push(c), c.toBack())
					}
					if (!isNaN(I) && 0 < A && d < r - 1) {
						s = this.gridAlpha;
						this.gridAlpha = this.minorGridAlpha;
						for (c = 1; c < q / I; c++) C = this.getCoordinate(p + I * c),
						C = new this.axisItemRenderer(this, C, "", !1, 0, 0, !1, !1, 0, !0),
						this.pushAxisItem(C);
						this.gridAlpha = s
					}
				}
				d = this.baseValue;
				this.min > this.baseValue && this.max > this.baseValue && (d = this.min);
				this.min < this.baseValue && this.max < this.baseValue && (d = this.max);
				h && d < this.minReal && (d = this.minReal);
				this.baseCoord = this.getCoordinate(d);
				d = {
					type: "axisChanged",
					target: this,
					chart: a
				};
				d.min = h ? this.minReal: this.min;
				d.max = this.max;
				this.fire("axisChanged", d);
				this.axisCreated = !0
			}
			h = this.axisLine.set;
			d = this.labelsSet;
			this.positionTitle();
			"radar" != a.type ? (a = this.viX, e = this.viY, b.translate(a, e), d.translate(a, e)) : h.toFront(); ! this.visible || g ? (b.hide(), h.hide(), d.hide()) : (b.show(), h.show(), d.show());
			this.axisY = this.y - this.viY;
			this.axisX = this.x - this.viX
		}
	},
	getMinorGridStep: function(a, b) {
		var c = [5, 4, 2];
		60 > b && c.shift();
		for (var d = Math.floor(Math.log(Math.abs(a)) * Math.LOG10E), e = 0; e < c.length; e++) {
			var f = a / c[e],
			g = Math.floor(Math.log(Math.abs(f)) * Math.LOG10E);
			if (! (0 < Math.abs(d - g))) if (1 > a) {
				if (g = Math.pow(10, -g) * f, g == Math.round(g)) return f
			} else if (f == Math.round(f)) return f
		}
	},
	stackGraphs: function(a) {
		var b = this.stackType;
		"stacked" == b && (b = "regular");
		"line" == b && (b = "none");
		"100% stacked" == b && (b = "100%");
		this.stackType = b;
		var c = [],
		d = [],
		e = [],
		f = [],
		g,
		h = this.chart.graphs,
		k,
		l,
		m,
		n,
		p = this.baseValue,
		r = !1;
		if ("line" == a || "step" == a || "smoothedLine" == a) r = !0;
		if (r && ("regular" == b || "100%" == b)) for (n = 0; n < h.length; n++) m = h[n],
		m.hidden || (l = m.type, m.chart == this.chart && m.valueAxis == this && a == l && m.stackable && (k && (m.stackGraph = k), k = m));
		for (k = this.start; k <= this.end; k++) {
			var u = 0;
			for (n = 0; n < h.length; n++) if (m = h[n], !m.hidden && (l = m.type, m.chart == this.chart && m.valueAxis == this && a == l && m.stackable && (l = this.data[k].axes[this.id].graphs[m.id], g = l.values.value, !isNaN(g)))) {
				var q = AmCharts.getDecimals(g);
				u < q && (u = q);
				isNaN(f[k]) ? f[k] = Math.abs(g) : f[k] += Math.abs(g);
				f[k] = AmCharts.roundTo(f[k], u);
				m = m.fillToGraph;
				r && m && (m = this.data[k].axes[this.id].graphs[m.id]) && (l.values.open = m.values.value);
				"regular" == b && (r && (isNaN(c[k]) ? (c[k] = g, l.values.close = g, l.values.open = this.baseValue) : (isNaN(g) ? l.values.close = c[k] : l.values.close = g + c[k], l.values.open = c[k], c[k] = l.values.close)), "column" != a || isNaN(g) || (l.values.close = g, 0 > g ? (l.values.close = g, isNaN(d[k]) ? l.values.open = p: (l.values.close += d[k], l.values.open = d[k]), d[k] = l.values.close) : (l.values.close = g, isNaN(e[k]) ? l.values.open = p: (l.values.close += e[k], l.values.open = e[k]), e[k] = l.values.close)))
			}
		}
		for (k = this.start; k <= this.end; k++) for (n = 0; n < h.length; n++) m = h[n],
		m.hidden || (l = m.type, m.chart == this.chart && m.valueAxis == this && a == l && m.stackable && (l = this.data[k].axes[this.id].graphs[m.id], g = l.values.value, isNaN(g) || (c = g / f[k] * 100, l.values.percents = c, l.values.total = f[k], "100%" == b && (isNaN(d[k]) && (d[k] = 0), isNaN(e[k]) && (e[k] = 0), 0 > c ? (l.values.close = AmCharts.fitToBounds(c + d[k], -100, 100), l.values.open = d[k], d[k] = l.values.close) : (l.values.close = AmCharts.fitToBounds(c + e[k], -100, 100), l.values.open = e[k], e[k] = l.values.close)))))
	},
	recalculate: function() {
		var a = this.chart.graphs,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b];
			if (c.valueAxis == this) {
				var d = "value";
				if ("candlestick" == c.type || "ohlc" == c.type) d = "open";
				var e, f, g = this.end + 2,
				g = AmCharts.fitToBounds(this.end + 1, 0, this.data.length - 1),
				h = this.start;
				0 < h && h--;
				var k;
				f = this.start;
				c.compareFromStart && (f = 0);
				for (k = f; k <= g && (f = this.data[k].axes[this.id].graphs[c.id], e = f.values[d], isNaN(e)); k++);
				for (d = h; d <= g; d++) {
					f = this.data[d].axes[this.id].graphs[c.id];
					f.percents = {};
					var h = f.values,
					l;
					for (l in h) f.percents[l] = "percents" != l ? h[l] / e * 100 - 100 : h[l]
				}
			}
		}
	},
	getMinMax: function() {
		var a = !1,
		b = this.chart,
		c = b.graphs,
		d;
		for (d = 0; d < c.length; d++) {
			var e = c[d].type; ("line" == e || "step" == e || "smoothedLine" == e) && this.expandMinMax && (a = !0)
		}
		a && (0 < this.start && this.start--, this.end < this.data.length - 1 && this.end++);
		"serial" == b.type && (!0 !== b.categoryAxis.parseDates || a || this.end < this.data.length - 1 && this.end++);
		a = this.minMaxMultiplier;
		this.min = this.getMin(this.start, this.end);
		this.max = this.getMax();
		a = (this.max - this.min) * (a - 1);
		this.min -= a;
		this.max += a;
		a = this.guides.length;
		if (this.includeGuidesInMinMax && 0 < a) for (b = 0; b < a; b++) c = this.guides[b],
		c.toValue < this.min && (this.min = c.toValue),
		c.value < this.min && (this.min = c.value),
		c.toValue > this.max && (this.max = c.toValue),
		c.value > this.max && (this.max = c.value);
		isNaN(this.minimum) || (this.min = this.minimum);
		isNaN(this.maximum) || (this.max = this.maximum);
		this.min > this.max && (a = this.max, this.max = this.min, this.min = a);
		isNaN(this.minTemp) || (this.min = this.minTemp);
		isNaN(this.maxTemp) || (this.max = this.maxTemp);
		this.minReal = this.min;
		this.maxReal = this.max;
		0 === this.min && 0 === this.max && (this.max = 9);
		this.min > this.max && (this.min = this.max - 1);
		a = this.min;
		b = this.max;
		c = this.max - this.min;
		d = 0 === c ? Math.pow(10, Math.floor(Math.log(Math.abs(this.max)) * Math.LOG10E)) / 10 : Math.pow(10, Math.floor(Math.log(Math.abs(c)) * Math.LOG10E)) / 10;
		isNaN(this.maximum) && isNaN(this.maxTemp) && (this.max = Math.ceil(this.max / d) * d + d);
		isNaN(this.minimum) && isNaN(this.minTemp) && (this.min = Math.floor(this.min / d) * d - d);
		0 > this.min && 0 <= a && (this.min = 0);
		0 < this.max && 0 >= b && (this.max = 0);
		"100%" == this.stackType && (this.min = 0 > this.min ? -100 : 0, this.max = 0 > this.max ? 0 : 100);
		c = this.max - this.min;
		d = Math.pow(10, Math.floor(Math.log(Math.abs(c)) * Math.LOG10E)) / 10;
		this.step = Math.ceil(c / this.gridCountR / d) * d;
		c = Math.pow(10, Math.floor(Math.log(Math.abs(this.step)) * Math.LOG10E));
		c = this.fixStepE(c);
		d = Math.ceil(this.step / c);
		5 < d && (d = 10);
		5 >= d && 2 < d && (d = 5);
		this.step = Math.ceil(this.step / (c * d)) * c * d;
		1 > c ? (this.maxDecCount = Math.abs(Math.log(Math.abs(c)) * Math.LOG10E), this.maxDecCount = Math.round(this.maxDecCount), this.step = AmCharts.roundTo(this.step, this.maxDecCount + 1)) : this.maxDecCount = 0;
		this.min = this.step * Math.floor(this.min / this.step);
		this.max = this.step * Math.ceil(this.max / this.step);
		0 > this.min && 0 <= a && (this.min = 0);
		0 < this.max && 0 >= b && (this.max = 0);
		1 < this.minReal && 1 < this.max - this.minReal && (this.minReal = Math.floor(this.minReal));
		c = Math.pow(10, Math.floor(Math.log(Math.abs(this.minReal)) * Math.LOG10E));
		0 === this.min && (this.minReal = c);
		0 === this.min && 1 < this.minReal && (this.minReal = 1);
		0 < this.min && 0 < this.minReal - this.step && (this.minReal = this.min + this.step < this.minReal ? this.min + this.step: this.min);
		c = Math.log(b) * Math.LOG10E - Math.log(a) * Math.LOG10E;
		this.logarithmic && (2 < c ? (this.minReal = this.min = Math.pow(10, Math.floor(Math.log(Math.abs(a)) * Math.LOG10E)), this.max = Math.pow(10, Math.ceil(Math.log(Math.abs(b)) * Math.LOG10E))) : (b = Math.pow(10, Math.floor(Math.log(Math.abs(this.min)) * Math.LOG10E)) / 10, a = Math.pow(10, Math.floor(Math.log(Math.abs(a)) * Math.LOG10E)) / 10, b < a && (this.minReal = this.min = 10 * a)))
	},
	fixStepE: function(a) {
		a = a.toExponential(0).split("e");
		var b = Number(a[1]);
		9 == Number(a[0]) && b++;
		return this.generateNumber(1, b)
	},
	generateNumber: function(a, b) {
		var c = "",
		d;
		d = 0 > b ? Math.abs(b) - 1 : Math.abs(b);
		var e;
		for (e = 0; e < d; e++) c += "0";
		return 0 > b ? Number("0." + c + String(a)) : Number(String(a) + c)
	},
	getMin: function(a, b) {
		var c, d;
		for (d = a; d <= b; d++) {
			var e = this.data[d].axes[this.id].graphs,
			f;
			for (f in e) if (e.hasOwnProperty(f)) {
				var g = this.chart.getGraphById(f);
				if (g.includeInMinMax && (!g.hidden || this.includeHidden)) {
					isNaN(c) && (c = Infinity);
					this.foundGraphs = !0;
					g = e[f].values;
					this.recalculateToPercents && (g = e[f].percents);
					var h;
					if (this.minMaxField) h = g[this.minMaxField],
					h < c && (c = h);
					else for (var k in g) g.hasOwnProperty(k) && "percents" != k && "total" != k && (h = g[k], h < c && (c = h))
				}
			}
		}
		return c
	},
	getMax: function() {
		var a, b;
		for (b = this.start; b <= this.end; b++) {
			var c = this.data[b].axes[this.id].graphs,
			d;
			for (d in c) if (c.hasOwnProperty(d)) {
				var e = this.chart.getGraphById(d);
				if (e.includeInMinMax && (!e.hidden || this.includeHidden)) {
					isNaN(a) && (a = -Infinity);
					this.foundGraphs = !0;
					e = c[d].values;
					this.recalculateToPercents && (e = c[d].percents);
					var f;
					if (this.minMaxField) f = e[this.minMaxField],
					f > a && (a = f);
					else for (var g in e) e.hasOwnProperty(g) && "percents" != g && "total" != g && (f = e[g], f > a && (a = f))
				}
			}
		}
		return a
	},
	dispatchZoomEvent: function(a, b) {
		var c = {
			type: "axisZoomed",
			startValue: a,
			endValue: b,
			target: this,
			chart: this.chart
		};
		this.fire(c.type, c)
	},
	zoomToValues: function(a, b) {
		if (b < a) {
			var c = b;
			b = a;
			a = c
		}
		a < this.min && (a = this.min);
		b > this.max && (b = this.max);
		c = {
			type: "axisSelfZoomed"
		};
		c.chart = this.chart;
		c.valueAxis = this;
		c.multiplier = this.axisWidth / Math.abs(this.getCoordinate(b) - this.getCoordinate(a));
		c.position = "V" == this.orientation ? this.reversed ? this.getCoordinate(a) : this.getCoordinate(b) : this.reversed ? this.getCoordinate(b) : this.getCoordinate(a);
		this.fire(c.type, c)
	},
	coordinateToValue: function(a) {
		if (isNaN(a)) return NaN;
		var b = this.axisWidth,
		c = this.stepWidth,
		d = this.reversed,
		e = this.rotate,
		f = this.min,
		g = this.minReal;
		return ! 0 === this.logarithmic ? Math.pow(10, (e ? !0 === d ? (b - a) / c: a / c: !0 === d ? a / c: (b - a) / c) + Math.log(g) * Math.LOG10E) : !0 === d ? e ? f - (a - b) / c: a / c + f: e ? a / c + f: f - (a - b) / c
	},
	getCoordinate: function(a) {
		if (isNaN(a)) return NaN;
		var b = this.rotate,
		c = this.reversed,
		d = this.axisWidth,
		e = this.stepWidth,
		f = this.min,
		g = this.minReal; ! 0 === this.logarithmic ? (a = Math.log(a) * Math.LOG10E - Math.log(g) * Math.LOG10E, b = b ? !0 === c ? d - e * a: e * a: !0 === c ? e * a: d - e * a) : b = !0 === c ? b ? d - e * (a - f) : e * (a - f) : b ? e * (a - f) : d - e * (a - f);
		b = this.rotate ? b + (this.x - this.viX) : b + (this.y - this.viY);
		return Math.round(b)
	},
	synchronizeWithAxis: function(a) {
		this.synchronizeWith = a;
		this.listenTo(this.synchronizeWith, "axisChanged", this.handleSynchronization)
	},
	handleSynchronization: function(a) {
		var b = this.synchronizeWith;
		a = b.min;
		var c = b.max,
		b = b.step,
		d = this.synchronizationMultiplier;
		d && (this.min = a * d, this.max = c * d, this.step = b * d, a = Math.pow(10, Math.floor(Math.log(Math.abs(this.step)) * Math.LOG10E)), a = Math.abs(Math.log(Math.abs(a)) * Math.LOG10E), this.maxDecCount = a = Math.round(a), this.draw())
	}
});
AmCharts.RecAxis = AmCharts.Class({
	construct: function(a) {
		var b = a.chart,
		c = a.axisThickness,
		d = a.axisColor,
		e = a.axisAlpha,
		f = a.offset,
		g = a.dx,
		h = a.dy,
		k = a.viX,
		l = a.viY,
		m = a.viH,
		n = a.viW,
		p = b.container;
		"H" == a.orientation ? (d = AmCharts.line(p, [0, n], [0, 0], d, e, c), this.axisWidth = a.width, "bottom" == a.position ? (a = c / 2 + f + m + l - 1, c = k) : (a = -c / 2 - f + l + h, c = g + k)) : (this.axisWidth = a.height, "right" == a.position ? (d = AmCharts.line(p, [0, 0, -g], [0, m, m - h], d, e, c), a = l + h, c = c / 2 + f + g + n + k - 1) : (d = AmCharts.line(p, [0, 0], [0, m], d, e, c), a = l, c = -c / 2 - f + k));
		d.translate(c, a);
		b.axesSet.push(d);
		this.set = d
	}
});
AmCharts.RecItem = AmCharts.Class({
	construct: function(a, b, c, d, e, f, g, h, k, l) {
		b = Math.round(b);
		void 0 == c && (c = "");
		k || (k = 0);
		void 0 == d && (d = !0);
		var m = a.chart.fontFamily,
		n = a.fontSize;
		void 0 == n && (n = a.chart.fontSize);
		var p = a.color;
		void 0 == p && (p = a.chart.color);
		var r = a.chart.container,
		u = r.set();
		this.set = u;
		var q = a.axisThickness,
		t = a.axisColor,
		v = a.axisAlpha,
		s = a.tickLength,
		w = a.gridAlpha,
		A = a.gridThickness,
		C = a.gridColor,
		y = a.dashLength,
		z = a.fillColor,
		J = a.fillAlpha,
		I = a.labelsEnabled,
		B = a.labelRotation,
		Z = a.counter,
		S = a.inside,
		da = a.dx,
		X = a.dy,
		Fa = a.orientation,
		ea = a.position,
		$ = a.previousCoord,
		L = a.viH,
		V = a.viW,
		oa = a.offset,
		Y, W;
		g ? (I = !0, isNaN(g.tickLength) || (s = g.tickLength), void 0 != g.lineColor && (C = g.lineColor), void 0 != g.color && (p = g.color), isNaN(g.lineAlpha) || (w = g.lineAlpha), isNaN(g.dashLength) || (y = g.dashLength), isNaN(g.lineThickness) || (A = g.lineThickness), !0 === g.inside && (S = !0), isNaN(g.labelRotation) || (B = g.labelRotation), isNaN(g.fontSize) || (n = g.fontSize), g.position && (ea = g.position)) : "" === c && (s = 0);
		W = "start";
		e && (W = "middle");
		var aa = B * Math.PI / 180,
		ha, G = 0,
		D = 0,
		ia = 0,
		P = ha = 0;
		"V" == Fa && (B = 0);
		var x;
		I && (x = a.autoWrap && 0 === B ? AmCharts.wrappedText(r, c, p, m, n, W, h, e, 0) : AmCharts.text(r, c, p, m, n, W, h), P = x.getBBox().width);
		if ("H" == Fa) {
			if (0 <= b && b <= V + 1 && (0 < s && 0 < v && b + k <= V + 1 && (Y = AmCharts.line(r, [b + k, b + k], [0, s], t, v, A), u.push(Y)), 0 < w && (W = AmCharts.line(r, [b, b + da, b + da], [L, L + X, X], C, w, A, y), u.push(W))), D = 0, G = b, g && 90 == B && (G -= n), !1 === d ? (W = "start", D = "bottom" == ea ? S ? D + s: D - s: S ? D - s: D + s, G += 3, e && (G += e / 2, W = "middle"), 0 < B && (W = "middle")) : W = "middle", 1 == Z && 0 < J && !g && !l && $ < V && (d = AmCharts.fitToBounds(b, 0, V), $ = AmCharts.fitToBounds($, 0, V), ha = d - $, 0 < ha && (fill = AmCharts.rect(r, ha, a.height, z, J), fill.translate(d - ha + da, X), u.push(fill))), "bottom" == ea ? (D += L + n / 2 + oa, S ? 0 < B ? (D = L - P / 2 * Math.sin(aa) - s - 3, G += P / 2 * Math.cos(aa)) : 0 > B ? (D -= s + n + 3 + 3 - P / 2 * Math.sin(aa), G -= P / 2 * Math.cos(aa)) : D -= s + n + 3 + 3 : 0 < B ? (D = L + P / 2 * Math.sin(aa) + s + 3, G -= P / 2 * Math.cos(aa)) : 0 > B ? (D = L + s + 3 - P / 2 * Math.sin(aa) + 2, G += P / 2 * Math.cos(aa)) : D += s + q + 3 + 3) : (D += X + n / 2 - oa, G += da, S ? 0 < B ? (D = P / 2 * Math.sin(aa) + s + 3, G -= P / 2 * Math.cos(aa)) : D += s + 3 : 0 < B ? (D = -(P / 2) * Math.sin(aa) - s - 6, G += P / 2 * Math.cos(aa)) : D -= s + n + 3 + q + 3), "bottom" == ea ? ha = (S ? L - s - 1 : L + q - 1) + oa: (ia = da, ha = (S ? X: X - s - q + 1) - oa), f && (G += f), X = G, 0 < B && (X += P / 2 * Math.cos(aa)), x && (ea = 0, S && (ea = P / 2 * Math.cos(aa)), X + ea > V + 2 || 0 > X)) x.remove(),
			x = null
		} else {
			0 <= b && b <= L + 1 && (0 < s && 0 < v && b + k <= L + 1 && (Y = AmCharts.line(r, [0, s], [b + k, b + k], t, v, A), u.push(Y)), 0 < w && (W = AmCharts.line(r, [0, da, V + da], [b, b + X, b + X], C, w, A, y), u.push(W)));
			W = "end";
			if (!0 === S && "left" == ea || !1 === S && "right" == ea) W = "start";
			D = b - n / 2;
			1 == Z && 0 < J && !g && !l && (d = AmCharts.fitToBounds(b, 0, L), $ = AmCharts.fitToBounds($, 0, L), aa = d - $, fill = AmCharts.polygon(r, [0, a.width, a.width, 0], [0, 0, aa, aa], z, J), fill.translate(da, d - aa + X), u.push(fill));
			D += n / 2;
			"right" == ea ? (G += da + V + oa, D += X, S ? (G -= s + 4, f || (D -= n / 2 + 3)) : (G += s + 4 + q, D -= 2)) : S ? (G += s + 4 - oa, f || (D -= n / 2 + 3), g && (G += da, D += X)) : (G += -s - q - 4 - 2 - oa, D -= 2);
			Y && ("right" == ea ? (ia += da + oa + V, ha += X, ia = S ? ia - q: ia + q) : (ia -= oa, S || (ia -= s + q)));
			f && (D += f);
			S = -3;
			"right" == ea && (S += X);
			x && (D > L + 1 || D < S) && (x.remove(), x = null)
		}
		Y && Y.translate(ia, ha); ! 1 === a.visible && (Y && Y.remove(), x && (x.remove(), x = null));
		x && (x.attr({
			"text-anchor": W
		}), x.translate(G, D), 0 !== B && x.rotate( - B, a.chart.backgroundColor), a.allLabels.push(x), " " != c && (this.label = x));
		l || (a.counter = 0 === Z ? 1 : 0, a.previousCoord = b);
		0 === this.set.node.childNodes.length && this.set.remove()
	},
	graphics: function() {
		return this.set
	},
	getLabel: function() {
		return this.label
	}
});
AmCharts.RecFill = AmCharts.Class({
	construct: function(a, b, c, d) {
		var e = a.dx,
		f = a.dy,
		g = a.orientation,
		h = 0;
		if (c < b) {
			var k = b;
			b = c;
			c = k
		}
		var l = d.fillAlpha;
		isNaN(l) && (l = 0);
		k = a.chart.container;
		d = d.fillColor;
		"V" == g ? (b = AmCharts.fitToBounds(b, 0, a.viH), c = AmCharts.fitToBounds(c, 0, a.viH)) : (b = AmCharts.fitToBounds(b, 0, a.viW), c = AmCharts.fitToBounds(c, 0, a.viW));
		c -= b;
		isNaN(c) && (c = 4, h = 2, l = 0);
		0 > c && "object" == typeof d && (d = d.join(",").split(",").reverse());
		"V" == g ? (a = AmCharts.rect(k, a.width, c, d, l), a.translate(e, b - h + f)) : (a = AmCharts.rect(k, c, a.height, d, l), a.translate(b - h + e, f));
		this.set = k.set([a])
	},
	graphics: function() {
		return this.set
	},
	getLabel: function() {}
});
AmCharts.AmChart = AmCharts.Class({
	construct: function(a) {
		this.theme = a;
		this.version = "3.4.2";
		AmCharts.addChart(this);
		this.createEvents("dataUpdated", "init", "rendered", "drawn");
		this.height = this.width = "100%";
		this.dataChanged = !0;
		this.chartCreated = !1;
		this.previousWidth = this.previousHeight = 0;
		this.backgroundColor = "#FFFFFF";
		this.borderAlpha = this.backgroundAlpha = 0;
		this.color = this.borderColor = "#000000";
		this.fontFamily = "Verdana";
		this.fontSize = 11;
		this.usePrefixes = !1;
		this.numberFormatter = {
			precision: -1,
			decimalSeparator: ".",
			thousandsSeparator: ","
		};
		this.percentFormatter = {
			precision: 2,
			decimalSeparator: ".",
			thousandsSeparator: ","
		};
		this.labels = [];
		this.allLabels = [];
		this.titles = [];
		this.marginRight = this.marginLeft = this.autoMarginOffset = 0;
		this.timeOuts = [];
		this.creditsPosition = "top-left";
		var b = document.createElement("div"),
		c = b.style;
		c.overflow = "hidden";
		c.position = "relative";
		c.textAlign = "left";
		this.chartDiv = b;
		b = document.createElement("div");
		c = b.style;
		c.overflow = "hidden";
		c.position = "relative";
		c.textAlign = "left";
		this.legendDiv = b;
		this.titleHeight = 0;
		this.hideBalloonTime = 150;
		this.handDrawScatter = 2;
		this.handDrawThickness = 1;
		this.prefixesOfBigNumbers = [{
			number: 1E3,
			prefix: "k"
		},
		{
			number: 1E6,
			prefix: "M"
		},
		{
			number: 1E9,
			prefix: "G"
		},
		{
			number: 1E12,
			prefix: "T"
		},
		{
			number: 1E15,
			prefix: "P"
		},
		{
			number: 1E18,
			prefix: "E"
		},
		{
			number: 1E21,
			prefix: "Z"
		},
		{
			number: 1E24,
			prefix: "Y"
		}];
		this.prefixesOfSmallNumbers = [{
			number: 1E-24,
			prefix: "y"
		},
		{
			number: 1E-21,
			prefix: "z"
		},
		{
			number: 1E-18,
			prefix: "a"
		},
		{
			number: 1E-15,
			prefix: "f"
		},
		{
			number: 1E-12,
			prefix: "p"
		},
		{
			number: 1E-9,
			prefix: "n"
		},
		{
			number: 1E-6,
			prefix: "\u03bc"
		},
		{
			number: 0.001,
			prefix: "m"
		}];
		this.panEventsEnabled = !1;
		AmCharts.bezierX = 3;
		AmCharts.bezierY = 6;
		this.product = "amcharts";
		this.animations = [];
		this.balloon = new AmCharts.AmBalloon(this.theme);
		this.balloon.chart = this;
		AmCharts.applyTheme(this, a, "AmChart")
	},
	drawChart: function() {
		this.drawBackground();
		this.redrawLabels();
		this.drawTitles();
		this.brr()
	},
	drawBackground: function() {
		AmCharts.remove(this.background);
		var a = this.container,
		b = this.backgroundColor,
		c = this.backgroundAlpha,
		d = this.set;
		AmCharts.isModern || 0 !== c || (c = 0.001);
		var e = this.updateWidth();
		this.realWidth = e;
		var f = this.updateHeight();
		this.realHeight = f;
		this.background = b = AmCharts.polygon(a, [0, e - 1, e - 1, 0], [0, 0, f - 1, f - 1], b, c, 1, this.borderColor, this.borderAlpha);
		d.push(b);
		if (b = this.backgroundImage) this.path && (b = this.path + b),
		this.bgImg = a = a.image(b, 0, 0, e, f),
		d.push(a)
	},
	drawTitles: function() {
		var a = this.titles;
		if (AmCharts.ifArray(a)) {
			var b = 20,
			c;
			for (c = 0; c < a.length; c++) {
				var d = a[c],
				e = d.color;
				void 0 === e && (e = this.color);
				var f = d.size;
				isNaN(d.alpha);
				var g = this.marginLeft,
				e = AmCharts.text(this.container, d.text, e, this.fontFamily, f);
				e.translate(g + (this.realWidth - this.marginRight - g) / 2, b);
				g = !0;
				void 0 !== d.bold && (g = d.bold);
				g && e.attr({
					"font-weight": "bold"
				});
				b += f + 6;
				this.freeLabelsSet.push(e)
			}
		}
	},
	write: function(a) {
		a = "object" != typeof a ? document.getElementById(a) : a;
		a.innerHTML = "";
		this.div = a;
		a.style.overflow = "hidden";
		a.style.textAlign = "left";
		var b = this.chartDiv,
		c = this.legendDiv,
		d = this.legend,
		e = c.style,
		f = b.style;
		this.measure();
		var g, h = document.createElement("div");
		g = h.style;
		g.position = "relative";
		this.containerDiv = h;
		a.appendChild(h);
		var k = this.exportConfig;
		k && AmCharts.AmExport && !this.AmExport && (this.AmExport = new AmCharts.AmExport(this, k));
		if (d) switch (d = this.addLegend(d, d.divId), d.position) {
		case "bottom":
			h.appendChild(b);
			h.appendChild(c);
			break;
		case "top":
			h.appendChild(c);
			h.appendChild(b);
			break;
		case "absolute":
			g.width = a.style.width;
			g.height = a.style.height;
			e.position = "absolute";
			f.position = "absolute";
			void 0 !== d.left && (e.left = d.left + "px");
			void 0 !== d.right && (e.right = d.right + "px");
			void 0 !== d.top && (e.top = d.top + "px");
			void 0 !== d.bottom && (e.bottom = d.bottom + "px");
			d.marginLeft = 0;
			d.marginRight = 0;
			h.appendChild(b);
			h.appendChild(c);
			break;
		case "right":
			g.width = a.style.width;
			g.height = a.style.height;
			e.position = "relative";
			f.position = "absolute";
			h.appendChild(b);
			h.appendChild(c);
			break;
		case "left":
			g.width = a.style.width;
			g.height = a.style.height;
			e.position = "absolute";
			f.position = "relative";
			h.appendChild(b);
			h.appendChild(c);
			break;
		case "outside":
			h.appendChild(b)
		} else h.appendChild(b);
		this.listenersAdded || (this.addListeners(), this.listenersAdded = !0);
		this.initChart()
	},
	createLabelsSet: function() {
		AmCharts.remove(this.labelsSet);
		this.labelsSet = this.container.set();
		this.freeLabelsSet.push(this.labelsSet)
	},
	initChart: function() {
		this.divIsFixed = AmCharts.findIfFixed(this.chartDiv);
		this.previousHeight = this.divRealHeight;
		this.previousWidth = this.divRealWidth;
		this.destroy();
		this.startInterval();
		var a = 0;
		document.attachEvent && !window.opera && (a = 1);
		this.dmouseX = this.dmouseY = 0;
		var b = document.getElementsByTagName("html")[0];
		b && window.getComputedStyle && (b = window.getComputedStyle(b, null)) && (this.dmouseY = AmCharts.removePx(b.getPropertyValue("margin-top")), this.dmouseX = AmCharts.removePx(b.getPropertyValue("margin-left")));
		this.mouseMode = a;
		a = new AmCharts.AmDraw(this.chartDiv, this.realWidth, this.realHeight, this);
		a.handDrawn = this.handDrawn;
		a.handDrawScatter = this.handDrawScatter;
		a.handDrawThickness = this.handDrawThickness;
		this.container = a;
		if (AmCharts.VML || AmCharts.SVG) a = this.container,
		this.set = a.set(),
		this.gridSet = a.set(),
		this.graphsBehindSet = a.set(),
		this.bulletBehindSet = a.set(),
		this.columnSet = a.set(),
		this.graphsSet = a.set(),
		this.trendLinesSet = a.set(),
		this.axesLabelsSet = a.set(),
		this.axesSet = a.set(),
		this.cursorSet = a.set(),
		this.scrollbarsSet = a.set(),
		this.bulletSet = a.set(),
		this.freeLabelsSet = a.set(),
		this.balloonsSet = a.set(),
		this.balloonsSet.setAttr("id", "balloons"),
		this.zoomButtonSet = a.set(),
		this.linkSet = a.set(),
		this.renderFix()
	},
	measure: function() {
		var a = this.div,
		b = this.chartDiv,
		c = a.offsetWidth,
		d = a.offsetHeight,
		e = this.container;
		a.clientHeight && (c = a.clientWidth, d = a.clientHeight);
		var f = AmCharts.removePx(AmCharts.getStyle(a, "padding-left")),
		g = AmCharts.removePx(AmCharts.getStyle(a, "padding-right")),
		h = AmCharts.removePx(AmCharts.getStyle(a, "padding-top")),
		k = AmCharts.removePx(AmCharts.getStyle(a, "padding-bottom"));
		isNaN(f) || (c -= f);
		isNaN(g) || (c -= g);
		isNaN(h) || (d -= h);
		isNaN(k) || (d -= k);
		f = a.style;
		a = f.width;
		f = f.height; - 1 != a.indexOf("px") && (c = AmCharts.removePx(a)); - 1 != f.indexOf("px") && (d = AmCharts.removePx(f));
		a = AmCharts.toCoordinate(this.width, c);
		f = AmCharts.toCoordinate(this.height, d);
		this.balloon = AmCharts.processObject(this.balloon, AmCharts.AmBalloon, this.theme);
		this.balloon.chart = this;
		if (a != this.previousWidth || f != this.previousHeight) b.style.width = a + "px",
		b.style.height = f + "px",
		e && e.setSize(a, f),
		this.balloon.setBounds(2, 2, a - 2, f);
		this.realWidth = a;
		this.realHeight = f;
		this.divRealWidth = c;
		this.divRealHeight = d
	},
	destroy: function() {
		this.chartDiv.innerHTML = "";
		this.clearTimeOuts();
		this.interval && clearInterval(this.interval);
		this.interval = NaN
	},
	clearTimeOuts: function() {
		var a = this.timeOuts;
		if (a) {
			var b;
			for (b = 0; b < a.length; b++) clearTimeout(a[b])
		}
		this.timeOuts = []
	},
	clear: function(a) {
		AmCharts.callMethod("clear", [this.chartScrollbar, this.scrollbarV, this.scrollbarH, this.chartCursor]);
		this.chartCursor = this.scrollbarH = this.scrollbarV = this.chartScrollbar = null;
		this.clearTimeOuts();
		this.container && (this.container.remove(this.chartDiv), this.container.remove(this.legendDiv));
		a || AmCharts.removeChart(this)
	},
	setMouseCursor: function(a) {
		"auto" == a && AmCharts.isNN && (a = "default");
		this.chartDiv.style.cursor = a;
		this.legendDiv.style.cursor = a
	},
	redrawLabels: function() {
		this.labels = [];
		var a = this.allLabels;
		this.createLabelsSet();
		var b;
		for (b = 0; b < a.length; b++) this.drawLabel(a[b])
	},
	drawLabel: function(a) {
		if (this.container) {
			var b = a.y,
			c = a.text,
			d = a.align,
			e = a.size,
			f = a.color,
			g = a.rotation,
			h = a.alpha,
			k = a.bold,
			l = AmCharts.toCoordinate(a.x, this.realWidth),
			b = AmCharts.toCoordinate(b, this.realHeight);
			l || (l = 0);
			b || (b = 0);
			void 0 === f && (f = this.color);
			isNaN(e) && (e = this.fontSize);
			d || (d = "start");
			"left" == d && (d = "start");
			"right" == d && (d = "end");
			"center" == d && (d = "middle", g ? b = this.realHeight - b + b / 2 : l = this.realWidth / 2 - l);
			void 0 === h && (h = 1);
			void 0 === g && (g = 0);
			b += e / 2;
			c = AmCharts.text(this.container, c, f, this.fontFamily, e, d, k, h);
			c.translate(l, b);
			0 !== g && c.rotate(g);
			a.url && (c.setAttr("cursor", "pointer"), c.click(function() {
				AmCharts.getURL(a.url)
			}));
			this.labelsSet.push(c);
			this.labels.push(c)
		}
	},
	addLabel: function(a, b, c, d, e, f, g, h, k, l) {
		a = {
			x: a,
			y: b,
			text: c,
			align: d,
			size: e,
			color: f,
			alpha: h,
			rotation: g,
			bold: k,
			url: l
		};
		this.container && this.drawLabel(a);
		this.allLabels.push(a)
	},
	clearLabels: function() {
		var a = this.labels,
		b;
		for (b = a.length - 1; 0 <= b; b--) a[b].remove();
		this.labels = [];
		this.allLabels = []
	},
	updateHeight: function() {
		var a = this.divRealHeight,
		b = this.legend;
		if (b) {
			var c = this.legendDiv.offsetHeight,
			b = b.position;
			if ("top" == b || "bottom" == b) {
				a -= c;
				if (0 > a || isNaN(a)) a = 0;
				this.chartDiv.style.height = a + "px"
			}
		}
		return a
	},
	updateWidth: function() {
		var a = this.divRealWidth,
		b = this.divRealHeight,
		c = this.legend;
		if (c) {
			var d = this.legendDiv,
			e = d.offsetWidth,
			f = d.offsetHeight,
			d = d.style,
			g = this.chartDiv.style,
			c = c.position;
			if ("right" == c || "left" == c) {
				a -= e;
				if (0 > a || isNaN(a)) a = 0;
				g.width = a + "px";
				"left" == c ? g.left = e + "px": d.left = a + "px";
				d.top = (b - f) / 2 + "px"
			}
		}
		return a
	},
	getTitleHeight: function() {
		var a = 0,
		b = this.titles;
		if (0 < b.length) {
			var a = 15,
			c;
			for (c = 0; c < b.length; c++) a += b[c].size + 6
		}
		return a
	},
	addTitle: function(a, b, c, d, e) {
		isNaN(b) && (b = this.fontSize + 2);
		a = {
			text: a,
			size: b,
			color: c,
			alpha: d,
			bold: e
		};
		this.titles.push(a);
		return a
	},
	addMouseWheel: function() {
		var a = this;
		window.addEventListener && (window.addEventListener("DOMMouseScroll",
		function(b) {
			a.handleWheel.call(a, b)
		},
		!1), document.addEventListener("mousewheel",
		function(b) {
			a.handleWheel.call(a, b)
		},
		!1))
	},
	handleWheel: function(a) {
		if (this.mouseIsOver) {
			var b = 0;
			a || (a = window.event);
			a.wheelDelta ? b = a.wheelDelta / 120 : a.detail && (b = -a.detail / 3);
			b && this.handleWheelReal(b, a.shiftKey);
			a.preventDefault && a.preventDefault()
		}
	},
	handleWheelReal: function(a) {},
	addListeners: function() {
		var a = this,
		b = a.chartDiv;
		document.addEventListener ? (a.panEventsEnabled && "ontouchstart" in document.documentElement && (b.addEventListener("touchstart",
		function(b) {
			a.handleTouchMove.call(a, b);
			a.handleTouchStart.call(a, b)
		},
		!0), b.addEventListener("touchmove",
		function(b) {
			a.handleTouchMove.call(a, b)
		},
		!0), b.addEventListener("touchend",
		function(b) {
			a.handleTouchEnd.call(a, b)
		},
		!0)), b.addEventListener("mousedown",
		function(b) {
			a.handleMouseDown.call(a, b)
		},
		!0), b.addEventListener("mouseover",
		function(b) {
			a.handleMouseOver.call(a, b)
		},
		!0), b.addEventListener("mouseout",
		function(b) {
			a.handleMouseOut.call(a, b)
		},
		!0)) : (b.attachEvent("onmousedown",
		function(b) {
			a.handleMouseDown.call(a, b)
		}), b.attachEvent("onmouseover",
		function(b) {
			a.handleMouseOver.call(a, b)
		}), b.attachEvent("onmouseout",
		function(b) {
			a.handleMouseOut.call(a, b)
		}))
	},
	dispDUpd: function() {
		var a;
		this.dispatchDataUpdated && (this.dispatchDataUpdated = !1, a = "dataUpdated", this.fire(a, {
			type: a,
			chart: this
		}));
		this.chartCreated || (a = "init", this.fire(a, {
			type: a,
			chart: this
		}));
		this.chartRendered || (a = "rendered", this.fire(a, {
			type: a,
			chart: this
		}), this.chartRendered = !0);
		a = "drawn";
		this.fire(a, {
			type: a,
			chart: this
		})
	},
	validateSize: function() {
		var a = this;
		a.measure();
		var b = a.legend;
		if ((a.realWidth != a.previousWidth || a.realHeight != a.previousHeight) && 0 < a.realWidth && 0 < a.realHeight) {
			a.sizeChanged = !0;
			if (b) {
				clearTimeout(a.legendInitTO);
				var c = setTimeout(function() {
					b.invalidateSize()
				},
				100);
				a.timeOuts.push(c);
				a.legendInitTO = c
			}
			a.marginsUpdated = "xy" != a.type ? !1 : !0;
			clearTimeout(a.initTO);
			c = setTimeout(function() {
				a.initChart()
			},
			150);
			a.timeOuts.push(c);
			a.initTO = c
		}
		a.renderFix();
		b && b.renderFix()
	},
	invalidateSize: function() {
		this.previousHeight = this.previousWidth = NaN;
		this.invalidateSizeReal()
	},
	invalidateSizeReal: function() {
		var a = this;
		a.marginsUpdated = !1;
		clearTimeout(a.validateTO);
		var b = setTimeout(function() {
			a.validateSize()
		},
		5);
		a.timeOuts.push(b);
		a.validateTO = b
	},
	validateData: function(a) {
		this.chartCreated && (this.dataChanged = !0, this.marginsUpdated = "xy" != this.type ? !1 : !0, this.initChart(a))
	},
	validateNow: function() {
		this.chartRendered = this.listenersAdded = !1;
		this.write(this.div)
	},
	showItem: function(a) {
		a.hidden = !1;
		this.initChart()
	},
	hideItem: function(a) {
		a.hidden = !0;
		this.initChart()
	},
	hideBalloon: function() {
		var a = this;
		clearInterval(a.hoverInt);
		clearTimeout(a.balloonTO);
		a.hoverInt = setTimeout(function() {
			a.hideBalloonReal.call(a)
		},
		a.hideBalloonTime)
	},
	cleanChart: function() {},
	hideBalloonReal: function() {
		var a = this.balloon;
		a && a.hide()
	},
	showBalloon: function(a, b, c, d, e) {
		var f = this;
		clearTimeout(f.balloonTO);
		clearInterval(f.hoverInt);
		f.balloonTO = setTimeout(function() {
			f.showBalloonReal.call(f, a, b, c, d, e)
		},
		1)
	},
	showBalloonReal: function(a, b, c, d, e) {
		this.handleMouseMove();
		var f = this.balloon;
		f.enabled && (f.followCursor(!1), f.changeColor(b), !c || f.fixedPosition ? (f.setPosition(d, e), f.followCursor(!1)) : f.followCursor(!0), a && f.showBalloon(a))
	},
	handleTouchMove: function(a) {
		this.hideBalloon();
		var b = this.chartDiv;
		a.touches && (a = a.touches.item(0), this.mouseX = a.pageX - AmCharts.findPosX(b), this.mouseY = a.pageY - AmCharts.findPosY(b))
	},
	handleMouseOver: function(a) {
		AmCharts.resetMouseOver();
		this.mouseIsOver = !0
	},
	handleMouseOut: function(a) {
		AmCharts.resetMouseOver();
		this.mouseIsOver = !1
	},
	handleMouseMove: function(a) {
		if (this.mouseIsOver) {
			var b = this.chartDiv;
			a || (a = window.event);
			var c, d;
			if (a) {
				this.posX = AmCharts.findPosX(b);
				this.posY = AmCharts.findPosY(b);
				switch (this.mouseMode) {
				case 1:
					c = a.clientX - this.posX;
					d = a.clientY - this.posY;
					if (!this.divIsFixed) {
						var b = document.body,
						e, f;
						b && (e = b.scrollLeft, y1 = b.scrollTop);
						if (b = document.documentElement) f = b.scrollLeft,
						y2 = b.scrollTop;
						e = Math.max(e, f);
						f = Math.max(y1, y2);
						c += e;
						d += f
					}
					break;
				case 0:
					this.divIsFixed ? (c = a.clientX - this.posX, d = a.clientY - this.posY) : (c = a.pageX - this.posX, d = a.pageY - this.posY)
				}
				a.touches && (a = a.touches.item(0), c = a.pageX - this.posX, d = a.pageY - this.posY);
				this.mouseX = c - this.dmouseX;
				this.mouseY = d - this.dmouseY
			}
		}
	},
	handleTouchStart: function(a) {
		this.handleMouseDown(a)
	},
	handleTouchEnd: function(a) {
		AmCharts.resetMouseOver();
		this.handleReleaseOutside(a)
	},
	handleReleaseOutside: function(a) {},
	handleMouseDown: function(a) {
		AmCharts.resetMouseOver();
		this.mouseIsOver = !0;
		a && a.preventDefault && a.preventDefault()
	},
	addLegend: function(a, b) {
		a = AmCharts.processObject(a, AmCharts.AmLegend, this.theme);
		a.divId = b;
		var c;
		c = "object" != typeof b && b ? document.getElementById(b) : b;
		this.legend = a;
		a.chart = this;
		c ? (a.div = c, a.position = "outside", a.autoMargins = !1) : a.div = this.legendDiv;
		c = this.handleLegendEvent;
		this.listenTo(a, "showItem", c);
		this.listenTo(a, "hideItem", c);
		this.listenTo(a, "clickMarker", c);
		this.listenTo(a, "rollOverItem", c);
		this.listenTo(a, "rollOutItem", c);
		this.listenTo(a, "rollOverMarker", c);
		this.listenTo(a, "rollOutMarker", c);
		this.listenTo(a, "clickLabel", c);
		return a
	},
	removeLegend: function() {
		this.legend = void 0;
		this.legendDiv.innerHTML = ""
	},
	handleResize: function() { (AmCharts.isPercents(this.width) || AmCharts.isPercents(this.height)) && this.invalidateSizeReal();
		this.renderFix()
	},
	renderFix: function() {
		if (!AmCharts.VML) {
			var a = this.container;
			a && a.renderFix()
		}
	},
	getSVG: function() {
		if (AmCharts.hasSVG) return this.container
	},
	animate: function(a, b, c, d, e, f, g) {
		a["an_" + b] && AmCharts.removeFromArray(this.animations, a["an_" + b]);
		c = {
			obj: a,
			frame: 0,
			attribute: b,
			from: c,
			to: d,
			time: e,
			effect: f,
			suffix: g
		};
		a["an_" + b] = c;
		this.animations.push(c);
		return c
	},
	setLegendData: function(a) {
		var b = this.legend;
		b && b.setData(a)
	},
	startInterval: function() {
		var a = this;
		clearInterval(a.interval);
		a.interval = setInterval(function() {
			a.updateAnimations.call(a)
		},
		AmCharts.updateRate)
	},
	stopAnim: function(a) {
		AmCharts.removeFromArray(this.animations, a)
	},
	updateAnimations: function() {
		var a;
		this.container && this.container.update();
		for (a = this.animations.length - 1; 0 <= a; a--) {
			var b = this.animations[a],
			c = 1E3 * b.time / AmCharts.updateRate,
			d = b.frame + 1,
			e = b.obj,
			f = b.attribute;
			if (d <= c) {
				b.frame++;
				var g = Number(b.from),
				h = Number(b.to) - g,
				c = AmCharts[b.effect](0, d, g, h, c);
				0 === h ? this.animations.splice(a, 1) : e.node.style[f] = c + b.suffix
			} else e.node.style[f] = Number(b.to) + b.suffix,
			this.animations.splice(a, 1)
		}
	},
	brr: function() {
		var a = window.location.hostname.split("."),
		b;
		2 <= a.length && (b = a[a.length - 2] + "." + a[a.length - 1]);
		this.amLink && (a = this.amLink.parentNode) && a.removeChild(this.amLink);
		a = this.creditsPosition;
		if ("amcharts.com" == b) {
			var c = b = 0,
			d = this.realWidth,
			e = this.realHeight;
			if ("serial" == this.type || "xy" == this.type) b = this.marginLeftReal,
			c = this.marginTopReal,
			d = b + this.plotAreaWidth,
			e = c + this.plotAreaHeight;
			var f = "http://www.amcharts.com/javascript-charts/",
			g = "JavaScript charts",
			h = "JS chart by amCharts";
			"ammap" == this.product && (f = "http://www.ammap.com/javascript-maps/", g = "JavaScript maps", h = "JS map by amCharts");
			var k = document.createElement("a"),
			h = document.createTextNode(h);
			k.setAttribute("href", f);
			k.setAttribute("title", g);
			k.appendChild(h);
			this.chartDiv.appendChild(k);
			this.amLink = k;
			f = k.style;
			f.position = "absolute";
			f.textDecoration = "none";
			f.color = this.color;
			f.fontFamily = this.fontFamily;
			f.fontSize = this.fontSize + "px";
			f.opacity = 0.5;
			f.display = "block";
			var g = k.offsetWidth,
			k = k.offsetHeight,
			h = 5 + b,
			l = c + 5;
			"bottom-left" == a && (h = 5 + b, l = e - k - 3);
			"bottom-right" == a && (h = d - g - 5, l = e - k - 3);
			"top-right" == a && (h = d - g - 5, l = c + 5);
			f.left = h + "px";
			f.top = l + "px"
		}
	}
});
AmCharts.Slice = AmCharts.Class({
	construct: function() {}
});
AmCharts.SerialDataItem = AmCharts.Class({
	construct: function() {}
});
AmCharts.GraphDataItem = AmCharts.Class({
	construct: function() {}
});
AmCharts.Guide = AmCharts.Class({
	construct: function(a) {
		this.cname = "Guide";
		AmCharts.applyTheme(this, a, this.cname)
	}
});
AmCharts.AmGraph = AmCharts.Class({
	construct: function(a) {
		this.cname = "AmGraph";
		this.createEvents("rollOverGraphItem", "rollOutGraphItem", "clickGraphItem", "doubleClickGraphItem", "rightClickGraphItem", "clickGraph");
		this.type = "line";
		this.stackable = !0;
		this.columnCount = 1;
		this.columnIndex = 0;
		this.centerCustomBullets = this.showBalloon = !0;
		this.maxBulletSize = 50;
		this.minBulletSize = 0;
		this.balloonText = "[[value]]";
		this.hidden = this.scrollbar = this.animationPlayed = !1;
		this.pointPosition = "middle";
		this.depthCount = 1;
		this.includeInMinMax = !0;
		this.negativeBase = 0;
		this.visibleInLegend = !0;
		this.showAllValueLabels = !1;
		this.showBalloonAt = "close";
		this.lineThickness = 1;
		this.dashLength = 0;
		this.connect = !0;
		this.lineAlpha = 1;
		this.bullet = "none";
		this.bulletBorderThickness = 2;
		this.bulletBorderAlpha = 0;
		this.bulletAlpha = 1;
		this.bulletSize = 8;
		this.hideBulletsCount = this.bulletOffset = 0;
		this.labelPosition = "top";
		this.cornerRadiusTop = 0;
		this.cursorBulletAlpha = 1;
		this.gradientOrientation = "vertical";
		this.dy = this.dx = 0;
		this.periodValue = "";
		this.clustered = !0;
		this.periodSpan = 1;
		this.y = this.x = 0;
		AmCharts.applyTheme(this, a, this.cname)
	},
	draw: function() {
		var a = this.chart,
		b = a.container;
		this.container = b;
		this.destroy();
		var c = b.set(),
		d = b.set();
		this.behindColumns ? (a.graphsBehindSet.push(c), a.bulletBehindSet.push(d)) : (a.graphsSet.push(c), a.bulletSet.push(d));
		var e = this.bulletAxis;
		AmCharts.isString(e) && (this.bulletAxis = a.getValueAxisById(e));
		this.bulletSet = d;
		this.scrollbar || (e = a.marginLeftReal, a = a.marginTopReal, c.translate(e, a), d.translate(e, a));
		b = b.set();
		AmCharts.remove(this.columnsSet);
		c.push(b);
		this.set = c;
		this.columnsSet = b;
		this.columnsArray = [];
		this.ownColumns = [];
		this.allBullets = [];
		this.animationArray = [];
		AmCharts.ifArray(this.data) && (c = !1, "xy" == this.chart.type ? this.xAxis.axisCreated && this.yAxis.axisCreated && (c = !0) : this.valueAxis.axisCreated && (c = !0), !this.hidden && c && this.createGraph())
	},
	createGraph: function() {
		var a = this,
		b = a.chart;
		"inside" == a.labelPosition && "column" != a.type && (a.labelPosition = "bottom");
		a.startAlpha = b.startAlpha;
		a.seqAn = b.sequencedAnimation;
		a.baseCoord = a.valueAxis.baseCoord;
		void 0 === a.fillAlphas && (a.fillAlphas = 0);
		a.bulletColorR = a.bulletColor;
		void 0 === a.bulletColorR && (a.bulletColorR = a.lineColorR, a.bulletColorNegative = a.negativeLineColor);
		void 0 === a.bulletAlpha && (a.bulletAlpha = a.lineAlpha);
		clearTimeout(a.playedTO);
		if (!isNaN(a.valueAxis.min) && !isNaN(a.valueAxis.max)) {
			switch (b.type) {
			case "serial":
				a.categoryAxis && (a.createSerialGraph(), "candlestick" == a.type && 1 > a.valueAxis.minMaxMultiplier && a.positiveClip(a.set));
				break;
			case "radar":
				a.createRadarGraph();
				break;
			case "xy":
				a.createXYGraph(),
				a.positiveClip(a.set)
			}
			a.playedTO = setTimeout(function() {
				a.setAnimationPlayed.call(a)
			},
			500 * a.chart.startDuration)
		}
	},
	setAnimationPlayed: function() {
		this.animationPlayed = !0
	},
	createXYGraph: function() {
		var a = [],
		b = [],
		c = this.xAxis,
		d = this.yAxis;
		this.pmh = d.viH + 1;
		this.pmw = c.viW + 1;
		this.pmy = this.pmx = 0;
		var e;
		for (e = this.start; e <= this.end; e++) {
			var f = this.data[e].axes[c.id].graphs[this.id],
			g = f.values,
			h = g.x,
			k = g.y,
			g = c.getCoordinate(h),
			l = d.getCoordinate(k); ! isNaN(h) && !isNaN(k) && (a.push(g), b.push(l), (h = this.createBullet(f, g, l, e)) || (h = 0), k = this.labelText) && (f = this.createLabel(f, g, l, k), this.allBullets.push(f), this.positionLabel(g, l, f, this.labelPosition, h))
		}
		this.drawLineGraph(a, b);
		this.launchAnimation()
	},
	createRadarGraph: function() {
		var a = this.valueAxis.stackType,
		b = [],
		c = [],
		d,
		e,
		f;
		for (f = this.start; f <= this.end; f++) {
			var g = this.data[f].axes[this.valueAxis.id].graphs[this.id],
			h;
			h = "none" == a || "3d" == a ? g.values.value: g.values.close;
			if (isNaN(h)) this.drawLineGraph(b, c),
			b = [],
			c = [];
			else {
				var k = this.y - (this.valueAxis.getCoordinate(h) - this.height),
				l = 180 - 360 / (this.end - this.start + 1) * f;
				h = k * Math.sin(l / 180 * Math.PI);
				k *= Math.cos(l / 180 * Math.PI);
				b.push(h);
				c.push(k); (l = this.createBullet(g, h, k, f)) || (l = 0);
				var m = this.labelText;
				m && (g = this.createLabel(g, h, k, m), this.allBullets.push(g), this.positionLabel(h, k, g, this.labelPosition, l));
				isNaN(d) && (d = h);
				isNaN(e) && (e = k)
			}
		}
		b.push(d);
		c.push(e);
		this.drawLineGraph(b, c);
		this.launchAnimation()
	},
	positionLabel: function(a, b, c, d, e) {
		var f = c.getBBox();
		switch (d) {
		case "left":
			a -= (f.width + e) / 2 + 2;
			break;
		case "top":
			b -= (e + f.height) / 2 + 1;
			break;
		case "right":
			a += (f.width + e) / 2 + 2;
			break;
		case "bottom":
			b += (e + f.height) / 2 + 1
		}
		c.translate(a, b)
	},
	getGradRotation: function() {
		var a = 270;
		"horizontal" == this.gradientOrientation && (a = 0);
		return this.gradientRotation = a
	},
	createSerialGraph: function() {
		this.dashLengthSwitched = this.fillColorsSwitched = this.lineColorSwitched = void 0;
		var a = this.chart,
		b = this.id,
		c = this.index,
		d = this.data,
		e = this.chart.container,
		f = this.valueAxis,
		g = this.type,
		h = this.columnWidthReal;
		isNaN(this.columnWidth) || (h = this.columnWidth);
		isNaN(h) && (h = 0.8);
		var k = this.width,
		l = this.height,
		m = this.y,
		n = this.rotate,
		p = this.columnCount,
		r = AmCharts.toCoordinate(this.cornerRadiusTop, h / 2),
		u = this.connect,
		q = [],
		t = [],
		v,
		s,
		w,
		A,
		C = this.chart.graphs.length,
		y,
		z = this.dx / this.depthCount,
		J = this.dy / this.depthCount,
		I = f.stackType,
		B = this.labelPosition,
		Z = this.start,
		S = this.end,
		da = this.scrollbar,
		X = this.categoryAxis,
		Fa = this.baseCoord,
		ea = this.negativeBase,
		$ = this.columnIndex,
		L = this.lineThickness,
		V = this.lineAlpha,
		oa = this.lineColorR,
		Y = this.dashLength,
		W = this.set,
		aa = B,
		ha = this.getGradRotation(),
		G = this.chart.columnSpacing,
		D = X.cellWidth,
		ia = (D * h - p) / p;
		G > ia && (G = ia);
		var P, x, Va, ab = l + 1,
		bb = k + 1,
		Wa = 0,
		cb = 0,
		db, eb, Xa, Ya, Kb = this.fillColorsR,
		Ka = this.negativeFillColors,
		Aa = this.negativeLineColor,
		La = this.fillAlphas,
		Ma = this.negativeFillAlphas;
		"object" == typeof La && (La = La[0]);
		"object" == typeof Ma && (Ma = Ma[0]);
		var Za = f.getCoordinate(f.min);
		f.logarithmic && (Za = f.getCoordinate(f.minReal));
		this.minCoord = Za;
		this.resetBullet && (this.bullet = "none");
		if (!da && ("line" == g || "smoothedLine" == g || "step" == g) && (1 == d.length && "step" != g && "none" == this.bullet && (this.bullet = "round", this.resetBullet = !0), Ka || void 0 != Aa)) {
			var Ga = ea;
			Ga > f.max && (Ga = f.max);
			Ga < f.min && (Ga = f.min);
			f.logarithmic && (Ga = f.minReal);
			var xa = f.getCoordinate(Ga),
			ub = f.getCoordinate(f.max);
			n ? (ab = l, bb = Math.abs(ub - xa), db = l, eb = Math.abs(Za - xa), Ya = cb = 0, f.reversed ? (Wa = 0, Xa = xa) : (Wa = xa, Xa = 0)) : (bb = k, ab = Math.abs(ub - xa), eb = k, db = Math.abs(Za - xa), Xa = Wa = 0, f.reversed ? (Ya = m, cb = xa) : Ya = xa + 1)
		}
		var ya = Math.round;
		this.pmx = ya(Wa);
		this.pmy = ya(cb);
		this.pmh = ya(ab);
		this.pmw = ya(bb);
		this.nmx = ya(Xa);
		this.nmy = ya(Ya);
		this.nmh = ya(db);
		this.nmw = ya(eb);
		AmCharts.isModern || (this.nmy = this.nmx = 0, this.nmh = this.height);
		h = "column" == g ? (D * h - G * (p - 1)) / p: D * h;
		1 > h && (h = 1);
		var H;
		if ("line" == g || "step" == g || "smoothedLine" == g) {
			if (0 < Z) {
				for (H = Z - 1; - 1 < H; H--) if (P = d[H], x = P.axes[f.id].graphs[b], Va = x.values.value, !isNaN(Va)) {
					Z = H;
					break
				}
				if (this.lineColorField) for (H = Z; - 1 < H; H--) if (P = d[H], x = P.axes[f.id].graphs[b], x.lineColor) {
					this.bulletColorSwitched = this.lineColorSwitched = x.lineColor;
					break
				}
				if (this.fillColorsField) for (H = Z; - 1 < H; H--) if (P = d[H], x = P.axes[f.id].graphs[b], x.fillColors) {
					this.fillColorsSwitched = x.fillColors;
					break
				}
				if (this.dashLengthField) for (H = Z; - 1 < H; H--) if (P = d[H], x = P.axes[f.id].graphs[b], !isNaN(x.dashLength)) {
					this.dashLengthSwitched = x.dashLength;
					break
				}
			}
			if (S < d.length - 1) for (H = S + 1; H < d.length; H++) if (P = d[H], x = P.axes[f.id].graphs[b], Va = x.values.value, !isNaN(Va)) {
				S = H;
				break
			}
		}
		S < d.length - 1 && S++;
		var T = [],
		U = [],
		Na = !1;
		if ("line" == g || "step" == g || "smoothedLine" == g) if (this.stackable && "regular" == I || "100%" == I || this.fillToGraph) Na = !0;
		var vb = this.noStepRisers;
		for (H = Z; H <= S; H++) {
			P = d[H];
			x = P.axes[f.id].graphs[b];
			x.index = H;
			var M, N, K, ba, ja = NaN,
			F = NaN,
			E = NaN,
			Q = NaN,
			O = NaN,
			Oa = NaN,
			Ba = NaN,
			Pa = NaN,
			Ca = NaN,
			ca = NaN,
			ga = NaN,
			pa = NaN,
			qa = NaN,
			R = NaN,
			fb = NaN,
			gb = NaN,
			la = NaN,
			na = void 0,
			za = Kb,
			Qa = La,
			ra = oa,
			ma, sa, Ra = this.pattern;
			void 0 != x.pattern && (Ra = x.pattern);
			void 0 != x.color && (za = x.color);
			x.fillColors && (za = x.fillColors);
			isNaN(x.alpha) || (Qa = x.alpha);
			isNaN(x.dashLength) || (Y = x.dashLength);
			var ta = x.values;
			f.recalculateToPercents && (ta = x.percents);
			if (ta) {
				R = this.stackable && "none" != I && "3d" != I ? ta.close: ta.value;
				if ("candlestick" == g || "ohlc" == g) R = ta.close,
				gb = ta.low,
				Ba = f.getCoordinate(gb),
				fb = ta.high,
				Ca = f.getCoordinate(fb);
				la = ta.open;
				E = f.getCoordinate(R);
				isNaN(la) || (O = f.getCoordinate(la));
				if (!da) switch (this.showBalloonAt) {
				case "close":
					x.y = E;
					break;
				case "open":
					x.y = O;
					break;
				case "high":
					x.y = Ca;
					break;
				case "low":
					x.y = Ba
				}
				var ja = P.x[X.id],
				Ha = this.periodSpan - 1,
				ka = Math.floor(D / 2) + Math.floor(Ha * D / 2),
				ua = ka,
				wb = 0;
				"left" == this.stepDirection && (wb = (2 * D + Ha * D) / 2, ja -= wb);
				"start" == this.pointPosition && (ja -= D / 2 + Math.floor(Ha * D / 2), ka = 0, ua = Math.floor(D) + Math.floor(Ha * D));
				"end" == this.pointPosition && (ja += D / 2 + Math.floor(Ha * D / 2), ka = Math.floor(D) + Math.floor(Ha * D), ua = 0);
				if (vb) {
					var hb = this.columnWidth;
					isNaN(hb) || (ka *= hb, ua *= hb)
				}
				da || (x.x = ja); - 1E5 > ja && (ja = -1E5);
				ja > k + 1E5 && (ja = k + 1E5);
				n ? (F = E, Q = O, O = E = ja, isNaN(la) && !this.fillToGraph && (Q = Fa), Oa = Ba, Pa = Ca) : (Q = F = ja, isNaN(la) && !this.fillToGraph && (O = Fa));
				R < la && (x.isNegative = !0, Ka && (za = Ka), Ma && (Qa = Ma), void 0 != Aa && (ra = Aa));
				switch (g) {
				case "line":
					isNaN(R) ? u || (this.drawLineGraph(q, t, T, U), q = [], t = [], T = [], U = []) : (x.isNegative = R < ea ? !0 : !1, q.push(F), t.push(E), ca = F, ga = E, pa = F, qa = E, !Na || isNaN(O) || isNaN(Q) || (T.push(Q), U.push(O)), void 0 == x.lineColor && void 0 == x.fillColors && isNaN(x.dashLength) || (this.drawLineGraph(q, t, T, U), q = [F], t = [E], T = [], U = [], this.lineColorSwitched = x.lineColor, this.fillColorsSwitched = x.fillColors, this.dashLengthSwitched = x.dashLength));
					break;
				case "smoothedLine":
					isNaN(R) ? u || (this.drawSmoothedGraph(q, t, T, U), q = [], t = [], T = [], U = []) : (x.isNegative = R < ea ? !0 : !1, q.push(F), t.push(E), ca = F, ga = E, pa = F, qa = E, !Na || isNaN(O) || isNaN(Q) || (T.push(Q), U.push(O)), void 0 == x.lineColor && void 0 == x.fillColors && isNaN(x.dashLength) || (this.drawSmoothedGraph(q, t, T, U), q = [F], t = [E], T = [], U = [], this.lineColorSwitched = x.lineColor, this.fillColorsSwitched = x.fillColors, this.dashLengthSwitched = x.dashLength));
					break;
				case "step":
					if (!isNaN(R)) x.isNegative = R < ea ? !0 : !1,
					void 0 == x.lineColor && void 0 == x.fillColors && isNaN(x.dashLength) || (this.drawLineGraph(q, t, T, U), q = [], t = [], T = [], U = [], this.lineColorSwitched = x.lineColor, this.fillColorsSwitched = x.fillColors, this.dashLengthSwitched = x.dashLength),
					n ? (isNaN(v) || (q.push(v), t.push(E - ka)), t.push(E - ka), q.push(F), t.push(E + ua), q.push(F), !Na || isNaN(O) || isNaN(Q) || (T.push(w), U.push(O - ka), T.push(Q), U.push(O - ka), T.push(Q), U.push(O + ua))) : (isNaN(s) || (t.push(s), q.push(v), t.push(s), q.push(F - ka)), q.push(F - ka), t.push(E), q.push(F + ua), t.push(E), !Na || isNaN(O) || isNaN(Q) || (T.push(Q - ka), U.push(A), T.push(Q - ka), U.push(O), T.push(Q + ua), U.push(O))),
					v = F,
					s = E,
					w = Q,
					A = O,
					ca = F,
					ga = E,
					pa = F,
					qa = E,
					vb && (v = s = NaN, this.drawLineGraph(q, t, T, U), q = [], t = [], T = [], U = []);
					else if (!u) {
						if (1 >= this.periodSpan || 1 < this.periodSpan && F - v > ka + ua) v = s = NaN;
						this.drawLineGraph(q, t, T, U);
						q = [];
						t = [];
						T = [];
						U = []
					}
					break;
				case "column":
					ma = ra;
					void 0 != x.lineColor && (ma = x.lineColor);
					if (!isNaN(R)) {
						R < ea ? (x.isNegative = !0, Ka && (za = Ka), void 0 != Aa && (ma = Aa)) : x.isNegative = !1;
						var xb = f.min,
						yb = f.max;
						if (! (R < xb && la < xb || R > yb && la > yb)) if (n) {
							"3d" == I ? (N = E - 0.5 * (h + G) + G / 2 + J * $, M = Q + z * $) : (N = Math.floor(E - (p / 2 - $) * (h + G) + G / 2), M = Q);
							K = h;
							ca = F;
							ga = N + h / 2;
							pa = F;
							qa = N + h / 2;
							N + K > l && (K = l - N);
							0 > N && (K += N, N = 0);
							ba = F - Q;
							var Lb = M;
							M = AmCharts.fitToBounds(M, 0, k);
							ba += Lb - M;
							ba = AmCharts.fitToBounds(ba, -M, k - M + z * $);
							if (N < l && 0 < K && (na = new AmCharts.Cuboid(e, ba, K, z - a.d3x, J - a.d3y, za, Qa, L, ma, V, ha, r, n, Y, Ra), "bottom" != B && "inside" != B && "middle" != B)) if (B = f.reversed ? "left": "right", 0 > R) B = f.reversed ? "right": "left";
							else if ("regular" == I || "100%" == I) ca += this.dx
						} else {
							"3d" == I ? (M = F - 0.5 * (h + G) + G / 2 + z * $, N = O + J * $) : (M = F - (p / 2 - $) * (h + G) + G / 2, N = O);
							K = h;
							ca = M + h / 2;
							ga = E;
							pa = M + h / 2;
							qa = E;
							M + K > k + $ * z && (K = k - M + $ * z);
							0 > M && (K += M, M = 0);
							ba = E - O;
							var Mb = N;
							N = AmCharts.fitToBounds(N, this.dy, l);
							ba += Mb - N;
							ba = AmCharts.fitToBounds(ba, -N + J * $, l - N);
							if (M < k + $ * z && 0 < K) if (na = new AmCharts.Cuboid(e, K, ba, z - a.d3x, J - a.d3y, za, Qa, L, ma, this.lineAlpha, ha, r, n, Y, Ra), 0 > R && "middle" != B && "inside" != B) B = "bottom";
							else if (B = aa, "regular" == I || "100%" == I) ga += this.dy
						}
						if (na && (sa = na.set, sa.translate(M, N), this.columnsSet.push(sa), (x.url || this.showHandOnHover) && sa.setAttr("cursor", "pointer"), !da)) {
							"none" == I && (y = n ? (this.end + 1 - H) * C - c: C * H + c);
							"3d" == I && (n ? (y = (C - c) * (this.end + 1 - H), ca += z * this.columnIndex, pa += z * this.columnIndex, x.y += z * this.columnIndex) : (y = (C - c) * (H + 1), ca += 3, ga += J * this.columnIndex + 7, qa += J * this.columnIndex, x.y += J * this.columnIndex));
							if ("regular" == I || "100%" == I)"inside" != B && (B = "middle"),
							y = n ? 0 < ta.value ? (this.end + 1 - H) * C + c: (this.end + 1 - H) * C - c: 0 < ta.value ? C * H + c: C * H - c;
							this.columnsArray.push({
								column: na,
								depth: y
							});
							x.x = n ? N + K / 2 : M + K / 2;
							this.ownColumns.push(na);
							this.animateColumns(na, H, F, Q, E, O);
							this.addListeners(sa, x)
						}
					}
					break;
				case "candlestick":
					if (!isNaN(la) && !isNaN(R)) {
						var $a, ib;
						ma = ra;
						void 0 != x.lineColor && (ma = x.lineColor);
						if (n) {
							if (N = E - h / 2, M = Q, K = h, N + K > l && (K = l - N), 0 > N && (K += N, N = 0), N < l && 0 < K) {
								var jb, kb;
								R > la ? (jb = [F, Pa], kb = [Q, Oa]) : (jb = [Q, Pa], kb = [F, Oa]); ! isNaN(Pa) && !isNaN(Oa) && E < l && 0 < E && ($a = AmCharts.line(e, jb, [E, E], ma, V, L), ib = AmCharts.line(e, kb, [E, E], ma, V, L));
								ba = F - Q;
								na = new AmCharts.Cuboid(e, ba, K, z, J, za, La, L, ma, V, ha, r, n, Y, Ra)
							}
						} else if (M = F - h / 2, N = O + L / 2, K = h, M + K > k && (K = k - M), 0 > M && (K += M, M = 0), ba = E - O, M < k && 0 < K) {
							var na = new AmCharts.Cuboid(e, K, ba, z, J, za, Qa, L, ma, V, ha, r, n, Y, Ra),
							lb,
							mb;
							R > la ? (lb = [E, Ca], mb = [O, Ba]) : (lb = [O, Ca], mb = [E, Ba]); ! isNaN(Ca) && !isNaN(Ba) && F < k && 0 < F && ($a = AmCharts.line(e, [F, F], lb, ma, V, L), ib = AmCharts.line(e, [F, F], mb, ma, V, L))
						}
						na && (sa = na.set, W.push(sa), sa.translate(M, N - L / 2), (x.url || this.showHandOnHover) && sa.setAttr("cursor", "pointer"), $a && (W.push($a), W.push(ib)), ca = F, ga = E, pa = F, qa = E, da || (x.x = n ? N + K / 2 : M + K / 2, this.animateColumns(na, H, F, Q, E, O), this.addListeners(sa, x)))
					}
					break;
				case "ohlc":
					if (! (isNaN(la) || isNaN(fb) || isNaN(gb) || isNaN(R))) {
						R < la && (x.isNegative = !0, void 0 != Aa && (ra = Aa));
						var nb, ob, pb;
						if (n) {
							var qb = E - h / 2,
							qb = AmCharts.fitToBounds(qb, 0, l),
							zb = AmCharts.fitToBounds(E, 0, l),
							rb = E + h / 2,
							rb = AmCharts.fitToBounds(rb, 0, l);
							ob = AmCharts.line(e, [Q, Q], [qb, zb], ra, V, L, Y);
							0 < E && E < l && (nb = AmCharts.line(e, [Oa, Pa], [E, E], ra, V, L, Y));
							pb = AmCharts.line(e, [F, F], [zb, rb], ra, V, L, Y)
						} else {
							var sb = F - h / 2,
							sb = AmCharts.fitToBounds(sb, 0, k),
							Ab = AmCharts.fitToBounds(F, 0, k),
							tb = F + h / 2,
							tb = AmCharts.fitToBounds(tb, 0, k);
							ob = AmCharts.line(e, [sb, Ab], [O, O], ra, V, L, Y);
							0 < F && F < k && (nb = AmCharts.line(e, [F, F], [Ba, Ca], ra, V, L, Y));
							pb = AmCharts.line(e, [Ab, tb], [E, E], ra, V, L, Y)
						}
						W.push(ob);
						W.push(nb);
						W.push(pb);
						ca = F;
						ga = E;
						pa = F;
						qa = E
					}
				}
				if (!da && !isNaN(R)) {
					var Bb = this.hideBulletsCount;
					if (this.end - this.start <= Bb || 0 === Bb) {
						var Da = this.createBullet(x, pa, qa, H);
						Da || (Da = 0);
						var Cb = this.labelText;
						if (Cb) {
							var fa = this.createLabel(x, 0, 0, Cb),
							va = 0,
							wa = 0,
							Db = fa.getBBox(),
							Ia = Db.width,
							Ea = Db.height;
							switch (B) {
							case "left":
								va = -(Ia / 2 + Da / 2 + 3);
								break;
							case "top":
								wa = -(Ea / 2 + Da / 2 + 3);
								break;
							case "right":
								va = Da / 2 + 2 + Ia / 2;
								break;
							case "bottom":
								n && "column" == g ? (ca = Fa, 0 > R || 0 < R && f.reversed ? (va = -6, fa.attr({
									"text-anchor": "end"
								})) : (va = 6, fa.attr({
									"text-anchor": "start"
								}))) : (wa = Da / 2 + Ea / 2, fa.x = -(Ia / 2 + 2));
								break;
							case "middle":
								"column" == g && (n ? (wa = -(Ea / 2) + this.fontSize / 2, va = -(F - Q) / 2 - z, 0 > ba && (va += z), Math.abs(F - Q) < Ia && !this.showAllValueLabels && (fa.remove(), fa = null)) : (wa = -(E - O) / 2, 0 > ba && (wa -= J), Math.abs(E - O) < Ea && !this.showAllValueLabels && (fa.remove(), fa = null)));
								break;
							case "inside":
								n ? (wa = -(Ea / 2) + this.fontSize / 2, va = 0 > ba ? Ia / 2 + 6 : -Ia / 2 - 6) : wa = 0 > ba ? Ea: -Ea
							}
							if (fa) {
								if (isNaN(ga) || isNaN(ca)) fa.remove(),
								fa = null;
								else if (ca += va, ga += wa, fa.translate(ca, ga), n) {
									if (0 > ga || ga > l) fa.remove(),
									fa = null
								} else {
									var Eb = 0;
									"3d" == I && (Eb = z * $);
									if (0 > ca || ca > k + Eb) fa.remove(),
									fa = null
								}
								fa && this.allBullets.push(fa)
							}
						}
						if ("regular" == I || "100%" == I) {
							var Fb = f.totalText;
							if (Fb) {
								var Ja = this.createLabel(x, 0, 0, Fb, f.totalTextColor);
								this.allBullets.push(Ja);
								var Gb = Ja.getBBox(),
								Hb = Gb.width,
								Ib = Gb.height,
								Sa,
								Ta,
								Jb = f.totals[H];
								Jb && Jb.remove();
								var Ua = 0;
								"column" != g && (Ua = Da);
								n ? (Ta = E, Sa = 0 > R ? F - Hb / 2 - 2 - Ua: F + Hb / 2 + 3 + Ua) : (Sa = F, Ta = 0 > R ? E + Ib / 2 + Ua: E - Ib / 2 - 3 - Ua);
								Ja.translate(Sa, Ta);
								f.totals[H] = Ja;
								n ? (0 > Ta || Ta > l) && Ja.remove() : (0 > Sa || Sa > k) && Ja.remove()
							}
						}
					}
				}
			}
		}
		if ("line" == g || "step" == g || "smoothedLine" == g)"smoothedLine" == g ? this.drawSmoothedGraph(q, t, T, U) : this.drawLineGraph(q, t, T, U),
		da || this.launchAnimation();
		this.bulletsHidden && this.hideBullets()
	},
	animateColumns: function(a, b, c, d, e, f) {
		var g = this;
		c = g.chart.startDuration;
		0 < c && !g.animationPlayed && (g.seqAn ? (a.set.hide(), g.animationArray.push(a), a = setTimeout(function() {
			g.animate.call(g)
		},
		c / (g.end - g.start + 1) * (b - g.start) * 1E3), g.timeOuts.push(a)) : g.animate(a))
	},
	createLabel: function(a, b, c, d, e) {
		var f = this.chart,
		g = a.labelColor;
		g || (g = this.color);
		g || (g = f.color);
		e && (g = e);
		e = this.fontSize;
		void 0 === e && (this.fontSize = e = f.fontSize);
		a = f.formatString(d, a);
		a = AmCharts.cleanFromEmpty(a);
		f = AmCharts.text(this.container, a, g, f.fontFamily, e);
		f.translate(b, c);
		this.bulletSet.push(f);
		return f
	},
	positiveClip: function(a) {
		a.clipRect(this.pmx, this.pmy, this.pmw, this.pmh)
	},
	negativeClip: function(a) {
		a.clipRect(this.nmx, this.nmy, this.nmw, this.nmh)
	},
	drawLineGraph: function(a, b, c, d) {
		var e = this;
		if (1 < a.length) {
			var f = e.set,
			g = e.container,
			h = g.set(),
			k = g.set();
			f.push(k);
			f.push(h);
			var l = e.lineAlpha,
			m = e.lineThickness,
			f = e.fillAlphas,
			n = e.lineColorR,
			p = e.negativeLineAlpha;
			isNaN(p) && (p = l);
			var r = e.lineColorSwitched;
			r && (n = r);
			var r = e.fillColorsR,
			u = e.fillColorsSwitched;
			u && (r = u);
			var q = e.dashLength; (u = e.dashLengthSwitched) && (q = u);
			var u = e.negativeLineColor,
			t = e.negativeFillColors,
			v = e.negativeFillAlphas,
			s = e.baseCoord;
			0 !== e.negativeBase && (s = e.valueAxis.getCoordinate(e.negativeBase));
			l = AmCharts.line(g, a, b, n, l, m, q, !1, !0);
			h.push(l);
			h.click(function(a) {
				e.handleGraphClick(a)
			});
			void 0 !== u && (m = AmCharts.line(g, a, b, u, p, m, q, !1, !0), k.push(m));
			if (0 < f || 0 < v) if (m = a.join(";").split(";"), p = b.join(";").split(";"), "serial" == e.chart.type && (0 < c.length ? (c.reverse(), d.reverse(), m = a.concat(c), p = b.concat(d)) : e.rotate ? (p.push(p[p.length - 1]), m.push(s), p.push(p[0]), m.push(s), p.push(p[0]), m.push(m[0])) : (m.push(m[m.length - 1]), p.push(s), m.push(m[0]), p.push(s), m.push(a[0]), p.push(p[0]))), a = e.gradientRotation, 0 < f && (b = AmCharts.polygon(g, m, p, r, f, 1, "#000", 0, a), b.pattern(e.pattern), h.push(b)), t || void 0 !== u) isNaN(v) && (v = f),
			t || (t = u),
			g = AmCharts.polygon(g, m, p, t, v, 1, "#000", 0, a),
			g.pattern(e.pattern),
			k.push(g),
			k.click(function(a) {
				e.handleGraphClick(a)
			});
			e.applyMask(k, h)
		}
	},
	applyMask: function(a, b) {
		var c = a.length();
		"serial" != this.chart.type || this.scrollbar || (this.positiveClip(b), 0 < c && this.negativeClip(a))
	},
	drawSmoothedGraph: function(a, b, c, d) {
		if (1 < a.length) {
			var e = this.set,
			f = this.container,
			g = f.set(),
			h = f.set();
			e.push(h);
			e.push(g);
			var k = this.lineAlpha,
			l = this.lineThickness,
			e = this.dashLength,
			m = this.fillAlphas,
			n = this.lineColorR,
			p = this.fillColorsR,
			r = this.negativeLineColor,
			u = this.negativeFillColors,
			q = this.negativeFillAlphas,
			t = this.baseCoord,
			v = this.lineColorSwitched;
			v && (n = v); (v = this.fillColorsSwitched) && (p = v);
			v = this.negativeLineAlpha;
			isNaN(v) && (v = k);
			k = new AmCharts.Bezier(f, a, b, n, k, l, p, 0, e);
			g.push(k.path);
			void 0 !== r && (l = new AmCharts.Bezier(f, a, b, r, v, l, p, 0, e), h.push(l.path));
			0 < m && (k = a.join(";").split(";"), n = b.join(";").split(";"), l = "", 0 < c.length ? (c.push("M"), d.push("M"), c.reverse(), d.reverse(), k = a.concat(c), n = b.concat(d)) : (this.rotate ? (l += " L" + t + "," + b[b.length - 1], l += " L" + t + "," + b[0]) : (l += " L" + a[a.length - 1] + "," + t, l += " L" + a[0] + "," + t), l += " L" + a[0] + "," + b[0]), c = new AmCharts.Bezier(f, k, n, NaN, 0, 0, p, m, e, l), c.path.pattern(this.pattern), g.push(c.path), u || void 0 !== r) && (q || (q = m), u || (u = r), a = new AmCharts.Bezier(f, a, b, NaN, 0, 0, u, q, e, l), a.path.pattern(this.pattern), h.push(a.path));
			this.applyMask(h, g)
		}
	},
	launchAnimation: function() {
		var a = this,
		b = a.chart.startDuration;
		if (0 < b && !a.animationPlayed) {
			var c = a.set,
			d = a.bulletSet;
			AmCharts.VML || (c.attr({
				opacity: a.startAlpha
			}), d.attr({
				opacity: a.startAlpha
			}));
			c.hide();
			d.hide();
			a.seqAn ? (b = setTimeout(function() {
				a.animateGraphs.call(a)
			},
			a.index * b * 1E3), a.timeOuts.push(b)) : a.animateGraphs()
		}
	},
	animateGraphs: function() {
		var a = this.chart,
		b = this.set,
		c = this.bulletSet,
		d = this.x,
		e = this.y;
		b.show();
		c.show();
		var f = a.startDuration,
		a = a.startEffect;
		b && (this.rotate ? (b.translate( - 1E3, e), c.translate( - 1E3, e)) : (b.translate(d, -1E3), c.translate(d, -1E3)), b.animate({
			opacity: 1,
			translate: d + "," + e
		},
		f, a), c.animate({
			opacity: 1,
			translate: d + "," + e
		},
		f, a))
	},
	animate: function(a) {
		var b = this.chart,
		c = this.animationArray; ! a && 0 < c.length && (a = c[0], c.shift());
		c = AmCharts[AmCharts.getEffect(b.startEffect)];
		b = b.startDuration;
		a && (this.rotate ? a.animateWidth(b, c) : a.animateHeight(b, c), a.set.show())
	},
	legendKeyColor: function() {
		var a = this.legendColor,
		b = this.lineAlpha;
		void 0 === a && (a = this.lineColorR, 0 === b && (b = this.fillColorsR) && (a = "object" == typeof b ? b[0] : b));
		return a
	},
	legendKeyAlpha: function() {
		var a = this.legendAlpha;
		void 0 === a && (a = this.lineAlpha, this.fillAlphas > a && (a = this.fillAlphas), 0 === a && (a = this.bulletAlpha), 0 === a && (a = 1));
		return a
	},
	createBullet: function(a, b, c, d) {
		d = this.container;
		var e = this.bulletOffset,
		f = this.bulletSize;
		isNaN(a.bulletSize) || (f = a.bulletSize);
		var g = a.values.value;
		isNaN(this.maxValue) || isNaN(g) || (f = g / this.maxValue * this.maxBulletSize);
		var h = f;
		this.bulletAxis && (f = a.values.error, isNaN(f) || (g = f), f = this.bulletAxis.stepWidth * g);
		f < this.minBulletSize && (f = this.minBulletSize);
		this.rotate ? b += e: c -= e;
		var k, l = this.bulletColorR;
		a.lineColor && (this.bulletColorSwitched = a.lineColor);
		this.bulletColorSwitched && (l = this.bulletColorSwitched);
		a.isNegative && void 0 !== this.bulletColorNegative && (l = this.bulletColorNegative);
		void 0 !== a.color && (l = a.color);
		var m;
		"xy" == this.chart.type && this.valueField && (m = this.pattern, a.pattern && (m = a.pattern));
		e = this.bullet;
		a.bullet && (e = a.bullet);
		var g = this.bulletBorderThickness,
		n = this.bulletBorderColorR,
		p = this.bulletBorderAlpha,
		r = this.bulletAlpha;
		n || (n = l);
		this.useLineColorForBulletBorder && (n = this.lineColorR);
		var u = a.alpha;
		isNaN(u) || (r = u);
		if ("none" != this.bullet || a.bullet) k = AmCharts.bullet(d, e, f, l, r, g, n, p, h, 0, m);
		if (this.customBullet || a.customBullet) m = this.customBullet,
		a.customBullet && (m = a.customBullet),
		m && (k && k.remove(), "function" == typeof m ? (k = new m, k.chart = this.chart, a.bulletConfig && (k.availableSpace = c, k.graph = this, a.bulletConfig.minCoord = this.minCoord - c, k.bulletConfig = a.bulletConfig), k.write(d), k = k.set) : (this.chart.path && (m = this.chart.path + m), k = d.set(), d = d.image(m, 0, 0, f, f), k.push(d), this.centerCustomBullets && d.translate( - f / 2, -f / 2)));
		k && ((a.url || this.showHandOnHover) && k.setAttr("cursor", "pointer"), "serial" == this.chart.type && (0 > b - 0 || b - 0 > this.width || c < -f / 2 || c - 0 > this.height) && (k.remove(), k = null), k && (this.bulletSet.push(k), k.translate(b, c), this.addListeners(k, a), this.allBullets.push(k)), a.bx = b, a.by = c);
		a.bulletGraphics = k;
		return f
	},
	showBullets: function() {
		var a = this.allBullets,
		b;
		this.bulletsHidden = !1;
		for (b = 0; b < a.length; b++) a[b].show()
	},
	hideBullets: function() {
		var a = this.allBullets,
		b;
		this.bulletsHidden = !0;
		for (b = 0; b < a.length; b++) a[b].hide()
	},
	addListeners: function(a, b) {
		var c = this;
		a.mouseover(function(a) {
			c.handleRollOver(b, a)
		}).mouseout(function(a) {
			c.handleRollOut(b, a)
		}).touchend(function(a) {
			c.handleRollOver(b, a);
			c.chart.panEventsEnabled && c.handleClick(b, a)
		}).touchstart(function(a) {
			c.handleRollOver(b, a)
		}).click(function(a) {
			c.handleClick(b, a)
		}).dblclick(function(a) {
			c.handleDoubleClick(b, a)
		}).contextmenu(function(a) {
			c.handleRightClick(b, a)
		})
	},
	handleRollOver: function(a, b) {
		if (a) {
			var c = this.chart,
			d = {
				type: "rollOverGraphItem",
				item: a,
				index: a.index,
				graph: this,
				target: this,
				chart: this.chart,
				event: b
			};
			this.fire("rollOverGraphItem", d);
			c.fire("rollOverGraphItem", d);
			clearTimeout(c.hoverInt);
			d = this.showBalloon;
			c.chartCursor && "serial" == c.type && (d = !1, !c.chartCursor.valueBalloonsEnabled && this.showBalloon && (d = !0));
			if (d) {
				var d = c.formatString(this.balloonText, a, !0),
				e = this.balloonFunction;
				e && (d = e(a, a.graph));
				d = AmCharts.cleanFromEmpty(d);
				e = c.getBalloonColor(this, a);
				c.balloon.showBullet = !1;
				c.balloon.pointerOrientation = "V";
				var f = a.x,
				g = a.y;
				c.rotate && (f = a.y, g = a.x);
				c.showBalloon(d, e, !0, f + c.marginLeftReal, g + c.marginTopReal)
			}
		}
	},
	handleRollOut: function(a, b) {
		this.chart.hideBalloon();
		if (a) {
			var c = {
				type: "rollOutGraphItem",
				item: a,
				index: a.index,
				graph: this,
				target: this,
				chart: this.chart,
				event: b
			};
			this.fire("rollOutGraphItem", c);
			this.chart.fire("rollOutGraphItem", c)
		}
	},
	handleClick: function(a, b) {
		if (a) {
			var c = {
				type: "clickGraphItem",
				item: a,
				index: a.index,
				graph: this,
				target: this,
				chart: this.chart,
				event: b
			};
			this.fire("clickGraphItem", c);
			this.chart.fire("clickGraphItem", c);
			AmCharts.getURL(a.url, this.urlTarget)
		}
		this.handleGraphClick(b)
	},
	handleGraphClick: function(a) {
		a = {
			type: "clickGraph",
			graph: this,
			target: this,
			chart: this.chart,
			event: a
		};
		this.fire("clickGraph", a);
		this.chart.fire("clickGraph", a)
	},
	handleRightClick: function(a, b) {
		if (a) {
			var c = {
				type: "rightClickGraphItem",
				item: a,
				index: a.index,
				graph: this,
				target: this,
				chart: this.chart,
				event: b
			};
			this.fire("rightClickGraphItem", c);
			this.chart.fire("rightClickGraphItem", c)
		}
	},
	handleDoubleClick: function(a, b) {
		if (a) {
			var c = {
				type: "doubleClickGraphItem",
				item: a,
				index: a.index,
				graph: this,
				target: this,
				chart: this.chart,
				event: b
			};
			this.fire("doubleClickGraphItem", c);
			this.chart.fire("doubleClickGraphItem", c)
		}
	},
	zoom: function(a, b) {
		this.start = a;
		this.end = b;
		this.draw()
	},
	changeOpacity: function(a) {
		var b = this.set;
		b && b.setAttr("opacity", a);
		if (b = this.ownColumns) {
			var c;
			for (c = 0; c < b.length; c++) {
				var d = b[c].set;
				d && d.setAttr("opacity", a)
			}
		} (b = this.bulletSet) && b.setAttr("opacity", a)
	},
	destroy: function() {
		AmCharts.remove(this.set);
		AmCharts.remove(this.bulletSet);
		var a = this.timeOuts;
		if (a) {
			var b;
			for (b = 0; b < a.length; b++) clearTimeout(a[b])
		}
		this.timeOuts = []
	}
});
AmCharts.ChartCursor = AmCharts.Class({
	construct: function(a) {
		this.cname = "ChartCursor";
		this.createEvents("changed", "zoomed", "onHideCursor", "draw", "selected", "moved");
		this.enabled = !0;
		this.cursorAlpha = 1;
		this.selectionAlpha = 0.2;
		this.cursorColor = "#CC0000";
		this.categoryBalloonAlpha = 1;
		this.color = "#FFFFFF";
		this.type = "cursor";
		this.zoomed = !1;
		this.zoomable = !0;
		this.pan = !1;
		this.categoryBalloonDateFormat = "MMM DD, YYYY";
		this.categoryBalloonEnabled = this.valueBalloonsEnabled = !0;
		this.rolledOver = !1;
		this.cursorPosition = "middle";
		this.bulletsEnabled = this.skipZoomDispatch = !1;
		this.bulletSize = 8;
		this.selectWithoutZooming = this.oneBalloonOnly = !1;
		this.graphBulletSize = 1.7;
		this.animationDuration = 0.3;
		this.zooming = !1;
		this.adjustment = 0;
		AmCharts.applyTheme(this, a, this.cname)
	},
	draw: function() {
		var a = this;
		a.destroy();
		var b = a.chart,
		c = b.container;
		a.rotate = b.rotate;
		a.container = c;
		c = c.set();
		c.translate(a.x, a.y);
		a.set = c;
		b.cursorSet.push(c);
		c = new AmCharts.AmBalloon;
		c.chart = b;
		a.categoryBalloon = c;
		AmCharts.copyProperties(b.balloon, c);
		c.cornerRadius = 0;
		c.shadowAlpha = 0;
		c.borderThickness = 1;
		c.borderAlpha = 1;
		c.showBullet = !1;
		var d = a.categoryBalloonColor;
		void 0 === d && (d = a.cursorColor);
		c.fillColor = d;
		c.fillAlpha = a.categoryBalloonAlpha;
		c.borderColor = d;
		c.color = a.color;
		a.rotate && (c.pointerOrientation = "H");
		a.prevX = [];
		a.prevY = [];
		a.prevTX = [];
		a.prevTY = [];
		if (a.valueBalloonsEnabled) for (c = 0; c < b.graphs.length; c++) d = new AmCharts.AmBalloon,
		d.chart = b,
		AmCharts.copyProperties(b.balloon, d),
		b.graphs[c].valueBalloon = d;
		"cursor" == a.type ? a.createCursor() : a.createCrosshair();
		a.interval = setInterval(function() {
			a.detectMovement.call(a)
		},
		40)
	},
	updateData: function() {
		var a = this.chart;
		this.data = a.chartData;
		this.firstTime = a.firstTime;
		this.lastTime = a.lastTime
	},
	createCursor: function() {
		var a = this.chart,
		b = this.cursorAlpha,
		c = a.categoryAxis,
		d = c.position,
		e = c.inside,
		f = c.axisThickness,
		g = this.categoryBalloon,
		h, k, l = a.dx,
		m = a.dy,
		n = this.x,
		p = this.y,
		r = this.width,
		u = this.height,
		a = a.rotate,
		q = c.tickLength;
		g.pointerWidth = q;
		a ? (h = [0, r, r + l], k = [0, 0, m]) : (h = [l, 0, 0], k = [m, 0, u]);
		this.line = b = AmCharts.line(this.container, h, k, this.cursorColor, b, 1);
		this.set.push(b);
		a ? (e && (g.pointerWidth = 0), "right" == d ? e ? g.setBounds(n, p + m, n + r + l, p + u + m) : g.setBounds(n + r + l + f, p + m, n + r + 1E3, p + u + m) : e ? g.setBounds(n, p, r + n, u + p) : g.setBounds( - 1E3, -1E3, n - q - f, p + u + 15)) : (g.maxWidth = r, c.parseDates && (q = 0, g.pointerWidth = 0), "top" == d ? e ? g.setBounds(n + l, p + m, r + l + n, u + p) : g.setBounds(n + l, -1E3, r + l + n, p + m - q - f) : e ? g.setBounds(n, p, r + n, u + p - q) : g.setBounds(n, p + u + q + f - 1, n + r, p + u + q + f));
		this.hideCursor()
	},
	createCrosshair: function() {
		var a = this.cursorAlpha,
		b = this.container,
		c = AmCharts.line(b, [0, 0], [0, this.height], this.cursorColor, a, 1),
		a = AmCharts.line(b, [0, this.width], [0, 0], this.cursorColor, a, 1);
		this.set.push(c);
		this.set.push(a);
		this.vLine = c;
		this.hLine = a;
		this.hideCursor()
	},
	detectMovement: function() {
		var a = this.chart;
		if (a.mouseIsOver) {
			var b = a.mouseX - this.x,
			c = a.mouseY - this.y;
			0 < b && b < this.width && 0 < c && c < this.height ? (this.drawing ? this.rolledOver || a.setMouseCursor("crosshair") : this.pan && (this.rolledOver || a.setMouseCursor("move")), this.rolledOver = !0, this.setPosition()) : this.rolledOver && (this.handleMouseOut(), this.rolledOver = !1)
		} else this.rolledOver && (this.handleMouseOut(), this.rolledOver = !1)
	},
	getMousePosition: function() {
		var a, b = this.width,
		c = this.height;
		a = this.chart;
		this.rotate ? (a = a.mouseY - this.y, 0 > a && (a = 0), a > c && (a = c)) : (a = a.mouseX - this.x, 0 > a && (a = 0), a > b && (a = b));
		return a
	},
	updateCrosshair: function() {
		var a = this.chart,
		b = a.mouseX - this.x,
		c = a.mouseY - this.y,
		d = this.vLine,
		e = this.hLine,
		b = AmCharts.fitToBounds(b, 0, this.width),
		c = AmCharts.fitToBounds(c, 0, this.height);
		0 < this.cursorAlpha && (d.show(), e.show(), d.translate(b, 0), e.translate(0, c));
		this.zooming && (a.hideXScrollbar && (b = NaN), a.hideYScrollbar && (c = NaN), this.updateSelectionSize(b, c));
		this.fireMoved();
		a.mouseIsOver || this.zooming || this.hideCursor()
	},
	fireMoved: function() {
		var a = this.chart,
		b = {
			type: "moved",
			target: this
		};
		b.chart = a;
		b.zooming = this.zooming;
		b.x = a.mouseX - this.x;
		b.y = a.mouseY - this.y;
		this.fire("moved", b)
	},
	updateSelectionSize: function(a, b) {
		AmCharts.remove(this.selection);
		var c = this.selectionPosX,
		d = this.selectionPosY,
		e = 0,
		f = 0,
		g = this.width,
		h = this.height;
		isNaN(a) || (c > a && (e = a, g = c - a), c < a && (e = c, g = a - c), c == a && (e = a, g = 0));
		isNaN(b) || (d > b && (f = b, h = d - b), d < b && (f = d, h = b - d), d == b && (f = b, h = 0));
		0 < g && 0 < h && (c = AmCharts.rect(this.container, g, h, this.cursorColor, this.selectionAlpha), c.translate(e + this.x, f + this.y), this.selection = c)
	},
	arrangeBalloons: function() {
		var a = this.valueBalloons,
		b = this.x,
		c = this.y,
		d = this.height + c;
		a.sort(this.compareY);
		var e;
		for (e = 0; e < a.length; e++) {
			var f = a[e].balloon;
			f.setBounds(b, c, b + this.width, d);
			f.prevX = this.prevX[e];
			f.prevY = this.prevY[e];
			f.prevTX = this.prevTX[e];
			f.prevTY = this.prevTY[e];
			f.draw();
			d = f.yPos - 3
		}
		this.arrangeBalloons2()
	},
	compareY: function(a, b) {
		return a.yy < b.yy ? 1 : -1
	},
	arrangeBalloons2: function() {
		var a = this.valueBalloons;
		a.reverse();
		var b, c = this.x,
		d, e, f = a.length;
		for (e = 0; e < f; e++) {
			var g = a[e].balloon;
			b = g.bottom;
			var h = g.bottom - g.yPos,
			k = f - e - 1;
			0 < e && b - h < d + 3 && (g.setBounds(c, d + 3, c + this.width, d + h + 3), g.prevX = this.prevX[k], g.prevY = this.prevY[k], g.prevTX = this.prevTX[k], g.prevTY = this.prevTY[k], g.draw());
			g.set && g.set.show();
			this.prevX[k] = g.prevX;
			this.prevY[k] = g.prevY;
			this.prevTX[k] = g.prevTX;
			this.prevTY[k] = g.prevTY;
			d = g.bottom
		}
	},
	showBullets: function() {
		AmCharts.remove(this.allBullets);
		var a = this.container,
		b = a.set();
		this.set.push(b);
		this.set.show();
		this.allBullets = b;
		var b = this.chart.graphs,
		c;
		for (c = 0; c < b.length; c++) {
			var d = b[c];
			if (!d.hidden && d.balloonText) {
				var e = this.data[this.index].axes[d.valueAxis.id].graphs[d.id],
				f = e.y;
				if (!isNaN(f)) {
					var g, h;
					g = e.x;
					this.rotate ? (h = f, f = g) : h = g;
					d = AmCharts.circle(a, this.bulletSize / 2, this.chart.getBalloonColor(d, e, !0), d.cursorBulletAlpha);
					d.translate(h, f);
					this.allBullets.push(d)
				}
			}
		}
	},
	destroy: function() {
		this.clear();
		AmCharts.remove(this.selection);
		this.selection = null;
		var a = this.categoryBalloon;
		a && a.destroy();
		this.destroyValueBalloons();
		AmCharts.remove(this.set)
	},
	clear: function() {
		clearInterval(this.interval)
	},
	destroyValueBalloons: function() {
		var a = this.valueBalloons;
		if (a) {
			var b;
			for (b = 0; b < a.length; b++) a[b].balloon.hide()
		}
	},
	zoom: function(a, b, c, d) {
		var e = this.chart;
		this.destroyValueBalloons();
		this.zooming = !1;
		var f;
		this.rotate ? this.selectionPosY = f = e.mouseY: this.selectionPosX = f = e.mouseX;
		this.start = a;
		this.end = b;
		this.startTime = c;
		this.endTime = d;
		this.zoomed = !0;
		var g = e.categoryAxis,
		e = this.rotate;
		f = this.width;
		var h = this.height;
		g.parseDates && !g.equalSpacing ? (a = d - c + g.minDuration(), a = e ? h / a: f / a) : a = e ? h / (b - a) : f / (b - a);
		this.stepWidth = a;
		this.tempVal = this.valueBalloonsEnabled;
		this.valueBalloonsEnabled = !1;
		this.setPosition();
		this.valueBalloonsEnabled = this.tempVal;
		this.hideCursor()
	},
	hideObj: function(a) {
		a && a.hide()
	},
	hideCursor: function(a) {
		void 0 === a && (a = !0);
		this.hideObj(this.set);
		this.hideObj(this.categoryBalloon);
		this.hideObj(this.line);
		this.hideObj(this.vLine);
		this.hideObj(this.hLine);
		this.hideObj(this.allBullets);
		this.destroyValueBalloons();
		this.selectWithoutZooming || AmCharts.remove(this.selection);
		this.previousIndex = NaN;
		a && this.fire("onHideCursor", {
			type: "onHideCursor",
			chart: this.chart,
			target: this
		});
		this.drawing || this.chart.setMouseCursor("auto");
		this.normalizeBulletSize()
	},
	setPosition: function(a, b) {
		void 0 === b && (b = !0);
		if ("cursor" == this.type) {
			if (AmCharts.ifArray(this.data)) {
				isNaN(a) && (a = this.getMousePosition());
				if ((a != this.previousMousePosition || !0 === this.zoomed || this.oneBalloonOnly) && !isNaN(a)) {
					var c = this.chart.categoryAxis.xToIndex(a);
					if (c != this.previousIndex || this.zoomed || "mouse" == this.cursorPosition || this.oneBalloonOnly) this.updateCursor(c, b),
					this.zoomed = !1
				}
				this.previousMousePosition = a
			}
		} else this.updateCrosshair()
	},
	normalizeBulletSize: function() {
		var a = this.resizedBullets;
		if (a) for (var b = 0; b < a.length; b++) {
			var c = a[b],
			d = c.bulletGraphics;
			d && d.translate(c.bx, c.by, 1)
		}
	},
	updateCursor: function(a, b) {
		var c = this.chart,
		d = c.mouseX - this.x,
		e = c.mouseY - this.y;
		this.drawingNow && (AmCharts.remove(this.drawingLine), this.drawingLine = AmCharts.line(this.container, [this.x + this.drawStartX, this.x + d], [this.y + this.drawStartY, this.y + e], this.cursorColor, 1, 1));
		if (this.enabled) {
			void 0 === b && (b = !0);
			this.index = a += this.adjustment;
			var f = c.categoryAxis,
			g = c.dx,
			h = c.dy,
			k = this.x,
			l = this.y,
			m = this.width,
			n = this.height,
			p = this.data[a];
			this.fireMoved();
			if (p) {
				var r = p.x[f.id],
				u = c.rotate,
				q = f.inside,
				t = this.stepWidth,
				v = this.categoryBalloon,
				s = this.firstTime,
				w = this.lastTime,
				A = this.cursorPosition,
				C = f.position,
				y = this.zooming,
				z = this.panning,
				J = c.graphs,
				I = f.axisThickness;
				if (c.mouseIsOver || y || z || this.forceShow) if (this.forceShow = !1, z) {
					var g = this.panClickPos,
					c = this.panClickEndTime,
					y = this.panClickStartTime,
					B = this.panClickEnd,
					k = this.panClickStart,
					d = (u ? g - e: g - d) / t;
					if (!f.parseDates || f.equalSpacing) d = Math.round(d);
					0 !== d && (g = {
						type: "zoomed",
						target: this
					},
					g.chart = this.chart, f.parseDates && !f.equalSpacing ? (c + d > w && (d = w - c), y + d < s && (d = s - y), g.start = y + d, g.end = c + d, this.fire(g.type, g)) : B + d >= this.data.length || 0 > k + d || (g.start = k + d, g.end = B + d, this.fire(g.type, g)))
				} else {
					"start" == A && (r -= f.cellWidth / 2);
					"mouse" == A && c.mouseIsOver && (r = u ? e - 2 : d - 2);
					if (u) {
						if (0 > r) if (y) r = 0;
						else {
							this.hideCursor();
							return
						}
						if (r > n + 1) if (y) r = n + 1;
						else {
							this.hideCursor();
							return
						}
					} else {
						if (0 > r) if (y) r = 0;
						else {
							this.hideCursor();
							return
						}
						if (r > m) if (y) r = m;
						else {
							this.hideCursor();
							return
						}
					}
					0 < this.cursorAlpha && (s = this.line, u ? (w = 0, t = r + h) : (w = r, t = 0), A = this.animationDuration, 0 < A && !this.zooming ? isNaN(this.previousX) ? s.translate(w, t) : (s.translate(this.previousX, this.previousY), s.animate({
						translate: w + "," + t
					},
					A, "easeOutSine")) : s.translate(w, t), this.previousX = w, this.previousY = t, s.show());
					this.linePos = u ? r + h: r;
					y && (u ? this.updateSelectionSize(NaN, r) : this.updateSelectionSize(r, NaN));
					t = !0;
					y && (t = !1);
					this.categoryBalloonEnabled && t ? (u ? (q && ("right" == C ? v.setBounds(k, l + h, k + m + g, l + r + h) : v.setBounds(k, l + h, k + m + g, l + r)), "right" == C ? q ? v.setPosition(k + m + g, l + r + h) : v.setPosition(k + m + g + I, l + r + h) : q ? v.setPosition(k, l + r) : v.setPosition(k - I, l + r)) : "top" == C ? q ? v.setPosition(k + r + g, l + h) : v.setPosition(k + r + g, l + h - I + 1) : q ? v.setPosition(k + r, l + n) : v.setPosition(k + r, l + n + I - 1), (s = this.categoryBalloonFunction) ? v.showBalloon(s(p.category)) : f.parseDates ? (f = AmCharts.formatDate(p.category, this.categoryBalloonDateFormat), -1 != f.indexOf("fff") && (f = AmCharts.formatMilliseconds(f, p.category)), v.showBalloon(f)) : v.showBalloon(AmCharts.fixNewLines(p.category))) : v.hide();
					J && this.bulletsEnabled && this.showBullets();
					if (this.oneBalloonOnly) {
						h = Infinity;
						for (w = 0; w < J.length; w++) f = J[w],
						f.showBalloon && !f.hidden && f.balloonText && (v = p.axes[f.valueAxis.id].graphs[f.id], s = v.y, isNaN(s) || (u ? Math.abs(d - s) < h && (h = Math.abs(d - s), B = f) : Math.abs(e - s) < h && (h = Math.abs(e - s), B = f)));
						this.mostCloseGraph && (B = this.mostCloseGraph)
					}
					if (a != this.previousIndex || B != this.previousMostCloseGraph) if (this.normalizeBulletSize(), this.destroyValueBalloons(), this.resizedBullets = [], J && this.valueBalloonsEnabled && t && c.balloon.enabled) {
						this.valueBalloons = h = [];
						for (w = 0; w < J.length; w++) if (f = J[w], (!this.oneBalloonOnly || f == B) && f.showBalloon && !f.hidden && f.balloonText && ("step" == f.type && "left" == f.stepDirection && (p = this.data[a + 1]), p)) {
							v = p.axes[f.valueAxis.id].graphs[f.id];
							s = v.y;
							if (this.showNextAvailable && isNaN(s) && a + 1 < this.data.length) for (r = a + 1; r < this.data.length; r++) if (q = this.data[r]) if (v = q.axes[f.valueAxis.id].graphs[f.id], s = v.y, !isNaN(s)) break;
							if (!isNaN(s)) {
								q = v.x;
								t = !0;
								if (u) {
									if (r = s, 0 > q || q > n) t = !1
								} else if (r = q, q = s, 0 > r || r > m + g + 1) t = !1;
								t && (1 != this.graphBulletSize && AmCharts.isModern && (t = v.bulletGraphics) && (t.getBBox(), t.translate(v.bx, v.by, this.graphBulletSize), this.resizedBullets.push(v)), t = f.valueBalloon, C = c.getBalloonColor(f, v), t.setBounds(k, l, k + m, l + n), t.pointerOrientation = "H", t.changeColor(C), void 0 !== f.balloonAlpha && (t.fillAlpha = f.balloonAlpha), void 0 !== f.balloonTextColor && (t.color = f.balloonTextColor), t.setPosition(r + k, q + l), r = c.formatString(f.balloonText, v, !0), (q = f.balloonFunction) && (r = q(v, f).toString()), "" !== r && (u ? t.showBalloon(r) : (t.text = r, t.show = !0)), !u && t.set && t.set.hide(), h.push({
									yy: s,
									balloon: t
								}))
							}
						}
						u || this.arrangeBalloons()
					}
					b ? (g = {
						type: "changed"
					},
					g.index = a, g.chart = this.chart, g.zooming = y, g.mostCloseGraph = B, g.position = u ? e: d, g.target = this, c.fire("changed", g), this.fire("changed", g), this.skipZoomDispatch = !1) : (this.skipZoomDispatch = !0, c.updateLegendValues(a));
					this.previousIndex = a;
					this.previousMostCloseGraph = B
				}
			}
		} else this.hideCursor()
	},
	enableDrawing: function(a) {
		this.enabled = !a;
		this.hideCursor();
		this.rolledOver = !1;
		this.drawing = a
	},
	isZooming: function(a) {
		a && a != this.zooming && this.handleMouseDown("fake");
		a || a == this.zooming || this.handleMouseUp()
	},
	handleMouseOut: function() {
		if (this.enabled) if (this.zooming) this.setPosition();
		else {
			this.index = void 0;
			var a = {
				type: "changed",
				index: void 0,
				target: this
			};
			a.chart = this.chart;
			this.fire("changed", a);
			this.hideCursor()
		}
	},
	handleReleaseOutside: function() {
		this.handleMouseUp()
	},
	handleMouseUp: function() {
		var a = this.chart,
		b = this.data,
		c;
		if (a) {
			var d = a.mouseX - this.x,
			e = a.mouseY - this.y;
			if (this.drawingNow) {
				this.drawingNow = !1;
				AmCharts.remove(this.drawingLine);
				c = this.drawStartX;
				var f = this.drawStartY;
				if (2 < Math.abs(c - d) || 2 < Math.abs(f - e)) c = {
					type: "draw",
					target: this,
					chart: a,
					initialX: c,
					initialY: f,
					finalX: d,
					finalY: e
				},
				this.fire(c.type, c)
			}
			if (this.enabled && 0 < b.length) {
				if (this.pan) this.rolledOver = !1;
				else if (this.zoomable && this.zooming) {
					c = this.selectWithoutZooming ? {
						type: "selected"
					}: {
						type: "zoomed"
					};
					c.target = this;
					c.chart = a;
					if ("cursor" == this.type) this.rotate ? this.selectionPosY = e: this.selectionPosX = e = d,
					2 > Math.abs(e - this.initialMouse) && this.fromIndex == this.index || (this.index < this.fromIndex ? (c.end = this.fromIndex, c.start = this.index) : (c.end = this.index, c.start = this.fromIndex), e = a.categoryAxis, e.parseDates && !e.equalSpacing && (c.start = b[c.start].time, c.end = a.getEndTime(b[c.end].time)), this.skipZoomDispatch || this.fire(c.type, c));
					else {
						var g = this.initialMouseX,
						h = this.initialMouseY;
						3 > Math.abs(d - g) && 3 > Math.abs(e - h) || (b = Math.min(g, d), f = Math.min(h, e), d = Math.abs(g - d), e = Math.abs(h - e), a.hideXScrollbar && (b = 0, d = this.width), a.hideYScrollbar && (f = 0, e = this.height), c.selectionHeight = e, c.selectionWidth = d, c.selectionY = f, c.selectionX = b, this.skipZoomDispatch || this.fire(c.type, c))
					}
					this.selectWithoutZooming || AmCharts.remove(this.selection)
				}
				this.panning = this.zooming = this.skipZoomDispatch = !1
			}
		}
	},
	showCursorAt: function(a) {
		var b = this.chart.categoryAxis;
		a = b.parseDates ? b.dateToCoordinate(a) : b.categoryToCoordinate(a);
		this.previousMousePosition = NaN;
		this.forceShow = !0;
		this.setPosition(a, !1)
	},
	handleMouseDown: function(a) {
		if (this.zoomable || this.pan || this.drawing) {
			var b = this.rotate,
			c = this.chart,
			d = c.mouseX - this.x,
			e = c.mouseY - this.y;
			if (0 < d && d < this.width && 0 < e && e < this.height || "fake" == a) this.setPosition(),
			this.selectWithoutZooming && AmCharts.remove(this.selection),
			this.drawing ? (this.drawStartY = e, this.drawStartX = d, this.drawingNow = !0) : this.pan ? (this.zoomable = !1, c.setMouseCursor("move"), this.panning = !0, this.panClickPos = b ? e: d, this.panClickStart = this.start, this.panClickEnd = this.end, this.panClickStartTime = this.startTime, this.panClickEndTime = this.endTime) : this.zoomable && ("cursor" == this.type ? (this.fromIndex = this.index, b ? (this.initialMouse = e, this.selectionPosY = this.linePos) : (this.initialMouse = d, this.selectionPosX = this.linePos)) : (this.initialMouseX = d, this.initialMouseY = e, this.selectionPosX = d, this.selectionPosY = e), this.zooming = !0)
		}
	}
});
AmCharts.SimpleChartScrollbar = AmCharts.Class({
	construct: function(a) {
		this.createEvents("zoomed");
		this.backgroundColor = "#D4D4D4";
		this.backgroundAlpha = 1;
		this.selectedBackgroundColor = "#EFEFEF";
		this.scrollDuration = this.selectedBackgroundAlpha = 1;
		this.resizeEnabled = !0;
		this.hideResizeGrips = !1;
		this.scrollbarHeight = 20;
		this.updateOnReleaseOnly = !1;
		9 > document.documentMode && (this.updateOnReleaseOnly = !0);
		this.dragIconWidth = 18;
		this.dragIconHeight = 25;
		AmCharts.applyTheme(this, a, "SimpleChartScrollbar")
	},
	draw: function() {
		var a = this;
		a.destroy();
		a.interval = setInterval(function() {
			a.updateScrollbar.call(a)
		},
		40);
		var b = a.chart.container,
		c = a.rotate,
		d = a.chart,
		e = b.set();
		a.set = e;
		d.scrollbarsSet.push(e);
		var f, g;
		c ? (f = a.scrollbarHeight, g = d.plotAreaHeight) : (g = a.scrollbarHeight, f = d.plotAreaWidth);
		a.width = f;
		if ((a.height = g) && f) {
			var h = AmCharts.rect(b, f, g, a.backgroundColor, a.backgroundAlpha, 1, a.backgroundColor, a.backgroundAlpha);
			a.bg = h;
			e.push(h);
			h = AmCharts.rect(b, f, g, "#000", 0.005);
			e.push(h);
			a.invisibleBg = h;
			h.click(function() {
				a.handleBgClick()
			}).mouseover(function() {
				a.handleMouseOver()
			}).mouseout(function() {
				a.handleMouseOut()
			}).touchend(function() {
				a.handleBgClick()
			});
			h = AmCharts.rect(b, f, g, a.selectedBackgroundColor, a.selectedBackgroundAlpha);
			a.selectedBG = h;
			e.push(h);
			f = AmCharts.rect(b, f, g, "#000", 0.005);
			a.dragger = f;
			e.push(f);
			f.mousedown(function(b) {
				a.handleDragStart(b)
			}).mouseup(function() {
				a.handleDragStop()
			}).mouseover(function() {
				a.handleDraggerOver()
			}).mouseout(function() {
				a.handleMouseOut()
			}).touchstart(function(b) {
				a.handleDragStart(b)
			}).touchend(function() {
				a.handleDragStop()
			});
			f = d.pathToImages;
			c ? (h = f + "dragIconH.gif", f = a.dragIconWidth, c = a.dragIconHeight) : (h = f + "dragIcon.gif", c = a.dragIconWidth, f = a.dragIconHeight);
			g = b.image(h, 0, 0, c, f);
			var h = b.image(h, 0, 0, c, f),
			k = 10,
			l = 20;
			d.panEventsEnabled && (k = 25, l = a.scrollbarHeight);
			var m = AmCharts.rect(b, k, l, "#000", 0.005),
			n = AmCharts.rect(b, k, l, "#000", 0.005);
			n.translate( - (k - c) / 2, -(l - f) / 2);
			m.translate( - (k - c) / 2, -(l - f) / 2);
			c = b.set([g, n]);
			b = b.set([h, m]);
			a.iconLeft = c;
			a.iconRight = b;
			c.mousedown(function() {
				a.leftDragStart()
			}).mouseup(function() {
				a.leftDragStop()
			}).mouseover(function() {
				a.iconRollOver()
			}).mouseout(function() {
				a.iconRollOut()
			}).touchstart(function(b) {
				a.leftDragStart()
			}).touchend(function() {
				a.leftDragStop()
			});
			b.mousedown(function() {
				a.rightDragStart()
			}).mouseup(function() {
				a.rightDragStop()
			}).mouseover(function() {
				a.iconRollOver()
			}).mouseout(function() {
				a.iconRollOut()
			}).touchstart(function(b) {
				a.rightDragStart()
			}).touchend(function() {
				a.rightDragStop()
			});
			AmCharts.ifArray(d.chartData) ? e.show() : e.hide();
			a.hideDragIcons();
			a.clipDragger(!1)
		}
		e.translate(a.x, a.y)
	},
	updateScrollbarSize: function(a, b) {
		var c = this.dragger,
		d, e, f, g;
		this.rotate ? (d = 0, e = a, f = this.width + 1, g = b - a, c.setAttr("height", b - a), c.setAttr("y", e)) : (d = a, e = 0, f = b - a, g = this.height + 1, c.setAttr("width", b - a), c.setAttr("x", d));
		this.clipAndUpdate(d, e, f, g)
	},
	updateScrollbar: function() {
		var a, b = !1,
		c, d, e = this.x,
		f = this.y,
		g = this.dragger,
		h = this.getDBox();
		c = h.x + e;
		d = h.y + f;
		var k = h.width,
		h = h.height,
		l = this.rotate,
		m = this.chart,
		n = this.width,
		p = this.height,
		r = m.mouseX,
		u = m.mouseY;
		a = this.initialMouse;
		m.mouseIsOver && (this.dragging && (m = this.initialCoord, l ? (a = m + (u - a), 0 > a && (a = 0), m = p - h, a > m && (a = m), g.setAttr("y", a)) : (a = m + (r - a), 0 > a && (a = 0), m = n - k, a > m && (a = m), g.setAttr("x", a))), this.resizingRight && (l ? (a = u - d, a + d > p + f && (a = p - d + f), 0 > a ? (this.resizingRight = !1, b = this.resizingLeft = !0) : (0 === a && (a = 0.1), g.setAttr("height", a))) : (a = r - c, a + c > n + e && (a = n - c + e), 0 > a ? (this.resizingRight = !1, b = this.resizingLeft = !0) : (0 === a && (a = 0.1), g.setAttr("width", a)))), this.resizingLeft && (l ? (c = d, d = u, d < f && (d = f), d > p + f && (d = p + f), a = !0 === b ? c - d: h + c - d, 0 > a ? (this.resizingRight = !0, this.resizingLeft = !1, g.setAttr("y", c + h - f)) : (0 === a && (a = 0.1), g.setAttr("y", d - f), g.setAttr("height", a))) : (d = r, d < e && (d = e), d > n + e && (d = n + e), a = !0 === b ? c - d: k + c - d, 0 > a ? (this.resizingRight = !0, this.resizingLeft = !1, g.setAttr("x", c + k - e)) : (0 === a && (a = 0.1), g.setAttr("x", d - e), g.setAttr("width", a)))), this.clipDragger(!0))
	},
	clipDragger: function(a) {
		var b = this.getDBox();
		if (b) {
			var c = b.x,
			d = b.y,
			e = b.width,
			b = b.height,
			f = !1;
			if (this.rotate) {
				if (c = 0, e = this.width + 1, this.clipY != d || this.clipH != b) f = !0
			} else if (d = 0, b = this.height + 1, this.clipX != c || this.clipW != e) f = !0;
			f && (this.clipAndUpdate(c, d, e, b), a && (this.updateOnReleaseOnly || this.dispatchScrollbarEvent()))
		}
	},
	maskGraphs: function() {},
	clipAndUpdate: function(a, b, c, d) {
		this.clipX = a;
		this.clipY = b;
		this.clipW = c;
		this.clipH = d;
		this.selectedBG.clipRect(a, b, c, d);
		this.updateDragIconPositions();
		this.maskGraphs(a, b, c, d)
	},
	dispatchScrollbarEvent: function() {
		if (this.skipEvent) this.skipEvent = !1;
		else {
			var a = this.chart;
			a.hideBalloon();
			var b = this.getDBox(),
			c = b.x,
			d = b.y,
			e = b.width,
			b = b.height;
			this.rotate ? (c = d, e = this.height / b) : e = this.width / e;
			a = {
				type: "zoomed",
				position: c,
				chart: a,
				target: this,
				multiplier: e
			};
			this.fire(a.type, a)
		}
	},
	updateDragIconPositions: function() {
		var a = this.getDBox(),
		b = a.x,
		c = a.y,
		d = this.iconLeft,
		e = this.iconRight,
		f,
		g,
		h = this.scrollbarHeight;
		this.rotate ? (f = this.dragIconWidth, g = this.dragIconHeight, d.translate(this.x + (h - g) / 2, this.y + c - f / 2), e.translate(this.x + (h - g) / 2, this.y + c + a.height - f / 2)) : (f = this.dragIconHeight, g = this.dragIconWidth, d.translate(this.x + b - g / 2, this.y + (h - f) / 2), e.translate(this.x + b - g / 2 + a.width, this.y + (h - f) / 2))
	},
	showDragIcons: function() {
		this.resizeEnabled && (this.iconLeft.show(), this.iconRight.show())
	},
	hideDragIcons: function() {
		this.resizingLeft || this.resizingRight || this.dragging || (this.hideResizeGrips && (this.iconLeft.hide(), this.iconRight.hide()), this.removeCursors())
	},
	removeCursors: function() {
		this.chart.setMouseCursor("auto")
	},
	relativeZoom: function(a, b) {
		this.dragger.stop();
		this.multiplier = a;
		this.position = b;
		this.updateScrollbarSize(b, this.rotate ? b + this.height / a: b + this.width / a)
	},
	destroy: function() {
		this.clear();
		AmCharts.remove(this.set);
		AmCharts.remove(this.iconRight);
		AmCharts.remove(this.iconLeft)
	},
	clear: function() {
		clearInterval(this.interval)
	},
	handleDragStart: function() {
		var a = this.chart;
		this.dragger.stop();
		this.removeCursors();
		this.dragging = !0;
		var b = this.getDBox();
		this.rotate ? (this.initialCoord = b.y, this.initialMouse = a.mouseY) : (this.initialCoord = b.x, this.initialMouse = a.mouseX)
	},
	handleDragStop: function() {
		this.updateOnReleaseOnly && (this.updateScrollbar(), this.skipEvent = !1, this.dispatchScrollbarEvent());
		this.dragging = !1;
		this.mouseIsOver && this.removeCursors();
		this.updateScrollbar()
	},
	handleDraggerOver: function() {
		this.handleMouseOver()
	},
	leftDragStart: function() {
		this.dragger.stop();
		this.resizingLeft = !0
	},
	leftDragStop: function() {
		this.resizingLeft = !1;
		this.mouseIsOver || this.removeCursors();
		this.updateOnRelease()
	},
	rightDragStart: function() {
		this.dragger.stop();
		this.resizingRight = !0
	},
	rightDragStop: function() {
		this.resizingRight = !1;
		this.mouseIsOver || this.removeCursors();
		this.updateOnRelease()
	},
	iconRollOut: function() {
		this.removeCursors()
	},
	iconRollOver: function() {
		this.rotate ? this.chart.setMouseCursor("n-resize") : this.chart.setMouseCursor("e-resize");
		this.handleMouseOver()
	},
	getDBox: function() {
		if (this.dragger) return this.dragger.getBBox()
	},
	handleBgClick: function() {
		if (!this.resizingRight && !this.resizingLeft) {
			this.zooming = !0;
			var a, b, c = this.scrollDuration,
			d = this.dragger;
			a = this.getDBox();
			var e = a.height,
			f = a.width;
			b = this.chart;
			var g = this.y,
			h = this.x,
			k = this.rotate;
			k ? (a = "y", b = b.mouseY - e / 2 - g, b = AmCharts.fitToBounds(b, 0, this.height - e)) : (a = "x", b = b.mouseX - f / 2 - h, b = AmCharts.fitToBounds(b, 0, this.width - f));
			this.updateOnReleaseOnly ? (this.skipEvent = !1, d.setAttr(a, b), this.dispatchScrollbarEvent(), this.clipDragger()) : (b = Math.round(b), k ? d.animate({
				y: b
			},
			c, ">") : d.animate({
				x: b
			},
			c, ">"))
		}
	},
	updateOnRelease: function() {
		this.updateOnReleaseOnly && (this.updateScrollbar(), this.skipEvent = !1, this.dispatchScrollbarEvent())
	},
	handleReleaseOutside: function() {
		if (this.set) {
			if (this.resizingLeft || this.resizingRight || this.dragging) this.updateOnRelease(),
			this.removeCursors();
			this.mouseIsOver = this.dragging = this.resizingRight = this.resizingLeft = !1;
			this.hideDragIcons();
			this.updateScrollbar()
		}
	},
	handleMouseOver: function() {
		this.mouseIsOver = !0;
		this.showDragIcons()
	},
	handleMouseOut: function() {
		this.mouseIsOver = !1;
		this.hideDragIcons()
	}
});
AmCharts.ChartScrollbar = AmCharts.Class({
	inherits: AmCharts.SimpleChartScrollbar,
	construct: function(a) {
		this.cname = "ChartScrollbar";
		AmCharts.ChartScrollbar.base.construct.call(this, a);
		this.graphLineColor = "#BBBBBB";
		this.graphLineAlpha = 0;
		this.graphFillColor = "#BBBBBB";
		this.graphFillAlpha = 1;
		this.selectedGraphLineColor = "#888888";
		this.selectedGraphLineAlpha = 0;
		this.selectedGraphFillColor = "#888888";
		this.selectedGraphFillAlpha = 1;
		this.gridCount = 0;
		this.gridColor = "#FFFFFF";
		this.gridAlpha = 0.7;
		this.skipEvent = this.autoGridCount = !1;
		this.color = "#FFFFFF";
		this.scrollbarCreated = !1;
		AmCharts.applyTheme(this, a, this.cname)
	},
	init: function() {
		var a = this.categoryAxis,
		b = this.chart;
		a || (this.categoryAxis = a = new AmCharts.CategoryAxis);
		a.chart = b;
		a.id = "scrollbar";
		a.dateFormats = b.categoryAxis.dateFormats;
		a.markPeriodChange = b.categoryAxis.markPeriodChange;
		a.boldPeriodBeginning = b.categoryAxis.boldPeriodBeginning;
		a.axisItemRenderer = AmCharts.RecItem;
		a.axisRenderer = AmCharts.RecAxis;
		a.guideFillRenderer = AmCharts.RecFill;
		a.inside = !0;
		a.fontSize = this.fontSize;
		a.tickLength = 0;
		a.axisAlpha = 0;
		AmCharts.isString(this.graph) && (this.graph = AmCharts.getObjById(b.graphs, this.graph));
		if (a = this.graph) {
			var c = this.valueAxis;
			c || (this.valueAxis = c = new AmCharts.ValueAxis, c.visible = !1, c.scrollbar = !0, c.axisItemRenderer = AmCharts.RecItem, c.axisRenderer = AmCharts.RecAxis, c.guideFillRenderer = AmCharts.RecFill, c.labelsEnabled = !1, c.chart = b);
			b = this.unselectedGraph;
			b || (b = new AmCharts.AmGraph, b.scrollbar = !0, this.unselectedGraph = b, b.negativeBase = a.negativeBase, b.noStepRisers = a.noStepRisers);
			b = this.selectedGraph;
			b || (b = new AmCharts.AmGraph, b.scrollbar = !0, this.selectedGraph = b, b.negativeBase = a.negativeBase, b.noStepRisers = a.noStepRisers)
		}
		this.scrollbarCreated = !0
	},
	draw: function() {
		var a = this;
		AmCharts.ChartScrollbar.base.draw.call(a);
		a.scrollbarCreated || a.init();
		var b = a.chart,
		c = b.chartData,
		d = a.categoryAxis,
		e = a.rotate,
		f = a.x,
		g = a.y,
		h = a.width,
		k = a.height,
		l = b.categoryAxis,
		m = a.set;
		d.setOrientation(!e);
		d.parseDates = l.parseDates;
		d.rotate = e;
		d.equalSpacing = l.equalSpacing;
		d.minPeriod = l.minPeriod;
		d.startOnAxis = l.startOnAxis;
		d.viW = h;
		d.viH = k;
		d.width = h;
		d.height = k;
		d.gridCount = a.gridCount;
		d.gridColor = a.gridColor;
		d.gridAlpha = a.gridAlpha;
		d.color = a.color;
		d.tickLength = 0;
		d.axisAlpha = 0;
		d.autoGridCount = a.autoGridCount;
		d.parseDates && !d.equalSpacing && d.timeZoom(b.firstTime, b.lastTime);
		d.zoom(0, c.length - 1);
		if (l = a.graph) {
			var n = a.valueAxis,
			p = l.valueAxis;
			n.id = p.id;
			n.rotate = e;
			n.setOrientation(e);
			n.width = h;
			n.height = k;
			n.viW = h;
			n.viH = k;
			n.dataProvider = c;
			n.reversed = p.reversed;
			n.logarithmic = p.logarithmic;
			n.gridAlpha = 0;
			n.axisAlpha = 0;
			m.push(n.set);
			e ? (n.y = g, n.x = 0) : (n.x = f, n.y = 0);
			var f = Infinity,
			g = -Infinity,
			r;
			for (r = 0; r < c.length; r++) {
				var u = c[r].axes[p.id].graphs[l.id].values,
				q;
				for (q in u) if (u.hasOwnProperty(q) && "percents" != q && "total" != q) {
					var t = u[q];
					t < f && (f = t);
					t > g && (g = t)
				}
			}
			Infinity != f && (n.minimum = f); - Infinity != g && (n.maximum = g + 0.1 * (g - f));
			f == g && (n.minimum -= 1, n.maximum += 1);
			void 0 !== a.minimum && (n.minimum = a.minimum);
			void 0 !== a.maximum && (n.maximum = a.maximum);
			n.zoom(0, c.length - 1);
			q = a.unselectedGraph;
			q.id = l.id;
			q.rotate = e;
			q.chart = b;
			q.data = c;
			q.valueAxis = n;
			q.chart = l.chart;
			q.categoryAxis = a.categoryAxis;
			q.periodSpan = l.periodSpan;
			q.valueField = l.valueField;
			q.openField = l.openField;
			q.closeField = l.closeField;
			q.highField = l.highField;
			q.lowField = l.lowField;
			q.lineAlpha = a.graphLineAlpha;
			q.lineColorR = a.graphLineColor;
			q.fillAlphas = a.graphFillAlpha;
			q.fillColorsR = a.graphFillColor;
			q.connect = l.connect;
			q.hidden = l.hidden;
			q.width = h;
			q.height = k;
			q.pointPosition = l.pointPosition;
			q.stepDirection = l.stepDirection;
			q.periodSpan = l.periodSpan;
			p = a.selectedGraph;
			p.id = l.id;
			p.rotate = e;
			p.chart = b;
			p.data = c;
			p.valueAxis = n;
			p.chart = l.chart;
			p.categoryAxis = d;
			p.periodSpan = l.periodSpan;
			p.valueField = l.valueField;
			p.openField = l.openField;
			p.closeField = l.closeField;
			p.highField = l.highField;
			p.lowField = l.lowField;
			p.lineAlpha = a.selectedGraphLineAlpha;
			p.lineColorR = a.selectedGraphLineColor;
			p.fillAlphas = a.selectedGraphFillAlpha;
			p.fillColorsR = a.selectedGraphFillColor;
			p.connect = l.connect;
			p.hidden = l.hidden;
			p.width = h;
			p.height = k;
			p.pointPosition = l.pointPosition;
			p.stepDirection = l.stepDirection;
			p.periodSpan = l.periodSpan;
			b = a.graphType;
			b || (b = l.type);
			q.type = b;
			p.type = b;
			c = c.length - 1;
			q.zoom(0, c);
			p.zoom(0, c);
			p.set.click(function() {
				a.handleBackgroundClick()
			}).mouseover(function() {
				a.handleMouseOver()
			}).mouseout(function() {
				a.handleMouseOut()
			});
			q.set.click(function() {
				a.handleBackgroundClick()
			}).mouseover(function() {
				a.handleMouseOver()
			}).mouseout(function() {
				a.handleMouseOut()
			});
			m.push(q.set);
			m.push(p.set)
		}
		m.push(d.set);
		m.push(d.labelsSet);
		a.bg.toBack();
		a.invisibleBg.toFront();
		a.dragger.toFront();
		a.iconLeft.toFront();
		a.iconRight.toFront()
	},
	timeZoom: function(a, b, c) {
		this.startTime = a;
		this.endTime = b;
		this.timeDifference = b - a;
		this.skipEvent = !AmCharts.toBoolean(c);
		this.zoomScrollbar();
		this.skipEvent || this.dispatchScrollbarEvent()
	},
	zoom: function(a, b) {
		this.start = a;
		this.end = b;
		this.skipEvent = !0;
		this.zoomScrollbar()
	},
	dispatchScrollbarEvent: function() {
		if (this.skipEvent) this.skipEvent = !1;
		else {
			var a = this.chart.chartData,
			b, c, d = this.dragger.getBBox();
			b = d.x;
			c = d.y;
			var e = d.width,
			f = d.height,
			d = this.chart;
			this.rotate ? (b = c, c = f) : c = e;
			e = {
				type: "zoomed",
				target: this
			};
			e.chart = d;
			var f = this.categoryAxis,
			g = this.stepWidth;
			if (f.parseDates && !f.equalSpacing) {
				if (a = d.firstTime, f.minDuration(), d = Math.round(b / g) + a, a = this.dragging ? d + this.timeDifference: Math.round((b + c) / g) + a, d > a && (d = a), d != this.startTime || a != this.endTime) this.startTime = d,
				this.endTime = a,
				e.start = d,
				e.end = a,
				e.startDate = new Date(d),
				e.endDate = new Date(a),
				this.fire(e.type, e)
			} else if (f.startOnAxis || (b += g / 2), c -= this.stepWidth / 2, d = f.xToIndex(b), b = f.xToIndex(b + c), d != this.start || this.end != b) f.startOnAxis && (this.resizingRight && d == b && b++, this.resizingLeft && d == b && (0 < d ? d--:b = 1)),
			this.start = d,
			this.end = this.dragging ? this.start + this.difference: b,
			e.start = this.start,
			e.end = this.end,
			f.parseDates && (a[this.start] && (e.startDate = new Date(a[this.start].time)), a[this.end] && (e.endDate = new Date(a[this.end].time))),
			this.fire(e.type, e)
		}
	},
	zoomScrollbar: function() {
		var a, b;
		a = this.chart;
		var c = a.chartData,
		d = this.categoryAxis;
		d.parseDates && !d.equalSpacing ? (c = d.stepWidth, d = a.firstTime, a = c * (this.startTime - d), b = c * (this.endTime - d)) : (a = c[this.start].x[d.id], b = c[this.end].x[d.id], c = d.stepWidth, d.startOnAxis || (d = c / 2, a -= d, b += d));
		this.stepWidth = c;
		this.updateScrollbarSize(a, b)
	},
	maskGraphs: function(a, b, c, d) {
		var e = this.selectedGraph;
		e && e.set.clipRect(a, b, c, d)
	},
	handleDragStart: function() {
		AmCharts.ChartScrollbar.base.handleDragStart.call(this);
		this.difference = this.end - this.start;
		this.timeDifference = this.endTime - this.startTime;
		0 > this.timeDifference && (this.timeDifference = 0)
	},
	handleBackgroundClick: function() {
		AmCharts.ChartScrollbar.base.handleBackgroundClick.call(this);
		this.dragging || (this.difference = this.end - this.start, this.timeDifference = this.endTime - this.startTime, 0 > this.timeDifference && (this.timeDifference = 0))
	}
});
AmCharts.AmBalloon = AmCharts.Class({
	construct: function(a) {
		this.cname = "AmBalloon";
		this.enabled = !0;
		this.fillColor = "#FFFFFF";
		this.fillAlpha = 0.8;
		this.borderThickness = 2;
		this.borderColor = "#FFFFFF";
		this.borderAlpha = 1;
		this.cornerRadius = 0;
		this.maximumWidth = 220;
		this.horizontalPadding = 8;
		this.verticalPadding = 4;
		this.pointerWidth = 6;
		this.pointerOrientation = "V";
		this.color = "#000000";
		this.adjustBorderColor = !0;
		this.show = this.follow = this.showBullet = !1;
		this.bulletSize = 3;
		this.shadowAlpha = 0.4;
		this.shadowColor = "#000000";
		this.fadeOutDuration = this.animationDuration = 0.3;
		this.fixedPosition = !1;
		this.offsetY = 6;
		this.offsetX = 1;
		AmCharts.isModern || (this.offsetY *= 1.5);
		AmCharts.applyTheme(this, a, this.cname)
	},
	draw: function() {
		var a = this.pointToX,
		b = this.pointToY;
		this.deltaSignX = this.deltaSignY = 1;
		var c = this.chart;
		AmCharts.VML && (this.fadeOutDuration = 0);
		this.xAnim && c.stopAnim(this.xAnim);
		this.yAnim && c.stopAnim(this.yAnim);
		if (!isNaN(a)) {
			var d = this.follow,
			e = c.container,
			f = this.set;
			AmCharts.remove(f);
			this.removeDiv();
			this.set = f = e.set();
			c.balloonsSet.push(f);
			if (this.show) {
				var g = this.l,
				h = this.t,
				k = this.r,
				l = this.b,
				m = this.balloonColor,
				n = this.fillColor,
				p = this.borderColor,
				r = n;
				void 0 != m && (this.adjustBorderColor ? r = p = m: n = m);
				var u = this.horizontalPadding,
				q = this.verticalPadding,
				t = this.pointerWidth,
				v = this.pointerOrientation,
				s = this.cornerRadius,
				w = c.fontFamily,
				A = this.fontSize;
				void 0 == A && (A = c.fontSize);
				var m = document.createElement("div"),
				C = m.style;
				C.position = "absolute";
				m.innerHTML = '<div style="max-width:' + this.maxWidth + "px; font-size:" + A + "px; color:" + this.color + "; font-family:" + w + '">' + this.text + "</div>";
				c.chartDiv.appendChild(m);
				this.textDiv = m;
				A = m.offsetWidth;
				w = m.offsetHeight;
				m.clientHeight && (A = m.clientWidth, w = m.clientHeight);
				var w = w + 2 * q,
				y = A + 2 * u;
				window.opera && (w += 2);
				var z, J = !1;
				z = this.offsetY;
				c.handDrawn && (z += c.handDrawScatter + 2);
				"H" != v ? (A = a - y / 2, b < h + w + 10 && "down" != v ? (J = !0, d && (b += z), z = b + t, this.deltaSignY = -1) : (d && (b -= z), z = b - w - t, this.deltaSignY = 1)) : (2 * t > w && (t = w / 2), z = b - w / 2, a < g + (k - g) / 2 ? (A = a + t, this.deltaSignX = -1) : (A = a - y - t, this.deltaSignX = 1));
				z + w >= l && (z = l - w);
				z < h && (z = h);
				A < g && (A = g);
				A + y > k && (A = k - y);
				var h = z + q,
				l = A + u,
				q = this.shadowAlpha,
				I = this.shadowColor,
				u = this.borderThickness,
				B = this.bulletSize,
				Z;
				0 < s || 0 === t ? (0 < q && (a = AmCharts.rect(e, y, w, n, 0, u + 1, I, q, this.cornerRadius), AmCharts.isModern ? a.translate(1, 1) : a.translate(4, 4), f.push(a)), n = AmCharts.rect(e, y, w, n, this.fillAlpha, u, p, this.borderAlpha, this.cornerRadius), this.showBullet && (Z = AmCharts.circle(e, B, r, this.fillAlpha), f.push(Z))) : (r = [], s = [], "H" != v ? (g = a - A, g > y - t && (g = y - t), g < t && (g = t), r = [0, g - t, a - A, g + t, y, y, 0, 0], s = J ? [0, 0, b - z, 0, 0, w, w, 0] : [w, w, b - z, w, w, 0, 0, w]) : (r = b - z, r > w - t && (r = w - t), r < t && (r = t), s = [0, r - t, b - z, r + t, w, w, 0, 0], r = a < g + (k - g) / 2 ? [0, 0, A < a ? 0 : a - A, 0, 0, y, y, 0] : [y, y, A + y > a ? y: a - A, y, y, 0, 0, y]), 0 < q && (a = AmCharts.polygon(e, r, s, n, 0, u, I, q), a.translate(1, 1), f.push(a)), n = AmCharts.polygon(e, r, s, n, this.fillAlpha, u, p, this.borderAlpha));
				this.bg = n;
				f.push(n);
				n.toFront();
				e = 1 * this.deltaSignX;
				C.left = l + "px";
				C.top = h + "px";
				f.translate(A - e, z);
				n = n.getBBox();
				this.bottom = z + w + 1;
				this.yPos = n.y + z;
				Z && Z.translate(this.pointToX - A + e, b - z);
				b = this.animationDuration;
				0 < this.animationDuration && !d && !isNaN(this.prevX) && (f.translate(this.prevX, this.prevY), f.animate({
					translate: A - e + "," + z
				},
				b, "easeOutSine"), m && (C.left = this.prevTX + "px", C.top = this.prevTY + "px", this.xAnim = c.animate({
					node: m
				},
				"left", this.prevTX, l, b, "easeOutSine", "px"), this.yAnim = c.animate({
					node: m
				},
				"top", this.prevTY, h, b, "easeOutSine", "px")));
				this.prevX = A - e;
				this.prevY = z;
				this.prevTX = l;
				this.prevTY = h
			}
		}
	},
	followMouse: function() {
		if (this.follow && this.show) {
			var a = this.chart.mouseX - this.offsetX * this.deltaSignX,
			b = this.chart.mouseY;
			this.pointToX = a;
			this.pointToY = b;
			if (a != this.previousX || b != this.previousY) if (this.previousX = a, this.previousY = b, 0 === this.cornerRadius) this.draw();
			else {
				var c = this.set;
				if (c) {
					var d = c.getBBox(),
					a = a - d.width / 2,
					e = b - d.height - 10;
					a < this.l && (a = this.l);
					a > this.r - d.width && (a = this.r - d.width);
					e < this.t && (e = b + 10);
					c.translate(a, e);
					b = this.textDiv.style;
					b.left = a + this.horizontalPadding + "px";
					b.top = e + this.verticalPadding + "px"
				}
			}
		}
	},
	changeColor: function(a) {
		this.balloonColor = a
	},
	setBounds: function(a, b, c, d) {
		this.l = a;
		this.t = b;
		this.r = c;
		this.b = d;
		this.destroyTO && clearTimeout(this.destroyTO)
	},
	showBalloon: function(a) {
		this.text = a;
		this.show = !0;
		this.destroyTO && clearTimeout(this.destroyTO);
		a = this.chart;
		this.fadeAnim1 && a.stopAnim(this.fadeAnim1);
		this.fadeAnim2 && a.stopAnim(this.fadeAnim2);
		this.draw()
	},
	hide: function() {
		var a = this,
		b = a.fadeOutDuration,
		c = a.chart;
		if (0 < b) {
			a.destroyTO = setTimeout(function() {
				a.destroy.call(a)
			},
			1E3 * b);
			a.follow = !1;
			a.show = !1;
			var d = a.set;
			d && (d.setAttr("opacity", a.fillAlpha), a.fadeAnim1 = d.animate({
				opacity: 0
			},
			b, "easeInSine"));
			a.textDiv && (a.fadeAnim2 = c.animate({
				node: a.textDiv
			},
			"opacity", 1, 0, b, "easeInSine", ""))
		} else a.show = !1,
		a.follow = !1,
		a.destroy()
	},
	setPosition: function(a, b, c) {
		this.pointToX = a;
		this.pointToY = b;
		c && (a == this.previousX && b == this.previousY || this.draw());
		this.previousX = a;
		this.previousY = b
	},
	followCursor: function(a) {
		var b = this; (b.follow = a) ? (b.pShowBullet = b.showBullet, b.showBullet = !1) : void 0 !== b.pShowBullet && (b.showBullet = b.pShowBullet);
		clearInterval(b.interval);
		var c = b.chart.mouseX,
		d = b.chart.mouseY; ! isNaN(c) && a && (b.pointToX = c - b.offsetX * b.deltaSignX, b.pointToY = d, b.followMouse(), b.interval = setInterval(function() {
			b.followMouse.call(b)
		},
		40))
	},
	removeDiv: function() {
		if (this.textDiv) {
			var a = this.textDiv.parentNode;
			a && a.removeChild(this.textDiv)
		}
	},
	destroy: function() {
		clearInterval(this.interval);
		AmCharts.remove(this.set);
		this.removeDiv();
		this.set = null
	}
});
AmCharts.AmCoordinateChart = AmCharts.Class({
	inherits: AmCharts.AmChart,
	construct: function(a) {
		AmCharts.AmCoordinateChart.base.construct.call(this, a);
		this.theme = a;
		this.createEvents("rollOverGraphItem", "rollOutGraphItem", "clickGraphItem", "doubleClickGraphItem", "rightClickGraphItem", "clickGraph");
		this.startAlpha = 1;
		this.startDuration = 0;
		this.startEffect = "elastic";
		this.sequencedAnimation = !0;
		this.colors = "#FF6600 #FCD202 #B0DE09 #0D8ECF #2A0CD0 #CD0D74 #CC0000 #00CC00 #0000CC #DDDDDD #999999 #333333 #990000".split(" ");
		this.balloonDateFormat = "MMM DD, YYYY";
		this.valueAxes = [];
		this.graphs = [];
		this.gridAboveGraphs = !1;
		AmCharts.applyTheme(this, a, "AmCoordinateChart")
	},
	initChart: function() {
		AmCharts.AmCoordinateChart.base.initChart.call(this);
		var a = this.categoryAxis;
		a && (this.categoryAxis = AmCharts.processObject(a, AmCharts.CategoryAxis, this.theme));
		this.processValueAxes();
		this.createValueAxes();
		this.processGraphs();
		AmCharts.VML && (this.startAlpha = 1);
		this.setLegendData(this.graphs);
		this.gridAboveGraphs && this.gridSet.toFront()
	},
	createValueAxes: function() {
		if (0 === this.valueAxes.length) {
			var a = new AmCharts.ValueAxis;
			this.addValueAxis(a)
		}
	},
	parseData: function() {
		this.processValueAxes();
		this.processGraphs()
	},
	parseSerialData: function() {
		var a = this.graphs,
		b, c = {},
		d = this.seriesIdField;
		d || (d = this.categoryField);
		this.chartData = [];
		var e = this.dataProvider;
		if (e) {
			var f = !1,
			g, h = this.categoryAxis,
			k;
			h && (f = h.parseDates, k = h.forceShowField, g = h.categoryFunction);
			var l, m;
			f && (b = AmCharts.extractPeriod(h.minPeriod), l = b.period, m = b.count);
			var n = {};
			this.lookupTable = n;
			var p, r = this.dataDateFormat;
			for (p = 0; p < e.length; p++) {
				var u = {},
				q = e[p];
				b = q[this.categoryField];
				u.dataContext = q;
				u.category = g ? g(b, q, h) : String(b);
				k && (u.forceShow = q[k]);
				n[q[d]] = u;
				f && (b = h.categoryFunction ? h.categoryFunction(b, q, h) : b instanceof Date ? "fff" == h.minPeriod ? AmCharts.useUTC ? new Date(b.getUTCFullYear(), b.getUTCMonth(), b.getUTCDate(), b.getUTCHours(), b.getUTCMinutes(), b.getUTCSeconds(), b.getUTCMilliseconds()) : new Date(b.getFullYear(), b.getMonth(), b.getDate(), b.getHours(), b.getMinutes(), b.getSeconds(), b.getMilliseconds()) : new Date(b) : r ? AmCharts.stringToDate(b, r) : new Date(b), b = AmCharts.resetDateToMin(b, l, m, h.firstDayOfWeek), u.category = b, u.time = b.getTime());
				var t = this.valueAxes;
				u.axes = {};
				u.x = {};
				var v;
				for (v = 0; v < t.length; v++) {
					var s = t[v].id;
					u.axes[s] = {};
					u.axes[s].graphs = {};
					var w;
					for (w = 0; w < a.length; w++) {
						b = a[w];
						var A = b.id,
						C = b.periodValue;
						if (b.valueAxis.id == s) {
							u.axes[s].graphs[A] = {};
							var y = {};
							y.index = p;
							var z = q;
							b.dataProvider && (z = c);
							y.values = this.processValues(z, b, C);
							this.processFields(b, y, z);
							y.category = u.category;
							y.serialDataItem = u;
							y.graph = b;
							u.axes[s].graphs[A] = y
						}
					}
				}
				this.chartData[p] = u
			}
		}
		for (c = 0; c < a.length; c++) b = a[c],
		b.dataProvider && this.parseGraphData(b)
	},
	processValues: function(a, b, c) {
		var d = {},
		e, f = !1;
		"candlestick" != b.type && "ohlc" != b.type || "" === c || (f = !0);
		e = Number(a[b.valueField + c]);
		isNaN(e) || (d.value = e);
		e = Number(a[b.errorField + c]);
		isNaN(e) || (d.error = e);
		f && (c = "Open");
		e = Number(a[b.openField + c]);
		isNaN(e) || (d.open = e);
		f && (c = "Close");
		e = Number(a[b.closeField + c]);
		isNaN(e) || (d.close = e);
		f && (c = "Low");
		e = Number(a[b.lowField + c]);
		isNaN(e) || (d.low = e);
		f && (c = "High");
		e = Number(a[b.highField + c]);
		isNaN(e) || (d.high = e);
		return d
	},
	parseGraphData: function(a) {
		var b = a.dataProvider,
		c = a.seriesIdField;
		c || (c = this.seriesIdField);
		c || (c = this.categoryField);
		var d;
		for (d = 0; d < b.length; d++) {
			var e = b[d],
			f = this.lookupTable[String(e[c])],
			g = a.valueAxis.id;
			f && (g = f.axes[g].graphs[a.id], g.serialDataItem = f, g.values = this.processValues(e, a, a.periodValue), this.processFields(a, g, e))
		}
	},
	addValueAxis: function(a) {
		a.chart = this;
		this.valueAxes.push(a);
		this.validateData()
	},
	removeValueAxesAndGraphs: function() {
		var a = this.valueAxes,
		b;
		for (b = a.length - 1; - 1 < b; b--) this.removeValueAxis(a[b])
	},
	removeValueAxis: function(a) {
		var b = this.graphs,
		c;
		for (c = b.length - 1; 0 <= c; c--) {
			var d = b[c];
			d && d.valueAxis == a && this.removeGraph(d)
		}
		b = this.valueAxes;
		for (c = b.length - 1; 0 <= c; c--) b[c] == a && b.splice(c, 1);
		this.validateData()
	},
	addGraph: function(a) {
		this.graphs.push(a);
		this.chooseGraphColor(a, this.graphs.length - 1);
		this.validateData()
	},
	removeGraph: function(a) {
		var b = this.graphs,
		c;
		for (c = b.length - 1; 0 <= c; c--) b[c] == a && (b.splice(c, 1), a.destroy());
		this.validateData()
	},
	processValueAxes: function() {
		var a = this.valueAxes,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b],
			c = AmCharts.processObject(c, AmCharts.ValueAxis, this.theme);
			a[b] = c;
			c.chart = this;
			c.id || (c.id = "valueAxisAuto" + b + "_" + (new Date).getTime());
			if (!0 === this.usePrefixes || !1 === this.usePrefixes) c.usePrefixes = this.usePrefixes
		}
	},
	processGraphs: function() {
		var a = this.graphs,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b],
			c = AmCharts.processObject(c, AmCharts.AmGraph, this.theme);
			a[b] = c;
			this.chooseGraphColor(c, b);
			c.chart = this;
			AmCharts.isString(c.valueAxis) && (c.valueAxis = this.getValueAxisById(c.valueAxis));
			c.valueAxis || (c.valueAxis = this.valueAxes[0]);
			c.id || (c.id = "graphAuto" + b + "_" + (new Date).getTime())
		}
	},
	formatString: function(a, b, c) {
		var d = b.graph,
		e = d.valueAxis;
		e.duration && b.values.value && (e = AmCharts.formatDuration(b.values.value, e.duration, "", e.durationUnits, e.maxInterval, e.numberFormatter), a = a.split("[[value]]").join(e));
		a = AmCharts.massReplace(a, {
			"[[title]]": d.title,
			"[[description]]": b.description
		});
		a = c ? AmCharts.fixNewLines(a) : AmCharts.fixBrakes(a);
		return a = AmCharts.cleanFromEmpty(a)
	},
	getBalloonColor: function(a, b, c) {
		var d = a.lineColor,
		e = a.balloonColor;
		c && (e = d);
		c = a.fillColorsR;
		"object" == typeof c ? d = c[0] : void 0 !== c && (d = c);
		b.isNegative && (c = a.negativeLineColor, a = a.negativeFillColors, "object" == typeof a ? c = a[0] : void 0 !== a && (c = a), void 0 !== c && (d = c));
		void 0 !== b.color && (d = b.color);
		void 0 === e && (e = d);
		return e
	},
	getGraphById: function(a) {
		return AmCharts.getObjById(this.graphs, a)
	},
	getValueAxisById: function(a) {
		return AmCharts.getObjById(this.valueAxes, a)
	},
	processFields: function(a, b, c) {
		if (a.itemColors) {
			var d = a.itemColors,
			e = b.index;
			b.color = e < d.length ? d[e] : AmCharts.randomColor()
		}
		d = "lineColor color alpha fillColors description bullet customBullet bulletSize bulletConfig url labelColor dashLength pattern".split(" ");
		for (e = 0; e < d.length; e++) {
			var f = d[e],
			g = a[f + "Field"];
			g && (g = c[g], AmCharts.isDefined(g) && (b[f] = g))
		}
		b.dataContext = c
	},
	chooseGraphColor: function(a, b) {
		if (a.lineColor) a.lineColorR = a.lineColor;
		else {
			var c;
			c = this.colors.length > b ? this.colors[b] : AmCharts.randomColor();
			a.lineColorR = c
		}
		a.fillColorsR = a.fillColors ? a.fillColors: a.lineColorR;
		a.bulletBorderColorR = a.bulletBorderColor ? a.bulletBorderColor: a.useLineColorForBulletBorder ? a.lineColorR: a.bulletColor;
		a.bulletColorR = a.bulletColor ? a.bulletColor: a.lineColorR;
		if (c = this.patterns) a.pattern = c[b]
	},
	handleLegendEvent: function(a) {
		var b = a.type;
		a = a.dataItem;
		if (!this.legend.data && a) {
			var c = a.hidden,
			d = a.showBalloon;
			switch (b) {
			case "clickMarker":
				d ? this.hideGraphsBalloon(a) : this.showGraphsBalloon(a);
				break;
			case "clickLabel":
				d ? this.hideGraphsBalloon(a) : this.showGraphsBalloon(a);
				break;
			case "rollOverItem":
				c || this.highlightGraph(a);
				break;
			case "rollOutItem":
				c || this.unhighlightGraph();
				break;
			case "hideItem":
				this.hideGraph(a);
				break;
			case "showItem":
				this.showGraph(a)
			}
		}
	},
	highlightGraph: function(a) {
		var b = this.graphs,
		c, d = 0.2;
		this.legend && (d = this.legend.rollOverGraphAlpha);
		if (1 != d) for (c = 0; c < b.length; c++) {
			var e = b[c];
			e != a && e.changeOpacity(d)
		}
	},
	unhighlightGraph: function() {
		var a;
		this.legend && (a = this.legend.rollOverGraphAlpha);
		if (1 != a) {
			a = this.graphs;
			var b;
			for (b = 0; b < a.length; b++) a[b].changeOpacity(1)
		}
	},
	showGraph: function(a) {
		a.hidden = !1;
		this.dataChanged = !0;
		this.marginsUpdated = !1;
		this.chartCreated && this.initChart()
	},
	hideGraph: function(a) {
		this.dataChanged = !0;
		this.marginsUpdated = !1;
		a.hidden = !0;
		this.chartCreated && this.initChart()
	},
	hideGraphsBalloon: function(a) {
		a.showBalloon = !1;
		this.updateLegend()
	},
	showGraphsBalloon: function(a) {
		a.showBalloon = !0;
		this.updateLegend()
	},
	updateLegend: function() {
		this.legend && this.legend.invalidateSize()
	},
	resetAnimation: function() {
		var a = this.graphs;
		if (a) {
			var b;
			for (b = 0; b < a.length; b++) a[b].animationPlayed = !1
		}
	},
	animateAgain: function() {
		this.resetAnimation();
		this.validateNow()
	}
});
AmCharts.AmSlicedChart = AmCharts.Class({
	inherits: AmCharts.AmChart,
	construct: function(a) {
		this.createEvents("rollOverSlice", "rollOutSlice", "clickSlice", "pullOutSlice", "pullInSlice", "rightClickSlice");
		AmCharts.AmSlicedChart.base.construct.call(this, a);
		this.colors = "#FF0F00 #FF6600 #FF9E01 #FCD202 #F8FF01 #B0DE09 #04D215 #0D8ECF #0D52D1 #2A0CD0 #8A0CCF #CD0D74 #754DEB #DDDDDD #999999 #333333 #000000 #57032A #CA9726 #990000 #4B0C25".split(" ");
		this.alpha = 1;
		this.groupPercent = 0;
		this.groupedTitle = "Other";
		this.groupedPulled = !1;
		this.groupedAlpha = 1;
		this.marginLeft = 0;
		this.marginBottom = this.marginTop = 10;
		this.marginRight = 0;
		this.hoverAlpha = 1;
		this.outlineColor = "#FFFFFF";
		this.outlineAlpha = 0;
		this.outlineThickness = 1;
		this.startAlpha = 0;
		this.startDuration = 1;
		this.startEffect = "bounce";
		this.sequencedAnimation = !0;
		this.pullOutDuration = 1;
		this.pullOutEffect = "bounce";
		this.pullOnHover = this.pullOutOnlyOne = !1;
		this.labelsEnabled = !0;
		this.labelTickColor = "#000000";
		this.labelTickAlpha = 0.2;
		this.hideLabelsPercent = 0;
		this.urlTarget = "_self";
		this.autoMarginOffset = 10;
		this.gradientRatio = [];
		AmCharts.applyTheme(this, a, "AmSlicedChart")
	},
	initChart: function() {
		AmCharts.AmSlicedChart.base.initChart.call(this);
		this.dataChanged && (this.parseData(), this.dispatchDataUpdated = !0, this.dataChanged = !1, this.setLegendData(this.chartData));
		this.drawChart()
	},
	handleLegendEvent: function(a) {
		var b = a.type;
		a = a.dataItem;
		if (!this.legend.data && a) {
			var c = a.hidden;
			switch (b) {
			case "clickMarker":
				c || this.clickSlice(a);
				break;
			case "clickLabel":
				c || this.clickSlice(a);
				break;
			case "rollOverItem":
				c || this.rollOverSlice(a, !1);
				break;
			case "rollOutItem":
				c || this.rollOutSlice(a);
				break;
			case "hideItem":
				this.hideSlice(a);
				break;
			case "showItem":
				this.showSlice(a)
			}
		}
	},
	invalidateVisibility: function() {
		this.recalculatePercents();
		this.initChart();
		var a = this.legend;
		a && a.invalidateSize()
	},
	addEventListeners: function(a, b) {
		var c = this;
		a.mouseover(function(a) {
			c.rollOverSlice(b, !0, a)
		}).mouseout(function(a) {
			c.rollOutSlice(b, a)
		}).touchend(function(a) {
			c.rollOverSlice(b, a);
			c.panEventsEnabled && c.clickSlice(b, a)
		}).touchstart(function(a) {
			c.rollOverSlice(b, a)
		}).click(function(a) {
			c.clickSlice(b, a)
		}).contextmenu(function(a) {
			c.handleRightClick(b, a)
		})
	},
	formatString: function(a, b, c) {
		a = AmCharts.formatValue(a, b, ["value"], this.numberFormatter, "", this.usePrefixes, this.prefixesOfSmallNumbers, this.prefixesOfBigNumbers);
		a = AmCharts.formatValue(a, b, ["percents"], this.percentFormatter);
		a = AmCharts.massReplace(a, {
			"[[title]]": b.title,
			"[[description]]": b.description
		}); - 1 != a.indexOf("[[") && (a = AmCharts.formatDataContextValue(a, b.dataContext));
		a = c ? AmCharts.fixNewLines(a) : AmCharts.fixBrakes(a);
		return a = AmCharts.cleanFromEmpty(a)
	},
	startSlices: function() {
		var a;
		for (a = 0; a < this.chartData.length; a++) 0 < this.startDuration && this.sequencedAnimation ? this.setStartTO(a) : this.startSlice(this.chartData[a])
	},
	setStartTO: function(a) {
		var b = this;
		a = setTimeout(function() {
			b.startSequenced.call(b)
		},
		b.startDuration / b.chartData.length * 500 * a);
		b.timeOuts.push(a)
	},
	pullSlices: function(a) {
		var b = this.chartData,
		c;
		for (c = 0; c < b.length; c++) {
			var d = b[c];
			d.pulled && this.pullSlice(d, 1, a)
		}
	},
	startSequenced: function() {
		var a = this.chartData,
		b;
		for (b = 0; b < a.length; b++) if (!a[b].started) {
			this.startSlice(this.chartData[b]);
			break
		}
	},
	startSlice: function(a) {
		a.started = !0;
		var b = a.wedge,
		c = this.startDuration;
		b && 0 < c && (0 < a.alpha && b.show(), b.translate(a.startX, a.startY), b.animate({
			opacity: 1,
			translate: "0,0"
		},
		c, this.startEffect))
	},
	showLabels: function() {
		var a = this.chartData,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b];
			if (0 < c.alpha) {
				var d = c.label;
				d && d.show(); (c = c.tick) && c.show()
			}
		}
	},
	showSlice: function(a) {
		isNaN(a) ? a.hidden = !1 : this.chartData[a].hidden = !1;
		this.invalidateVisibility()
	},
	hideSlice: function(a) {
		isNaN(a) ? a.hidden = !0 : this.chartData[a].hidden = !0;
		this.hideBalloon();
		this.invalidateVisibility()
	},
	rollOverSlice: function(a, b, c) {
		isNaN(a) || (a = this.chartData[a]);
		clearTimeout(this.hoverInt);
		this.pullOnHover && this.pullSlice(a, 1);
		1 > this.hoverAlpha && a.wedge && a.wedge.attr({
			opacity: this.hoverAlpha
		});
		var d = a.balloonX,
		e = a.balloonY;
		a.pulled && (d += a.pullX, e += a.pullY);
		var f = this.formatString(this.balloonText, a, !0),
		g = AmCharts.adjustLuminosity(a.color, -0.15);
		this.showBalloon(f, g, b, d, e);
		a = {
			type: "rollOverSlice",
			dataItem: a,
			chart: this,
			event: c
		};
		this.fire(a.type, a)
	},
	rollOutSlice: function(a, b) {
		isNaN(a) || (a = this.chartData[a]);
		a.wedge && a.wedge.attr({
			opacity: 1
		});
		this.hideBalloon();
		var c = {
			type: "rollOutSlice",
			dataItem: a,
			chart: this,
			event: b
		};
		this.fire(c.type, c)
	},
	clickSlice: function(a, b) {
		isNaN(a) || (a = this.chartData[a]);
		a.pulled ? this.pullSlice(a, 0) : this.pullSlice(a, 1);
		AmCharts.getURL(a.url, this.urlTarget);
		var c = {
			type: "clickSlice",
			dataItem: a,
			chart: this,
			event: b
		};
		this.fire(c.type, c)
	},
	handleRightClick: function(a, b) {
		isNaN(a) || (a = this.chartData[a]);
		var c = {
			type: "rightClickSlice",
			dataItem: a,
			chart: this,
			event: b
		};
		this.fire(c.type, c)
	},
	drawTicks: function() {
		var a = this.chartData,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b];
			if (c.label) {
				var d = c.ty,
				d = AmCharts.line(this.container, [c.tx0, c.tx, c.tx2], [c.ty0, d, d], this.labelTickColor, this.labelTickAlpha);
				c.tick = d;
				c.wedge.push(d)
			}
		}
	},
	initialStart: function() {
		var a = this,
		b = a.startDuration,
		c = setTimeout(function() {
			a.showLabels.call(a)
		},
		1E3 * b);
		a.timeOuts.push(c);
		a.chartCreated ? a.pullSlices(!0) : (b = setTimeout(function() {
			a.pullSlices.call(a)
		},
		1200 * b), a.timeOuts.push(b), a.startSlices())
	},
	pullSlice: function(a, b, c) {
		var d = this.pullOutDuration; ! 0 === c && (d = 0); (c = a.wedge) && c.animate({
			translate: b * a.pullX + "," + b * a.pullY
		},
		d, this.pullOutEffect);
		1 == b ? (a.pulled = !0, this.pullOutOnlyOne && this.pullInAll(a.index), a = {
			type: "pullOutSlice",
			dataItem: a,
			chart: this
		}) : (a.pulled = !1, a = {
			type: "pullInSlice",
			dataItem: a,
			chart: this
		});
		this.fire(a.type, a)
	},
	pullInAll: function(a) {
		var b = this.chartData,
		c;
		for (c = 0; c < this.chartData.length; c++) c != a && b[c].pulled && this.pullSlice(b[c], 0)
	},
	pullOutAll: function(a) {
		a = this.chartData;
		var b;
		for (b = 0; b < a.length; b++) a[b].pulled || this.pullSlice(a[b], 1)
	},
	parseData: function() {
		var a = [];
		this.chartData = a;
		var b = this.dataProvider;
		isNaN(this.pieAlpha) || (this.alpha = this.pieAlpha);
		if (void 0 !== b) {
			var c = b.length,
			d = 0,
			e, f, g;
			for (e = 0; e < c; e++) {
				f = {};
				var h = b[e];
				f.dataContext = h;
				f.value = Number(h[this.valueField]); (g = h[this.titleField]) || (g = "");
				f.title = g;
				f.pulled = AmCharts.toBoolean(h[this.pulledField], !1); (g = h[this.descriptionField]) || (g = "");
				f.description = g;
				f.labelRadius = Number(h[this.labelRadiusField]);
				f.url = h[this.urlField];
				g = h[this.patternField]; ! g && this.patterns && (g = this.patterns[e]);
				f.pattern = g;
				f.visibleInLegend = AmCharts.toBoolean(h[this.visibleInLegendField], !0);
				g = h[this.alphaField];
				f.alpha = void 0 !== g ? Number(g) : this.alpha;
				g = h[this.colorField];
				void 0 !== g && (f.color = AmCharts.toColor(g));
				f.labelColor = AmCharts.toColor(h[this.labelColorField]);
				d += f.value;
				f.hidden = !1;
				a[e] = f
			}
			for (e = b = 0; e < c; e++) f = a[e],
			f.percents = f.value / d * 100,
			f.percents < this.groupPercent && b++;
			1 < b && (this.groupValue = 0, this.removeSmallSlices(), a.push({
				title: this.groupedTitle,
				value: this.groupValue,
				percents: this.groupValue / d * 100,
				pulled: this.groupedPulled,
				color: this.groupedColor,
				url: this.groupedUrl,
				description: this.groupedDescription,
				alpha: this.groupedAlpha,
				pattern: this.groupedPattern
			}));
			c = this.baseColor;
			c || (c = this.pieBaseColor);
			d = this.brightnessStep;
			d || (d = this.pieBrightnessStep);
			for (e = 0; e < a.length; e++) c ? g = AmCharts.adjustLuminosity(c, e * d / 100) : (g = this.colors[e], void 0 === g && (g = AmCharts.randomColor())),
			void 0 === a[e].color && (a[e].color = g);
			this.recalculatePercents()
		}
	},
	recalculatePercents: function() {
		var a = this.chartData,
		b = 0,
		c, d;
		for (c = 0; c < a.length; c++) d = a[c],
		!d.hidden && 0 < d.value && (b += d.value);
		for (c = 0; c < a.length; c++) d = this.chartData[c],
		d.percents = !d.hidden && 0 < d.value ? 100 * d.value / b: 0
	},
	removeSmallSlices: function() {
		var a = this.chartData,
		b;
		for (b = a.length - 1; 0 <= b; b--) a[b].percents < this.groupPercent && (this.groupValue += a[b].value, a.splice(b, 1))
	},
	animateAgain: function() {
		var a = this;
		a.startSlices();
		var b = setTimeout(function() {
			a.pullSlices.call(a)
		},
		1200 * a.startDuration);
		a.timeOuts.push(b)
	},
	measureMaxLabel: function() {
		var a = this.chartData,
		b = 0,
		c;
		for (c = 0; c < a.length; c++) {
			var d = this.formatString(this.labelText, a[c]),
			d = AmCharts.text(this.container, d, this.color, this.fontFamily, this.fontSize),
			e = d.getBBox().width;
			e > b && (b = e);
			d.remove()
		}
		return b
	}
});
AmCharts.AmRectangularChart = AmCharts.Class({
	inherits: AmCharts.AmCoordinateChart,
	construct: function(a) {
		AmCharts.AmRectangularChart.base.construct.call(this, a);
		this.theme = a;
		this.createEvents("zoomed");
		this.marginRight = this.marginBottom = this.marginTop = this.marginLeft = 20;
		this.verticalPosition = this.horizontalPosition = this.depth3D = this.angle = 0;
		this.heightMultiplier = this.widthMultiplier = 1;
		this.plotAreaFillColors = "#FFFFFF";
		this.plotAreaFillAlphas = 0;
		this.plotAreaBorderColor = "#000000";
		this.plotAreaBorderAlpha = 0;
		this.zoomOutButtonImageSize = 17;
		this.zoomOutButtonImage = "lens.png";
		this.zoomOutText = "Show all";
		this.zoomOutButtonColor = "#e5e5e5";
		this.zoomOutButtonAlpha = 0;
		this.zoomOutButtonRollOverAlpha = 1;
		this.zoomOutButtonPadding = 8;
		this.trendLines = [];
		this.autoMargins = !0;
		this.marginsUpdated = !1;
		this.autoMarginOffset = 10;
		AmCharts.applyTheme(this, a, "AmRectangularChart")
	},
	initChart: function() {
		AmCharts.AmRectangularChart.base.initChart.call(this);
		this.updateDxy();
		var a = !0; ! this.marginsUpdated && this.autoMargins && (this.resetMargins(), a = !1);
		this.processScrollbars();
		this.updateMargins();
		this.updatePlotArea();
		this.updateScrollbars();
		this.updateTrendLines();
		this.updateChartCursor();
		this.updateValueAxes();
		a && (this.scrollbarOnly || this.updateGraphs())
	},
	drawChart: function() {
		AmCharts.AmRectangularChart.base.drawChart.call(this);
		this.drawPlotArea();
		if (AmCharts.ifArray(this.chartData)) {
			var a = this.chartCursor;
			a && a.draw();
			a = this.zoomOutText;
			"" !== a && a && this.drawZoomOutButton()
		}
	},
	resetMargins: function() {
		var a = {},
		b;
		if ("serial" == this.type) {
			var c = this.valueAxes;
			for (b = 0; b < c.length; b++) {
				var d = c[b];
				d.ignoreAxisWidth || (d.setOrientation(this.rotate), d.fixAxisPosition(), a[d.position] = !0)
			} (b = this.categoryAxis) && !b.ignoreAxisWidth && (b.setOrientation(!this.rotate), b.fixAxisPosition(), b.fixAxisPosition(), a[b.position] = !0)
		} else {
			d = this.xAxes;
			c = this.yAxes;
			for (b = 0; b < d.length; b++) {
				var e = d[b];
				e.ignoreAxisWidth || (e.setOrientation(!0), e.fixAxisPosition(), a[e.position] = !0)
			}
			for (b = 0; b < c.length; b++) d = c[b],
			d.ignoreAxisWidth || (d.setOrientation(!1), d.fixAxisPosition(), a[d.position] = !0)
		}
		a.left && (this.marginLeft = 0);
		a.right && (this.marginRight = 0);
		a.top && (this.marginTop = 0);
		a.bottom && (this.marginBottom = 0);
		this.fixMargins = a
	},
	measureMargins: function() {
		var a = this.valueAxes,
		b, c = this.autoMarginOffset,
		d = this.fixMargins,
		e = this.realWidth,
		f = this.realHeight,
		g = c,
		h = c,
		k = e - c;
		b = f - c;
		var l;
		for (l = 0; l < a.length; l++) b = this.getAxisBounds(a[l], g, k, h, b),
		g = b.l,
		k = b.r,
		h = b.t,
		b = b.b;
		if (a = this.categoryAxis) b = this.getAxisBounds(a, g, k, h, b),
		g = b.l,
		k = b.r,
		h = b.t,
		b = b.b;
		d.left && g < c && (this.marginLeft = Math.round( - g + c));
		d.right && k > e - c && (this.marginRight = Math.round(k - e + c));
		d.top && h < c + this.titleHeight && (this.marginTop = Math.round(this.marginTop - h + c + this.titleHeight));
		d.bottom && b > f - c && (this.marginBottom = Math.round(this.marginBottom + b - f + c));
		this.initChart()
	},
	getAxisBounds: function(a, b, c, d, e) {
		if (!a.ignoreAxisWidth) {
			var f = a.labelsSet,
			g = a.tickLength;
			a.inside && (g = 0);
			if (f) switch (f = a.getBBox(), a.position) {
			case "top":
				a = f.y;
				d > a && (d = a);
				break;
			case "bottom":
				a = f.y + f.height;
				e < a && (e = a);
				break;
			case "right":
				a = f.x + f.width + g + 3;
				c < a && (c = a);
				break;
			case "left":
				a = f.x - g,
				b > a && (b = a)
			}
		}
		return {
			l: b,
			t: d,
			r: c,
			b: e
		}
	},
	drawZoomOutButton: function() {
		var a = this,
		b = a.container.set();
		a.zoomButtonSet.push(b);
		var c = a.color,
		d = a.fontSize,
		e = a.zoomOutButtonImageSize,
		f = a.zoomOutButtonImage,
		g = a.zoomOutText,
		h = a.zoomOutButtonColor,
		k = a.zoomOutButtonAlpha,
		l = a.zoomOutButtonFontSize,
		m = a.zoomOutButtonPadding;
		isNaN(l) || (d = l); (l = a.zoomOutButtonFontColor) && (c = l);
		var l = a.zoomOutButton,
		n;
		l && (l.fontSize && (d = l.fontSize), l.color && (c = l.color), l.backgroundColor && (h = l.backgroundColor), isNaN(l.backgroundAlpha) || (a.zoomOutButtonRollOverAlpha = l.backgroundAlpha));
		var p = l = 0;
		void 0 !== a.pathToImages && f && (n = a.container.image(a.pathToImages + f, 0, 0, e, e), b.push(n), n = n.getBBox(), l = n.width + 5);
		void 0 !== g && (c = AmCharts.text(a.container, g, c, a.fontFamily, d, "start"), d = c.getBBox(), p = n ? n.height / 2 - 3 : d.height / 2, c.translate(l, p), b.push(c));
		n = b.getBBox();
		h = AmCharts.rect(a.container, n.width + 2 * m + 5, n.height + 2 * m - 2, h, 1, 1, h, 1);
		h.setAttr("opacity", k);
		h.translate( - m, -m);
		b.push(h);
		h.toBack();
		a.zbBG = h;
		n = h.getBBox();
		b.translate(a.marginLeftReal + a.plotAreaWidth - n.width + m, a.marginTopReal + m);
		b.hide();
		b.mouseover(function() {
			a.rollOverZB()
		}).mouseout(function() {
			a.rollOutZB()
		}).click(function() {
			a.clickZB()
		}).touchstart(function() {
			a.rollOverZB()
		}).touchend(function() {
			a.rollOutZB();
			a.clickZB()
		});
		for (k = 0; k < b.length; k++) b[k].attr({
			cursor: "pointer"
		});
		a.zbSet = b
	},
	rollOverZB: function() {
		this.zbBG.setAttr("opacity", this.zoomOutButtonRollOverAlpha)
	},
	rollOutZB: function() {
		this.zbBG.setAttr("opacity", this.zoomOutButtonAlpha)
	},
	clickZB: function() {
		this.zoomOut()
	},
	zoomOut: function() {
		this.updateScrollbar = !0;
		this.zoom()
	},
	drawPlotArea: function() {
		var a = this.dx,
		b = this.dy,
		c = this.marginLeftReal,
		d = this.marginTopReal,
		e = this.plotAreaWidth - 1,
		f = this.plotAreaHeight - 1,
		g = this.plotAreaFillColors,
		h = this.plotAreaFillAlphas,
		k = this.plotAreaBorderColor,
		l = this.plotAreaBorderAlpha;
		this.trendLinesSet.clipRect(c, d, e, f);
		"object" == typeof h && (h = h[0]);
		g = AmCharts.polygon(this.container, [0, e, e, 0], [0, 0, f, f], g, h, 1, k, l, this.plotAreaGradientAngle);
		g.translate(c + a, d + b);
		g.node.setAttribute("class", "amChartsPlotArea");
		this.set.push(g);
		0 !== a && 0 !== b && (g = this.plotAreaFillColors, "object" == typeof g && (g = g[0]), g = AmCharts.adjustLuminosity(g, -0.15), e = AmCharts.polygon(this.container, [0, a, e + a, e, 0], [0, b, b, 0, 0], g, h, 1, k, l), e.translate(c, d + f), this.set.push(e), a = AmCharts.polygon(this.container, [0, 0, a, a, 0], [0, f, f + b, b, 0], g, h, 1, k, l), a.translate(c, d), this.set.push(a)); (c = this.bbset) && this.scrollbarOnly && c.remove()
	},
	updatePlotArea: function() {
		var a = this.updateWidth(),
		b = this.updateHeight(),
		c = this.container;
		this.realWidth = a;
		this.realWidth = b;
		c && this.container.setSize(a, b);
		a = a - this.marginLeftReal - this.marginRightReal - this.dx;
		b = b - this.marginTopReal - this.marginBottomReal;
		1 > a && (a = 1);
		1 > b && (b = 1);
		this.plotAreaWidth = Math.round(a);
		this.plotAreaHeight = Math.round(b)
	},
	updateDxy: function() {
		this.dx = Math.round(this.depth3D * Math.cos(this.angle * Math.PI / 180));
		this.dy = Math.round( - this.depth3D * Math.sin(this.angle * Math.PI / 180));
		this.d3x = Math.round(this.columnSpacing3D * Math.cos(this.angle * Math.PI / 180));
		this.d3y = Math.round( - this.columnSpacing3D * Math.sin(this.angle * Math.PI / 180))
	},
	updateMargins: function() {
		var a = this.getTitleHeight();
		this.titleHeight = a;
		this.marginTopReal = this.marginTop - this.dy + a;
		this.marginBottomReal = this.marginBottom;
		this.marginLeftReal = this.marginLeft;
		this.marginRightReal = this.marginRight
	},
	updateValueAxes: function() {
		var a = this.valueAxes,
		b = this.marginLeftReal,
		c = this.marginTopReal,
		d = this.plotAreaHeight,
		e = this.plotAreaWidth,
		f;
		for (f = 0; f < a.length; f++) {
			var g = a[f];
			g.axisRenderer = AmCharts.RecAxis;
			g.guideFillRenderer = AmCharts.RecFill;
			g.axisItemRenderer = AmCharts.RecItem;
			g.dx = this.dx;
			g.dy = this.dy;
			g.viW = e - 1;
			g.viH = d - 1;
			g.marginsChanged = !0;
			g.viX = b;
			g.viY = c;
			this.updateObjectSize(g)
		}
	},
	updateObjectSize: function(a) {
		a.width = (this.plotAreaWidth - 1) * this.widthMultiplier;
		a.height = (this.plotAreaHeight - 1) * this.heightMultiplier;
		a.x = this.marginLeftReal + this.horizontalPosition;
		a.y = this.marginTopReal + this.verticalPosition
	},
	updateGraphs: function() {
		var a = this.graphs,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b];
			c.x = this.marginLeftReal + this.horizontalPosition;
			c.y = this.marginTopReal + this.verticalPosition;
			c.width = this.plotAreaWidth * this.widthMultiplier;
			c.height = this.plotAreaHeight * this.heightMultiplier;
			c.index = b;
			c.dx = this.dx;
			c.dy = this.dy;
			c.rotate = this.rotate
		}
	},
	updateChartCursor: function() {
		var a = this.chartCursor;
		a && (a = AmCharts.processObject(a, AmCharts.ChartCursor, this.theme), this.addChartCursor(a), a.x = this.marginLeftReal, a.y = this.marginTopReal, a.width = this.plotAreaWidth - 1, a.height = this.plotAreaHeight - 1, a.chart = this)
	},
	processScrollbars: function() {
		var a = this.chartScrollbar;
		a && (a = AmCharts.processObject(a, AmCharts.ChartScrollbar, this.theme), this.addChartScrollbar(a))
	},
	updateScrollbars: function() {},
	addChartCursor: function(a) {
		AmCharts.callMethod("destroy", [this.chartCursor]);
		a && (this.listenTo(a, "changed", this.handleCursorChange), this.listenTo(a, "zoomed", this.handleCursorZoom));
		this.chartCursor = a
	},
	removeChartCursor: function() {
		AmCharts.callMethod("destroy", [this.chartCursor]);
		this.chartCursor = null
	},
	zoomTrendLines: function() {
		var a = this.trendLines,
		b;
		for (b = 0; b < a.length; b++) {
			var c = a[b];
			c.valueAxis.recalculateToPercents ? c.set && c.set.hide() : (c.x = this.marginLeftReal + this.horizontalPosition, c.y = this.marginTopReal + this.verticalPosition, c.draw())
		}
	},
	addTrendLine: function(a) {
		this.trendLines.push(a)
	},
	removeTrendLine: function(a) {
		var b = this.trendLines,
		c;
		for (c = b.length - 1; 0 <= c; c--) b[c] == a && b.splice(c, 1)
	},
	adjustMargins: function(a, b) {
		var c = a.scrollbarHeight;
		"top" == a.position ? b ? this.marginLeftReal += c: this.marginTopReal += c: b ? this.marginRightReal += c: this.marginBottomReal += c
	},
	getScrollbarPosition: function(a, b, c) {
		a.position = b ? "bottom" == c || "left" == c ? "bottom": "top": "top" == c || "right" == c ? "bottom": "top"
	},
	updateChartScrollbar: function(a, b) {
		if (a) {
			a.rotate = b;
			var c = this.marginTopReal,
			d = this.marginLeftReal,
			e = a.scrollbarHeight,
			f = this.dx,
			g = this.dy;
			"top" == a.position ? b ? (a.y = c, a.x = d - e) : (a.y = c - e + g - 1, a.x = d + f) : b ? (a.y = c + g, a.x = d + this.plotAreaWidth + f) : (a.y = c + this.plotAreaHeight, a.x = this.marginLeftReal)
		}
	},
	showZB: function(a) {
		var b = this.zbSet;
		b && (a ? b.show() : b.hide(), this.rollOutZB())
	},
	handleReleaseOutside: function(a) {
		AmCharts.AmRectangularChart.base.handleReleaseOutside.call(this, a); (a = this.chartCursor) && a.handleReleaseOutside()
	},
	handleMouseDown: function(a) {
		AmCharts.AmRectangularChart.base.handleMouseDown.call(this, a);
		var b = this.chartCursor;
		b && b.handleMouseDown(a)
	},
	handleCursorChange: function(a) {}
});
AmCharts.TrendLine = AmCharts.Class({
	construct: function(a) {
		this.cname = "TrendLine";
		this.createEvents("click");
		this.isProtected = !1;
		this.dashLength = 0;
		this.lineColor = "#00CC00";
		this.lineThickness = this.lineAlpha = 1;
		AmCharts.applyTheme(this, a, this.cname)
	},
	draw: function() {
		var a = this;
		a.destroy();
		var b = a.chart,
		c = b.container,
		d, e, f, g, h = a.categoryAxis,
		k = a.initialDate,
		l = a.initialCategory,
		m = a.finalDate,
		n = a.finalCategory,
		p = a.valueAxis,
		r = a.valueAxisX,
		u = a.initialXValue,
		q = a.finalXValue,
		t = a.initialValue,
		v = a.finalValue,
		s = p.recalculateToPercents,
		w = b.dataDateFormat;
		h && (k && (k instanceof Date || (k = w ? AmCharts.stringToDate(k, w) : new Date(k)), a.initialDate = k, d = h.dateToCoordinate(k)), l && (d = h.categoryToCoordinate(l)), m && (m instanceof Date || (m = w ? AmCharts.stringToDate(m, w) : new Date(m)), a.finalDate = m, e = h.dateToCoordinate(m)), n && (e = h.categoryToCoordinate(n)));
		r && !s && (isNaN(u) || (d = r.getCoordinate(u)), isNaN(q) || (e = r.getCoordinate(q)));
		p && !s && (isNaN(t) || (f = p.getCoordinate(t)), isNaN(v) || (g = p.getCoordinate(v)));
		isNaN(d) || isNaN(e) || isNaN(f) || isNaN(f) || (b.rotate ? (h = [f, g], e = [d, e]) : (h = [d, e], e = [f, g]), f = a.lineColor, d = AmCharts.line(c, h, e, f, a.lineAlpha, a.lineThickness, a.dashLength), g = h, k = e, n = h[1] - h[0], p = e[1] - e[0], 0 === n && (n = 0.01), 0 === p && (p = 0.01), l = n / Math.abs(n), m = p / Math.abs(p), p = n * p / Math.abs(n * p) * Math.sqrt(Math.pow(n, 2) + Math.pow(p, 2)), n = Math.asin(n / p), p = 90 * Math.PI / 180 - n, n = Math.abs(5 * Math.cos(p)), p = Math.abs(5 * Math.sin(p)), g.push(h[1] - l * p, h[0] - l * p), k.push(e[1] + m * n, e[0] + m * n), h = AmCharts.polygon(c, g, k, f, 0.005, 0), c = c.set([h, d]), c.translate(b.marginLeftReal, b.marginTopReal), b.trendLinesSet.push(c), a.line = d, a.set = c, h.mouseup(function() {
			a.handleLineClick()
		}).mouseover(function() {
			a.handleLineOver()
		}).mouseout(function() {
			a.handleLineOut()
		}), h.touchend && h.touchend(function() {
			a.handleLineClick()
		}))
	},
	handleLineClick: function() {
		var a = {
			type: "click",
			trendLine: this,
			chart: this.chart
		};
		this.fire(a.type, a)
	},
	handleLineOver: function() {
		var a = this.rollOverColor;
		void 0 !== a && this.line.attr({
			stroke: a
		})
	},
	handleLineOut: function() {
		this.line.attr({
			stroke: this.lineColor
		})
	},
	destroy: function() {
		AmCharts.remove(this.set)
	}
});
AmCharts.circle = function(a, b, c, d, e, f, g, h) {
	if (void 0 == e || 0 === e) e = 0.01;
	void 0 === f && (f = "#000000");
	void 0 === g && (g = 0);
	d = {
		fill: c,
		stroke: f,
		"fill-opacity": d,
		"stroke-width": e,
		"stroke-opacity": g
	};
	a = a.circle(0, 0, b).attr(d);
	h && a.gradient("radialGradient", [c, AmCharts.adjustLuminosity(c, -0.6)]);
	return a
};
AmCharts.text = function(a, b, c, d, e, f, g, h) {
	f || (f = "middle");
	"right" == f && (f = "end");
	isNaN(h) && (h = 1);
	void 0 !== b && (b = String(b), AmCharts.isIE && !AmCharts.isModern && (b = b.replace("&amp;", "&"), b = b.replace("&", "&amp;")));
	c = {
		fill: c,
		"font-family": d,
		"font-size": e,
		opacity: h
	}; ! 0 === g && (c["font-weight"] = "bold");
	c["text-anchor"] = f;
	return a.text(b, c)
};
AmCharts.polygon = function(a, b, c, d, e, f, g, h, k, l, m) {
	isNaN(f) && (f = 0.01);
	isNaN(h) && (h = e);
	var n = d,
	p = !1;
	"object" == typeof n && 1 < n.length && (p = !0, n = n[0]);
	void 0 === g && (g = n);
	e = {
		fill: n,
		stroke: g,
		"fill-opacity": e,
		"stroke-width": f,
		"stroke-opacity": h
	};
	void 0 !== m && 0 < m && (e["stroke-dasharray"] = m);
	m = AmCharts.dx;
	f = AmCharts.dy;
	a.handDrawn && (c = AmCharts.makeHD(b, c, a.handDrawScatter), b = c[0], c = c[1]);
	g = Math.round;
	l && (g = AmCharts.doNothing);
	l = "M" + (g(b[0]) + m) + "," + (g(c[0]) + f);
	for (h = 1; h < b.length; h++) l += " L" + (g(b[h]) + m) + "," + (g(c[h]) + f);
	a = a.path(l + " Z").attr(e);
	p && a.gradient("linearGradient", d, k);
	return a
};
AmCharts.rect = function(a, b, c, d, e, f, g, h, k, l, m) {
	isNaN(f) && (f = 0);
	void 0 === k && (k = 0);
	void 0 === l && (l = 270);
	isNaN(e) && (e = 0);
	var n = d,
	p = !1;
	"object" == typeof n && (n = n[0], p = !0);
	void 0 === g && (g = n);
	void 0 === h && (h = e);
	b = Math.round(b);
	c = Math.round(c);
	var r = 0,
	u = 0;
	0 > b && (b = Math.abs(b), r = -b);
	0 > c && (c = Math.abs(c), u = -c);
	r += AmCharts.dx;
	u += AmCharts.dy;
	e = {
		fill: n,
		stroke: g,
		"fill-opacity": e,
		"stroke-opacity": h
	};
	void 0 !== m && 0 < m && (e["stroke-dasharray"] = m);
	a = a.rect(r, u, b, c, k, f).attr(e);
	p && a.gradient("linearGradient", d, l);
	return a
};
AmCharts.bullet = function(a, b, c, d, e, f, g, h, k, l, m) {
	var n;
	"circle" == b && (b = "round");
	switch (b) {
	case "round":
		n = AmCharts.circle(a, c / 2, d, e, f, g, h);
		break;
	case "square":
		n = AmCharts.polygon(a, [ - c / 2, c / 2, c / 2, -c / 2], [c / 2, c / 2, -c / 2, -c / 2], d, e, f, g, h, l - 180);
		break;
	case "rectangle":
		n = AmCharts.polygon(a, [ - c, c, c, -c], [c / 2, c / 2, -c / 2, -c / 2], d, e, f, g, h, l - 180);
		break;
	case "diamond":
		n = AmCharts.polygon(a, [ - c / 2, 0, c / 2, 0], [0, -c / 2, 0, c / 2], d, e, f, g, h);
		break;
	case "triangleUp":
		n = AmCharts.triangle(a, c, 0, d, e, f, g, h);
		break;
	case "triangleDown":
		n = AmCharts.triangle(a, c, 180, d, e, f, g, h);
		break;
	case "triangleLeft":
		n = AmCharts.triangle(a, c, 270, d, e, f, g, h);
		break;
	case "triangleRight":
		n = AmCharts.triangle(a, c, 90, d, e, f, g, h);
		break;
	case "bubble":
		n = AmCharts.circle(a, c / 2, d, e, f, g, h, !0);
		break;
	case "yError":
		n = a.set();
		n.push(AmCharts.line(a, [0, 0], [ - c / 2, c / 2], d, e, f));
		n.push(AmCharts.line(a, [ - k, k], [ - c / 2, -c / 2], d, e, f));
		n.push(AmCharts.line(a, [ - k, k], [c / 2, c / 2], d, e, f));
		break;
	case "xError":
		n = a.set(),
		n.push(AmCharts.line(a, [ - c / 2, c / 2], [0, 0], d, e, f)),
		n.push(AmCharts.line(a, [ - c / 2, -c / 2], [ - k, k], d, e, f)),
		n.push(AmCharts.line(a, [c / 2, c / 2], [ - k, k], d, e, f))
	}
	n && n.pattern(m);
	return n
};
AmCharts.triangle = function(a, b, c, d, e, f, g, h) {
	if (void 0 === f || 0 === f) f = 1;
	void 0 === g && (g = "#000");
	void 0 === h && (h = 0);
	d = {
		fill: d,
		stroke: g,
		"fill-opacity": e,
		"stroke-width": f,
		"stroke-opacity": h
	};
	b /= 2;
	var k;
	0 === c && (k = " M" + -b + "," + b + " L0," + -b + " L" + b + "," + b + " Z");
	180 == c && (k = " M" + -b + "," + -b + " L0," + b + " L" + b + "," + -b + " Z");
	90 == c && (k = " M" + -b + "," + -b + " L" + b + ",0 L" + -b + "," + b + " Z");
	270 == c && (k = " M" + -b + ",0 L" + b + "," + b + " L" + b + "," + -b + " Z");
	return a.path(k).attr(d)
};
AmCharts.line = function(a, b, c, d, e, f, g, h, k, l, m) {
	if (a.handDrawn && !m) return AmCharts.handDrawnLine(a, b, c, d, e, f, g, h, k, l, m);
	f = {
		fill: "none",
		"stroke-width": f
	};
	void 0 !== g && 0 < g && (f["stroke-dasharray"] = g);
	isNaN(e) || (f["stroke-opacity"] = e);
	d && (f.stroke = d);
	d = Math.round;
	l && (d = AmCharts.doNothing);
	l = AmCharts.dx;
	e = AmCharts.dy;
	g = "M" + (d(b[0]) + l) + "," + (d(c[0]) + e);
	for (h = 1; h < b.length; h++) g += " L" + (d(b[h]) + l) + "," + (d(c[h]) + e);
	if (AmCharts.VML) return a.path(g, void 0, !0).attr(f);
	k && (g += " M0,0 L0,0");
	return a.path(g).attr(f)
};
AmCharts.makeHD = function(a, b, c) {
	for (var d = [], e = [], f = 1; f < a.length; f++) for (var g = Number(a[f - 1]), h = Number(b[f - 1]), k = Number(a[f]), l = Number(b[f]), m = Math.sqrt(Math.pow(k - g, 2) + Math.pow(l - h, 2)), m = Math.round(m / 50) + 1, k = (k - g) / m, l = (l - h) / m, n = 0; n <= m; n++) {
		var p = g + n * k + Math.random() * c,
		r = h + n * l + Math.random() * c;
		d.push(p);
		e.push(r)
	}
	return [d, e]
};
AmCharts.handDrawnLine = function(a, b, c, d, e, f, g, h, k, l, m) {
	var n = a.set();
	for (m = 1; m < b.length; m++) for (var p = [b[m - 1], b[m]], r = [c[m - 1], c[m]], r = AmCharts.makeHD(p, r, a.handDrawScatter), p = r[0], r = r[1], u = 1; u < p.length; u++) n.push(AmCharts.line(a, [p[u - 1], p[u]], [r[u - 1], r[u]], d, e, f + Math.random() * a.handDrawThickness - a.handDrawThickness / 2, g, h, k, l, !0));
	return n
};
AmCharts.doNothing = function(a) {
	return a
};
AmCharts.wedge = function(a, b, c, d, e, f, g, h, k, l, m, n) {
	var p = Math.round;
	f = p(f);
	g = p(g);
	h = p(h);
	var r = p(g / f * h),
	u = AmCharts.VML,
	q = 359.5 + f / 100;
	359.94 < q && (q = 359.94);
	e >= q && (e = q);
	var t = 1 / 180 * Math.PI,
	q = b + Math.sin(d * t) * h,
	v = c - Math.cos(d * t) * r,
	s = b + Math.sin(d * t) * f,
	w = c - Math.cos(d * t) * g,
	A = b + Math.sin((d + e) * t) * f,
	C = c - Math.cos((d + e) * t) * g,
	y = b + Math.sin((d + e) * t) * h,
	t = c - Math.cos((d + e) * t) * r,
	z = {
		fill: AmCharts.adjustLuminosity(l.fill, -0.2),
		"stroke-opacity": 0,
		"fill-opacity": l["fill-opacity"]
	},
	J = 0;
	180 < Math.abs(e) && (J = 1);
	d = a.set();
	var I;
	u && (q = p(10 * q), s = p(10 * s), A = p(10 * A), y = p(10 * y), v = p(10 * v), w = p(10 * w), C = p(10 * C), t = p(10 * t), b = p(10 * b), k = p(10 * k), c = p(10 * c), f *= 10, g *= 10, h *= 10, r *= 10, 1 > Math.abs(e) && 1 >= Math.abs(A - s) && 1 >= Math.abs(C - w) && (I = !0));
	e = "";
	var B;
	n && (z["fill-opacity"] = 0, z["stroke-opacity"] = l["stroke-opacity"] / 2, z.stroke = l.stroke);
	0 < k && (u ? (B = " M" + q + "," + (v + k) + " L" + s + "," + (w + k), I || (B += " A" + (b - f) + "," + (k + c - g) + "," + (b + f) + "," + (k + c + g) + "," + s + "," + (w + k) + "," + A + "," + (C + k)), B += " L" + y + "," + (t + k), 0 < h && (I || (B += " B" + (b - h) + "," + (k + c - r) + "," + (b + h) + "," + (k + c + r) + "," + y + "," + (k + t) + "," + q + "," + (k + v)))) : (B = " M" + q + "," + (v + k) + " L" + s + "," + (w + k) + (" A" + f + "," + g + ",0," + J + ",1," + A + "," + (C + k) + " L" + y + "," + (t + k)), 0 < h && (B += " A" + h + "," + r + ",0," + J + ",0," + q + "," + (v + k))), B += " Z", B = a.path(B, void 0, void 0, "1000,1000").attr(z), d.push(B), B = a.path(" M" + q + "," + v + " L" + q + "," + (v + k) + " L" + s + "," + (w + k) + " L" + s + "," + w + " L" + q + "," + v + " Z", void 0, void 0, "1000,1000").attr(z), k = a.path(" M" + A + "," + C + " L" + A + "," + (C + k) + " L" + y + "," + (t + k) + " L" + y + "," + t + " L" + A + "," + C + " Z", void 0, void 0, "1000,1000").attr(z), d.push(B), d.push(k));
	u ? (I || (e = " A" + p(b - f) + "," + p(c - g) + "," + p(b + f) + "," + p(c + g) + "," + p(s) + "," + p(w) + "," + p(A) + "," + p(C)), f = " M" + p(q) + "," + p(v) + " L" + p(s) + "," + p(w) + e + " L" + p(y) + "," + p(t)) : f = " M" + q + "," + v + " L" + s + "," + w + (" A" + f + "," + g + ",0," + J + ",1," + A + "," + C) + " L" + y + "," + t;
	0 < h && (u ? I || (f += " B" + (b - h) + "," + (c - r) + "," + (b + h) + "," + (c + r) + "," + y + "," + t + "," + q + "," + v) : f += " A" + h + "," + r + ",0," + J + ",0," + q + "," + v);
	a.handDrawn && (b = AmCharts.line(a, [q, s], [v, w], l.stroke, l.thickness * Math.random() * a.handDrawThickness, l["stroke-opacity"]), d.push(b));
	a = a.path(f + " Z", void 0, void 0, "1000,1000").attr(l);
	if (m) {
		b = [];
		for (c = 0; c < m.length; c++) b.push(AmCharts.adjustLuminosity(l.fill, m[c]));
		0 < b.length && a.gradient("linearGradient", b)
	}
	a.pattern(n);
	d.push(a);
	return d
};
AmCharts.adjustLuminosity = function(a, b) {
	a = String(a).replace(/[^0-9a-f]/gi, "");
	6 > a.length && (a = String(a[0]) + String(a[0]) + String(a[1]) + String(a[1]) + String(a[2]) + String(a[2]));
	b = b || 0;
	var c = "#",
	d, e;
	for (e = 0; 3 > e; e++) d = parseInt(a.substr(2 * e, 2), 16),
	d = Math.round(Math.min(Math.max(0, d + d * b), 255)).toString(16),
	c += ("00" + d).substr(d.length);
	return c
};
AmCharts.Bezier = AmCharts.Class({
	construct: function(a, b, c, d, e, f, g, h, k, l) {
		"object" == typeof g && (g = g[0]);
		"object" == typeof h && (h = h[0]);
		f = {
			fill: g,
			"fill-opacity": h,
			"stroke-width": f
		};
		void 0 !== k && 0 < k && (f["stroke-dasharray"] = k);
		isNaN(e) || (f["stroke-opacity"] = e);
		d && (f.stroke = d);
		d = "M" + Math.round(b[0]) + "," + Math.round(c[0]);
		e = [];
		for (k = 0; k < b.length; k++) e.push({
			x: Number(b[k]),
			y: Number(c[k])
		});
		1 < e.length && (b = this.interpolate(e), d += this.drawBeziers(b));
		l ? d += l: AmCharts.VML || (d += "M0,0 L0,0");
		this.path = a.path(d).attr(f)
	},
	interpolate: function(a) {
		var b = [];
		b.push({
			x: a[0].x,
			y: a[0].y
		});
		var c = a[1].x - a[0].x,
		d = a[1].y - a[0].y,
		e = AmCharts.bezierX,
		f = AmCharts.bezierY;
		b.push({
			x: a[0].x + c / e,
			y: a[0].y + d / f
		});
		var g;
		for (g = 1; g < a.length - 1; g++) {
			var h = a[g - 1],
			k = a[g],
			d = a[g + 1];
			isNaN(d.x) && (d = k);
			isNaN(k.x) && (k = h);
			isNaN(h.x) && (h = k);
			c = d.x - k.x;
			d = d.y - h.y;
			h = k.x - h.x;
			h > c && (h = c);
			b.push({
				x: k.x - h / e,
				y: k.y - d / f
			});
			b.push({
				x: k.x,
				y: k.y
			});
			b.push({
				x: k.x + h / e,
				y: k.y + d / f
			})
		}
		d = a[a.length - 1].y - a[a.length - 2].y;
		c = a[a.length - 1].x - a[a.length - 2].x;
		b.push({
			x: a[a.length - 1].x - c / e,
			y: a[a.length - 1].y - d / f
		});
		b.push({
			x: a[a.length - 1].x,
			y: a[a.length - 1].y
		});
		return b
	},
	drawBeziers: function(a) {
		var b = "",
		c;
		for (c = 0; c < (a.length - 1) / 3; c++) b += this.drawBezierMidpoint(a[3 * c], a[3 * c + 1], a[3 * c + 2], a[3 * c + 3]);
		return b
	},
	drawBezierMidpoint: function(a, b, c, d) {
		var e = Math.round,
		f = this.getPointOnSegment(a, b, 0.75),
		g = this.getPointOnSegment(d, c, 0.75),
		h = (d.x - a.x) / 16,
		k = (d.y - a.y) / 16,
		l = this.getPointOnSegment(a, b, 0.375);
		a = this.getPointOnSegment(f, g, 0.375);
		a.x -= h;
		a.y -= k;
		b = this.getPointOnSegment(g, f, 0.375);
		b.x += h;
		b.y += k;
		c = this.getPointOnSegment(d, c, 0.375);
		h = this.getMiddle(l, a);
		f = this.getMiddle(f, g);
		g = this.getMiddle(b, c);
		l = " Q" + e(l.x) + "," + e(l.y) + "," + e(h.x) + "," + e(h.y);
		l += " Q" + e(a.x) + "," + e(a.y) + "," + e(f.x) + "," + e(f.y);
		l += " Q" + e(b.x) + "," + e(b.y) + "," + e(g.x) + "," + e(g.y);
		return l += " Q" + e(c.x) + "," + e(c.y) + "," + e(d.x) + "," + e(d.y)
	},
	getMiddle: function(a, b) {
		return {
			x: (a.x + b.x) / 2,
			y: (a.y + b.y) / 2
		}
	},
	getPointOnSegment: function(a, b, c) {
		return {
			x: a.x + (b.x - a.x) * c,
			y: a.y + (b.y - a.y) * c
		}
	}
});
AmCharts.AmDraw = AmCharts.Class({
	construct: function(a, b, c, d) {
		AmCharts.SVG_NS = "http://www.w3.org/2000/svg";
		AmCharts.SVG_XLINK = "http://www.w3.org/1999/xlink";
		AmCharts.hasSVG = !!document.createElementNS && !!document.createElementNS(AmCharts.SVG_NS, "svg").createSVGRect;
		1 > b && (b = 10);
		1 > c && (c = 10);
		this.div = a;
		this.width = b;
		this.height = c;
		this.rBin = document.createElement("div");
		if (AmCharts.hasSVG) {
			AmCharts.SVG = !0;
			var e = this.createSvgElement("svg");
			e.style.position = "absolute";
			e.style.width = b + "px";
			e.style.height = c + "px";
			b = this.createSvgElement("desc");
			b.appendChild(document.createTextNode("JavaScript chart by amCharts " + d.version));
			e.appendChild(b);
			AmCharts.rtl && (e.setAttribute("direction", "rtl"), e.style.left = "auto", e.style.right = "0px");
			e.setAttribute("version", "1.1");
			a.appendChild(e);
			this.container = e;
			this.R = new AmCharts.SVGRenderer(this)
		} else AmCharts.isIE && AmCharts.VMLRenderer && (AmCharts.VML = !0, AmCharts.vmlStyleSheet || (document.namespaces.add("amvml", "urn:schemas-microsoft-com:vml"), e = document.createStyleSheet(), e.addRule(".amvml", "behavior:url(#default#VML); display:inline-block; antialias:true"), AmCharts.vmlStyleSheet = e), this.container = a, this.R = new AmCharts.VMLRenderer(this, d), this.R.disableSelection(a))
	},
	createSvgElement: function(a) {
		return document.createElementNS(AmCharts.SVG_NS, a)
	},
	circle: function(a, b, c, d) {
		var e = new AmCharts.AmDObject("circle", this);
		e.attr({
			r: c,
			cx: a,
			cy: b
		});
		this.addToContainer(e.node, d);
		return e
	},
	setSize: function(a, b) {
		0 < a && 0 < b && (this.container.style.width = a + "px", this.container.style.height = b + "px")
	},
	rect: function(a, b, c, d, e, f, g) {
		var h = new AmCharts.AmDObject("rect", this);
		AmCharts.VML && (e = 100 * e / Math.min(c, d), c += 2 * f, d += 2 * f, h.bw = f, h.node.style.marginLeft = -f, h.node.style.marginTop = -f);
		1 > c && (c = 1);
		1 > d && (d = 1);
		h.attr({
			x: a,
			y: b,
			width: c,
			height: d,
			rx: e,
			ry: e,
			"stroke-width": f
		});
		this.addToContainer(h.node, g);
		return h
	},
	image: function(a, b, c, d, e, f) {
		var g = new AmCharts.AmDObject("image", this);
		g.attr({
			x: b,
			y: c,
			width: d,
			height: e
		});
		this.R.path(g, a);
		this.addToContainer(g.node, f);
		return g
	},
	addToContainer: function(a, b) {
		b || (b = this.container);
		b.appendChild(a)
	},
	text: function(a, b, c) {
		return this.R.text(a, b, c)
	},
	path: function(a, b, c, d) {
		var e = new AmCharts.AmDObject("path", this);
		d || (d = "100,100");
		e.attr({
			cs: d
		});
		c ? e.attr({
			dd: a
		}) : e.attr({
			d: a
		});
		this.addToContainer(e.node, b);
		return e
	},
	set: function(a) {
		return this.R.set(a)
	},
	remove: function(a) {
		if (a) {
			var b = this.rBin;
			b.appendChild(a);
			b.innerHTML = ""
		}
	},
	renderFix: function() {
		var a = this.container,
		b = a.style,
		c;
		try {
			c = a.getScreenCTM() || a.createSVGMatrix()
		} catch(d) {
			c = a.createSVGMatrix()
		}
		a = 1 - c.e % 1;
		c = 1 - c.f % 1;
		0.5 < a && (a -= 1);
		0.5 < c && (c -= 1);
		a && (b.left = a + "px");
		c && (b.top = c + "px")
	},
	update: function() {
		this.R.update()
	}
});
AmCharts.AmDObject = AmCharts.Class({
	construct: function(a, b) {
		this.D = b;
		this.R = b.R;
		this.node = this.R.create(this, a);
		this.y = this.x = 0;
		this.scale = 1
	},
	attr: function(a) {
		this.R.attr(this, a);
		return this
	},
	getAttr: function(a) {
		return this.node.getAttribute(a)
	},
	setAttr: function(a, b) {
		this.R.setAttr(this, a, b);
		return this
	},
	clipRect: function(a, b, c, d) {
		this.R.clipRect(this, a, b, c, d)
	},
	translate: function(a, b, c, d) {
		d || (a = Math.round(a), b = Math.round(b));
		this.R.move(this, a, b, c);
		this.x = a;
		this.y = b;
		this.scale = c;
		this.angle && this.rotate(this.angle)
	},
	rotate: function(a, b) {
		this.R.rotate(this, a, b);
		this.angle = a
	},
	animate: function(a, b, c) {
		for (var d in a) if (a.hasOwnProperty(d)) {
			var e = d,
			f = a[d];
			c = AmCharts.getEffect(c);
			this.R.animate(this, e, f, b, c)
		}
	},
	push: function(a) {
		if (a) {
			var b = this.node;
			b.appendChild(a.node);
			var c = a.clipPath;
			c && b.appendChild(c); (a = a.grad) && b.appendChild(a)
		}
	},
	text: function(a) {
		this.R.setText(this, a)
	},
	remove: function() {
		this.R.remove(this)
	},
	clear: function() {
		var a = this.node;
		if (a.hasChildNodes()) for (; 1 <= a.childNodes.length;) a.removeChild(a.firstChild)
	},
	hide: function() {
		this.setAttr("visibility", "hidden")
	},
	show: function() {
		this.setAttr("visibility", "visible")
	},
	getBBox: function() {
		return this.R.getBBox(this)
	},
	toFront: function() {
		var a = this.node;
		if (a) {
			this.prevNextNode = a.nextSibling;
			var b = a.parentNode;
			b && b.appendChild(a)
		}
	},
	toPrevious: function() {
		var a = this.node;
		a && this.prevNextNode && (a = a.parentNode) && a.insertBefore(this.prevNextNode, null)
	},
	toBack: function() {
		var a = this.node;
		if (a) {
			this.prevNextNode = a.nextSibling;
			var b = a.parentNode;
			if (b) {
				var c = b.firstChild;
				c && b.insertBefore(a, c)
			}
		}
	},
	mouseover: function(a) {
		this.R.addListener(this, "mouseover", a);
		return this
	},
	mouseout: function(a) {
		this.R.addListener(this, "mouseout", a);
		return this
	},
	click: function(a) {
		this.R.addListener(this, "click", a);
		return this
	},
	dblclick: function(a) {
		this.R.addListener(this, "dblclick", a);
		return this
	},
	mousedown: function(a) {
		this.R.addListener(this, "mousedown", a);
		return this
	},
	mouseup: function(a) {
		this.R.addListener(this, "mouseup", a);
		return this
	},
	touchstart: function(a) {
		this.R.addListener(this, "touchstart", a);
		return this
	},
	touchend: function(a) {
		this.R.addListener(this, "touchend", a);
		return this
	},
	contextmenu: function(a) {
		this.node.addEventListener ? this.node.addEventListener("contextmenu", a, !0) : this.R.addListener(this, "contextmenu", a);
		return this
	},
	stop: function(a) { (a = this.animationX) && AmCharts.removeFromArray(this.R.animations, a); (a = this.animationY) && AmCharts.removeFromArray(this.R.animations, a)
	},
	length: function() {
		return this.node.childNodes.length
	},
	gradient: function(a, b, c) {
		this.R.gradient(this, a, b, c)
	},
	pattern: function(a, b) {
		a && this.R.pattern(this, a, b)
	}
});
AmCharts.VMLRenderer = AmCharts.Class({
	construct: function(a, b) {
		this.chart = b;
		this.D = a;
		this.cNames = {
			circle: "oval",
			rect: "roundrect",
			path: "shape"
		};
		this.styleMap = {
			x: "left",
			y: "top",
			width: "width",
			height: "height",
			"font-family": "fontFamily",
			"font-size": "fontSize",
			visibility: "visibility"
		}
	},
	create: function(a, b) {
		var c;
		if ("group" == b) c = document.createElement("div"),
		a.type = "div";
		else if ("text" == b) c = document.createElement("div"),
		a.type = "text";
		else if ("image" == b) c = document.createElement("img"),
		a.type = "image";
		else {
			a.type = "shape";
			a.shapeType = this.cNames[b];
			c = document.createElement("amvml:" + this.cNames[b]);
			var d = document.createElement("amvml:stroke");
			c.appendChild(d);
			a.stroke = d;
			var e = document.createElement("amvml:fill");
			c.appendChild(e);
			a.fill = e;
			e.className = "amvml";
			d.className = "amvml";
			c.className = "amvml"
		}
		c.style.position = "absolute";
		c.style.top = 0;
		c.style.left = 0;
		return c
	},
	path: function(a, b) {
		a.node.setAttribute("src", b)
	},
	setAttr: function(a, b, c) {
		if (void 0 !== c) {
			var d;
			8 === document.documentMode && (d = !0);
			var e = a.node,
			f = a.type,
			g = e.style;
			"r" == b && (g.width = 2 * c, g.height = 2 * c);
			"roundrect" != a.shapeType || "width" != b && "height" != b || (c -= 1);
			"cursor" == b && (g.cursor = c);
			"cx" == b && (g.left = c - AmCharts.removePx(g.width) / 2);
			"cy" == b && (g.top = c - AmCharts.removePx(g.height) / 2);
			var h = this.styleMap[b];
			void 0 !== h && (g[h] = c);
			"text" == f && ("text-anchor" == b && (a.anchor = c, h = e.clientWidth, "end" == c && (g.marginLeft = -h + "px"), "middle" == c && (g.marginLeft = -(h / 2) + "px", g.textAlign = "center"), "start" == c && (g.marginLeft = "0px")), "fill" == b && (g.color = c), "font-weight" == b && (g.fontWeight = c));
			if (g = a.children) for (h = 0; h < g.length; h++) g[h].setAttr(b, c);
			if ("shape" == f) {
				"cs" == b && (e.style.width = "100px", e.style.height = "100px", e.setAttribute("coordsize", c));
				"d" == b && e.setAttribute("path", this.svgPathToVml(c));
				"dd" == b && e.setAttribute("path", c);
				f = a.stroke;
				a = a.fill;
				"stroke" == b && (d ? f.color = c: f.setAttribute("color", c));
				"stroke-width" == b && (d ? f.weight = c: f.setAttribute("weight", c));
				"stroke-opacity" == b && (d ? f.opacity = c: f.setAttribute("opacity", c));
				"stroke-dasharray" == b && (g = "solid", 0 < c && 3 > c && (g = "dot"), 3 <= c && 6 >= c && (g = "dash"), 6 < c && (g = "longdash"), d ? f.dashstyle = g: f.setAttribute("dashstyle", g));
				if ("fill-opacity" == b || "opacity" == b) 0 === c ? d ? a.on = !1 : a.setAttribute("on", !1) : d ? a.opacity = c: a.setAttribute("opacity", c);
				"fill" == b && (d ? a.color = c: a.setAttribute("color", c));
				"rx" == b && (d ? e.arcSize = c + "%": e.setAttribute("arcsize", c + "%"))
			}
		}
	},
	attr: function(a, b) {
		for (var c in b) b.hasOwnProperty(c) && this.setAttr(a, c, b[c])
	},
	text: function(a, b, c) {
		var d = new AmCharts.AmDObject("text", this.D),
		e = d.node;
		e.style.whiteSpace = "pre";
		e.innerHTML = a;
		this.D.addToContainer(e, c);
		this.attr(d, b);
		return d
	},
	getBBox: function(a) {
		return this.getBox(a.node)
	},
	getBox: function(a) {
		var b = a.offsetLeft,
		c = a.offsetTop,
		d = a.offsetWidth,
		e = a.offsetHeight,
		f;
		if (a.hasChildNodes()) {
			var g, h, k;
			for (k = 0; k < a.childNodes.length; k++) {
				f = this.getBox(a.childNodes[k]);
				var l = f.x;
				isNaN(l) || (isNaN(g) ? g = l: l < g && (g = l));
				var m = f.y;
				isNaN(m) || (isNaN(h) ? h = m: m < h && (h = m));
				l = f.width + l;
				isNaN(l) || (d = Math.max(d, l));
				f = f.height + m;
				isNaN(f) || (e = Math.max(e, f))
			}
			0 > g && (b += g);
			0 > h && (c += h)
		}
		return {
			x: b,
			y: c,
			width: d,
			height: e
		}
	},
	setText: function(a, b) {
		var c = a.node;
		c && (c.innerHTML = b);
		this.setAttr(a, "text-anchor", a.anchor)
	},
	addListener: function(a, b, c) {
		a.node["on" + b] = c
	},
	move: function(a, b, c) {
		var d = a.node,
		e = d.style;
		"text" == a.type && (c -= AmCharts.removePx(e.fontSize) / 2 - 1);
		"oval" == a.shapeType && (b -= AmCharts.removePx(e.width) / 2, c -= AmCharts.removePx(e.height) / 2);
		a = a.bw;
		isNaN(a) || (b -= a, c -= a);
		isNaN(b) || isNaN(c) || (d.style.left = b + "px", d.style.top = c + "px")
	},
	svgPathToVml: function(a) {
		var b = a.split(" ");
		a = "";
		var c, d = Math.round,
		e;
		for (e = 0; e < b.length; e++) {
			var f = b[e],
			g = f.substring(0, 1),
			f = f.substring(1),
			h = f.split(","),
			k = d(h[0]) + "," + d(h[1]);
			"M" == g && (a += " m " + k);
			"L" == g && (a += " l " + k);
			"Z" == g && (a += " x e");
			if ("Q" == g) {
				var l = c.length,
				m = c[l - 1],
				n = h[0],
				p = h[1],
				k = h[2],
				r = h[3];
				c = d(c[l - 2] / 3 + 2 / 3 * n);
				m = d(m / 3 + 2 / 3 * p);
				n = d(2 / 3 * n + k / 3);
				p = d(2 / 3 * p + r / 3);
				a += " c " + c + "," + m + "," + n + "," + p + "," + k + "," + r
			}
			"A" == g && (a += " wa " + f);
			"B" == g && (a += " at " + f);
			c = h
		}
		return a
	},
	animate: function(a, b, c, d, e) {
		var f = a.node,
		g = this.chart;
		if ("translate" == b) {
			b = c.split(",");
			c = b[1];
			var h = f.offsetTop;
			g.animate(a, "left", f.offsetLeft, b[0], d, e, "px");
			g.animate(a, "top", h, c, d, e, "px")
		}
	},
	clipRect: function(a, b, c, d, e) {
		a = a.node;
		0 === b && 0 === c ? (a.style.width = d + "px", a.style.height = e + "px", a.style.overflow = "hidden") : a.style.clip = "rect(" + c + "px " + (b + d) + "px " + (c + e) + "px " + b + "px)"
	},
	rotate: function(a, b, c) {
		if (0 !== Number(b)) {
			var d = a.node;
			a = d.style;
			c || (c = this.getBGColor(d.parentNode));
			a.backgroundColor = c;
			a.paddingLeft = 1;
			c = b * Math.PI / 180;
			var e = Math.cos(c),
			f = Math.sin(c),
			g = AmCharts.removePx(a.left),
			h = AmCharts.removePx(a.top),
			k = d.offsetWidth,
			d = d.offsetHeight;
			b /= Math.abs(b);
			a.left = g + k / 2 - k / 2 * Math.cos(c) - b * d / 2 * Math.sin(c) + 3;
			a.top = h - b * k / 2 * Math.sin(c) + b * d / 2 * Math.sin(c);
			a.cssText = a.cssText + "; filter:progid:DXImageTransform.Microsoft.Matrix(M11='" + e + "', M12='" + -f + "', M21='" + f + "', M22='" + e + "', sizingmethod='auto expand');"
		}
	},
	getBGColor: function(a) {
		var b = "#FFFFFF";
		if (a.style) {
			var c = a.style.backgroundColor;
			"" !== c ? b = c: a.parentNode && (b = this.getBGColor(a.parentNode))
		}
		return b
	},
	set: function(a) {
		var b = new AmCharts.AmDObject("group", this.D);
		this.D.container.appendChild(b.node);
		if (a) {
			var c;
			for (c = 0; c < a.length; c++) b.push(a[c])
		}
		return b
	},
	gradient: function(a, b, c, d) {
		var e = "";
		"radialGradient" == b && (b = "gradientradial", c.reverse());
		"linearGradient" == b && (b = "gradient");
		var f;
		for (f = 0; f < c.length; f++) {
			var g = Math.round(100 * f / (c.length - 1)),
			e = e + (g + "% " + c[f]);
			f < c.length - 1 && (e += ",")
		}
		a = a.fill;
		90 == d ? d = 0 : 270 == d ? d = 180 : 180 == d ? d = 90 : 0 === d && (d = 270);
		8 === document.documentMode ? (a.type = b, a.angle = d) : (a.setAttribute("type", b), a.setAttribute("angle", d));
		e && (a.colors.value = e)
	},
	remove: function(a) {
		a.clipPath && this.D.remove(a.clipPath);
		this.D.remove(a.node)
	},
	disableSelection: function(a) {
		void 0 !== typeof a.onselectstart && (a.onselectstart = function() {
			return ! 1
		});
		a.style.cursor = "default"
	},
	pattern: function(a, b) {
		var c = a.fill;
		a.node.fillColor = "none";
		8 === document.documentMode ? (c.type = "tile", c.src = b.url) : (c.setAttribute("type", "tile"), c.setAttribute("src", b.url))
	},
	update: function() {}
});
AmCharts.SVGRenderer = AmCharts.Class({
	construct: function(a) {
		this.D = a;
		this.animations = []
	},
	create: function(a, b) {
		return document.createElementNS(AmCharts.SVG_NS, b)
	},
	attr: function(a, b) {
		for (var c in b) b.hasOwnProperty(c) && this.setAttr(a, c, b[c])
	},
	setAttr: function(a, b, c) {
		void 0 !== c && a.node.setAttribute(b, c)
	},
	animate: function(a, b, c, d, e) {
		var f = a.node;
		a["an_" + b] && AmCharts.removeFromArray(this.animations, a["an_" + b]);
		"translate" == b ? (f = (f = f.getAttribute("transform")) ? String(f).substring(10, f.length - 1) : "0,0", f = f.split(", ").join(" "), f = f.split(" ").join(","), 0 === f && (f = "0,0")) : f = Number(f.getAttribute(b));
		c = {
			obj: a,
			frame: 0,
			attribute: b,
			from: f,
			to: c,
			time: d,
			effect: e
		};
		this.animations.push(c);
		a["an_" + b] = c
	},
	update: function() {
		var a, b = this.animations;
		for (a = b.length - 1; 0 <= a; a--) {
			var c = b[a],
			d = 1E3 * c.time / AmCharts.updateRate,
			e = c.frame + 1,
			f = c.obj,
			g = c.attribute,
			h,
			k,
			l;
			e <= d ? (c.frame++, "translate" == g ? (h = c.from.split(","), g = Number(h[0]), h = Number(h[1]), isNaN(h) && (h = 0), k = c.to.split(","), l = Number(k[0]), k = Number(k[1]), l = 0 === l - g ? l: Math.round(AmCharts[c.effect](0, e, g, l - g, d)), c = 0 === k - h ? k: Math.round(AmCharts[c.effect](0, e, h, k - h, d)), g = "transform", c = "translate(" + l + "," + c + ")") : (k = Number(c.from), h = Number(c.to), l = h - k, c = AmCharts[c.effect](0, e, k, l, d), isNaN(c) && (c = h), 0 === l && this.animations.splice(a, 1)), this.setAttr(f, g, c)) : ("translate" == g ? (k = c.to.split(","), l = Number(k[0]), k = Number(k[1]), f.translate(l, k)) : (h = Number(c.to), this.setAttr(f, g, h)), this.animations.splice(a, 1))
		}
	},
	getBBox: function(a) {
		if (a = a.node) try {
			return a.getBBox()
		} catch(b) {}
		return {
			width: 0,
			height: 0,
			x: 0,
			y: 0
		}
	},
	path: function(a, b) {
		a.node.setAttributeNS(AmCharts.SVG_XLINK, "xlink:href", b)
	},
	clipRect: function(a, b, c, d, e) {
		var f = a.node,
		g = a.clipPath;
		g && this.D.remove(g);
		var h = f.parentNode;
		h && (f = document.createElementNS(AmCharts.SVG_NS, "clipPath"), g = AmCharts.getUniqueId(), f.setAttribute("id", g), this.D.rect(b, c, d, e, 0, 0, f), h.appendChild(f), b = "#", AmCharts.baseHref && !AmCharts.isIE && (b = window.location.href + b), this.setAttr(a, "clip-path", "url(" + b + g + ")"), this.clipPathC++, a.clipPath = f)
	},
	text: function(a, b, c) {
		var d = new AmCharts.AmDObject("text", this.D);
		a = String(a).split("\n");
		var e = b["font-size"],
		f;
		for (f = 0; f < a.length; f++) {
			var g = this.create(null, "tspan");
			g.appendChild(document.createTextNode(a[f]));
			g.setAttribute("y", (e + 2) * f + Math.round(e / 2));
			g.setAttribute("x", 0);
			d.node.appendChild(g)
		}
		d.node.setAttribute("y", Math.round(e / 2));
		this.attr(d, b);
		this.D.addToContainer(d.node, c);
		return d
	},
	setText: function(a, b) {
		var c = a.node;
		c && (c.removeChild(c.firstChild), c.appendChild(document.createTextNode(b)))
	},
	move: function(a, b, c, d) {
		b = "translate(" + b + "," + c + ")";
		d && (b = b + " scale(" + d + ")");
		this.setAttr(a, "transform", b)
	},
	rotate: function(a, b) {
		var c = a.node.getAttribute("transform"),
		d = "rotate(" + b + ")";
		c && (d = c + " " + d);
		this.setAttr(a, "transform", d)
	},
	set: function(a) {
		var b = new AmCharts.AmDObject("g", this.D);
		this.D.container.appendChild(b.node);
		if (a) {
			var c;
			for (c = 0; c < a.length; c++) b.push(a[c])
		}
		return b
	},
	addListener: function(a, b, c) {
		a.node["on" + b] = c
	},
	gradient: function(a, b, c, d) {
		var e = a.node,
		f = a.grad;
		f && this.D.remove(f);
		b = document.createElementNS(AmCharts.SVG_NS, b);
		f = AmCharts.getUniqueId();
		b.setAttribute("id", f);
		if (!isNaN(d)) {
			var g = 0,
			h = 0,
			k = 0,
			l = 0;
			90 == d ? k = 100 : 270 == d ? l = 100 : 180 == d ? g = 100 : 0 === d && (h = 100);
			b.setAttribute("x1", g + "%");
			b.setAttribute("x2", h + "%");
			b.setAttribute("y1", k + "%");
			b.setAttribute("y2", l + "%")
		}
		for (d = 0; d < c.length; d++) g = document.createElementNS(AmCharts.SVG_NS, "stop"),
		h = 100 * d / (c.length - 1),
		0 === d && (h = 0),
		g.setAttribute("offset", h + "%"),
		g.setAttribute("stop-color", c[d]),
		b.appendChild(g);
		e.parentNode.appendChild(b);
		c = "#";
		AmCharts.baseHref && !AmCharts.isIE && (c = window.location.href + c);
		e.setAttribute("fill", "url(" + c + f + ")");
		a.grad = b
	},
	pattern: function(a, b, c) {
		var d = a.node;
		isNaN(c) && (c = 1);
		var e = a.patternNode;
		e && this.D.remove(e);
		var e = document.createElementNS(AmCharts.SVG_NS, "pattern"),
		f = AmCharts.getUniqueId(),
		g = b;
		b.url && (g = b.url);
		var h = Number(b.width);
		isNaN(h) && (h = 4);
		var k = Number(b.height);
		isNaN(k) && (k = 4);
		h /= c;
		k /= c;
		c = b.x;
		isNaN(c) && (c = 0);
		var l = -Math.random() * Number(b.randomX);
		isNaN(l) || (c = l);
		l = b.y;
		isNaN(l) && (l = 0);
		b = -Math.random() * Number(b.randomY);
		isNaN(b) || (l = b);
		e.setAttribute("id", f);
		e.setAttribute("width", h);
		e.setAttribute("height", k);
		e.setAttribute("patternUnits", "userSpaceOnUse");
		e.setAttribute("xlink:href", g);
		this.D.image(g, 0, 0, h, k, e).translate(c, l);
		g = "#";
		AmCharts.baseHref && !AmCharts.isIE && (g = window.location.href + g);
		d.setAttribute("fill", "url(" + g + f + ")");
		a.patternNode = e;
		d.parentNode.appendChild(e)
	},
	remove: function(a) {
		a.clipPath && this.D.remove(a.clipPath);
		a.grad && this.D.remove(a.grad);
		a.patternNode && this.D.remove(a.patternNode);
		this.D.remove(a.node)
	}
});
AmCharts.AmDSet = AmCharts.Class({
	construct: function(a) {
		this.create("g")
	},
	attr: function(a) {
		this.R.attr(this.node, a)
	},
	move: function(a, b) {
		this.R.move(this.node, a, b)
	}
});
AmCharts.AmLegend = AmCharts.Class({
	construct: function(a) {
		this.cname = "AmLegend";
		this.createEvents("rollOverMarker", "rollOverItem", "rollOutMarker", "rollOutItem", "showItem", "hideItem", "clickMarker", "rollOverItem", "rollOutItem", "clickLabel");
		this.position = "bottom";
		this.borderColor = this.color = "#000000";
		this.borderAlpha = 0;
		this.markerLabelGap = 5;
		this.verticalGap = 10;
		this.align = "left";
		this.horizontalGap = 0;
		this.spacing = 10;
		this.markerDisabledColor = "#AAB3B3";
		this.markerType = "square";
		this.markerSize = 16;
		this.markerBorderThickness = this.markerBorderAlpha = 1;
		this.marginBottom = this.marginTop = 0;
		this.marginLeft = this.marginRight = 20;
		this.autoMargins = !0;
		this.valueWidth = 50;
		this.switchable = !0;
		this.switchType = "x";
		this.switchColor = "#FFFFFF";
		this.rollOverColor = "#CC0000";
		this.reversedOrder = !1;
		this.labelText = "[[title]]";
		this.valueText = "[[value]]";
		this.useMarkerColorForLabels = !1;
		this.rollOverGraphAlpha = 1;
		this.textClickEnabled = !1;
		this.equalWidths = !0;
		this.dateFormat = "DD-MM-YYYY";
		this.backgroundColor = "#FFFFFF";
		this.backgroundAlpha = 0;
		this.useGraphSettings = !1;
		this.showEntries = !0;
		AmCharts.applyTheme(this, a, this.cname)
	},
	setData: function(a) {
		this.legendData = a;
		this.invalidateSize()
	},
	invalidateSize: function() {
		this.destroy();
		this.entries = [];
		this.valueLabels = []; (AmCharts.ifArray(this.legendData) || AmCharts.ifArray(this.data)) && this.drawLegend()
	},
	drawLegend: function() {
		var a = this.chart,
		b = this.position,
		c = this.width,
		d = a.divRealWidth,
		e = a.divRealHeight,
		f = this.div,
		g = this.legendData;
		this.data && (g = this.data);
		isNaN(this.fontSize) && (this.fontSize = a.fontSize);
		if ("right" == b || "left" == b) this.maxColumns = 1,
		this.autoMargins && (this.marginLeft = this.marginRight = 10);
		else if (this.autoMargins) {
			this.marginRight = a.marginRight;
			this.marginLeft = a.marginLeft;
			var h = a.autoMarginOffset;
			"bottom" == b ? (this.marginBottom = h, this.marginTop = 0) : (this.marginTop = h, this.marginBottom = 0)
		}
		c = void 0 !== c ? AmCharts.toCoordinate(c, d) : a.realWidth;
		"outside" == b ? (c = f.offsetWidth, e = f.offsetHeight, f.clientHeight && (c = f.clientWidth, e = f.clientHeight)) : (f.style.width = c + "px", f.className = "amChartsLegend");
		this.divWidth = c;
		this.container = new AmCharts.AmDraw(f, c, e, a);
		this.lx = 0;
		this.ly = 8;
		b = this.markerSize;
		b > this.fontSize && (this.ly = b / 2 - 1);
		0 < b && (this.lx += b + this.markerLabelGap);
		this.titleWidth = 0;
		if (b = this.title) a = AmCharts.text(this.container, b, this.color, a.fontFamily, this.fontSize, "start", !0),
		a.translate(this.marginLeft, this.marginTop + this.verticalGap + this.ly + 1),
		a = a.getBBox(),
		this.titleWidth = a.width + 15,
		this.titleHeight = a.height + 6;
		this.index = this.maxLabelWidth = 0;
		if (this.showEntries) {
			for (a = 0; a < g.length; a++) this.createEntry(g[a]);
			for (a = this.index = 0; a < g.length; a++) this.createValue(g[a])
		}
		this.arrangeEntries();
		this.updateValues()
	},
	arrangeEntries: function() {
		var a = this.position,
		b = this.marginLeft + this.titleWidth,
		c = this.marginRight,
		d = this.marginTop,
		e = this.marginBottom,
		f = this.horizontalGap,
		g = this.div,
		h = this.divWidth,
		k = this.maxColumns,
		l = this.verticalGap,
		m = this.spacing,
		n = h - c - b,
		p = 0,
		r = 0,
		u = this.container,
		q = u.set();
		this.set = q;
		u = u.set();
		q.push(u);
		var t = this.entries,
		v, s;
		for (s = 0; s < t.length; s++) {
			v = t[s].getBBox();
			var w = v.width;
			w > p && (p = w);
			v = v.height;
			v > r && (r = v)
		}
		var A = w = 0,
		C = f;
		for (s = 0; s < t.length; s++) {
			var y = t[s];
			this.reversedOrder && (y = t[t.length - s - 1]);
			v = y.getBBox();
			var z;
			this.equalWidths ? z = f + A * (p + m + this.markerLabelGap) : (z = C, C = C + v.width + f + m);
			z + v.width > n && 0 < s && 0 !== A && (w++, A = 0, z = f, C = z + v.width + f + m);
			y.translate(z, (r + l) * w);
			A++; ! isNaN(k) && A >= k && (A = 0, w++);
			u.push(y)
		}
		v = u.getBBox();
		k = v.height + 2 * l - 1;
		"left" == a || "right" == a ? (h = v.width + 2 * f, g.style.width = h + b + c + "px") : h = h - b - c - 1;
		c = AmCharts.polygon(this.container, [0, h, h, 0], [0, 0, k, k], this.backgroundColor, this.backgroundAlpha, 1, this.borderColor, this.borderAlpha);
		q.push(c);
		q.translate(b, d);
		c.toBack();
		b = f;
		if ("top" == a || "bottom" == a || "absolute" == a || "outside" == a)"center" == this.align ? b = f + (h - v.width) / 2 : "right" == this.align && (b = f + h - v.width);
		u.translate(b, l + 1);
		this.titleHeight > k && (k = this.titleHeight);
		a = k + d + e + 1;
		0 > a && (a = 0);
		g.style.height = Math.round(a) + "px"
	},
	createEntry: function(a) {
		if (!1 !== a.visibleInLegend) {
			var b = this.chart,
			c = a.markerType;
			c || (c = this.markerType);
			var d = a.color,
			e = a.alpha;
			a.legendKeyColor && (d = a.legendKeyColor());
			a.legendKeyAlpha && (e = a.legendKeyAlpha());
			var f; ! 0 === a.hidden && (f = d = this.markerDisabledColor);
			var g = a.pattern,
			h = a.customMarker;
			h || (h = this.customMarker);
			var k = this.container,
			l = this.markerSize,
			m = 0,
			n = 0,
			p = l / 2;
			if (this.useGraphSettings) if (m = a.type, this.switchType = void 0, "line" == m || "step" == m || "smoothedLine" == m || "ohlc" == m) g = k.set(),
			a.hidden || (d = a.lineColorR, f = a.bulletBorderColorR),
			n = AmCharts.line(k, [0, 2 * l], [l / 2, l / 2], d, a.lineAlpha, a.lineThickness, a.dashLength),
			g.push(n),
			a.bullet && (a.hidden || (d = a.bulletColorR), n = AmCharts.bullet(k, a.bullet, a.bulletSize, d, a.bulletAlpha, a.bulletBorderThickness, f, a.bulletBorderAlpha)) && (n.translate(l + 1, l / 2), g.push(n)),
			p = 0,
			m = l,
			n = l / 3;
			else {
				var r;
				a.getGradRotation && (r = a.getGradRotation());
				m = a.fillColorsR; ! 0 === a.hidden && (m = d);
				if (g = this.createMarker("rectangle", m, a.fillAlphas, a.lineThickness, d, a.lineAlpha, r, g)) p = l,
				g.translate(p, l / 2);
				m = l
			} else h ? (b.path && (h = b.path + h), g = k.image(h, 0, 0, l, l)) : (g = this.createMarker(c, d, e, void 0, void 0, void 0, void 0, g)) && g.translate(l / 2, l / 2);
			this.addListeners(g, a);
			k = k.set([g]);
			this.switchable && k.setAttr("cursor", "pointer"); (f = this.switchType) && "none" != f && ("x" == f ? (r = this.createX(), r.translate(l / 2, l / 2)) : r = this.createV(), r.dItem = a, !0 !== a.hidden ? "x" == f ? r.hide() : r.show() : "x" != f && r.hide(), this.switchable || r.hide(), this.addListeners(r, a), a.legendSwitch = r, k.push(r));
			f = this.color;
			a.showBalloon && this.textClickEnabled && void 0 !== this.selectedColor && (f = this.selectedColor);
			this.useMarkerColorForLabels && (f = d); ! 0 === a.hidden && (f = this.markerDisabledColor);
			d = AmCharts.massReplace(this.labelText, {
				"[[title]]": a.title
			});
			r = this.fontSize;
			g && l <= r && g.translate(p, l / 2 + this.ly - r / 2 + (r + 2 - l) / 2 - n);
			var u;
			d && (d = AmCharts.fixBrakes(d), a.legendTextReal = d, u = AmCharts.text(this.container, d, f, b.fontFamily, r, "start"), u.translate(this.lx + m, this.ly), k.push(u), b = u.getBBox().width, this.maxLabelWidth < b && (this.maxLabelWidth = b));
			this.entries[this.index] = k;
			a.legendEntry = this.entries[this.index];
			a.legendLabel = u;
			this.index++
		}
	},
	addListeners: function(a, b) {
		var c = this;
		a && a.mouseover(function() {
			c.rollOverMarker(b)
		}).mouseout(function() {
			c.rollOutMarker(b)
		}).click(function() {
			c.clickMarker(b)
		})
	},
	rollOverMarker: function(a) {
		this.switchable && this.dispatch("rollOverMarker", a);
		this.dispatch("rollOverItem", a)
	},
	rollOutMarker: function(a) {
		this.switchable && this.dispatch("rollOutMarker", a);
		this.dispatch("rollOutItem", a)
	},
	clickMarker: function(a) {
		this.switchable ? !0 === a.hidden ? this.dispatch("showItem", a) : this.dispatch("hideItem", a) : this.textClickEnabled && this.dispatch("clickMarker", a)
	},
	rollOverLabel: function(a) {
		a.hidden || (this.textClickEnabled && a.legendLabel && a.legendLabel.attr({
			fill: this.rollOverColor
		}), this.dispatch("rollOverItem", a))
	},
	rollOutLabel: function(a) {
		if (!a.hidden) {
			if (this.textClickEnabled && a.legendLabel) {
				var b = this.color;
				void 0 !== this.selectedColor && a.showBalloon && (b = this.selectedColor);
				this.useMarkerColorForLabels && (b = a.lineColor, void 0 === b && (b = a.color));
				a.legendLabel.attr({
					fill: b
				})
			}
			this.dispatch("rollOutItem", a)
		}
	},
	clickLabel: function(a) {
		this.textClickEnabled ? a.hidden || this.dispatch("clickLabel", a) : this.switchable && (!0 === a.hidden ? this.dispatch("showItem", a) : this.dispatch("hideItem", a))
	},
	dispatch: function(a, b) {
		this.fire(a, {
			type: a,
			dataItem: b,
			target: this,
			chart: this.chart
		})
	},
	createValue: function(a) {
		var b = this,
		c = b.fontSize;
		if (!1 !== a.visibleInLegend) {
			var d = b.maxLabelWidth;
			b.equalWidths || (b.valueAlign = "left");
			"left" == b.valueAlign && (d = a.legendEntry.getBBox().width);
			var e = d;
			if (b.valueText && 0 < b.valueWidth) {
				var f = b.color;
				b.useMarkerColorForValues && (f = a.color, a.legendKeyColor && (f = a.legendKeyColor())); ! 0 === a.hidden && (f = b.markerDisabledColor);
				var g = b.valueText,
				d = d + b.lx + b.markerLabelGap + b.valueWidth,
				h = "end";
				"left" == b.valueAlign && (d -= b.valueWidth, h = "start");
				f = AmCharts.text(b.container, g, f, b.chart.fontFamily, c, h);
				f.translate(d, b.ly);
				b.entries[b.index].push(f);
				e += b.valueWidth + 2 * b.markerLabelGap;
				f.dItem = a;
				b.valueLabels.push(f)
			}
			b.index++;
			f = b.markerSize;
			f < c + 7 && (f = c + 7, AmCharts.VML && (f += 3));
			c = b.container.rect(b.markerSize, 0, e, f, 0, 0).attr({
				stroke: "none",
				fill: "#ffffff",
				"fill-opacity": 0.005
			});
			c.dItem = a;
			b.entries[b.index - 1].push(c);
			c.mouseover(function() {
				b.rollOverLabel(a)
			}).mouseout(function() {
				b.rollOutLabel(a)
			}).click(function() {
				b.clickLabel(a)
			})
		}
	},
	createV: function() {
		var a = this.markerSize;
		return AmCharts.polygon(this.container, [a / 5, a / 2, a - a / 5, a / 2], [a / 3, a - a / 5, a / 5, a / 1.7], this.switchColor)
	},
	createX: function() {
		var a = (this.markerSize - 4) / 2,
		b = {
			stroke: this.switchColor,
			"stroke-width": 3
		},
		c = this.container,
		d = AmCharts.line(c, [ - a, a], [ - a, a]).attr(b),
		a = AmCharts.line(c, [ - a, a], [a, -a]).attr(b);
		return this.container.set([d, a])
	},
	createMarker: function(a, b, c, d, e, f, g, h) {
		var k = this.markerSize,
		l = this.container;
		e || (e = this.markerBorderColor);
		e || (e = b);
		isNaN(d) && (d = this.markerBorderThickness);
		isNaN(f) && (f = this.markerBorderAlpha);
		return AmCharts.bullet(l, a, k, b, c, d, e, f, k, g, h)
	},
	validateNow: function() {
		this.invalidateSize()
	},
	updateValues: function() {
		var a = this.valueLabels,
		b = this.chart,
		c, d = this.data;
		for (c = 0; c < a.length; c++) {
			var e = a[c],
			f = e.dItem,
			g = " ";
			if (d) f.value ? e.text(f.value) : e.text("");
			else {
				if (void 0 !== f.type) {
					var h = f.currentDataItem,
					k = this.periodValueText;
					f.legendPeriodValueText && (k = f.legendPeriodValueText);
					h ? (g = this.valueText, f.legendValueText && (g = f.legendValueText), g = b.formatString(g, h)) : k && (g = b.formatPeriodString(k, f))
				} else g = b.formatString(this.valueText, f); (h = f.legendLabel) && h.text(f.legendTextReal);
				e.text(g)
			}
		}
	},
	renderFix: function() {
		if (!AmCharts.VML) {
			var a = this.container;
			a && a.renderFix()
		}
	},
	destroy: function() {
		this.div.innerHTML = "";
		AmCharts.remove(this.set)
	}
});
AmCharts.formatMilliseconds = function(a, b) {
	if ( - 1 != a.indexOf("fff")) {
		var c = b.getMilliseconds(),
		d = String(c);
		10 > c && (d = "00" + c);
		10 <= c && 100 > c && (d = "0" + c);
		a = a.replace(/fff/g, d)
	}
	return a
};
AmCharts.extractPeriod = function(a) {
	var b = AmCharts.stripNumbers(a),
	c = 1;
	b != a && (c = Number(a.slice(0, a.indexOf(b))));
	return {
		period: b,
		count: c
	}
};
AmCharts.resetDateToMin = function(a, b, c, d) {
	void 0 === d && (d = 1);
	var e, f, g, h, k, l, m;
	AmCharts.useUTC ? (e = a.getUTCFullYear(), f = a.getUTCMonth(), g = a.getUTCDate(), h = a.getUTCHours(), k = a.getUTCMinutes(), l = a.getUTCSeconds(), m = a.getUTCMilliseconds(), a = a.getUTCDay()) : (e = a.getFullYear(), f = a.getMonth(), g = a.getDate(), h = a.getHours(), k = a.getMinutes(), l = a.getSeconds(), m = a.getMilliseconds(), a = a.getDay());
	switch (b) {
	case "YYYY":
		e = Math.floor(e / c) * c;
		f = 0;
		g = 1;
		m = l = k = h = 0;
		break;
	case "MM":
		f = Math.floor(f / c) * c;
		g = 1;
		m = l = k = h = 0;
		break;
	case "WW":
		0 === a && 0 < d && (a = 7);
		g = g - a + d;
		m = l = k = h = 0;
		break;
	case "DD":
		m = l = k = h = 0;
		break;
	case "hh":
		h = Math.floor(h / c) * c;
		m = l = k = 0;
		break;
	case "mm":
		k = Math.floor(k / c) * c;
		m = l = 0;
		break;
	case "ss":
		l = Math.floor(l / c) * c;
		m = 0;
		break;
	case "fff":
		m = Math.floor(m / c) * c
	}
	AmCharts.useUTC ? (a = new Date, a.setUTCFullYear(e, f, g), a.setUTCHours(h, k, l, m)) : a = new Date(e, f, g, h, k, l, m);
	return a
};
AmCharts.getPeriodDuration = function(a, b) {
	void 0 === b && (b = 1);
	var c;
	switch (a) {
	case "YYYY":
		c = 316224E5;
		break;
	case "MM":
		c = 26784E5;
		break;
	case "WW":
		c = 6048E5;
		break;
	case "DD":
		c = 864E5;
		break;
	case "hh":
		c = 36E5;
		break;
	case "mm":
		c = 6E4;
		break;
	case "ss":
		c = 1E3;
		break;
	case "fff":
		c = 1
	}
	return c * b
};
AmCharts.intervals = {
	s: {
		nextInterval: "ss",
		contains: 1E3
	},
	ss: {
		nextInterval: "mm",
		contains: 60,
		count: 0
	},
	mm: {
		nextInterval: "hh",
		contains: 60,
		count: 1
	},
	hh: {
		nextInterval: "DD",
		contains: 24,
		count: 2
	},
	DD: {
		nextInterval: "",
		contains: Infinity,
		count: 3
	}
};
AmCharts.getMaxInterval = function(a, b) {
	var c = AmCharts.intervals;
	return a >= c[b].contains ? (a = Math.round(a / c[b].contains), b = c[b].nextInterval, AmCharts.getMaxInterval(a, b)) : "ss" == b ? c[b].nextInterval: b
};
AmCharts.dayNames = "Sunday Monday Tuesday Wednesday Thursday Friday Saturday".split(" ");
AmCharts.shortDayNames = "Sun Mon Tue Wed Thu Fri Sat".split(" ");
AmCharts.monthNames = "January February March April May June July August September October November December".split(" ");
AmCharts.shortMonthNames = "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" ");
AmCharts.getWeekNumber = function(a) {
	a = new Date(a);
	a.setHours(0, 0, 0);
	a.setDate(a.getDate() + 4 - (a.getDay() || 7));
	var b = new Date(a.getFullYear(), 0, 1);
	return Math.ceil(((a - b) / 864E5 + 1) / 7)
};
AmCharts.stringToDate = function(a, b) {
	var c = {},
	d = [{
		pattern: "YYYY",
		period: "year"
	},
	{
		pattern: "YY",
		period: "year"
	},
	{
		pattern: "MM",
		period: "month"
	},
	{
		pattern: "M",
		period: "month"
	},
	{
		pattern: "DD",
		period: "date"
	},
	{
		pattern: "D",
		period: "date"
	},
	{
		pattern: "JJ",
		period: "hours"
	},
	{
		pattern: "J",
		period: "hours"
	},
	{
		pattern: "HH",
		period: "hours"
	},
	{
		pattern: "H",
		period: "hours"
	},
	{
		pattern: "KK",
		period: "hours"
	},
	{
		pattern: "K",
		period: "hours"
	},
	{
		pattern: "LL",
		period: "hours"
	},
	{
		pattern: "L",
		period: "hours"
	},
	{
		pattern: "NN",
		period: "minutes"
	},
	{
		pattern: "N",
		period: "minutes"
	},
	{
		pattern: "SS",
		period: "seconds"
	},
	{
		pattern: "S",
		period: "seconds"
	},
	{
		pattern: "QQQ",
		period: "milliseconds"
	},
	{
		pattern: "QQ",
		period: "milliseconds"
	},
	{
		pattern: "Q",
		period: "milliseconds"
	}],
	e = !0,
	f = b.indexOf("AA"); - 1 != f && (a.substr(f, 2), "pm" == a.toLowerCase && (e = !1));
	var f = b,
	g, h, k;
	for (k = 0; k < d.length; k++) h = d[k].period,
	c[h] = 0,
	"date" == h && (c[h] = 1);
	for (k = 0; k < d.length; k++) if (g = d[k].pattern, h = d[k].period, -1 != b.indexOf(g)) {
		var l = AmCharts.getFromDateString(g, a, f);
		b = b.replace(g, "");
		if ("KK" == g || "K" == g || "LL" == g || "L" == g) e || (l += 12);
		c[h] = l
	}
	return new Date(c.year, c.month, c.date, c.hours, c.minutes, c.seconds, c.milliseconds)
};
AmCharts.getFromDateString = function(a, b, c) {
	c = c.indexOf(a);
	b = b.substr(c, a.length);
	"0" == b.charAt(0) && (b = b.substr(1, b.length - 1));
	b = Number(b);
	isNaN(b) && (b = 0); - 1 != a.indexOf("M") && b--;
	return b
};
AmCharts.formatDate = function(a, b) {
	var c, d, e, f, g, h, k, l, m = AmCharts.getWeekNumber(a);
	AmCharts.useUTC ? (c = a.getUTCFullYear(), d = a.getUTCMonth(), e = a.getUTCDate(), f = a.getUTCDay(), g = a.getUTCHours(), h = a.getUTCMinutes(), k = a.getUTCSeconds(), l = a.getUTCMilliseconds()) : (c = a.getFullYear(), d = a.getMonth(), e = a.getDate(), f = a.getDay(), g = a.getHours(), h = a.getMinutes(), k = a.getSeconds(), l = a.getMilliseconds());
	var n = String(c).substr(2, 2),
	p = d + 1;
	9 > d && (p = "0" + p);
	var r = e;
	10 > e && (r = "0" + e);
	var u = "0" + f;
	b = b.replace(/W/g, m);
	m = g;
	24 == m && (m = 0);
	var q = m;
	10 > q && (q = "0" + q);
	b = b.replace(/JJ/g, q);
	b = b.replace(/J/g, m);
	m = g;
	0 === m && (m = 24);
	q = m;
	10 > q && (q = "0" + q);
	b = b.replace(/HH/g, q);
	b = b.replace(/H/g, m);
	m = g;
	11 < m && (m -= 12);
	q = m;
	10 > q && (q = "0" + q);
	b = b.replace(/KK/g, q);
	b = b.replace(/K/g, m);
	m = g;
	0 === m && (m = 12);
	12 < m && (m -= 12);
	q = m;
	10 > q && (q = "0" + q);
	b = b.replace(/LL/g, q);
	b = b.replace(/L/g, m);
	m = h;
	10 > m && (m = "0" + m);
	b = b.replace(/NN/g, m);
	b = b.replace(/N/g, h);
	h = k;
	10 > h && (h = "0" + h);
	b = b.replace(/SS/g, h);
	b = b.replace(/S/g, k);
	k = l;
	10 > k && (k = "00" + k);
	100 > k && (k = "0" + k);
	h = l;
	10 > h && (h = "00" + h);
	b = b.replace(/QQQ/g, k);
	b = b.replace(/QQ/g, h);
	b = b.replace(/Q/g, l);
	b = 12 > g ? b.replace(/A/g, "am") : b.replace(/A/g, "pm");
	b = b.replace(/YYYY/g, "@IIII@");
	b = b.replace(/YY/g, "@II@");
	b = b.replace(/MMMM/g, "@XXXX@");
	b = b.replace(/MMM/g, "@XXX@");
	b = b.replace(/MM/g, "@XX@");
	b = b.replace(/M/g, "@X@");
	b = b.replace(/DD/g, "@RR@");
	b = b.replace(/D/g, "@R@");
	b = b.replace(/EEEE/g, "@PPPP@");
	b = b.replace(/EEE/g, "@PPP@");
	b = b.replace(/EE/g, "@PP@");
	b = b.replace(/E/g, "@P@");
	b = b.replace(/@IIII@/g, c);
	b = b.replace(/@II@/g, n);
	b = b.replace(/@XXXX@/g, AmCharts.monthNames[d]);
	b = b.replace(/@XXX@/g, AmCharts.shortMonthNames[d]);
	b = b.replace(/@XX@/g, p);
	b = b.replace(/@X@/g, d + 1);
	b = b.replace(/@RR@/g, r);
	b = b.replace(/@R@/g, e);
	b = b.replace(/@PPPP@/g, AmCharts.dayNames[f]);
	b = b.replace(/@PPP@/g, AmCharts.shortDayNames[f]);
	b = b.replace(/@PP@/g, u);
	return b = b.replace(/@P@/g, f)
};
AmCharts.changeDate = function(a, b, c, d, e) {
	var f = -1;
	void 0 === d && (d = !0);
	void 0 === e && (e = !1); ! 0 === d && (f = 1);
	switch (b) {
	case "YYYY":
		a.setFullYear(a.getFullYear() + c * f);
		d || e || a.setDate(a.getDate() + 1);
		break;
	case "MM":
		b = a.getMonth();
		a.setMonth(a.getMonth() + c * f);
		a.getMonth() > b + c * f && a.setDate(a.getDate() - 1);
		d || e || a.setDate(a.getDate() + 1);
		break;
	case "DD":
		a.setDate(a.getDate() + c * f);
		break;
	case "WW":
		a.setDate(a.getDate() + c * f * 7);
		break;
	case "hh":
		a.setHours(a.getHours() + c * f);
		break;
	case "mm":
		a.setMinutes(a.getMinutes() + c * f);
		break;
	case "ss":
		a.setSeconds(a.getSeconds() + c * f);
		break;
	case "fff":
		a.setMilliseconds(a.getMilliseconds() + c * f)
	}
	return a
};