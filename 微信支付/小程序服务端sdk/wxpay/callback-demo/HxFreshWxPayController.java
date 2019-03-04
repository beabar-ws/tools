package com.g5.sky.fresh.controller;

import com.alibaba.fastjson.JSON;
import com.g5.sky.common.util.IdUtils;
import com.g5.sky.fresh.domain.HxFreshOrderWxpayEntity;
import com.g5.sky.fresh.domain.HxFreshOrderWxrefundEntity;
import com.g5.sky.fresh.service.HxFreshOrderService;
import com.g5.sky.fresh.service.HxFreshWxPayService;
import com.g5.sky.wxpay.notice.WxMiniPayNotice;
import com.g5.sky.wxpay.notice.WxMiniPayNoticeRe;
import com.g5.sky.wxpay.notice.WxMiniPayRefundNotice;
import com.g5.sky.wxpay.notice.WxMiniPayRefundNoticeRe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.g5.sky.common.constants.WxPayConstants.*;

/**
 * todo 注释
 *
 * @author shuai . 2018/11/9 . 11:09 AM
 */
@RestController
@RequestMapping("/notify/wx")
public class HxFreshWxPayController {

    @Value("${xl.wx.AppID}")
    private String wxAppid;

    @Value("${xl.wx.AppSecret}")
    private String wxAppSecret;

    @Value("${xl.wx.AppKey}")
    private String wxAppKey;

    @Value("${xl.wx.MCHID}")
    private String wxMCHID;

    private static Logger logger = LoggerFactory.getLogger(HxFreshWxPayController.class);

    private static final String success = "SUCCESS";
    private static final String fail = "FAIL";

    @Autowired
    private HxFreshWxPayService payService;

    @Autowired
    private HxFreshOrderService orderService;

    @RequestMapping("/pay")
    public void wxnotifyPay(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BufferedReader wx_br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder wx_sb = new StringBuilder();
        while ((line = wx_br.readLine()) != null) {
            wx_sb.append(line);
        }

        String requXml = wx_sb.toString();
        logger.info("===> 华夏生鲜：微信支付回调：收到微信支付回调通知，requXml=" + requXml);

        WxMiniPayNotice wxMiniPayNotice = new WxMiniPayNotice(requXml);

        String return_code = wxMiniPayNotice.getReturn_code();
        String return_msg = wxMiniPayNotice.getReturn_msg();

        if (!success.equalsIgnoreCase(return_code)) {
            logger.info("===> 华夏生鲜：微信支付回调：通信失败，return_code::return_msg::requXml=" + return_code + "::" + return_msg + "::" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, return_msg);
            wxMiniPayNoticeRe.response(response);
            return;
        }

        boolean verifySign = wxMiniPayNotice.verifySign(requXml, wxAppKey);
        if (!verifySign) {
            logger.info("===> 华夏生鲜：微信支付回调：验签失败，requXml=" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, "验签失败");
            wxMiniPayNoticeRe.response(response);
            return;
        }

        WxMiniPayNotice.ReturnSuccess returnSuccess = wxMiniPayNotice.getReturnSuccess();
        if (returnSuccess == null) {
            logger.info("===> 华夏生鲜：微信支付回调：微信通知成功数据处理失败，requXml=" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, "微信通知成功数据处理失败");
            wxMiniPayNoticeRe.response(response);
            return;
        }

        String payId = returnSuccess.getOut_trade_no();
        HxFreshOrderWxpayEntity wxpayEntity = payService.queryPay(payId);
        if (wxpayEntity == null) {
            logger.info("===> 华夏生鲜：微信支付回调：未找到支付订单，payId::requXml=" + payId + "::" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, "未找到支付订单");
            wxMiniPayNoticeRe.response(response);
            return;
        }

        int totalFee = returnSuccess.getTotal_fee();
        int payAmount = wxpayEntity.getPayAmount();
        if (payAmount != totalFee) {
            logger.info("===> 华夏生鲜：微信支付回调：订单金额校验未通过，payId::payAmount::totalFee::requXml=" + payId + "::" + payAmount + "::" + totalFee + "::" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, "金额不正确");
            wxMiniPayNoticeRe.response(response);
            return;
        }

        String result_code = returnSuccess.getResult_code();
        String err_code = returnSuccess.getErr_code();
        String err_code_des = returnSuccess.getErr_code_des();
        String transaction_id = returnSuccess.getTransaction_id();
        String time_end = returnSuccess.getTime_end();
        if (!success.equalsIgnoreCase(result_code)) {
            logger.info("===> 华夏生鲜：微信支付回调：支付失败，payId::err_code::err_code_des::requXml=" + payId + "::" + err_code + "::" + err_code_des + "::" + requXml);
            WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(fail, err_code + "::" + err_code_des);
            payService.updatePayForNotice(payId, paystatus_payfail, transaction_id, time_end, JSON.toJSONString(wxMiniPayNotice), JSON.toJSONString(wxMiniPayNoticeRe));
            wxMiniPayNoticeRe.response(response);
            return;
        }

        logger.info("===> 华夏生鲜：微信支付回调：支付成功，payId::requXml=" + payId + "::" + requXml);

        WxMiniPayNoticeRe wxMiniPayNoticeRe = new WxMiniPayNoticeRe(success, success);
        payService.updatePayForNotice(payId, paystatus_paysucess, transaction_id, time_end, JSON.toJSONString(wxMiniPayNotice), JSON.toJSONString(wxMiniPayNoticeRe));
        orderService.paySuccess(wxpayEntity.getOrderId(), JSON.toJSONString(wxMiniPayNotice));
        wxMiniPayNoticeRe.response(response);
        return;
    }

