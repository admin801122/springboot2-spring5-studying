package filterChain;

/**
 * Created by zhangshukang.
 */
public class MyFilters{


    /**
     * 定义三个Myfilter
     *
     */
    public static class MyFilter1 implements  MyFilter{
        @Override
        public String getName() {
            return "myFilter1";
        }

        @Override
        public void execute(FilterChain filterChain) {
            System.out.println(getName()+"before...");
            if (null != filterChain) {
                filterChain.doFilter(filterChain);
            }
            System.out.println(getName()+"after...");
        }
    }


    public static class MyFilter2 implements  MyFilter{
        @Override
        public String getName() {
            return "myFilter2";
        }

        @Override
        public void execute(FilterChain filterChain) {
            System.out.println(getName()+"before...");
            if (null != filterChain) {
                filterChain.doFilter(filterChain);
            }
            System.out.println(getName()+"after...");
        }
    }

    public static class MyFilter3 implements  MyFilter{
        @Override
        public String getName() {
            return "myFilter3";
        }

        @Override
        public void execute(FilterChain filterChain) {
            System.out.println(getName()+"before...");
            if (null != filterChain) {
                filterChain.doFilter(filterChain);
            }
            System.out.println(getName()+"after...");
        }
    }


}
