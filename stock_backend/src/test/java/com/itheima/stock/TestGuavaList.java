package com.itheima.stock;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by itheima
 * @Date 2022/1/14
 * @Description
 */
public class TestGuavaList {


    @Test
    public void testGuava(){
        //模拟股票id集合
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 3001; i++) {
            ids.add(i);
        }
        //要求：将3001长度的集合分为n片，每片的大小100
        List<List<Integer>> partition = Lists.partition(ids, 100);
        for (List<Integer> eachIds : partition) {
            System.out.println(eachIds.size());
            System.out.println(eachIds);
            System.out.println("###############");
        }
//        Lists.partition(ids, 100).forEach(items->{
//
//        });
    }


}
