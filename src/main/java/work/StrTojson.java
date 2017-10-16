package work;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sl on 2017/9/17.
 */
public class StrTojson {


    public static void main(String[] args) {
        String s = "[]";

        File file = new File("d:/test.txt");
        try {
            System.out.println(toJson(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toJson(File file) throws IOException{
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileReader freader = new FileReader(file);
        BufferedReader breader = new BufferedReader(freader);
        String tmp = "";
        String result = "";
        while ((tmp =breader.readLine()) !=null) {
            tmp = strLineToJson(tmp)+"\r\n";
            result += tmp;
        }
        return "{" +result.substring(0,result.length()-3) + "}";
    }

    public static String strLineToJson(String str) {
        String[] origin = str.split(":");
        String result = "";
        if (origin.length < 1) {
            System.out.println("str is null");
        } else if (origin.length == 1) {
            result = "\"" + origin[0] + "\": \"\",";
        } else if (origin.length == 2){

            if (origin[1].contains("[") || origin[1].contains("{")) {
                result = "\"" + origin[0] + "\":" + origin[1] + ",";
            } else {
                result = "\"" + origin[0] + "\":\"" + origin[1] + "\",";
            }
        } else {
            String tmp = "";
            for (int i = 1; i <= origin.length -1 ; i++) {
                tmp = tmp+":"+origin[i];
            }
            if (origin[1].contains("[") || origin[1].contains("{")) {
                result = "\"" + origin[0] + "\":" + tmp + ",";
                if (result.contains("::")) {
                    result = result.replaceAll("::", ":");
                }
            } else {
                result = "\"" + origin[0] + "\":\"" + tmp + "\",";
                if (result.contains("::")) {
                    result = result.replaceAll("::", ":");
                }
            }
        }
        return result;

    }

}

