
package com.jdon.sample.test.bankaccount.b;

import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.AccountParameterVO;
import com.jdon.sample.test.bankaccount.a.BankAccount;


public interface AbEventToCommandIF {
	@Send("withdrawCommand")
	DomainMessage transfer(@Receiver BankAccount bModel, AccountParameterVO parameterVO);

}