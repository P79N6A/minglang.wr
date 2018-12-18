package com.taobao.cun.auge.station;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.Application;
import com.taobao.cun.auge.client.result.ResultModel;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.goods.CuntaoGoodsService;
import com.taobao.cun.auge.station.dto.FileUploadDto;
import com.taobao.cun.auge.station.dto.StationDecorateFeedBackDto;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.vividsolutions.jts.util.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@EnableAutoConfiguration
public class TestStationDecorateService {

	@Autowired
	private StationDecorateService stationDecorateService;
	
	@Test
	public void testUploadStationDecorateCheckForMobile(){
		StationDecorateFeedBackDto feedBackDto = new StationDecorateFeedBackDto();
		feedBackDto.setStationId(300000067151L);
		feedBackDto.setOperator("3840659724");
		List<FileUploadDto> outSidePhotoList = new ArrayList<>(1);
		FileUploadDto fileUploadDto1 = new FileUploadDto();
		fileUploadDto1.setTitle("室外照片");
		fileUploadDto1.setFileType("png");
		fileUploadDto1.setFileUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		outSidePhotoList.add(fileUploadDto1);
		feedBackDto.setFeedbackOutsidePhoto(outSidePhotoList);

		List<FileUploadDto> doorPhotoList = new ArrayList<>(1);
		FileUploadDto fileUploadDto2 = new FileUploadDto();
		fileUploadDto2.setTitle("门头照片");
		fileUploadDto2.setFileType("png");
		fileUploadDto2.setFileUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		doorPhotoList.add(fileUploadDto2);
		feedBackDto.setFeedbackDoorPhoto(doorPhotoList);

		List<FileUploadDto> wallDeskPhotoList = new ArrayList<>(1);
		FileUploadDto fileUploadDto3 = new FileUploadDto();
		fileUploadDto3.setTitle("前台及背景墙照片");
		fileUploadDto3.setFileType("png");
		fileUploadDto3.setFileUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		wallDeskPhotoList.add(fileUploadDto3);
		feedBackDto.setFeedbackWallDeskPhoto(wallDeskPhotoList);

		List<FileUploadDto> insidePhotoList = new ArrayList<>(1);
		FileUploadDto fileUploadDto4 = new FileUploadDto();
		fileUploadDto4.setTitle("室内照片");
		fileUploadDto4.setFileType("png");
		fileUploadDto4.setFileUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		insidePhotoList.add(fileUploadDto4);
		feedBackDto.setFeedbackInsidePhoto(insidePhotoList);

		List<FileUploadDto> materielPhoto = new ArrayList<>(1);
		FileUploadDto fileUploadDto5 = new FileUploadDto();
		fileUploadDto5.setTitle("其他logo照片");
		fileUploadDto5.setFileType("png");
		fileUploadDto5.setFileUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		materielPhoto.add(fileUploadDto5);
		feedBackDto.setFeedbackMaterielPhoto(materielPhoto);

		List<FileUploadDto> insideVideo = new ArrayList<>(1);
		FileUploadDto fileUploadDto6 = new FileUploadDto();
		fileUploadDto6.setTitle("室内视频");
		fileUploadDto6.setFileType("mp4");
		fileUploadDto6.setFileUrl("https://cloud.video.taobao.com/play/u/p/2/e/6/t/1/207829668001.mp4");
		fileUploadDto6.setAddtionalUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		insideVideo.add(fileUploadDto6);
		feedBackDto.setFeedbackInsideVideo(insideVideo);

		List<FileUploadDto> outsideVideo = new ArrayList<>(1);
		FileUploadDto fileUploadDto7 = new FileUploadDto();
		fileUploadDto7.setTitle("室外视频");
		fileUploadDto7.setFileType("mp4");
		fileUploadDto7.setAddtionalUrl("https://img.alicdn.com/imgextra/i3/6000000007303/TB2uOH6tYArBKNjSZFLXXc_dVXa_!!6000000007303-0-cuntao_space.jpg");
		fileUploadDto7.setFileUrl("https://cloud.video.taobao.com/play/u/p/2/e/6/t/1/207829668001.mp4");
		outsideVideo.add(fileUploadDto7);
		feedBackDto.setFeedbackOutsideVideo(outsideVideo);

		ResultModel<Boolean> resultModel = stationDecorateService.uploadStationDecorateCheckForMobile(feedBackDto);
		System.out.println(JSON.toJSONString(resultModel));
	}
	
	
	@Test
	public void testGetStationDecorateFeedBackDtoByUserId(){
		ResultModel<StationDecorateFeedBackDto> resultModel = stationDecorateService.getStationDecorateFeedBackDtoByUserId(3840659724L);
		StationDecorateFeedBackDto feedBackDto = resultModel.getResult();
		System.out.println(JSON.toJSONString(feedBackDto));


	}

}
