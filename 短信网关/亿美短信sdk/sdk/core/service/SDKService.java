package com.base.sms.server.emay.sdk.core.service;

import com.base.sms.server.emay.sdk.core.dto.sms.common.ResultModel;
import com.base.sms.server.emay.sdk.core.dto.sms.request.*;
import com.base.sms.server.emay.sdk.core.dto.sms.response.*;
import com.base.sms.server.emay.sdk.util.exception.SDKParamsException;

public interface SDKService {

	public ResultModel<RetrieveReportResponse> retrieveReport(String appId, String timestamp, String sign, String host, RetrieveReportRequest reportRequest);

	/**
	 * 发送单条短信
	 * 
	 * @return
	 */
	public ResultModel<SmsResponse> sendSingleSms(String appId, String secretKey, String host, SmsSingleRequest request);

	/**
	 * 发送批次短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendBatchOnlySms(String appId, String secretKey, String host, SmsBatchOnlyRequest request);

	/**
	 * 发送批次短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendBatchSms(String appId, String secretKey, String host, SmsBatchRequest request) throws SDKParamsException;

	/**
	 * 发送个性短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendPersonalitySms(String appId, String secretKey, String host, SmsPersonalityRequest request) throws SDKParamsException;

	/**
	 * 发送批次短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendPersonalityAllSms(String appId, String secretKey, String host, SmsPersonalityAllRequest request) throws SDKParamsException;

	/**
	 * 获取余额
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<BalanceResponse> getBalance(String appId, String secretKey, String host, BalanceRequest request);

	/**
	 * 获取状态报告
	 * @param appId
	 * @param secretKey
	 * @param host
	 * @param reportRequest
	 * @return
	 */
	public ResultModel<ReportResponse[]> getReport(String appId, String secretKey, String host, ReportRequest reportRequest);

	/**
	 * 获取上行短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<MoResponse[]> getMo(String appId, String secretKey, String host, MoRequest request);

}
