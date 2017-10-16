package update.door;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test4 {

    public static void main(String[] args) {
     String str = "600.00";
     if(str.indexOf('.') > 0){
         str = str.replaceAll("0+?$", "");
         System.out.println(str);
         str = str.replaceAll("[.]$", "");
         System.out.println(str);
     }
    }


    public static Object[] getNum(int num,int min,int max) {

        return getNum(num,min,max);
    }

    public static Map<String,Integer> getNum(int num) {
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("min",num-1);
        map.put("max",num+1);
        return map;
    }
}
