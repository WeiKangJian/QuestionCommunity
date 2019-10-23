package net.bewithu.questioncommunity.Service;

import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

//构建前缀树
@Service
public class SensitiveService implements InitializingBean {
    private  static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    private PrefixNode rootNode =new PrefixNode();

    /**
     * 根据关键词文本初始化前缀树
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String text;
            while ((text = bf.readLine()) != null) {
                //删除字符串末尾和开头的空格
                addWord(text.trim());
            }
            bf.close();
        }catch (Exception e){
            logger.error("出现错误",e.getMessage());
        }
    }

    //增加关键词
    public void addWord(String text){
        PrefixNode tempfixNode =rootNode;

        for(int i=0;i<text.length();i++){
            Character c =text.charAt(i);
            //没有的话，新建一个，再下移动
            //有的话直接下移
            if(tempfixNode.getSubNode(c)==null){
                tempfixNode.addSubNode(c,new PrefixNode());
            }
                tempfixNode =tempfixNode.getSubNode(c);
            if(i==text.length()-1){
                tempfixNode.setEnd(true);
            }
        }
    }
    /**
     * 前缀树匹配查询
     */
    public String matchTree(String data){
        String replace ="***";
        int start =0;
        int poistion =0;
        StringBuffer sb =new StringBuffer();
        PrefixNode tempNode =rootNode;
        while(poistion<data.length()){
            char c =data.charAt(poistion);
            //过滤掉中间文字
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    start++;
                    sb.append(c);
                }
                poistion++;
                continue;
            }
            if(tempNode.getSubNode(c)==null){
                sb.append(data.charAt(start));
                start++;
                poistion=start;
                tempNode =rootNode;
            }
            else{
                tempNode=tempNode.getSubNode(c);
                ++poistion;
                if(tempNode.getEnd()){
                    sb.append(replace);
                    start=poistion;
                    tempNode =rootNode;
                }
            }
            if(poistion==data.length()){
                sb.append(data.substring(start));
            }
        }
        return sb.toString();
    }
    /**
     * 前缀树的数据结构
     */
    private class PrefixNode{
        private boolean end =false;
        private  HashMap<Character,PrefixNode> subNode=new HashMap<>();

        public void addSubNode(Character c,PrefixNode node){
            subNode.put(c, node);
        }

        public PrefixNode getSubNode(Character c){
            return subNode.get(c);
        }
        public void setEnd(boolean end){
            this.end =end;
        }
        public boolean getEnd(){
            return end;
        }
    }

    /**
     * 过滤非法文字
     * @param
     */
    public boolean isSymbol(char c){
        int ic =(int)c;
        //既不是数字也不是东亚文字
        return !CharUtils.isAsciiAlphanumeric(c)&& (ic<0x2E80 || ic >0x9FFF);
    }

}
