/*
 * Created by wangde on 15-6-8 下午12:24
 * Copyright 2015 www.mahua.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cn.bingoogolapple.refreshlayout.demo.ui.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * 
* <p> @ClassName: CommTool </p>
*
* <p> @Description: CommTool </p>
*
* <p> @author wangde dongen_wang@163.com </p>
*
* <p> @version 1.00.00 </p>
*
* @date Jan 20, 2015 / 6:33:54 PM
 */
public class CommTool {
	
	private static String TAG = "CommTool";
	
	public static final String PLATFORM = "android";
	public static String API_VERSION = "3.0";
	public static String telPhoneDevice;
	/**
	 * 得到R中资源的ID
	 * <p>因打包到jar中后，访问其中资源的方式不一样，导致jar中的R文件资源定义无法使用，故通过该方法获取正确的资源</p>
	 * @param res_name 资源名
	 * @param type 资源类型
	 * @param res 资源
	 * @param package_name 包名
	 * @return 资源ID
	 */
	public static int getRID(String res_name, String type, Resources res, String package_name){
		return res.getIdentifier(res_name,type,package_name);
	}
	/**
	 * 
	* @Title: getRID 
	* @Description: 
	* @param @param type
	* @param @param ctx
	* @param @return 
	* @return int    返回类型 
	* @date 2014-3-12 上午8:48:05 
	* @throws
	 */
	public static int getRID(String res_name, String type,Context ctx){
		Resources res = ctx.getResources();
		String package_name = ctx.getPackageName();
		return res.getIdentifier(res_name,type,package_name);
	}
	
	
	/**
	 * 初始化
	 * @param context Context引用
	 */
	public static void initSystem(Context context){
		telPhoneDevice = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		
	}
	
	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏键盘
	* @return void    返回类型
	* @throws
	 */
	public static void hideSoftInput(View view){
//		InputMethodManager imm = (InputMethodManager) ProjectApplication.mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//		if(imm.isActive())
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	/*
	public static SpannableStringBuilder getSpannableText(FragmentActivity activity,MyTextView mView,String ss,String tag){
		String allReg = "\\[MHU\\].*?\\[:MHU\\]";
		String reg = "\\[MHU\\]|\\[:MHU\\]";
		String atReg = " @";
		Pattern p = Pattern.compile(allReg);
		Matcher m = p.matcher(ss);
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
		}
		int[][] arr = new int[result.size()][2];
		int i = 0,len = result.size();
		String[] args = new String[result.size()];
		try {
			for(String s1:result){
				String ssTmp = s1.replaceAll(reg, "");
				args[i] = ssTmp.split("=")[0];
				String urlTmp1 = " @"+ssTmp.split("=")[1];
				ss = ss.replaceFirst(allReg,urlTmp1);
				arr[i][0] = ss.indexOf(urlTmp1);
				arr[i][1] = arr[i][0]+urlTmp1.length()-1;
				ss = ss.replaceFirst(atReg, "@");
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpannableStringBuilder style=new SpannableStringBuilder(ss);   
		i=0;
		for(;i<len;i++){
			URLSpanNoUnderline cls = new URLSpanNoUnderline(URLSpanNoUnderline.MHU+":"+args[i],activity,tag);
			style.setSpan(cls, arr[i][0], arr[i][1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		mView.setSelectArr(arr);
		return style;
	}
	

	public static SpannableStringBuilder getSpannableTextSim(FragmentActivity activity,MyTextView mView,String ss,String tag){
		String allReg = "\\[CHECK_SIMILAR_IMG\\].*?\\[:CHECK_SIMILAR_IMG\\]";
		String reg = "\\[CHECK_SIMILAR_IMG\\]|\\[:CHECK_SIMILAR_IMG\\]";
		String atReg = " @";
		Pattern p = Pattern.compile(allReg);
		Matcher m = p.matcher(ss);
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
		}
		int[][] arr = new int[result.size()][2];
		int i = 0,len = result.size();
		String[] args = new String[result.size()];
		try {
			for(String s1:result){
				String ssTmp = s1.replaceAll(reg, "");
				args[i] = ssTmp.split("=")[0];
				String urlTmp1 = " @"+ssTmp.split("=")[1];
				ss = ss.replaceFirst(allReg,urlTmp1);
				arr[i][0] = ss.indexOf(urlTmp1);
				arr[i][1] = arr[i][0]+urlTmp1.length()-1;
				ss = ss.replaceFirst(atReg, "@");
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpannableStringBuilder style=new SpannableStringBuilder(ss);   
		i=0;
		for(;i<len;i++){
			URLSpanNoUnderline cls = new URLSpanNoUnderline(URLSpanNoUnderline.CHECK_SIMILAR_IMG+":"+args[i],activity,tag);
			style.setSpan(cls, arr[i][0], arr[i][1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		mView.setSelectArr(arr);
		return style;
	}
	*/
	/*
	public static SpannableStringBuilder getSpannableText(String matcher, FragmentActivity activity,MyTextView mView,String ss,String tag){
		String allReg = "\\["+matcher+"\\].*?\\[:"+matcher+"\\]";
		String reg = "\\["+matcher+"\\]|\\[:"+matcher+"\\]";
		String atReg = " @";
		Pattern p = Pattern.compile(allReg);
		//modify by wangde 05/18 
		ss = ss == null?"":ss;
		Matcher m = p.matcher(ss);
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
		}
		int[][] arr = new int[result.size()][2];
		int i = 0,len = result.size();
		String[] args = new String[result.size()];
		try {
			//用户处理
			if(URLSpanNoUnderline.MHU.equals(matcher)){
				for(String s1:result){
					String ssTmp = s1.replaceAll(reg, "");
					args[i] = ssTmp.split("=")[0];
					String urlTmp1 = " @"+ssTmp.split("=")[1];
					ss = ss.replaceFirst(allReg,urlTmp1);
					arr[i][0] = ss.indexOf(urlTmp1);
					arr[i][1] = arr[i][0]+urlTmp1.length()-1;
					ss = ss.replaceFirst(atReg, "@");
					i++;
				}
			}else if(URLSpanNoUnderline.CHECK_SIMILAR_IMG.equals(matcher)){
				for(String s1:result){
					String ssTmp = s1.replaceAll(reg, "");
					args[i] = ssTmp.split("=")[0];
					String urlTmp1 = ssTmp.split("=")[1];
					ss = ss.replaceFirst(allReg,urlTmp1);
					arr[i][0] = ss.indexOf(urlTmp1);
					arr[i][1] = arr[i][0]+urlTmp1.length();
//					ss = ss.replaceFirst(atReg, "@");
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpannableStringBuilder style=new SpannableStringBuilder(ss);   
		i=0;
		//用户处理
		if(URLSpanNoUnderline.MHU.equals(matcher)){
			for(;i<len;i++){
				URLSpanNoUnderline cls = new URLSpanNoUnderline(matcher+":"+args[i],activity,tag);
				style.setSpan(cls, arr[i][0], arr[i][1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}else if(URLSpanNoUnderline.CHECK_SIMILAR_IMG.equals(matcher)){
			for(;i<len;i++){
				URLSpanNoUnderline cls = new URLSpanNoUnderline(matcher+":"+args[i],activity,tag);
				style.setSpan(cls, arr[i][0], arr[i][1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		mView.setSelectArr(arr);
		return style;
	}
	*/
	/** 
	* 实现文本复制功能 
	* @param content 
	*/  
	public static void copy(String content, Context context){
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setPrimaryClip(ClipData.newPlainText(null, content));
	}  
	/** 
	* 实现粘贴功能 
	* @param context 
	* @return 
	*/  
	public static String paste(Context context){
		// 得到剪贴板管理器  
		ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cmb.getPrimaryClip().getItemAt(0).coerceToText(context).toString();
	} 
	/** 
	 * 调用系统界面，给指定的号码发送短信，并附带短信内容 
	 *  
	 * @param context 
	 * @param body
	 */  
	public static void sendSmsWithDefault(Context context, String body) {
	    Intent intent = new Intent(Intent.ACTION_SEND);
	    intent.setData(Uri.parse("smsto:"));
	    intent.putExtra("sms_body", body);  
	    intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "好友推荐"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, body); // 文案
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(intent);  
	}  
	

