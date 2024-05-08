function getCharValue(str) {

	if ((str == "a") || (str == "A"))
		return 10;
	else if ((str == "b") || (str == "B"))
		return 12;
	else if ((str == "c") || (str == "C"))
		return 13;
	else if ((str == "d") || (str == "D"))
		return 14;
	else if ((str == "e") || (str == "E"))
		return 15;
	else if ((str == "f") || (str == "F"))
		return 16;
	else if ((str == "g") || (str == "G"))
		return 17;
	else if ((str == "h") || (str == "H"))
		return 18;
	else if ((str == "i") || (str == "I"))
		return 19;
	else if ((str == "j") || (str == "J"))
		return 20;
	else if ((str == "k") || (str == "K"))
		return 21;
	else if ((str == "l") || (str == "L"))
		return 23;
	else if ((str == "m") || (str == "M"))
		return 24;
	else if ((str == "n") || (str == "N"))
		return 25;
	else if ((str == "o") || (str == "O"))
		return 26;
	else if ((str == "p") || (str == "P"))
		return 27;
	else if ((str == "q") || (str == "Q"))
		return 28;
	else if ((str == "r") || (str == "R"))
		return 29;
	else if ((str == "s") || (str == "S"))
		return 30;
	else if ((str == "t") || (str == "T"))
		return 31;
	else if ((str == "u") || (str == "U"))
		return 32;
	else if ((str == "v") || (str == "V"))
		return 34;
	else if ((str == "w") || (str == "W"))
		return 35;
	else if ((str == "x") || (str == "X"))
		return 36;
	else if ((str == "y") || (str == "Y"))
		return 37;
	else if ((str == "z") || (str == "Z"))
		return 38;
	else
		return -1000;

}

var needCheck = true;
var currentCntNos = '';

function checkCntno(strcntr) {
	if(strcntr == currentCntNos && !needCheck){
		return;
	}
	currentCntNos = strcntr;
	
	var num = new Array(10)
	for (i = 0; i < 11; i++) {
		num[i] = 0;
	}
	test = strcntr;
	len = test.length;
	if(test == null || test == "") {
		return true;
	}
	if (len != 11) {
		var a = confirm("输入的集装箱编码应该是11位，你确定保存么！The container No. should be 11 bits. Are you sure you want to save it?")
		if(a){
			needCheck = false;
		}
		if(checkcntnoJs.getValue() == "Y"){
			cntnoFieldJs.setValue('');
		}
		return a;
		// location.reload();
	} else {
		exp = /^[A-Za-z]{4}[0-9]{7}$/g;
		if (!exp.test(test)) {
			if(checkcntnoJs.getValue() == "Y"){
				cntnoFieldJs.setValue('');
			}
			return confirm("集装箱编码格式不正确，前四位应为字母，后七位为数字，你确定么！The format of the container No. is not correct, the first four should be alphabet, and the last seven are numbers. Are you sure?");
		}

		left = test.substr(0, 4);
		right = test.substr(4, 7);
		testnum = test.substr(10, 1);

		char1 = test.substr(0, 1);
		char2 = test.substr(1, 1);
		char3 = test.substr(2, 1);
		char4 = test.substr(3, 1);
		// 箱号字头
		num[0] = getCharValue(char1);
		num[1] = getCharValue(char2);
		num[2] = getCharValue(char3);
		num[3] = getCharValue(char4);

		// 序列号
		num[4] = test.substr(4, 1);
		num[5] = test.substr(5, 1);
		num[6] = test.substr(6, 1);
		num[7] = test.substr(7, 1);
		num[8] = test.substr(8, 1);
		num[9] = test.substr(9, 1);
		// 校验数字
		num[10] = test.substr(10, 1);

		sum = num[0] + num[1] * 2 + num[2] * 4 + num[3] * 8 + num[4] * 16
				+ num[5] * 32 + num[6] * 64 + num[7] * 128 + num[8] * 256
				+ num[9] * 512;
		result = sum % 11;
		//校验码等于10，则减去10，为0
		if(result == "10") {
			result = 0;
		}
		if (result != num[10]) {
			alert("校验码错误！请确认柜号是否输入正确[" + result+"] Check code error! Please confirm whether the container No. is input correctly.");
			if(checkcntnoJs.getValue() == "Y"){
				cntnoFieldJs.setValue('');
			}
			return false;
		} else {
			return true;
		}

	}
}

function getRealCntNo(cntno){
	var no = "";
	if(cntno && /^[A-Za-z]{4}[0-9]{7}$/g.test(cntno)){
		no = cntno;
	}else if(cntno && /^[A-Za-z]{4}[0-9]{6}$/g.test(cntno)){
		no = cntno + "1";
	}else{
		return null;
	}
	
	var num = new Array(10)
	for (i = 0; i < 11; i++) {
		num[i] = 0;
	}
	
	left = no.substr(0, 4);
	right = no.substr(4, 7);
	testnum = no.substr(10, 1);

	char1 = no.substr(0, 1);
	char2 = no.substr(1, 1);
	char3 = no.substr(2, 1);
	char4 = no.substr(3, 1);
	// 箱号字头
	num[0] = getCharValue(char1);
	num[1] = getCharValue(char2);
	num[2] = getCharValue(char3);
	num[3] = getCharValue(char4);

	// 序列号
	num[4] = no.substr(4, 1);
	num[5] = no.substr(5, 1);
	num[6] = no.substr(6, 1);
	num[7] = no.substr(7, 1);
	num[8] = no.substr(8, 1);
	num[9] = no.substr(9, 1);

	sum = num[0] + num[1] * 2 + num[2] * 4 + num[3] * 8 + num[4] * 16
			+ num[5] * 32 + num[6] * 64 + num[7] * 128 + num[8] * 256
			+ num[9] * 512;
	var result = sum % 11;
	//校验码等于10，则减去10，为0
	if(result == "10") {
		result = 0;
	}
	return no.substr(0,10).concat(result);
}