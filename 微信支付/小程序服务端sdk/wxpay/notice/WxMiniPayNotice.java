package com.g5.sky.wxpay.notice;

import java.util.*;

import static com.g5.sky.wxpay.utils.WxPayUtils.generateSignature;
import static com.g5.sky.wxpay.utils.WxPayUtils.parseInt;
import static com.g5.sky.wxpay.utils.WxPayUtils.xml2map;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 6:54 PM
 */
public class WxMiniPayNotice {

    public boolean verifySign(String xml, String key) {
        Map<String, String> map = xml2map(xml);

        String wx_sign = map.get("sign");
        String wx_sign_type = map.get("sign_type");

        SortedMap<String, String> signParams = new TreeMap<>();
        signParams.putAll(map);

        signParams.remove("sign");

        String sign = generateSignature(map, key);

        if (wx_sign == null || !wx_sign.equals(sign)) return false;

        return true;
    }

    public WxMiniPayNotice(String xml) {

        Map<String, String> map = xml2map(xml);

        if (map == null) {
            this.return_code = "FAIL";
            this.return_msg = "使用通知 xml 构建微信支付通知实体失败";
            return;
        }

        this.return_code = map.get("return_code");
        this.return_msg = map.get("return_msg");

        if (!"SUCCESS".equalsIgnoreCase(this.return_code)) {
            this.returnSuccess = null;
            return;
        }

        this.returnSuccess = this.new ReturnSuccess();

        this.returnSuccess.appid = map.get("appid");
        this.returnSuccess.mch_id = map.get("mch_id");
        this.returnSuccess.device_info = map.get("device_info");
        this.returnSuccess.nonce_str = map.get("nonce_str");
        this.returnSuccess.sign = map.get("sign");
        this.returnSuccess.sign_type = map.get("sign_type");
        this.returnSuccess.result_code = map.get("result_code");
        this.returnSuccess.err_code = map.get("err_code");
        this.returnSuccess.err_code_des = map.get("err_code_des");
        this.returnSuccess.openid = map.get("openid");
        this.returnSuccess.is_subscribe = map.get("is_subscribe");
        this.returnSuccess.trade_type = map.get("trade_type");
        this.returnSuccess.bank_type = map.get("bank_type");
        this.returnSuccess.total_fee = parseInt(map.get("total_fee"), -99);
        this.returnSuccess.settlement_total_fee = parseInt(map.get("settlement_total_fee"), -99);
        this.returnSuccess.fee_type = map.get("fee_type");
        this.returnSuccess.cash_fee = parseInt(map.get("cash_fee"), -99);
        this.returnSuccess.cash_fee_type = map.get("cash_fee_type");
        this.returnSuccess.coupon_fee = parseInt(map.get("coupon_fee"), -99);
        this.returnSuccess.coupon_count = parseInt(map.get("coupon_count"), -1);
        this.returnSuccess.transaction_id = map.get("transaction_id");
        this.returnSuccess.out_trade_no = map.get("out_trade_no");
        this.returnSuccess.attach = map.get("attach");
        this.returnSuccess.time_end = map.get("time_end");

        for (int i=0; i < this.returnSuccess.coupon_count; i++) {
            if (this.returnSuccess.coupons == null) this.returnSuccess.coupons = new ArrayList<>();
            String coupon_type = map.get("coupon_type_" + i);
            int coupon_fee = parseInt(map.get("coupon_fee_" + i), -99);
            String coupon_id = map.get("coupon_id_" + i);
            this.returnSuccess.coupons.add(new Coupon(coupon_type, coupon_fee, coupon_id));
        }
    }

    public WxMiniPayNotice() {
    }

    // 返回状态码	return_code	是	String(16)	SUCCESS	    SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String return_code;

    // 返回信息	return_msg	否	String(128)	签名失败	    返回信息，如非空，为错误原因 签名失败 参数格式校验错误
    private String return_msg;

    // 在return_code为SUCCESS的时候有返回
    private ReturnSuccess returnSuccess;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public ReturnSuccess getReturnSuccess() {
        return returnSuccess;
    }

    public void setReturnSuccess(ReturnSuccess returnSuccess) {
        this.returnSuccess = returnSuccess;
    }

    public class ReturnSuccess {

        // 小程序ID	appid	是	String(32)	wx8888888888888888	微信分配的小程序ID
        private String appid;

        // 商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
        private String mch_id;

        // 设备号	device_info	否	String(32)	013467007045764	微信支付分配的终端设备号，
        private String device_info;

        // 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位
        private String nonce_str;

        // 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	签名，详见签名算法
        private String sign;

        // 签名类型	sign_type	否	String(32)	HMAC-SHA256	签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        private String sign_type;

        // 业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
        private String result_code;

        // 错误代码	err_code	否	String(32)	SYSTEMERROR	错误返回的信息描述
        private String err_code;

        // 错误代码描述	err_code_des	否	String(128)	系统错误	错误返回的信息描述
        private String err_code_des;

