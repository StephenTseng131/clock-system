package com.example.clocksystem.utils;

import java.util.ArrayList;
import java.util.List;

public class SearchJudge {
    //搜索判断
    public static boolean searchJudge(String searchContent, String judgeContent){
        var keywords=searchContent.split("[\\s]");
        var keywordList= new ArrayList<ArrayList<String>>();
        //将关键词分开
        for(var keyword:keywords){
            var arrayList=new ArrayList<String>(List.of(keyword.split("[+]")));
            keywordList.add(arrayList);
        }
        for(var array:keywordList){
            //用于判断是否符合条件
            boolean flag=true;
            for(var keyword:array){
                if(!judgeContent.contains(keyword)){
                    flag=false;
                }
            }
            if(flag){
                return true;
            }
        }
        return false;
    }
}
