package com.example.alipay.vo;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
	/**
	 * 成功
	 */
	SUCCESS(true, 20000,"成功"),
	UNKNOWN_REASON(false, 20001, "未知错误"),

	LOGIN_PHONE_ERRROR(false, 20002, "手机号码不能为空"),
	ACCOUNT_PHONE_ERRROR(false, 20002, "账号信息不能为空"),
	LOGIN_PHONE_PATTARN_ERRROR(false, 20003, "手机号码格式不正确"),
	VALIDATION_CODE_ERROR(false, 20004, "验证码不正确"),
	LOGIN_CODE_ERROR(false, 20005, "短信验证码不能为空"),
	LOGIN_CAPATA_ERROR(false, 20006, "图形验证码不能为空"),
	LOGIN_CODE_FAIL_ERROR(false, 20007, "短信验证码失效，请重新发送"),
	LOGIN_CODE_INPUT_ERROR(false, 20008, "输入的短信码有误"),
	PHONE_ERROR_MSG(false, 20009, "该手机号未绑定账户"),
	USER_FORBIDDEN(false, 20010, "该用户已被禁用，请联系平台客服"),
	LOGIN_PWD_ERROR(false, 200011, "密码不允许为空"),
	LOGIN_PWD_INPUT_ERROR(false, 200012, "密码输入有误"),
	LOGIN_PWD_NO_INPUT_ERROR(false, 200013, "检测到没有完善密码信息"),


	BAD_SQL_GRAMMAR(false, 21001, "sql语法错误"),
	JSON_PARSE_ERROR(false, 21002, "json解析异常"),
	PARAM_ERROR(false, 21003, "参数不正确"),
	USER_PWD_ERROR(false, 21003, "尚未找到对应的用户信息");


	private Boolean success;
	private Integer code;
	private String message;

	ResultCodeEnum(Boolean success, Integer code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}
}