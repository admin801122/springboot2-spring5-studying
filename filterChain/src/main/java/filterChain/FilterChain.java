package filterChain;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangshukang.
 */

@Data
public class FilterChain {

    private MyFilter currentFilter;
    private FilterChain next;
    private List<MyFilter> filters;


    /**
     *
     * 模拟 SpringBoot Jetty 中的 fiterChain 责任链实现机制
     */
    public FilterChain(List<MyFilter> filters) {
        if (filters.size() > 0) {
            this.currentFilter = filters.get(0);
            filters.remove(0);
            this.next = new FilterChain(filters);
        }
    }

    public void doFilter(FilterChain filterChain) {
        MyFilter currentFilter = filterChain.getCurrentFilter();
        if (null != currentFilter) {
            currentFilter.execute(filterChain.next);
        }
    }
}


