<%@ page pageEncoding="UTF-8" %>
<!--
	 /********************************************
	 * 文件名称：highquery.htm
	 * 功能描述：高级查询控件
	 * 创建日期：2008-07-18
	 * @author：codeslave
	 * @version 0.3
	 *********************************************/
-->
<html>
<head>
<title>高级查询控件</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link type="text/css" rel="stylesheet" href="style.css" />
<script type="text/javascript" src="check.js"></script>
<script type="text/javascript" src="calendar.js"></script>
<script type="text/javascript">
<!--
/********************************字段信息***********************************/

var arrField = new Array('username','regDate','id','email','address','zip', '');
var arrAlias = new Array('名称','注册日期','编号','Email','地址','邮政编码', '请选择一个字段');
var arrType = new Array('varchar','datetime','number','varchar','varchar','varchar','');
var arrLength = new Array('100','18','10','100', '200','6', '0');

/*
var arrField = new Array('username','regDate', 'id', 'email', 'address', 'zip' ,'');
var arrAlias = new Array('名称','注册日期', '编号', 'Email', '请选择一个字段');
var arrType = new Array('varchar','datetime','number', 'varchar', '');
var arrLength = new Array('100','19', '10', '100', '0');
*/
//-->
</script>
</head>
<body>



<table id="highQuery" class="HQ_TABLE">
<th class="HQ_TH">连接</th>
<th class="HQ_TH">左括号</th>
<th class="HQ_TH">字段</th>
<th class="HQ_TH">运算符</th>
<th class="HQ_TH">内容</th>
<th class="HQ_TH">右括号</th>
<th class="HQ_TH">操作</th>
</table>
<script type="text/javascript">
<!--
/******************************以下为初始化部分******************************/

var autoId = 0; // 自增变量

// 取得一般查询条件的html文件(日期)。
function getDateHtml(txtId)
{
	var strHtml = "<input type='button' class='HQ_BUTTON' onClick=selDate(document.getElementById('" + txtId + "')) value='选择'>";
	strHtml += "<input type='button' class='HQ_BUTTON' onClick=clearContent(document.getElementById('" + txtId + "')) value='清除'>";
	return strHtml;
}

// 取得in或not in查询条件的html文件(日期)。
function getDateInHtml(txtId)
{
	var strHtml = "<input type='button' class='HQ_BUTTON' onClick=selDateIn(document.getElementById('" + txtId + "')) value='选择'>";
	strHtml += "<input type='button' class='HQ_BUTTON' onClick=clearContent(document.getElementById('" + txtId + "')) value='清除'>";
	return strHtml;
}

// 取得一般查询条件的html文件(日期时间)。
function getDateTimeHtml(txtId)
{
	var strHtml = "<input type='button' class='HQ_BUTTON' onClick=selDateTime(document.getElementById('" + txtId + "')) value='选择'>";
	strHtml += "<input type='button' class='HQ_BUTTON' onClick=clearContent(document.getElementById('" + txtId + "')) value='清除'>";
	return strHtml;
}

// 取得in或not in查询条件的html文件(日期时间)。
function getDateTimeInHtml(txtId)
{
	var strHtml = "<input type='button' class='HQ_BUTTON' onClick=selDateTimeIn(document.getElementById('" + txtId + "')) value='选择'>";
	strHtml += "<input type='button' class='HQ_BUTTON' onClick=clearContent(document.getElementById('" + txtId + "')) value='清除'>";
	return strHtml;
}

// 取得like查询条件的html文本。
function getLikeHtml(id) 
{
	var strHtml = "<select id='darkSign_" + id + "'><option value='0'>%</option><option value='1'>_</option></select>";
	strHtml += "&nbsp;<span id='darkB_" + id + "' onClick=addDarkSign('b') style='font-weight: bold;cursor: hand'>←→</span>" + 
		"&nbsp;<span id='darkL_" + id + "' onClick=addDarkSign('l') style='font-weight: bold;cursor: hand'>←</span>" + 
		"&nbsp;<span id='darkR_" + id + "' onClick=addDarkSign('r') style='font-weight: bold;cursor: hand'>→</span>";
	return strHtml;
}

