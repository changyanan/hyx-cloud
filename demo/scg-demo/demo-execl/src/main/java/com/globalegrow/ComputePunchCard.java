package com.globalegrow;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.globalegrow.core.Context;
import com.globalegrow.core.utils.ExcelPoi;
import com.globalegrow.core.utils.ListUtils;
import com.globalegrow.core.utils.MapUtils;

@RestController
@RequestMapping("/tr")
@SpringBootApplication(scanBasePackages="com.globalegrow")
public class ComputePunchCard {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(ComputePunchCard.class, args);
	}
	
	@PostMapping("/transform")
	public void transform(@RequestPart("file") MultipartFile file) throws IOException {
		File xls = new File(file.getOriginalFilename());
		file.transferTo(xls);
		System.err.println(xls.getAbsolutePath());
		List<PunchCard> punchCards = readXls(xls);
		Map<String, Collection<PunchCardDay>> dk =compute(punchCards);
		ServletOutputStream out = Context.getResponse().getOutputStream();
		outXls(dk, out,true);
		Context.getResponse().addHeader("Content-Disposition", "attachment;filename=考勤记录(计算结果).xls" );//设置文件名，attachment和filename之间是分号，注意！  
		Context.getResponse().flushBuffer();
	}
	
	@SuppressWarnings("unchecked")
	private static void outXls(Map<String, Collection<PunchCardDay>> dk,OutputStream out,boolean is2003) throws IOException {
		ExcelPoi.exPort(out, "考勤原始记录(计算结果)", 1, is2003,(index, sheetid) -> {
			Iterator<String> it = dk.keySet().iterator();
			if(!it.hasNext())
				return null;
			Map<String,Object> map=Collections.emptyMap();
			List<Map<String, Object>> list = ListUtils.n(dk.remove(it.next()))
					.order(udk->udk.date).
					list(data->MapUtils.
							<String,Object>n().
							a("userId",data.userId).
							a("name",data. name).
							a("date", data.date).
							a("beWork", data.beWork).
							a("offWork", data.offWork).
							a("noPCard", data.noPCard).
							a("workTime", data.workTime).
							a("layTime", data.layTime).
							a("workTime1", data.workTime1).
							a("layTime1", data.layTime1).
							a("work", data.work?"否":"是").
							to()
					).a(map,map,map).to();
			
			return ListUtils.n(MapUtils.
					<String,Object>n().
					a("userId","用户编号").
					a("name","用户名").
					a("date", "日期").
					a("beWork", "上班时间").
					a("offWork", "下班时间").
					a("noPCard", "忘记打开次数").
					a("workTime", "9:00-18:00 工作制早到晚走时间").
					a("layTime", "9:00-18:00 晚到早退时间").
					a("workTime1", "9:30-18:30 工作制早到晚走时间").
					a("layTime1", "9:30-18:30晚到早退时间").
					a("work", "是否是周六或者周日").
					to()).a(list).to();
		}, (row, map, index, sheetid) -> {
			ExcelPoi.writeRow(row, 1, map, "userId", "name", "date","work", "beWork", "offWork","noPCard","workTime","layTime","workTime1","layTime1" );
		});
	}
	
	private static int SH=6,EH=15 ,SW=9,DW=18;
	
	@SuppressWarnings("deprecation")
	private static Map<String, Collection<PunchCardDay>> compute(List<PunchCard> punchCards) {
		DateFormat dayFormat=new SimpleDateFormat("yyyy-MM-dd");
		DateFormat timeFormat=new SimpleDateFormat("HH:mm");
		return ListUtils.n(punchCards).group(pc->pc.userId).map((uid,ulist)->{
			Map<String, PunchCardDay> md = ListUtils.n(ulist).group(uk->{
					uk.date.setHours(uk.date.getHours()-SH);
					String day = dayFormat.format(uk.date);
					uk.date.setHours(uk.date.getHours()+SH);
					return day;
				}).map((day,gulist)->{
					if(ListUtils.isEmpty(gulist))
						return null;
					gulist.sort((t1,t2)->t1.date.compareTo(t2.date));
					PunchCardDay cardDay=new PunchCardDay();
					cardDay.userId=gulist.get(0).userId;
					cardDay.name=gulist.get(0).name;
					cardDay.userId=gulist.get(0).userId;
					cardDay.date=day;
					Date Day=null;
					try {
						Day = dayFormat.parse(cardDay.date);
					} catch (ParseException e2) {
						e2.printStackTrace();
					}
					Date s = gulist.get(0).date;
					Date e = gulist.get(gulist.size()-1).date;
					if(s.getHours()>=SH&&s.getHours()<=EH){
						cardDay.beWork=timeFormat.format(s);
						int minutes=(s.getHours()-SW)*60+s.getMinutes();
						cardDay.workTime+=minutes>0?0:-minutes;
						cardDay.layTime+=minutes>0?minutes:0;
						minutes-=30;
						cardDay.workTime1+=minutes>0?0:-minutes;
						cardDay.layTime1+=minutes>0?minutes:0;
						if(minutes>500)
						System.err.println(minutes);
					}
					else
						cardDay.noPCard++;
					if(e.getHours()>=SH&&e.getHours()<EH){
						cardDay.noPCard++;
					}else{
						cardDay.offWork=timeFormat.format(e);
						int minutes=((e.getDate()-Day.getDate())*24+e.getHours()-DW)*60+e.getMinutes();
						cardDay.layTime+=minutes>0?0:-minutes;
						cardDay.workTime+=minutes>0?minutes:0;
						minutes-=30;
						cardDay.layTime1+=minutes>0?0:-minutes;
						cardDay.workTime1+=minutes>0?minutes:0;
					}
					cardDay.work=Day.getDay()%6!=0;
					return cardDay;
				}).to();
			try {
				PunchCardDay cardDay=md.values().iterator().next();
				Date yue = dayFormat.parse(cardDay.date);
				yue.setMonth(yue.getMonth()+1);
				yue.setDate(0);
				yue.setSeconds(0);
				int ye=yue.getDate();
				
				for (int i=ye;i>0;i--) {
					yue.setDate(i);
					String day = dayFormat.format(yue);
					if(!md.containsKey(day)){
						md.put(day, new PunchCardDay(cardDay.userId, cardDay.name, day, null, null, 0, 0, 0, 0, 0, yue.getDay()%6!=0));
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return md.values();
		}).to();
	}
	private static List<PunchCard> readXls(File xls) throws IOException {
		List<PunchCard> list=new ArrayList<>();
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ExcelPoi.imPort(xls, 1, (row, rownum, sheetName) -> {
			Map<String, Object> map = ExcelPoi.readRow(row, 0,  "userId", "name", "date");
			
			PunchCard punchCard=new PunchCard();
			punchCard.userId=(String) map.get("userId");
			if(punchCard.userId==null)
				return ;
			punchCard.name= (String) map.get("name");
			try {
				punchCard.date= dateFormat.parse((String) map.get("date")) ;
			} catch (ParseException e) {
				e.printStackTrace();
				return ;
			}
			list.add(punchCard);
		});
		return list;
	}
}

