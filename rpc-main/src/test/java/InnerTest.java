/**
 * Created by liqiushi on 2017/12/25.
 */
interface Test {
    void test();
}

public class InnerTest {
    public static void main(String[] args) {
        new Test() {
            public void test() {
                System.out.println("test");
            }
        }.test();
    }
}
