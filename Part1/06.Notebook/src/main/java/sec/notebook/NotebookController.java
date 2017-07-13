package sec.notebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotebookController {

	private List<String> notes;
	private List<String> recentNotes;
	
	public NotebookController(){
		this.notes = new ArrayList<>();
		this.recentNotes = new ArrayList<>();
	}
	
	@RequestMapping(value = "/")
    public String addNote(Model model, @RequestParam(required = false) String note) {
        if(note != null && !note.trim().isEmpty()) {
            this.notes.add(note);
        }
        
		if(this.notes.size() > 10){
			
			recentNotes = notes.subList(this.notes.size() - 10, this.notes.size());
			
		}
		else{
			recentNotes = notes;
		}
		
		model.addAttribute("recentNotes", recentNotes);
		return "index";

    }
	
}