// 取得is查询条件的html文本。
function getIsHtml(id) 
{
	var strHtml = "<span id='null_" + id + "' onClick=document.getElementById('bContent_" + id + "').value='null'" + 
		" style='font-weight: bold;cursor: hand'>○</span>" + 
		"&nbsp;<span id='notNull_" + id + "' onClick=document.getElementById('bContent_" + id + "').value='not&nbsp;null'" + 
		" style='font-weight: bold;cursor: hand'>●</span>";
	return strHtml;
}

// 初始化字段列表。
function initFieldList(id)
{
	var strField = "<select onChange='changeField()' id='field_" + id + "'>";
	for(var i = 0; i < arrField.length; i++)
	{
		strField += "<option value='" + arrField[i] + "'>" + arrAlias[i] + "</option>"
	}
	strField += '</select>';
	return strField;
}

// 初始化条件相连的运算符。
function initJoin(id)
{
	var strJoin = "<select id='join_" + id + "'><option value='and'>AND</option><option value='or'>OR</option></select>";
	return strJoin;
}

// 初始化条件运算符。
function initCondition(id)
{
	var strCondition = "<select onChange='changeCondition()' id='condition_" + id + "'>" + 
		"<option value='='>=</option><option value='<'><</option>" + 
		"<option value='<='><=</option><option value='>'>></option>" +
		"<option value='>='>>=</option><option value='<>'><></option>" +
		"<option value='in'>IN</option><option value='not_in'>NOT IN</option>" + 
		"<option value='between'>BETWEEN</option><option value='is'>IS</option>" +
		"<option value='like'>LIKE</option></select>";
	strCondition += "<span id='signPanel_" + id + "'></span>" // 存放查询符号，主要用于like和is查询
	return strCondition;
}

// 初始化条件内容。
function initContent(id)
{
	var strContent = "<input class='HQ_TEXT' id='bContent_" + id + "' maxLength=" + arrLength[arrLength.length-1] + ">";
	strContent += "<span id='bChoicePanel_" + id + "'></span>"; // 存放第一个文本控件的"选择"和"清除"按钮
	strContent += "<span id='eContentPanel_" + id + "'></span>"; // 存放第二个文本控件、以及eChoicePanel面板和他内部的"选择"和"清除"按钮
	return strContent;
}

// 初始化第一行。
function initFristRow()
{
	highQuery.insertRow();
	var newRow = highQuery.rows[highQuery.rows.length - 1];
	
	newRow.id = "row_" + autoId;

	newRow.insertCell();
	newRow.cells[0].className = "HQ_TD";
	newRow.cells[0].innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	newRow.insertCell();
	newRow.cells[1].className = "HQ_TD";
	newRow.cells[1].innerHTML = "<span id='lParenthesisPanel_" + autoId + "' style='font-weight: bold;'></span>"; // 左括号面板
	
	newRow.insertCell();
	newRow.cells[2].className = "HQ_TD";
	newRow.cells[2].innerHTML = initFieldList(autoId);
	
	newRow.insertCell();
	//newRow.cells[3].width = 200;
	newRow.cells[3].className = "HQ_TD";
	newRow.cells[3].innerHTML = initCondition(autoId);
	
	newRow.insertCell();
	newRow.cells[4].className = "HQ_TD";
	newRow.cells[4].innerHTML = initContent(autoId);

	newRow.insertCell();
	newRow.cells[5].className = "HQ_TD";
	newRow.cells[5].innerHTML = "<span id='rParenthesisPanel_" + autoId + "' style='font-weight: bold;'></span>"; // 右括号面板
	
	newRow.insertCell();
	newRow.cells[6].className = "HQ_TD";
	newRow.cells[6].innerHTML = "<input type='button' class='HQ_BUTTON' value='+(' onClick=addParenthesis('" + autoId + "','l')>&nbsp;" + 
		"<input type='button' class='HQ_BUTTON' value='+)' onClick=addParenthesis('" + autoId + "','r')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='-(' onClick=removeParenthesis('" + autoId + "','l')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='-)' onClick=removeParenthesis('" + autoId + "','r')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='增加条件' onClick='addRow()'>";
	
	var objField = document.getElementById("field_" + autoId); // 取得当前的字段选择控件对象
	if(objField.options.length > 0)
		objField.options.selectedIndex = objField.options.length - 1; // 选中字段列表的最后一个"请选择一个字段"

	autoId += 1; // autoId加1
}

