package org.sid.configs;

import org.sid.dao.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

//@Component
public class BankTransactionItemProcessorAnalytics implements ItemProcessor<BankTransaction, BankTransaction> {

    private double totalDebit;
    private double totalCredit;
    @Override
    public BankTransaction process(BankTransaction bankTransaction) throws Exception {

        if(bankTransaction.getTransactionType().equals("C")) totalCredit+= bankTransaction.getAmount();
        else if(bankTransaction.getTransactionType().equals("D")) totalDebit+= bankTransaction.getAmount();

        return bankTransaction;
    }

    public double getTotalDebit() {
        return totalDebit;
    }

    public double getTotalCredit() {
        return totalCredit;
    }
}
