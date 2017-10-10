package com.zige.robot.interf;

/**
 * 听写回调
 * 
 * @author ydshu
 * 
 */
public interface IRecognizeListener {

	/**
	 * 听写完毕回调
	 * 
	 * @param text
	 *            识别结果
	 */
	public void onRecognizeOver(String text);

}
