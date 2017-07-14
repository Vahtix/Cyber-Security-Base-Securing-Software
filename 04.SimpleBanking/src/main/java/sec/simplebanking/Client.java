package sec.simplebanking;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Client extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
	
	@OneToMany(mappedBy = "client")
	private List<Account> accounts = new ArrayList<>();

    // DO SOMETHING HERE
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<Account> getAccounts(){
    	return accounts;
    } 
    
    public void addAccount(Account account){
    	this.accounts.add(account);
    }

}
