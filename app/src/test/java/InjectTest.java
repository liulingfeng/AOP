/**
 * @author liuxiaoshuai
 * @date 2019-07-05
 * @desc 1.APT
 *       2.ASM
 *       3.反射中会使用注解
 * @email liulingfeng@mistong.com
 */
public class InjectTest {
    @InjectTime
    public static void test(int i, int j) {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        System.out.println("execute"+(endTime-startTime)+".ms");
    }

    public static void main(String[] args) {
        test(1, 1);
    }
}
