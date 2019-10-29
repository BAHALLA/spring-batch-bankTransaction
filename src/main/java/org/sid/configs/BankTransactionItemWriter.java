package org.sid.configs;

import org.sid.dao.BankTransaction;
import org.sid.dao.BankTransactionRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {
    @Autowired
    private BankTransactionRepository transactionRepository;
    @Override
    public void write(List<? extends BankTransaction> list) throws Exception {
        transactionRepository.saveAll(list);
    }
}
