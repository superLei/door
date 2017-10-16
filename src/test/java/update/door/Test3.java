package update.door;

public class Test3 {

    public static void main(String[] args) {
        String[] str1 = {"abc","ddd","eee","afd"};
        String[] str2 = {"abc","ddd","e2e","afd","2232","23s","33d"};
        int tmp = 0;
        int tmp_big = 0;
        if (str1.length > str2.length) {
            tmp = str2.length;
            tmp_big = str1.length;
        }else {
            tmp = str1.length;
            tmp_big = str2.length;
        }
        for (int i = 0; i <tmp ; i++) {
            for (int j = 0; j <tmp_big ; j++) {
                if (str1[i] == str2[j]) {
                    System.out.println(i);
                }
            }
        }


    }

    public static void findNum(int length) {
        for (int i = 0; i <length ; i++) {

        }
    }
}
