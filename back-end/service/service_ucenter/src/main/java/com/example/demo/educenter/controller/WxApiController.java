package com.example.demo.educenter.controller;

import com.example.commonutils.JwtUtils;
import com.example.demo.educenter.entity.UcenterMember;
import com.example.demo.educenter.service.UcenterMemberService;
import com.example.demo.educenter.utils.ConstantWxUtils;
import com.example.demo.educenter.utils.HttpClientUtils;
import com.example.exceptionhandler.GuliException;
import com.google.gson.Gson;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;


@Controller
//@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    // 获取扫描人的信息
    @GetMapping("/callback")
    public String callback(String code, String state) {
        try {
            // 1、先获取到code值，临时票据，类似于验证码

            // 2、拿着code去请求微信固定地址，得到两个值access_token 和 open_id
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            // 拼接三个参数： id 秘钥 code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            // 请求这个拼接好的地址，得到返回的两个值， access_toke和openid
            // 使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:" + accessTokenInfo);

            // 从accessTokenInfo字符串中将access_info和openid取出来
            // 把accessTokenInfo字符串转换为map集合，根据map里面key获取对应值
            // 使用转换工具gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            // 扫码人信息添加到数据库中
            // 先判断数据库表中是否存在相同微信信息，根据openid判断
            // 这里要注入UcenterMemberService
            UcenterMember member = memberService.getMemberByOpenId(openid);
            if(member == null) { // member为空，说明表中没有相同数据，进行添加

                // 3、拿着access_token和openid，再去请求微信提供的地址，获取扫码人的信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );

                // 发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("userInfo:" + userInfo);

                // 获取返回userInfo字符串中扫码人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname"); // 昵称
                String headimgurl = (String) userInfoMap.get("headimgurl"); // 头像

                // 添加到数据库
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            // 使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            // 回到首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token=" + jwtToken;

        }catch (Exception e){
            throw new GuliException(20001, "登录失败");
        }
    }

    // 1、生成微信扫描的二维码
    @GetMapping("/login")
    public String getWxCode() {
        // 可以使用字符串拼接，但是不建议

        // 微信开放平台授权baseUrl，%s相当于?占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 对redirect_url进行URLEncode编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        // 设置%s的值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "daydayup"
        );

        // 重定向到请求微信的地址里
        return "redirect:" + url;
    }
}
