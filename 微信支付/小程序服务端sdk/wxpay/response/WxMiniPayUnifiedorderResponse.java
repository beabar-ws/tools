package com.g5.sky.wxpay.response;

import java.util.Map;

import static com.g5.sky.wxpay.utils.WxPayUtils.xml2map;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 11:55 AM
 */
public class WxMiniPayUnifiedorderResponse {

    public WxMiniPayUnifiedorderResponse() {
    }

    public WxMiniPayUnifiedorderResponse(String xml) {

        Map<String, String> map = xml2map(xml);

        if (map == null) {
            this.return_code = "FAIL";
            this.return_msg = "使用返回 xml 构建统一下单返回实体失败";
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
        this.returnSuccess.result_code = map.get("result_code");
        this.returnSuccess.err_code = map.get("err_code");
        this.returnSuccess.err_code_des = map.get("err_code_des");

        if (!"SUCCESS".equalsIgnoreCase(this.returnSuccess.result_code)) {
            this.returnSuccess.resultSuccess = null;
            return;
        }

        this.returnSuccess.resultSuccess = this.returnSuccess.new ResultSuccess();

        this.returnSuccess.resultSuccess.trade_type = map.get("trade_type");
        this.returnSuccess.resultSuccess.prepay_id = map.get("prepay_id");
        this.returnSuccess.resultSuccess.code_url = map.get("code_url");

    }

    // 返回状态码	return_code	是	String(16)	SUCCESS SUCCESS/FAIL，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String return_code;

    // 返回信息	return_msg	否	String(128)	签名失败    返回信息，如非空，为错误原因，签名失败
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

        // 小程序ID	appid	是	String(32)	wx8888888888888888	调用接口提交的小程序ID
        private String appid;

        // 商户号	mch_id	是	String(32)	1900000109	调用接口提交的商户号
        private String mch_id;

        // 设备号	device_info	否	String(32)	013467007045764	自定义参数，可以为请求支付的终端设备号等
        private String device_info;

        // 随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	微信返回的随机字符串
        private String nonce_str;

        // 签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	微信返回的签名值，详见签名算法
        private String sign;

        // 业务结果	result_code	是	String(16)	SUCCESS	SUCCESS/FAIL
        private String result_code;

        // 错误代码	err_code	否	String(32)	SYSTEMERROR	详细参见下文错误列表
        private String err_code;

        // 错误代码描述	err_code_des	否	String(128)	系统错误	错误信息描述
        private String err_code_des;

        // 在return_code 和result_code都为SUCCESS的时候有返回
        private ResultSuccess resultSuccess;

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

        public ResultSuccess getResultSuccess() {
            return resultSuccess;
        }

        public void setResultSuccess(ResultSuccess resultSuccess) {
            this.resultSuccess = resultSuccess;
        }

        public class ResultSuccess {

            // 交易类型	trade_type	是	String(16)	JSAPI	交易类型，取值为：JSAPI，NATIVE，APP等，说明详见参数规定
            private String trade_type;

            // 预支付交易会话标识	prepay_id	是	String(64)	wx201410272009395522657a690389285100	微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时
            private String prepay_id;

            // 二维码链接	code_url	否	String(64)	URl：weixin：//wxpay/s/An4baqw	trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
            private String code_url;

            public String getTrade_type() {
                return trade_type;
            }

            public void setTrade_type(String trade_type) {
                this.trade_type = trade_type;
            }

            public String getPrepay_id() {
                return prepay_id;
            }

            public void setPrepay_id(String prepay_id) {
                this.prepay_id = prepay_id;
            }

            public String getCode_url() {
                return code_url;
            }

            public void setCode_url(String code_url) {
                this.code_url = code_url;
            }
        }
    }











}
