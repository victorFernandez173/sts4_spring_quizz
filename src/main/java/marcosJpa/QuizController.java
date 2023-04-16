package marcosJpa;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import jakarta.servlet.http.HttpSession;

@Controller
public class QuizController {
	
	@Autowired
	private IPlayers repositorio;

	@GetMapping("/createUser")
	public String process() {
		return "createUser";
	}
	
	@PostMapping("/createUser")
	public String createUser(@RequestParam("nick") String nick, HttpSession session) {
		boolean insertado = false;
		session.setAttribute("INSERTADO", insertado);
		String nickname = nick;
		session.setAttribute("NICKNAME", nickname);
		return "question1";
	}

	@PostMapping("/question1")
	public String question1(@RequestParam("q1") String q1, HttpSession session) {
		List<String> answers = new ArrayList<>();
		answers.add(q1);
		session.setAttribute("SESS_ANSWERS", answers);
		return "question2";
	}

	@PostMapping("/question2")
	public String question2(@RequestParam("q2") String q2, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(StringUtils.capitalize(q2.toLowerCase()));
		session.setAttribute("SESS_ANSWERS", answers);
		return "question3";
	}

	@PostMapping("/question3")
	public String question3(@RequestParam("q3") String q3, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(q3);
		session.setAttribute("SESS_ANSWERS", answers);
		return "question4";
	}

	@PostMapping("/question4")
	public String question4(@RequestParam("q4") String q4, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(q4);
		session.setAttribute("SESS_ANSWERS", answers);
		return "question5";
	}

	@PostMapping("/question5")
	public String question5(@RequestParam("q5") String q5, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(q5);
		session.setAttribute("SESS_ANSWERS", answers);
		return "question6";
	}

	@PostMapping("/question6")
	public String question6(@RequestParam("q6") String q6, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(StringUtils.capitalize(q6.toLowerCase()));
		session.setAttribute("SESS_ANSWERS", answers);
		return "question7";
	}

	@PostMapping("/question7")
	public String question7(@RequestParam("q7") String q7, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		answers.add(q7);
		session.setAttribute("SESS_ANSWERS", answers);
		return "redirect:/final";
	}

	@GetMapping("/final")
	public String resultados(Model model, HttpSession session) {
		@SuppressWarnings("unchecked")
		List<String> answers = (List<String>) session.getAttribute("SESS_ANSWERS");
		model.addAttribute("SESS_ANSWERS", answers);
		model.addAttribute("SOLUTIONS", getSolutions());
		model.addAttribute("SCORE", getScore(answers, getSolutions()));
		model.addAttribute("CLASSIFICATION", genClassification((String) model.getAttribute("SCORE")));
//		añado un nuevo registro a la bbdd
		boolean insertado = (boolean)session.getAttribute("INSERTADO");
		if(!insertado) {
			
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
			String fecha = now.format(dtf);
			String TFecha = fecha.replace(' ', 'T');
			LocalDateTime parsedDate = LocalDateTime.parse(TFecha);
			
			repositorio.save(new Player((String)session.getAttribute("NICKNAME"), (String)model.getAttribute("SCORE"), parsedDate));
			insertado = true;
			session.setAttribute("INSERTADO", insertado);
		}
		
//		obtengo todos los registros para mostrarlos
		List<Player> allPlayers = (ArrayList<Player>) repositorio.findAll();
		model.addAttribute("ALLPLAYERS", allPlayers);
		
		return "final";
	}

//	devuelve arraylist con las soluciones
	public List<String> getSolutions() {
		List<String> sols = new ArrayList<>(
				Arrays.asList("Herodoto", "Dionisio", "Macedonia", "Homero", "Mirón", "Socrates", "Siglo VIII a.C."));
		return sols;
	}

//	devuelve String con la puntuacion
	public String getScore(List<String> answers, List<String> solutions) {
		double ok = 0;
		for (int i = 0; i < 7; i++) {
			if (answers.get(i).equals(solutions.get(i)))
				ok += 1;
		}
		return String.valueOf((int)((ok / 7) * 100));
	}

//	en base al parametro clasificación, devuelve una letra
	public String genClassification(String clasificacion) {
		int c = Integer.parseInt(clasificacion);
		String clase;
		if (c < 16)
			clase = "C";
		else if (c < 44)
			clase = "B";
		else if (c < 86)
			clase = "A";
		else
			clase = "S";
		return clase;
	}

	@PostMapping("/final")
	public String destroySession(HttpSession session) {
//		session.invalidate();
		return "redirect:/";
	}
	
	
	@PostMapping("/filtrar")
	public String filtrado(HttpSession session, @RequestParam(defaultValue = "-1") String score, Model model) {
		
		List<Player> allPlayers = (ArrayList<Player>) repositorio.findAll();
		model.addAttribute("ALLPLAYERS", allPlayers);
		
		
		List<Player> filtrados = (ArrayList<Player>) repositorio.findByScore(score);
		model.addAttribute("FILTRADOS", filtrados);
		
		return "filtrar";
	}
}