// 初始化第一行。
initFristRow();

/******************************以下为条件增删部分******************************/

// 新添加一行。
function addRow()
{
	highQuery.insertRow();
	var newRow = highQuery.rows[highQuery.rows.length - 1];

	newRow.id = "row_" + autoId;
	
	newRow.insertCell();
	newRow.cells[0].className = "HQ_TD";
	newRow.cells[0].innerHTML = initJoin(autoId);
	
	newRow.insertCell();
	newRow.cells[1].className = "HQ_TD";
	newRow.cells[1].innerHTML = "<span id='lParenthesisPanel_" + autoId + "' style='font-weight: bold;'></span>"; // 左括号面板

	newRow.insertCell();
	newRow.cells[2].className = "HQ_TD";
	newRow.cells[2].innerHTML = initFieldList(autoId);
	
	newRow.insertCell();
	newRow.cells[3].className = "HQ_TD";
	newRow.cells[3].innerHTML = initCondition(autoId);
	
	newRow.insertCell();
	newRow.cells[4].className = "HQ_TD";
	newRow.cells[4].innerHTML = initContent(autoId);
	
	newRow.insertCell();
	newRow.cells[5].className = "HQ_TD";
	newRow.cells[5].innerHTML = "<span id='rParenthesisPanel_" + autoId + "' style='font-weight: bold;'></span>"; // 右括号面板

	newRow.insertCell();
	newRow.cells[6].className = "HQ_TD";
	newRow.cells[6].innerHTML = "<input type='button' class='HQ_BUTTON' value='+(' onClick=addParenthesis('" + autoId + "','l')>&nbsp;" + 
		"<input type='button' class='HQ_BUTTON' value='+)' onClick=addParenthesis('" + autoId + "','r')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='-(' onClick=removeParenthesis('" + autoId + "','l')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='-)' onClick=removeParenthesis('" + autoId + "','r')>&nbsp;" +
		"<input type='button' class='HQ_BUTTON' value='删除' onClick=removeRow('" + newRow.id + "')>";
	
	var objField = document.getElementById("field_" + autoId); // 取得当前的字段选择控件对象
	if(objField.options.length > 0)
		objField.options.selectedIndex = objField.options.length - 1; // 选中字段列表的最后一个"请选择一个字段"

	autoId += 1; // autoId加1
}

// 删除行。
function removeRow(rowId)
{
	var trRow = document.getElementById(rowId);
	trRow.removeNode(true);
}

