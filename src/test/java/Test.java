import java.util.Random;

/**
 * @author jarvis
 * @date 2021/3/31 0031 21:39
 */
public class Test {


    public static void main(String[] args) {
        //创建一个长度为6的 int 数组
        int[] intArray1 = new int[6];

        String[] split = null;
        String str = "";

        while (true) {
            //创建 1-30的随机数
            int round = (int) (Math.random() * 30);
            //利用一个字符串判断随机得到的数据是否重复
            if (!str.contains(String.valueOf(round))) {
                //随机出来的数据不重复
                str = str + round + ",";
            }
            split = str.split(",");
            if (split.length == intArray1.length) {
                //有6个了
                break;
            }

        }

        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }
}
