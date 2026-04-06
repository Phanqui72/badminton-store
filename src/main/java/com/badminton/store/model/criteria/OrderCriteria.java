package com.badminton.store.model.criteria;

import com.badminton.store.constant.MgrConstant;
import com.badminton.store.utils.DateUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class OrderCriteria {
    private Long userId;
    private Integer status;
    private String fromDate; // Chuỗi dd/MM/yyyy
    private String toDate;

    public Specification<Order> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) predicates.add(cb.equal(root.get("customer").get("id"), userId));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));

            // Xử lý ngày tháng bằng DateUtils
            if (StringUtils.isNotBlank(fromDate)) {
                Date start = DateUtils.converDate(fromDate, MgrConstant.DATE_FORMAT);
                if (start != null) predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), DateUtils.startOfDay(start)));
            }
            if (StringUtils.isNotBlank(toDate)) {
                Date end = DateUtils.converDate(toDate, MgrConstant.DATE_FORMAT);
                if (end != null) predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), DateUtils.endOfDay(end)));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}