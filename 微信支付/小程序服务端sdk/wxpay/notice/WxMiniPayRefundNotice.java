package com.g5.sky.wxpay.notice;

import java.util.Map;

import static com.g5.sky.wxpay.utils.WxPayUtils.decryptRefundNotice;
import static com.g5.sky.wxpay.utils.WxPayUtils.parseInt;
import static com.g5.sky.wxpay.utils.WxPayUtils.xml2map;

/**
 * todo 注释
 *
 * @author shuai . 2018/10/29 . 6:54 PM
 */
public class WxMiniPayRefundNotice {

    public static void main(String[] args) throws Exception {

        String xml = "<xml><return_code>SUCCESS</return_code><appid><![CDATA[wx7e46fb347c8c087a]]></appid><mch_id><![CDATA[1488030162]]></mch_id><nonce_str><![CDATA[f9f4c79512b034b25c57681f8e31e0d5]]></nonce_str><req_info><![CDATA[ouw36hRCGgsIsPonAu71bM9/zfElNQCb/ZO2wYp41x2ARo07bre0+qVr9BiB7T4Y3aC9bk/ENOlsDkhUuDTfBnHeMWpBRQFAxBzKkXeHHDb46pNSKS2qE/jLyCruJOfifuJDcT6boJ9SSQlIo/CDzIxBo3qgzG316sQTwpoLcUrh+udeWK2JXA563CGKtTYCQl1koT/KnQZ27QDIeQDHTO/RMPPhSUxRuJj2/EA/mTkbgrsEd/YDLcMjIu4D4jNv146oqq7xs8tBbUFtfj4Pi4+VIJjC7yszKvQ+orQKsHQUQPDcbyHfVkoDb2Q94O1avBzpgUtawJwaWUKKgbsOLWyaTVQeZKr9bSKjGV3C2L5d9p9JEtNR4sLBGJkfnqFEuvAvh0BE8GEvycAu8zLM/KNXwGv2RB5eoGKiYGtB3BFtkVhbnTSloM7SyZRwe5rV+GPdwOQUS/t5M0vK0v6oDbaSWlPHy5aPMzd3xWsYinGr6dK22Rm2GctP1WmmpDrjcz7kQVD1QYsiWSdXGl70vv2S+GSRwE5FE/lvPDtHcbIFKXTZ0YzytN5Ngb7qP/hS5gB28IO5WFw+S7SOQFtvK5PEj+CM7RYRZTK9ObjfVecXAip/Qg4MiCsjG3z1YfKbQ6GAwkkGAMPh4Nzn0zy048+Q//9vvYvDs5w/HScFa9pRhPepzN2vJSWM7BZqR07Q8SPVxW0+qc3r7OzIQ7NlNkWsn4hH5J9ZbKGY0YrzA2qDE4p+HTQ1QA0UHidNVmTHsAxe9v8Qd/gVje09VBA9PS2mgcVv/hcI6q+aNSlz8+O999rri0DAPowECZ4hBDYUNYLbevQJWdAOQoFjMLRrxFKUedO3t4kUaYK14bjPqrASTsznlqFV90T7S9L0Z443eluqY4tAhGe98FpnSPmPnwEH7+OkGq6TCRMD/LL3qjiiWMsd4d89bLfGIN9Ca6u+/H3+xEqLHnuVuwD7LVk9zzWQ0ULfi9x6aWBVc3J63RGTIS6S6NDZ4Q3jWDDol1bqb7HRYOwMbR+MHoScnNatmGah6S1hVPf5x9UX1vnXUmnoEs+1UZlgdx9+tLWL6VhA]]></req_info></xml>";
        Map<String, String> map = xml2map(xml);
        System.out.println("1=================================");
        System.out.println(map);

        String req_info = map.get("req_info");
        System.out.println("2=================================");
        System.out.println(req_info);

        String reqXml = decryptRefundNotice("XhJVvt9ajWIvKLv9Zji07Vl0P8tOAy56", req_info);
        System.out.println("3=================================");
        System.out.println(reqXml);

//        String content = "ouw36hRCGgsIsPonAu71bM9/zfElNQCb/ZO2wYp41x2ARo07bre0+qVr9BiB7T4Y3aC9bk/ENOlsDkhUuDTfBnHeMWpBRQFAxBzKkXeHHDb46pNSKS2qE/jLyCruJOfifuJDcT6boJ9SSQlIo/CDzIxBo3qgzG316sQTwpoLcUrh+udeWK2JXA563CGKtTYCQl1koT/KnQZ27QDIeQDHTO/RMPPhSUxRuJj2/EA/mTkbgrsEd/YDLcMjIu4D4jNv146oqq7xs8tBbUFtfj4Pi4+VIJjC7yszKvQ+orQKsHQUQPDcbyHfVkoDb2Q94O1avBzpgUtawJwaWUKKgbsOLWyaTVQeZKr9bSKjGV3C2L5d9p9JEtNR4sLBGJkfnqFEuvAvh0BE8GEvycAu8zLM/KNXwGv2RB5eoGKiYGtB3BFtkVhbnTSloM7SyZRwe5rV+GPdwOQUS/t5M0vK0v6oDbaSWlPHy5aPMzd3xWsYinGr6dK22Rm2GctP1WmmpDrjcz7kQVD1QYsiWSdXGl70vv2S+GSRwE5FE/lvPDtHcbIFKXTZ0YzytN5Ngb7qP/hS5gB28IO5WFw+S7SOQFtvK5PEj+CM7RYRZTK9ObjfVecXAip/Qg4MiCsjG3z1YfKbQ6GAwkkGAMPh4Nzn0zy048+Q//9vvYvDs5w/HScFa9pRhPepzN2vJSWM7BZqR07Q8SPVxW0+qc3r7OzIQ7NlNkWsn4hH5J9ZbKGY0YrzA2qDE4p+HTQ1QA0UHidNVmTHsAxe9v8Qd/gVje09VBA9PS2mgcVv/hcI6q+aNSlz8+O999rri0DAPowECZ4hBDYUNYLbevQJWdAOQoFjMLRrxFKUedO3t4kUaYK14bjPqrASTsznlqFV90T7S9L0Z443eluqY4tAhGe98FpnSPmPnwEH7+OkGq6TCRMD/LL3qjiiWMsd4d89bLfGIN9Ca6u+/H3+xEqLHnuVuwD7LVk9zzWQ0ULfi9x6aWBVc3J63RGTIS6S6NDZ4Q3jWDDol1bqb7HRYOwMbR+MHoScnNatmGah6S1hVPf5x9UX1vnXUmnoEs+1UZlgdx9+tLWL6VhA";
//        System.out.println("4=================================");
//        System.out.println(content);
//
//        String contentXml = decryptRefundNotice("XhJVvt9ajWIvKLv9Zji07Vl0P8tOAy56", content);
//        System.out.println("5=================================");
//        System.out.println(contentXml);
    }