// 生成条件查询语句。
function interpreter()
{
	var arrReturn = new Array();
	var strCondition = "";
	var strAliasCondition = "";
	var isNoProblem = true;
	
	// 语法检测
	if(!checkExpression())
	{
		isNoProblem = false;
		arrReturn[0] = "";
		arrReturn[1] = "";
		return arrReturn;
	}

	for(var i = 1; i < highQuery.rows.length; i++)
	{
		var id = highQuery.rows[i].id.split("_")[1];
		var objField = document.getElementById("field_" + id); // 取得字段选择控件对象
		var objCondition = document.getElementById("condition_" + id); // 取得条件运算符选择控件对象
		var objBContent = document.getElementById("bContent_" + id); // 取得第一个条件内容文件控件对象
		var objLParenthesisPanel = document.getElementById("lParenthesisPanel_" + id); // 取得左括号面板对象
		var objRParenthesisPanel = document.getElementById("rParenthesisPanel_" + id); // 取得右括号面板对象
		var strType = arrType[objField.selectedIndex]; // 内容类别
		var strAlias = arrAlias[objField.selectedIndex]; // 字段别名

		if(i > 1) // 如果不是第一个条件就要加上连接条件符号(AND,OR)。
		{
			var objJoin = document.getElementById("join_" + id); // 条件相连的运算符
			strCondition += " " + objJoin.value + " ";
			strAliasCondition +=  " " + objJoin.value + " ";
		}

		// 加上左括号
		strCondition += objLParenthesisPanel.outerText;
		strAliasCondition += objLParenthesisPanel.outerText;
			
		if(objField.value != "") // 字段是否为空
		{
			strCondition += objField.value;
			strAliasCondition += strAlias;
		}
		else
		{
			isNoProblem = false;
			alert("所选的条件中存在不明确的字段名，请先选择一个有效字段名！");
			objField.focus();
			arrReturn[0] = "";
			arrReturn[1] = "";
			return arrReturn;
		}

		switch(objCondition.value)
		{
			case "like": // 查询条件为like时
				if(checkDataLike(objBContent, strAlias, emendationType(strType))) 
				{
					strCondition += likeCondition(objBContent, strType, objCondition.value);
					strAliasCondition += likeCondition(objBContent, strType, objCondition.value);
				}
				else
					isNoProblem = false;
				break;
			case "in": // 查询条件为in时
				if(checkDataIn(objBContent, strAlias, emendationType(strType)))
				{
					strCondition += inCondition(objBContent, strType, objCondition.value);
					strAliasCondition += inCondition(objBContent, strType, objCondition.value);
				}
				else
					isNoProblem = false;
				break;
			case "not_in": // 查询条件为not in时
				if(checkDataIn(objBContent, strAlias, emendationType(strType)))
				{
					strCondition += inCondition(objBContent, strType, objCondition.value);
					strAliasCondition += inCondition(objBContent, strType, objCondition.value);
				}
				else
					isNoProblem = false;
				break;
			case "between": // 查询条件为between时
				var objEContent = document.getElementById("eContent_" + id); // between查询时才会出现的第二个条件内容对象
				if(checkDataBetween(new Array(objBContent, objEContent), strAlias, emendationType(strType)))
				{
					strCondition += betweenCondition(objBContent, objEContent, strType, objCondition.value);
					strAliasCondition += betweenCondition(objBContent, objEContent, strType, objCondition.value);
				}
				else
					isNoProblem = false;
				break;
			case "is": // 查询条件为is时
				if(checkDataIs(objBContent, strAlias, "checknull"))
				{
					strCondition += isCondition(objBContent, strType, objCondition.value);
					strAliasCondition += isCondition(objBContent, strType, objCondition.value);
				}
				else
					isNoProblem = false;
				break;
			default: // 查询条件为其他时
				if(checkDataNormal(objBContent, strAlias, emendationType(strType)))
				{
					strCondition += " " + objCondition.value + normalCondition(objBContent, strType);
					strAliasCondition +=  " " + objCondition.value + normalCondition(objBContent, strType);
				}
				else
					isNoProblem = false;
				break;
		}
		if(isNoProblem != true)
		{
			arrReturn[0] = "";
			arrReturn[1] = "";
			return arrReturn;
		}

		// 加上右括号
		strCondition += objRParenthesisPanel.outerText;
		strAliasCondition += objRParenthesisPanel.outerText;
	}
	arrReturn[0] = strAliasCondition;
	arrReturn[1] = strCondition;
	return arrReturn;
}

// 生成like查询条件内容。
function likeCondition(objTemp, strType, strCondition)
{
	var strTemp = normalCondition(objTemp, strType);
	return " " + strCondition + strTemp;
}

// 生成in或not in查询条件内容。
function inCondition(objTemp, strType, strCondition)
{
	var strTemp = normalCondition(objTemp, strType);
	var strReturn = "";
	if(strTemp != " ''")
	{
		strTemp = strTemp.substring(2, strTemp.length - 1); // 除去两边的单引号
		var arrTemp = strTemp.split(',');
		for(var i = 0; i < arrTemp.length; i++)
		{
			if(arrTemp[i] != null)
			{
				strReturn += "'" + arrTemp[i] + "',"
			}
		}
		strReturn = strReturn.substring(0, strReturn.length - 1); // 除去最后一个豆号
		return " " + strCondition.replace("_", " ") + " ( " + strReturn + " )";
	} 
	else
	{
		strReturn = strTemp;
		return " " + strCondition.replace("_", " ") + " ( " + strReturn + " )";
	}
}

// 生成between的查询条件内容。
function betweenCondition(objTemp1, objTemp2, strType, strCondition)
{
	strTemp1 = normalCondition(objTemp1, strType);
	strTemp2 = normalCondition(objTemp2, strType);
	return " " + strCondition + strTemp1 + " and" + strTemp2;
}

