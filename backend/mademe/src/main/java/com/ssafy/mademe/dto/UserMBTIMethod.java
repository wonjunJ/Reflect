package com.ssafy.mademe.dto;

import com.ssafy.mademe.entity.MBTICode;
import org.springframework.context.annotation.Bean;

import java.util.*;

public class UserMBTIMethod {
    public String getMBTIs(Map<MBTICode, Long> map){
        StringBuilder sb = new StringBuilder(); //가능한 MBTI코드들이 다 들어가있다.
        if(map.isEmpty()){
            return "";
        }
        for (Map.Entry<MBTICode, Long> entrySet : map.entrySet()) {
            System.out.println(entrySet.getKey() + " : " + entrySet.getValue());
        }
        //I, E
        if(map.containsKey(MBTICode.I) && map.containsKey(MBTICode.E)){
            if(map.get(MBTICode.I) < map.get(MBTICode.E)){
                sb.append("E");
            } else if (map.get(MBTICode.I) > map.get(MBTICode.E)) {
                sb.append("I");
            } else {
                sb.append("I/E");
                //sb.append("E");
            }
        } else if (map.containsKey(MBTICode.E)) {
            sb.append("E");
        } else {
            sb.append("I");
        }
        //N, S
        if(map.containsKey(MBTICode.N) && map.containsKey(MBTICode.S)){
            if(map.get(MBTICode.N) < map.get(MBTICode.S)){
                sb.append("S");
            } else if (map.get(MBTICode.N) > map.get(MBTICode.S)) {
                sb.append("N");
            } else {
                sb.append("N/S");
                //sb.append("S");
            }
        } else if (map.containsKey(MBTICode.S)) {
            sb.append("S");
        } else if (map.containsKey(MBTICode.N)) {
            sb.append("N");
        }
        //F, T
        if(map.containsKey(MBTICode.F) && map.containsKey(MBTICode.T)){
            if(map.get(MBTICode.F) < map.get(MBTICode.T)){
                sb.append("T");
            } else if (map.get(MBTICode.F) > map.get(MBTICode.T)) {
                sb.append("F");
            } else {
                sb.append("F/T");
                //sb.append("T");
            }
        } else if (map.containsKey(MBTICode.T)) {
            sb.append("T");
        } else if (map.containsKey(MBTICode.F)) {
            sb.append("F");
        }
        //J, P
        if(map.containsKey(MBTICode.J) && map.containsKey(MBTICode.P)){
            if(map.get(MBTICode.J) < map.get(MBTICode.P)){
                sb.append("P");
            } else if (map.get(MBTICode.J) > map.get(MBTICode.P)) {
                sb.append("J");
            } else {
                sb.append("J/P");
                //sb.append("P");
            }
        } else if (map.containsKey(MBTICode.P)) {
            sb.append("P");
        } else if (map.containsKey(MBTICode.J)) {
            sb.append("J");
        }
        System.out.println(sb.toString());
        return sb.toString();

//        Set<String> mbtiList = new HashSet<>();
//        for(int a=0; a<2; a++){
//            StringBuilder tmp = new StringBuilder();
//            String ie = "IE";
//            if(sb.toString().contains("IE")){
//                tmp.append(ie.charAt(a));
//            }else{
//                tmp.append(sb.charAt(0));
//            }
//            for (int b=0; b<2; b++){
//                String ns = "NS";
//                if(sb.toString().contains("NS")){
//                    if(tmp.length() == 1){
//                        tmp.append(ns.charAt(b));
//                    }else{
//                        tmp.replace(1, 2, String.valueOf(ns.charAt(b)));
//                    }
//                }else {
//                    if(sb.toString().contains("N")){
//                        if(tmp.length() == 1){
//                            tmp.append("N");
//                        }
//                    }else{
//                        if(tmp.length() == 1){
//                            tmp.append("S");
//                        }
//                    }
//                }
//                for(int c=0; c<2; c++){
//                    String ft = "FT";
//                    if(sb.toString().contains("FT")){
//                        if(tmp.length() == 2){
//                            tmp.append(ft.charAt(c));
//                        }else{
//                            tmp.replace(2, 3, String.valueOf(ft.charAt(c)));
//                        }
//                    }else {
//                        if(sb.toString().contains("F")){
//                            if(tmp.length() == 2){
//                                tmp.append("F");
//                            }
//                        }else{
//                            if(tmp.length() == 2){
//                                tmp.append("T");
//                            }
//                        }
//                    }
//                    for(int d=0; d<2; d++){
//                        String jp = "JP";
//                        if(sb.toString().contains("JP")){
//                            if(tmp.length() == 3){
//                                tmp.append(jp.charAt(d));
//                            }else{
//                                tmp.replace(3, 4, String.valueOf(jp.charAt(d)));
//                            }
//                        }else {
//                            if(sb.toString().contains("J")){
//                                if(tmp.length() == 3){
//                                    tmp.append("J");
//                                }
//                            }else{
//                                if(tmp.length() == 3){
//                                    tmp.append("P");
//                                }
//                            }
//                        }
//                        mbtiList.add(tmp.toString());
//                    }
//                }
//            }
//        }
        //return new ArrayList<>(mbtiList);
    }
}