    @RequestMapping("/refund")
    public void wxnotifyrefund(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BufferedReader wx_br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line;
        StringBuilder wx_sb = new StringBuilder();
        while ((line = wx_br.readLine()) != null) {
            wx_sb.append(line);
        }

        String requXml = wx_sb.toString();
        logger.info("===> 华夏生鲜：微信退款回调：收到微信退款回调通知，requXml=" + requXml);

        WxMiniPayRefundNotice wxMiniPayRefundNotice = new WxMiniPayRefundNotice(requXml, wxAppKey);

        String return_code = wxMiniPayRefundNotice.getReturn_code();
        String return_msg = wxMiniPayRefundNotice.getReturn_msg();

        if (!success.equalsIgnoreCase(return_code)) {
            logger.info("===> 华夏生鲜：微信退款回调：通信失败，return_code::return_msg::requXml=" + return_code + "::" + return_msg + "::" + requXml);
            WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(fail, return_msg);
            wxMiniPayRefundNoticeRe.response(response);
            return;
        }

        WxMiniPayRefundNotice.ReturnSuccess returnSuccess = wxMiniPayRefundNotice.getReturnSuccess();
        if (returnSuccess == null) {
            logger.info("===> 华夏生鲜：微信退款回调：微信通知成功数据处理失败，requXml=" + requXml);
            WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(fail, "微信通知成功数据处理失败");
            wxMiniPayRefundNoticeRe.response(response);
            return;
        }

        WxMiniPayRefundNotice.ReturnSuccess.ReqInfo reqInfo = returnSuccess.getReqInfo();
        if (reqInfo == null) {
            logger.info("===> 华夏生鲜：微信退款回调：微信通知加密数据处理失败，requXml=" + requXml);
            WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(fail, "微信通知加密数据处理失败");
            wxMiniPayRefundNoticeRe.response(response);
            return;
        }

        String refund_id = reqInfo.getRefund_id();
        String success_time = reqInfo.getSuccess_time();
        String refund_request_source = reqInfo.getRefund_request_source();
        String refund_status = reqInfo.getRefund_status();
        String out_trade_no = reqInfo.getOut_trade_no();
        String out_refund_no = reqInfo.getOut_refund_no();

        HxFreshOrderWxrefundEntity wxrefundEntity = payService.queryRefund(out_refund_no);
        if (wxrefundEntity == null) {
            logger.info("===> 华夏生鲜：微信退款回调：未查询到支付退款订单，refundId::requXml=" + out_refund_no + "::" + requXml);
            WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(fail, "未查询到支付退款订单");
            wxMiniPayRefundNoticeRe.response(response);
            return;
        }

        switch (refund_request_source) {
            case "API":
                logger.info("===> 华夏生鲜：微信退款回调：退款发起方式为 API，refundId::requXml=" + out_refund_no + "::" + requXml);

                String orderRefundId = wxrefundEntity.getOrderRefundId();

                if (!success.equalsIgnoreCase(refund_status)) {
                    logger.info("===> 华夏生鲜：微信退款回调：退款失败，refundId::refund_status::requXml=" + out_refund_no + "::" + refund_status + "::" + requXml);
                    WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(fail, refund_status);
                    payService.updateRefndForNotice(out_refund_no, refundstatus_refundfail, refund_id, success_time, JSON.toJSONString(wxMiniPayRefundNotice), JSON.toJSONString(wxMiniPayRefundNoticeRe));
                    orderService.refundsCallback(false, Long.parseLong(orderRefundId), JSON.toJSONString(wxMiniPayRefundNotice));
                    wxMiniPayRefundNoticeRe.response(response);
                    return;
                }

                logger.info("===> 华夏生鲜：微信退款回调：退款成功，refundId::requXml=" + out_refund_no + "::" + requXml);
                WxMiniPayRefundNoticeRe wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(success, success);
                payService.updateRefndForNotice(out_refund_no, refundstatus_refundsucess, refund_id, success_time, JSON.toJSONString(wxMiniPayRefundNotice), JSON.toJSONString(wxMiniPayRefundNoticeRe));
                orderService.refundsCallback(false, Long.parseLong(orderRefundId), JSON.toJSONString(wxMiniPayRefundNotice));
                wxMiniPayRefundNoticeRe.response(response);
                return;

            case "VENDOR_PLATFORM":
                logger.info("===> 华夏生鲜：微信退款回调：退款发起方式为 VENDOR_PLATFORM，requXml=" + requXml);

                HxFreshOrderWxpayEntity payEntity = payService.queryPay(out_trade_no);

                wxMiniPayRefundNoticeRe = new WxMiniPayRefundNoticeRe(success, success);

                // 补充创建退款订单

                int settlement_total_fee = reqInfo.getSettlement_total_fee();
                int total_fee = reqInfo.getTotal_fee();

                String refundId = IdUtils.dateramdom20id('0');

                HxFreshOrderWxrefundEntity refundEntity = new HxFreshOrderWxrefundEntity();
                refundEntity.setRefundId(refundId);
                refundEntity.setRefundAmount(settlement_total_fee);
                refundEntity.setRefundCreateDate(new Date());
                refundEntity.setPayId(out_trade_no);
                refundEntity.setPayAmount(total_fee);
                refundEntity.setOrderId(payEntity.getOrderId());
                refundEntity.setOrderRefundId(null);
                refundEntity.setOrderAmount(payEntity.getOrderAmount());
                refundEntity.setWxPreId(payEntity.getWxPreId());
                refundEntity.setWxTransactionId(payEntity.getWxTransactionId());
                refundEntity.setWxRefundId(refund_id);
                refundEntity.setRefundwxApplyRequest("商户平台退款");
                refundEntity.setRefundwxApplyResponse("商户平台退款");
                refundEntity.setRefundwxApplyFinishDate(null);
                refundEntity.setWxrefundNoticeStatus(refundnotice_received);
                refundEntity.setWxrefundNoticeRequest(JSON.toJSONString(wxMiniPayRefundNotice));
                refundEntity.setWxrefundNoticeResponse(JSON.toJSONString(wxMiniPayRefundNoticeRe));
                refundEntity.setWxrefundNoticeDate(new Date());

                Date finishDate = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    finishDate = sdf.parse(success_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                    logger.error("补充创建退款支付订单，微信退款成功时间转换失败", e);
                }

                refundEntity.setWxrefundFinishDate(finishDate);

                if (!success.equalsIgnoreCase(refund_status)) {
                    orderService.merchanTrefundsCallback(false, payEntity.getOrderId(), settlement_total_fee/100f, JSON.toJSONString(wxMiniPayRefundNotice));
                    refundEntity.setRefundStatus(refundstatus_refundfail);
                } else {
                    orderService.merchanTrefundsCallback(true, payEntity.getOrderId(), settlement_total_fee/100f, JSON.toJSONString(wxMiniPayRefundNotice));
                    refundEntity.setRefundStatus(refundstatus_refundsucess);
                }

                payService.saveRefundForNotice(refundEntity);

                logger.info("===> 华夏生鲜：微信退款回调：退款成功，refundId::requXml=" + refundId + "::" + requXml);

                wxMiniPayRefundNoticeRe.response(response);
                return;
            default:
                logger.info("===> 华夏生鲜：微信退款回调：未知退款来源，wx_out_trade_no=" + out_trade_no);
                return;
        }

    }

}