// 生成is查询条件内容。
function isCondition(objTemp, strType, strCondition)
{
	var strTemp = objTemp.value;
	strTemp = strTemp.trim();
	return strTemp==""?" " + strCondition + " null":" " + strCondition + " " + strTemp;
}

// 生成一般查询条件内容。
function normalCondition(objTemp, strType)
{
	var strTemp = objTemp.value;
	strTemp = strTemp.trim();
	return strTemp==""?" ''":" '" + strTemp + "'";
}

/******************************以下为字段类型验测函数******************************/

// 根据类型返回相应校正的项目。
function emendationType(strType)
{
	if(strType == 'number')
	{
		return "checknumber";
	}
	else if(strType == 'date' || strType == 'datetime')
	{
		return "";
	}
	return "";
}

/******************************以下为校正函数**********************************/

// 语法校正
function checkExpression()
{
	var strLParenthesis = "";
	var strRParenthesis = "";

	for(var i = 1; i < highQuery.rows.length; i++)
	{
		var id = highQuery.rows[i].id.split("_")[1];
		var objLParenthesisPanel = document.getElementById("lParenthesisPanel_" + id); // 取得左括号面板对象
		var objRParenthesisPanel = document.getElementById("rParenthesisPanel_" + id); // 取得右括号面板对象
		strLParenthesis += objLParenthesisPanel.outerText;
		strRParenthesis += objRParenthesisPanel.outerText;
	}

	if(strLParenthesis.length != strRParenthesis.length)
	{
		alert("\"(\"与\")\"的数量不对应，请检查！");
		return false;
	}
	return true;
}

// 数据校正(like查询专用)。
function checkDataLike(objTemp, strText, action)
{
	var strTemp = objTemp.value;
	//if(strTemp == "" || action == "checkdate")
	//	return true;
	strTemp = strTemp.replace(/\%/g, "");
	strTemp = strTemp.replace(/\_/g, "");
	if(strTemp == "")
		return true;
	return checkS(objTemp, strTemp, strText, action);
}

// 数据校正(in或not in查询专用)。
function checkDataIn(objTemp, strText, action)
{
	var strTemp = objTemp.value;
	var temps = strTemp.split(',');
	var boolOK = true;
	for(var i = 0; i < temps.length; i++)
	{
		if(temps[i] != "")
			boolOK = checkS(objTemp, temps[i], strText, action);
		if(boolOK == false)
			break;
	}
	return boolOK;
}

// 数据校正。(between查询专用)
function checkDataBetween(objTemp, strText, action)
{
	var strTemp = new Array(objTemp[0].value, objTemp[1].value);
	var boolOK = true;
	for(var i = 0; i < objTemp.length; i++)
	{
		if(strTemp[i] != "")
			boolOK = checkS(objTemp[i], strTemp[i], strText, action);
		if(boolOK == false)
			break;
	}
	return boolOK;
}

// 数据校正。(is查询时用)
function checkDataIs(objTemp, strText, action)
{
	var strTemp = objTemp.value;
	return checkS(objTemp, strTemp, strText, action);
}

// 数据校正。(一般查询时用)
function checkDataNormal(objTemp, strText, action)
{
	var strTemp = objTemp.value;
	if(strTemp == "")
		return true;
	return checkS(objTemp, strTemp, strText, action);
}

/******************************以下为触发功能******************************/

