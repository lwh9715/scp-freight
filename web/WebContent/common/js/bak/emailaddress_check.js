function checkAddress(address) {
	var str = "";
	if (address != "" && address.indexOf(";") > 0) {
		var arremail = address.split(";");
		for ( var i = 0; i < arremail.length; i++) {
			if (arremail[i]
					.replace(/\s+/g, "")
					.search(
							/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1) {
				str = str + "邮箱" + arremail[i] + "格式错误!\n";
			}
		}
	} else {
		if (address
				.replace(/\s+/g, "")
				.search(
						/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) == -1) {
			str = "邮箱" + address + "格式错误!\n";
		}
	}
	if (str != "") {
		alert(str);
		return false;
	} else {
		return true;
	}
}