    public WxMiniPayRefundNotice(String xml, String key) {

        Map<String, String> map = xml2map(xml);

        if (map == null) {
            this.return_code = "FAIL";
            this.return_msg = "使用通知 xml 构建微信支付通知实体失败";
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
        this.returnSuccess.nonce_str = map.get("nonce_str");
        this.returnSuccess.req_info = map.get("req_info");

        //  解密方式
        //      解密步骤如下：
        //          （1）对加密串A做base64解码，得到加密串B
        //          （2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
        //          （3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）

        String reqInfoXml = decryptRefundNotice(key, this.returnSuccess.req_info);

        Map<String, String> reqInfoMap = xml2map(reqInfoXml);
        if (reqInfoMap == null) return;

        this.returnSuccess.reqInfo = this.returnSuccess.new ReqInfo();

        this.returnSuccess.reqInfo.transaction_id = reqInfoMap.get("transaction_id");
        this.returnSuccess.reqInfo.out_trade_no = reqInfoMap.get("out_trade_no");
        this.returnSuccess.reqInfo.refund_id = reqInfoMap.get("refund_id");
        this.returnSuccess.reqInfo.out_refund_no = reqInfoMap.get("out_refund_no");
        this.returnSuccess.reqInfo.total_fee = parseInt(reqInfoMap.get("total_fee"), -99);
        this.returnSuccess.reqInfo.settlement_total_fee = parseInt(reqInfoMap.get("settlement_total_fee"), -99);
        this.returnSuccess.reqInfo.refund_fee = parseInt(reqInfoMap.get("refund_fee"), -99);
        this.returnSuccess.reqInfo.settlement_refund_fee = parseInt(reqInfoMap.get("settlement_refund_fee"), -99);
        this.returnSuccess.reqInfo.refund_status = reqInfoMap.get("refund_status");
        this.returnSuccess.reqInfo.success_time = reqInfoMap.get("success_time");
        this.returnSuccess.reqInfo.refund_recv_accout = reqInfoMap.get("refund_recv_accout");
        this.returnSuccess.reqInfo.refund_account = reqInfoMap.get("refund_account");
        this.returnSuccess.reqInfo.refund_request_source = reqInfoMap.get("refund_request_source");

    }

    public WxMiniPayRefundNotice() {
    }

    // 返回状态码	return_code	是	String(16)	SUCCESS	 SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看trade_state来判断
    private String return_code;

    // 返回信息	return_msg	是	String(128)	OK	 当return_code为FAIL时返回信息为错误原因 ，例如 签名失败 参数格式校验错误
    private String return_msg;

    // 在return_code为SUCCESS的时候有返回：
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

        // 公众账号ID appid 是 String(32) wx8888888888888888 微信分配的公众账号ID（企业号corpid即为此appId）
        private String appid;

        // 退款的商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
        private String mch_id;

        // 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位。推荐随机数生成算法
        private String nonce_str;

        // 加密信息 req_info 是 String(1024) 加密信息请用商户秘钥进行解密，详见解密方式
        private String req_info;

        // 返回的加密字段
        private ReqInfo reqInfo;

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

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getReq_info() {
            return req_info;
        }

        public void setReq_info(String req_info) {
            this.req_info = req_info;
        }

        public ReqInfo getReqInfo() {
            return reqInfo;
        }

        public void setReqInfo(ReqInfo reqInfo) {
            this.reqInfo = reqInfo;
        }

        public class ReqInfo {

            // 微信订单号 transaction_id 是 String(32) 1217752501201407033233368018 微信订单号
            private String transaction_id;

            // 商户订单号 out_trade_no 是 String(32) 1217752501201407033233368018 商户系统内部的订单号
            private String out_trade_no;

            // 微信退款单号 refund_id 是 String(32) 1217752501201407033233368018 微信退款单号
            private String refund_id;

            // 商户退款单号 out_refund_no 是 String(64) 1217752501201407033233368018 商户退款单号
            private String out_refund_no;

            // 订单金额 total_fee 是 Int 100 订单总金额，单位为分，只能为整数，详见支付金额
            private int total_fee;

            // 应结订单金额 settlement_total_fee  否 Int 100 当该订单有使用非充值券时，返回此字段。应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
            private int settlement_total_fee;

            // 申请退款金额 refund_fee 是 Int 100 退款总金额,单位为分
            private int refund_fee;

            // 退款金额 settlement_refund_fee 是 Int 100 退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
            private int settlement_refund_fee;

            // 退款状态 refund_status 是 String(16) SUCCESS SUCCESS-退款成功 CHANGE-退款异常 REFUNDCLOSE—退款关闭
            private String refund_status;

            // 退款成功时间 success_time 否 String(20) 2017-12-15 09:46:01 资金退款至用户帐号的时间，格式2017-12-15 09:46:01
            private String success_time;

            // 退款入账账户 refund_recv_accout 是 String(64) 招商银行信用卡0403 取当前退款单的退款入账方 1）退回银行卡： {银行名称}{卡类型}{卡尾号} 2）退回支付用户零钱: 支付用户零钱 3）退还商户: 商户基本账户 商户结算银行账户 4）退回支付用户零钱通: 支付用户零钱通
            private String refund_recv_accout;

            // 退款资金来源 refund_account 是 String(30) REFUND_SOURCE_RECHARGE_FUNDS REFUND_SOURCE_RECHARGE_FUNDS 可用余额退款/基本账户 REFUND_SOURCE_UNSETTLED_FUNDS 未结算资金退款
            private String refund_account;

            // 退款发起来源 refund_request_source 是 String(30) API API接口 VENDOR_PLATFORM商户平台
            private String refund_request_source;

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

            public String getRefund_id() {
                return refund_id;
            }

            public void setRefund_id(String refund_id) {
                this.refund_id = refund_id;
            }

            public String getOut_refund_no() {
                return out_refund_no;
            }

            public void setOut_refund_no(String out_refund_no) {
                this.out_refund_no = out_refund_no;
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

            public int getRefund_fee() {
                return refund_fee;
            }

            public void setRefund_fee(int refund_fee) {
                this.refund_fee = refund_fee;
            }

            public int getSettlement_refund_fee() {
                return settlement_refund_fee;
            }

            public void setSettlement_refund_fee(int settlement_refund_fee) {
                this.settlement_refund_fee = settlement_refund_fee;
            }

            public String getRefund_status() {
                return refund_status;
            }

            public void setRefund_status(String refund_status) {
                this.refund_status = refund_status;
            }

            public String getSuccess_time() {
                return success_time;
            }

            public void setSuccess_time(String success_time) {
                this.success_time = success_time;
            }

            public String getRefund_recv_accout() {
                return refund_recv_accout;
            }

            public void setRefund_recv_accout(String refund_recv_accout) {
                this.refund_recv_accout = refund_recv_accout;
            }

            public String getRefund_account() {
                return refund_account;
            }

            public void setRefund_account(String refund_account) {
                this.refund_account = refund_account;
            }

            public String getRefund_request_source() {
                return refund_request_source;
            }

            public void setRefund_request_source(String refund_request_source) {
                this.refund_request_source = refund_request_source;
            }
        }
    }
}