// 改变所选字段时，相应改变界面。
function changeField()
{
	var objField = window.event.srcElement; // 取得字段选择控件
	var id = objField.id.split("_")[1];
	var trRow = document.getElementById("row_" + id); // 取得行对象。
	var objCondition = document.getElementById("condition_" + id); // 取得条件运算符选择控件对象
	var objSignPanel = document.getElementById("signPanel_" + id); // 取得查询符号面板对象
	var objBChoicePanel = document.getElementById("bChoicePanel_" + id); // 取得第一个内容文本"选择"面板对象
	var objEContentPanel = document.getElementById("eContentPanel_" + id); // 取得第二个内容文本面板对象
	var objBContent = document.getElementById("bContent_" + id); // 取得第一个条件内容文件控件对象
	var isDate = arrType[objField.selectedIndex] == "date"?true:false;
	var isDateTime = arrType[objField.selectedIndex] == "datetime"?true:false;
	objBContent.value = "";
	objBContent.maxLength = arrLength[objField.selectedIndex]; // 根据字段长度改变文本控件的长度

	if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,删除like查询条件
	{
		var conditionTemp = objCondition.value;
		if(objCondition.options[objCondition.length-1].value == "like")
		{
			objCondition.options.remove(objCondition.options[objCondition.length-1].index)
			objSignPanel.innerHTML = ""; // 清除查询符号面板内容
		}
		if(conditionTemp != "like")
			objCondition.value = conditionTemp;
	}
	else 
	{
		if(objCondition.options[objCondition.length-1].value != "like")
		{
			objCondition.options.add(new Option('LIKE', 'like'));
		}
	}
	
	switch(objCondition.value)
	{
		case "like": // 查询条件为like时
			objBContent.setAttribute("readOnly", "");
			objSignPanel.innerHTML = getLikeHtml(id);
			objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			break;
		case "in": // 查询条件为in时
			objBContent.maxLength = '2147483647'; // 设为最大值
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
			{
				objBContent.setAttribute("readOnly", "true");
				objBChoicePanel.innerHTML = isDate?getDateInHtml("bContent_" + id):getDateTimeInHtml("bContent_" + id);
			}
			else
			{
				objBContent.setAttribute("readOnly", "");
				objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			}
			break;
		case "not_in": // 查询条件为not in时
			objBContent.maxLength = '2147483647'; // 设为最大值
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
			{
				objBContent.setAttribute("readOnly", "true");
				objBChoicePanel.innerHTML = isDate?getDateInHtml("bContent_" + id):getDateTimeInHtml("bContent_" + id);
			}
			else
			{
				objBContent.setAttribute("readOnly", "");
				objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			}
			break;
		case "between": // 查询条件为between时
			var objEContent = document.getElementById("eContent_" + id); // between查询时才会出现的第二个条件内容对象
			var objEChoicePanel = document.getElementById("eChoicePanel_" + id); // between查询时才会出现的第二个内容文本"选择"面板对象
			objEContent.value = ""; // 如果查询条件使用了BETWEEN时，这个元素就存在
			objEContent.maxLength = objBContent.maxLength;
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
			{
				objBContent.setAttribute("readOnly", "true");
				objEContent.setAttribute("readOnly", "true");
				objBChoicePanel.innerHTML = isDate?getDateHtml("bContent_" + id):getDateTimeHtml("bContent_" + id);
				objEChoicePanel.innerHTML = isDate?getDateHtml("eContent_" + id):getDateTimeHtml("eContent_" + id);
			}
			else
			{
				objBContent.setAttribute("readOnly", "");
				objEContent.setAttribute("readOnly", "");
				objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
				objEChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第二个内容文本"选择"面板内容(不论是否存在)
			}
			break;
		case "is": // 查询条件为is时
			objBContent.setAttribute("readOnly", "true");
			objSignPanel.innerHTML = getIsHtml(id);
			objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			break;
		default: // 查询条件为其他时
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
			{
				objBContent.setAttribute("readOnly", "true");
				objBChoicePanel.innerHTML = isDate?getDateHtml("bContent_" + id):getDateTimeHtml("bContent_" + id);
			}
			else
			{
				objBContent.setAttribute("readOnly", "");
				objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			}
			break;
	}
}

