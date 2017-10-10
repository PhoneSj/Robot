package com.zige.robot.interf;

/**
 * 语义理解回调
 * 
 * @author ydshu
 * 
 */
public interface ITextUnderStandListener {

	/**
	 * 语义理解完毕回调
	 * 
	 * @param text
	 *
	 */
	public void onUnderStandOver(String text);

}
