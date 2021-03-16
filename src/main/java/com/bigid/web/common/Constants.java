package com.bigid.web.common;
public final class Constants {
	
	/* Avoid accidental instantiation of this class */
	private Constants(){}
	
	/*
	 * Blank or an empty String.
	 */
	public static String BLANK = "";
	public static final String COMMA =",";
	public static final String CEDILLA= "\u00C7";
	public static final String MUE = "\u00B5";
	public static final String BACKSLASH = "\\";
	public static final String FORWARDSLASH =  "/";
	public static final String HYPHEN = "-";
	public static final char DOUBLE_QUOTE = '"';
	public static final String UNDERSCORE = "_";
	public static final String DOT = ".";
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	public static final String ASTERISK = "*";
	public static final String AMPERSAND = "&";
	public static final String EQUAL = "=";
	public static final String SEMICOLON = ";";
	public static final String SINGLESPACE = " ";
	public static final String OPENINGSQUAREBRACKET = "[";
	public static final String CLOSINGSQUAREBRACKET = "]";
	public static final String QUESTION_MARK = "?";
	public static final String OPENING_SMALL_BRACKET = "(";
	public static final String CLOSING_SMALL_BRACKET = ")";
	public static final String WHITE_SPACE="  ";
	public static final String HASH="#";
	
	public static final String SPECIAL_CHARS="[~!@#$%^&*()_+=|\\:\";'<>?,./-]";
	public static final String SPECIAL_CHARS_OFFLINE="[~!@#$%^&*()+=|\\:\";'<>?,./]";

	public static final String NON_WORD_CHARS_FOR_SKU="[^a-zA-Z0-9-./_ ]";
	public static final String SPECIAL_CHARS_WITH_BRACKET = "[~!@#$%^&*()_+=|\\:\";'<>?,./\\[-\\]]";
	public static final String NON_WORD_CHARS="[^a-zA-Z0-9 ]";
	public static final String SPECIAL_CHARS_WITHOUT_HIPEN="[~!@#$%^&*()_+=|\\:\";'<>?,./]";
	
	public static final String Y = "Y";
	public static final String N = "N";
	public static final String USER_PROFILE = "profile";
	public static final String USER_POST = "post";
	
	
	/*--------------------Post related --------------------*/
	public static final String POST_DEFAULT_LISTING_CRITERIA ="latest";
	public static final int POST_DEFAULT_FETCH_SIZE = 10;
	
		/*--------------------Post Vote related --------------------*/
	public static final Integer ByDefaultPushAvail = 3;
	public static final Integer PUSH_AVAILABILITY_NO = 3;
	public static final Integer GET_PUSH_AVAILABILITY_NO = 1;
	public static final Integer PUSH_COUNT=1;
}