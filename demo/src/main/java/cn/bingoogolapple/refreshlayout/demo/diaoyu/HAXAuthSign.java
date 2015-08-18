//
//  HAXAuthSign.java
//  HoneyAnt
//
//  Created by dongjianbo on 15-2-12.
//  Copyright 2015 www.mafengwo.cn
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not
//  use this file except in compliance with the License.  You may obtain a copy
//  of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
//  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
//  License for the specific language governing permissions and limitations under
//  the License.
//

package cn.bingoogolapple.refreshlayout.demo.diaoyu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HAXAuthSign {
    
    /**
     * 生成url参数
     * 
     * @param requestURL
     *            接口地址
     * @param httpMethod
     *            http请求的方法，GET 或 POST
     * @param params
     *            key-value形式的参数集
     * @param consumerKey
     *            API Key(组件信息中的API Key值)
     * @param consumerKeySecret
     *            Secret Key(组件信息中的Secret Key值)
     * @param tokenSecret
     *            Token Secret 授权后的Access Token Secret
     * @return key-value形式的url参数集
     */
    public static HashMap<String, String> generateURLParams(String httpMethod, String requestURL, HashMap<String, String> params, String consumerKey, String consumerKeySecret,
            String accessToken, String tokenSecret) {
        if (params == null) {
            params = new HashMap<String, String>();
        }

        if (params.get("x_auth_mode") != null) {
            params.remove("x_auth_mode");
        }
        
        if (params.get("oauth_token") != null) {
            params.remove("oauth_token");
        }
        
        if (params.get("oauth_consumer_key") != null) {
            params.remove("oauth_consumer_key");
        }
        
        if (params.get("oauth_signature_method") != null) {
            params.remove("oauth_signature_method");
        }
        
        if (params.get("oauth_timestamp") != null) {
            params.remove("oauth_timestamp");
        }
        
        if (params.get("oauth_nonce") != null) {
            params.remove("oauth_nonce");
        }
        
        if (params.get("oauth_version") != null) {
            params.remove("oauth_version");
        }
        
        if (params.get("oauth_signature") != null) {
            params.remove("oauth_signature");
        }
        
        // 公用参数
        params.put("x_auth_mode", "client_auth");
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        long nonce = timestamp + random.nextInt();
        String onceMd5 = StringUtil.md5(String.valueOf(nonce));
        if (accessToken != null) {
            params.put("oauth_token", accessToken);
        }
        params.put("oauth_consumer_key", consumerKey);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", String.valueOf(timestamp));
        params.put("oauth_nonce", onceMd5);
        params.put("oauth_version", "1.0");
        
        // 生成baseString
        StringBuffer sb = new StringBuffer();
        Set<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(encode(key.toLowerCase()));
            sb.append("=");
            sb.append(encode(params.get(key)));
        }
        StringBuffer base = new StringBuffer(httpMethod).append("&").append(encode(requestURL.toLowerCase())).append("&").append(encode(sb.toString()));
        String baseString = base.toString();

//        DLog.i(HAXAuthSign.class.getName(), baseString);
        // 签名
        String signature = generateSignature(baseString, consumerKeySecret, tokenSecret);
        params.put("oauth_signature", signature);
        
        return params;
    }
    
    private static String generateSignature(String baseString, String consumerKeySecret, String tokenSecret) {
        
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            String oauthSignature = encode(consumerKeySecret) + "&" + ((tokenSecret != null) ? encode(tokenSecret) : "");

//            DLog.i(HAXAuthSign.class.getName()+" oauthSignature: ", oauthSignature);
            spec = new SecretKeySpec(oauthSignature.getBytes(), "HmacSHA1");
            mac.init(spec);
            byteHMAC = mac.doFinal(baseString.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException ignore) {
            // should never happen
        }
        return new BASE64Encoder().encode(byteHMAC);
    }
    
    private static String encode(String value) {
        if (value == null) {
            return "";
        }
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuffer buf = new StringBuffer(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
    
    private static String constructRequestURL(String url) {
        int index = url.indexOf("?");
        if (-1 != index) {
            url = url.substring(0, index);
        }
        int slashIndex = url.indexOf("/", 8);
        String baseURL = url.substring(0, slashIndex).toLowerCase();
        int colonIndex = baseURL.indexOf(":", 8);
        if (-1 != colonIndex) {
            // 地址包含端口号
            if (baseURL.startsWith("http://") && baseURL.endsWith(":80")) {
                // 排除默认80端口
                baseURL = baseURL.substring(0, colonIndex);
            } else if (baseURL.startsWith("https://") && baseURL.endsWith(":443")) {
                // 排除默认443端口
                baseURL = baseURL.substring(0, colonIndex);
            }
        }
        url = baseURL + url.substring(slashIndex);
        
        return url;
    }

/*    *//**
     * 生成Html5页面url的xauth串
     * @param url
     * @return
     *//*
    public static String generateH5XauthString(String url){
        HashMap<String,String> param = new HashMap<String,String>();
        String[] idArr = url.split("\\?");
        String urlTmp = url;
        if(idArr != null && idArr.length>1){
            urlTmp = idArr[0];
            String[] paramArr = idArr[1].split("&");
            if(paramArr != null){
                int plen = paramArr.length;
                for(int p=0;p<plen;p++){
                    String pa = paramArr[p];
                    String[] paArr = pa.split("=");
                    String key = "",value = "";
                    if(paArr.length > 1) {
                        key = paArr[0];
                        value = paArr[1];
                    }
                    param.put(key,value);
                }
            }
        }
        if(AccountStorage.getInstance().skinId== ProjectConst.SKIN_ID_DARK){
            param.put("night_mode", 1+"");
        }
        param.put("platform", "android");
        param.put("client_version", Const.VERSION_NAME);
        param.put("api_version", "1.0");
        param.put("app_channel", Const.CHANNEL_ID);
        param.put("device_id", Const.DEVICE_ID);
        param.put("request_source","web_browser");
        param = HAXAuthSign.generateURLParams("GET", urlTmp, param, Const.CONSUMER_KEY, Const.CONSUMER_SECRET, ProjectApplication.getUser().getToken(), ProjectApplication.getUser().getToken_secret());
        Iterator iter = param.entrySet().iterator();
        StringBuffer params = new StringBuffer();
        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey()==null?"":entry.getKey().toString();
            String cString = entry.getValue()==null?"":entry.getValue().toString();
            params.append(key+"="+cString+"&");
        }
        String result = params.substring(0, params.length()-1);
        urlTmp = urlTmp+"?"+result;
        return urlTmp;
    }*/
}
