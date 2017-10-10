package com.zige.robot.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 在ArrayList<String>里搜索特定串
     *
     * @param list 列表
     * @return true: 找到
     * false: 未找到
     */
    public static boolean searchList(ArrayList list, String subStr) {
        if (list == null || list.size() < 1 || subStr == null) {
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            if (subStr.equals(list.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断子串是否包含在数组中
     *
     * @param subString 待查找串
     * @param array     字符串数组
     * @return true: 找到
     * false: 未找到
     */
    public static boolean searchArray(String subString, String[] array) {
        if (subString == null || array == null)
            return false;
        for (int i = 0; i < array.length; i++) {
            if (subString.equals(array[i]))
                return true;
        }
        return false;
    }

    /**
     * @param @param  subString
     * @param @param  array
     * @param @return 设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: searchArray
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    public static boolean searchArray(String subString, String[][] array) {
        if (subString == null || array == null)
            return false;
//		Logs.d("+++ array length: " + array.length);
        for (int i = 0; i < array.length; i++) {
//			Logs.d("+++ " + subString + " vs " + array[i][0]);
            if (subString.equals(array[i][0]))
                return true;
        }
        return false;
    }

    /**
     * @param @param  sentence
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: trimSentenceMark
     * @Description: TODO去除一个句子里的最后一个标点，以及句子最后的"的"
     */
    public String trimSentenceMark(String sentence) {
        String newSentence = "";
        int length = sentence.length();        //字符串长度

        String str = sentence.substring(length - 1, length);
        System.out.println("str = " + str);
        String str1 = sentence.substring(length - 2, length - 1);
        System.out.println("str1 = " + str1);
        if (str.equals("。") || str.equals("，") || str.equals("！") || str.equals("？")) {        //如果句尾有标点
            if (str1.equals("的")) {        //最后一个字是的
                newSentence = sentence.substring(0, length - 2);
                System.out.println("newSentence = " + newSentence);
                return newSentence;
            } else {
                newSentence = sentence.substring(0, length - 1);
                System.out.println("newSentence = " + newSentence);
                return newSentence;
            }

        }

        return null;
    }

    /**
     * @param @param  sentence
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: removeSentenceMark
     * @Description: 删除句子后面语义不强部分
     */
    public static String removeSentenceMark(String sentence) {
        String str = "";
        if (!TextUtils.isEmpty(sentence)) {
            Pattern pattern = Pattern.compile("[,.!?，。！？啊哦阿地嗯啦哇吗吧呢呀]*$");
            Matcher matcher = pattern.matcher(sentence); // 替换全部符合正则的数据
            str = matcher.replaceAll("");
            str = str.trim();
        }
        return str;
    }


    /**
     * 删除句子后面的表达语义关联不大的词删除
     *
     * @param sentence
     * @return
     */
    public static String removeMeaninglessWord(String sentence) {
        String res = removeSentenceMark(sentence);
        if (res != null && res.length() > 0) {
            String[] finds = new String[]{"怎么样", "好不好", "怎么办", "行不行", "好不",
                    "可以", "行不", "不行", "不"};
            for (String find : finds) {
                if (res.endsWith(find) && (res.length() > find.length()))
                    res = res.substring(0, res.length() - find.length());
            }
            // System.out.println("hello world:"+(res.length() >= 3));
            if (res.indexOf("那") == 0 && res.length() >= 3)
                res = res.substring(1);
        }
        // System.out.println("StringUtils.removeMeaninglessWord():"+ res);
        return res;
    }


    public static String filterSpecChar(String str) {
        // 只允许字母和数字
        // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;\",\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    // 处理叠词
    public static String processWord(String str) {
        str = filterSpecChar(str);
        if (str != null && str.length() > 0) {
            String oneChar = str.substring(0, 1);
            int index = str.indexOf(oneChar, 1);
            // System.out.println("xiaoluo test str:"+str+"---index:"+index);
            if (index != -1) {
                String red = str.substring(0, index);
                String res = str.replaceAll(red, "");
                if ("".equals(res)) {
                    return red;
                }
            }
        }
        return str;
    }


    public StringUtils() {
    }

    ;

//	public static void main(String[] args){
//		StringUtils su = new StringUtils();
//		String str = "好佛挡杀佛的！";
//		String str1 = su.trimSentenceMark(str);
//		System.out.println("test str1 = "+str1);
//	}

}