        // 用户标识	openid	是	String(128)	wxd930ea5d5a258f4f	用户在商户appid下的唯一标识
        private String openid;

        // 是否关注公众账号	is_subscribe	是	String(1)	Y	用户是否关注公众账号，Y-关注，N-未关注
        private String is_subscribe;

        // 交易类型	trade_type	是	String(16)	JSAPI	JSAPI、NATIVE、APP
        private String trade_type;

        // 付款银行	bank_type	是	String(16)	CMC	银行类型，采用字符串类型的银行标识，银行类型见银行列表
        private String bank_type;

        // 订单金额	total_fee	是	Int	100	订单总金额，单位为分
        private int total_fee;

        // 应结订单金额	settlement_total_fee	否	Int	100	应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
        private int settlement_total_fee;

        // 货币种类	fee_type	否	String(8)	CNY	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        private String fee_type;

        // 现金支付金额	cash_fee	是	Int	100	现金支付金额订单现金支付金额，详见支付金额
        private int cash_fee;

        // 现金支付货币类型	cash_fee_type	否	String(16)	CNY	货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
        private String cash_fee_type;

        // 总代金券金额	coupon_fee	否	Int	10	代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
        private int coupon_fee;

        // 代金券使用数量	coupon_count	否	Int	1	代金券使用数量
        private int coupon_count;

        // 微信支付订单号	transaction_id	是	String(32)	1217752501201407033233368018	微信支付订单号
        private String transaction_id;

        // 商户订单号	out_trade_no	是	String(32)	1212321211201407033568112322	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
        private String out_trade_no;

        // 商家数据包	attach	否	String(128)	123456	商家数据包，原样返回
        private String attach;

        // 支付完成时间	time_end	是	String(14)	20141030133525	支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
        private String time_end;

        // 代金券
        private List<Coupon> coupons;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getDevice_info() {
            return device_info;
        }

        public void setDevice_info(String device_info) {
            this.device_info = device_info;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign_type() {
            return sign_type;
        }

        public void setSign_type(String sign_type) {
            this.sign_type = sign_type;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getErr_code() {
            return err_code;
        }

        public void setErr_code(String err_code) {
            this.err_code = err_code;
        }

        public String getErr_code_des() {
            return err_code_des;
        }

        public void setErr_code_des(String err_code_des) {
            this.err_code_des = err_code_des;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getIs_subscribe() {
            return is_subscribe;
        }

        public void setIs_subscribe(String is_subscribe) {
            this.is_subscribe = is_subscribe;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getBank_type() {
            return bank_type;
        }

        public void setBank_type(String bank_type) {
            this.bank_type = bank_type;
        }

        public int getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(int total_fee) {
            this.total_fee = total_fee;
        }

        public int getSettlement_total_fee() {
            return settlement_total_fee;
        }

        public void setSettlement_total_fee(int settlement_total_fee) {
            this.settlement_total_fee = settlement_total_fee;
        }

        public String getFee_type() {
            return fee_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public int getCash_fee() {
            return cash_fee;
        }

        public void setCash_fee(int cash_fee) {
            this.cash_fee = cash_fee;
        }

        public String getCash_fee_type() {
            return cash_fee_type;
        }

        public void setCash_fee_type(String cash_fee_type) {
            this.cash_fee_type = cash_fee_type;
        }

        public int getCoupon_fee() {
            return coupon_fee;
        }

        public void setCoupon_fee(int coupon_fee) {
            this.coupon_fee = coupon_fee;
        }

        public int getCoupon_count() {
            return coupon_count;
        }

        public void setCoupon_count(int coupon_count) {
            this.coupon_count = coupon_count;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getAttach() {
            return attach;
        }

        public void setAttach(String attach) {
            this.attach = attach;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public List<Coupon> getCoupons() {
            return coupons;
        }

        public void setCoupons(List<Coupon> coupons) {
            this.coupons = coupons;
        }
    }

    public class Coupon {

        // 代金券类型	coupon_type_$n	否	String	CASH    CASH--充值代金券 NO_CASH---非充值代金券 并且订单使用了免充值券后有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
        private String coupon_type;

        // 单个代金券支付金额	coupon_fee_$n	否	Int	100	单个代金券支付金额,$n为下标，从0开始编号
        private int coupon_fee;

        // 代金券ID	coupon_id_$n	否	String(20)	10000	代金券ID,$n为下标，从0开始编号
        private String coupon_id;

        public Coupon() {
        }

        public Coupon(String coupon_type, int coupon_fee, String coupon_id) {
            this.coupon_type = coupon_type;
            this.coupon_fee = coupon_fee;
            this.coupon_id = coupon_id;
        }

        public String getCoupon_type() {
            return coupon_type;
        }

        public void setCoupon_type(String coupon_type) {
            this.coupon_type = coupon_type;
        }

        public int getCoupon_fee() {
            return coupon_fee;
        }

        public void setCoupon_fee(int coupon_fee) {
            this.coupon_fee = coupon_fee;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }
    }
}
