package com.ye.balance.balanceproject.model.service;

import com.ye.balance.balanceproject.model.domain.entity.Balance;
import com.ye.balance.balanceproject.model.domain.entity.BalanceItem;
import com.ye.balance.balanceproject.model.domain.form.BalanceEditForm;
import com.ye.balance.balanceproject.model.domain.vo.BalanceReportVo;
import com.ye.balance.balanceproject.model.repo.BalanceItemRepo;
import com.ye.balance.balanceproject.model.repo.BalanceRepo;
import com.ye.balance.balanceproject.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepo balanceRepo;

    @Autowired
    private BalanceItemRepo balanceItemRepo;

    @Autowired
    private UserRepo userRepo;

    @PreAuthorize("authenticated()")
    public Page<BalanceItem> searchItems(Balance.Type type,
                                         LocalDate dateFrom,
                                         LocalDate dateTo,
                                         String keyword,
                                         Optional<Integer> page,
                                         Optional<Integer> size) {

        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));

        var username = SecurityContextHolder.getContext().getAuthentication().getName();


        //login user specification
        Specification<BalanceItem> spec = (root, query, builder) ->
                builder.equal(root.get("balance").get("user").get("name"), username);

        //type specification
        spec = spec.and(((root, query, builder) ->
                builder.equal(root.get("balance").get("type"), type)));

        //date from specification
        if (null != dateFrom) {
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("balance").get("date"), dateFrom));
        }

        // date to specification
        if (null != dateTo) {
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("balance").get("date"), dateTo));
        }
        // keyword specification  ( ** join category and item )
        if (StringUtils.hasLength(keyword)) {
            Specification<BalanceItem> category = (root, query, builder) ->
                    builder.like(builder.lower(root.get("balance").get("category")),
                            "%%%s%%".formatted(keyword.toLowerCase()));
            Specification<BalanceItem> item = (root, query, builder) ->
                    builder.like(builder.lower(root.get("item")), "%%%s%%".formatted(keyword.toLowerCase()));

            spec = spec.and(item.or(category));
        }

        return balanceItemRepo.findAll(spec, pageInfo);
    }

    public BalanceEditForm findById(Integer id) {

        return balanceRepo.findById(id).map(BalanceEditForm::new).orElseThrow();

    }

    @Transactional
    public int save(BalanceEditForm form) {

        var balance = form.getHeader().getId() == 0 ? new Balance()
                : balanceRepo.findById(form.getHeader().getId()).orElseThrow();

        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepo.findOneByLoginId(username).orElseThrow();

        balance.setUser(user);
        balance.setCategory(form.getHeader().getCategory());
        balance.setDate(form.getHeader().getDate());
        balance.setType(form.getHeader().getType());

        balance = balanceRepo.save(balance);

        for (var formItem : form.getItems()) {
            var item = formItem.getId() == 0 ? new BalanceItem()
                    : balanceItemRepo.findById(formItem.getId()).orElseThrow();

            if (formItem.getDeleted() != null && formItem.getDeleted()) {
                balanceItemRepo.delete(item);
                continue;
            }

            item.setItem(formItem.getItem());
            item.setQuantity(formItem.getQuantity());
            item.setUnitPrice(formItem.getUniPrice());
            item.setBalance(balance);

            balanceItemRepo.save(item);
        }

        return balance.getId();
    }

    @Transactional
    public void deleteById(int id) {
        userRepo.deleteById(id);
    }

    @PreAuthorize("authenticated()")
    public Page<BalanceReportVo> searchReports(Balance.Type type, LocalDate dateFrom, LocalDate dateTo, Optional<Integer> page, Optional<Integer> size) {

        Specification<Balance> spec = (root, query, builder) -> builder.equal(root.get("user").get("loginId"),
                SecurityContextHolder.getContext().getAuthentication().getName());

        if (null != type) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type));
        }

        if (null != dateFrom) {
            spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("date"), dateFrom));
        }

        if (null != dateTo) {
            spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("date"), dateTo));
        }

        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(10));

        var result = balanceRepo.findAll(spec, pageInfo).map(BalanceReportVo::new);

        //lost profit calculation
        if (!result.getContent().isEmpty()) {
            var firstId = result.getContent().get(0).getId();

            var lastIncomes = balanceItemRepo.getLastBalance ( firstId   , Balance.Type.Income )
                    .map(a -> a.intValue()).orElse(0);

            var lastExpenses = balanceItemRepo.getLastBalance ( firstId   , Balance.Type.Expense )
                    .map(a -> a.intValue()).orElse(0);

           var lastBalance = lastIncomes - lastExpenses;

            for (var vo : result.getContent()) {
                if (vo.getType() == Balance.Type.Income) {
                    lastBalance += vo.getAmount();
                } else {
                    lastBalance -= vo.getAmount();
                }
                vo.setBalance(lastBalance);
            }

        }
        return result;
    }
}
