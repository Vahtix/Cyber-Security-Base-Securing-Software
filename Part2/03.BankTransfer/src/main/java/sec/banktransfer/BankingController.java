package sec.banktransfer;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BankingController {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        // test data to the application
        Account account = new Account();
        account.setIban("0000");
        account.setBalance(1000);
        accountRepository.save(account);

        Account account2 = new Account();
        account2.setIban("0001");
        account2.setBalance(500);
        accountRepository.save(account2);

        Account account3 = new Account();
        account3.setIban("0002");
        account3.setBalance(50);
        accountRepository.save(account3);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("accounts", accountRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @Transactional
    public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam Integer amount) {
        Account accountFrom = accountRepository.findByIban(from);
        Account accountTo = accountRepository.findByIban(to);

        
        if(accountFrom.equals(accountTo) || amount <= 0){
        	return "redirect:/";
        }
        
        if(accountFrom.getBalance() - amount < 0 ){
        	return "redirect:/";
        }else if(accountTo.getBalance() + amount < 0){
        	return "redirect:/";
        }else{
            accountFrom.setBalance(accountFrom.getBalance() - amount);
            accountTo.setBalance(accountTo.getBalance() + amount);

            accountRepository.save(accountFrom);
            accountRepository.save(accountTo);

            return "redirect:/";
        }
        

    }
}
