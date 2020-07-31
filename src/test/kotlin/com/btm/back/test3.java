package com.btm.back;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.imageaudit.model.v20191230.ScanTextRequest;
import com.aliyuncs.imageaudit.model.v20191230.ScanTextResponse;
import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardRequest;
import com.aliyuncs.ocr.model.v20191230.RecognizeIdentityCardResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.btm.back.bean.IdCardModel;
import com.btm.back.helper.JsonHelper;
import com.btm.back.utils.OSSClientConstants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class test3 {
    public static void main(String[] args) {
        testid();
    }
    private static void test(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        ScanTextRequest request = new ScanTextRequest();
        request.setRegionId("cn-shanghai");

        List<ScanTextRequest.Tasks> tasksList = new ArrayList<ScanTextRequest.Tasks>();

        ScanTextRequest.Tasks tasks1 = new ScanTextRequest.Tasks();
        tasks1.setContent("傻逼");
        tasksList.add(tasks1);

        ScanTextRequest.Tasks tasks2 = new ScanTextRequest.Tasks();
        tasks2.setContent("你妈个逼");
        tasksList.add(tasks2);
        request.setTaskss(tasksList);

        List<ScanTextRequest.Labels> labelsList = new ArrayList<ScanTextRequest.Labels>();

        ScanTextRequest.Labels labels1 = new ScanTextRequest.Labels();
        labels1.setLabel("porn");
        labelsList.add(labels1);

        ScanTextRequest.Labels labels2 = new ScanTextRequest.Labels();
        labels2.setLabel("politics");
        labelsList.add(labels2);

        ScanTextRequest.Labels labels3 = new ScanTextRequest.Labels();
        labels3.setLabel("abuse");
        labelsList.add(labels3);
        request.setLabelss(labelsList);

        try {
            ScanTextResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }

    }
//    MIGJAoGBAKeKTFgkMfQ8VQMhoAaFEqZGG43vjWbcSuI2LkMy2qUOsjilF9UXBo34+7H/PbFusoKKYKSmz7nhCoZBFLnIz590ba1uosV5iUXXl3dvd7Ch1zU7225x6VK62lqOEOXd04oEvs9zr4L7ko3FgzmL5XkasLJi0VNM/F6qGToifV/JAgMBAAE=
    private static void testid(){
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", OSSClientConstants.ACCESS_KEY_ID, OSSClientConstants.ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        RecognizeIdentityCardRequest request = new RecognizeIdentityCardRequest();
        request.setRegionId("cn-shanghai");
        request.setImageURL("http://explorer-image.oss-cn-shanghai.aliyuncs.com/1032913586529687/IMG_2221.JPG?OSSAccessKeyId=LTAI4Fk9FstqSEYnqKJ5Dpeo&Expires=1596090326&Signature=6e9gqn0TMqcp%2Bb%2BTSvds6q9Qsn8%3D");
        request.setSide("face");

        try {
            RecognizeIdentityCardResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }
}
