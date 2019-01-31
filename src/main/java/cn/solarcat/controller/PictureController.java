package cn.solarcat.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

@Controller
public class PictureController {
	private String ACCESS_KEY = "RxP7sU-RUTe6Em54E4IlZOnCrq7kiSjGuI9m1GfK";
	private String SECRET_KEY = "zylJZsYq956jj_UFDUbav4wuTxPGlb_F0EKMisYU";
	private String BUCKET = "solrcat";
	private String IMAGE_SERVER_URL_QINIU = "http://img.solarcat.cn/";

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String PicUpload(MultipartFile uploadFile) throws IOException {
		Configuration cfg = new Configuration(Zone.zone2());
		UploadManager uploadManager = new UploadManager(cfg);
		String accessKey = ACCESS_KEY;
		String secretKey = SECRET_KEY;
		String bucket = BUCKET;
//		String originalFileName = uploadFile.getOriginalFilename();
//		String extName = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = UUID.randomUUID().toString().substring(0, 16)/* +extName */;
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
			byte[] b = uploadFile.getBytes();
			Response response = uploadManager.put(b, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			String url = IMAGE_SERVER_URL_QINIU + putRet.key;
			Map<String, Object> result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			// return JsonUtils.objectToJson(result);
			return JSONObject.toJSONString(result);
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
			Map<String, Object> result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			// return JsonUtils.objectToJson(result);
			return JSONObject.toJSONString(result);
		}
	}
}
