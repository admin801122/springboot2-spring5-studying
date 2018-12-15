package filterChain;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zhangshukang.
 */
public class FilterChainBuilder2 {

    public static FilterChain buildFilterChain(List<MyFilter> filters) {

        if (CollectionUtils.isEmpty(filters)) {
            return null;
        }

        MyFilter currentFilter = filters.get(0);
        FilterChain filterChain2 = new FilterChain(currentFilter);
        filters.remove(0);

        if (filters.size() > 0) {
            filterChain2.setNext(buildFilterChain(filters));
        }
        return filterChain2;
    }
}
