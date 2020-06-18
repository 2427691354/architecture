/*
 * Copyright (c) 2017, XIANDIAN and/or its affiliates. All rights reserved.
 * XIANDIAN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.lixing.util;

//import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.Base64;

//simport org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * 工具类，提供公共方法
 * 
 * @author XuanHuiDong
 * @since V2.0
 * 
 */

public class UtilTools {

	public static int byte2Int(byte b) {
		return b & 0xFF;
	}

	public static String addZero(String number) {
		if (number.length() == 1)
			return "0" + number;
		else
			return number;
	}

	public static String removeZero(String number) {
		if (number.equals("0"))
			return "0";
		else if (number.equals("00"))
			return "0";
		else if (number.equals("000"))
			return "0";
		else if (number.equals("0000"))
			return "0";
		else if (number.equals("00000"))
			return "0";
		else if (number.equals("000000"))
			return "0";
		else if (number.equals("0000000"))
			return "0";
		else if (number.equals("00000000"))
			return "0";
		else
			while (number.startsWith("0"))
				number = number.substring(1, number.length());
		return number;
	}

}
