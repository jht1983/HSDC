function $(a) {
    return document.getElementById(a)
}
var ylt = ylt || {};
ylt.flow = ylt.flow || {},
ylt.winCommand = ylt.winCommand || {},
yltWinCommand = ylt.winCommand = {
    getScrollLeft: function() {
        return document.body.scrollLeft + document.documentElement.scrollLeft
    },
    getScrollTop: function() {
        return document.body.scrollTop + document.documentElement.scrollTop
    }
},
yltFlow = ylt.flow = {
    drawPaper: null,
    iDrawType: 0,
    bIsDel: !1,
    objLineTo: null,
    iCurDragPanelX: 0,
    iCurDragPanelY: 0,
    iArrowWidth: 5,
    iArrowHeight: 3,
    f_LineWidth: 1,
    objCurDashLine: null,
    objCurDrawLineArrow: null,
    bDrawLineIng: !1,
    bIsGenerLine: !1,
    objEndNode: null,
    objStartNode: null,
    iNodeWidth: 100,
    iNodeHeight: 50,
    iLineOffset: 20,
    objCurGraph: {},
    objCurSelGraph: null,
    strNodeColor: ["#19c9ee", "#f7e043", "#be80fe", "#f8d993", "#edd8bf", "#f96665", "white"],
    strNodeBorderColor: ["#000", "#000", "#000", "#000", "#000", "#000", "#000"],
    arrStrAttrNames: ["S_AUDIT_TABLECONTROL", "S_AUDIT_TYPE", "S_AUDIT_DESC", "S_AUDIT_TZXX", "S_AUDIT_ROLE", "S_AUDIT_BRANCH", "S_AUDIT_USER", "S_AUDIT_YQTS", "S_AUDIT_YQTSCL", "S_AUDIT_THJD", "NO_AUDIT_SHOW", "NO_AUDIT_USER"],
    arrStrAttrIds: ["S_AUDIT_TABLECONTROL", "S_AUDIT_TYPE", "S_AUDIT_DESC", "S_AUDIT_TZXX", "S_AUDIT_ROLE", "S_AUDIT_BRANCH", "S_AUDIT_USER", "S_AUDIT_YQTS", "S_AUDIT_YQTSCL", "S_AUDIT_THJD", "NO_AUDIT_SHOW", "NO_AUDIT_USER"],
    iNodeId: 0,
    strFlowId: "",
    addNodeByEvent: function(a) {
        if (0 != this.iDrawType) {
            this.iNodeId++;
            var b = 4;
            2 == this.iDrawType && (b = 3),
            this.addNode(this.iNodeId + "", a.clientX, a.clientY, this.iNodeWidth, this.iNodeHeight, "", this.iDrawType, b)
        }
    },
    generTranRect: function(a, b, c, d) {
        var e = "M " + (a + c / 2) + "," + b + " L" + a + "," + (b + d / 2) + " L" + (a + c / 2) + "," + (b + d) + " L" + (a + c) + "," + (b + d / 2) + " L" + (a + c / 2) + "," + b;
        return e
    },
    generTranRectByPos: function(a, b, c) {
        return a.attr("x", b),
        a.attr("y", c),
        this.generTranRect(b, c, a.attr("width"), a.attr("height"))
    },
    viewAttr: function() {
        var a = event.srcElement;
        return alert(a.type),
        !1
    },
    generText: function(a, b, c) {
        return "" == a && (a = b),
        this.strAudNodeId == c && (a += "\n" + this.strAudUser),
        a
    },
    addNode: function(a, b, c, d, e, f, g, h) {
        var i, j, k, l = null,
        m = b + d / 2,
        n = c + e / 2;
        switch (g) {
        case 1: //活动
            f = this.generText(f, "\u6d3b\u52a8", a),
            l = this.drawPaper.rect(b, c, d, e, 5).attr({
                fill: this.strNodeColor[h],
                stroke: this.strNodeBorderColor[h],
                "stroke-width": this.f_LineWidth,
                cursor: "pointer"
            });
            break;
        case 2: //网关
            f = this.generText(f, "\u7f51\u5173", a),
            i = this.generTranRect(b, c, d, e),
            l = this.drawPaper.path(i).attr({
                fill: this.strNodeColor[h],
                stroke: this.strNodeBorderColor[h],
                "stroke-width": this.f_LineWidth,
                cursor: "pointer"
            }),
            l.attr("x", b),
            l.attr("y", c),
            l.attr("width", d),
            l.attr("height", e);
            break;
        case 3: //开始
            j = "#57aa57",
            "" != this.strAudNodeId && (j = "white"),
            f = this.generText(f, "\u5f00\u59cbʼ", a),
            l = this.drawPaper.circle(b, c, e / 2, 5).attr({
                fill: j,
                stroke: this.strNodeBorderColor[h],
                "stroke-width": this.f_LineWidth,
                cursor: "pointer"
            }),
            l.attr("x", b - e / 2),
            l.attr("y", c - e / 2),
            l.attr("width", e),
            l.attr("height", e),
            m = b,
            n = c;
            break;
        case 4: //结束
            j = "#f29494",
            "" != this.strAudNodeId && (j = "white"),
            f = this.generText(f, "\u7ed3\u675f", a),
            l = this.drawPaper.circle(b, c, e / 2, 5).attr({
                fill: j,
                stroke: this.strNodeBorderColor[h],
                "stroke-width": this.f_LineWidth,
                cursor: "pointer"
            }),
            l.attr("x", b - e / 2),
            l.attr("y", c - e / 2),
            l.attr("width", e),
            l.attr("height", e),
            m = b,
            n = c;
            break;
        case 5: //子流程
            j = "#bbdbfc",
            "" != this.strAudNodeId && (j = "white"),
            f = this.generText(f, "\u5b50\u6d41\u7a0b", a),
            l = this.drawPaper.circle(b, c, e / 2, 5).attr({
                fill: j,
                stroke: this.strNodeBorderColor[h],
                "stroke-width": this.f_LineWidth,
                cursor: "pointer"
            }),
            l.attr("x", b - e / 2),
            l.attr("y", c - e / 2),
            l.attr("width", e),
            l.attr("height", e),
            m = b,
            n = c
        }
        return l.drag(this.dragMove, this.dragStart, this.dragUp),
        l.mouseover(this.lineMoseOver),
        l.mouseout(this.lineMoseOut),
        l.dblclick(this.showAttrs),
        l.click(this.graphClick),
        k = this.drawPaper.text(m, n, f).attr({
            "font-family": "\u9ed1\u4f53",
            "font-size": 15
        }),
        k.data("objParRect", l),
        k.click(this.graphClick),
        k.dblclick(this.editTextCaption),
        l.data("id", a),
        l.data("iType", g),
        l.data("text", f),
        l.data("rectText", k),
        l.data("iIndexColor", h),
        this.objCurGraph[a] = l,
        l
    },
    objCurEditText: null,
    editTextCaption: function() {
        var a, b, c = this,
        d = this.data("objParRect");
        null != d && (c = d),
        ylt.flow.objCurEditText = c,
        a = document.getElementById("sys_input_caption_div"),
        null == a && (a = document.createElement("div"), a.setAttribute("id", "sys_input_caption_div"), a.style.position = "absolute", a.style.backgroundColor = "white", a.style.fontFamily = "\u9ed1\u4f53", a.style.fontSize = "12px", a.style.zIndex = 1e3, a.innerHTML = "<textarea id='sys_input_caption_div_input' style='width:100%;height:100%;border:1px solid gray;overflow:hidden;'></textarea>", document.body.appendChild(a), document.getElementById("sys_input_caption_div_input").onblur = function() {
            ylt.flow.changeNodeText()
        }),
        a.style.display = "",
        b = document.getElementById("sys_input_caption_div_input"),
        b.value = c.data("text"),
        a.style.left = c.attr("x") + "px",
        a.style.top = c.attr("y") + "px",
        a.style.width = c.attr("width") + "px",
        a.style.height = c.attr("height") + "px",
        b.focus()
    },
    changeNodeText: function() {
        var a, b, c;
        null != this.objCurEditText && (a = document.getElementById("sys_input_caption_div"), null != a && (b = document.getElementById("sys_input_caption_div_input").value, this.objCurEditText.data("text", b), c = this.objCurEditText.data("rectText"), c.attr({
            text: b
        }), a.style.display = "none", this.objCurEditText = null))
    },
    generPointCoordinate: function(a) {
        var b = a.attr("x"),
        c = a.attr("y"),
        d = a.attr("width"),
        e = a.attr("height"),
        f = b + d / 2,
        g = c + e / 2;
        a.data("topCoordinate", {
            x: f,
            y: c,
            pointtype: "top"
        }),
        a.data("leftCoordinate", {
            x: b,
            y: g,
            pointtype: "left"
        }),
        a.data("rightCoordinate", {
            x: b + d,
            y: g,
            pointtype: "right"
        }),
        a.data("bottomCoordinate", {
            x: f,
            y: c + e,
            pointtype: "bottom"
        })
    },
    graphClick: function() {
        var a, b, c, d, e;
        if (ylt.flow.changeNodeText(), null != ylt.flow.objCurSelGraph && ylt.flow.removeLinePoint(ylt.flow.objCurSelGraph), ylt.flow.bIsDel || ylt.flow.clearAddNodeStatus(), a = this, b = this.data("objParRect"), null != b && (a = b), ylt.flow.bIsDel) {
            c = a.data("graphStartLine");
            for (d in c) ylt.flow.delLine(c[d]);
            e = a.data("graphEndLine");
            for (d in e) ylt.flow.delLine(e[d]);
            return delete ylt.flow.objCurGraph[a.data("id")],
            a.data("rectText").remove(),
            a.remove(),
            void 0
        }
        ylt.flow.compGraphCenterPoint(a),
        ylt.flow.objCurSelGraph = a
    },
    showAttrs: function() {
        var a, b, c, d, e, f, g, h, i, j, k = this;
        for (this.objCurSelGraph = k, a = "&flowid=" + ylt.flow.strFlowId + "&nodeid=" + k.data("id") + "&caption=" + k.data("text") + "&nodetype=" + k.data("iType"), b = k.data("child"), null == b && (b = ""), c = b.split(","), d = c.length, a += "&nodechilds=" + b, e = "&nodechilds=" + b, f = 0; d > f; f++) g = c[f],
        "" != g && (e += "&nodechild_" + g + "=" + ylt.flow.objCurGraph[g].data("text"));
        for (h = ylt.flow.arrStrAttrIds.length, f = 0; h > f; f++) i = k.data("attr_" + ylt.flow.arrStrAttrIds[f]),
        i || (i = ""),
        a += "&" + ylt.flow.arrStrAttrIds[f] + "=" + i;
        j = this.objCurSelGraph.data("iType"),
        "1" == j ? miniWin("\u8bbe\u7f6e\u6d3b\u52a8", "", "flow-select.v?formid=" + str_Sys_FormId, 1440, 800, "", "") : "2" == j ? miniWin("\u7f51\u5173\u8bbe\u7f6e", "", "flow-gate.v?formid=" + str_Sys_FormId + e, 1440, 800, "", "") : "4" == j ? miniWin("\u8bbe\u7f6e\u7ed3\u675f\u8282\u70b9", "", "flow-end.v?formid=" + str_Sys_FormId, 1440, 800, "", "") : "5" == j ? miniWin("\u8bbe\u7f6e\u5b50\u6d41\u7a0b", "", "flow-child.v?formid=" + str_Sys_FormId, 1440, 800, "", "") : miniWin("\u8bbe\u7f6e\u5f00\u59cb\u8282\u70b9", "", "flow-start.v?formid=" + str_Sys_FormId, 1440, 800, "", "")
    },
    changeAttr: function(a) {
        var b, c, d, e = a["attreventname"];
        for (ylt.flow.objCurSelGraph.data("text", e), ylt.flow.objCurSelGraph.data("rectText").attr({
            text: e
        }), b = ylt.flow.arrStrAttrIds.length, c = 0; b > c; c++) d = a[ylt.flow.arrStrAttrIds[c]],
        ylt.flow.objCurSelGraph.data("attr_" + ylt.flow.arrStrAttrIds[c], d);
        null != a["childcon"] && ylt.flow.objCurSelGraph.data("attr_childcon", a["childcon"])
    },
    changeAttr_bak: function(a) {
        ylt.flow.objCurSelGraph.data("attr_" + a.id, a.value)
    },
    changeTextAttr: function(a) {
        var b = a.value;
        ylt.flow.objCurSelGraph.data("text", b),
        ylt.flow.objCurSelGraph.data("rectText").attr({
            text: b
        })
    },
    initAttr: function() {
        var a, b, c = this.arrStrAttrIds.length;
        for (attreventname.value = ylt.flow.objCurSelGraph.data("text"), a = 0; c > a; a++) b = ylt.flow.objCurSelGraph.data("attr_" + this.arrStrAttrIds[a]),
        b || (b = ""),
        $(this.arrStrAttrIds[a]).value = b,
        "" != b && ylt.Select.setSelValueById(this.arrStrAttrIds[a], b)
    },
    removeLinePoint: function(a) {
        null != a.data("objTopPoint") && (a.data("objTopPoint").remove(), a.data("objLeftPoint").remove(), a.data("objRightPoint").remove(), a.data("objBottomPoint").remove())
    },
    compGraphCenterPoint: function(a) {
        var b, c, d, e;
        ylt.flow.removeLinePoint(a),
        ylt.flow.generPointCoordinate(a),
        b = ylt.flow.drawPaper.circle(a.data("topCoordinate").x, a.data("topCoordinate").y, 3).attr({
            fill: "white"
        }).drag(ylt.flow.dragLineMove, ylt.flow.dragLineStart, ylt.flow.dragLineUp).mouseover(ylt.flow.pointMouseOver).mouseout(ylt.flow.pointMouseOut),
        c = ylt.flow.drawPaper.circle(a.data("leftCoordinate").x, a.data("leftCoordinate").y, 3).attr({
            fill: "white"
        }).drag(ylt.flow.dragLineMove, ylt.flow.dragLineStart, ylt.flow.dragLineUp).mouseover(ylt.flow.pointMouseOver).mouseout(ylt.flow.pointMouseOut),
        d = ylt.flow.drawPaper.circle(a.data("rightCoordinate").x, a.data("rightCoordinate").y, 3).attr({
            fill: "white"
        }).drag(ylt.flow.dragLineMove, ylt.flow.dragLineStart, ylt.flow.dragLineUp).mouseover(ylt.flow.pointMouseOver).mouseout(ylt.flow.pointMouseOut),
        e = ylt.flow.drawPaper.circle(a.data("bottomCoordinate").x, a.data("bottomCoordinate").y, 3).attr({
            fill: "white"
        }).drag(ylt.flow.dragLineMove, ylt.flow.dragLineStart, ylt.flow.dragLineUp).mouseover(ylt.flow.pointMouseOver).mouseout(ylt.flow.pointMouseOut),
        b.data("parentGraph", a).data("pointtype", "top"),
        c.data("parentGraph", a).data("pointtype", "left"),
        d.data("parentGraph", a).data("pointtype", "right"),
        e.data("parentGraph", a).data("pointtype", "bottom"),
        a.data("objTopPoint", b),
        a.data("objLeftPoint", c),
        a.data("objRightPoint", d),
        a.data("objBottomPoint", e)
    },
    pointMouseOver: function() {
        ylt.flow.bIsGenerLine = !0,
        ylt.flow.objEndNode = this.data("parentGraph"),
        ylt.flow.objEndNode.data("strEndPointType", this.data("pointtype")),
        this.attr({
            fill: "red",
            cursor: "pointer"
        })
    },
    pointMouseOut: function() {
        ylt.flow.bIsGenerLine = !1,
        ylt.flow.objEndNode = null,
        this.attr({
            fill: "white",
            cursor: ""
        })
    },
    dragLineStart: function() {
        this.attr({
            fill: "red"
        }),
        ylt.flow.bDrawLineIng = !0
    },
    dragLineMove: function(a, b) {
        var c, d = this.attr("cx"),
        e = this.attr("cy"),
        f = d + a,
        g = e + b,
        h = ylt.flow.generLinePath(d, e, f, g, this.data("pointtype"));
        null == ylt.flow.objCurDashLine ? (c = ylt.flow.drawPaper.path(h[0]).attr({
            stroke: "black",
            "stroke-dasharray": "--",
            "stroke-width": this.f_LineWidth
        }), ylt.flow.objCurDrawLineArrow = ylt.flow.drawPaper.path(h[1]).attr({
            stroke: "black",
            fill: "black"
        }), c.data("iStartX", d), c.data("iStartY", e), c.data("objArrow", ylt.flow.objCurDrawLineArrow), c.data("strStartPointType", this.data("pointtype")), ylt.flow.objCurDashLine = c) : (ylt.flow.objCurDashLine.attr({
            path: h[0]
        }), ylt.flow.objCurDrawLineArrow.attr({
            path: h[1]
        }).attr({
            stroke: "black",
            fill: "black"
        }), ylt.flow.objCurDashLine.data("iEndX", f), ylt.flow.objCurDashLine.data("iEndY", g))
    },
    dragLineUp: function() {
        var a, b, c, d, e, f, g, h;
        this.attr({
            fill: "white"
        }),
        ylt.flow.bIsGenerLine && null != ylt.flow.objEndNode ? (a = this.data("parentGraph"), null != a.data("graphStartLine") && Object.getOwnPropertyNames(a.data("graphStartLine")).length > 0 && 2 != a.data("iType") ? (ylt.flow.objCurDashLine.remove(), ylt.flow.objCurDrawLineArrow.remove(), alert("\u4e00\u4e2a\u8282\u70b9\u53ea\u80fd\u6709\u4e00\u4e2a\u51fa\u53e3\uff01")) : (b = ylt.flow.objEndNode.data("id"), c = a.data("child"), null == c && (c = ""), -1 != ("," + c + ",").indexOf("," + b + ",") ? (ylt.flow.objCurDashLine.remove(), ylt.flow.objCurDrawLineArrow.remove(), alert("\u5df2\u6709\u8be5\u5b50\u8282\u70b9\uff0c\u51fa\u53e3\u91cd\u590d\uff01")) : (ylt.flow.objCurDashLine.attr({
            cursor: "pointer",
            stroke: "black",
            "stroke-dasharray": ""
        }), ylt.flow.objCurDashLine.click(ylt.flow.opLineClick), ylt.flow.objCurDashLine.mouseover(ylt.flow.opLineMoseOver), ylt.flow.objCurDashLine.mouseout(ylt.flow.opLineMoseOut), d = a.data("id"), "" == c ? (c = b, e = new Object, e[b] = ylt.flow.objCurDashLine, a.data("graphStartLine", e), a.data("child", c), a.data("start", ylt.flow.objCurDashLine.data("strStartPointType") + "Coordinate"), a.data("end", ylt.flow.objEndNode.data("strEndPointType") + "Coordinate")) : (c += "," + b, e = a.data("graphStartLine"), e[b] = ylt.flow.objCurDashLine, a.data("graphStartLine", e), a.data("child", c), f = a.data("start"), g = a.data("end"), a.data("start", f + "," + ylt.flow.objCurDashLine.data("strStartPointType") + "Coordinate"), a.data("end", g + "," + ylt.flow.objEndNode.data("strEndPointType") + "Coordinate")), h = ylt.flow.objEndNode.data("graphEndLine"), null == h && (h = new Object), h[d] = ylt.flow.objCurDashLine, ylt.flow.objEndNode.data("graphEndLine", h)))) : (ylt.flow.objCurDashLine.remove(), ylt.flow.objCurDrawLineArrow.remove()),
        ylt.flow.objCurDashLine = null,
        ylt.flow.bDrawLineIng = !1
    },
    generArrow: function(a, b, c) {
        var d = "";
        switch (c) {
        case "top":
            d = "M " + (a - ylt.flow.iArrowHeight) + "," + b + " L" + a + "," + (b - ylt.flow.iArrowWidth) + " L" + (a + ylt.flow.iArrowHeight) + "," + b + " L" + (a - ylt.flow.iArrowHeight) + "," + b;
            break;
        case "bottom":
            d = "M " + (a - ylt.flow.iArrowHeight) + "," + b + " L" + a + "," + (b + ylt.flow.iArrowWidth) + " L" + (a + ylt.flow.iArrowHeight) + "," + b + " L" + (a - ylt.flow.iArrowHeight) + "," + b;
            break;
        case "left":
            d = "M " + a + "," + (b - ylt.flow.iArrowHeight) + " L" + (a - ylt.flow.iArrowWidth) + "," + b + " L" + a + "," + (b + ylt.flow.iArrowHeight) + " L" + a + "," + (b - ylt.flow.iArrowHeight);
            break;
        case "right":
            d = "M " + a + "," + (b - ylt.flow.iArrowHeight) + " L" + (a + ylt.flow.iArrowWidth) + "," + b + " L" + a + "," + (b + ylt.flow.iArrowHeight) + " L" + a + "," + (b - ylt.flow.iArrowHeight)
        }
        return d
    },
    generLinePathByPoint: function(a, b, c) {
        var d = a.x,
        e = a.y,
        f = b.x,
        g = b.y,
        h = ylt.flow.generLinePath(d, e, f, g, a.pointtype),
        i = ylt.flow.drawPaper.path(h[0]).attr({
            stroke: this.strNodeBorderColor[c],
            "stroke-width": this.f_LineWidth,
            "stroke-dasharray": "",
            cursor: "pointer"
        });
        return ylt.flow.objCurDrawLineArrow = ylt.flow.drawPaper.path(h[1]).attr({
            stroke: this.strNodeBorderColor[c],
            fill: this.strNodeBorderColor[c]
        }),
        i.data("iStartX", d),
        i.data("iStartY", e),
        i.data("objArrow", ylt.flow.objCurDrawLineArrow),
        i.data("strStartPointType", a.pointtype),
        i.data("iEndX", f),
        i.data("iEndY", g),
        i
    },
    generEndLinePoint: function(a, b, c, d) {
        return " L" + a + "," + b + " L" + c + "," + d
    },
    generEndLinePath: function() {},
    generLinePath: function(a, b, c, d, e) {
        var f, g, h, i, j, k, l;
        switch (e) {
        case "top":
            return d > b ? (f = a + this.iNodeWidth / 2, g = a - this.iNodeWidth / 2, c > a && f > c ? (c += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "left"), i = "M " + a + "," + b + " L" + a + "," + (b - ylt.flow.iLineOffset) + " L" + (f + ylt.flow.iLineOffset) + "," + (b - ylt.flow.iLineOffset) + this.generEndLinePoint(f + ylt.flow.iLineOffset, d, c, d), new Array(i, h)) : a > c && c > g ? (c -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "right"), i = "M " + a + "," + b + " L" + a + "," + (b - ylt.flow.iLineOffset) + " L" + (g - ylt.flow.iLineOffset) + "," + (b - ylt.flow.iLineOffset) + this.generEndLinePoint(g - ylt.flow.iLineOffset, d, c, d), new Array(i, h)) : (d -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "bottom"), i = "M " + a + "," + b + " L" + a + "," + (b - ylt.flow.iLineOffset) + this.generEndLinePoint(c, b - ylt.flow.iLineOffset, c, d), new Array(i, h))) : (j = "", iOffsetLength = c - a, iOffsetLength < ylt.flow.iArrowWidth && iOffsetLength > -ylt.flow.iArrowWidth ? (j = "top", c = a, d += ylt.flow.iArrowWidth) : a > c ? (j = "left", c += ylt.flow.iArrowWidth) : (j = "right", c -= ylt.flow.iArrowWidth), h = this.generArrow(c, d, j), i = "M " + a + "," + b + this.generEndLinePoint(a, d, c, d), new Array(i, h));
        case "bottom":
            return b > d ? (f = a + this.iNodeWidth / 2, g = a - this.iNodeWidth / 2, c > a && f > c ? (c += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "left"), i = "M " + a + "," + b + " L" + a + "," + (b + ylt.flow.iLineOffset) + " L" + (f + ylt.flow.iLineOffset) + "," + (b + ylt.flow.iLineOffset) + this.generEndLinePoint(f + ylt.flow.iLineOffset, d, c, d), new Array(i, h)) : a > c && c > g ? (c -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "right"), i = "M " + a + "," + b + " L" + a + "," + (b + ylt.flow.iLineOffset) + " L" + (g - ylt.flow.iLineOffset) + "," + (b + ylt.flow.iLineOffset) + this.generEndLinePoint(g - ylt.flow.iLineOffset, d, c, d), new Array(i, h)) : (d += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "top"), i = "M " + a + "," + b + " L" + a + "," + (b + ylt.flow.iLineOffset) + this.generEndLinePoint(c, b + ylt.flow.iLineOffset, c, d), new Array(i, h))) : (j = "right", iOffsetLength = c - a, iOffsetLength < ylt.flow.iArrowWidth && iOffsetLength > -ylt.flow.iArrowWidth ? (j = "bottom", c = a, d -= ylt.flow.iArrowWidth) : a > c ? (j = "left", c += ylt.flow.iArrowWidth) : (j = "right", c -= ylt.flow.iArrowWidth), h = this.generArrow(c, d, j), i = "M " + a + "," + b + this.generEndLinePoint(a, d, c, d), new Array(i, h));
        case "left":
            return c > a ? (k = b - this.iNodeHeight / 2, l = b + this.iNodeHeight / 2, d > k && b > d ? (d -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "bottom"), i = "M " + a + "," + b + " L" + (a - ylt.flow.iLineOffset) + "," + b + " L" + (a - ylt.flow.iLineOffset) + "," + (k - ylt.flow.iLineOffset) + this.generEndLinePoint(c, k - ylt.flow.iLineOffset, c, d), new Array(i, h)) : d > b && l > d ? (d += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "top"), i = "M " + a + "," + b + " L" + (a - ylt.flow.iLineOffset) + "," + b + " L" + (a - ylt.flow.iLineOffset) + "," + (l + ylt.flow.iLineOffset) + this.generEndLinePoint(c, l + ylt.flow.iLineOffset, c, d), new Array(i, h)) : (c -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "right"), i = "M " + a + "," + b + " L" + (a - ylt.flow.iLineOffset) + "," + b + this.generEndLinePoint(a - ylt.flow.iLineOffset, d, c, d), new Array(i, h))) : (j = "", iOffsetLength = d - b, iOffsetLength < ylt.flow.iArrowWidth && iOffsetLength > -ylt.flow.iArrowWidth ? (j = "left", d = b, c += ylt.flow.iArrowWidth) : b > d ? (j = "top", d += ylt.flow.iArrowWidth) : (j = "bottom", d -= ylt.flow.iArrowWidth), h = this.generArrow(c, d, j), i = "M " + a + "," + b + this.generEndLinePoint(c, b, c, d), new Array(i, h));
        case "right":
            return a > c ? (k = b - this.iNodeHeight / 2, l = b + this.iNodeHeight / 2, d > k && b > d ? (d -= ylt.flow.iArrowWidth, h = this.generArrow(c, d, "bottom"), i = "M " + a + "," + b + " L" + (a + ylt.flow.iLineOffset) + "," + b + " L" + (a + ylt.flow.iLineOffset) + "," + (k - ylt.flow.iLineOffset) + this.generEndLinePoint(c, k - ylt.flow.iLineOffset, c, d), new Array(i, h)) : d > b && l > d ? (d += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "top"), i = "M " + a + "," + b + " L" + (a + ylt.flow.iLineOffset) + "," + b + " L" + (a + ylt.flow.iLineOffset) + "," + (l + ylt.flow.iLineOffset) + this.generEndLinePoint(c, l + ylt.flow.iLineOffset, c, d), new Array(i, h)) : (c += ylt.flow.iArrowWidth, h = this.generArrow(c, d, "left"), i = "M " + a + "," + b + " L" + (a + ylt.flow.iLineOffset) + "," + b + this.generEndLinePoint(a + ylt.flow.iLineOffset, d, c, d), new Array(i, h))) : (j = "", iOffsetLength = d - b, iOffsetLength < ylt.flow.iArrowWidth && iOffsetLength > -ylt.flow.iArrowWidth ? (j = "right", d = b, c -= ylt.flow.iArrowWidth) : b > d ? (j = "top", d += ylt.flow.iArrowWidth) : (j = "bottom", d -= ylt.flow.iArrowWidth), h = this.generArrow(c, d, j), i = "M " + a + "," + b + this.generEndLinePoint(c, b, c, d), new Array(i, h))
        }
    },
    objCurTolls: null,
    clearAddNodeStatus: function() {
        null != this.objCurTolls && (this.objCurTolls.style.border = ""),
        this.objCurTolls = null,
        ylt.flow.iDrawType = 0
    },
    setDrawType: function(a, b) {
        null != this.objCurTolls && (this.objCurTolls.style.border = ""),
        this.objCurTolls = a,
        a.style.border = "1px solid black",
        this.iDrawType = b,
        this.bIsDel = !1
    },
    setDelModl: function(a) {
        null != this.objCurTolls && (this.objCurTolls.style.border = ""),
        this.objCurTolls = a,
        a.style.border = "1px solid black",
        this.iDrawType = 0,
        this.bIsDel = !0
    },
    changeDrawType: function(a) {
        this.iDrawType = a
    },
    getAjaxActive: function() {
        var a;
        return window.ActiveXObject ? a = new ActiveXObject("Microsoft.XMLHTTP") : window.XMLHttpRequest && (a = new XMLHttpRequest),
        a
    },
    saveToServer: function(a, b) {
        var c, d = this.getAjaxActive();
        return d.open("POST", b, !1),
        d.setRequestHeader("content-type", "application/x-www-form-urlencoded"),
        d.send(a),
        c = d.responseText
    },
    getSaveAttrs: function(a, b) {
        var c, d, e, f = "",
        g = this.arrStrAttrIds.length;
        for (c = 0; g > c; c++) d = b.data("attr_" + this.arrStrAttrIds[c]),
        d || (d = ""),
        f += "&" + a + "_attr" + this.arrStrAttrIds[c] + "=" + d;
        return e = b.data("attr_childcon"),
        null == e && (e = ""),
        f += "&" + a + "_attrchildcon=" + e
    },
    updateForm: function(a, b) {
        this.saveToServer("comid=007&S_FORMS=" + a.value + "&S_FLOWID=" + b, "docommand")
    },
    save: function(a) {
        var b, c, d, e, f = "sys_type=saveflow&sys_flow_ver=" + str_Sys_Ver + "&flowid=" + a + "&id=",
        g = "";
        for (b in this.objCurGraph) c = this.objCurGraph[b],
        f += b + ",",
        d = c.data("iType"),
        3 == d || 4 == d ? (g += "&" + b + "_x=" + (c.attr("x") + this.iNodeHeight / 2), g += "&" + b + "_y=" + (c.attr("y") + this.iNodeHeight / 2)) : (g += "&" + b + "_x=" + c.attr("x"), g += "&" + b + "_y=" + c.attr("y")),
        g += "&" + b + "_text=" + c.data("text"),
        g += "&" + b + "_type=" + d,
        null != c.data("child") && (g += "&" + b + "_child=" + c.data("child"), g += "&" + b + "_start=" + c.data("start"), g += "&" + b + "_end=" + c.data("end")),
        g += "&" + b + "_iIndexColor=" + c.data("iIndexColor") + this.getSaveAttrs(b, c);
        e = this.saveToServer(f + g + "&attrs=" + this.arrStrAttrIds.join(), "comp"),
        "ok" == e ? alert("\u4fdd\u5b58\u6210\u529f\uff01") : alert("\u4fdd\u5b58\u5931\u8d25\uff01")
    },
    strAudNodeId: "",
    strAudUser: "",
    init: function(a, b, c) {
        var e, f, g, h, i, j, k, l;
        $("treepanel"),
        this.drawPaper = Raphael(treepanel);
        for (e in a) {
            for (f = a[e], g = f.iIndexColor, null != c && (this.strAudNodeId = c.nodeId, this.strAudUser = c.audUser, g = e != this.strAudNodeId ? 6 : 5, h = c["_" + e], null != h && (g = 3)), i = this.addNode(e, f.x, f.y, this.iNodeWidth, this.iNodeHeight, f.text, f.type, g), j = this.arrStrAttrIds.length, k = 0; j > k; k++) l = this.arrStrAttrIds[k],
            i.data("attr_" + l, f[l]);
            i.data("attr_childcon", f.S_CONDITION),
            this.generPointCoordinate(i)
        }
        this.drawDataLine(a),
        this.iNodeId = b,
        document.body.onmousemove = function() {
            ylt.flow.dragBodyPanel()
        },
        document.body.onmousedown = function() {
            ylt.flow.bodyDragStart()
        },
        document.body.onmouseup = function() {
            ylt.flow.bodyDragEnd()
        },
        document.body.oncontextmenu = function() {
            return ylt.flow.clearAddNodeStatus(),
            ylt.flow.bIsDel = !1,
            !1
        },
        document.body.onselectstart = function() {
            return ! 1
        },
        document.body.onselect = function() {
            document.selection.empty()
        }
    },
    drawDataLine: function(a) {
        var b, c, d, e, f, g, h, i, j, k;
        for (b in a) if (c = a[b], "" != c.child) {
            for (d = c.child.split(","), e = c.start.split(","), f = c.end.split(","), g = d.length, h = 0; g > h; h++) i = this.generLinePathByPoint(this.objCurGraph[b].data(e[h]), this.objCurGraph[d[h]].data(f[h]), c.iIndexColor),
            j = this.objCurGraph[b].data("graphStartLine"),
            null == j && (j = new Object),
            j[d[h]] = i,
            this.objCurGraph[b].data("graphStartLine", j),
            k = this.objCurGraph[d[h]].data("graphEndLine"),
            null == k && (k = new Object),
            k[b] = i,
            this.objCurGraph[d[h]].data("graphEndLine", k),
            i.mouseover(this.opLineMoseOver),
            i.mouseout(this.opLineMoseOut),
            i.click(this.opLineClick);
            this.objCurGraph[b].data("child", c.child),
            this.objCurGraph[b].data("start", c.start),
            this.objCurGraph[b].data("end", c.end)
        }
    },
    delLine: function(a) {
        var b, c, d, e, f, g, h, i, j, k;
        a.data("objArrow").remove(),
        b = null,
        c = null;
        for (d in this.objCurGraph) if (e = this.objCurGraph[d].data("graphStartLine"), null != e) {
            for (f in e) e[f] == a && (delete e[f], b = this.objCurGraph[d]);
            this.objCurGraph[d].data("graphStartLine", e)
        }
        for (d in this.objCurGraph) if (e = this.objCurGraph[d].data("graphEndLine"), null != e) {
            for (f in e) e[f] == a && (delete e[f], c = this.objCurGraph[d]);
            this.objCurGraph[d].data("graphEndLine", e)
        }
        if (null != b) {
            for (g = b.data("child").split(","), h = b.data("start").split(","), i = b.data("end").split(","), j = c.data("id"), k = g.length, d = 0; k > d; d++) g[d] == j && (g.splice(d, 1), h.splice(d, 1), i.splice(d, 1));
            b.data("child", g.join()),
            b.data("start", h.join()),
            b.data("end", i.join())
        }
        a.remove()
    },
    opLineClick: function() {
        ylt.flow.bIsDel && ylt.flow.delLine(this)
    },
    opLineMoseOver: function() {
        this.attr({
            "stroke-dasharray": "--"
        })
    },
    opLineMoseOut: function() {
        this.attr({
            "stroke-dasharray": ""
        })
    },
    lineMoseOut: function() {
        this.animate({
            "fill-opacity": 1
        },
        500)
    },
    lineMoseOver: function() {
        ylt.flow.bDrawLineIng && ylt.flow.compGraphCenterPoint(this),
        this.animate({
            "fill-opacity": .2
        },
        500)
    },
    dragStart: function() {
        var a, b, c, d, e;
        if (this.ox = this.attr("x"), this.oy = this.attr("y"), this.animate({
            "fill-opacity": .2
        },
        500), ylt.flow.removeLinePoint(this), a = this.data("graphStartLine"), null != a) for (b in a) c = a[b],
        c.ox = c.data("iStartX"),
        c.oy = c.data("iStartY");
        if (d = this.data("graphEndLine"), null != d) for (e in d) c = d[e],
        c.ox = c.data("iEndX"),
        c.oy = c.data("iEndY")
    },
    dragMove: function(a, b) {
        var c, d, e, f, g, h, i, j, k, l, m, n = {
            x: this.ox + a,
            y: this.oy + b
        };
        if (this.attr(n), c = this.data("iType"), "2" == c ? this.attr({
            path: ylt.flow.generTranRectByPos(this, this.ox + a, this.oy + b)
        }) : ("3" == c || "4" == c || "5" == c) && (n = {
            cx: this.attr("x") + this.attr("width") / 2,
            cy: this.attr("y") + this.attr("height") / 2
        },
        this.attr(n)), this.data("rectText").attr({
            x: this.ox + a + this.attr("width") / 2,
            y: this.oy + b + this.attr("height") / 2
        }), d = this.data("graphStartLine"), null != d) for (e in d) f = d[e],
        g = f.ox + a,
        h = f.oy + b,
        i = ylt.flow.generLinePath(g, h, f.data("iEndX"), f.data("iEndY"), f.data("strStartPointType")),
        f.data("iStartX", g),
        f.data("iStartY", h),
        f.attr({
            path: i[0]
        }),
        f.data("objArrow").attr({
            path: i[1]
        });
        if (j = this.data("graphEndLine"), null != j) for (k in j) f = j[k],
        l = f.ox + a,
        m = f.oy + b,
        i = ylt.flow.generLinePath(f.data("iStartX"), f.data("iStartY"), l, m, f.data("strStartPointType")),
        f.data("iEndX", l),
        f.data("iEndY", m),
        f.attr({
            path: i[0]
        }),
        f.data("objArrow").attr({
            path: i[1]
        })
    },
    bIsDragBody: !1,
    bodyDragStart: function() {
        var a = a || window.event;
        "2" == a.button && (this.iCurDragPanelY = event.y, this.iCurDragPanelX = event.x, this.bIsDragBody = !0)
    },
    bodyDragEnd: function() {
        this.bIsDragBody = !1
    },
    dragBodyPanel: function() {
        this.bIsDragBody && (window.scrollTo(yltWinCommand.getScrollLeft() - (event.x - ylt.flow.iCurDragPanelX), yltWinCommand.getScrollTop() - (event.y - ylt.flow.iCurDragPanelY)), ylt.flow.iCurDragPanelY = event.y, ylt.flow.iCurDragPanelX = event.x)
    },
    dragUp: function() {
        this.animate({
            "fill-opacity": 1
        },
        500)
    }
};