	/**
	 * 得到默认的sd卡路径
	 * @return String 
	 */
	public static String getSDRoot(){
		return android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 得到默认下载的sd卡路径
	 * @return String 
	 */
	public static String getDownloadFolder(){
		return android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"diaoyu_pic";
	}

	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏软键盘
	* @return void    返回类型
	* @throws
	 */
	public static void hideSoftInput(Activity activity){
		if(activity!=null){
			InputMethodManager iManager = ((InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE));
			if(iManager!=null && activity.getCurrentFocus()!=null && activity.getCurrentFocus().getWindowToken()!=null)
				iManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 
	* @Title: hideSoftInput 
	* @Description: 隐藏软键盘
	* @return void    返回类型
	* @throws
	 */
	public static void showSoftInput(Context context,View view){
		if(context!=null){
			InputMethodManager iManager = ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE));
			iManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		}
	}

	public static String getTAG(Class clazz){
		return clazz.getName();
	}
	
	/*
	* check the app is installed
	*/
	public static boolean isAppInstalled(Context context,String packagename){
		PackageInfo packageInfo;
		try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
         }catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
         }
         if(packageInfo ==null){
            //System.out.println("没有安装");
            return false;
         }else{
            //System.out.println("已经安装");
            return true;
        }
	}
	
	public static long getProcessTotalMemery() {
        return Runtime.getRuntime().totalMemory();
    }
	
	//cpu cunt
	public static int getNumCores() {
	    //Private Class to display only CPU devices in the directory listing
	    class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }

	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
	        Log.d(TAG, "CPU Count: " + files.length);
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Print exception
	        Log.d(TAG, "CPU Count: Failed.");
	        e.printStackTrace();
	        //Default to return 1 core
	        return 1;
	    }
	}
	
	//列表强制刷新
	public static void dealFirstRefresh(){
//		long first_load_time = SharePreferenceUtils.getSharePreferencesLongValue(ProjectConst.TIME_FIRST_LOAD);
//		if(first_load_time==0){
//			SharePreferenceUtils.setSharePreferencesValue(ProjectConst.TIME_FIRST_LOAD, System.currentTimeMillis());
//		}else{
//			long time = SharePreferenceUtils.getSharePreferencesLongValue(ProjectConst.CONFIG_list_force_refresh_time_new);
//			DLog.i(TAG, "System.currentTimeMillis()-->"+";first_load_time-->"+first_load_time+";CONFIG_list_force_refresh_time-->"+time);
//			if((System.currentTimeMillis()-first_load_time)>time*1000){
//				SharePreferenceUtils.setSharePreferencesValue(ProjectConst.TIME_FIRST_LOAD_REFRESH, true);
//				SharePreferenceUtils.setSharePreferencesValue(ProjectConst.TIME_FIRST_LOAD, System.currentTimeMillis());
//			}else{
//				SharePreferenceUtils.setSharePreferencesValue(ProjectConst.TIME_FIRST_LOAD_REFRESH, false);
//			}
//		}
	}
//
//	public static void deleteForseJoke(){
//		String[] pathArr = {ProjectConst.PATH_JOKE_HOT,ProjectConst.PATH_JOKE_HOTNEW,ProjectConst.PATH_JOKE_LATEST,ProjectConst.PATH_JOKE_TEXT,ProjectConst.PATH_JOKE_PIC,ProjectConst.PATH_JOKE_GIF};
//		int len = pathArr.length;
//		for(int i=0;i<len;i++){
//			String type = pathArr[i];
//			SharePreferenceUtils.setSharePreferencesValue(type, null);
//			SharePreferenceUtils.setSharePreferencesValue(type+"_page", "");
//			SharePreferenceUtils.setSharePreferencesValue(type+"_selected", 0);
//		}
//	}
}
