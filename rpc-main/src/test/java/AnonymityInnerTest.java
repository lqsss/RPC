/**
 * Created by liqiushi on 2017/12/25.
 */

public class AnonymityInnerTest {
    private static class A {
        private Integer a;

        public A(Integer a) {
            this.a = a;
        }

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }
    }

    private interface B{
        public void set();
    }

    public static void main(String[] agrs){
        final A a = new A(5);
        B test = new B() {
          
            public void set() {
                a.setA(12);
            }
        };

        test.set();

        System.out.println(a.getA());
    }

}