// 改变条件运算符时，相应改变界面。
function changeCondition()
{
	var objCondition = window.event.srcElement; // 取得条件运算符选择控件对象
	var id = objCondition.id.split("_")[1];
	var trRow = document.getElementById("row_" + id); // 取得行对象。
	var objField = document.getElementById("field_" + id); // 取得字段选择控件对象
	var objSignPanel = document.getElementById("signPanel_" + id); // 取得查询符号面板对象
	var objBChoicePanel = document.getElementById("bChoicePanel_" + id); // 取得第一个内容文本"选择"面板对象
	var objEContentPanel = document.getElementById("eContentPanel_" + id); // 取得第二个内容文本面板对象
	var objBContent = document.getElementById("bContent_" + id); // 取得第一个条件内容文件控件对象
	var isDate = arrType[objField.selectedIndex] == "date"?true:false;
	var isDateTime = arrType[objField.selectedIndex] == "datetime"?true:false;
	objBContent.maxLength = arrLength[objField.selectedIndex]; // 根据字段长度改变文本控件的长度
	
	// 如果文本内容长度大于最大长度(通常出现在in查询时)就清除内容或者条件运算符为"is"
	if(objBContent.value.length > objBContent.maxLength || objCondition.value == "is")
	{
		objBContent.value = "";
	}

	if(isDate || isDateTime || objCondition.value == "is")
		objBContent.setAttribute("readOnly", "true");
	else 
		objBContent.setAttribute("readOnly", "");

	switch(objCondition.value)
	{
		case "like": // 查询条件为like时
			objSignPanel.innerHTML = getLikeHtml(id);
			objEContentPanel.innerHTML = ""; // 清除between查询的第二个文本面板内容(不论是否存在)
			break;
		case "in": // 查询条件为in时
			objBContent.maxLength = '2147483647'; // 设为最大值
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
				objBChoicePanel.innerHTML = isDate?getDateInHtml("bContent_" + id):getDateTimeInHtml("bContent_" + id);
			else
				objBChoicePanel.innerHTML = "";
			objSignPanel.innerHTML= ""; // 清除查询符号面板内容(不论是否存在)
			objEContentPanel.innerHTML = ""; // 清除between查询的第二个文本面板内容(不论是否存在)
			break;
		case "not_in": // 查询条件为not in时
			objBContent.maxLength = '2147483647'; // 设为最大值
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
				objBChoicePanel.innerHTML = isDate?getDateInHtml("bContent_" + id):getDateTimeInHtml("bContent_" + id);
			else
				objBChoicePanel.innerHTML = "";
			objSignPanel.innerHTML= ""; // 清除查询符号面板内容(不论是否存在)
			objEContentPanel.innerHTML = ""; // 清除between查询的第二个文本面板内容(不论是否存在)
			break;
		case "between": // 查询条件为between时
			var maxLen = objBContent.maxLength;
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
			{
				objBChoicePanel.innerHTML = isDate?getDateHtml("bContent_" + id):getDateTimeHtml("bContent_" + id);
				objEContentPanel.innerHTML = "--<input class='HQ_TEXT' id='eContent_" + id + "' readOnly maxLength='" + maxLen + 
					"'><span id='eChoicePanel_" + id + "'>" + (isDate?getDateHtml("eContent_" + id):getDateTimeHtml("eContent_" + id)) + "</span>";
			}
			else
			{
				objBChoicePanel.innerHTML = "";
				objEContentPanel.innerHTML = "--<input class='HQ_TEXT' id='eContent_" + id + "' maxLength='" + maxLen + "'>" + 
					"<span id='eChoicePanel_" + id + "'></span>";
			}
			objSignPanel.innerHTML= ""; // 清除查询符号面板内容(不论是否存在)
			break;
		case "is": // 查询条件为is时
			objSignPanel.innerHTML = getIsHtml(id);
			objBChoicePanel.innerHTML = ""; // 清除date或datetime查询时留下的第一个内容文本"选择"面板内容(不论是否存在)
			break;
		default: // 查询条件为其他时
			if(isDate || isDateTime) // 如果对应的字段类型是date或datetime时,给用户选择日期
				objBChoicePanel.innerHTML = isDate?getDateHtml("bContent_" + id):getDateTimeHtml("bContent_" + id);
			objSignPanel.innerHTML= ""; // 清除查询符号面板内容(不论是否存在)
			objEContentPanel.innerHTML = ""; // 清除between查询的第二个文本面板内容(不论是否存在)
			break;
	}
}

/******************************以下为应用函数******************************/

