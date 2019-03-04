package com.g5.sky.wxpay.request;

import java.util.SortedMap;
import java.util.TreeMap;

import static com.g5.sky.wxpay.utils.WxPayUtils.generateNonceStr;
import static com.g5.sky.wxpay.utils.WxPayUtils.generateSignature;
import static com.g5.sky.wxpay.utils.WxPayUtils.map2xml;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 5:37 PM
 */
public class WxMiniPayRefundRequest {

    public String toXml(String key) {

        SortedMap<String, String> map = new TreeMap<>();
        map.put("appid", this.appid);
        map.put("mch_id", this.mch_id);
        map.put("nonce_str", this.nonce_str);
        map.put("sign_type", this.sign_type);
        map.put("transaction_id", this.transaction_id);
        map.put("out_trade_no", this.out_trade_no);
        map.put("out_refund_no", this.out_refund_no);
        map.put("total_fee", this.total_fee + "");
        map.put("refund_fee", this.refund_fee + "");
        map.put("refund_fee_type", this.refund_fee_type);
        map.put("refund_desc", this.refund_desc);
        map.put("refund_account", this.refund_account);
        map.put("notify_url", this.notify_url);

        String sign = generateSignature(map, key);
        if (sign == null) return null;

        this.sign = sign;
        map.put("sign", this.sign);

        return map2xml(map);
    }

    public WxMiniPayRefundRequest() {
    }

    public WxMiniPayRefundRequest(
            String appid, String mch_id, String transaction_id,
            String out_trade_no, String out_refund_no, int total_fee, int refund_fee) {
        this.appid = appid;
        this.mch_id = mch_id;
        this.transaction_id = transaction_id;
        this.out_trade_no = out_trade_no;
        this.out_refund_no = out_refund_no;
        this.total_fee = total_fee;
        this.refund_fee = refund_fee;
    }

    // 小程序ID	appid	是	String(32)	wx8888888888888888	微信分配的小程序ID
    private String appid;

    // 商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
    private String mch_id;

    // 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位。推荐随机数生成算法
    private String nonce_str = generateNonceStr();;

    // 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名生成算法
    private String sign;

    // 签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
    private String sign_type = "MD5";

    // 微信订单号	transaction_id	二选一	String(32)	1217752501201407033233368018	微信生成的订单号，在支付通知中有返回
    private String transaction_id;

    // 商户订单号	out_trade_no	String(32)	1217752501201407033233368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
    private String out_trade_no;

    // 商户退款单号	out_refund_no	是	String(64)	1217752501201407033233368018	商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
    private String out_refund_no;

    // 订单金额	total_fee	是	Int	100	订单总金额，单位为分，只能为整数，详见支付金额
    private int total_fee;

    // 退款金额	refund_fee	是	Int	100	退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
    private int refund_fee;

    // 货币种类	refund_fee_type	否	String(8)	CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    private String refund_fee_type = "CNY";

    // 退款原因	refund_desc	否	String(80)	商品已售完	若商户传入，会在下发给用户的退款消息中体现退款原因
    private String refund_desc;

    // 退款资金来源	refund_account	否	String(30)	REFUND_SOURCE_RECHARGE_FUNDS    仅针对老资金流商户使用,REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）,REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
    private String refund_account;

    // 退款结果通知url	notify_url	否	String(256)	https://weixin.qq.com/notify/	异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数, 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
    private String notify_url;

    public String getAppid() {
        return appid;
    }

    public WxMiniPayRefundRequest setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getMch_id() {
        return mch_id;
    }

    public WxMiniPayRefundRequest setMch_id(String mch_id) {
        this.mch_id = mch_id;
        return this;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public WxMiniPayRefundRequest setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public WxMiniPayRefundRequest setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getSign_type() {
        return sign_type;
    }

    public WxMiniPayRefundRequest setSign_type(String sign_type) {
        this.sign_type = sign_type;
        return this;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public WxMiniPayRefundRequest setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
        return this;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public WxMiniPayRefundRequest setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
        return this;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public WxMiniPayRefundRequest setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
        return this;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public WxMiniPayRefundRequest setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
        return this;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public WxMiniPayRefundRequest setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
        return this;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public WxMiniPayRefundRequest setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
        return this;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public WxMiniPayRefundRequest setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
        return this;
    }

    public String getRefund_account() {
        return refund_account;
    }

    public WxMiniPayRefundRequest setRefund_account(String refund_account) {
        this.refund_account = refund_account;
        return this;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public WxMiniPayRefundRequest setNotify_url(String notify_url) {
        this.notify_url = notify_url;
        return this;
    }
}
