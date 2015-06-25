/**
 * 
 */
package com.family.familyedu.util;

/**
 * BASE64编码类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2013-1-31 下午7:16:16
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author dingyj@c-platform.com
 * @version 1.0.0
 */
public final class Base64 {

	/** */
	private static final int BASELENGTH = 255;

	/** */
	private static final int LOOKUPLENGTH = 64;

	/** */
	private static final int TWENTYFOURBITGROUP = 24;

	/** */
	private static final int EIGHTBIT = 8;

	/** */
	private static final int SIXTEENBIT = 16;

	/** */
	private static final int FOURBYTE = 4;

	/** The sign bit as an int */
	private static final int SIGN = -128;

	/** The padding character */
	private static final byte PAD = (byte) '=';

	/** base 64 table */
	final static String baseTable = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	/** The alphabet */
	private static final byte[] BASE64_ALPHABET = new byte[BASELENGTH];

	/** The lookup alphabet */
	private static final byte[] LOOKUP_BASE64_ALPHABET = new byte[LOOKUPLENGTH];

	static {

		for (int i = 0; i < BASELENGTH; i++) {
			BASE64_ALPHABET[i] = -1;
		}
		for (int i = 'Z'; i >= 'A'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - 'A');
		}
		for (int i = 'z'; i >= 'a'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - 'a' + 26);
		}

		for (int i = '9'; i >= '0'; i--) {
			BASE64_ALPHABET[i] = (byte) (i - '0' + 52);
		}

		BASE64_ALPHABET['+'] = 62;
		BASE64_ALPHABET['/'] = 63;

		for (int i = 0; i <= 25; i++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('A' + i);
		}

		for (int i = 26, j = 0; i <= 51; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('a' + j);
		}

		for (int i = 52, j = 0; i <= 61; i++, j++) {
			LOOKUP_BASE64_ALPHABET[i] = (byte) ('0' + j);
		}
		LOOKUP_BASE64_ALPHABET[62] = (byte) '+';
		LOOKUP_BASE64_ALPHABET[63] = (byte) '/';

	}

	/**
	 * Return true if the specified octect is base64
	 * 
	 * @param octect
	 *            The octet to test.
	 * @return boolean True if the octect is base64.
	 */
	static boolean isBase64(byte octect) {
		// Should we ignore white space?
		return (octect == PAD || BASE64_ALPHABET[octect] != -1);
	}

	/**
	 * Encodes hex octects into Base64
	 * 
	 * @param binaryData
	 *            Array containing binaryData
	 * @return Base64-encoded array
	 */
	public static byte[] encode(byte[] binaryData) {

		int lengthDataBits = binaryData.length * EIGHTBIT;
		int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
		int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
		byte encodedData[] = null;

		if (fewerThan24bits != 0) { // data not divisible by 24 bit
			encodedData = new byte[(numberTriplets + 1) * 4];
		} else { // 16 or 8 bit
			encodedData = new byte[numberTriplets * 4];
		}

		byte k = 0;
		byte l = 0;
		byte b1 = 0;
		byte b2 = 0;
		byte b3 = 0;
		int encodedIndex = 0;
		int dataIndex = 0;
		int i = 0;
		for (i = 0; i < numberTriplets; i++) {

			dataIndex = i * 3;
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			b3 = binaryData[dataIndex + 2];

			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			encodedIndex = i * 4;
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);

			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
			byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2 | (k << 4)];
			encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[(l << 2) | val3];
			encodedData[encodedIndex + 3] = LOOKUP_BASE64_ALPHABET[b3 & 0x3f];
		}

		// form integral number of 6-bit groups
		dataIndex = i * 3;
		encodedIndex = i * 4;
		if (fewerThan24bits == EIGHTBIT) {
			b1 = binaryData[dataIndex];
			k = (byte) (b1 & 0x03);
			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[k << 4];
			encodedData[encodedIndex + 2] = PAD;
			encodedData[encodedIndex + 3] = PAD;
		} else if (fewerThan24bits == SIXTEENBIT) {
			b1 = binaryData[dataIndex];
			b2 = binaryData[dataIndex + 1];
			l = (byte) (b2 & 0x0f);
			k = (byte) (b1 & 0x03);

			byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
			byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

			encodedData[encodedIndex] = LOOKUP_BASE64_ALPHABET[val1];
			encodedData[encodedIndex + 1] = LOOKUP_BASE64_ALPHABET[val2 | (k << 4)];
			encodedData[encodedIndex + 2] = LOOKUP_BASE64_ALPHABET[l << 2];
			encodedData[encodedIndex + 3] = PAD;
		}
		return encodedData;
	}

	/**
	 * Decodes Base64 data into octects
	 * 
	 * @param base64Data
	 *            byte array containing Base64 data
	 * @return Array containing decoded data.
	 */
	public static byte[] decode(byte[] base64Data) {
		// Should we throw away anything not in base64Data ?

		// handle the edge case, so we don't have to worry about it later
		if (base64Data.length == 0) {
			return new byte[0];
		}

		int numberQuadruple = base64Data.length / FOURBYTE;
		byte decodedData[] = null;
		byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;

		int encodedIndex = 0;
		int dataIndex = 0;
		{
			// this block sizes the output array properly - rlw
			int lastData = base64Data.length;
			// ignore the '=' padding
			while (base64Data[lastData - 1] == PAD) {
				if (--lastData == 0) {
					return new byte[0];
				}
			}
			decodedData = new byte[lastData - numberQuadruple];
		}

		for (int i = 0; i < numberQuadruple; i++) {
			dataIndex = i * 4;
			marker0 = base64Data[dataIndex + 2];
			marker1 = base64Data[dataIndex + 3];

			b1 = BASE64_ALPHABET[base64Data[dataIndex]];
			b2 = BASE64_ALPHABET[base64Data[dataIndex + 1]];

			if (marker0 != PAD && marker1 != PAD) { // No PAD e.g 3cQl
				b3 = BASE64_ALPHABET[marker0];
				b4 = BASE64_ALPHABET[marker1];

				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
			} else if (marker0 == PAD) { // Two PAD e.g. 3c[Pad][Pad]
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
			} else if (marker1 == PAD) { // One PAD e.g. 3cQ[Pad]
				b3 = BASE64_ALPHABET[marker0];
				decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex + 1] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
			}
			encodedIndex += 3;
		}
		return decodedData;
	}

	// Add by JimmyChen @20110308 begin

	/**
	 * Encode a byte array.
	 * 
	 * @param bytes
	 *            a byte array to be encoded.
	 * @return encoded object as a String object.
	 */
	// public static String encodeToString(byte[] bytes) {
	// StringBuffer tmp = new StringBuffer();
	// int i = 0;
	// byte pos;
	//
	// for(i=0; i < (bytes.length - bytes.length%3); i+=3) {
	//
	// pos = (byte) ((bytes[i] >> 2) & 63);
	// tmp.append(baseTable.charAt(pos));
	//
	// pos = (byte) (((bytes[i] & 3) << 4) + ((bytes[i+1] >> 4) & 15));
	// tmp.append(baseTable.charAt( pos ));
	//
	// pos = (byte) (((bytes[i+1] & 15) << 2) + ((bytes[i+2] >> 6) & 3));
	// tmp.append(baseTable.charAt(pos));
	//
	// pos = (byte) (((bytes[i+2]) & 63));
	// tmp.append(baseTable.charAt(pos));
	//
	// // Add a new line for each 76 chars.
	// // 76*3/4 = 57
	// if(((i+2)%56) == 0) {
	// tmp.append("\r\n");
	// }
	// }
	//
	// if(bytes.length % 3 != 0) {
	//
	// if(bytes.length % 3 == 2) {
	//
	// pos = (byte) ((bytes[i] >> 2) & 63);
	// tmp.append(baseTable.charAt(pos));
	//
	// pos = (byte) (((bytes[i] & 3) << 4) + ((bytes[i+1] >> 4) & 15));
	// tmp.append(baseTable.charAt( pos ));
	//
	// pos = (byte) ((bytes[i+1] & 15) << 2);
	// tmp.append(baseTable.charAt(pos));
	//
	// tmp.append("=");
	//
	// } else if(bytes.length % 3 == 1) {
	//
	// pos = (byte) ((bytes[i] >> 2) & 63);
	// tmp.append(baseTable.charAt(pos));
	//
	// pos = (byte) ((bytes[i] & 3) << 4);
	// tmp.append(baseTable.charAt( pos ));
	//
	// tmp.append("==");
	// }
	// }
	// return tmp.toString();
	//
	// }

	// Add by JimmyChen @20110308 end
}