// 添加模糊查询符号。
function addDarkSign(strFlag)
{
	var id = window.event.srcElement.id.split("_")[1];
	var objBContent = document.getElementById("bContent_" + id); // 取得当前的第一个条件内容文件控件对象
	var objDarkSign = document.getElementById("darkSign_" + id); // 取得当前的标记选择控件对象('%','_')

	if(objBContent.value != "")
	{
		if(objDarkSign.value == "0")
		{
			if(strFlag == 'b')
			{
				objBContent.value = objBContent.value.trimPS();	// 清除内容两边的"%"
				objBContent.value = "%" + objBContent.value + "%"; // 两边模糊。
			}
			else if(strFlag == 'l')
			{
				objBContent.value = objBContent.value.trimPS(); // 清除内容两边的"%"
				objBContent.value = "%" + objBContent.value; // 左边模糊。
			}
			else if(strFlag == 'r')
			{
				objBContent.value = objBContent.value.trimPS(); // 清除内容两边的"%"
				objBContent.value = objBContent.value + "%"; // 右边模糊。
			}
		}
		else if(objDarkSign.value == "1")
		{
			if(strFlag == 'b')
			{
				objBContent.value = "_" + objBContent.value + "_"; // 两边模糊。
			}
			else if(strFlag == 'l')
			{
				objBContent.value = "_" + objBContent.value; // 左边模糊。
			}
			else if(strFlag == 'r')
			{
				objBContent.value = objBContent.value + "_"; // 右边模糊。
			}
		}

	}
}

// 清除内容。
function clearContent(objTemp)
{
	objTemp.value = "";
}

//添加"(" 或 ")"
function addParenthesis(id, strFlag)
{
	if(strFlag == "l")
	{
		var objLParenthesisPanel = document.getElementById("lParenthesisPanel_" + id); // 取得左括号面板对象
		objLParenthesisPanel.innerText = objLParenthesisPanel.outerText + "(";
	}
	else if(strFlag == 'r')
	{
		var objRParenthesisPanel = document.getElementById("rParenthesisPanel_" + id); // 取得左括号面板对象
		objRParenthesisPanel.innerText = objRParenthesisPanel.outerText + ")";
	}
}

//删除"(" 或 ")"
function removeParenthesis(id, strFlag)
{
	if(strFlag == "l")
	{
		var objLParenthesisPanel = document.getElementById("lParenthesisPanel_" + id); // 取得左括号面板对象
		if(objLParenthesisPanel.outerText.length > 0)
			objLParenthesisPanel.innerText = objLParenthesisPanel.outerText.substring(0, objLParenthesisPanel.outerText.length - 1);
	}
	else if(strFlag == 'r')
	{
		var objRParenthesisPanel = document.getElementById("rParenthesisPanel_" + id); // 取得左括号面板对象
		objRParenthesisPanel.innerText = objRParenthesisPanel.outerText.substring(0, objRParenthesisPanel.outerText.length - 1);
	}
}

// 选择查询日期。
function selDate(objTemp)
{
	objTemp.value = showDateForm();
}

// 选择查询日期(in或not in查询专用)。
function selDateIn(objTemp)
{
	if(objTemp.value == "")
		objTemp.value = showDateForm();
	else
	{
		var strTemp = showDateForm();
		objTemp.value += (strTemp==""?strTemp:"," + strTemp);
	}
}

// 选择查询日期时间。
function selDateTime(objTemp)
{
	objTemp.value = showDateTimeForm();
}

// 选择查询日期时间(in或not in查询专用)。
function selDateTimeIn(objTemp)
{
	if(objTemp.value == "")
		objTemp.value = showDateTimeForm();
	else
	{
		var strTemp = showDateTimeForm();
		objTemp.value += (strTemp==""?strTemp:"," + strTemp);
	}
}

// 测试用
function show()
{
	var arrReturn = interpreter();
	
	document.getElementById("where").value = arrReturn[0];//别名
	document.getElementById("condition").value = arrReturn[1];// 字段名
}

//-->
</script>
<br>


<script type="text/javascript">
// 提交之前先生成查询条件, 然后再提交表单
function formCheck(form) {
	show();
	if(form.where.value == '') {
		//alert('查询条件不能为空');
		return false;
	} else {
		return true;
	}
	
	return false;
}
</script>

<form action="../admin/advanceUserQuery.action" method="post" onsubmit="return formCheck(this)">
<input type=hidden name="condition" id="condition">
<input type=button onclick="show()" value="预览查询条件">
查询条件:<br>
<textarea class="HQ_TEXTAREA" name="where" id="where" rows="6" cols="100"></textarea><br>
<input type=submit value="查询">
</form>
</body>
</html>
