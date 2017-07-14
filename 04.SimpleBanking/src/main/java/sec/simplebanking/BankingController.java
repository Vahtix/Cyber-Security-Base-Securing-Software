package sec.simplebanking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BankingController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    
    private Client client;
    private Account account;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String add(@RequestParam String name, @RequestParam String iban) {
    	// DO SOMETHING HERE
        if (name.trim().isEmpty() || iban.trim().isEmpty()) {
            return "redirect:/";
        }

        client = clientRepository.findByName(name);
        account = accountRepository.findByIban(iban);
 
        if (client == null) {
            client = new Client();
            client.setName(name);
        }
        
        if (account == null) {
            account = new Account();
            account.setIban(iban);
            account.setBalance(100);
            account.setClient(client);
        }

        if(!client.getAccounts().contains(account)){
        	client.addAccount(account);
        }
        
        clientRepository.save(client);
        accountRepository.save(account);

        return "redirect:/";
    }
}
