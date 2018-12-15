package filterChain;

/**
 * Created by zhangshukang on 2018/12/7.
 */
public interface MyFilter {

    String getName();
    void execute(FilterChain filterChain);

}
