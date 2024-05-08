package com.scp.util;


/**
 * @author Neo
 */
public class AmountUtil {
	/**
	 * 用法打印结果如下：(注：小数点後只能解析两位，这个主要用於解析货币数用) 01=ONE ONLY 09=NINE ONLY 11=ELEVEN
	 * ONLY 19=NINETEEN ONLY 20=TWENTY ONLY 90=NINETY ONLY 21=TWENTY ONE ONLY
	 * 99=NINETY NINE ONLY 100=ONE HUNDRED ONLY 1000=ONE THOUSAND ONLY 10000=TEN
	 * THOUSAND ONLY 100000=ONE HUNDRED THOUSAND ONLY 1000000=ONE MILLION ONLY
	 * 10000000=TEN MILLION ONLY 101=ONE HUNDRED AND ONE ONLY 109=ONE HUNDRED
	 * AND NINE ONLY 120=ONE HUNDRED AND TWENTY ONLY 121=ONE HUNDRED AND TWENTY
	 * ONE ONLY 199=ONE HUNDRED AND NINETY NINE ONLY 999=NINE HUNDRED AND NINETY
	 * NINE ONLY 1001=ONE THOUSAND ONE ONLY 1020=ONE THOUSAND TWENTY ONLY
	 * 1021=ONE THOUSAND TWENTY ONE ONLY 9999=NINE THOUSAND NINE HUNDRED AND
	 * NINETY NINE ONLY 2000=TWO THOUSAND ONLY 2001=TWO THOUSAND ONE ONLY
	 * 10001=TEN THOUSAND ONE ONLY 12001=TWELEVE THOUSAND ONE ONLY 123456789=ONE
	 * HUNDRED AND TWENTY THREE MILLION FOUR HUNDRED AND FIFTY SIX THOUSAND
	 * SEVEN HUNDRED AND EIGHTY NINE ONLY 100000001=ONE HUNDRED MILLION ONE ONLY
	 * 100.1=ONE HUNDRED AND CENTS ONE ONLY 120.9=ONE HUNDRED AND TWENTY AND
	 * CENTS NINE ONLY 123.456=ONE HUNDRED AND TWENTY THREE AND CENTS FORTY FIVE
	 * ONLY 1000102.03=ONE MILLION ONE HUNDRED AND TWO AND CENTS THREE ONLY
	 * @param args
	 */
	public static void main(String[] args) {
		String[] num = new String[] { "01", "09", "11", "19", "20", "90", "21",
				"99", "100", "1000", "10000", "100000", "1000000", "10000000",
				"101", "109", "120", "121", "199", "999", "1001", "1020",
				"1021", "9999", "2000", "2001", "10001", "12001", "123456789",
				"100000001", "100.1", "120.9", "123.456", "1000102.03", "5920","5920.1","5920.12","5920.124","5920.12365","5920.00000" };
		for (int i = 0; i < num.length; i++) {
			//AppUtils.debug(num[i] + "=" + AmountUtil.parse2En(num[i]));
		}
	}

	public static String parse2En(String x) {
		int z = x.indexOf("."); // 取小数点位置
		String lstr = "", rstr = "";
		if (z > -1) { // 看是否有小数，如果有，则分别取左边和右边
			lstr = x.substring(0, z);
			rstr = x.substring(z + 1);
		} else
			// 否则就是全部
			lstr = x;

		String lstrrev = reverse(lstr); // 对左边的字串取反
		String[] a = new String[5]; // 定义5个字串变量来存放解析出来的叁位一组的字串

		switch (lstrrev.length() % 3) {
		case 1:
			lstrrev += "00";
			break;
		case 2:
			lstrrev += "0";
			break;
		}
		String lm = ""; // 用来存放转换後的整数部分
		for (int i = 0; i < lstrrev.length() / 3; i++) {
			a[i] = reverse(lstrrev.substring(3 * i, 3 * i + 3)); // 截取第一个叁位
			if (!a[i].equals("000")) { // 用来避免这种情况：1000000 = one million
										// thousand only
				if (i != 0)
					lm = transThree(a[i]) + " " + parseMore(String.valueOf(i))
							+ " " + lm; // 加: thousand、million、billion
				else
					lm = transThree(a[i]); // 防止i=0时， 在多加两个空格.
			} else
				lm += transThree(a[i]);
		}

		String xs = ""; // 用来存放转换後小数部分
		if (z > -1){
			if("0".equals(rstr.trim())|| "00".equals(rstr.trim())|| "000".equals(rstr.trim())|| "0000".equals(rstr.trim())|| "00000".equals(rstr.trim())|| "000000".equals(rstr.trim())){
				
			}else{
				xs = "AND CENTS " + transTwo(rstr) + " "; // 小数部分存在时转换小数
			}
		}

		return lm.trim() + " " + xs + "ONLY";
	}

	private static String parseFirst(String s) {
		String[] a = new String[] { "", "ONE", "TWO", "THREE", "FOUR", "FIVE",
				"SIX", "SEVEN", "EIGHT", "NINE" };
		return a[Integer.parseInt(s.substring(s.length() - 1))];
	}

	private static String parseTeen(String s) {
		String[] a = new String[] { "TEN", "ELEVEN", "TWELEVE", "THIRTEEN",
				"FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN",
				"NINETEEN" };
		return a[Integer.parseInt(s) - 10];
	}

	private static String parseTen(String s) {
		String[] a = new String[] { "TEN", "TWENTY", "THIRTY", "FORTY",
				"FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY" };
		return a[Integer.parseInt(s.substring(0, 1)) - 1];
	}

	// 两位
	private static String transTwo(String s) {
		String value = "";
		// 判断位数
		if (s.length() > 2)
			s = s.substring(0, 2);
		else if (s.length() < 2)
			s = "0" + s;

		if (s.startsWith("0")) // 07 - seven 是否小於10
			value = parseFirst(s);
		else if (s.startsWith("1")) // 17 seventeen 是否在10和20之间
			value = parseTeen(s);
		else if (s.endsWith("0")) // 是否在10与100之间的能被10整除的数
			value = parseTen(s);
		else
			value = parseTen(s) + " " + parseFirst(s);
		return value;
	}

	private static String parseMore(String s) {
		String[] a = new String[] { "", "THOUSAND", "MILLION", "BILLION" };
		return a[Integer.parseInt(s)];
	}

	// 制作叁位的数
	// s.length = 3
	private static String transThree(String s) {
		String value = "";
		if (s.startsWith("0")) // 是否小於100
			value = transTwo(s.substring(1));
		else if (s.substring(1).equals("00")) // 是否被100整除
			value = parseFirst(s.substring(0, 1)) + " HUNDRED";
		else
			value = parseFirst(s.substring(0, 1)) + " HUNDRED AND "
					+ transTwo(s.substring(1));
		return value;
	}

	private static String reverse(String s) {
		char[] aChr = s.toCharArray();
		StringBuffer tmp = new StringBuffer();
		for (int i = aChr.length - 1; i >= 0; i--) {
			tmp.append(aChr[i]);
		}
		return tmp.toString();
	}
}