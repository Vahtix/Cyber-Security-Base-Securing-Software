package sec.simplebanking;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Account extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1L;
	private String iban;
    private Integer balance;
    
    @ManyToOne
    private Client client;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
    
    public Client getClient(){
    	return client;
    }
    
    public void setClient(Client client){
    	this.client = client;
    }

}
